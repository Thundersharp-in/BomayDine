package com.thundersharp.bombaydine.user.ui.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;

import java.util.List;

public class AllItemsActivity extends AppCompatActivity implements HomeDataContract.AllItems,HomeDataContract.DataLoadFailure {

    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;
    private RecyclerView offerscroll;
    private ShimmerFrameLayout shl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this,this,this);

        recyclermain = findViewById(R.id.recyclermain);
        offerscroll = findViewById(R.id.offerscroll);
        shl = findViewById(R.id.shl);
        recyclermain.setVisibility(View.GONE);
        shl.startShimmer();

        homeDataProvider.fetchAllitems();

        OffersProvider.initializeOffersProvider(new OfferListner.getOfferListner() {
            @Override
            public void OnGetOfferSuccess(List<Object> data) {
                AllOfferAdapters allOfferAdapters = AllOfferAdapters.getInstance(AllItemsActivity.this,data);
                offerscroll.setAdapter(allOfferAdapters);
            }

            @Override
            public void OnOfferFetchFailure(Exception e) {
                Toast.makeText(AllItemsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).fetchAllOffers();


    }


    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnallItemsFetchSucess(List<Object> data) {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);
        shl.stopShimmer();
        shl.setVisibility(View.GONE);
    }
}