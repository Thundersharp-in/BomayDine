package com.thundersharp.bombaydine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.orders.OrderStatus;
import com.thundersharp.bombaydine.user.ui.tableBooking.TableBookingMain;

public class DeeplinkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);


        FirebaseDynamicLinks
                .getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink.toString().contains("#orderid%")){

                                String uid = deepLink.toString().substring(deepLink.toString().indexOf("=")+1,deepLink.toString().indexOf("#"));
                                String orderiD = deepLink.toString().substring(deepLink.toString().indexOf("%")+1);

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                        .child(uid)
                                        .child(CONSTANTS.DATABASE_NODE_ORDERS)
                                        .child(CONSTANTS.DATABASE_NODE_OVERVIEW)
                                        .child(orderiD)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    startActivity(new Intent(DeeplinkActivity.this,OrderStatus.class).putExtra("data",snapshot.getValue(OrederBasicDetails.class)));
                                                    finish();
                                                }else {
                                                    Toast.makeText(DeeplinkActivity.this, "No data found for the order !", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(DeeplinkActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }else Toast.makeText(DeeplinkActivity.this, deepLink.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });

    }
}