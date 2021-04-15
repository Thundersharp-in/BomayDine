package com.thundersharp.bombaydine.user.ui.location;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thundersharp.bombaydine.R;


public class Service_areas extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng TamWorth = new LatLng(13.083925, 77.479119);
    private LatLng NewCastle = new LatLng(13.093330, 77.489017);
    private LatLng Brisbane = new LatLng(13.075816, 77.480262);
    private LatLng point5 = new LatLng(13.068875, 77.507298);
    private LatLng point6 = new LatLng(13.070047, 77.465500);

    private LatLng bombaydine = new LatLng(13.083519,77.4822703);


    private RecyclerView recyclerView;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_areas, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return view;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_night);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);
        //mMap.setMaxZoomPreference(10f);
        markerOptions.title("Bombay dine restaurant");
        markerOptions.position(bombaydine);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));
        mMap.setMinZoomPreference(10f);
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getActivity(),"Lat :"+latLng.latitude+"Lon : "+latLng.longitude,Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Toast.makeText(getActivity(),"We deliver only in this area",Toast.LENGTH_SHORT).show();
            }
        });


        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.addPolyline((new PolylineOptions())
                .add(Brisbane, NewCastle, TamWorth, point5,point6,Brisbane)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
        // on below line we will be starting the drawing of polyline.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Brisbane, 13));

    }


}