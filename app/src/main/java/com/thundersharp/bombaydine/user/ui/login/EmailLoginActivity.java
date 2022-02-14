package com.thundersharp.bombaydine.user.ui.login;

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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.FirebaseLoginClient;
import com.thundersharp.bombaydine.user.core.login.LoginHelper;
import com.thundersharp.bombaydine.user.ui.home.MainPage;

public class EmailLoginActivity extends AppCompatActivity implements FirebaseLoginClient.loginSucessListner,
        FirebaseLoginClient.loginFailureListner{

    private AlertDialog.Builder builder;
    private Dialog dialog;

    private TextView phoneback;
    private LinearLayout createemailaccount;
    private AppCompatButton login,facebok_login,google_login;
    private EditText email,passwords;


    private LoginHelper loginHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        loginHelper = new LoginHelper(this,this,this);
        phoneback = findViewById(R.id.phoneback);
        createemailaccount = findViewById(R.id.emaillogin);
        facebok_login = findViewById(R.id.fb);
        email = findViewById(R.id.email);
        passwords = findViewById(R.id.password);
        google_login = findViewById(R.id.google);
        login = findViewById(R.id.sendotp);


        builder = new AlertDialog.Builder(this);
        View dialogview = LayoutInflater.from(this).inflate(R.layout.progress_dialog_admin,null,false);
        builder.setView(dialogview);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        phoneback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this,LoginActivity.class));
                finish();
            }
        });

        createemailaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this,EmailAccountCreate.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                if (email.getText().toString().isEmpty()){
                    email.setError("Email can't be empty !");
                    email.requestFocus();
                    dialog.dismiss();
                }else if(passwords.getText().toString().isEmpty()){
                    passwords.setError("Email can't be empty !");
                    passwords.requestFocus();
                    dialog.dismiss();
                }else {
                    loginHelper.loginwithfirebase(email.getText().toString(),passwords.getText().toString());
                }
            }
        });


    }

    @Override
    public void setOnLoginFailureListner(Exception exception, int type) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void setOnLoginSucessListner(Task<AuthResult> task, boolean isDataRegisteredToDatabase, boolean isDataExists) {
        Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainPage.class));
        finish();

        dialog.dismiss();
    }
}