package com.thundersharp.admin.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.CategoryAdapter;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;

import java.util.List;

public class AllCategoryActivity extends AppCompatActivity implements HomeDataContract.categoryFetch, HomeDataContract.DataLoadFailure{
    private HomeDataProvider homeDataProvider;
    private RecyclerView categoryRecycler;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category_admin);
        homeDataProvider = new HomeDataProvider(this,this,this);
        categoryRecycler = findViewById(R.id.categoryRecycler);
        toolbar = findViewById(R.id.tool);
        swipeRefreshLayout =findViewById(R.id.swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            homeDataProvider.fetchTopSellingAll();
        });
        toolbar.setNavigationOnClickListener(view -> {finish();});
        homeDataProvider.fetchAllCategories();
    }

    @Override
    public void onCategoryFetchSuccess(List<Object> data) {
        swipeRefreshLayout.setRefreshing(false);
        CategoryAdapter categoryAdapter = new CategoryAdapter(data, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        categoryRecycler.setLayoutManager(gridLayoutManager);
        categoryRecycler.setAdapter(categoryAdapter);
    }

    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}