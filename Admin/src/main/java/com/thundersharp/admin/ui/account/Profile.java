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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.thundersharp.admin.R;
import com.thundersharp.admin.ui.offers.AllOffersActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    private RelativeLayout bottomHolderprofile;
    private MaterialCardView your_orders,helpNfeedback;
    private CoordinatorLayout containermain;
    private TextView switchbtn,logout,open;
    private BottomSheetDialog bottomSheetDialogloc;

    private TextView profile_name,profile_email,updatedata;
    private CircleImageView profilepic;
    private TextView wallet_balance,orderNo,foodie_level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profiles_admin, container, false);

        ((MaterialCardView)view.findViewById(R.id.allOffers)).setOnClickListener(allOffer ->{
            startActivity(new Intent(getContext(), AllOffersActivity.class));
        });

        ((TextView)view.findViewById(R.id.switchbtn)).setOnClickListener(C ->{
            clearAllData();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setPackage("com.thundersharp.bombaydine");
            startActivity(sharingIntent);
            getActivity().finish();
        });

        return view;
    }

    public void clearAllData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmpAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}