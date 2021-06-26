package com.thundersharp.admin.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.thundersharp.admin.AdminModule;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.ui.offers.AllOffersActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    private RelativeLayout bottomHolderprofile;
    private MaterialCardView helpNfeedback;
    private CoordinatorLayout containermain;
    private TextView switchbtn,logout,open;
    private BottomSheetDialog bottomSheetDialogloc;
    LinearLayout bookmarks;

    private TextView profile_name,profile_email,updatedata;
    private CircleImageView profilepic;
    private TextView wallet_balance,orderNo,foodie_level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profiles_admin, container, false);

        ((MaterialCardView)view.findViewById(R.id.allOffers)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), AllOffersActivity.class)));

        ((MaterialCardView)view.findViewById(R.id.reportIssue)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), ReportIssue.class)));

        ((MaterialCardView)view.findViewById(R.id.your_orders)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), ReportIssue.class)));


        ((TextView)view.findViewById(R.id.switchbtn)).setOnClickListener(C ->{
            AdminHelpers.getInstance(getActivity()).clearAllAdminData();
            AdminModule.switchAndRestartApp(getActivity());
            getActivity().finish();
        });

        ((TextView)view.findViewById(R.id.logoutn)).setOnClickListener(C ->{
            AdminHelpers.getInstance(getActivity()).clearAllAdminData();
            AdminModule.signOutAndRestartApp(getActivity());
            getActivity().finish();
        });
        bookmarks =  view.findViewById(R.id.bookmarks);
        bookmarks.setOnClickListener(v -> {});
        return view;
    }

    public void clearAllData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmpAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}