package com.thundersharp.bombaydine.user.ui.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.vipulasri.timelineview.TimelineView;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.TimeLineAdapter;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.animation.Animator;

import java.util.ArrayList;
import java.util.List;

public class OrderStatus extends AppCompatActivity {

    public static void showOrderStatus(Context context, OrederBasicDetails orederBasicDetails){
        context.startActivity(new Intent(context,OrderStatus.class).putExtra("data",orederBasicDetails));
    }

    private OrederBasicDetails orederBasicDetails;
    private TimelineView timelineView;

    private TextView fav,unfav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        if (getIntent().getSerializableExtra("data") != null) {
            orederBasicDetails = (OrederBasicDetails)getIntent().getSerializableExtra("data");
        }else {
            Toast.makeText(this, "Error in getting details", Toast.LENGTH_SHORT).show();
            finish();
        }

        fav=findViewById(R.id.fav);
        unfav=findViewById(R.id.unfav);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setVisibility(View.GONE);
                unfav.setVisibility(View.VISIBLE);
            }
        });
        unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfav.setVisibility(View.GONE);
                fav.setVisibility(View.VISIBLE);
            }
        });

        timelineView = findViewById(R.id.timeline);
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(getdata(),Integer.parseInt(orederBasicDetails.getStatus()));
        ((RecyclerView)findViewById(R.id.recycler)).setAdapter(timeLineAdapter);


    }


    private List<String> getdata(){
       List<String> list = new ArrayList<String>();
       list.add("ORDER PLACED");
        list.add("FOOD IS BEING PREPARED");
        list.add("PICKED UP");
        list.add("DELIVERED");
        return list;

    }


}

