package com.thundersharp.bombaydine.user.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressUpdater;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddressEdit extends AppCompatActivity implements OnMapReadyCallback,
        AddressUpdater.OnAddressUpdateListner, Cordinateslistner.fetchSuccessListener {

    private GoogleMap mMap;
    private AddressData addressData;
    private Address address;
    private Marker marker;
    private CordinatesInteractor cordinatesInteractor;
    private AddressHelper addressHelper;

    private AppCompatButton savencontinue;
    private EditText addressline1,addressline2,city,zip;
    private ChipGroup worktype;


/*    private LatLng TamWorth = new LatLng(13.083925, 77.479119);
    private LatLng NewCastle = new LatLng(13.093330, 77.489017);
    private LatLng Brisbane = new LatLng(13.075816, 77.480262);
    private LatLng point5 = new LatLng(13.068875, 77.507298);
    private LatLng point6 = new LatLng(13.070047, 77.465500);*/

    List<LatLng> latLngs = new ArrayList<>();
    private LinearLayout bottomholder,data2;

    /**
     * Calling this activity requires searlized data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        addressData = (AddressData) getIntent().getSerializableExtra("data");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addressHelper = new AddressHelper(AddressEdit.this,this,"");
        cordinatesInteractor = new CordinatesInteractor(this);


        savencontinue = findViewById(R.id.savenproceed);
        addressline1 = findViewById(R.id.addressline1);
        addressline2 = findViewById(R.id.addressline2);
        city = findViewById(R.id.city);
        zip = findViewById(R.id.pin);
        worktype = findViewById(R.id.worktype);
        bottomholder = findViewById(R.id.data1);
        data2 = findViewById(R.id.data2);


        if (addressData.getADDRESS_NICKNAME().equalsIgnoreCase("Home")){
            worktype.check(R.id.home);
        }else if (addressData.getADDRESS_NICKNAME().equalsIgnoreCase("Office")){
            worktype.check(R.id.office);
        }else worktype.check(R.id.other);

        addressline1.setText(addressData.getADDRESS_LINE1());
        addressline2.setText(addressData.getADDRESS_LINE2());
        city.setText(addressData.getCITY());
        zip.setText(addressData.getZIP()+"");

        savencontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomholder.getVisibility() == View.GONE){
                    String nickname=null;
                    if (worktype.getCheckedChipId() == R.id.home){
                        nickname= "Home";
                    }else if (worktype.getCheckedChipId() == R.id.office){
                        nickname = "Office";
                    }else nickname = "Others";
                    //Toedo {Go with update flow}
                    String lat_long = marker.getPosition().latitude+","+marker.getPosition().longitude;
                    AddressData addressDataf = new AddressData(
                            addressline1.getText().toString(),
                            addressline2.getText().toString(),
                            nickname,
                            city.getText().toString(),
                            addressData.getID(),
                            lat_long,
                            Integer.parseInt(zip.getText().toString()));
                    addressHelper.dataUpdate(addressDataf);


                }else {
                    bottomholder.setVisibility(View.GONE);
                    data2.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);

        //mMap.setMaxZoomPreference(10f);
        markerOptions.title("Your Marked Address");
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        //Toast.makeText(this, ""+getlatlang(addressData.getLAT_LONG()).latitude+","+getlatlang(addressData.getLAT_LONG()).longitude, Toast.LENGTH_SHORT).show();
        markerOptions.position(getlatlang(addressData.getLAT_LONG()));
        markerOptions.draggable(true);

        mMap.setMinZoomPreference(10f);
        mMap.setBuildingsEnabled(true);
        checkForPermissions();
        mMap.setMyLocationEnabled(true);
        marker = mMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(AddressEdit.this,"Lat :"+latLng.latitude+"Lon : "+latLng.longitude,Toast.LENGTH_SHORT).show();
                boolean iSoutsidepolyline = PolyUtil.containsLocation(latLng,latLngs,true);
                if (iSoutsidepolyline) {
                    marker.remove();

                    markerOptions.position(latLng);
                    marker = mMap.addMarker(markerOptions);

                    try {
                        address = getLocationfromLat(latLng.latitude, latLng.longitude);
                        addressline1.setText(address.getAddressLine(0));
                        addressline2.setText(address.getAddressLine(1));
                        city.setText(address.getLocality());
                        zip.setText(address.getPostalCode());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(AddressEdit.this,"Sorry we are not yet serving to your location.",Toast.LENGTH_SHORT).show();
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                boolean iSoutsidepolyline = PolyUtil.containsLocation(marker.getPosition(),latLngs,true);
                if (iSoutsidepolyline) {
                    try {
                        address = getLocationfromLat(marker.getPosition().latitude, marker.getPosition().longitude);
                        addressline1.setText(address.getAddressLine(0));
                        addressline2.setText(address.getAddressLine(1));
                        city.setText(address.getLocality());
                        zip.setText(address.getPostalCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(AddressEdit.this,"Sorry we are not yet serving to your location.",Toast.LENGTH_SHORT).show();

                //Toast.makeText(AddressEdit.this,String.valueOf(marker.getPosition().longitude)+"\naddress : "+ address.getAddressLine(0)+"\nPostal : "+address.getPostalCode(),Toast.LENGTH_LONG).show();
            }
        });


/*
        mMap.addPolyline((new PolylineOptions())
                .add(Brisbane, NewCastle, TamWorth, point5,point6,Brisbane)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
*/

        cordinatesInteractor.fetchAllCoordinates();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getlatlang(addressData.getLAT_LONG()), 18));




    }

    @NonNull
    private Address getLocationfromLat(double lat , double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return addresses.get(0);
    }


    private LatLng getlatlang(String lat_long) {
        double lat = Double.parseDouble(lat_long.substring(0,lat_long.indexOf(",") ));
        double longitude = Double.parseDouble(lat_long.substring(lat_long.indexOf(",")+1));
        return new LatLng(lat,longitude);
    }

    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {
            //initializeViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //initializeFragments();
            } else {
                openAlertDialog();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app requires your location to function!").setCancelable(false);
        alertDialogBuilder.setPositiveButton("Try again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        checkForPermissions();
                    }
                });

        alertDialogBuilder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:com.thundersharp.bombaydine"));
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onAddressUpdate(Task<Void> task, boolean isTaskSucessful) {
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddressUpdateFailure(Exception e) {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        //Toast.makeText(this, ""+coOrdinates[0].latitude+","+coOrdinates[0].longitude, Toast.LENGTH_SHORT).show();
        latLngs = Arrays.asList(coOrdinates);

        try {

            mMap.addPolyline((new PolylineOptions())
                    .add(coOrdinates)
                    .width(8)
                    .color(Color.RED)
                    .geodesic(true));

        }catch (Exception e){
            Log.d("EXXXXXX",e.getMessage());
        }



    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
    }
}