package com.thundersharp.admin.ui.carousel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.GallaryAdapter;
import com.thundersharp.admin.core.Adapters.SliderAdapter;
import com.thundersharp.admin.core.Model.SliderModel;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.edits.EditItemActivity;
import com.thundersharp.admin.ui.gallary.FirebaseGallary;
import com.thundersharp.admin.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarouselActivity extends AppCompatActivity {

    ImageView imagehome;
    MaterialCardView imageGallary, dinner;
    TextInputLayout url;
    AppCompatButton upload;
    RecyclerView rv_slider;
    List<SliderModel> modelList;
    SliderAdapter adapter;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        initViews();

        loadImages();

        upload.setOnClickListener(view ->{
            if (url.getEditText().getText().toString().isEmpty()){
                url.getEditText().setError("Required!");
                url.getEditText().requestFocus();
            }else {

                id = getId();
                uploadTostorage(url.getEditText().getText().toString(),id);
            }
        });


        dinner.setOnClickListener(view->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 10089);
        });
    }
    private int getId() {
        return (int)(Math.random()*9000)+1000;
    }


    private void uploadTostorage(String url, int id) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("SliderImages/"+String.valueOf(id)+".jpg");
        storageReference.putFile(Uri.parse(url)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    String path = task.getResult()
                            .getMetadata()
                            .getPath();
                    task.getResult()
                            .getStorage()
                            .getDownloadUrl()
                            .addOnSuccessListener(uri ->{
                                updateDatabase(uri,path, id);
                            });
                }else {
                    Toast.makeText(CarouselActivity.this, "Storage Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDatabase(Uri uri, String path, int id) {
        HashMap<String,Object> data = new HashMap<>();
        data.put("PAGE",id);
        data.put("URL",uri.toString());

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                .child(String.valueOf(id))
                .updateChildren(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        modelList.add(new SliderModel(Integer.parseInt(data.get("PAGE").toString()),data.get("URL").toString()));
                        adapter.notifyItemInserted(modelList.size() - 1);

                        adapter = new SliderAdapter(this,modelList);
                        Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        url.getEditText().setText("");
                        // imageurl.getEditText().setText(uri.toString());
                        // Glide.with(EditItemActivity.this).load(uri.toString()).into(homeImage);

                    }else {
                        FirebaseDatabase.getInstance().getReference(path).removeValue();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1356){
            if (data != null) {
                url.getEditText().setText(data.getParcelableExtra("data").toString());
                Glide.with(CarouselActivity.this).load(url.getEditText().getText().toString()).into(imagehome);

            }else Toast.makeText(CarouselActivity.this,"Error in receiving image data",Toast.LENGTH_LONG).show();
        }else if (requestCode ==10089 && resultCode ==RESULT_OK){
            Uri pickedImage = data.getData();
            url.getEditText().setText(pickedImage.toString());
            Glide.with(CarouselActivity.this).load(pickedImage.toString()).into(imagehome);
            //upLoadImageToStorage(pickedImage);
        }
    }

    private void loadImages() {
        modelList = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                               modelList.add(dataSnapshot.getValue(SliderModel.class));
                            }
                            rv_slider.setLayoutManager(new GridLayoutManager(CarouselActivity.this,2));
                            adapter = new SliderAdapter(CarouselActivity.this, modelList);
                            rv_slider.setAdapter(adapter);

                        }else Toast.makeText(CarouselActivity.this, "No images found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CarouselActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void initViews() {
        imagehome = findViewById(R.id.imagehome);
        imageGallary = findViewById(R.id.imageGallary);
        dinner = findViewById(R.id.dinner);
        url = findViewById(R.id.url);
        upload = findViewById(R.id.upload);
        rv_slider = findViewById(R.id.rv_slider);
    }
}