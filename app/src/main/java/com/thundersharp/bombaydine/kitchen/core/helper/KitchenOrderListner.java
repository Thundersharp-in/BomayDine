package com.thundersharp.bombaydine.kitchen.core.helper;

import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.Delevery.core.DeliveryOrderContract;
import com.thundersharp.bombaydine.Delevery.core.DeliveryOrderListner;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class KitchenOrderListner implements OrderContract , OrderContract.Status{

    static KitchenOrderListner kitchenOrderListner;
    private KitchenOrder onOrderFetch;
    private String date;
    private StatusSuccessFailure statusSuccessFailure;

    public static KitchenOrderListner getKitchenOrderInstance(){
        kitchenOrderListner = new KitchenOrderListner();
        return kitchenOrderListner;
    }

    public KitchenOrderListner() {
    }

    public KitchenOrderListner setOnOrderSuccessFailureListner(KitchenOrder orderSuccessFailureListner){
        kitchenOrderListner.KitchenOrderListne(orderSuccessFailureListner);
        return kitchenOrderListner;
    }

    public KitchenOrderListner setOnStatusSuccessFailureListner(OrderContract.StatusSuccessFailure statusSuccessFailure){
        kitchenOrderListner.KitchenOrderListne(statusSuccessFailure);
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

    public void KitchenOrderListne(StatusSuccessFailure statusSuccessFailure){
        this.statusSuccessFailure = statusSuccessFailure;
    }
    public void KitchenOrderListne(KitchenOrder orderFetch){
        this.onOrderFetch = orderFetch;
    }

    @Override
    public void fetchRecentOrders() {
        List<Object> allOrders =new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ORDERS)
                .child(date)//  getDate()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                            onOrderFetch.onOrderFetchSuccess(snapshot,true);

                        }else {
                            onOrderFetch.onDataFetchFailure(new Exception("No orders Today yet !"));
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        onOrderFetch.onOrderFetchSuccess(snapshot,false);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
