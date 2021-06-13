package com.thundersharp.admin.ui.edits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.Random;

public class CategoryAddEdit extends AppCompatActivity {

    CategoryData categoryData;
    ImageView imagehome,edit;
    AppCompatButton categoryUpdate;
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

        });

        categoryUpdate.setOnClickListener(v->{

            if (categoryName.getEditText().getText().toString().isEmpty()){

                categoryName.getEditText().setError("Name Cannot be empty !!");
                categoryName.getEditText().requestFocus();

            }else if (url.getEditText().getText().toString().isEmpty()){
                url.getEditText().setError("Url Cannot be empty ! Either enter url or select a image from your device.");
                url.getEditText().requestFocus();
            }else {
                if (categoryData.ID == null){
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
}