package com.thundersharp.admin.ui.offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllOfferAdapters;
import com.thundersharp.admin.core.Data.OfferListner;
import com.thundersharp.admin.core.Data.OffersProvider;
import com.thundersharp.admin.ui.edits.CreateOffer;

import java.util.List;

public class AllOffersActivity extends AppCompatActivity  implements OfferListner.getOfferListner {
    RecyclerView re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers_admin);
        re =findViewById(R.id.recycler);



        OffersProvider
                .initializeOffersProvider()
                .setOfferCount(0)
                .setGetOfferListner(this)
                .fetchAllOffers();

        ((FloatingActionButton)findViewById(R.id.add_offer)).setOnClickListener(view->{
            startActivity(new Intent(AllOffersActivity.this, CreateOffer.class).putExtra("edit",false));
        });

    }
    @Override
    public void OnGetOfferSuccess(List<Object> data) {
        re.setAdapter(AllOfferAdapters.getInstance(this,data,1));
    }

    @Override
    public void OnOfferFetchFailure(Exception e) {

    }
}