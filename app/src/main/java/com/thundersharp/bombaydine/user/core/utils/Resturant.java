package com.thundersharp.bombaydine.user.core.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class Resturant {

    public static final LatLng resturantLatLong = new LatLng(23.659914, 86.158865);

    public static final double deliveryChargesPerKilometer = 10.0;
    public static final double maxDeliveryCharges = 100.0;


    public static final double averageSpaeed = 30.0;
    public static final int averagePreperationTime = 30;
    public static final String resturantcontact = "+91 7301694135";


    public static void isOpen(com.thundersharp.conversation.utils.Resturant.Resturantopen resturantopen) {

        FirebaseDatabase
                .getInstance()
                .getReference("RESTURANT_STATUS")
                .child("OPEN")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            resturantopen.isOpen(snapshot.getValue(Boolean.class));
                        else resturantopen.isOpen(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public interface Resturantopen{
        void isOpen(boolean isOpen);
    }
}
