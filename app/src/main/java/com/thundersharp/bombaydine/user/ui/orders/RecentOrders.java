package com.thundersharp.bombaydine.user.ui.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.RecentAdapter;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.orders.OrderHistoryProvider;

import java.util.List;

public class RecentOrders extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        toolbar.setNavigationOnClickListener(view -> finish());

        OrderHistoryProvider
                .getOrderInstance(6)
                .setOnOrderFetchListener(new OrderContract.onOrderFetch() {
            @Override
            public void onOrderFetchSuccess(List<Object> data) {
                //DataSnapshot dataSnapshot = (DataSnapshot)data.get(0);
                recyclerView.setAdapter(RecentAdapter.initialize(RecentOrders.this,data));
                //Toast.makeText(RecentOrders.this,dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataFetchFailure(Exception e) {
                Toast.makeText(RecentOrders.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).fetchRecentOrders();

    }
}