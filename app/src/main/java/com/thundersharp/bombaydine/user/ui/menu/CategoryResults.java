package com.thundersharp.bombaydine.user.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.CategoryitemAdapter;
import com.thundersharp.bombaydine.user.core.Data.CategoryDataContract;
import com.thundersharp.bombaydine.user.core.Data.CategoryDataProvider;
import com.thundersharp.bombaydine.user.core.Model.CategoryData;

import java.util.List;

public class CategoryResults extends AppCompatActivity implements CategoryDataContract.OnCategoryDataFetch {

    private CategoryDataProvider categoryDataProvider;
    private ImageView imagehome;
    private CategoryData categoryData;
    private TextView txttop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_results);
        categoryData = (CategoryData) getIntent().getSerializableExtra("data");
        categoryDataProvider = new CategoryDataProvider(this);


        imagehome = findViewById(R.id.imagehome);
        txttop = findViewById(R.id.txttop);
        txttop.setText(categoryData.getNAME());
        Glide.with(CategoryResults.this).load(categoryData.getIMAGES()).into(imagehome);


        categoryDataProvider.fetchCategoryData(categoryData);


    }

    @Override
    public void OnCategoryDataSuccess(List<Object> data) {
        RecyclerView recyclerView = findViewById(R.id.recyclerviewcategory);
        CategoryitemAdapter categoryitemAdapter = new CategoryitemAdapter(this,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(categoryitemAdapter);
    }

    @Override
    public void OnCategoryDataFetchFailure(Exception exception) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
    }
}