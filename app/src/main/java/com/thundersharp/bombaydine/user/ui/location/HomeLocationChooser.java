package com.thundersharp.bombaydine.user.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressUpdater;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.Resturant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HomeLocationChooser extends AppCompatActivity implements OnMapReadyCallback,
        AddressUpdater.OnAddressUpdateListner, Cordinateslistner.fetchSuccessListener{

    private static final int REQUEST_CHECK_SETTINGS = 10007;
    private GoogleMap mMap;
    //private AddressData addressData;
    private Address address;
    private Marker marker;
    private CordinatesInteractor cordinatesInteractor;
    private AddressHelper addressHelper;

    private LocationRequest locationRequest;

    private AppCompatButton savencontinue;
    private EditText addressline1;
    private String addressline2,city;
    private Integer zip;
    private ChipGroup worktype;

    List<LatLng> latLngs = new ArrayList<>();
    private LinearLayout bottomholder;
    private MarkerOptions markerOptions;


    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout relativeLayout;

    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_location_chooser);
        createLocationRequest();

        //addressData = (AddressData) getIntent().getSerializableExtra("data");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addressHelper = new AddressHelper(this,this,"");
        cordinatesInteractor = new CordinatesInteractor(this);
        sharedPrefHelper = new SharedPrefHelper(this, new SharedPrefUpdater.OnSharedprefUpdated() {
            @Override
            public void onSharedPrefUpdate(AddressData addressData) {
                finish();
            }
        });

        shimmerFrameLayout = findViewById(R.id.shimmermap1);
        relativeLayout = findViewById(R.id.mapholder);
        savencontinue = findViewById(R.id.savenproceed);
        addressline1 = findViewById(R.id.addressline1);
        //addressline2 = findViewById(R.id.addressline2);
        //city = findViewById(R.id.city);
       // zip = findViewById(R.id.pin);
        worktype = findViewById(R.id.worktype);
        bottomholder = findViewById(R.id.data1);
        worktype.check(R.id.home);

        relativeLayout.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmer();


        savencontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addressline1.getText().toString().isEmpty()) {
                    String nickname = null;
                    if (worktype.getCheckedChipId() == R.id.home) {
                        nickname = "Home";
                    } else if (worktype.getCheckedChipId() == R.id.office) {
                        nickname = "Office";
                    } else nickname = "Others";
                    //Toedo {Go with update flow}
                    String lat_long = marker.getPosition().latitude + "," + marker.getPosition().longitude;
                    if (addressline2.isEmpty()|| addressline2==null){
                        addressline2="";
                    }
                    if (zip==null){
                        zip=0;
                    }
                    if (city.isEmpty()||city == null){
                        city="";
                    }
                    AddressData addressDataf = new AddressData(addressline1.getText().toString(), addressline2, nickname, city, System.currentTimeMillis(), lat_long, zip);

                    sharedPrefHelper.SaveDataToSharedPref(addressDataf);
                    /*
                    if (FirebaseAuth.getInstance().getCurrentUser() != null ) {
                        FirebaseDatabase
                                .getInstance()
                                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(CONSTANTS.DATABASE_NODE_ADDRESS)
                                .child(String.valueOf(addressDataf.getID()))
                                .setValue(addressDataf)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(HomeLocationChooser.this, "", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(HomeLocationChooser.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                     */

                }
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
        markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);


        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.title("Bombay dine restaurant");
        markerOptions1.position(Resturant.resturantLatLong);
        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));

        //mMap.setMaxZoomPreference(10f);
        markerOptions.title("Your Marked Address");
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        //Toast.makeText(this, ""+getlatlang(addressData.getLAT_LONG()).latitude+","+getlatlang(addressData.getLAT_LONG()).longitude, Toast.LENGTH_SHORT).show();
        //TODO UPDATE DEVICE CURRENT LOCATION HERE
        markerOptions.position(Resturant.resturantLatLong);
        markerOptions.draggable(true);

        mMap.setMinZoomPreference(5f);
        mMap.setBuildingsEnabled(true);
        checkForPermissions();
        mMap.setMyLocationEnabled(true);

        marker = mMap.addMarker(markerOptions);
        mMap.addMarker(markerOptions1);

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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    //Toast.makeText(HomeLocationChooser.this,"Sorry we are not yet serving to your location.",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeLocationChooser.this);
                    builder.setMessage("Sorry we don't deliver in your location, We will be to your location soon.");
                    builder.setPositiveButton("OK",(dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        //HomeLocationChooser.this.marker.remove();
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getlatlang("13.083754127044125,77.48171612620354"), 18));
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    //Toast.makeText(HomeLocationChooser.this,"Sorry we are not yet serving to your location.",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeLocationChooser.this);
                    builder.setMessage("Sorry we don't deliver in your location, We will be to your location soon.");
                    builder.setPositiveButton("OK",(dialogInterface, i) -> {
                        HomeLocationChooser.this.marker.remove();
                    });
                    builder.setCancelable(false);
                    builder.show();
                }

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Resturant.resturantLatLong, 18));



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

        addressline2 = "State: $"+state+"# Country: %"+country+"* Address: @"+address+"^ KnownName: !"+knownName;
        this.city = city;
        zip = Integer.valueOf(postalCode);


        return addresses.get(0);
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

    }

    @Override
    public void onAddressUpdateFailure(Exception e) {

    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {

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

    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);


                    getFusedLocationProviderClient(HomeLocationChooser.this)
                            .requestLocationUpdates(locationRequest,new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            // do work here
                                            relativeLayout.setVisibility(View.VISIBLE);
                                            shimmerFrameLayout.setVisibility(View.GONE);

                                            //Toast.makeText(HomeLocationChooser.this, ""+locationResult.getLastLocation().getLatitude(), Toast.LENGTH_SHORT).show();

                                            if (marker != null){
                                                marker.remove();
                                                markerOptions.position(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));
                                                marker = mMap.addMarker(markerOptions);
                                            }



                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()), 18));

                                            boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()),latLngs,true);
                                            if (!iSoutsidepolyline){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeLocationChooser.this);
                                                builder.setMessage("Sorry we don't deliver in your location, But you can still order for someone in our delivery location.");
                                                builder.setPositiveButton("OK",(dialogInterface, i) -> {
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Resturant.resturantLatLong, 18));
                                                });
                                                builder.setCancelable(false);
                                                builder.show();
                                            }else{
                                                try {
                                                    address = getLocationfromLat(marker.getPosition().latitude, marker.getPosition().longitude);
                                                    addressline1.setText(address.getAddressLine(0));

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            //onLocationChanged(locationResult.getLastLocation());
                                        }
                                    },
                                    Looper.myLooper());

                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        HomeLocationChooser.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            break;
                    }
                }

                //Toast.makeText(HomeLocationChooser.this,"e1",Toast.LENGTH_SHORT).show();

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(HomeLocationChooser.this,"e",Toast.LENGTH_SHORT).show();
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(HomeLocationChooser.this,
                                1000);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                getFusedLocationProviderClient(HomeLocationChooser.this)
                        .requestLocationUpdates(locationRequest,new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // do work here
                                        relativeLayout.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.setVisibility(View.GONE);

                                        //Toast.makeText(HomeLocationChooser.this, ""+locationResult.getLastLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                                        if (marker != null){
                                            marker.remove();
                                            markerOptions.position(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));
                                            marker = mMap.addMarker(markerOptions);
                                        }

                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()), 18));

                                        boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()),latLngs,true);
                                        if (!iSoutsidepolyline){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeLocationChooser.this);
                                            builder.setMessage("Sorry we don't deliver in your location, But you can still order for someone in our delivery location.");
                                            builder.setPositiveButton("OK",(dialogInterface, i) -> {
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Resturant.resturantLatLong, 18));
                                            });
                                            builder.setCancelable(false);
                                            builder.show();
                                        }else {
                                            try {
                                                address = getLocationfromLat(marker.getPosition().latitude, marker.getPosition().longitude);
                                                addressline1.setText(address.getAddressLine(0));

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //onLocationChanged(locationResult.getLastLocation());
                                    }
                                },
                                Looper.myLooper());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(this, "Location is required to mark current location", Toast.LENGTH_SHORT).show();
                createLocationRequest();
            }
        }
    }

}