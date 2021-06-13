package com.thundersharp.admin.ui.edits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;

public class CategoryAddEdit extends AppCompatActivity {

    CategoryData categoryData;
    ImageView imagehome;
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

        if (getIntent().getSerializableExtra("data") !=null) {
            Glide.with(this).load(categoryData.getIMAGES()).into(imagehome);
            categoryName.getEditText().setText(categoryData.getNAME());
            url.getEditText().setText(categoryData.getIMAGES());
        }

        categoryUpdate.setOnClickListener(v->{

        });

    }
}