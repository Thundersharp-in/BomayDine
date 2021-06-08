package com.thundersharp.admin.core.address;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.utils.CONSTANTS;

public class CordinatesInteractor implements Cordinateslistner, ValueEventListener {


    Cordinateslistner.fetchSuccessListener fetchSuccessListener;

    public CordinatesInteractor(Cordinateslistner.fetchSuccessListener fetchSuccessListener) {
        this.fetchSuccessListener = fetchSuccessListener;
    }

    @Override
    public void fetchAllCoordinates() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_SERVICIABLE_AREA)
                .addListenerForSingleValueEvent(this);
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
