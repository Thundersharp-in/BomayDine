package com.thundersharp.admin.ui.gallary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.GallaryAdapter;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGallary extends AppCompatActivity implements UrlTransfer{

    List<String> url;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_gallary);

        url = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference("UPLOADED_IMAGES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                url.add(dataSnapshot.child("uri").getValue(String.class));
                            }

                            RecyclerView recyclerView =(RecyclerView)findViewById(R.id.recycler);

                            recyclerView.setLayoutManager(new GridLayoutManager(FirebaseGallary.this,2));
                            recyclerView.setAdapter(new GallaryAdapter(FirebaseGallary.this,url,FirebaseGallary.this));


                        }else
                            Toast.makeText(FirebaseGallary.this, "No image uploaded till now upload some", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    @Override
    public void onGetUrl(Uri uri) {

        setResult(1356,getIntent().putExtra("data",uri));
        finish();
    }
}