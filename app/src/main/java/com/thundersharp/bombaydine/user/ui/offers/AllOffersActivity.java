package com.thundersharp.bombaydine.user.ui.offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.bombaydine.user.core.offer.OfferCode;

import java.util.ArrayList;
import java.util.List;

public class AllOffersActivity extends AppCompatActivity implements OfferListner.getOfferListner , OfferCode {

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
        re.setAdapter(AllOfferAdapters.getInstance(this,data,1,this));
    }

    @Override
    public void OnOfferFetchFailure(Exception e) {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getOfferCode(String code) {
        setResult(001,getIntent().putExtra("code_name",code));
        finish();
    }
}