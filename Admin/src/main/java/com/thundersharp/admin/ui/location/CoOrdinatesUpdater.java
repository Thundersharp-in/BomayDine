package com.thundersharp.admin.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.address.CordinatesInteractor;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.core.utils.ResturantCoordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CoOrdinatesUpdater extends AppCompatActivity implements OnMapReadyCallback {

    public static void main(Context context , LatLng latLng, Integer position, Integer size){
        context.startActivity(new Intent(context,CoOrdinatesUpdater.class).putExtra("data",latLng).putExtra("position",position).putExtra("size",size));
    }

    private GoogleMap mMap;
    private Marker restmark,markedmarker;
    LinearLayout btn_holder;
    LatLng existingCoOrdinate;
    TextInputLayout coOrdinate,addressE;
    boolean isValited = false;
    String marker_value = null;
    AppCompatButton validate_btn;
    Toolbar toolbar;
    Integer pos = null, size =null;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v ->finish());
        pos = getIntent().getIntExtra("position",1);
        size = getIntent().getIntExtra("size",1);

        //((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(V->finish());
        coOrdinate=findViewById(R.id.coordinates);
        addressE = findViewById(R.id.addAddress);
        validate_btn = findViewById(R.id.validate_btn);
        marker_value = existingCoOrdinate.latitude+","+existingCoOrdinate.longitude;
        coOrdinate.getEditText().setText(marker_value);
        try {
            addressE.getEditText().setText(getLocationfromLat(existingCoOrdinate.latitude,existingCoOrdinate.longitude).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
            addressE.getEditText().setText("Error unable to fetch address");
        }

        validate_btn.setOnClickListener(v -> {
            if (!coOrdinate.getEditText().getText().toString().isEmpty()) {
                if (coOrdinate.getEditText().getText().toString().equals(marker_value)) {
                    isValited = true;
                    Toast.makeText(this, "Validated successfully !", Toast.LENGTH_SHORT).show();
                } else if (coOrdinate.getEditText().getText().toString().contains(",")){
                    String lat=null , log = null;
                    lat=coOrdinate.getEditText().getText().toString().substring(0,coOrdinate.getEditText().getText().toString().indexOf(",")-1);
                    log=coOrdinate.getEditText().getText().toString().substring(coOrdinate.getEditText().getText().toString().indexOf(",")+1);
                    try {
                        double latitude = Double.parseDouble(lat);
                        double longitude = Double.parseDouble(log);
                        String string = getLocationfromLat(latitude,longitude).getAddressLine(0);
                        Toast.makeText(this, ""+string, Toast.LENGTH_SHORT).show();
                        if (string != null){
                            if (mMap!= null){
                                markedmarker.remove();
                                MarkerOptions destmarker = new MarkerOptions();
                                destmarker.title("Your Location");
                                destmarker.position(new LatLng(latitude,longitude));

                                markedmarker = mMap.addMarker(destmarker);
                                addressE.getEditText().setText(string);
                                isValited = true;

                            }
                        }

                    }catch (NumberFormatException numberFormatException){
                        isValited = false;

                        Toast.makeText(this, "Enter LatLong in this format only\n\nLat,Long", Toast.LENGTH_SHORT).show();
                        if(markedmarker!= null){
                            markedmarker.remove();
                        }

                    } catch (IOException e) {
                        isValited = false;
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        if(markedmarker!= null){
                            markedmarker.remove();
                        }
                        e.printStackTrace();
                    }catch (IllegalArgumentException w){
                        isValited = false;
                        Toast.makeText(this, "Couldn't validate \n:: IllegalArgument ::"+w.getMessage(), Toast.LENGTH_SHORT).show();
                        if(markedmarker!= null){
                            markedmarker.remove();
                        }
                    }

                }else {
                    if(markedmarker!= null){
                        markedmarker.remove();
                    }
                    isValited = false;
                    Toast.makeText(this, "Check the coordinate format", Toast.LENGTH_SHORT).show();
                }
            }else {
                if(markedmarker!= null){
                    markedmarker.remove();
                }
                isValited = false;
                Toast.makeText(this, "No co-ordinates present to render !", Toast.LENGTH_SHORT).show();
            }
        });

        coOrdinate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValited){ isValited = false; }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save){
            if (isValited){
                if (pos != null && size != null) {
                    HashMap<String, Object> value = new HashMap<>();
                    if (pos.equals(1)){
                        value.put("LATLON" + pos,coOrdinate.getEditText().getText().toString());
                        value.put("LATLON" + size,coOrdinate.getEditText().getText().toString());
                    }else value.put("LATLON" + pos,coOrdinate.getEditText().getText().toString());

                    FirebaseDatabase
                            .getInstance()
                            .getReference(CONSTANTS.DATABASE_NODE_SERVICIABLE_AREA)
                            .updateChildren(value)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CoOrdinatesUpdater.this, "Saved!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CoOrdinatesUpdater.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else Toast.makeText(this, "Position or Size error .\nTry to open again.", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Validate the location first", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
            isValited = true;
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
                    marker_value = latLng.latitude+","+latLng.longitude;
                    coOrdinate.getEditText().setText(marker_value);
                    isValited = true;
                    address = getLocationfromLat(latLng.latitude, latLng.longitude);
                    addressE.getEditText().setText(address.getAddressLine(0));
                    existingCoOrdinate = latLng;
                } catch (IOException e) {
                    e.printStackTrace();
                    existingCoOrdinate = latLng;
                    addressE.getEditText().setText("Error unable to fetch address");
                }catch (IllegalArgumentException illegalArgumentException){

                }


            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(existingCoOrdinate, 13));

    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException ,IllegalArgumentException{

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0);
    }
}