package com.thundersharp.bombaydine.Delevery.core;

import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DeliveryOrderListner implements OrderContract , OrderContract.Status{

    static DeliveryOrderListner kitchenOrderListner;
    private OrderContract.onOrderFetch onOrderFetch;
    private String date;
    private StatusSuccessFailure statusSuccessFailure;

    public static DeliveryOrderListner getKitchenOrderInstance(){
        kitchenOrderListner = new DeliveryOrderListner();
        return kitchenOrderListner;
    }

    public DeliveryOrderListner() {
    }

    public DeliveryOrderListner setOnOrderSuccessFailureListner(OrderContract.onOrderFetch orderSuccessFailureListner){
        kitchenOrderListner.KitchenOrderListner(orderSuccessFailureListner);
        return kitchenOrderListner;
    }

    public DeliveryOrderListner setOnStatusSuccessFailureListner(StatusSuccessFailure statusSuccessFailure){
        kitchenOrderListner.KitchenOrderListner(statusSuccessFailure);
        return kitchenOrderListner;
    }

    /**
     * @apiNote Provide date in format {dd-MM-yyyy}
     * @param date
     * @return
     */
    public DeliveryOrderListner setDate(String date){
        this.date= date;
        return kitchenOrderListner;
    }

    public String getDate(){
        return date;
    }

    public void KitchenOrderListner(StatusSuccessFailure statusSuccessFailure){
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
                .child(date)//  getDate()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                //OrederBasicDetails orederBasicDetails= snapshot1.getValue(OrederBasicDetails.class);
                                allOrders.add(snapshot1);
                            }
                            onOrderFetch.onOrderFetchSuccess(allOrders);

                        }else {
                            onOrderFetch.onDataFetchFailure(new Exception("No orders Today yet !"));
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CHeck____________", "Not Found");
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
