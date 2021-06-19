package com.thundersharp.admin.ui.discover;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.RecentAdapter;
import com.thundersharp.admin.core.animation.Animator;
import com.thundersharp.admin.core.orders.OrderContract;
import com.thundersharp.admin.core.orders.OrderHistoryProvider;
import com.thundersharp.admin.ui.orders.RecentOrders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Discover extends Fragment implements OrderContract.onOrderFetch {

    RecyclerView recyclerview;
    RecentAdapter recentOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_admin, container, false);

        Animator.initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,view.findViewById(R.id.containermain));

        recyclerview = view.findViewById(R.id.recyclerview);

        ((MaterialCardView)view.findViewById(R.id.date)).setOnClickListener(date->{
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (view1, year, month, day) -> {

                String monthOfYear = String.valueOf(month+1),dayOfMonth = String.valueOf(day);

                if(month < 10) monthOfYear = "0" + monthOfYear;

                if(day < 10) dayOfMonth  = "0" + dayOfMonth ;

                String date1 = dayOfMonth + "-" + monthOfYear + "-" + year;

                LoadOrders(date1);

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        LoadOrders(getDate());


        return view;
    }

    private void LoadOrders(String date) {
        recentOrderAdapter=null;
        OrderHistoryProvider
                .getOrderInstance()
                .setDate(date)
                .setOnOrderSuccessFailureListner(this)
                .fetchRecentOrders();
    }

    @Override
    public void onOrderFetchSuccess(DataSnapshot data, boolean isNew) {
        if (isNew){
            if (recentOrderAdapter == null) {
                List<DataSnapshot> list= new ArrayList<>();
                list.add(data);
                recentOrderAdapter = new RecentAdapter(getActivity(), list);
                recyclerview.setAdapter(recentOrderAdapter);
            }else recentOrderAdapter.addNew(data);

        }else recentOrderAdapter.upDateExisting(data);

    }

    @Override
    public void onDataFetchFailure(Exception e) {  //TODO CHECK
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}