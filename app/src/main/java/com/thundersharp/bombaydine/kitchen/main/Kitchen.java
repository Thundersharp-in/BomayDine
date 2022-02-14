package com.thundersharp.bombaydine.kitchen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.core.Adapter.ItemOrderHolder;
import com.thundersharp.bombaydine.kitchen.core.helper.KitchenOrder;
import com.thundersharp.bombaydine.kitchen.core.helper.KitchenOrderListner;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Kitchen extends AppCompatActivity  implements KitchenOrder {

    RecyclerView rv_order;
    ItemOrderHolder itemOrderHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        rv_order = findViewById(R.id.rv_order);

        KitchenOrderListner
                .getKitchenOrderInstance()
                .setDate(getDate())
                .setOnOrderSuccessFailureListner(this)
                .fetchRecentOrders();


        findViewById(R.id.logout).setOnClickListener(viewclk -> {
            AccountHelper
                    .getInstance(Kitchen.this)
                    .clearAllData();
            startActivity(new Intent(Kitchen.this, MainActivity.class));
            finish();
        });

    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onOrderFetchSuccess(DataSnapshot data, boolean isNew) {
        if (isNew){
            if (itemOrderHolder == null) {
                List<DataSnapshot> list= new ArrayList<>();
                list.add(data);
                itemOrderHolder = new ItemOrderHolder(Kitchen.this,list);
                rv_order.setAdapter(itemOrderHolder);
            }else itemOrderHolder.addNew(data);

        }else itemOrderHolder.upDateExisting(data);


        //Collections.reverse(data);
        // Toast.makeText(getContext(), "data"+data.size(), Toast.LENGTH_SHORT).show();
        // rv_order.setAdapter(new ItemOrderHolder(getContext(),data));
    }

    @Override
    public void onDataFetchFailure(Exception e) {
        Toast.makeText(Kitchen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}