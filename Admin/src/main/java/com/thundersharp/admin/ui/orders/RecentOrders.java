package com.thundersharp.admin.ui.orders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.AdminModule;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMainCreateAdapter;
import com.thundersharp.admin.core.Adapters.AllOfferAdapters;
import com.thundersharp.admin.core.Adapters.CartItemAdapter;
import com.thundersharp.admin.core.Adapters.DealOfTheDayAdapter;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;
import com.thundersharp.admin.core.Data.OfferListner;
import com.thundersharp.admin.core.Data.OffersProvider;
import com.thundersharp.admin.core.Errors.ResignException;
import com.thundersharp.admin.core.Model.CartItemModel;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.admin.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.admin.core.TokenVerificationAdmin;
import com.thundersharp.admin.core.address.SharedPrefHelper;
import com.thundersharp.admin.core.animation.Animator;
import com.thundersharp.admin.core.cart.CartEmptyUpdater;
import com.thundersharp.admin.core.cart.CartHandler;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.core.location.DistanceFromCoordinates;
import com.thundersharp.admin.core.payments.PrePayment;
import com.thundersharp.admin.core.payments.parePayListener;
import com.thundersharp.admin.ui.ReauthCreateUser;
import com.thundersharp.conversation.utils.Resturant;
import com.thundersharp.payments.payments.Payments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecentOrders extends AppCompatActivity implements   HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure{

    private SharedPrefHelper sharedPrefHelper;
    private RecyclerView recyclermain;
    private AllItemAdapterMainCreateAdapter allItemAdapterMailAdapter;
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
    private String customerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders_admin);
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
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                showCart();
            else {
                Toast.makeText(this, "Login to place order ", Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(this, LoginActivity.class));

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
                        offerscroll.setAdapter(AllOfferAdapters.getInstance(RecentOrders.this, data, 0));
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
                Toast.makeText(RecentOrders.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
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

        View bottomview = LayoutInflater.from(this).inflate(R.layout.botomsheet_cart_admin, findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        rec1 = bottomview.findViewById(R.id.rec1);
        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()), this, 2));

        //TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        TextView delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
       /* TextView name_phone = bottomview.findViewById(R.id.name_phone);
        T
*/
        TextView changeName = bottomview.findViewById(R.id.change);
        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);
        TextView payable = bottomview.findViewById(R.id.payable);
        changeName.setOnClickListener(vv -> startActivityForResult(new Intent(RecentOrders.this, ReauthCreateUser.class), 1008));

/*        if (sharedPrefHelper != null) {
            if (sharedPrefHelper.getNamePhoneData().getName().isEmpty() || sharedPrefHelper.getNamePhoneData().getPhone().isEmpty()) {
                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null || !FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                    name_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "," + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    sharedPrefHelper.saveNamePhoneData(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                } else {
                    name_phone.setText("Update your phone no in profile first .");
                    pay.setEnabled(false);
                }

            } else {
                name_phone.setText(sharedPrefHelper.getNamePhoneData().getName() + "," + sharedPrefHelper.getNamePhoneData().getPhone());
            }
        }*/

        List<CartItemModel> data = updateCartData();

        //delevering_to_address.setText("Delivering to :" + sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        //shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(this, AllOffersActivity.class), 001));

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

                    Resturant.isOpen(isOpen -> {

                        if (isOpen) {
                            PrePayment
                                    .getInstance()
                                    .setDadaistListener(new parePayListener() {
                                        @Override
                                        public void addSuccess() {
                                            Payments
                                                    .initialize(RecentOrders.this)
                                                    .startPayment("ORDER #" + orederBasicDetails.getOrderID(), Double.parseDouble(orederBasicDetails.getTotalamt()), "support@thundersharp.in", "7301694135");
                                        }

                                        @Override
                                        public void addFailure(Exception exception) {
                                            Toast.makeText(RecentOrders.this, "Payment cannot be initialized cause :" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).setOrderToDatabase(data, orederBasicDetails);

                        } else
                            Toast.makeText(RecentOrders.this, "Resturant not open", Toast.LENGTH_SHORT).show();
                    });


                } else {
                    Toast.makeText(RecentOrders.this, "Log in first", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RecentOrders.this, LoginActivity.class));
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

                double deleveryCharges = 0.0;


                itemtotal.setText("\u20B9 " + sum);
                delehevry.setText("\u20B9 " + Math.round(deleveryCharges));

                grandtot.setText("" + (sum + Math.round(deleveryCharges))); //TODO SUBTRACT DISCOUNT LATER


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO UPDATE LOGIC HERE
        if (TokenVerificationAdmin.getInstance(this).reVerifyAdminCurrentToken()){
            Toast.makeText(this, "Admin Account", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Admin account tokenVerification failed retrying to resign in !", Toast.LENGTH_SHORT).show();
            try {
                AdminHelpers.getInstance(this).reSignAdmin();
            } catch (ResignException e) {
                e.printStackTrace();
                Toast.makeText(this,"Critical Error in core while ReVerifying Token !! Logging you out.",Toast.LENGTH_LONG).show();
                AdminHelpers.getInstance(this).clearAllAdminData();
                AdminModule.signOutAndRestartApp(this);
                finish();

            }


        }
    }

    private void refreshAdapter() {
        allItemAdapterMailAdapter = new AllItemAdapterMainCreateAdapter(staticAllItemsData, this);
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
        allItemAdapterMailAdapter = new AllItemAdapterMainCreateAdapter(data, this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== 1008){
            if (data.getAction() != null){

                this.customerUid = data.getAction();

            }else Toast.makeText(this,"No data received retry",Toast.LENGTH_LONG).show();

            try {
                AdminHelpers.getInstance(this).reSignAdmin();
                Toast.makeText(this, "Costumer id "+customerUid+"\nAdmin "+FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
            } catch (ResignException e) {
                e.printStackTrace();
                Toast.makeText(this,"Critical Error in core while ReVerifying Token !! Logging you out."+e.getMessage(),Toast.LENGTH_LONG).show();
                AdminHelpers.getInstance(this).clearAllAdminData();
                AdminModule.signOutAndRestartApp(this);
                finish();
            }
        }
    }


}