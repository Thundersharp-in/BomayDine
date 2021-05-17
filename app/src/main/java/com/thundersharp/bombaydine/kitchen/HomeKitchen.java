package com.thundersharp.bombaydine.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.model.MarkerOptions;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

public class HomeKitchen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kitchen);

        findViewById(R.id.acccswitch).setOnClickListener(view -> {

            AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        });
    }
}