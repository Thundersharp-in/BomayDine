package com.thundersharp.bombaydine.user.ui.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.PaymentData;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Adapters.CartItemAdapter;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.cart.CartEmptyUpdater;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.location.DistanceFromCoordinates;
import com.thundersharp.bombaydine.user.core.utils.LatLongConverter;
import com.thundersharp.bombaydine.user.core.utils.ResturantCoordinates;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.payments.payments.PaymentObserver;
import com.thundersharp.payments.payments.Payments;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity implements
        HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure{

    private  SharedPrefHelper sharedPrefHelper;
    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;
    private RecyclerView offerscroll;
    private ShimmerFrameLayout shl;
    private RelativeLayout bottomholder;
    private OfflineDataProvider offlineDataProvider;
    private CartProvider cartProvider;
    private TextView noofItems,totalamt;
    private ImageView filter;
    boolean isfilerOpen = false;
    private LinearLayout radiogroup;
    public static BottomSheetDialog bottomSheetDialog;
    private RecyclerView rec1;


    private TextView itemtotal,delehevry,grandtot,promoamt;
    private AppCompatButton pay;

    public static List<Object> staticAllItemsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this,this,this);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        sharedPrefHelper = new SharedPrefHelper(this,null);

        offlineDataProvider = OfflineDataProvider.getInstance(this);
        recyclermain = findViewById(R.id.recyclermain);
        offerscroll = findViewById(R.id.offerscroll);
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
        bottomholder.setVisibility(View.INVISIBLE);

        bottomholder.setOnClickListener(view -> {
            showCart();
        });

        CartEmptyUpdater
                .initializeCartUpdater()
                .setOnCartEmptyListener(new CartHandler.OnCartEmpty() {
            @Override
            public void OnCartEmptyListener() {
                if (bottomSheetDialog != null){
                    if (bottomSheetDialog.isShowing()){
                        bottomSheetDialog.hide();
                    }
                }
            }
        });
        homeDataProvider.fetchAllitems();

        filter.setOnClickListener(view -> {
            if (isfilerOpen){
                isfilerOpen = false;
                radiogroup.setVisibility(View.GONE);

            }else {
                isfilerOpen = true;
                radiogroup.setVisibility(View.VISIBLE);

            }
        });

        if (offlineDataProvider.doSharedPrefExists()){
            String dataraw = offlineDataProvider.fetchitemfromStorage();
            if (!dataraw.equalsIgnoreCase("[]")){

                List<CartItemModel> data = offlineDataProvider.returnDataFromString(dataraw);

                double totalamount = 0.0;
                for (int i = 0; i<data.size(); i++){
                    if (data.get(i).getQUANTITY() > 1){
                        totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                    }else totalamount = totalamount+data.get(i).getAMOUNT();
                }
                totalamt.setText("Rs. "+totalamount+" plus taxes");

                noofItems.setText(data.size()+" ITEMS");

                Animator.initializeAnimator().slideUp(bottomholder);

            }else {
                offlineDataProvider.clearSharedPref();
            }
        }


        

        OffersProvider
                .initializeOffersProvider()
                .setOfferCount(5)
                .setGetOfferListner(new OfferListner.getOfferListner() {
                    @Override
                    public void OnGetOfferSuccess(List<Object> data) {
                        offerscroll.setAdapter(AllOfferAdapters.getInstance(AllItemsActivity.this,data,0));
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

                if (data == null || data.isEmpty()){
                    Animator.initializeAnimator().slideDown(bottomholder);

                }else {

                    for (int i = 0; i<data.size(); i++){
                        if (data.get(i).getQUANTITY() > 1){
                            totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                        }else totalamount = totalamount+data.get(i).getAMOUNT();
                    }
                    totalamt.setText("Rs. "+totalamount+" plus taxes");

                    noofItems.setText(data.size()+" ITEMS");
                    if (bottomholder.getVisibility() == View.GONE || bottomholder.getVisibility() ==View.INVISIBLE){
                        Animator.initializeAnimator().slideUp(bottomholder);
                    }
                }
            }

            @Override
            public void addFailure(Exception exception) {
                Toast.makeText(AllItemsActivity.this, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


       registerReceiver(broadcastReceiver,new IntentFilter("updated"));
    }

    private void showCart() {

        View bottomview = LayoutInflater.from(this).inflate(R.layout.botomsheet_cart, findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        rec1 = bottomview.findViewById(R.id.rec1);
        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()),this,2));

        TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        TextView delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);
        updateCartData();

        delevering_to_address.setText("Delivering to :"+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());
        shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(this, AllOffersActivity.class),001));
        pay.setOnClickListener(view -> {
            Payments.initialize(this)
                    .startPayment("Test1",""+System.currentTimeMillis(),500,"support@thundersharp.in","7301694135")
                    .attachObserver(new PaymentObserver() {
                @Override
                public void OnPaymentSuccess(String s, PaymentData paymentData) {

                }

                @Override
                public void OnPaymentFailed(int i, String s, PaymentData paymentData) {
                    Toast.makeText(AllItemsActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                }
            });
        });

        bottomSheetDialog.show();
    }


    private void updateCartData(){
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
                pay.setText("PAY \u20B9"+(sum+Math.round(deleveryCharges)));

            } else {
                itemtotal.setText("\u20B9 0");
                delehevry.setText("\u20B9 0");
                promoamt.setText("\u20B9 0");
                grandtot.setText("\u20B9 0");
                pay.setText("PAY \u20B9 0");
            }
        }
    }

    private void refreshAdapter() {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(staticAllItemsData,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);
    }

    BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
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
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);
        Animator
                .initializeAnimator()
                .runRecyclerSlideRightAnimation(recyclermain);
        shl.stopShimmer();
        shl.setVisibility(View.GONE);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }



}