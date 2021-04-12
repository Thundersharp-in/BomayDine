package com.thundersharp.bombaydine.user.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.thundersharp.bombaydine.R;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpVerificationActivity extends AppCompatActivity {

    OtpTextView otp_view;
    AppCompatButton submit,cancel;
    boolean isSubmitEnabled = false;
    private String otp;
    RelativeLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otp_view = findViewById(R.id.otp_view);
        submit = findViewById(R.id.vieify);
        cancel = findViewById(R.id.cancel);
        main = findViewById(R.id.main);

        submit.setOnClickListener(view -> {
            if (isSubmitEnabled){
                Intent i = new Intent();
                i.setAction(otp);
                setResult(10001,i);
                finish();
            }else {
                Snackbar.make(main,"Enter complete otp first",Snackbar.LENGTH_SHORT).show();
            }
        });

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otpr) {
                otp = otpr;
                isSubmitEnabled = true;
                //Toast.makeText(OtpVerificationActivity.this,otp,Toast.LENGTH_SHORT).show();
            }
        });

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setAction("123456");
                setResult(10001,i);
                finish();
            }
        },5000);
*/
    }
}