package com.thundersharp.bombaydine.user.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.login.AccountSwitcher;
import com.thundersharp.bombaydine.user.ui.home.MainPage;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

import java.util.HashMap;

public class Profile extends Fragment {

    private RelativeLayout bottomHolderprofile;
    private MaterialCardView your_orders;
    private CoordinatorLayout containermain;
    private TextView switchbtn;
    private BottomSheetDialog bottomSheetDialogloc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_main, container, false);

        bottomHolderprofile = view.findViewById(R.id.bottomHolderprofile);
        your_orders = view.findViewById(R.id.your_orders);
        containermain = view.findViewById(R.id.containermain);
        switchbtn = view.findViewById(R.id.switchbtn);

        Animator
                .initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,containermain);

        your_orders.setOnClickListener(view1 -> {

            MainPage.navController.navigate(R.id.discover);
        });

        switchbtn.setOnClickListener(view1 -> {
            bottomSheetDialogloc = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            View bottomview = LayoutInflater.from(getContext()).inflate(R.layout.switch_layout,null);
            AppCompatButton submit = bottomview.findViewById(R.id.suv);
            AppCompatButton cancel = bottomview.findViewById(R.id.cancel);
            EditText code = bottomview.findViewById(R.id.search_home);

            cancel.setOnClickListener(c -> bottomSheetDialogloc.hide());
            submit.setOnClickListener(view2 -> {

                if(code.getText().toString().isEmpty()){
                    code.setError("Cant be empty");
                    code.requestFocus();
                }else if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Toast.makeText(getActivity(),"Log in first",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    AccountHelper
                            .getInstance(getActivity())
                            .setUid(FirebaseAuth.getInstance().getUid())
                            .setListner(new SharedPrefUpdater.AccountSwitch.lisetner() {
                                @Override
                                public void onSaveSuccess(String employeeCode, String name) {
                                    if (employeeCode.equals(code.getText().toString())) {
                                        Toast.makeText(getContext(), "done " + employeeCode, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getContext(), "Entered code is incorrect.", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onSaveFailure(Exception e) {
                                    if (e.getMessage().equals("ER1")) Toast.makeText(getContext(), "Account not Authorised to access this area.", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            bottomSheetDialogloc.setContentView(bottomview);


            bottomSheetDialogloc.show();
        });


        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Animator.initializeAnimator().customAnimation(R.anim.slide_from_left_fast,containermain);
    }
}

