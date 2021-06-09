package com.thundersharp.admin.ui.location;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.address.CordinatesInteractor;
import com.thundersharp.admin.core.address.Cordinateslistner;
import com.thundersharp.admin.core.location.DistanceFromCoordinates;
import com.thundersharp.admin.core.utils.ResturantCoordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Service_areas extends Fragment implements OnMapReadyCallback, Cordinateslistner.fetchSuccessListener {

    private GoogleMap mMap;
    private Marker restmark,markedmarker;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView address,address1,address2;
    private List<LatLng> coordinatesVal = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_areas_admin, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        shimmerFrameLayout = view.findViewById(R.id.shimmermap);
        address = view.findViewById(R.id.address);
        address1 = view.findViewById(R.id.address1);
        address2 = view.findViewById(R.id.address2);
        shimmerFrameLayout.startShimmer();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_night);
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


        CordinatesInteractor cordinatesInteractor = new CordinatesInteractor(this);
        cordinatesInteractor.fetchAllCoordinates();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions destmarker = new MarkerOptions();
                destmarker.title("Your Location");
                destmarker.position(latLng);

                markedmarker = mMap.addMarker(destmarker);

                boolean isInsideBondry = PolyUtil.containsLocation(latLng, coordinatesVal, true);

                Address address = null;
                try {
                    address = getLocationfromLat(latLng.latitude, latLng.longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Toast.makeText(getActivity(), "We deliver only in this area", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        coordinatesVal = Arrays.asList(coOrdinates);
        mMap.addPolyline((new PolylineOptions())
                .add(coOrdinates)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
        if (coordinatesVal.size()>3) {
            try {
                address.setText(coOrdinates[0].latitude+","+coOrdinates[0].longitude+"\n"+getLocationfromLat(coOrdinates[0].latitude,coOrdinates[0].longitude).getAddressLine(0));
                address1.setText(coOrdinates[1].latitude+","+coOrdinates[1].longitude+"\n"+getLocationfromLat(coOrdinates[1].latitude,coOrdinates[1].longitude).getAddressLine(0));
                address2.setText(coOrdinates[2].latitude+","+coOrdinates[1].longitude+"\n"+getLocationfromLat(coOrdinates[2].latitude,coOrdinates[2].longitude).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // on below line we will be starting the drawing of polyline.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ResturantCoordinates.resturantLatLong, 13));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

    }

    @Override
    public void onCordinatesFailure(Exception exception) {

    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0);
    }
}