package com.thundersharp.bombaydine.Delevery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.thundersharp.bombaydine.Delevery.core.DeliveryOrderListner;
import com.thundersharp.bombaydine.Delevery.core.ItemDeliverHolder;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.core.Adapter.ItemOrderHolder;
import com.thundersharp.bombaydine.kitchen.core.KitchenOrderListner;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeDelevery extends AppCompatActivity implements OrderContract.onOrderFetch{

    RecyclerView rv_delivery_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delevery);

        rv_delivery_orders = findViewById(R.id.rv_delivery_orders);

        DeliveryOrderListner
                .getKitchenOrderInstance()
                .setDate(getDate())
                .setOnOrderSuccessFailureListner(this)
                .fetchRecentOrders();


        findViewById(R.id.acccswitch).setOnClickListener(view -> {
            AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onOrderFetchSuccess(List<Object> data) {
        Collections.reverse(data);
        // Toast.makeText(getContext(), "data"+data.size(), Toast.LENGTH_SHORT).show();
        rv_delivery_orders.setAdapter(new ItemDeliverHolder(this,data));
    }

    @Override
    public void onDataFetchFailure(Exception e) {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}