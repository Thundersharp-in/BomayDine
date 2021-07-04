package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllPreviousAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.ui.location.AddressEdit;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateAddress extends AppCompatActivity {

    AddressHelper addressHelper;
    List<AddressData> addressDatamain;
    AllPreviousAdapter allPreviousAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_update_final);

        ((FloatingActionButton)findViewById(R.id.addAddress)).setOnClickListener(c->startActivityForResult(new Intent(this, AddressEdit.class),1078));
        addressHelper = new AddressHelper(this, new AddressLoader.onAddresLoadListner() {
            @Override
            public void onAddressLoaded(List<AddressData> addressData) {
                addressDatamain = addressData;
                allPreviousAdapter = new AllPreviousAdapter(AddUpdateAddress.this,addressData);
                ((RecyclerView)findViewById(R.id.recyclerview)).setAdapter(allPreviousAdapter);
            }

            @Override
            public void onAddressLoadFailure(Exception e) {

            }
        });

        addressHelper.loaduseraddress();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1078){
            AddressData addressData = (AddressData) data.getSerializableExtra("data");
            if (addressDatamain == null){
                List<AddressData> data1 = new ArrayList<>();
                data1.add(addressData);
                ((RecyclerView)findViewById(R.id.recyclerview)).setAdapter(new AllPreviousAdapter(AddUpdateAddress.this,data1));

            }else {
                addressDatamain.add(addressData);
                allPreviousAdapter.notifyDataSetChanged();
            }
        }
    }

}