package com.thundersharp.admin.ui.edits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;

public class CategoryAddEdit extends AppCompatActivity {

    CategoryData categoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_edit);
        if (getIntent().getSerializableExtra("data") !=null){
            categoryData = (CategoryData)getIntent().getSerializableExtra("data");

        }


    }
}