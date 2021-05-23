package com.thundersharp.bombaydine.user.ui.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.OrderItem;
import com.thundersharp.bombaydine.user.core.Adapters.TimeLineAdapter;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.OrderModel;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.utils.ResturantCoordinates;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderStatus extends AppCompatActivity implements OrderDetail.OrderListner{

    public static void showOrderStatus(Context context, OrederBasicDetails orederBasicDetails){
        context.startActivity(new Intent(context,OrderStatus.class).putExtra("data",orederBasicDetails));
    }

    private OrederBasicDetails orederBasicDetails;
    private TimelineView timelineView;

    private TextView fav,unfav,promo_code,delevery_charge,grand_total,order_no,order_date,delever_address
            ,order_caller_no,item_total,promo_amount,total_saving,payment_type,order_phone_no;

    private RecyclerView recycler_dishes;
    private LinearLayout lllb;
    List<OrderModel> model;

    OrderDetailHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        if (getIntent().getSerializableExtra("data") != null) {
            orederBasicDetails = (OrederBasicDetails)getIntent().getSerializableExtra("data");
            initializeViews();
        }else {
            Toast.makeText(this, "Error in getting details", Toast.LENGTH_SHORT).show();
            finish();
        }

        initializeViews();


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
        
        helper=new OrderDetailHelper(OrderStatus.this);
        setData();

    }

    private void setData() {
        delever_address.setText(orederBasicDetails.getDelivery_address());
        grand_total.setText("\u20B9 "+orederBasicDetails.getTotalamt());
        order_no.setText("#"+orederBasicDetails.getOrderID());
        order_date.setText(TimeUtils.getTimeFromTimeStamp(orederBasicDetails.getOrderID()));
        delevery_charge.setText("\u20B9"+orederBasicDetails.getDelivery_charge());
        promo_code.setText(orederBasicDetails.getPromocodeNameNdiscount());
        order_caller_no.setText("Call Resturant on : "+ResturantCoordinates.resturantcontact);
        helper.FetchOrder(orederBasicDetails.getOrderID());
    }

    private void initializeViews() {
        //fav=findViewById(R.id.fav);

        item_total=findViewById(R.id.item_total);
        promo_code=findViewById(R.id.promo_code);
        promo_amount=findViewById(R.id.promo_amount);
        delevery_charge=findViewById(R.id.delevery_charge);
        grand_total=findViewById(R.id.grand_total);
        total_saving=findViewById(R.id.total_saving);
        order_no=findViewById(R.id.order_no);
        payment_type=findViewById(R.id.payment_type);
        order_date=findViewById(R.id.order_date);
        order_phone_no=findViewById(R.id.order_phone_no);
        delever_address=findViewById(R.id.delever_address);
        order_caller_no=findViewById(R.id.order_caller_no);
        recycler_dishes=findViewById(R.id.recycler_dishes);
        lllb=findViewById(R.id.lllb);
        unfav=findViewById(R.id.unfav);
    }


    private List<String> getdata(){
       List<String> list = new ArrayList<String>();
       list.add("ORDER PLACED");
        list.add("FOOD IS BEING PREPARED");
        list.add("PICKED UP");
        list.add("DELIVERED");
        return list;

    }


    @Override
    public void onSuccess(List<OrderModel> model) {
        OrderItem adapter=new OrderItem(OrderStatus.this,model);
        recycler_dishes.setAdapter(adapter);
        double total=0.0;
        for (int i=0;i<model.size();i++){
            total=total+model.get(i).getAmount();
        }
        item_total.setText("\u20B9 "+String.valueOf(total));

        total_saving.setText("\u20B9"+String.valueOf(Double.parseDouble(orederBasicDetails.getTotalamt())-(total+Double.parseDouble(orederBasicDetails.getDelivery_charge()))));
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

