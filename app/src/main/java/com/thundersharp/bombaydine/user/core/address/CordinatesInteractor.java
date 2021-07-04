package com.thundersharp.bombaydine.user.core.address;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresFeature;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.bombaydine.user.core.location.StorageFailure;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CordinatesInteractor implements Cordinateslistner, ValueEventListener {


    Cordinateslistner.fetchSuccessListener fetchSuccessListener;
    Context context;

    public CordinatesInteractor(Cordinateslistner.fetchSuccessListener fetchSuccessListener,Context context) {
        this.fetchSuccessListener = fetchSuccessListener;
        this.context = context;
    }

    @Override
    public void fetchAllCoordinates() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_SERVICIABLE_AREA)
                .addListenerForSingleValueEvent(this);
    }

    @RequiresFeature(name = "Cordinateslistner.fetchSuccessListener", enforcement = "com.thundersharp.admin.core.address.Cordinateslistner.fetchSuccessListener")
    @Override
    public void fetchAllCoordinatesFromStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("points",Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("data",null);
        Gson gson = new Gson();
        LatLng[] dataarray;
        try {
            dataarray = gson.fromJson(data, new TypeToken<LatLng[]>(){}.getType());
            fetchSuccessListener.onCordinatesSuccess(dataarray);
        }catch (Exception e){
            fetchSuccessListener.onCordinatesFailure(new StorageFailure(e.getMessage()));
        }

    }

    @Override
    public void saveCoordinatesToStorage(LatLng... coOrdinates) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("points",Context.MODE_PRIVATE);
        String data = gson.toJson(coOrdinates);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data",data);
        editor.apply();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        LatLng[] strings = new LatLng[(int)snapshot.getChildrenCount()];
        int counter = 0;
        if (snapshot.exists()){
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                strings[counter] = getlatlang(dataSnapshot.getValue(String.class));
                counter++;
            }
            fetchSuccessListener.onCordinatesSuccess(strings);
        }else {
            Exception exception = new Exception("No data found cannot draw palatines !!");
            fetchSuccessListener.onCordinatesFailure(exception);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        fetchSuccessListener.onCordinatesFailure(error.toException());
    }

    private LatLng getlatlang(String lat_long) {
        double lat = Double.parseDouble(lat_long.substring(0,lat_long.indexOf(",")));
        double longitude = Double.parseDouble(lat_long.substring(lat_long.indexOf(",")+1));
        return new LatLng(lat,longitude);
    }
}
