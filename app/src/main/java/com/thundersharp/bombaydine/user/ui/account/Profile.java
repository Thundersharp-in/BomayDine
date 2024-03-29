package com.thundersharp.bombaydine.user.ui.account;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.TokenVerification;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.login.Logout;
import com.thundersharp.bombaydine.user.ui.AcharayaLogin;
import com.thundersharp.bombaydine.user.ui.home.MainPage;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.bombaydine.user.ui.ratings.RatingsNReview;
import com.thundersharp.bombaydine.user.ui.settings.SettingsActivity;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;
import com.thundersharp.bombaydine.user.ui.wallets.WalletActivity;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    private RelativeLayout bottomHolderprofile;
    private MaterialCardView your_orders,helpNfeedback;
    private CoordinatorLayout containermain;
    private TextView switchbtn,logout;
    private BottomSheetDialog bottomSheetDialogloc;

    private TextView profile_name,profile_email;
    private CircleImageView profilepic;
    private TextView wallet_balance,orderNo,foodie_level, email_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_main, container, false);

        bottomHolderprofile = view.findViewById(R.id.bottomHolderprofile);
        your_orders = view.findViewById(R.id.your_orders);
        containermain = view.findViewById(R.id.containermain);
        switchbtn = view.findViewById(R.id.switchbtn);
        logout = view.findViewById(R.id.logoutn);
        helpNfeedback = view.findViewById(R.id.help);

        profile_name = view.findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        profilepic = view.findViewById(R.id.profilepic);
        wallet_balance = view.findViewById(R.id.wallet_balance);
        orderNo = view.findViewById(R.id.orderNo);
        foodie_level = view.findViewById(R.id.foodie_level);
        email_status = view.findViewById(R.id.email_status);

        orderNo.setText("0");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            profile_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profile_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                email_status.setText("Email id is not verified yet to verify navigate to your provided email id or , details in your profile click on Update data above.");
            }

            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profilepic);
            }else {
                Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(profilepic);
            }

            fetchOrderNo();

            ((TextView)view.findViewById(R.id.updatedata)).setOnClickListener(b->{
                startActivity(new Intent(getActivity(),UpdateProfileActivity.class));
            });
        }

        Animator
                .initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,containermain);

        your_orders.setOnClickListener(view1 -> {

            MainPage.navController.navigate(R.id.discover);
        });



        ((MaterialCardView)view.findViewById(R.id.wallet)).setOnClickListener(V->{
            startActivity(new Intent(getActivity(), WalletActivity.class));
        });

        ((LinearLayout)view.findViewById(R.id.rate)).setOnClickListener(xc -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
            }
        });

        ((LinearLayout)view.findViewById(R.id.settings)).setOnClickListener(vir ->{
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        });

        ((LinearLayout)view.findViewById(R.id.bookmarks)).setOnClickListener(vir ->{
            startActivity(new Intent(getActivity(), Bookmarks.class));
        });

        ((LinearLayout)view.findViewById(R.id.payments)).setOnClickListener(vir ->{
            startActivity(new Intent(getActivity(), Payments.class));
        });

        ((LinearLayout)view.findViewById(R.id.addAddress)).setOnClickListener(vir ->{
            startActivity(new Intent(getActivity(), AddUpdateAddress.class).putExtra("fetch",false));
        });


        ((MaterialCardView)view.findViewById(R.id.allOffers)).setOnClickListener(itemView->{
            startActivity(new Intent(getActivity(), AllOffersActivity.class));//AllOffersDisplay
        });

        ((MaterialCardView)view.findViewById(R.id.reportIssue)).setOnClickListener(itemView->{
            startActivity(new Intent(getActivity(), ReportSeriousIssue.class));
        });

        ((MaterialCardView)view.findViewById(R.id.ratingsNreview)).setOnClickListener(V ->{
            startActivity(new Intent(getActivity(), RatingsNReview.class));
        });

        ((MaterialCardView)view.findViewById(R.id.refunds)).setOnClickListener(V ->{
            startActivity(new Intent(getActivity(), Refunds.class));
        });

        wallet_balance.setOnClickListener(View-> Toast.makeText(getActivity(), "Comming soon", Toast.LENGTH_SHORT).show());

        helpNfeedback.setOnClickListener(view1 -> {
            ChatStarter chatStarter = ChatStarter.initializeChat(getActivity());
            chatStarter.setCostomerName("Hrishikesh Prateek");
            chatStarter.setChatType(ChatStarter.MODE_CHAT_FROM_PROFILE_HELP_N_FEEDBACK);
            chatStarter.setSenderUid(FirebaseAuth.getInstance().getUid());

            try {
                chatStarter.startChat();
            } catch (ParametersMissingException e) {
                e.printStackTrace();
            }
        });

        logout.setOnClickListener(view1 -> {
            new AlertDialog
                    .Builder(getActivity())
                    .setMessage("Are you sure you want to logout from this device ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new SharedPrefHelper(getActivity()).clearSavedHomeLocationData();
                            Logout.logout();
                            startActivity(new Intent(getActivity(),LoginActivity.class));
                            getActivity().finish();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        });

        switchbtn.setOnClickListener(view1 -> {
            bottomSheetDialogloc = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            View bottomview = LayoutInflater.from(getContext()).inflate(R.layout.switch_layout,null);
            AppCompatButton submit = bottomview.findViewById(R.id.suv);
            AppCompatButton cancel = bottomview.findViewById(R.id.cancel);
            EditText code = bottomview.findViewById(R.id.search_home);

            cancel.setOnClickListener(c -> bottomSheetDialogloc.hide());
            submit.setOnClickListener(view2 -> {

                if (FirebaseAuth.getInstance().getCurrentUser().getEmail() !=null || !FirebaseAuth.getInstance().getCurrentUser().getEmail().isEmpty()) {
                    if (code.getText().toString().isEmpty()) {
                        code.setError("Cant be empty");
                        code.requestFocus();
                    } else if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Toast.makeText(getActivity(), "Log in first", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    } else {
                        AccountHelper
                                .getInstance(getActivity())
                                .setUid(FirebaseAuth.getInstance().getUid())
                                .setListner(new SharedPrefUpdater.AccountSwitch.lisetner() {
                                    @Override
                                    public void onSaveSuccess(String employeeCode, String name, String type) {

                                        if (employeeCode.equals(code.getText().toString())) {
                                            String des = null;
                                            if (type.equalsIgnoreCase("1")) {
                                                des = "Kitchen staff";
                                            } else if (type.equalsIgnoreCase("2")) {
                                                des = "Delivery Partner";
                                            } else if (type.equalsIgnoreCase("0")) {
                                                des = "Admin";
                                            }
                                            new AlertDialog
                                                    .Builder(getActivity())
                                                    .setMessage("Welcome " + name + " Switch to your account as " + des)
                                                    .setCancelable(false)
                                                    .setPositiveButton("SWITCH", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            TokenVerification
                                                                    .getInstance(getActivity())
                                                                    .saveCredentials(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                                                            FirebaseAuth.getInstance().getUid());
                                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                                            getActivity().finish();
                                                        }
                                                    })
                                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            AccountHelper
                                                                    .getInstance(getActivity())
                                                                    .clearAllData();
                                                            dialogInterface.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else
                                            Toast.makeText(getContext(), "Entered code is incorrect.", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onSaveFailure(Exception e) {
                                        if (e.getMessage().equals("ER1"))
                                            Toast.makeText(getContext(), "Account not Authorised to access this area.", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }else
                    Toast.makeText(getActivity(), "Update email to proceed", Toast.LENGTH_SHORT).show();
            });

            bottomSheetDialogloc.setContentView(bottomview);

            bottomSheetDialogloc.show();
        });


        return view;

    }

    private void fetchOrderNo() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child(CONSTANTS.DATABASE_NODE_ORDERS)
                .child(CONSTANTS.DATABASE_NODE_OVERVIEW)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            orderNo.setText(String.valueOf(snapshot.getChildrenCount()));
                        }else {
                            orderNo.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Animator.initializeAnimator().customAnimation(R.anim.slide_from_left_fast,containermain);
    }
}

