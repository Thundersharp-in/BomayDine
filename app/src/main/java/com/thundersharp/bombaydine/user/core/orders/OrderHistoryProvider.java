package com.thundersharp.bombaydine.user.core.orders;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryProvider implements OrderContract{

    static OrderHistoryProvider orderHistoryProvider;
    static int renderNoq = 6;

    public static OrderHistoryProvider getOrderInstance(int renderNo){
        orderHistoryProvider = new OrderHistoryProvider();
        renderNoq = renderNo;
        return orderHistoryProvider;
    }

    public OrderHistoryProvider setOnOrderFetchListener(OrderContract.onOrderFetch onOrderFetchListener){
        orderHistoryProvider.OrderHistoryProvider(onOrderFetchListener);
        return orderHistoryProvider;
    }
    public OrderHistoryProvider(){}

    public void OrderHistoryProvider(OrderContract.onOrderFetch onOrderFetch) {
        this.onOrderFetch = onOrderFetch;
    }

    private OrderContract.onOrderFetch onOrderFetch;

    @Override
    public void fetchRecentOrders() {
        List<Object> objects = new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(CONSTANTS.DATABASE_NODE_ORDERS)
                    .child(CONSTANTS.DATABASE_NODE_OVERVIEW)
                    .limitToFirst(renderNoq)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    objects.add(dataSnapshot);
                                }
                                onOrderFetch.onOrderFetchSuccess(objects);

                            }else {
                                Exception exception = new Exception("ERROR 404 : NO DATA FOUND");
                                onOrderFetch.onDataFetchFailure(exception);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            onOrderFetch.onDataFetchFailure(error.toException());
                        }
                    });
        }else {
            Exception exception = new Exception("User not Logged in.");
            onOrderFetch.onDataFetchFailure(exception);
        }
    }
}
