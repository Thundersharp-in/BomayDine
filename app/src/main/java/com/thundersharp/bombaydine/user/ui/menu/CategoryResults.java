package com.thundersharp.bombaydine.user.ui.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Data.CategoryDataContract;
import com.thundersharp.bombaydine.user.core.Data.CategoryDataProvider;
import com.thundersharp.bombaydine.user.core.Model.CategoryData;

import java.util.List;

public class CategoryResults extends AppCompatActivity implements CategoryDataContract.OnCategoryDataFetch {

    private CategoryDataProvider categoryDataProvider;
    private ImageView imagehome;
    CategoryData categoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_results);
        categoryData = (CategoryData) getIntent().getSerializableExtra("data");
        categoryDataProvider = new CategoryDataProvider(this);


        imagehome = findViewById(R.id.imagehome);

        categoryDataProvider.fetchCategoryData(categoryData);

    }

    @Override
    public void OnCategoryDataSuccess(List<Object> data) {

    }

    @Override
    public void OnCategoryDataFetchFailure(Exception exception) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
    }
}