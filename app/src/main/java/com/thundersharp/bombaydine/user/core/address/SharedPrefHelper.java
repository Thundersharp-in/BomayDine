package com.thundersharp.bombaydine.user.core.address;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.Model.NamePhone;
import com.thundersharp.bombaydine.user.core.utils.Resturant;

public class SharedPrefHelper implements SharedPrefUpdater{

    private Context context;
    private SharedPrefUpdater.OnSharedprefUpdated onSharedprefUpdated;
    private SharedPreferences sharedPreferences,namePhoneData;

    public SharedPrefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Homelocation",Context.MODE_PRIVATE);
        namePhoneData = context.getSharedPreferences("NamePhoneData",Context.MODE_PRIVATE);
    }

    public SharedPrefHelper(Context context, OnSharedprefUpdated onSharedprefUpdated) {
        this.context = context;
        this.onSharedprefUpdated = onSharedprefUpdated;
        sharedPreferences = context.getSharedPreferences("Homelocation",Context.MODE_PRIVATE);
    }

    @Override
    public void SaveDataToSharedPref(AddressData addressData) {
        if (addressData.getADDRESS_NICKNAME().isEmpty()){
            addressData.setADDRESS_NICKNAME("Home");
        }
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
            addressData.setLAT_LONG(sharedPreferences.getString("lat_long", Resturant.resturantLatLong.latitude+","+ Resturant.resturantLatLong.longitude));
            onSharedprefUpdated.onSharedPrefUpdate(addressData);
        }
    }

    @Override
    public AddressData getSavedHomeLocationData() {
        AddressData addressData = new AddressData();
        addressData.setADDRESS_LINE1(sharedPreferences.getString("Address","Soldevahanalli"));
        addressData.setADDRESS_NICKNAME(sharedPreferences.getString("Nickname","Home"));
        addressData.setLAT_LONG(sharedPreferences.getString("lat_long", Resturant.resturantLatLong.latitude+","+ Resturant.resturantLatLong.longitude));
        return addressData;
    }

    @Override
    public void saveNamePhoneData(String name, String phone) {
        if (namePhoneData != null){
            SharedPreferences.Editor editor = namePhoneData.edit();
            editor.putString("NAME",name);
            editor.putString("PHONE",phone);
            editor.apply();
        }
    }

    public NamePhone getNamePhoneData(){
        NamePhone namePhone = new NamePhone();
        if (namePhoneData != null){

            namePhone.setName(namePhoneData.getString("NAME", FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
            namePhone.setPhone(namePhoneData.getString("PHONE",""));
        }else {
            namePhone.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            namePhone.setPhone("");
        }

        return namePhone;
    }

    public void clearSavedHomeLocationData() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
