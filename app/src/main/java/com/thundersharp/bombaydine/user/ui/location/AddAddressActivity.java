package com.thundersharp.bombaydine.user.ui.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;

public class AddAddressActivity extends AppCompatActivity {

    TextView add_new_location, view_detail, add_more_location;
    RecyclerView rv_location_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);

        add_new_location = findViewById(R.id.add_new_location);
        add_more_location = findViewById(R.id.add_more_location);
        view_detail = findViewById(R.id.view_detail);
        rv_location_history = findViewById(R.id.rv_location_history);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
            ((TextView)findViewById(R.id.location_type)).setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME());
            ((TextView)findViewById(R.id.address_full)).setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1()+" "+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE2());
        }

        ((AppCompatButton)findViewById(R.id.drop_choose)).setOnClickListener(v ->{
            //TODO LOCATION MANAGER
        });
        ((AppCompatButton)findViewById(R.id.edit_location)).setOnClickListener(v ->{
            //TODO LOCATION MANAGER
        });

    }
}