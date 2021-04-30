package com.thundersharp.bombaydine.user.core.Data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.Model.OfferModel;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class OffersProvider implements OfferListner{

    public static OffersProvider initializeOffersProvider(OfferListner.getOfferListner getOfferListner){
        return new OffersProvider(getOfferListner);
    }

    private OfferListner.getOfferListner getOfferListner;

    public OffersProvider(OfferListner.getOfferListner getOfferListner) {
        this.getOfferListner = getOfferListner;
    }

    @Override
    public void fetchAllOffers() {
        List<Object> objects = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                objects.add(dataSnapshot.getValue(OfferModel.class));
                            }
                            getOfferListner.OnGetOfferSuccess(objects);
                        }else {
                            Exception exception = new Exception("ERROR CODE : 14HJ7Y895");
                            getOfferListner.OnOfferFetchFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        getOfferListner.OnOfferFetchFailure(error.toException());
                    }
                });

    }
}
