package com.thundersharp.bombaydine.kitchen.core.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.core.Adapter.ItemOrderHolder;
import com.thundersharp.bombaydine.kitchen.core.KitchenOrderListner;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;

import java.util.List;


public class HomeKitchenFragment extends Fragment implements OrderContract.onOrderFetch{

    RecyclerView rv_order;
    KitchenOrderListner kitchenOrderListner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_kitchen, container, false);

        rv_order = view.findViewById(R.id.rv_order);

        view.findViewById(R.id.acccswitch).setOnClickListener(viewclk -> {

            ChatStarter chatStarter = ChatStarter.initializeChat(getActivity());
            chatStarter.setChatType(ChatStarter.MODE_CHAT_ADMIN);
            chatStarter.setCostomerName("Hrishikesh Prateek");
            chatStarter.setCustomerId(FirebaseAuth.getInstance().getUid());
            chatStarter.setSenderUid("SUPPORT56065");

            try {
                chatStarter.startChat();
            } catch (ParametersMissingException e) {
                e.printStackTrace();
            }

         /*   AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();*/

            kitchenOrderListner = new KitchenOrderListner().setOnOrderSuccessFailureListner(this);

            KitchenOrderListner
                    .getKitchenOrderInstance()
                    .fetchRecentOrders();


            /*
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
                                            }
                                        }
                                    }

                                }
                                rv_order.setAdapter(ItemOrderHolder.initialise(getContext(),allOrders));
                            }else {
                                Toast.makeText(getContext(), "Orders Not Placed Yet !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

             */

        });

        return view;
    }

    @Override
    public void onOrderFetchSuccess(List<Object> data) {
        rv_order.setAdapter(new ItemOrderHolder(getContext(),data));
    }

    @Override
    public void onDataFetchFailure(Exception e) {
        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}