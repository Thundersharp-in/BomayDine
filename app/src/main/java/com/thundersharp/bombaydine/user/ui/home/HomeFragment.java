package com.thundersharp.bombaydine.user.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllAddressHolderAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.CategoryAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.PlacesAutoCompleteAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.TopsellingAdapter;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.location.PinCodeContract;
import com.thundersharp.bombaydine.user.core.location.PinCodeInteractor;
import com.thundersharp.bombaydine.user.ui.location.HomeLocationChooser;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllCategoryActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.bombaydine.user.ui.menu.TopSellingAll;
import com.thundersharp.bombaydine.user.ui.orders.RecentOrders;
import com.thundersharp.bombaydine.user.ui.scanner.QrScanner;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.thundersharp.bombaydine.user.ui.home.MainPage.navController;


public class HomeFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener,
        PlacesAutoCompleteAdapter.ClickListener,
        /* PinCodeContract.onPinDatafetchListner,*/
        AddressLoader.onAddresLoadListner,
        SharedPrefUpdater.OnSharedprefUpdated,
        Cordinateslistner.fetchSuccessListener,
        HomeDataContract.DataLoadFailure,
        HomeDataContract.topSellingFetch,
        HomeDataContract.HomeAllCategoriesFetch,
        HomeDataContract.HomeAllItems {

    private static final int REQUEST_CHECK_SETTINGS = 140;
    /**
     * Slider layout and other ui components
     */
    private SliderLayout mDemoSlider;
    List<Object> data = new ArrayList<>();
    private CircleImageView profile;
    private ImageView qrcode;
    private TextView recentorders, allitemsview,allcategory,topsellingallv;
    public static TextView textcurrloc;
    private AllItemAdapter allItemAdapter;
    private RecyclerView horizontalScrollView, categoryRecycler, topsellingholder;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    public static BottomSheetDialog bottomSheetDialog;
    /**
     * Pin code details from API
     */
    String pinCode;
    private RequestQueue mRequestQueue;
    private LinearLayout current_loc;
    private LocationRequest locationRequest;
    //private PinCodeInteractor pinCodeInteractor;
    private ShimmerFrameLayout shimmerFrameLayout,shimmerplace_allitem;
    private RecyclerView addressholder;
    private Address address;

    /**
     * Address Listeners and helpers
     */
    private AddressHelper addressHelper;
    private SharedPrefHelper sharedPrefHelper;
    private List<LatLng> latLngs;
    private HomeDataProvider homeDataProvider;

    private ShimmerFrameLayout shimmerplace_cat,shimmerplace_topsell;

    private TextView version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        version = view.findViewById(R.id.version);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version.setText("V "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sharedPrefHelper = new SharedPrefHelper(getContext(), this);
        mDemoSlider = view.findViewById(R.id.slider);
        shimmerplace_cat = view.findViewById(R.id.shimmerplace_cat);
        shimmerplace_allitem = view.findViewById(R.id.shimmerplace_allitem);
        shimmerplace_topsell = view.findViewById(R.id.shimmerplace_topsell);
        horizontalScrollView = view.findViewById(R.id.allitems);
        topsellingholder = view.findViewById(R.id.topsellingholder);
        qrcode = view.findViewById(R.id.qrcode);
        allitemsview = view.findViewById(R.id.allitemsview);
        horizontalScrollView.setHasFixedSize(true);
        recentorders = view.findViewById(R.id.recentorders);
        profile = view.findViewById(R.id.profile);
        current_loc = view.findViewById(R.id.current_loc);
        textcurrloc = view.findViewById(R.id.textcurrloc);
        allcategory = view.findViewById(R.id.allcategory);
        topsellingallv = view.findViewById(R.id.topsellingallv);
        homeDataProvider = new HomeDataProvider(getActivity(), this, this, this, this);

        mRequestQueue = Volley.newRequestQueue(getContext());

        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));
        //pinCodeInteractor = new PinCodeInteractor(getContext(),this);
        addressHelper = new AddressHelper(getActivity(), this);

        //recyclerView = (RecyclerView) view.findViewById(R.id.places_recycler_view);

        topsellingallv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopSellingAll.class));
            }
        });

        allitemsview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllItemsActivity.class));
            }
        });

        allcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllCategoryActivity.class));
            }
        });

        current_loc.setOnClickListener(viewlocation -> {
            bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            View bottomview = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, view.findViewById(R.id.botomcontainer));
            addressHelper.loaduseraddress();
            //recyclerView = bottomview.findViewById(R.id.places_recycler_view);
            shimmerFrameLayout = bottomview.findViewById(R.id.shimmerlayout);
            recyclerView = bottomview.findViewById(R.id.addressholder);
            TextView currentloc = bottomview.findViewById(R.id.current_loc);
            LinearLayout linearLayout = bottomview.findViewById(R.id.searchedit);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getActivity(), HomeLocationChooser.class), 101);
                }
            });
            CordinatesInteractor cordinatesInteractor = new CordinatesInteractor(HomeFragment.this);

            currentloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cordinatesInteractor.fetchAllCoordinates();
                }
            });

/*
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() == 6){
                        pinCodeInteractor.getdetailsfromPincode(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
*/

