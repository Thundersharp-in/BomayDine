package com.thundersharp.bombaydine.user.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

public class LocationUpdater {

    Context context;

    public static LocationUpdater getInstance(Context context){
        return new LocationUpdater(context);
    }

    public LocationUpdater(Context context) {
        this.context = context;
    }

    public void saveCoOrdinates(LatLng latLng){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TableBookingCoOrdinates",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Lat",String.valueOf(latLng.latitude));
        editor.putString("Long",String.valueOf(latLng.longitude));
        editor.apply();
    }

    public LatLng getCoOrdinates(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TableBookingCoOrdinates",Context.MODE_PRIVATE);
        return new LatLng(Double.parseDouble(sharedPreferences.getString("Lat","0.0")),
                Double.parseDouble(sharedPreferences.getString("Long","0.0")));
    }
}
