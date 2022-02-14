package com.thundersharp.admin.ui.dailyfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.admin.core.DailyFoodListner.DailyFood;
import com.thundersharp.admin.core.DailyFoodListner.DailyFoodProvider;
import com.thundersharp.admin.ui.edits.EditDailyFoodActivity;

import java.util.List;

public class DailyfoodActivity extends AppCompatActivity implements DailyFood.dailyFoodListener {

    private RecyclerView recyclerviewbrh;
    private ImageView imagedaily;
    private TextView texttop;
    private ShimmerFrameLayout shimmerbr;
    private FloatingActionButton add;

    public static void getInstance(Context context, int foodType){
        context.startActivity(new Intent(context,DailyfoodActivity.class).putExtra("foodType",foodType));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyfood_admin);
        int foodType = getIntent().getIntExtra("foodType",0);

        shimmerbr = findViewById(R.id.shimmerbr);
        shimmerbr.startShimmer();
        recyclerviewbrh = findViewById(R.id.recyclerviewbrh);
        imagedaily = findViewById(R.id.imagedaily);
        texttop = findViewById(R.id.txttop);
        add = findViewById(R.id.add);

        switch (foodType){
            case 0:
                texttop.setText("Breakfast");
                imagedaily.setImageDrawable(getDrawable(R.drawable.breakfasth));
                break;
            case 1:
                imagedaily.setImageDrawable(getDrawable(R.drawable.lunch));
                texttop.setText("Lunch");
                break;
            case 2:
                imagedaily.setImageDrawable(getDrawable(R.drawable.dinnermain));
                texttop.setText("Dinner");
                break;
            default:
                imagedaily.setImageDrawable(getDrawable(R.drawable.error));
                texttop.setText("Internal Error");
                break;
        }

        add.setOnClickListener(vi->{
            startActivity(new Intent(DailyfoodActivity.this, EditDailyFoodActivity.class)
                    .putExtra("foodType",foodType));
            finish();
        });

        DailyFoodProvider
                .getReference(this)
                .getDailyFood(foodType);
    }

    @Override
    public void OnFoodFetchSuccess(List<Object> objects) {
        recyclerviewbrh.setAdapter(AllItemAdapterMailAdapter.initializeAdapter(objects,this));
        shimmerbr.stopShimmer();
        shimmerbr.setVisibility(View.GONE);
    }

    @Override
    public void OnFoodFetchFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        shimmerbr.stopShimmer();
        shimmerbr.setVisibility(View.GONE);

    }
}