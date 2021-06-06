package com.thundersharp.bombaydine.kitchen.core;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class KitchenOrderListner implements OrderContract , OrderContract.Status{

    static KitchenOrderListner kitchenOrderListner;
    private OrderContract.onOrderFetch onOrderFetch;
    private String date;
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

    /**
     * @apiNote Provide date in format {dd-MM-yyyy}
     * @param date
     * @return
     */
    public KitchenOrderListner setDate(String date){
        this.date= date;
        return kitchenOrderListner;
    }

    public String getDate(){
        return date;
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
        Log.e("Start","Statred");
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ORDERS)
                .child(date)//  getDate()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                allOrders.add(snapshot1);
                            }
                            onOrderFetch.onOrderFetchSuccess(allOrders);

                        }else {
                            onOrderFetch.onDataFetchFailure(new Exception("No orders Today yet !"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void setStatus(String order_id, int value,String custUid) {
        String date=getDateFromTimeStamp(Long.parseLong(order_id));

        HashMap<String,Object> updateToLocation = new HashMap<>();
        updateToLocation.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+date+"/"+order_id+"/status",String.valueOf(value));
        updateToLocation.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+ custUid+"/" +CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+order_id+"/status",String.valueOf(value));

        FirebaseDatabase
                .getInstance()
                .getReference()
                .updateChildren(updateToLocation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        statusSuccessFailure.onSuccess(task);
                    }else {
                        statusSuccessFailure.onFailure(task.getException());
                    }
                });
    }

    private String getDateFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }


}
