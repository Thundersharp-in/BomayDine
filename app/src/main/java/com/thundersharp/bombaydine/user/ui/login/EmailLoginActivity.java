package com.thundersharp.bombaydine.user.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
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

public class EmailLoginActivity extends AppCompatActivity implements FirebaseLoginClient.loginSucessListner,
        FirebaseLoginClient.loginFailureListner{

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
                if (email.getText().toString().isEmpty()){
                    email.setError("Email can't be empty !");
                    email.requestFocus();
                }else if(passwords.getText().toString().isEmpty()){
                    passwords.setError("Email can't be empty !");
                    passwords.requestFocus();
                }else {
                    loginHelper.loginwithfirebase(email.getText().toString(),passwords.getText().toString());
                }
            }
        });


    }

    @Override
    public void setOnLoginFailureListner(Exception exception, int type) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnLoginSucessListner(Task<AuthResult> task) {
        Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
    }
}