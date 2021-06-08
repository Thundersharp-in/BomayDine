package com.thundersharp.admin.ui.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.admin.core.Adapters.AllOfferAdapters;
import com.thundersharp.admin.core.Adapters.CartItemAdapter;
import com.thundersharp.admin.core.Adapters.DealOfTheDayAdapter;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;
import com.thundersharp.admin.core.Data.OfferListner;
import com.thundersharp.admin.core.Data.OffersProvider;
import com.thundersharp.admin.core.Model.CartItemModel;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.admin.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.admin.core.address.SharedPrefHelper;
import com.thundersharp.admin.core.animation.Animator;
import com.thundersharp.admin.core.cart.CartEmptyUpdater;
import com.thundersharp.admin.core.cart.CartHandler;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.core.location.DistanceFromCoordinates;
import com.thundersharp.admin.core.payments.PrePayment;
import com.thundersharp.admin.core.payments.parePayListener;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.core.utils.LatLongConverter;
import com.thundersharp.admin.core.utils.ResturantCoordinates;
import com.thundersharp.admin.core.utils.TimeUtils;
import com.thundersharp.admin.ui.offers.AllOffersActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.thundersharp.admin.ui.orders.OrderStatus;
import com.thundersharp.payments.payments.Payments;

public class AllItemsActivity extends AppCompatActivity implements
        HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure{

    private SharedPrefHelper sharedPrefHelper;
    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;
    private RecyclerView offerscroll;
    private ShimmerFrameLayout shl;
    private OfflineDataProvider offlineDataProvider;
    private CartProvider cartProvider;
    private TextView noofItems;
    private ImageView filter;
    boolean isfilerOpen = false;
    private LinearLayout radiogroup;
    public static BottomSheetDialog bottomSheetDialog;
    private RecyclerView rec1;
    private EditText searchbar;


    public static List<Object> staticAllItemsData = new ArrayList<>();
    public static List<Object> staticAllItemsRecomended = new ArrayList<>();

    private OrederBasicDetails orederBasicDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items_admin);
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


        noofItems = findViewById(R.id.text2);

        filter = findViewById(R.id.filter);
        radiogroup = findViewById(R.id.chkbox);
        radiogroup.setVisibility(View.GONE);



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


    }



    private void refreshAdapter() {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(staticAllItemsData, this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);

    }

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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




}