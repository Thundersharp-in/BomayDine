package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.ChangeNumber;
import com.thundersharp.bombaydine.user.core.login.LoginHelper;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.login.OtpVerificationActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AddNumber extends AppCompatActivity {

    private String mVerificationId;
    private AppCompatButton sendotp;
    private CountryCodePicker countryCodePicker;
    EditText editTextCarrierNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthCredential credential;
    Boolean isNoChanged;
    String PhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);

        countryCodePicker = findViewById(R.id.pkr);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        sendotp = findViewById(R.id.sendotp);

        countryCodePicker.registerCarrierNumberEditText(editTextCarrierNumber);

        sendotp.setOnClickListener(view -> {
            if (!editTextCarrierNumber.getText().toString().isEmpty()){
                Toast.makeText(this, "Otp Clicked", Toast.LENGTH_SHORT).show();
                SendOtp(countryCodePicker.getFormattedFullNumber());
            }
        });

    }

    private void SendOtp(String PhoneNo) {
        this.PhoneNo = PhoneNo;
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(AddNumber.this,"Otp verified",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(AddNumber.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                startActivityForResult(new Intent(AddNumber.this, OtpVerificationActivity.class),10001);
               // otpListner.postOtpSent(verificationId, token);
            }

        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(PhoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)  //(Activity)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001){

            credential = PhoneAuthProvider.getCredential(mVerificationId, data.getAction());
            isNoChanged = true;
            ChangeNumber changeNumber = new ChangeNumber(true, credential, PhoneNo);
            //postOtpSentListner(true , credential);
            setResult(1009,getIntent().putExtra("no_verification",changeNumber));
           // activityHandler.postOtpSentListner(true,credential);
        }else {
            Toast.makeText(this, "OOps error", Toast.LENGTH_SHORT).show();
            isNoChanged = false;
            ChangeNumber changeNumber = new ChangeNumber(true, credential, PhoneNo);
            setResult(1009,getIntent().putExtra("no_verification",changeNumber));
        }
    }


}