package com.thundersharp.bombaydine.user.ui.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.PolyUtil;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AddressHolder;
import com.thundersharp.bombaydine.user.core.Adapters.AllAddressHolderAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.ui.home.HomeFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class AddAddressActivity extends AppCompatActivity implements
        AddressLoader.onAddresLoadListner,
        SharedPrefUpdater.OnSharedprefUpdated,
        Cordinateslistner.fetchSuccessListener {

    TextView add_new_location, view_detail, location_type, address_full;
    RecyclerView rv_location_history;
    ImageView home;
    private List<LatLng> latLngs;
    private LocationRequest locationRequest;
    private Address address;
    private static final int REQUEST_CHECK_SETTINGS = 140;
    private SharedPrefHelper sharedPrefHelper;
    private AddressHelper addressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        loadViews();

        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        addressHelper = new AddressHelper(this, this);
        sharedPrefHelper = new SharedPrefHelper(this, this);
        CordinatesInteractor cordinatesInteractor = new CordinatesInteractor(this);

        addressHelper.loaduseraddress();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
            ((TextView)findViewById(R.id.location_type)).setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME());
            ((TextView)findViewById(R.id.address_full)).setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1()+" "+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE2());
        }

        ((AppCompatButton)findViewById(R.id.drop_choose)).setOnClickListener(v -> startActivityForResult(new Intent(this, HomeLocationChooser.class), 101));

        ((TextView)findViewById(R.id.add_more_location)).setOnClickListener(v -> startActivityForResult(new Intent(this, HomeLocationChooser.class), 101));

        ((AppCompatButton)findViewById(R.id.current_location)).setOnClickListener(v -> cordinatesInteractor.fetchAllCoordinates());

        view_detail.setOnClickListener(view->{
            //TODO DETAIL Activity
        });
        add_new_location.setOnClickListener(view->{
            //TODO detail activity with editable to a filed true
        });
    }

    private void loadViews() {
        add_new_location = findViewById(R.id.add_new_location);
        view_detail = findViewById(R.id.view_detail);
        rv_location_history = findViewById(R.id.rv_location_history);
        location_type = findViewById(R.id.location_type);
        home = findViewById(R.id.home);
        address_full = findViewById(R.id.address_full);
    }

    @Override
    public void onAddressLoaded(List<AddressData> addressData) {
        //shimmerFrameLayout.stopShimmer();
        //shimmerFrameLayout.setVisibility(View.GONE);
        AddressHolder allAddressHolderAdapter = new AddressHolder(this, addressData);
        rv_location_history.setAdapter(allAddressHolderAdapter);
        rv_location_history.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAddressLoadFailure(Exception e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        latLngs = Arrays.asList(coOrdinates);
        createLocationRequest();
    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                getFusedLocationProviderClient(this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // do work here

                                        boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), latLngs, true);
                                        if (!iSoutsidepolyline) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddAddressActivity.this);
                                            builder.setMessage("Sorry we don't deliver in your location, But you can still order for someone in our delivery location.");
                                            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                            });
                                            builder.setCancelable(false);
                                            builder.show();
                                        } else {
                                            try {
                                                address = getLocationfromLat(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                                                AddressData addressData = new AddressData(
                                                        address.getAddressLine(0),
                                                        "",
                                                        "",
                                                        "",
                                                        0,
                                                        locationResult.getLastLocation().getLatitude() + "," + locationResult.getLastLocation().getLongitude(),
                                                        0);

                                                sharedPrefHelper.SaveDataToSharedPref(addressData);
                                                //addressline1.setText(address.getAddressLine(0));

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
                Toast.makeText(this, "Location is required to get current location", Toast.LENGTH_SHORT).show();

            }
        }
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

        SettingsClient client = LocationServices.getSettingsClient(AddAddressActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(AddAddressActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);


                    getFusedLocationProviderClient(AddAddressActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            // do work here

                                            boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), latLngs, true);
                                            if (!iSoutsidepolyline) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddAddressActivity.this);
                                                builder.setMessage("Sorry we don't deliver in your location,");
                                                builder.setPositiveButton("OK", (dialogInterface, i) -> {

                                                });
                                                builder.setCancelable(false);
                                                builder.show();
                                            } else {
                                                try {
                                                    address = getLocationfromLat(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                                                    AddressData addressData = new AddressData(
                                                            address.getAddressLine(0),
                                                            "",
                                                            "",
                                                            "",
                                                            0,
                                                            locationResult.getLastLocation().getLatitude() + "," + locationResult.getLastLocation().getLongitude(),
                                                            0);

                                                    sharedPrefHelper.SaveDataToSharedPref(addressData);

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
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
                                        AddAddressActivity.this,
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


            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Toast.makeText(getContext(), "e", Toast.LENGTH_SHORT).show();
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(AddAddressActivity.this,
                                1000);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });


    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return addresses.get(0);
    }

    @Override
    public void onSharedPrefUpdate(AddressData addressData) {
        location_type.setText(addressData.getADDRESS_NICKNAME());
        address_full.setText(addressData.getADDRESS_LINE1());
    }
}