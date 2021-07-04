package com.thundersharp.bombaydine.user.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.thundersharp.admin.AdminModule;
import com.thundersharp.bombaydine.Delevery.main.HomeDelevery;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.main.HomeKitchen;
import com.thundersharp.bombaydine.user.core.Model.SavedAccountData;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.core.login.AccountSwitcher;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements Cordinateslistner.fetchSuccessListener{

    AccountHelper accountHelper;
    CordinatesInteractor cordinatesInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountHelper = AccountHelper.getInstance(this);
        cordinatesInteractor = new CordinatesInteractor(this,this);
        cordinatesInteractor.fetchAllCoordinates();




    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        cordinatesInteractor.saveCoordinatesToStorage(coOrdinates);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (accountHelper.isSavedAdministrativeDataAvailable()){

                    accountHelper
                            .fetchSavedAdminData()
                            .setAdminDataListener(new AccountSwitcher.onGetAdministrativeData() {
                                @Override
                                public void fetchSuccess(SavedAccountData accountData) {
                                    switch (accountData.getType()) {
                                        case "1":
                                            startActivity(new Intent(MainActivity.this, HomeKitchen.class));
                                            finish();
                                            break;
                                        case "2":
                                            startActivity(new Intent(MainActivity.this, HomeDelevery.class));
                                            finish();
                                            break;
                                        case "0":

                                            AdminModule
                                                    .getInstance(MainActivity.this)
                                                    .useFirebaseServices(true)
                                                    .setSupportiveData(new Bundle())
                                                    .startAdmin();
                                            finish();
                                            break;
                                    }
                                }

                                @Override
                                public void fetchFailure() {

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

    @Override
    public void onCordinatesFailure(Exception exception) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_LONG).show();
    }
}