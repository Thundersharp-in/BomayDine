package com.thundersharp.admin.core.orders;

import android.content.res.Resources;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.discover.DataNotFound;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderHistoryProvider implements OrderContract , OrderContract.Status{

    static OrderHistoryProvider orderHistoryProvider;
    private OrderContract.onOrderFetch onOrderFetch;
    private String date;
    private StatusSuccessFailure statusSuccessFailure;

    public static OrderHistoryProvider getOrderInstance(){
        orderHistoryProvider = new OrderHistoryProvider();
        return orderHistoryProvider;
    }

    public OrderHistoryProvider() {
    }

    public OrderHistoryProvider setOnOrderSuccessFailureListner(OrderContract.onOrderFetch orderSuccessFailureListner){
        orderHistoryProvider.KitchenOrderListne(orderSuccessFailureListner);
        return orderHistoryProvider;
    }

    public OrderHistoryProvider setOnStatusSuccessFailureListner(StatusSuccessFailure statusSuccessFailure){
        orderHistoryProvider.OrderHistoryProvider(statusSuccessFailure);
        return orderHistoryProvider;
    }

    public OrderHistoryProvider setDate(String date){
        this.date= date;
        return orderHistoryProvider;
    }

    public String getDate(){
        return date;
    }

    public void OrderHistoryProvider(StatusSuccessFailure statusSuccessFailure){
        this.statusSuccessFailure = statusSuccessFailure;
    }
    public void KitchenOrderListne(OrderContract.onOrderFetch orderFetch){
        this.onOrderFetch = orderFetch;
    }



    @Override
    public ChildEventListener fetchRecentOrders() {
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ORDERS)
                .child(date);
                return databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                            onOrderFetch.onOrderFetchSuccess(snapshot,true);

                        }else {
                            onOrderFetch.onDataFetchFailure(new DataNotFound("No orders on "+date+" yet !"));
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
                        onOrderFetch.onDataFetchFailure(error.toException());
                    }
                });
    }

    private String getDateFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }


    @Override
    public void setStatus(String order_id, int value, String custUid) {
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
}
