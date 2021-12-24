package com.thundersharp.admin.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.admin.core.Adapters.AllOfferAdapters;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;
import com.thundersharp.admin.core.Data.OfferListner;
import com.thundersharp.admin.core.Data.OffersProvider;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.admin.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.admin.core.address.SharedPrefHelper;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.ui.edits.EditItemActivity;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity implements
        HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure{

    FloatingActionButton add;
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

        add = findViewById(R.id.add);
        add.setOnClickListener(f-> startActivity(new Intent(this, EditItemActivity.class)));

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