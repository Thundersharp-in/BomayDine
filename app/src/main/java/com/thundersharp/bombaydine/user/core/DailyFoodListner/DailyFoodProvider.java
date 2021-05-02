package com.thundersharp.bombaydine.user.core.DailyFoodListner;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

public class DailyFoodProvider implements DailyFood{

    Context context;
    DailyFood.dailyFoodListener dailyFoodListener;

    public DailyFoodProvider(Context context, dailyFoodListener dailyFoodListener) {
        this.context = context;
        this.dailyFoodListener = dailyFoodListener;
    }

    public static DailyFoodProvider getReference(Context context, DailyFood.dailyFoodListener dailyFoodListener){
        return new DailyFoodProvider(context,dailyFoodListener);
    }

    @Override
    public void getDailyFood(int foodType) {
        switch (foodType){
            case 0:
                getBreakfast();
                break;
            case 1:
                getLunch();
                break;
            case 2:
                getDinner();
                break;
            default:
                Exception exception = new Exception("Incorrect Food Type use from the following:\n0 for Breakfast.\n1 for Lunch.\n2 for Dinner.");
                dailyFoodListener.OnFoodFetchFailure(exception);
                break;
        }
    }

    public void getBreakfast(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_BREAKFAST)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){



                        }else {
                            Exception exception = new Exception("ERROR 404 : NO DATA FOUND");
                            dailyFoodListener.OnFoodFetchFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dailyFoodListener.OnFoodFetchFailure(error.toException());
                    }
                });
    }

    public void getLunch(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_LUNCH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }else {
                            Exception exception = new Exception("ERROR 404 : NO DATA FOUND");
                            dailyFoodListener.OnFoodFetchFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dailyFoodListener.OnFoodFetchFailure(error.toException());
                    }
                });

    }

    public void getDinner(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_DINNER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }else {
                            Exception exception = new Exception("ERROR 404 : NO DATA FOUND");
                            dailyFoodListener.OnFoodFetchFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dailyFoodListener.OnFoodFetchFailure(error.toException());
                    }
                });

    }

}
