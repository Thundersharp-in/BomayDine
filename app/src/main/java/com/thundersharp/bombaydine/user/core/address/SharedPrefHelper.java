package com.thundersharp.bombaydine.user.core.address;

import android.content.Context;
import android.content.SharedPreferences;

import com.thundersharp.bombaydine.user.core.Model.AddressData;

public class SharedPrefHelper implements SharedPrefUpdater{

    private Context context;
    private SharedPrefUpdater.OnSharedprefUpdated onSharedprefUpdated;
    private SharedPreferences sharedPreferences;

    public SharedPrefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Homelocation",Context.MODE_PRIVATE);
    }

    public SharedPrefHelper(Context context, OnSharedprefUpdated onSharedprefUpdated) {
        this.context = context;
        this.onSharedprefUpdated = onSharedprefUpdated;
        sharedPreferences = context.getSharedPreferences("Homelocation",Context.MODE_PRIVATE);
    }

    @Override
    public void SaveDataToSharedPref(AddressData addressData) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Address",addressData.getADDRESS_LINE1());
        editor.putString("Nickname",addressData.getADDRESS_NICKNAME());
        editor.putString("lat_long",addressData.getLAT_LONG());
        editor.apply();
        onSharedprefUpdated.onSharedPrefUpdate(addressData);
    }

    @Override
    public void updatehomelocationData() {
        if (sharedPreferences != null){
            AddressData addressData = new AddressData();
            addressData.setADDRESS_LINE1(sharedPreferences.getString("Address","Soldevahanalli"));
            addressData.setADDRESS_NICKNAME(sharedPreferences.getString("Nickname","Home"));
            addressData.setLAT_LONG(sharedPreferences.getString("lat_long",""));

            onSharedprefUpdated.onSharedPrefUpdate(addressData);
        }else {
            AddressData addressData = new AddressData();
            addressData.setADDRESS_LINE1(sharedPreferences.getString("Address","Soldevahanalli"));
            addressData.setADDRESS_NICKNAME(sharedPreferences.getString("Nickname","Home"));
            addressData.setLAT_LONG(sharedPreferences.getString("lat_long","13.0833209,77.4843393"));
            onSharedprefUpdated.onSharedPrefUpdate(addressData);
        }
    }
}
