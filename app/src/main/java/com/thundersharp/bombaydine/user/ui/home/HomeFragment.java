package com.thundersharp.bombaydine.user.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllAddressHolderAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.CartItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.CategoryAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.PlacesAutoCompleteAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.TopsellingAdapter;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.location.DistanceFromCoordinates;
import com.thundersharp.bombaydine.user.core.location.StorageFailure;
import com.thundersharp.bombaydine.user.core.utils.DataUtils;
import com.thundersharp.bombaydine.user.core.utils.LatLongConverter;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.ui.account.UpdateProfileActivity;
import com.thundersharp.bombaydine.user.ui.dailyfood.DailyfoodActivity;
import com.thundersharp.bombaydine.user.ui.location.HomeLocationChooser;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllCategoryActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.bombaydine.user.ui.menu.TopSellingAll;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.bombaydine.user.ui.offers.CustomOfferActivity;
import com.thundersharp.bombaydine.user.ui.orders.ConfirmPhoneName;
import com.thundersharp.bombaydine.user.ui.orders.OrderStatus;
import com.thundersharp.bombaydine.user.ui.orders.RecentOrders;
import com.thundersharp.bombaydine.user.ui.scanner.QrScanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private MaterialCardView breakfast,lunch,dinner;
    private RelativeLayout bottomnoti_order;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    public static BottomSheetDialog bottomSheetDialog;
    public static BottomSheetDialog bottomSheetDialogloc;
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
    private TextView itemtotal,grandtot,promoamt,delehevry, name_phone;

    /**
     * Address Listeners and helpers
     */

    private AddressHelper addressHelper;
    private SharedPrefHelper sharedPrefHelper;
    private List<LatLng> latLngs;
    private HomeDataProvider homeDataProvider;

    private ShimmerFrameLayout shimmerplace_cat,shimmerplace_topsell;

    private TextView version;
    private boolean isVisible;
    private LinearLayout bottom_clickable_linear, bottom_clickable_linear_order;
    private TextView view_action;
    private RelativeLayout bottomnoti;
    private ImageView clearcompleate;

    private TextView view_action_order;
    private ImageView clearcompleate_order;
    private AppCompatButton pay;
    private EditText search_home;
    private RelativeLayout containermain;
    private CordinatesInteractor cordinatesInteractor;
    private SharedPreferences sharedPreferences;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    private static List<Object> foodItemAdapterListStatic = new ArrayList<>();

    OfflineDataProvider offlineDataProvider;
    SwipeRefreshLayout swipe_refresh;

    OrederBasicDetails last_order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        containermain = view.findViewById(R.id.containermain);

        version = view.findViewById(R.id.version);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version.setText("V "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);

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
        breakfast = view.findViewById(R.id.breakfast);
        lunch = view.findViewById(R.id.lunch);
        dinner = view.findViewById(R.id.dinner);
        bottomnoti = view.findViewById(R.id.bottomnoti);
        bottomnoti_order = view.findViewById(R.id.bottomnoti_order);
        clearcompleate = view.findViewById(R.id.clearcompleate);
        clearcompleate_order = view.findViewById(R.id.clearcompleate_order);
        bottom_clickable_linear = view.findViewById(R.id.bottom_clickable_linear);
        bottom_clickable_linear_order = view.findViewById(R.id.bottom_clickable_linear_order);
        view_action = view.findViewById(R.id.view_action);
        view_action_order = view.findViewById(R.id.view_action_order);
        search_home=view.findViewById(R.id.search_home);

        Animator
                .initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast, containermain);

        swipe_refresh = view.findViewById(R.id.swipe);
        swipe_refresh.setRefreshing(true);

        refresh(view);

        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.d("TAG", "Refreshed token: " + task.getResult());
                        FirebaseDatabase
                                .getInstance()
                                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("FCM_TOKEN")
                                .setValue(task.getResult()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });



        swipe_refresh.setOnRefreshListener(() -> {
            refresh(view);
            swipe_refresh.setRefreshing(false);
        });
        return view;
    }

    private void refresh(View view) {

        offlineDataProvider = OfflineDataProvider.getInstance(getContext());
        sharedPrefHelper = new SharedPrefHelper(getContext(), this);

        homeDataProvider = new HomeDataProvider(getActivity(), this, this, this, this);

        sharedPreferences = getActivity().getSharedPreferences(CONSTANTS.PROFILE_NODE_PROFILEPICURI, Context.MODE_PRIVATE);

        bottomnoti.setVisibility(View.INVISIBLE);
        bottomnoti_order.setVisibility(View.GONE);
        isVisible = false;

        mRequestQueue = Volley.newRequestQueue(getContext());

        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));
        //pinCodeInteractor = new PinCodeInteractor(getContext(),this);
        addressHelper = new AddressHelper(getActivity(), this);

        //recyclerView = (RecyclerView) view.findViewById(R.id.places_recycler_view);


        if (offlineDataProvider.doSharedPrefExists()){
            if (!offlineDataProvider.fetchitemfromStorage().equalsIgnoreCase("[]")){

                Animator.initializeAnimator().slideUp(bottomnoti);

            }else {
                offlineDataProvider.clearSharedPref();
            }
        }

        clearcompleate.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "Clicked ", Toast.LENGTH_SHORT).show();
            offlineDataProvider.clearSharedPref();
            Animator.initializeAnimator().slideDown(bottomnoti);
            refreshAdapter();
        });

        clearcompleate_order.setOnClickListener(view1 -> {
            Animator.initializeAnimator().slideDown(bottomnoti_order);
            bottomnoti_order.setVisibility(View.GONE);
        });


        /*
        bottom_clickable_linear.setOnClickListener(view1 -> {
            if (bottomnoti.getVisibility() == View.VISIBLE)
                startActivity(new Intent(getActivity(), AllItemsActivity.class));
            //showcart(view);
        });

        bottom_clickable_linear_order.setOnClickListener(view1 -> {
            if (bottomnoti_order.getVisibility() == View.VISIBLE) {
                if (last_order !=null) OrderStatus.showOrderStatus(getActivity(),last_order);
            }
        });
         */


        view_action.setOnClickListener(view1 -> {
            if (bottomnoti.getVisibility() == View.VISIBLE)
                startActivity(new Intent(getActivity(), AllItemsActivity.class));
            //showcart(view);
        });

        view_action_order.setOnClickListener(view1 -> {
            if (bottomnoti_order.getVisibility() == View.VISIBLE) {
                if (last_order !=null){
                    OrderStatus.showOrderStatus(getActivity(),last_order);
                }else {
                    bottomnoti_order.setVisibility(View.GONE);
                }
            }else {
                bottomnoti_order.setVisibility(View.GONE);
            }
            //showcart(view);
        });



        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
            Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profile);
        }else {
            Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(profile);
        }

        breakfast.setOnClickListener(view123 -> DailyfoodActivity.getInstance(getActivity(),0));

        lunch.setOnClickListener(view1 -> DailyfoodActivity.getInstance(getActivity(),1));

        dinner.setOnClickListener(view1r ->  DailyfoodActivity.getInstance(getActivity(),2));

        topsellingallv.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), TopSellingAll.class)));   ///TODO checkout with model

        allitemsview.setOnClickListener(view13 -> startActivity(new Intent(getActivity(), AllItemsActivity.class)));

        allcategory.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), AllCategoryActivity.class)));


        current_loc.setOnClickListener(viewlocation -> currentLocation(view));

        profile.setOnClickListener(viewclick -> navController.navigate(R.id.profile));

        recentorders.setOnClickListener(view16 -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(getActivity(), RecentOrders.class));
            } else {
                Toast.makeText(getContext(), "Kindly login to see your recent orders.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        qrcode.setOnClickListener(view15 -> startActivity(new Intent(getActivity(), QrScanner.class)));  //TODO Complete Scanner


        categoryRecycler = view.findViewById(R.id.recentordcategoryholderer);
        categoryRecycler.setHasFixedSize(true);



        homeDataProvider.fetchhomeAllCategories();
        homeDataProvider.fetchTopSelling();
        homeDataProvider.fetchHomeallItem();


        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(HomeFragment.this);

                                //add your extra information
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("PAGE",dataSnapshot.child("PAGE").getValue(Integer.class));
                                sliderView.bundle(bundle1);

                                mDemoSlider.addSlider(sliderView);
                            }


                        }else {
                            listUrl.add("https://thumbs.dreamstime.com/z/pizzeria-discount-banner-pepperoni-margherita-slices-italian-food-advertisement-pizza-tomatoes-mozzarella-ketchup-186048578.jpg");
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            for (int i = 0; i < listUrl.size(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                                // if you want show image only / without description text use DefaultSliderView instead

                                // initialize SliderLayout
                                sliderView
                                        .image(listUrl.get(i))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(HomeFragment.this);

                                //add your extra information
                                sliderView.bundle(bundle);

                                mDemoSlider.addSlider(sliderView);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(HomeFragment.this);
        mDemoSlider.stopCyclingWhenTouch(false);


        CartProvider cartProvider = CartProvider.initialize(getActivity(), new CartHandler.cart() {

            @Override
            public void onItemAddSuccess(boolean isAdded, List<CartItemModel> data) {
                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                if (bottomSheetDialog.isShowing()) updateCartData();
                refreshAdapter();
                if (data == null || data.isEmpty()){
                    Animator.initializeAnimator().slideDown(bottomnoti);


                }else {
                    if (bottomnoti.getVisibility() == View.GONE || bottomnoti.getVisibility() ==View.INVISIBLE){
                        Animator.initializeAnimator().slideUp(bottomnoti);
                    }
                }
            }

            @Override
            public void addFailure(Exception exception) {
                Toast.makeText(getActivity(), ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updated")) {
                    if (cartProvider != null)
                        cartProvider.syncData();
                }
            }

        };

        LoadOrderData();

        search_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (allItemAdapter!= null)
                    allItemAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("updated"));
        swipe_refresh.setRefreshing(false);

    }

    private void LoadOrderData() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            DatabaseReference reference = FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(CONSTANTS.DATABASE_NODE_ORDERS)
                    .child(CONSTANTS.DATABASE_NODE_OVERVIEW);

            Query query = reference.orderByKey().limitToFirst(1);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            last_order = snapshot1.getValue(OrederBasicDetails.class);
                        }
                        switch (last_order.getStatus()){
                            case "0" :
                            case "1" :
                            case "10":
                            case "2":
                            case "4":
                            case "5":
                            case "6":
                            case "8":
                            case "9":
                                Animator.initializeAnimator().slideUp(bottomnoti_order);
                                bottomnoti_order.setVisibility(View.VISIBLE);
                                break;
                            default:
                                bottomnoti_order.setVisibility(View.GONE);
                                break;



                        }

                    }else {
                        last_order = null;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), ""+error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void currentLocation(View view) {

        bottomSheetDialogloc = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
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
        cordinatesInteractor = new CordinatesInteractor(HomeFragment.this,getActivity());

        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cordinatesInteractor.fetchAllCoordinatesFromStorage();
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

        bottomSheetDialogloc.setContentView(bottomview);
        bottomSheetDialogloc.show();
    }


    /*
    private void showcart(View view) {

        View bottomview = LayoutInflater.from(getActivity()).inflate(R.layout.botomsheet_cart,view.findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView rec1 = bottomview.findViewById(R.id.rec1);
        TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        TextView delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
        TextView est_time = bottomview.findViewById(R.id.est_time);
        TextView ch_address = bottomview.findViewById(R.id.ch_address);
        TextView change_Name = bottomview.findViewById(R.id.change_Name);
        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);
        name_phone = bottomview.findViewById(R.id.name_phone);
        delevering_to_address.setText("Delivering to :"+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        long distance = Math.round(DistanceFromCoordinates.getInstance().convertLatLongToDistance(Resturant.resturantLatLong,LatLongConverter.initialize().getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG())));
        int time = (int) ((distance/ Resturant.averageSpaeed)*60)+ Resturant.averagePreperationTime;


        est_time.setText("Estimated delivery : "+time+" minutes");

        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()),getActivity(),1));
        updateCartData();

        shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(getActivity(), AllOffersActivity.class),001));
        bottomSheetDialog.show();

        ch_address.setOnClickListener(viewadd -> currentLocation(view));
        change_Name.setOnClickListener(addName ->startActivityForResult(new Intent(getActivity(), ConfirmPhoneName.class), 1008));
    }
     */


    private void updateCartData(){
        List<CartItemModel> data = offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage());

        if (data != null) {

            if (!data.isEmpty()) {
                double sum = 0.0;
                for (int i = 0; i < data.size(); i++) {
                    sum = sum + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                }

                double deleveryCharges = 0.0;
                deleveryCharges = (DistanceFromCoordinates
                        .getInstance()
                        .convertLatLongToDistance(
                                Resturant.resturantLatLong,
                                LatLongConverter.initialize().getlatlang(sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG()))) * Resturant.deliveryChargesPerKilometer;
                if (deleveryCharges > Resturant.maxDeliveryCharges) {
                    deleveryCharges = Resturant.maxDeliveryCharges;
                }


                itemtotal.setText("\u20B9 " + sum);
                delehevry.setText("\u20B9 " + Math.round(deleveryCharges));
                grandtot.setText("" + (sum + Math.round(deleveryCharges))); //TODO SUBTRACT DISCOUNT LATER
                pay.setText("PAY \u20B9"+(sum+Math.round(deleveryCharges)));

            } else {
                itemtotal.setText("\u20B9 0");
                delehevry.setText("\u20B9 0");
                promoamt.setText("\u20B9 0");
                grandtot.setText("\u20B9 0");
                pay.setText("PAY \u20B9 0");
            }
        }
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        switch (slider.getBundle().getInt("PAGE")){
            case 0:
                startActivity(new Intent(getActivity(),AllItemsActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(),RecentOrders.class));
                break;
            case 2:
                navController.navigate(R.id.discover);
                break;
            case 3:
                startActivity(new Intent(getActivity(),TopSellingAll.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(),AllCategoryActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), CustomOfferActivity.class));
                break;
            default:
        }

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
        shimmerFrameLayout.stopShimmer();
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
                                                    bottomSheetDialogloc.dismiss();

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
        }/*
         else if (resultCode == 1008) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (data.getData() != null) {
                    name_phone.setText(String.valueOf(data.getData()));
                } else
                    Toast.makeText(getActivity(), "Data fetch failure!", Toast.LENGTH_SHORT).show();//else name_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"+91 XXXXXXXXXX");
            } else Toast.makeText(getActivity(), "Login First", Toast.LENGTH_SHORT).show();
        }
        */
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
        createLocationRequest();
    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        if (exception instanceof StorageFailure){
            cordinatesInteractor.fetchAllCoordinates();
            Toast.makeText(getActivity(),"Storage failure",Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(getActivity(), ""+exception.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCategoryFetchSuccess(List<Object> data) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(data, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        categoryRecycler.setLayoutManager(gridLayoutManager);
        categoryRecycler.setAdapter(categoryAdapter);
        shimmerplace_cat.stopShimmer();
        shimmerplace_cat.setVisibility(View.GONE);
        Animator
                .initializeAnimator()
                .runRecyclerSlideRightAnimation(categoryRecycler);

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
        foodItemAdapterListStatic = data;
        allItemAdapter = new AllItemAdapter(data, getContext());

        horizontalScrollView.setAdapter(allItemAdapter);
        shimmerplace_allitem.stopShimmer();
        shimmerplace_allitem.setVisibility(View.GONE);
    }

    private void refreshAdapter(){
        allItemAdapter = new AllItemAdapter(foodItemAdapterListStatic, getContext());
        horizontalScrollView.setAdapter(allItemAdapter);
        shimmerplace_allitem.stopShimmer();
        shimmerplace_allitem.setVisibility(View.GONE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}