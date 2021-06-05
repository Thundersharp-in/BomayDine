package com.thundersharp.bombaydine.user.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.PaymentResultListener;
import com.thundersharp.billgenerator.Billing;
import com.thundersharp.billgenerator.InfoData;
import com.thundersharp.billgenerator.InvoiceGenerateObserver;
import com.thundersharp.billgenerator.InvoiceTableHolder;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.OrderItem;
import com.thundersharp.bombaydine.user.core.Adapters.TimeLineAdapter;
import com.thundersharp.bombaydine.user.core.Model.OrderModel;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.ResturantCoordinates;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;
import com.thundersharp.conversation.utils.Resturant;
import com.thundersharp.payments.payments.Payments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderStatus extends AppCompatActivity implements
        OrderDetail.OrderListner,
        InvoiceGenerateObserver, PaymentResultListener {

    private List<OrderModel> modeldatas;

    public static void showOrderStatus(Context context, OrederBasicDetails orederBasicDetails) {
        context.startActivity(new Intent(context, OrderStatus.class).putExtra("data", orederBasicDetails));
    }

    private OrederBasicDetails orederBasicDetails;
    private TimelineView timelineView;

    private TextView fav,
            unfav,
            promo_code,
            delevery_charge,
            grand_total,
            order_no,
            order_date,
            delever_address,
            order_caller_no,
            item_total,
            promo_amount,
            total_saving,
            payment_type,
            order_phone_no,
            textupdate;

    private RecyclerView recycler_dishes;
    private LinearLayout lllb;
    private List<OrderModel> model;
    private Toolbar toolbar;
    OrderDetailHelper helper;
    private LinearLayout button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getSerializableExtra("data") != null) {
            orederBasicDetails = (OrederBasicDetails) getIntent().getSerializableExtra("data");

            initializeViews();
        } else {
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

        toolbar.setNavigationOnClickListener(v ->finish());
        timelineView = findViewById(R.id.timeline);
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(getdata(), Integer.parseInt(orederBasicDetails.getStatus()));
        ((RecyclerView) findViewById(R.id.recycler)).setAdapter(timeLineAdapter);

        helper = new OrderDetailHelper(OrderStatus.this);
        setData();


        ((LinearLayout) findViewById(R.id.lllb)).setOnClickListener(view -> {
            //Toast.makeText(this, "yyy", Toast.LENGTH_SHORT).show();
            ArrayList<InvoiceTableHolder> holderArrayList = new ArrayList<>();

            for (int u = 0; u < modeldatas.size(); u++) {
                holderArrayList.add(new InvoiceTableHolder(modeldatas.get(u).getQuantity(), modeldatas.get(u).getAmount(), modeldatas.get(u).getName()));
            }
            try {
                Billing
                        .initializeBiller(OrderStatus.this)
                        .setInfoData(InfoData.setData(R.drawable.ic_launcher, "Prateek", "7301694135", orederBasicDetails.getDelivery_address(), orederBasicDetails.getOrderID(), "These are terms and Conditions .", "Welcome50", 100))
                        .attachObserver(this)
                        .createPdf(holderArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void setData() {
        delever_address.setText(orederBasicDetails.getDelivery_address());
        grand_total.setText("\u20B9 " + orederBasicDetails.getTotalamt());
        order_no.setText("#" + orederBasicDetails.getOrderID());
        order_date.setText(TimeUtils.getTimeFromTimeStamp(orederBasicDetails.getOrderID()));
        delevery_charge.setText("\u20B9" + orederBasicDetails.getDelivery_charge());
        promo_code.setText(orederBasicDetails.getPromocodeNameNdiscount());
        order_caller_no.setText("Call Resturant on : " + ResturantCoordinates.resturantcontact);
        helper.FetchOrder(orederBasicDetails.getOrderID());

        if (orederBasicDetails.getStatus().equalsIgnoreCase("0")){
            textupdate.setText("Current order status is Payment pending, Click here to retry payment within 10 minutes.");
            textupdate.setOnClickListener(v -> {
                Resturant.isOpen(new Resturant.Resturantopen() {
                    @Override
                    public void isOpen(boolean isOpen) {
                        if (isOpen) {
                            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null && FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null)
                                Payments.initialize(OrderStatus.this).startPayment("Order #" + orederBasicDetails.getOrderID(), orederBasicDetails.getOrderID(), Double.parseDouble(orederBasicDetails.getTotalamt()), FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                            else
                                Toast.makeText(OrderStatus.this, "Update phone no and email in profile", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(OrderStatus.this,"Resturant not accepting orders",Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }else if (orederBasicDetails.getStatus().equalsIgnoreCase("1")){
            textupdate.setText("Your order is being prepared click here to chat with the cook for customisations.");
            order_no.setText("#" + orederBasicDetails.getOrderID()+"\nPayment id : "+orederBasicDetails.getPaymentid());
            textupdate.setOnClickListener(c ->{

            });
        }
    }

    private void initializeViews() {
        //fav=findViewById(R.id.fav);

        item_total = findViewById(R.id.item_total);
        promo_code = findViewById(R.id.promo_code);
        promo_amount = findViewById(R.id.promo_amount);
        delevery_charge = findViewById(R.id.delevery_charge);
        grand_total = findViewById(R.id.grand_total);
        total_saving = findViewById(R.id.total_saving);
        order_no = findViewById(R.id.order_no);
        payment_type = findViewById(R.id.payment_type);
        order_date = findViewById(R.id.order_date);
        order_phone_no = findViewById(R.id.order_phone_no);
        delever_address = findViewById(R.id.delever_address);
        order_caller_no = findViewById(R.id.order_caller_no);
        recycler_dishes = findViewById(R.id.recycler_dishes);
        lllb = findViewById(R.id.lllb);
        unfav = findViewById(R.id.unfav);
        textupdate = findViewById(R.id.textupdate);
    }


    private List<String> getdata() {
        List<String> list = new ArrayList<String>();
        list.add("ORDER PLACED");
        list.add("FOOD IS BEING PREPARED");
        list.add("PICKED UP");
        list.add("DELIVERED");
        return list;

    }


    @Override
    public void onSuccess(List<OrderModel> model) {
        modeldatas = model;
        OrderItem adapter = new OrderItem(OrderStatus.this, model);
        recycler_dishes.setAdapter(adapter);
        double total = 0.0;
        for (int i = 0; i < model.size(); i++) {
            total = total + model.get(i).getAmount();
        }
        item_total.setText("\u20B9 " + String.valueOf(total));

        total_saving.setText("\u20B9" + String.valueOf(Double.parseDouble(orederBasicDetails.getTotalamt()) - (total + Double.parseDouble(orederBasicDetails.getDelivery_charge()))));
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pdfCreatedSuccess(Uri pdfLink) {
        Toast.makeText(this, ""+pdfLink, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_det_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chat) {
            ChatStarter chatStarter = ChatStarter.initializeChat(this);

            chatStarter.setSenderUid(FirebaseAuth.getInstance().getUid());
            chatStarter.setCostomerName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            chatStarter.setOrderId(orederBasicDetails.getOrderID());

            if (orederBasicDetails.getStatus().equalsIgnoreCase("3")) {
                chatStarter.setChatType(ChatStarter.MODE_CHAT_FROM_SPECIFIC_ORDER);

            } else chatStarter.setChatType(ChatStarter.MODE_CHAT_FROM_SPECIFIC_ORDER_PRE_DELIVERY);

            try {
                chatStarter.startChat();
            } catch (ParametersMissingException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPaymentSuccess(String s) {
        if (s.contains("pay_")){
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            HashMap<String,Object> updateDataRequest = new HashMap<>();
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID())+"/"+orederBasicDetails.getOrderID()+"/status","1");
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID())+"/"+orederBasicDetails.getOrderID()+"/paymentid",s);

            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+orederBasicDetails.getOrderID()+"/status","1");
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+orederBasicDetails.getOrderID()+"/paymentid",s);

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .updateChildren(updateDataRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //TODO UPDATE DIALOG BOX LOGIC
                                Toast.makeText(OrderStatus.this,"Order placed",Toast.LENGTH_LONG).show();
                                orederBasicDetails.setStatus("1");
                                orederBasicDetails.setPaymentid(s);
                                textupdate.setText("Your order is being prepared click here to chat with the cook for customisations.");
                                recreate();
                            }else {
                                //TODO UPDATE AUTO REFUND LOGIC
                                Toast.makeText(OrderStatus.this,"Could not update order contact support for your refund if not generated automatically",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        try {
            String replace = s.replace("&error=", "");
            JSONObject jsonObject = new JSONObject(replace);

            if (jsonObject.has("metadata")) {
                JSONObject metadata = jsonObject.getJSONObject("metadata");
                String payId = metadata.getString("payment_id");

                HashMap<String,Object> updateDataRequest = new HashMap<>();
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID())+"/"+orederBasicDetails.getOrderID()+"/status","4");
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID())+"/"+orederBasicDetails.getOrderID()+"/paymentid",payId);

                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+orederBasicDetails.getOrderID()+"/status","4");
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+orederBasicDetails.getOrderID()+"/paymentid",payId);

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(updateDataRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    //TODO UPDATE DIALOG BOX LOGIC
                                    Toast.makeText(OrderStatus.this,"Payment Failed.",Toast.LENGTH_LONG).show();
                                    orederBasicDetails.setStatus("4");
                                    orederBasicDetails.setPaymentid(payId);
                                    textupdate.setText("Payment Failed please re order !!");
                                    recreate();
                                }
                            }
                        });
            }
            else {
                Toast.makeText(this, "Payment failed : "+jsonObject.getJSONObject("error").getString("code"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

