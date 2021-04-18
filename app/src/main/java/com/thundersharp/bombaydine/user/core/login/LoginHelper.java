package com.thundersharp.bombaydine.user.core.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.messaging.Constants.TAG;

public class LoginHelper implements
        FirebaseLoginClient.loginContract,
        FirebaseLoginClient.ActivityHandler,
        FirebaseLoginClient.registerContract{

    private Context context;
    private FirebaseLoginClient.loginFailureListner failureListner;
    private FirebaseLoginClient.loginSucessListner loginSucessListner;
    private FirebaseLoginClient.otpListner otpListner;


    /**
     * Parameters required for phone auth callbacks
     */
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private FirebaseLoginClient.registerSucessFailureListner sucessFailureListner;


    public LoginHelper(Context context, FirebaseLoginClient.registerSucessFailureListner sucessFailureListner) {
        this.context = context;
        this.sucessFailureListner = sucessFailureListner;
    }

    /**
     * Call this constructor if using otp login with success and failure Listeners
     * @param context
     * @param failureListner
     * @param loginSucessListner
     * @param otpListner
     */

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

    public LoginHelper(Context context, FirebaseLoginClient.loginFailureListner failureListner, FirebaseLoginClient.loginSucessListner loginSucessListner) {
        this.context = context;
        this.failureListner = failureListner;
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


    }


    @Override
    public void loginwithfirebase(String email, String passWord) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email,passWord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) setLoginSucessListner(task,true,true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                throwfailure(e,2);
            }
        });
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

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                    .child("UID")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                setLoginSucessListner(task,true,true);
                                            }else {
                                                HashMap<String,Object> data = new HashMap<>();
                                                data.put("NAME",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                data.put("EMAIL",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                data.put("PHONE",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                                data.put("PROFILEPICURI",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
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
                                                                setLoginSucessListner(task,true,true);
                                                            }

                                                        }).addOnFailureListener(e -> {
                                                            setLoginSucessListner(task,false,false);
                                                            throwfailure(e,0);
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

      
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

    private void setLoginSucessListner(Task<AuthResult> task, boolean isDataRegisteredToDatabase, boolean isDataExists){
        loginSucessListner.setOnLoginSucessListner(task,isDataRegisteredToDatabase,isDataExists);
    }

    private void setOnRegisterSucess(Task<AuthResult> task,boolean isDataRegisteredToDatabase){
        sucessFailureListner.onRegisterSucessListner(task,isDataRegisteredToDatabase);
    }

    private void setOnRegisterFailure(Exception e){
        sucessFailureListner.onRegisterFailureListner(e);
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
                            //setLoginSucessListner(task,true,true);

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                    .child("UID")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                setLoginSucessListner(task,true,true);
                                            }else {
                                                HashMap<String,Object> data = new HashMap<>();
                                                data.put("NAME",null);
                                                data.put("EMAIL",null);
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
                                                                setLoginSucessListner(task,true,true);
                                                            }

                                                        }).addOnFailureListener(e -> {
                                                            setLoginSucessListner(task,false,false);
                                                            throwfailure(e,1);
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        } else {
                            // Sign in failed, display a message and update the UI
                            throwfailure(task.getException(),1);

                        }
                    }
                });

    }

    @Override
    public void registerData(@NonNull String name, @NonNull String email, @NonNull String password) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            HashMap<String,Object> data = new HashMap<>();
                            data.put("NAME",name);
                            data.put("EMAIL",email);
                            data.put("CREATEDON",System.currentTimeMillis());
                            data.put("UID",FirebaseAuth.getInstance().getUid());

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                    .setValue(data)
                            .addOnFailureListener(e -> {
                                setOnRegisterSucess(task,false);
                                setOnRegisterFailure(e);
                            })
                            .addOnCompleteListener(task1 -> {

                                if (task1.isSuccessful()){
                                    setOnRegisterSucess(task,true);
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setOnRegisterFailure(e);
                    }
                });
    }
}
