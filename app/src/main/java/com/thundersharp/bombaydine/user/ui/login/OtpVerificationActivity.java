package com.thundersharp.bombaydine.user.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.thundersharp.bombaydine.R;

public class OtpVerificationActivity extends AppCompatActivity {

    OtpView otp_view;
    AppCompatButton submit,cancel;
    boolean isSubmitEnabled = false;
    private String otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otp_view = findViewById(R.id.otp_view);
        submit = findViewById(R.id.vieify);
        cancel = findViewById(R.id.cancel);

        submit.setOnClickListener(view -> {
            if (isSubmitEnabled){
                Intent i = new Intent();
                i.setAction(otp);
                setResult(10001,i);
                finish();
            }else {
                //Snackbar.make(main,"Enter complete otp first",Snackbar.LENGTH_SHORT).show();
            }
        });

        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otpr) {
                otp = otpr;
                isSubmitEnabled = true;
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