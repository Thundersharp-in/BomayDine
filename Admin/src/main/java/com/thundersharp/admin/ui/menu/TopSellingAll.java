package com.thundersharp.admin.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.TopsellingAdapter;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;

import java.util.List;

public class TopSellingAll extends AppCompatActivity implements
        HomeDataContract.topSellingAllFetch,
        HomeDataContract.DataLoadFailure{

    private HomeDataProvider homeDataProvider;
    private RecyclerView topsellingrecycler;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_selling_all_admin);

        homeDataProvider = new HomeDataProvider(this,this,this);
        topsellingrecycler = findViewById(R.id.topsellingholderq);
        toolbar = findViewById(R.id.tool);
        swipeRefreshLayout =findViewById(R.id.swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            homeDataProvider.fetchTopSellingAll();
        });
        toolbar.setNavigationOnClickListener(view -> {finish();});
        homeDataProvider.fetchTopSellingAll();


    }
    @Override
    public void onAllTopSellingfetchSuccess(List<Object> data) {
        swipeRefreshLayout.setRefreshing(false);

        TopsellingAdapter categoryAdapter = new TopsellingAdapter(this, data,true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        topsellingrecycler.setLayoutManager(gridLayoutManager);
        topsellingrecycler.setAdapter(categoryAdapter);
    }

    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

}