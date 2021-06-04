package com.thundersharp.conversation.utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Resturant {
    public static final String RESTURANT_SUPPORT_NAME = "Bombay dine Support";
    public static final String RESTURANT_SUPPORT_ID = "SUPPORT56065";

    public static void isOpen(Resturantopen resturantopen) {

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
