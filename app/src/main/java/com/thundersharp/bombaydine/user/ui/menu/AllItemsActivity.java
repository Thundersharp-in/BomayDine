package com.thundersharp.bombaydine.user.ui.menu;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
import com.thundersharp.bombaydine.user.core.Model.FoodItemAdapter;
import com.thundersharp.bombaydine.user.core.Model.OfferModel;
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
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.bombaydine.user.ui.orders.ConfirmPhoneName;
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
        PaymentResultWithDataListener{

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
    private TextView itemtotal, delehevry, grandtot, promoamt, promo;
    String code;
    Double upto, promo_percent;
    RelativeLayout promo_line;
    private AppCompatButton pay;
    Chip veg, non_veg, egg;
    ChipGroup chip_group;

    public static List<Object> staticAllItemsData = new ArrayList<>();
    public static List<Object> staticAllItemsRecomended = new ArrayList<>();

    private OrederBasicDetails orederBasicDetails;
    String PromoCode = "";
    Integer offerType;
    Double wallet_amount, codeAmount;
    OfferModel offerModel;
    TextView delevering_to_address, est_time, name_phone ;

    private AlertDialog.Builder builder;
    private Dialog dialog;

    HashMap<String,Boolean> checkeditem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this, this, this);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        sharedPrefHelper = new SharedPrefHelper(this, null);

        checkeditem = new HashMap<>();
        checkeditem.put("0",false);
        checkeditem.put("1",false);
        checkeditem.put("2",false);

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

        veg = findViewById(R.id.veg);
        non_veg = findViewById(R.id.non_veg);
        egg = findViewById(R.id.egg);

        veg.setOnCheckedChangeListener((compoundButton, b) -> {
            checkeditem.put("0",b);
            sortCurrentData(checkeditem);
        });


        egg.setOnCheckedChangeListener((compoundButton, b) -> {
            checkeditem.put("2",b);
            sortCurrentData(checkeditem);
        });

        non_veg.setOnCheckedChangeListener((compoundButton, b) -> {
            checkeditem.put("1",b);
            sortCurrentData(checkeditem);
        });


        bottomholder.setVisibility(View.INVISIBLE);

        //processing dilog
        builder = new AlertDialog.Builder(this);
        View dialogview = LayoutInflater.from(this).inflate(R.layout.progress_dialog_admin,null,false);
        builder.setView(dialogview);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        recomended.setHasFixedSize(true);

        bottomholder.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                showCart();
            else {
                Toast.makeText(this, "Login to place order ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));

            }
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
                if (allItemAdapterMailAdapter != null)
                    allItemAdapterMailAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter("updated"));
    }


    private synchronized void sortCurrentData(HashMap<String, Boolean> checkeditem) {

        if (checkeditem.get("0") && checkeditem.get("1") && checkeditem.get("2")){
            refreshAdapter();
            recomended.setVisibility(View.GONE);

        }else if (checkeditem.get("0") && checkeditem.get("1")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 0 ||((FoodItemAdapter)o).getFOOD_TYPE() == 1){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);


        }else if (checkeditem.get("1") && checkeditem.get("2")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 1 ||((FoodItemAdapter)o).getFOOD_TYPE() == 2){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);

        }else if (checkeditem.get("0") && checkeditem.get("2")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 0 ||((FoodItemAdapter)o).getFOOD_TYPE() == 2){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);

        } else if (checkeditem.get("0")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 0){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);

        }else if (checkeditem.get("1")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 1){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);

        }else if (checkeditem.get("2")){

            List<Object> data = new ArrayList<>();

            for (Object o : staticAllItemsData){

                if (((FoodItemAdapter)o).getFOOD_TYPE() == 2){
                    data.add(o);
                }
            }

            recomended.setVisibility(View.GONE);
            refreshAdapter(data);

        }else {
            refreshAdapter();
            recomended.setVisibility(View.VISIBLE);
        }

    }

    private void showCart() {

        View bottomview = LayoutInflater.from(this).inflate(R.layout.botomsheet_cart, findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        rec1 = bottomview.findViewById(R.id.rec1);
        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()), this, 2));

        TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
        name_phone = bottomview.findViewById(R.id.name_phone);
        TextView changeName = bottomview.findViewById(R.id.change_Name);
        TextView change_address = bottomview.findViewById(R.id.ch_address);
        RelativeLayout cod = bottomview.findViewById(R.id.cod);
        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);
        promo = bottomview.findViewById(R.id.promo);
        promo_line = bottomview.findViewById(R.id.promo_line);
        est_time = bottomview.findViewById(R.id.est_time);

        promo_line.setVisibility(View.GONE);

        changeName.setOnClickListener(vv -> startActivityForResult(new Intent(AllItemsActivity.this, ConfirmPhoneName.class), 1008));

        change_address.setOnClickListener(change->{
            Intent intent = new Intent(AllItemsActivity.this, AddAddressActivity.class);
            intent.putExtra("fetch",true);
            startActivityForResult(intent,200);
            delevering_to_address.setText("Delivering to :" + sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        });

        if (sharedPrefHelper != null) {
            if (sharedPrefHelper.getNamePhoneData().getName().isEmpty() || sharedPrefHelper.getNamePhoneData().getPhone().isEmpty()) {
                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null || !FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty() || FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null) {
                    name_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "$&" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    sharedPrefHelper.saveNamePhoneData(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                } else {
                    name_phone.setText("Update your phone no in profile first .");
                    pay.setEnabled(false);
                }

            } else {
                name_phone.setText(sharedPrefHelper.getNamePhoneData().getName() + "$&" + sharedPrefHelper.getNamePhoneData().getPhone());
            }
        }

        List<CartItemModel> data = updateCartData();

        delevering_to_address.setText("Delivering to :" + sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(this, AllOffersActivity.class), 001));

        /*
        cod.setOnClickListener(vi->{
            dialog.show();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1().equals("Choose Location")){
                    dialog.dismiss();
                    Toast.makeText(AllItemsActivity.this, "Please the location before order confirmation!", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                if (offerModel!=null){
                    PromoCode = offerModel.getCODE()+"$"+offerModel.getTYPE()+"#"+codeAmount;
                }else {
                    PromoCode = "";
                }

                String name_no = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "," + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                if (name_phone.getText().toString().isEmpty()){
                    name_no = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "," + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                }else name_no = name_phone.getText().toString();

                orederBasicDetails = new OrederBasicDetails(
                        sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1(),
                        sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG(),
                        PromoCode,
                        stringBuilder.toString(),
                        delehevry.getText().toString().replace("\u20B9", ""),
                        grandtot.getText().toString().replace("\u20B9", ""),
                        name_no,
                        String.valueOf(System.currentTimeMillis()),
                        "");

                Resturant.isOpen(new com.thundersharp.conversation.utils.Resturant.Resturantopen() {
                    @Override
                    public void isOpen(boolean isOpen) {
                        if (isOpen) {
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
                                            dialog.dismiss();
                                            Toast.makeText(AllItemsActivity.this, "Payment cannot be initialized cause :" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).setOrderToDatabase(data, orederBasicDetails);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(AllItemsActivity.this, "Resturant not open", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                dialog.dismiss();
                Toast.makeText(AllItemsActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AllItemsActivity.this, LoginActivity.class));
            }

        });
         */

        pay.setOnClickListener(view -> {
            dialog.show();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1().equals("Choose Location")){
                    dialog.dismiss();
                    Toast.makeText(AllItemsActivity.this, "Please the location before payment confirmation!", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                if (offerModel!=null){
                    PromoCode = offerModel.getCODE()+"$"+offerModel.getTYPE()+"#"+codeAmount;
                }else {
                    PromoCode = "";
                }

                String name_no = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "$&" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                if (name_phone.getText().toString().isEmpty()){
                    name_no = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "$&" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                }else name_no = name_phone.getText().toString();

                orederBasicDetails = new OrederBasicDetails(
                        sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1(),
                        sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG(),
                        PromoCode,
                        stringBuilder.toString(),
                        delehevry.getText().toString().replace("\u20B9", ""),
                        grandtot.getText().toString().replace("\u20B9", ""),
                        name_no,
                        String.valueOf(System.currentTimeMillis()),
                        "");

                Resturant.isOpen(new com.thundersharp.conversation.utils.Resturant.Resturantopen() {
                    @Override
                    public void isOpen(boolean isOpen) {
                        if (isOpen) {
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
                                            dialog.dismiss();
                                            Toast.makeText(AllItemsActivity.this, "Payment cannot be initialized cause :" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).setOrderToDatabase(data, orederBasicDetails);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(AllItemsActivity.this, "Resturant not open", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                dialog.dismiss();
                Toast.makeText(AllItemsActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AllItemsActivity.this, LoginActivity.class));
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
                                .convertLatLongToDistance(Resturant.resturantLatLong,
                                        LatLongConverter
                                                .initialize()
                                                .getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG()))) *
                                Resturant.deliveryChargesPerKilometer;

                if (deleveryCharges > Resturant.maxDeliveryCharges) {
                    deleveryCharges = Resturant.maxDeliveryCharges;
                }

                est_time.setText("Delivery in : "+(int)Math.round(Resturant.averagePreperationTime+60*(DistanceFromCoordinates.getInstance().convertLatLongToDistance(Resturant.resturantLatLong, LatLongConverter.initialize().getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG())))/Resturant.averageSpaeed)+" minute");

                itemtotal.setText("\u20B9 " + sum);
                delehevry.setText("\u20B9 " + Math.round(deleveryCharges));
                promo.setText("Promo - (" + code + ")");

                if (offerModel!= null){
                    if (sum >= offerModel.getMINCART()){
                        if (offerType == 2){
                            Double amt ;
                            if (((sum*promo_percent)/100) > upto){
                                if (sum>upto){
                                    amt=upto;
                                } else{
                                    amt = sum;
                                }
                            }else amt = (sum*promo_percent)/100;
                            promo.setText("Promo - (" + offerModel.getCODE() + ")");
                            codeAmount = amt;
                            promoamt.setText("-\u20B9 "+ amt);
                            grandtot.setText(""+(sum+Math.round(deleveryCharges)-Math.round(amt)));
                            pay.setText("PAY \u20B9" +grandtot.getText().toString());
                            promo_line.setVisibility(View.VISIBLE);
                        }else {
                            Double amt ;
                            if (((sum*promo_percent)/100) > upto){
                                if (sum>upto){
                                    amt=upto;
                                } else{
                                    amt = sum;
                                }
                            }else amt = (sum*promo_percent)/100;
                            wallet_amount = amt;
                            promo_line.setVisibility(View.GONE);
                            promoamt.setText("-\u20B9 "+ 0);
                            grandtot.setText("" + (sum + Math.round(deleveryCharges)));
                            pay.setText("PAY \u20B9" + (sum + Math.round(deleveryCharges)));
                        }
                    }else {
                        offerModel = null;
                        promo_line.setVisibility(View.GONE);
                    }

                    /*
                     Double amt ;

                        if (((sum*promo_percent)/100) > upto){
                            if (sum>upto) amt=upto; else amt = sum;
                        }else amt = (sum*promo_percent)/100;
                        promoamt.setText("-\u20B9 "+ amt);
                        grandtot.setText("" + (sum + Math.round(deleveryCharges)-amt));
                        pay.setText("PAY \u20B9" + (sum + Math.round(deleveryCharges)-amt));
                     */


                }else {
                    promo_line.setVisibility(View.GONE);
                    promoamt.setText("-\u20B9 "+ 0);
                    grandtot.setText("" + (sum + Math.round(deleveryCharges)));
                    pay.setText("PAY \u20B9" + (sum + Math.round(deleveryCharges)));
                }

            } else {
                itemtotal.setText("\u20B9 0");
                delehevry.setText("\u20B9 0");
                codeAmount = 0.0;
                promoamt.setText("\u20B9 0");
                grandtot.setText("\u20B9 0");
                pay.setText("PAY \u20B9 0");
                promoamt.setText("-\u20B9 "+ 0);
                promo_line.setVisibility(View.GONE);
            }
        }

        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 001) {
            offerModel = (OfferModel) data.getSerializableExtra("code_name");
            if (offerModel != null){
                promo_line.setVisibility(View.VISIBLE);

                //String codeAmount = data.getStringExtra("code_name");
                code = offerModel.getCODE();
                Double total= 0.0;
                try {
                    promo_percent = Double.parseDouble(offerModel.getPERCENT().toString());
                    upto = Double.parseDouble(offerModel.getUPTO().toString());
                    total = Double.parseDouble(itemtotal.getText().toString().replace("\u20B9 ",""));
                }catch (NumberFormatException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                updateOfferData(offerModel,total);

            }

        }else if (resultCode == 200){
            delevering_to_address.setText("Delivering to :" + data.getData());

            double deleveryCharges =
                    (DistanceFromCoordinates
                            .getInstance()
                            .convertLatLongToDistance(Resturant.resturantLatLong,
                                    LatLongConverter
                                            .initialize()
                                            .getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG()))) *
                            Resturant.deliveryChargesPerKilometer;

            if (deleveryCharges > Resturant.maxDeliveryCharges) {
                deleveryCharges = Resturant.maxDeliveryCharges;
            }
            delehevry.setText("\u20B9 " + Math.round(deleveryCharges));

            est_time.setText("Delivery in : "+(int)Math.round(Resturant.averagePreperationTime+60*(DistanceFromCoordinates.getInstance().convertLatLongToDistance(Resturant.resturantLatLong, LatLongConverter.initialize().getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG())))/Resturant.averageSpaeed)+" minute");

            Double total= 0.0;
            try {
                total = Double.parseDouble(itemtotal.getText().toString().replace("\u20B9 ",""));
            }catch (NumberFormatException e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            updateOfferData(offerModel,total);
            //grandtot.setText("" + (total + Math.round(deleveryCharges)));
            //pay.setText("PAY \u20B9" + (total + Math.round(deleveryCharges)));

        }else if (resultCode == 1008){
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (data.getData()!=null){
                    name_phone.setText(String.valueOf(data.getData()));
                }else Toast.makeText(this, "Data fetch failure!", Toast.LENGTH_SHORT).show();//else name_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"+91 XXXXXXXXXX");
            }else Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();


            // offerModel = null;

           // Double total = Double.parseDouble(itemtotal.getText().toString().replace("\u20B9 ",""));
           // updateOfferData(offerModel,total);
            //Toast.makeText(this, "Code Not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateOfferData(OfferModel offerModel, Double total) {
        if (offerModel!= null){
            if (total >= offerModel.getMINCART()){
                promo.setText("Promo - (" + offerModel.getCODE() + ")");
                offerType = offerModel.getTYPE();
                /*
                switch (offerModel.getTYPE()){
                    case 0:
                        Toast.makeText(this, "Hurrey ! after order placed coins will be transfered to your wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(this, "Hurrey ! after order payment you can receive cashback ", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(this, "Hurrey ! You received instant discount", Toast.LENGTH_SHORT).show();
                        break;
                }
                 */
                if (offerType == 2){
                    promo_line.setVisibility(View.VISIBLE);
                    Double amt ;
                    if (((total*promo_percent)/100) > upto){
                        if (total>upto){
                            amt=upto;
                        } else{
                            amt = total;
                        }
                    }else amt = (total*promo_percent)/100;
                    promoamt.setText("-\u20B9 "+ amt);
                    codeAmount = amt;
                    grandtot.setText(""+(total+Double.parseDouble(delehevry.getText().toString().replace("\u20B9 ",""))-amt));
                    pay.setText("PAY \u20B9" +grandtot.getText().toString());

                }else {
                    Double amt ;
                    if (((total*promo_percent)/100) > upto){
                        if (total>upto){
                            amt=upto;
                        } else{
                            amt = total;
                        }
                    }else amt = (total*promo_percent)/100;
                    wallet_amount = amt;
                    codeAmount = amt;
                    promo_line.setVisibility(View.GONE);

                    grandtot.setText(""+(total+Double.parseDouble(delehevry.getText().toString().replace("\u20B9 ",""))));
                    pay.setText("PAY \u20B9" +grandtot.getText().toString());
                    Toast.makeText(this, "wallet "+wallet_amount, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(this, "Code Applied!", Toast.LENGTH_SHORT).show();
            }else {
                this.offerModel = null;
                promo_line.setVisibility(View.GONE);
                Toast.makeText(this, "Code can't be applied add more items to avail offer", Toast.LENGTH_SHORT).show();
            }

        }else {
            //code = null; promo_percent = 0.0; upto = 0.0;
            //grandtot.setText(""+(total+Double.parseDouble(delehevry.getText().toString().replace("\u20B9 ",""))));
            //pay.setText("PAY \u20B9" +grandtot.getText().toString());
            //promo_line.setVisibility(View.GONE);

        }
    }

    private void refreshAdapter(List<Object> data) {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data, this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);
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
        dialog.dismiss();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        if (s.contains("pay_")) {
            //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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
                                dialog.dismiss();
                                //TODO UPDATE DIALOG BOX LOGIC AND CART EMPTY LOGIC
                                cartProvider.clearCart();

                                Toast.makeText(AllItemsActivity.this, "Order placed", Toast.LENGTH_LONG).show();
                                orederBasicDetails.setStatus("1");
                                orederBasicDetails.setPaymentid(s);
                                OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                                //sendBroadcast(new Intent("updated"));
                                finish();
                            } else {
                                //TODO UPDATE AUTO REFUND LOGIC
                                dialog.dismiss();
                                Toast.makeText(AllItemsActivity.this, "Could not update order contact support for your refund if not generated automatically", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(AllItemsActivity.this, "Payment failed! ", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
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
                                    dialog.dismiss();
                                    OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                                    //textupdate.setText("Payment Failed please re order !!");
                                    finish();
                                }
                            }
                        });
            } else {
                cartProvider.clearCart();
                dialog.dismiss();
                sendBroadcast(new Intent("updated"));
                OrderStatus.showOrderStatus(AllItemsActivity.this, orederBasicDetails);
                Toast.makeText(this, "Payment failed : " + jsonObject.getJSONObject("error").getString("code"), Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

}