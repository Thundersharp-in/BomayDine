package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllPreviousAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.location.StorageFailure;
import com.thundersharp.bombaydine.user.core.utils.LatLongConverter;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.ui.location.AddressEdit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddUpdateAddress extends AppCompatActivity implements OnMapReadyCallback , Cordinateslistner.fetchSuccessListener {

    AddressHelper addressHelper;
    List<AddressData> addressDatamain;
    AllPreviousAdapter allPreviousAdapter;
    private GoogleMap mMap;
    public static List<AddressData> dataList;
    CordinatesInteractor cordinatesInteractor;
    private List<LatLng> coordinatesVal;
    private HashMap<String, Marker> markerHashMap;

    private AlertDialog.Builder builder;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_update_final);

        builder = new AlertDialog.Builder(this);
        View dialogview = LayoutInflater.from(this).inflate(R.layout.progress_dialog_admin,null,false);
        builder.setView(dialogview);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        markerHashMap = new HashMap<>();
        cordinatesInteractor = new CordinatesInteractor(this,this);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        dataList = new ArrayList<>();
        ((ExtendedFloatingActionButton)findViewById(R.id.addAddress))
                .setOnClickListener(c->{
                    dialog.show();
                    startActivityForResult(new Intent(this, AddressEdit.class),1078);

                });

        addressHelper = new AddressHelper(this, new AddressLoader.onAddresLoadListner() {
            @Override
            public void onAddressLoaded(List<AddressData> addressData) {
                addressDatamain = addressData;
                dataList = addressData;
                allPreviousAdapter = new AllPreviousAdapter(AddUpdateAddress.this,addressData);
                ((RecyclerView)findViewById(R.id.recyclerview)).setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                ((RecyclerView)findViewById(R.id.recyclerview)).setAdapter(allPreviousAdapter);
                updateMapPoints(mMap,dataList);
            }

            @Override
            public void onAddressLoadFailure(Exception e) {
                Toast.makeText(AddUpdateAddress.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        registerReceiver(broadcastReceiver,new IntentFilter("full"));
        addressHelper.loaduseraddress();

    }

    protected synchronized void updateMapPoints(GoogleMap mMap, List<AddressData> dataList) {
        markerHashMap.clear();
        if (mMap != null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));

            for (AddressData addressData : dataList){
                markerOptions.title(addressData.getADDRESS_LINE1());
                markerOptions.position(LatLongConverter.initialize().getlatlang(addressData.getLAT_LONG()));
                Marker marker = mMap.addMarker(markerOptions);
                markerHashMap.put(addressData.getLAT_LONG(),marker);
            }
            dialog.dismiss();
        }else {
            dialog.dismiss();
            Toast.makeText(this, "Map not yet ready waiting", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter("full"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1078){
            dialog.show();
            AddressData addressData = (AddressData) data.getSerializableExtra("data");
            if (addressDatamain == null){
                List<AddressData> data1 = new ArrayList<>();
                data1.add(addressData);
                dataList.add(addressData);
                ((RecyclerView)findViewById(R.id.recyclerview)).setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                ((RecyclerView)findViewById(R.id.recyclerview)).setAdapter(new AllPreviousAdapter(AddUpdateAddress.this,data1));
                updateMapPoints(mMap,dataList);

            }else {
                dataList.add(addressData);
                addressDatamain.add(addressData);
                updateMapPoints(mMap,dataList);
                allPreviousAdapter.notifyDataSetChanged();
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            refreshAdapter();
        }
    };

    public synchronized void refreshAdapter(){
        if (dataList !=null) {
            ((RecyclerView) findViewById(R.id.recyclerview))
                    .setAdapter(new AllPreviousAdapter(AddUpdateAddress.this, dataList));
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);

        mMap = googleMap;
        mMap.setMapStyle(style);
        mMap.setMyLocationEnabled(false);
        dialog.show();
        cordinatesInteractor.fetchAllCoordinatesFromStorage();
    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        coordinatesVal = Arrays.asList(coOrdinates);
        mMap.addPolyline((new PolylineOptions())
                .add(coOrdinates)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
        // on below line we will be starting the drawing of polyline.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Resturant.resturantLatLong, 13));
        dialog.dismiss();

    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        if (exception instanceof StorageFailure){
            cordinatesInteractor.fetchAllCoordinates();
            dialog.dismiss();
            Toast.makeText(this,"Storage failure "+exception.getMessage(),Toast.LENGTH_LONG).show();
        }else {
            dialog.dismiss();
            Toast.makeText(this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}