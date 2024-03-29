package com.thundersharp.admin.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.CategoryitemAdapter;
import com.thundersharp.admin.core.Data.CategoryDataContract;
import com.thundersharp.admin.core.Data.CategoryDataProvider;
import com.thundersharp.admin.core.Model.CategoryData;

import java.util.List;

public class CategoryResults extends AppCompatActivity implements CategoryDataContract.OnCategoryDataFetch {

    private CategoryDataProvider categoryDataProvider;
    private ImageView imagehome;
    private CategoryData categoryData;
    private TextView txttop;
    private ShimmerFrameLayout shimmerac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_results_admin);
        categoryData = (CategoryData) getIntent().getSerializableExtra("data");
        categoryDataProvider = new CategoryDataProvider(this);


        imagehome = findViewById(R.id.imagehome);
        txttop = findViewById(R.id.txttop);
        shimmerac = findViewById(R.id.shimmerac);
        shimmerac.startShimmer();
        txttop.setText(categoryData.NAME);
        Glide.with(CategoryResults.this).load(categoryData.IMAGES).into(imagehome);


        categoryDataProvider.fetchCategoryData(categoryData);

    }
    @Override
    public void OnCategoryDataSuccess(List<Object> data) {
        RecyclerView recyclerView = findViewById(R.id.recyclerviewcategory);
        CategoryitemAdapter categoryitemAdapter = new CategoryitemAdapter(this,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(categoryitemAdapter);

        shimmerac.stopShimmer();
        shimmerac.setVisibility(View.GONE);
    }

    @Override
    public void OnCategoryDataFetchFailure(Exception exception) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
        shimmerac.stopShimmer();
        shimmerac.setVisibility(View.GONE);
    }
}