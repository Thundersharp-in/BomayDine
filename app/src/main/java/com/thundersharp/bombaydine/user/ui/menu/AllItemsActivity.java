package com.thundersharp.bombaydine.user.ui.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Adapters.CartItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.DealOfTheDayAdapter;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.cart.CartEmptyUpdater;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.location.DistanceFromCoordinates;
import com.thundersharp.bombaydine.user.core.payments.PrePayment;
import com.thundersharp.bombaydine.user.core.payments.parePayListener;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.LatLongConverter;
import com.thundersharp.bombaydine.user.core.utils.ResturantCoordinates;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.bombaydine.user.ui.orders.OrderStatus;
import com.thundersharp.payments.payments.Payments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AllItemsActivity extends AppCompatActivity implements
        HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure,
        PaymentResultWithDataListener {

    private SharedPrefHelper sharedPrefHelper;
    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;
    private RecyclerView offerscroll;
    private ShimmerFrameLayout shl;
    private RelativeLayout bottomholder;
    private OfflineDataProvider offlineDataProvider;
    private CartProvider cartProvider;
    private TextView noofItems, totalamt;
    private ImageView filter;
    boolean isfilerOpen = false;
    private LinearLayout radiogroup;
    public static BottomSheetDialog bottomSheetDialog;
    private RecyclerView rec1;
    private EditText searchbar;

    private RecyclerView recomended;
    private TextView itemtotal, delehevry, grandtot, promoamt;
    private AppCompatButton pay;

    public static List<Object> staticAllItemsData = new ArrayList<>();
    public static List<Object> staticAllItemsRecomended = new ArrayList<>();

    private OrederBasicDetails orederBasicDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this, this, this);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        sharedPrefHelper = new SharedPrefHelper(this, null);

        offlineDataProvider = OfflineDataProvider.getInstance(this);
        recyclermain = findViewById(R.id.recyclermain);
        offerscroll = findViewById(R.id.offerscroll);
        searchbar = findViewById(R.id.searchbar);

        shl = findViewById(R.id.shl);
        shl.setVisibility(View.VISIBLE);
        recyclermain.setVisibility(View.GONE);
        shl.startShimmer();

        bottomholder = findViewById(R.id.bottomholder);
        noofItems = findViewById(R.id.text2);
        totalamt = findViewById(R.id.totalamt);
        filter = findViewById(R.id.filter);
        radiogroup = findViewById(R.id.chkbox);
        radiogroup.setVisibility(View.GONE);
        recomended = findViewById(R.id.recomended);

        bottomholder.setVisibility(View.INVISIBLE);

        bottomholder.setOnClickListener(view -> {
            showCart();
        });

        CartEmptyUpdater
                .initializeCartUpdater()
                .setOnCartEmptyListener(new CartHandler.OnCartEmpty() {
                    @Override
                    public void OnCartEmptyListener() {
                        if (bottomSheetDialog != null) {
                            if (bottomSheetDialog.isShowing()) {
                                bottomSheetDialog.hide();
                            }
                        }
                    }
                });
        homeDataProvider.fetchAllitems();

        filter.setOnClickListener(view -> {
            if (isfilerOpen) {
                isfilerOpen = false;
                radiogroup.setVisibility(View.GONE);

            } else {
                isfilerOpen = true;
                radiogroup.setVisibility(View.VISIBLE);

            }
        });

        if (offlineDataProvider.doSharedPrefExists()) {
            String dataraw = offlineDataProvider.fetchitemfromStorage();
            if (!dataraw.equalsIgnoreCase("[]")) {

                List<CartItemModel> data = offlineDataProvider.returnDataFromString(dataraw);

                double totalamount = 0.0;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getQUANTITY() > 1) {
                        totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                    } else totalamount = totalamount + data.get(i).getAMOUNT();
                }
                totalamt.setText("Rs. " + totalamount + " plus taxes");

                noofItems.setText(data.size() + " ITEMS");

                Animator.initializeAnimator().slideUp(bottomholder);

            } else {
                offlineDataProvider.clearSharedPref();
            }
        }


        OffersProvider
                .initializeOffersProvider()
                .setOfferCount(5)
                .setGetOfferListner(new OfferListner.getOfferListner() {
                    @Override
                    public void OnGetOfferSuccess(List<Object> data) {
                        offerscroll.setAdapter(AllOfferAdapters.getInstance(AllItemsActivity.this, data, 0));
                    }

                    @Override
                    public void OnOfferFetchFailure(Exception e) {

                    }
                }).fetchAllOffers();


        cartProvider = CartProvider.initialize(this, new CartHandler.cart() {

            @Override
            public void onItemAddSuccess(boolean isAdded, List<CartItemModel> data) {
                if (bottomSheetDialog.isShowing()) updateCartData();

                refreshAdapter();
                double totalamount = 0.0;

                if (data == null || data.isEmpty()) {
                    Animator.initializeAnimator().slideDown(bottomholder);

                } else {

                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getQUANTITY() > 1) {
                            totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                        } else totalamount = totalamount + data.get(i).getAMOUNT();
                    }
                    totalamt.setText("Rs. " + totalamount + " plus taxes");

                    noofItems.setText(data.size() + " ITEMS");
                    if (bottomholder.getVisibility() == View.GONE || bottomholder.getVisibility() == View.INVISIBLE) {
                        Animator.initializeAnimator().slideUp(bottomholder);
                    }
                }
            }

            @Override
            public void addFailure(Exception exception) {
                Toast.makeText(AllItemsActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //allItemAdapterMailAdapter.getFilter().filter(s.toString());
                //Toast.makeText(AllItemsActivity.this, "before "+ s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allItemAdapterMailAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter("updated"));
    }

    private void showCart() {

        View bottomview = LayoutInflater.from(this).inflate(R.layout.botomsheet_cart, findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        rec1 = bottomview.findViewById(R.id.rec1);
        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()), this, 2));

        TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        TextView delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);

        List<CartItemModel> data = updateCartData();

        delevering_to_address.setText("Delivering to :" + sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(this, AllOffersActivity.class), 001));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int q = 0; q < data.size(); q++) {
                        if (q == data.size() - 1) {
                            stringBuilder
                                    .append(data.get(q).getQUANTITY())
                                    .append(" X ")
                                    .append(data.get(q).getNAME());

                        } else {
                            stringBuilder
                                    .append(data.get(q).getQUANTITY())
                                    .append(" X ")
                                    .append(data.get(q).getNAME())
                                    .append(", ");
                        }
                    }
                    orederBasicDetails = new OrederBasicDetails(
                            sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1(),
                            sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG(),
                            "",
                            stringBuilder.toString(),
                            delehevry.getText().toString().replace("\u20B9", ""),
                            grandtot.getText().toString().replace("\u20B9", ""),
                            "",
                            String.valueOf(System.currentTimeMillis()),
                            "");

                    PrePayment
                            .getInstance()
                            .setDadaistListener(new parePayListener() {
                                @Override
                                public void addSuccess() {
                                    Payments
                                            .initialize(AllItemsActivity.this)
                                            .startPayment("ORDER #" + orederBasicDetails.getOrderID(), Double.parseDouble(orederBasicDetails.getTotalamt()), "support@thundersharp.in", "7301694135");

                                }

                                @Override
                                public void addFailure(Exception exception) {
                                    Toast.makeText(AllItemsActivity.this, "Payment cannot be initialized cause :" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).setOrderToDatabase(data, orederBasicDetails);
                } else {
                    Toast.makeText(AllItemsActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AllItemsActivity.this, LoginActivity.class));
                }

            }
        });


        bottomSheetDialog.show();
    }


    private List<CartItemModel> updateCartData() {
        List<CartItemModel> data = offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage());

        if (data != null) {

            if (!data.isEmpty()) {
                double sum = 0.0;
                for (int i = 0; i < data.size(); i++) {
                    sum = sum + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                }

                double deleveryCharges =
                        (DistanceFromCoordinates
                                .getInstance()
                                .convertLatLongToDistance(ResturantCoordinates.resturantLatLong,
                                        LatLongConverter
                                                .initialize()
                                                .getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG()))) *
                                ResturantCoordinates.deliveryChargesPerKilometer;

                if (deleveryCharges > ResturantCoordinates.maxDeliveryCharges) {
                    deleveryCharges = ResturantCoordinates.maxDeliveryCharges;
                }


                itemtotal.setText("\u20B9 " + sum);
                delehevry.setText("\u20B9 " + Math.round(deleveryCharges));

                grandtot.setText("" + (sum + Math.round(deleveryCharges))); //TODO SUBTRACT DISCOUNT LATER
                pay.setText("PAY \u20B9" + (sum + Math.round(deleveryCharges)));

            } else {
                itemtotal.setText("\u20B9 0");
                delehevry.setText("\u20B9 0");
                promoamt.setText("\u20B9 0");
                grandtot.setText("\u20B9 0");
                pay.setText("PAY \u20B9 0");
            }
        }

        return data;
    }

    private void refreshAdapter() {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(staticAllItemsData, this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);

        if (recomended.getVisibility() == View.VISIBLE) {
            recomended.setAdapter(new DealOfTheDayAdapter(staticAllItemsRecomended, this));
            //Animator.initializeAnimator().recyclerTooPos(recomended,DealOfTheDayAdapter.currentpos);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cartProvider.syncData();
               /* if (intent!=null){
                        allItemAdapter.notifyItemChanged(intent.getIntExtra("adapterPos",0));
                }*/
        }

    };

    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnallItemsFetchSucess(List<Object> data) {
        staticAllItemsData = data;
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data, this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);


        shl.stopShimmer();
        shl.setVisibility(View.GONE);
        setDealOfTheDay(data);
    }

    private synchronized void setDealOfTheDay(List<Object> data) {
        List<Object> list = new ArrayList<>();

        if (data.size() < 5) {
            recomended.setVisibility(View.GONE);
        } else {
            recomended.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5; i++) {
                Random random = new Random();
                Object dataselected = data.get(random.nextInt(data.size()));
                if (!list.contains(dataselected)) {
                    list.add(dataselected);
                }

            }
            recomended.setAdapter(new DealOfTheDayAdapter(list, this));
            staticAllItemsRecomended = list;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        if (s.contains("pay_")) {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            HashMap<String, Object> updateDataRequest = new HashMap<>();
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS + "/" + TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID()) + "/" + orederBasicDetails.getOrderID() + "/status", "1");
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS + "/" + TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID()) + "/" + orederBasicDetails.getOrderID() + "/paymentid", s);

            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS + "/" + FirebaseAuth.getInstance().getUid() + "/" + CONSTANTS.DATABASE_NODE_ORDERS + "/" + CONSTANTS.DATABASE_NODE_OVERVIEW + "/" + orederBasicDetails.getOrderID() + "/status", "1");
            updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS + "/" + FirebaseAuth.getInstance().getUid() + "/" + CONSTANTS.DATABASE_NODE_ORDERS + "/" + CONSTANTS.DATABASE_NODE_OVERVIEW + "/" + orederBasicDetails.getOrderID() + "/paymentid", s);

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .updateChildren(updateDataRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //TODO UPDATE DIALOG BOX LOGIC AND CART EMPTY LOGIC
                                cartProvider.clearCart();

                                Toast.makeText(AllItemsActivity.this, "Order placed", Toast.LENGTH_LONG).show();
                                orederBasicDetails.setStatus("1");
                                orederBasicDetails.setPaymentid(s);
                                OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                                sendBroadcast(new Intent("updated"));
                                finish();
                            } else {
                                //TODO UPDATE AUTO REFUND LOGIC
                                Toast.makeText(AllItemsActivity.this, "Could not update order contact support for your refund if not generated automatically", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            String replace = s.replace("&error=", "");
            JSONObject jsonObject = new JSONObject(replace);

            if (jsonObject.has("metadata")) {
                JSONObject metadata = jsonObject.getJSONObject("metadata");
                String payId = metadata.getString("payment_id");

                HashMap<String, Object> updateDataRequest = new HashMap<>();
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS + "/" + TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID()) + "/" + orederBasicDetails.getOrderID() + "/status", "4");
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS + "/" + TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID()) + "/" + orederBasicDetails.getOrderID() + "/paymentid", payId);

                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS + "/" + FirebaseAuth.getInstance().getUid() + "/" + CONSTANTS.DATABASE_NODE_ORDERS + "/" + CONSTANTS.DATABASE_NODE_OVERVIEW + "/" + orederBasicDetails.getOrderID() + "/status", "4");
                updateDataRequest.put(CONSTANTS.DATABASE_NODE_ALL_USERS + "/" + FirebaseAuth.getInstance().getUid() + "/" + CONSTANTS.DATABASE_NODE_ORDERS + "/" + CONSTANTS.DATABASE_NODE_OVERVIEW + "/" + orederBasicDetails.getOrderID() + "/paymentid", payId);

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(updateDataRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //TODO UPDATE DIALOG BOX LOGIC
                                    Toast.makeText(AllItemsActivity.this, "Payment Failed.", Toast.LENGTH_LONG).show();
                                    cartProvider.clearCart();
                                    orederBasicDetails.setStatus("4");
                                    orederBasicDetails.setPaymentid(payId);
                                    OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                                    //textupdate.setText("Payment Failed please re order !!");
                                    finish();
                                }
                            }
                        });
            } else {
                cartProvider.clearCart();
                sendBroadcast(new Intent("updated"));
                OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                Toast.makeText(this, "Payment failed : " + jsonObject.getJSONObject("error").getString("code"), Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}