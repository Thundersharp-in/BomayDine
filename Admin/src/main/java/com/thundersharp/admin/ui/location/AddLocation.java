package com.thundersharp.admin.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker restmark,markedmarker;
    List<Marker> latLngList;
    List<Polyline> polylineList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ((FloatingActionButton)findViewById(R.id.undo)).setOnClickListener(v ->{
            if (!latLngList.isEmpty() && latLngList != null ) {
                if (latLngList.size() >= 2) {
                    if (mMap != null) {
                        latLngList.get(latLngList.size() - 1).remove();
                        latLngList.remove(latLngList.size() - 1);
                        if (polylineList.size()>=1) {
                            polylineList.get(polylineList.size() - 1).remove();
                            polylineList.remove(polylineList.size() - 1);
                        }
                    }
                } else {
                    latLngList.get(latLngList.size() - 1).remove();
                    latLngList.remove(latLngList.size() - 1);
                    if (polylineList.size() >=1) {
                        polylineList.get(polylineList.size() - 1).remove();
                        polylineList.remove(polylineList.size() - 1);
                    }
                }
            }else Toast.makeText(this, "No markers to remove", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_address, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_area) {
            if (!latLngList.isEmpty() && latLngList != null){
                latLngList.add(latLngList.get(0));
                HashMap<String , Object> setLatLongVal = new HashMap<>();
                for (int i=0; i<latLngList.size(); i++){
                    setLatLongVal.put(""+(i+1),String.valueOf(latLngList.get(i).getPosition().latitude+","+latLngList.get(i).getPosition().longitude));
                }

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_SERVICIABLE_AREA)
                        .setValue(setLatLongVal)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(AddLocation.this, "Saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else
                                    Toast.makeText(AddLocation.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }else Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.clear_map){
            if (!latLngList.isEmpty() && latLngList != null){
                if (mMap!=null){
                    mMap.clear();
                    latLngList.clear();
                    polylineList.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Bombay dine restaurant");
                    markerOptions.position(ResturantCoordinates.resturantLatLong);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));
                    mMap.addMarker(markerOptions);
                }else Toast.makeText(this, "ERROR : Map object is null", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Nothing to clear", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        latLngList = new ArrayList<>();
        polylineList = new ArrayList<>();

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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions destmarker = new MarkerOptions();
                destmarker.position(latLng);
                if (latLngList.isEmpty()){
                    if (markedmarker!=null)
                    markedmarker.remove();
                    markedmarker = mMap.addMarker(destmarker);
                    latLngList.add(markedmarker);
                }else {
                    markedmarker = mMap.addMarker(destmarker);
                    latLngList.add(markedmarker);
                    polylineList.add(drawPoliLineBetweenPoints(latLngList.get(latLngList.size()-2).getPosition(),markedmarker.getPosition()));
                }

            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ResturantCoordinates.resturantLatLong, 13));

    }

    public Polyline drawPoliLineBetweenPoints(LatLng p1, LatLng p2){
        return mMap.addPolyline((new PolylineOptions())
                .add(p1,p2)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
    }

}