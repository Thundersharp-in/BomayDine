package com.thundersharp.bombaydine.user.ui.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.thundersharp.bombaydine.Delevery.HomeDelevery;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.HomeKitchen;
import com.thundersharp.bombaydine.user.core.Model.SavedAccountData;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.login.AccountSwitcher;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    AccountHelper accountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().getDecorView().getRootView();
        accountHelper = AccountHelper.getInstance(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (accountHelper.isSavedAdministrativeDataAvailable()){

                    accountHelper
                            .fetchSavedAdminData()
                            .setAdminDataListener(new AccountSwitcher.onGetAdministrativeData() {
                                @Override
                                public void fetchSuccess(SavedAccountData accountData) {
                                    Toast.makeText(MainActivity.this, "s", Toast.LENGTH_SHORT).show();
                                    if (accountData.getType().equals("1")) {
                                        startActivity(new Intent(MainActivity.this, HomeKitchen.class));
                                        finish();
                                    }else if (accountData.getType().equals("2")){
                                        startActivity(new Intent(MainActivity.this, HomeDelevery.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void fetchFailure() {
                                    Toast.makeText(MainActivity.this, "f", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });

                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        },2000);



    }
}