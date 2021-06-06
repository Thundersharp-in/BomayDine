package com.thundersharp.bombaydine.kitchen.core.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class HomeKitchenFragment extends Fragment implements OrderContract.onOrderFetch{

    RecyclerView rv_order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_kitchen, container, false);

        rv_order = view.findViewById(R.id.rv_order);

        KitchenOrderListner
                .getKitchenOrderInstance()
                .setDate(getDate())
                .setOnOrderSuccessFailureListner(this)
                .fetchRecentOrders();

        view.findViewById(R.id.acccswitch).setOnClickListener(viewclk -> {

            AccountHelper
                    .getInstance(getActivity())
                    .clearAllData();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();

        });

        return view;
    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onOrderFetchSuccess(List<Object> data) {
        Collections.reverse(data);
        // Toast.makeText(getContext(), "data"+data.size(), Toast.LENGTH_SHORT).show();
        rv_order.setAdapter(new ItemOrderHolder(getContext(),data));
    }

    @Override
    public void onDataFetchFailure(Exception e) {
        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}