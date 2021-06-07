package com.thundersharp.admin.ui.account;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.animation.Animator;

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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        return view;
    }


}