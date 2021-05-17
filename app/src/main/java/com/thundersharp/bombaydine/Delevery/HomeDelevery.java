package com.thundersharp.bombaydine.Delevery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

public class HomeDelevery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delevery);

        findViewById(R.id.acccswitch).setOnClickListener(view -> {
            AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }
}