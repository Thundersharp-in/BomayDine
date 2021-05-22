package com.thundersharp.bombaydine.user.ui.orders;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.Adapters.OrderItem;
import com.thundersharp.bombaydine.user.core.Model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailHelper implements OrderDetail{

    OrderDetail.OrderListner orderListner;
    List<OrderModel> modelList;


    public OrderDetailHelper(OrderListner orderListner) {
        this.orderListner = orderListner;
    }

    @Override
    public void FetchOrder(String ID) {
        modelList=new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference("USERS")
                .child(FirebaseAuth.getInstance().getUid())
                .child("ORDERS")
                .child("DETAILS")
                .child(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                modelList.add(snapshot1.getValue(OrderModel.class));
                            }
                            orderListner.onSuccess(modelList);
                        }else {
                            Exception exception=new Exception("Data Not Exists");
                            orderListner.onFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        orderListner.onFailure(error.toException());
                    }
                });
    }
}
