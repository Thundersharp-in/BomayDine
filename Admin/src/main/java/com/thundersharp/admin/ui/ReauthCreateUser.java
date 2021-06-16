package com.thundersharp.admin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.TokenVerificationAdmin;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.passcode.OtpVerifaction;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ReauthCreateUser extends AppCompatActivity {

    AppCompatButton reAuth;
    TextInputLayout customerName, costumerPhone,adminPassword;

    private FirebaseAuth mAuth;
    private String mVerificationId;
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_user);

        mAuth = FirebaseAuth.getInstance();
        reAuth = findViewById(R.id.buttonsubmit);
        customerName = findViewById(R.id.name);
        costumerPhone = findViewById(R.id.phone);
        adminPassword = findViewById(R.id.password);

        reAuth.setOnClickListener(b->{
            if (costumerPhone.getEditText().getText().toString().isEmpty()){
                costumerPhone.setError("Costumer phone is required.");
            }else if (adminPassword.getEditText().getText().toString().isEmpty()){
                adminPassword.setError("Admin password required.");
            }else {
                String emailAdmin = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                AuthCredential authCredential = EmailAuthProvider.getCredential(emailAdmin,adminPassword.getEditText().getText().toString());
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .reauthenticate(authCredential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    TokenVerificationAdmin tokenVerificationAdmin = TokenVerificationAdmin.getInstance(ReauthCreateUser.this);
                                    tokenVerificationAdmin.saveCredentials(emailAdmin,FirebaseAuth.getInstance().getUid());
                                    tokenVerificationAdmin.setAdminPassword(adminPassword.getEditText().getText().toString());

                                    startPhoneNumberVerification(costumerPhone.getEditText().getText().toString());

                                }else
                                    Toast.makeText(ReauthCreateUser.this, "Admin account password invalid", Toast.LENGTH_SHORT).show();
                            }
                        });

                //startActivityForResult(new Intent(this, OtpVerifaction.class),10009);

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                Toast.makeText(ReauthCreateUser.this, "User account logged in", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                Toast.makeText(ReauthCreateUser.this,e.getMessage(),Toast.LENGTH_LONG).show();
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                startActivityForResult(new Intent(ReauthCreateUser.this, OtpVerifaction.class),10009);
                //todo update on result

            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){

                                                Intent intent = new Intent();
                                                intent.setAction(user.getUid());
                                                setResult(1008,intent);
                                                finish();

                                            }else {

                                                HashMap<String,Object> data = new HashMap<>();
                                                data.put("NAME",customerName.getEditText().getText().toString());
                                                data.put("PHONE",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                                data.put("PROFILEPICURI",null);
                                                data.put("CREATEDON",System.currentTimeMillis());
                                                data.put("UID",FirebaseAuth.getInstance().getUid());

                                                FirebaseDatabase
                                                        .getInstance()
                                                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                                        .setValue(data)
                                                        .addOnCompleteListener(task1 -> {

                                                            if (task1.isSuccessful()){
                                                                Intent intent = new Intent();
                                                                intent.setAction(user.getUid());
                                                                setResult(1008,intent);
                                                                finish();
                                                            }

                                                        }).addOnFailureListener(e -> {

                                                    Intent intent = new Intent();
                                                    intent.setAction(user.getUid());
                                                    setResult(1008,intent);
                                                    finish();
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            // Update UI
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ReauthCreateUser.this, "Costumer otp invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==10009){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, data.getAction());
            signInWithPhoneAuthCredential(credential);
        }
    }
}