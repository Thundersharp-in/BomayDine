package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.Model.TopSellingModel;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EditTopSelling extends AppCompatActivity {

    TextInputLayout title, number, url;
    AppCompatButton updatet,delete;

    TopSellingModel topSellingModel;
    ImageView imagehome,edit;
    String uploadedurl;
    List<FoodItemAdapter> modelList,selectedItems;
    //AutoCompleteTextView item_name;
    //List<String> ids;
    TextView items;
    Boolean isEdit;
    String ID;
    AlertDialog.Builder builder;
    Dialog dialog;

    String[] text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_top_selling);

        if (getIntent().getSerializableExtra("isEdit") != null) {
            isEdit = getIntent().getBooleanExtra("isEdit", false);
            if (getIntent().getSerializableExtra("data") != null) {
                topSellingModel = (TopSellingModel) getIntent().getSerializableExtra("data");
            }
        }
        initViews();

        fetchList();

        if (isEdit) {
            delete.setVisibility(View.VISIBLE);
            updatet.setText("Update");
        } else {
            updatet.setText("Create");
            delete.setVisibility(View.GONE);
        }

        //TODO delete from other place also
        if (isEdit) {
            ID = topSellingModel.ITEMID;
            title.getEditText().setText(topSellingModel.NAME);
            number.getEditText().setText(topSellingModel.NOOFORDERS);
            url.getEditText().setText(topSellingModel.IMAGES);
            Glide.with(EditTopSelling.this).load(url.getEditText().toString()).into(imagehome);
        } else ID = String.valueOf(System.currentTimeMillis());


        edit.setOnClickListener(n -> {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 10087);

        });

        delete.setOnClickListener(vi -> {
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_NODE_TOP_SELLING)
                    .child(ID)
                    .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditTopSelling.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                Toast.makeText(EditTopSelling.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        updatet.setOnClickListener(v -> {

            if (title.getEditText().getText().toString().isEmpty()) {

                title.getEditText().setError("Name Cannot be empty !!");
                title.getEditText().requestFocus();

            } else if (number.getEditText().getText().toString().isEmpty()) {

                number.getEditText().setError("Please provide total number of sellings !!");
                number.getEditText().requestFocus();

            } else if (url.getEditText().getText().toString().isEmpty() && uploadedurl == null) {
                url.getEditText().setError("Url Cannot be empty ! Either enter url or select a image from your device.");
                url.getEditText().requestFocus();
            } else {
                topSellingModel = new TopSellingModel(url.getEditText().getText().toString(), ID ,title.getEditText().getText().toString(),number.getEditText().getText().toString());
                //TopSellingModel topSellingModel = new TopSellingModel(categoryName.getEditText().getText().toString(), categoryData.ID, url.getEditText().getText().toString());

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_TOP_SELLING)
                        .child(ID)
                        .setValue(topSellingModel)
                        .addOnCompleteListener(taskSnap -> {
                            if (taskSnap.isSuccessful()) {
                                Toast.makeText(this, "Updated Top selling", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                Toast.makeText(this, "" + taskSnap.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        items.setOnClickListener(vi->{
            openDilog();
        });

    }

    private void openDilog() {
        final ArrayList itemsSelected = new ArrayList();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Food Items From given list : ");

        builder.setMultiChoiceItems(text, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {

                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {

                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                }).setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < itemsSelected.size(); i++) {
                            selectedItems.add(modelList.get((int)itemsSelected.get(i)));
                        }

                        Toast.makeText(EditTopSelling.this, "Items Selected!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10087 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            upLoadImageToStorage(pickedImage);
        }
    }

    private void upLoadImageToStorage(Uri storageImgUri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TopSellingImages/"+ID+".jpg");
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

    private synchronized void fetchList() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int i =0;
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                FoodItemAdapter food = snapshot1.getValue(FoodItemAdapter.class);
                                modelList.add(food);
                                text[i] = food.NAME;
                                //ids.add(food.ID);
                            }
                        }else {
                            Toast.makeText(EditTopSelling.this, "Add items for restaurant first!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditTopSelling.this, "ERROR:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private synchronized void initViews() {
        title = findViewById(R.id.title);
        number = findViewById(R.id.number);
        url = findViewById(R.id.url);
        updatet = findViewById(R.id.updatet);
        delete = findViewById(R.id.delete);
        imagehome = findViewById(R.id.imagehome);
        edit = findViewById(R.id.edit);
        items = findViewById(R.id.items);
    }

}