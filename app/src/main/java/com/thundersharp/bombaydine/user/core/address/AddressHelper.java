package com.thundersharp.bombaydine.user.core.address;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class AddressHelper implements AddressLoader {

    private Context context;
    private AddressLoader.onAddresLoadListner onAddresLoadListner;


    public AddressHelper(Context context, AddressLoader.onAddresLoadListner onAddresLoadListner) {
        this.context = context;
        this.onAddresLoadListner = onAddresLoadListner;
    }

    @Override
    public void loaduseraddress() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child(CONSTANTS.DATABASE_NODE_ADDRESS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<AddressData> addressData=new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                addressData.add(dataSnapshot.getValue(AddressData.class));
                            }

                            onAddresLoadListner.onAddressLoaded(addressData);

                        }else {
                            Exception exception = new Exception("No Data found on our servers : ERROR 404");
                            onAddresLoadListner.onAddressLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onAddresLoadListner.onAddressLoadFailure(error.toException());
                    }
                });
    }

    @Override
    public void refreshAddress() {
        loaduseraddress();
    }
}