/*
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Set the fields to specify which types of place data to
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(getActivity());
                    startActivityForResult(intent, 1);

                }
            });
*/

            /*mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAutoCompleteAdapter.setClickListener(this);
            recyclerView.setAdapter(mAutoCompleteAdapter);
            mAutoCompleteAdapter.notifyDataSetChanged();*/

            bottomSheetDialog.setContentView(bottomview);
            bottomSheetDialog.show();
        });

        profile.setOnClickListener(viewclick -> {
            navController.navigate(R.id.profile);
        });

        recentorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), RecentOrders.class));
                } else {
                    Toast.makeText(getContext(), "Kindly login to see your recent orders.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QrScanner.class));
            }
        });


        categoryRecycler = view.findViewById(R.id.recentordcategoryholderer);
        categoryRecycler.setHasFixedSize(true);



        homeDataProvider.fetchhomeAllCategories();
        homeDataProvider.fetchTopSelling();
        homeDataProvider.fetchHomeallItem();


        ArrayList<String> listUrl = new ArrayList<>();

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/bombay-dine.appspot.com/o/4349754.jpg?alt=media&token=1f4c313b-d8dc-4316-a04a-8613710a1567");

        listUrl.add("https://thumbs.dreamstime.com/z/pizzeria-discount-banner-pepperoni-margherita-slices-italian-food-advertisement-pizza-tomatoes-mozzarella-ketchup-186048578.jpg");

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/bombay-dine.appspot.com/o/4349754.jpg?alt=media&token=1f4c313b-d8dc-4316-a04a-8613710a1567");

        listUrl.add("https://thumbs.dreamstime.com/z/pizzeria-discount-banner-pepperoni-margherita-slices-italian-food-advertisement-pizza-tomatoes-mozzarella-ketchup-186048578.jpg");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < listUrl.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(null)
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());

            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);


        return view;
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(getContext(),place.getLatLng().toString(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("ERROR",status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(getContext(), place.getAddress() + ", " + place.getLatLng().latitude + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPrefHelper.updatehomelocationData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


/*
    @Override
    public void onDataFetch(JSONObject obj) {
        String district = null,state = null,country = null,name = null;
        try {
            district = obj.getString("District");
            state = obj.getString("State");
            country = obj.getString("Country");
            name = obj.getString("Name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(),
                "Details of pin code is : \n" +
                        "District is : " + district + "\n" +
                        "State : " + state + "\n" +
                        "Country : " + country+
                        "\nName : "+name,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDataFetchFailureListner(Exception e) {
        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
*/

    @Override
    public void onAddressLoaded(List<AddressData> addressData) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        AllAddressHolderAdapter allAddressHolderAdapter = new AllAddressHolderAdapter(getActivity(), addressData);
        recyclerView.setAdapter(allAddressHolderAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        //Toast.makeText(getActivity(), ""+addressData.get(0).getADDRESS_LINE1(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAddressLoadFailure(Exception e) {
        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSharedPrefUpdate(AddressData addressData) {
        if (bottomSheetDialog != null) {
            if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.cancel();
            }
        }
        textcurrloc.setText(addressData.getADDRESS_NICKNAME() + ": " + addressData.getADDRESS_LINE1());

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

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);


                    getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            // do work here

                                            boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), latLngs, true);
                                            if (!iSoutsidepolyline) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                                                    //addressline1.setText(address.getAddressLine(0));

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
                                        getActivity(),
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

                //Toast.makeText(getContext(), "e1", Toast.LENGTH_SHORT).show();

            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
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
                        resolvable.startResolutionForResult(getActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                getFusedLocationProviderClient(getActivity())
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // do work here

                                        boolean iSoutsidepolyline = PolyUtil.containsLocation(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), latLngs, true);
                                        if (!iSoutsidepolyline) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                Toast.makeText(getContext(), "Location is required to get current location", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

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
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        latLngs = Arrays.asList(coOrdinates);
        Toast.makeText(getActivity(), "jj", Toast.LENGTH_SHORT).show();
        createLocationRequest();
    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCategoryFetchSuccess(List<Object> data) {
        //Toast.makeText(getActivity(), "dddd", Toast.LENGTH_SHORT).show();
        CategoryAdapter categoryAdapter = new CategoryAdapter(data, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        categoryRecycler.setLayoutManager(gridLayoutManager);
        categoryRecycler.setAdapter(categoryAdapter);
        shimmerplace_cat.stopShimmer();
        shimmerplace_cat.setVisibility(View.GONE);

    }

    @Override
    public void onTopSellingfetchSuccess(List<Object> data) {

        TopsellingAdapter categoryAdapter = new TopsellingAdapter(getContext(), data);
        topsellingholder.setAdapter(categoryAdapter);
        shimmerplace_topsell.stopShimmer();
        shimmerplace_topsell.setVisibility(View.GONE);

    }

    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnHomeAlldataFetchSucess(List<Object> data) {
        allItemAdapter = new AllItemAdapter(data, getContext());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        //horizontalScrollView.setLayoutManager(gridLayoutManager);
        horizontalScrollView.setAdapter(allItemAdapter);
        shimmerplace_allitem.stopShimmer();
        shimmerplace_allitem.setVisibility(View.GONE);
    }
}