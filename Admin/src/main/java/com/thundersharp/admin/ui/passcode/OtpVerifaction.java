package com.thundersharp.admin.ui.passcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.thundersharp.admin.R;

public class OtpVerifaction extends AppCompatActivity {

    OtpView otp_view;
    AppCompatButton submit;
    TextView cancel;
    boolean isSubmitEnabled = false;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verifaction);

        otp_view = findViewById(R.id.otp_view);
        submit = findViewById(R.id.vieify);
        cancel = findViewById(R.id.cancel);

        submit.setOnClickListener(view -> {
            if (isSubmitEnabled){
                Intent i = new Intent();
                i.setAction(otp);
                setResult(10009,i);
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
    }
}