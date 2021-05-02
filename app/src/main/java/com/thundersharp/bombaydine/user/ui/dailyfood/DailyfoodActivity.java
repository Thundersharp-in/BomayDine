package com.thundersharp.bombaydine.user.ui.dailyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.DailyFoodListner.DailyFood;
import com.thundersharp.bombaydine.user.core.DailyFoodListner.DailyFoodProvider;

import java.util.List;

public class DailyfoodActivity extends AppCompatActivity implements DailyFood.dailyFoodListener {


    public static void getInstance(Context context,int foodType){
        context.startActivity(new Intent(context,DailyfoodActivity.class).putExtra("foodType",foodType));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyfood);

        int foodType = getIntent().getIntExtra("foodType",0);

        DailyFoodProvider
                .getReference(this,this)
                .getDailyFood(foodType);
    }

    @Override
    public void OnFoodFetchSuccess(List<Object> objects) {

    }

    @Override
    public void OnFoodFetchFailure(Exception e) {

    }
}