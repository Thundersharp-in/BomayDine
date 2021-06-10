package com.thundersharp.admin.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.PolyUtil;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.address.CordinatesInteractor;
import com.thundersharp.admin.core.utils.ResturantCoordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoOrdinatesUpdater extends AppCompatActivity implements OnMapReadyCallback {

    public static void main(Context context , LatLng latLng){
        context.startActivity(new Intent(context,CoOrdinatesUpdater.class).putExtra("data",latLng));
    }

    private GoogleMap mMap;
    private Marker restmark,markedmarker;
    LinearLayout btn_holder;
    LatLng existingCoOrdinate;
    TextInputLayout coOrdinate,addressE;
    boolean isValited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_ordinates_updater);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getParcelableExtra("data") != null){
            existingCoOrdinate = (LatLng)getIntent().getParcelableExtra("data");
        }else {
            finish();
            Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(V->finish());
        coOrdinate=findViewById(R.id.coordinates);
        addressE = findViewById(R.id.addAddress);
        coOrdinate.getEditText().setText(existingCoOrdinate.latitude+","+existingCoOrdinate.longitude);
        try {
            addressE.getEditText().setText(getLocationfromLat(existingCoOrdinate.latitude,existingCoOrdinate.longitude).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
            addressE.getEditText().setText("Error unable to fetch address");
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);
        //mMap.setMaxZoomPreference(10f);
        markerOptions.title("Bombay dine restaurant");
        markerOptions.position(ResturantCoordinates.resturantLatLong);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));

        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
        restmark = mMap.addMarker(markerOptions);

        if (existingCoOrdinate !=null) {
            MarkerOptions initMarker = new MarkerOptions();
            initMarker.title("Existing Location");
            initMarker.position(existingCoOrdinate);
            markedmarker = mMap.addMarker(initMarker);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markedmarker.remove();
                MarkerOptions destmarker = new MarkerOptions();
                destmarker.title("Your Location");
                destmarker.position(latLng);

                markedmarker = mMap.addMarker(destmarker);


                Address address = null;
                try {
                    coOrdinate.getEditText().setText(latLng.latitude+","+latLng.longitude);
                    address = getLocationfromLat(latLng.latitude, latLng.longitude);
                    addressE.getEditText().setText(address.getAddressLine(0));
                    existingCoOrdinate = latLng;
                } catch (IOException e) {
                    e.printStackTrace();
                    existingCoOrdinate = latLng;
                    addressE.getEditText().setText("Error unable to fetch address");
                }


            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(existingCoOrdinate, 13));

    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0);
    }
}