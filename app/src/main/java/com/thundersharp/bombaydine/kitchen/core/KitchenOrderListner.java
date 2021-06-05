package com.thundersharp.bombaydine.kitchen.core;

import android.text.format.DateFormat;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class KitchenOrderListner implements OrderContract , OrderContract.Status{

    static KitchenOrderListner kitchenOrderListner;
    private OrderContract.onOrderFetch onOrderFetch;
    private OrderContract.StatusSuccessFailure statusSuccessFailure;

    public static KitchenOrderListner getKitchenOrderInstance(){
        kitchenOrderListner = new KitchenOrderListner();
        return kitchenOrderListner;
    }

    public KitchenOrderListner() {
    }

    public KitchenOrderListner setOnOrderSuccessFailureListner(OrderContract.onOrderFetch orderSuccessFailureListner){
        kitchenOrderListner.KitchenOrderListner(orderSuccessFailureListner);
        return kitchenOrderListner;
    }

    public KitchenOrderListner setOnStatusSuccessFailureListner(OrderContract.StatusSuccessFailure statusSuccessFailure){
        kitchenOrderListner.KitchenOrderListner(statusSuccessFailure);
        return kitchenOrderListner;
    }

    public void KitchenOrderListner(OrderContract.StatusSuccessFailure statusSuccessFailure){
        this.statusSuccessFailure = statusSuccessFailure;
    }
    public void KitchenOrderListner(OrderContract.onOrderFetch orderFetch){
        this.onOrderFetch = orderFetch;
    }

    @Override
    public void fetchRecentOrders() {
        List<Object> allOrders =new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ORDERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                if (snapshot1.exists()){
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()){
                                        if (snapshot2.exists()){
                                            allOrders.add(snapshot2);
                                        }else onOrderFetch.onDataFetchFailure(new Exception("Orders Not Placed Yet !"));
                                    }
                                }else onOrderFetch.onDataFetchFailure(new Exception("Orders Not Placed Yet !"));

                            }
                            onOrderFetch.onOrderFetchSuccess(allOrders);

                        }else {
                            onOrderFetch.onDataFetchFailure(new Exception("Orders Not Placed Yet !"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void setStatus(String order_id, int value) {
        String date=getDateFromTimeStamp(Long.parseLong(order_id));
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ORDERS)
                .child(date)
                .child(order_id)
                .child("status")
                .setValue("2")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            statusSuccessFailure.onSuccess(task);
                        }else {
                            statusSuccessFailure.onFailure(task.getException());
                        }
                    }
                });
    }

    private String getDateFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }
}
