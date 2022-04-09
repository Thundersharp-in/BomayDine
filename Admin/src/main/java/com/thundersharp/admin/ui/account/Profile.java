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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.AdminModule;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.ui.edits.EditTopSelling;
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
        profile_name = view.findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        profilepic = view.findViewById(R.id.profilepic);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            profile_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profile_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"(ADMIN)");

            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profilepic);
            }else {
                //Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(profilepic);
            }
        }


        ((MaterialCardView)view.findViewById(R.id.allOffers)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), AllOffersActivity.class)));

        ((MaterialCardView)view.findViewById(R.id.reportIssue)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), ReportIssue.class)));

        //((MaterialCardView)view.findViewById(R.id.your_orders)).setOnClickListener(allOffer -> startActivity(new Intent(getContext(), ReportIssue.class)));


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
        bookmarks.setOnClickListener(v -> Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show());


        ((LinearLayout)view.findViewById(R.id.settings)).setOnClickListener(vir ->{
            Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
        });

        ((LinearLayout)view.findViewById(R.id.bookmarks)).setOnClickListener(vir ->{
            Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
        });

        ((LinearLayout)view.findViewById(R.id.payments)).setOnClickListener(vir ->{
           Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
        });

        ((LinearLayout)view.findViewById(R.id.addAddress)).setOnClickListener(vir ->{
            Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
        });

        ((MaterialCardView)view.findViewById(R.id.ratingsNreview)).setOnClickListener(V ->{
            Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
        });

        ((MaterialCardView)view.findViewById(R.id.refunds)).setOnClickListener(V ->{
            Toast.makeText(getContext(), "Not Available for admin", Toast.LENGTH_SHORT).show();
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