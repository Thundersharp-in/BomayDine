package com.thundersharp.admin.ui.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.RecentAdapter;
import com.thundersharp.admin.core.orders.OrderContract;
import com.thundersharp.admin.core.orders.OrderHistoryProvider;

import java.util.ArrayList;
import java.util.List;

public class RecentOrders extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders_admin);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        toolbar.setNavigationOnClickListener(view -> finish());

        OrderHistoryProvider
                .getOrderInstance(6)
                .setOnOrderFetchListener(new OrderContract.onOrderFetch() {
                    @Override
                    public void onOrderFetchSuccess(List<Object> data) {
                        List<Object> objectList = new ArrayList<>();
                        recyclerView.setAdapter(RecentAdapter.initialize(RecentOrders.this,data));


                    }

                    @Override
                    public void onDataFetchFailure(Exception e) {
                        Toast.makeText(RecentOrders.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).fetchRecentOrders();

    }
}