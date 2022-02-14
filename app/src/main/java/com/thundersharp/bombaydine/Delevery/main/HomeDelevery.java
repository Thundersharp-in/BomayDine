package com.thundersharp.bombaydine.Delevery.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.thundersharp.bombaydine.Delevery.core.DeliveryOrderContract;
import com.thundersharp.bombaydine.Delevery.core.DeliveryOrderListner;
import com.thundersharp.bombaydine.Delevery.core.ItemDeliverHolder;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeDelevery extends AppCompatActivity implements  DeliveryOrderContract {

    RecyclerView rv_delivery_orders;
    ItemDeliverHolder itemDeliverHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delevery);

        rv_delivery_orders = findViewById(R.id.rv_delivery_orders);

        findViewById(R.id.load).setOnClickListener(view -> {
            DeliveryOrderListner
                    .getKitchenOrderInstance()
                    .setDate(getDate())
                    .setOnOrderSuccessFailureListner(this)
                    .fetchRecentOrders();
        });

        findViewById(R.id.logout).setOnClickListener(vi->
                {
                    AccountHelper
                            .getInstance(HomeDelevery.this)
                            .clearAllData();
                    startActivity(new Intent(HomeDelevery.this, MainActivity.class));
                    HomeDelevery.this.finish();
                }
        );
        /*
        AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
         */

    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onOrderFetchSuccess(DataSnapshot data, boolean isNew) {

        if (isNew){
            if (itemDeliverHolder == null) {
                List<DataSnapshot> list= new ArrayList<>();
                list.add(data);
                itemDeliverHolder = new ItemDeliverHolder(HomeDelevery.this,list);
                rv_delivery_orders.setAdapter(itemDeliverHolder);
            }else itemDeliverHolder.addNew(data);

        }else itemDeliverHolder.upDateExisting(data);

    }

    @Override
    public void onDataFetchFailure(Exception e) {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
