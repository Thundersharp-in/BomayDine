package com.thundersharp.bombaydine.user.core.DailyFoodListner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.Model.FoodItemAdapter;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class DailyFoodProvider implements DailyFood{

    private final DailyFood.dailyFoodListener dailyFoodListener;

    public DailyFoodProvider(dailyFoodListener dailyFoodListener) {
        this.dailyFoodListener = dailyFoodListener;
    }

    public static DailyFoodProvider getReference(DailyFood.dailyFoodListener dailyFoodListener){
        return new DailyFoodProvider(dailyFoodListener);
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
        List<Object> objects = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_BREAKFAST)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                objects.add(dataSnapshot.getValue(FoodItemAdapter.class));
                            }

                            dailyFoodListener.OnFoodFetchSuccess(objects);

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
        List<Object> objects = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_LUNCH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                objects.add(dataSnapshot.getValue(FoodItemAdapter.class));
                            }

                            dailyFoodListener.OnFoodFetchSuccess(objects);
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
        List<Object> objects = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_DINNER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                objects.add(dataSnapshot.getValue(FoodItemAdapter.class));
                            }

                            dailyFoodListener.OnFoodFetchSuccess(objects);
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
