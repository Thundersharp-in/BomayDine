package com.thundersharp.bombaydine.user.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
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

public class EmailAccountCreate extends AppCompatActivity implements FirebaseLoginClient.registerSucessFailureListner {

    private LinearLayout emaillogin;
    private EditText name,email,password,confpassword;
    private AppCompatButton register;

    private LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account_create);

        loginHelper = new LoginHelper(this,this);
        register = findViewById(R.id.register);
        emaillogin = findViewById(R.id.emaillogin);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confpassword = findViewById(R.id.confpassword);

        emaillogin.setOnClickListener(view -> {
            finish();
        });


        register.setOnClickListener(view -> {
            if (name.getText().toString().isEmpty()){
                name.setError("Required field !");
                name.requestFocus();
            }else if (email.getText().toString().isEmpty()){
                email.setError("Required field !");
                email.requestFocus();
            }else if(!password.getText().toString().equals(confpassword.getText().toString())){
                password.setError("Passwords mismached!");
                password.requestFocus();
            }else {
                loginHelper.registerData(name.getText().toString(),email.getText().toString(),password.getText().toString());
            }
        });

        
    }

    @Override
    public void onRegisterSucessListner(Task<AuthResult> task,boolean isDataRegisteredToDatabase) {
        startActivity(new Intent(this, MainPage.class));
        finish();
        Toast.makeText(this, "User Registered , Updated to Database : "+isDataRegisteredToDatabase, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRegisterFailureListner(Exception exception) {
        Toast.makeText(this, exception.getMessage(),Toast.LENGTH_SHORT).show();
    }
}