package com.thundersharp.admin.core.Data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.Model.OfferModel;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class OffersProvider implements OfferListner{

    int offerCount = 0;
    static OffersProvider  offersProvider;
    private OfferListner.getOfferListner getOfferListner;

    public static OffersProvider initializeOffersProvider(){
        offersProvider = new OffersProvider();
        return offersProvider;
    }

    public OffersProvider setGetOfferListner(OfferListner.getOfferListner getOfferListner){
        this.getOfferListner = getOfferListner;
        return offersProvider;
    }

    public OffersProvider(OfferListner.getOfferListner getOfferListner) {
        this.getOfferListner = getOfferListner;
    }

    public OffersProvider setOfferCount(int count){
        this.offerCount = count;
        return offersProvider;
    }

    public OffersProvider(){}

    @Override
    public void fetchAllOffers() {
        List<Object> objects = new ArrayList<>();
        if (offerCount != 0) {
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                    .limitToFirst(offerCount)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    objects.add(dataSnapshot.getValue(OfferModel.class));
                                }
                                getOfferListner.OnGetOfferSuccess(objects);
                            } else {
                                Exception exception = new Exception("ERROR CODE : 14HJ7Y895");
                                getOfferListner.OnOfferFetchFailure(exception);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getOfferListner.OnOfferFetchFailure(error.toException());
                        }
                    });
        }else {
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    objects.add(dataSnapshot.getValue(OfferModel.class));
                                }
                                getOfferListner.OnGetOfferSuccess(objects);
                            } else {
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
}
