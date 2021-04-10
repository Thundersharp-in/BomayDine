package com.thundersharp.bombaydine.user.core.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.messaging.Constants.TAG;

public class LoginHelper implements FirebaseLoginClient.loginContract ,FirebaseLoginClient.ActivityHandler{

    private Context context;
    private FirebaseLoginClient.loginFailureListner failureListner;
    private FirebaseLoginClient.loginSucessListner loginSucessListner;
    private FirebaseLoginClient.otpListner otpListner;



    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    public LoginHelper(Context context, FirebaseLoginClient.loginFailureListner failureListner, FirebaseLoginClient.loginSucessListner loginSucessListner, FirebaseLoginClient.otpListner otpListner) {
        this.context = context;
        this.failureListner = failureListner;
        this.loginSucessListner = loginSucessListner;
        this.otpListner = otpListner;

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(context,"Otp verified",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                throwfailure(e,1);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                otpListner.postOtpSent(verificationId, token);
                LoginActivity.activityHandler = LoginHelper.this;

            }

        };

    }

    public LoginHelper(Context context, FirebaseLoginClient.loginSucessListner loginSucessListner) {
        this.context = context;
        this.loginSucessListner = loginSucessListner;
    }


    @Override
    public void loginwithfirebase(String PhoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(PhoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

/*
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(context,"Otp verified",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                throwfailure(e,1);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                otpListner.postOtpSent(verificationId, token);
                LoginActivity.activityHandler = LoginHelper.this;

            }

        };
*/




    }



    @Override
    public void loginwithfirebase(String email, boolean isPasswordless) {
        if (isPasswordless){

        }
    }

    @Override
    public void loginfirebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            setLoginSucessListner(task);
      
                        } else {
                            throwfailure(task.getException(),0);
                        }

                        // ...
                    }
                });
    }


    private void throwfailure(Exception e,int type){
        failureListner.setOnLoginFailureListner(e,type);
    }

    private void setLoginSucessListner(Task<AuthResult> task){
        loginSucessListner.setOnLoginSucessListner(task);
    }

    @Override
    public void postOtpSentListner(boolean isVerified, PhoneAuthCredential credential) {
        FirebaseAuth
                .getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            setLoginSucessListner(task);

                        } else {
                            // Sign in failed, display a message and update the UI
                            throwfailure(task.getException(),1);

                        }
                    }
                });

    }
}
