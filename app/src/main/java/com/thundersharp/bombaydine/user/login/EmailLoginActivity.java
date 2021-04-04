package com.thundersharp.bombaydine.user.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thundersharp.bombaydine.R;

public class EmailLoginActivity extends AppCompatActivity {

    private TextView phoneback;
    private LinearLayout createemailaccount;
    private AppCompatButton login,facebok_login,google_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        phoneback = findViewById(R.id.phoneback);
        createemailaccount = findViewById(R.id.emaillogin);
        facebok_login = findViewById(R.id.fb);
        google_login = findViewById(R.id.google);
        login = findViewById(R.id.sendotp);

        phoneback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createemailaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this,EmailAccountCreate.class));
            }
        });


    }
}