package com.thundersharp.bombaydine.user.ui.tableBooking;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.core.utils.ResturantCoordinates;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.utils.LocationUpdater;
import com.thundersharp.bombaydine.user.core.utils.Resturant;

public class TableBookingConfirmation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking_confirmation);

        Glide.with(this).load(R.drawable.checked).into((ImageView)findViewById(R.id.greenTIck));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ResturantCoordinates.resturantLatLong);
        markerOptions.title(Resturant.resturant);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));


        MarkerOptions house = new MarkerOptions();
        house.position(LocationUpdater.getInstance(this).getCoOrdinates());
        house.title(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


        mMap = googleMap;
        mMap.setMapStyle(style);

        mMap.setMinZoomPreference(7f);
        mMap.setBuildingsEnabled(true);
        mMap.addMarker(markerOptions);
        mMap.addMarker(house);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(TableBookingConfirmation.this, ""+LocationUpdater.getInstance(TableBookingConfirmation.this).getCoOrdinates().toString(), Toast.LENGTH_SHORT).show();
                mMap.addPolyline((new PolylineOptions())
                        .add(ResturantCoordinates.resturantLatLong, LocationUpdater.getInstance(TableBookingConfirmation.this).getCoOrdinates())
                        .width(8)
                        .color(Color.RED)
                        .geodesic(true));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ResturantCoordinates.resturantLatLong, 9));
            }
        },800);



    }
}