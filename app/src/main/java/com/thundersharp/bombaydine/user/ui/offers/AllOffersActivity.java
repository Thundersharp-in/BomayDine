package com.thundersharp.bombaydine.user.ui.offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;

import java.util.List;

public class AllOffersActivity extends AppCompatActivity implements OfferListner.getOfferListner {

    RecyclerView re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        re =findViewById(R.id.recycler);

        OffersProvider
                .initializeOffersProvider()
                .setOfferCount(0)
                .setGetOfferListner(this)
                .fetchAllOffers();


    }

    @Override
    public void OnGetOfferSuccess(List<Object> data) {
        re.setAdapter(AllOfferAdapters.getInstance(this,data,1));
    }

    @Override
    public void OnOfferFetchFailure(Exception e) {

    }
}