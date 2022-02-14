package com.thundersharp.bombaydine.user.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.thundersharp.bombaydine.user.core.login.FirebaseLoginClient;
import com.thundersharp.bombaydine.user.core.login.LoginHelper;
import com.thundersharp.bombaydine.user.ui.home.MainPage;
import com.thundersharp.bombaydine.R;

public class LoginActivity extends AppCompatActivity implements FirebaseLoginClient.loginSucessListner,
        FirebaseLoginClient.loginFailureListner,
        FirebaseLoginClient.otpListner{

    private CountryCodePicker countryCodePicker;
    EditText editTextCarrierNumber;
    private GoogleSignInClient mGoogleSignInClient;
    private AppCompatButton sendotp,facebok_login,google_login;
    private LinearLayout emaillogin;
    private TextView skip;
    private LoginHelper loginHelper;

    private String verificationId;

    public static FirebaseLoginClient.ActivityHandler activityHandler;


    private AlertDialog.Builder builder;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = new AlertDialog.Builder(this);
        View dialogview = LayoutInflater.from(this).inflate(R.layout.progress_dialog_admin,null,false);
        builder.setView(dialogview);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        loginHelper = new LoginHelper(this,this,this,this);
        countryCodePicker = findViewById(R.id.pkr);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        sendotp = findViewById(R.id.sendotp);
        emaillogin = findViewById(R.id.emaillogin);
        skip = findViewById(R.id.skip);

        countryCodePicker.registerCarrierNumberEditText(editTextCarrierNumber);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(LoginActivity.this,countryCodePicker.getFormattedFullNumber(),Toast.LENGTH_LONG).show();
                if (!editTextCarrierNumber.getText().toString().isEmpty()){
                    dialog.show();
                    loginHelper.loginwithfirebase(countryCodePicker.getFormattedFullNumber());

                }
                //startActivity(new Intent(LoginActivity.this,OtpVerificationActivity.class));
            }
        });

        facebok_login = findViewById(R.id.fb);
        google_login = findViewById(R.id.google);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainPage.class));
                finish();
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,EmailLoginActivity.class));
                finish();
            }
        });

        facebok_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginHelper.loginfirebaseAuthWithGoogle(task.getResult().getIdToken());

        }
        if (requestCode == 10001){

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, data.getAction());
            activityHandler.postOtpSentListner(true,credential);
        }
    }


    @Override
    public void postOtpSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
        this.verificationId = verificationId;
        startActivityForResult(new Intent(LoginActivity.this,OtpVerificationActivity.class),10001);

    }

    @Override
    public void setOnLoginFailureListner(Exception exception, int type) {
        if (type == 1) {
            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
            }else if (exception instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded

            }
        }
        dialog.dismiss();
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnLoginSucessListner(Task<AuthResult> task, boolean isDataRegisteredToDatabase, boolean isDataExists) {
        //Toast.makeText(this,task.getResult().getUser().getPhoneNumber(),Toast.LENGTH_SHORT).show();
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() == null){
            startActivity(new Intent(this, UserDetails.class));
        }else startActivity(new Intent(this, MainPage.class));
        finish();
        dialog.dismiss();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() == null || FirebaseAuth.getInstance().getCurrentUser().getDisplayName().isEmpty()){
                startActivity(new Intent(this, UserDetails.class));
            }else startActivity(new Intent(this, MainPage.class));
            finish();
        }
    }
}