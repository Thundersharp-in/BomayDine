package com.thundersharp.bombaydine.user.ui.offers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.Model.OfferModel;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.bombaydine.user.core.offer.OfferCode;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class AllOffersActivity extends AppCompatActivity implements OfferListner.getOfferListner , OfferCode {

    private RecyclerView re;
    private EditText code_editor;
    private TextView apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        re =findViewById(R.id.recycler);
        code_editor = findViewById(R.id.code_editor);
        apply = findViewById(R.id.apply);

        OffersProvider
                .initializeOffersProvider()
                .setOfferCount(0)
                .setGetOfferListner(this)
                .fetchAllOffers();

        apply.setOnClickListener(L ->{
            if (!code_editor.getText().toString().isEmpty()) {
                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                        .child(code_editor.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){

                                }else {
                                    code_editor.setError("Promo code in valid");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

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
    public void getOfferCode(OfferModel code) {
        setResult(001,getIntent().putExtra("code_name",code));
        finish();
    }
}