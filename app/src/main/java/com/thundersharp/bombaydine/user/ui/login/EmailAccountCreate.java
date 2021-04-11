package com.thundersharp.bombaydine.user.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.FirebaseLoginClient;

public class EmailAccountCreate extends AppCompatActivity implements FirebaseLoginClient.registerSucessFailureListner {

    private LinearLayout emaillogin;
    private TextView name,email,password,confpassword;
    private AppCompatButton register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account_create);

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

            }else if (email.getText().toString().isEmpty()){

            }else if(!password.getText().toString().equals(confpassword.getText().toString())){

            }else {

            }
        });

        
    }

    @Override
    public void onRegisterSucessListner(Task<AuthResult> task,boolean isDataRegisteredToDatabase) {

    }

    @Override
    public void onRegisterFailureListner(Exception exception) {

    }
}