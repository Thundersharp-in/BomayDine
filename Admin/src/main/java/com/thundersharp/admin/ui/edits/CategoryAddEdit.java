package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.net.URI;
import java.util.HashMap;
import java.util.Random;

public class CategoryAddEdit extends AppCompatActivity {

    CategoryData categoryData;
    ImageView imagehome,edit;
    AppCompatButton categoryUpdate;
    String uploadedurl;
    TextInputLayout categoryName,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_edit);
        if (getIntent().getSerializableExtra("data") !=null){
            categoryData = (CategoryData)getIntent().getSerializableExtra("data");

        }

        imagehome = findViewById(R.id.imagehome);
        categoryName = findViewById(R.id.cn);
        url = findViewById(R.id.url);
        categoryUpdate = findViewById(R.id.updatec);
        edit = findViewById(R.id.edit);

        if (getIntent().getSerializableExtra("data") !=null) {
            Glide.with(this).load(categoryData.IMAGES).into(imagehome);
            categoryName.getEditText().setText(categoryData.NAME);
            url.getEditText().setText(categoryData.IMAGES);
        }

        edit.setOnClickListener(n->{

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 10087);

        });

        categoryUpdate.setOnClickListener(v->{

            if (categoryName.getEditText().getText().toString().isEmpty()){

                categoryName.getEditText().setError("Name Cannot be empty !!");
                categoryName.getEditText().requestFocus();

            }else if (url.getEditText().getText().toString().isEmpty() && uploadedurl == null){
                url.getEditText().setError("Url Cannot be empty ! Either enter url or select a image from your device.");
                url.getEditText().requestFocus();
            }else {
                if (categoryData == null){
                    Random random = new Random();
                    categoryData = new CategoryData(categoryName.getEditText().getText().toString(),String.valueOf(random.nextInt(9999999)),url.getEditText().getText().toString());
                }

                CategoryData categoryData1 = new CategoryData(categoryName.getEditText().getText().toString(),categoryData.ID,url.getEditText().getText().toString());

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_CATEGORY)
                        .child(categoryData.ID)
                        .setValue(categoryData1)
                        .addOnCompleteListener(taskSnap ->{
                            if (taskSnap.isSuccessful()){
                                Toast.makeText(this, "Updated category", Toast.LENGTH_SHORT).show();
                                finish();
                            }else Toast.makeText(this, ""+taskSnap.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10087 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            Toast.makeText(this, ""+pickedImage.toString(), Toast.LENGTH_SHORT).show();
            upLoadImageToStorage(pickedImage);
        }
    }

    private void upLoadImageToStorage(Uri storageImgUri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("CategoryImages/"+System.currentTimeMillis()+".jpg");
        storageReference.putFile(storageImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    String path = task.getResult()
                            .getMetadata()
                            .getPath();
                    task.getResult()
                            .getStorage()
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String,Object> data = new HashMap<>();
                            data.put("path",path);
                            data.put("uri",uri.toString());

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference("UPLOADED_IMAGES")
                                    .child(String.valueOf(System.currentTimeMillis()))
                                    .updateChildren(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        url.getEditText().setText(uri.toString());

                                    }else {
                                        FirebaseDatabase.getInstance().getReference(path).removeValue();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}