package com.thundersharp.admin.ui.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.CordinatesPointsAdapter;
import com.thundersharp.admin.core.address.CordinatesInteractor;
import com.thundersharp.admin.core.address.Cordinateslistner;

import java.util.Arrays;
import java.util.List;

public class UpdatePoints extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_points);

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        load();
        swipeRefreshLayout.setRefreshing(true);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(c ->finish());

    }

    public void load(){
        new CordinatesInteractor(new Cordinateslistner.fetchSuccessListener() {
            @Override
            public void onCordinatesSuccess(LatLng... coOrdinates) {
                List<LatLng> latLngs = Arrays.asList(coOrdinates);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                RecyclerView recyclerView = findViewById(R.id.coordinates);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(new CordinatesPointsAdapter(UpdatePoints.this,latLngs));
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCordinatesFailure(Exception exception) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(UpdatePoints.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).fetchAllCoordinates();

    }
}