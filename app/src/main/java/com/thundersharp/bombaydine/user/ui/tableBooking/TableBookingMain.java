package com.thundersharp.bombaydine.user.ui.tableBooking;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.ExtraChargesAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.ExtraServiceRequestAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.SlotTimeHolderAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.Model.CartOptionsModel;
import com.thundersharp.bombaydine.user.core.Model.PaymentsRequestOptions;
import com.thundersharp.bombaydine.user.core.location.LocationRequester;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.LocationUpdater;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.bombaydine.user.ui.account.Payments;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.orders.ConfirmPhoneName;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.payments.payments.PaymentObserver;
import com.thundersharp.tableactions.listeners.GuestChangeListener;
import com.thundersharp.tableactions.view.TableGuestCounter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.function.UnaryOperator;

import de.hdodenhof.circleimageview.CircleImageView;

public class
TableBookingMain extends Fragment implements PaymentObserver, DateSelectedListener {

    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private TableGuestCounter tableGuestCounter;
    private TextView display_data, month_display, display_total_guest;
    private LinearLayout cal_container;
    private RelativeLayout guest_container;
    private CompactCalendarView compactCalendar_view;
    private ImageView calendar_toggle, drop_icon, slot_Icon;
    private ImageView parking, bar, cigarette, couple, roof, wifi;
    private boolean toggle_cal, toggle_no_of_guest, toggle_time_slot;
    private RecyclerView time_slots;
    private AppCompatButton book_button;
    private Integer data;

    private TextView nameBott;
    private String dataName_Phone = FirebaseAuth
            .getInstance()
            .getCurrentUser()
            .getDisplayName() + ", " +
            FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .getPhoneNumber();


    /**
     * Variables to be passed to the bottom sheet
     */
    private Date bookingDate;
    private int tablesCount, guestCount;
    private double totalCartAmount = 0;

    public static Object time_slot;

    private DateSelectedListener dateSelectedListener;
    private TableDataInteractior tableDataInteractior;


    private List<String> tablesList;
    private List<String> timeSlots;
    private HashMap<String, DataSnapshot> todayBookingData;

    private boolean isTableAvailableForSlot = false;

    @Override
    public void onDateSelected(Date date) {
        fetchTimeSlotsAvailable(date);
    }

    private void fetchTimeSlotsAvailable(Date date) {

        if (date != null) {
            fetchData(date);
            bindTableDataListener(new TableDataInteractior() {
                @Override
                public void onDataFetchSuccess(List<String> tablesList, List<String> timeSlots, HashMap<String, DataSnapshot> todayBookingData) {
                    TableBookingMain.this.tablesList = tablesList;
                    TableBookingMain.this.timeSlots = timeSlots;
                    TableBookingMain.this.todayBookingData = todayBookingData;

                    time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    time_slots.setAdapter(new SlotTimeHolderAdapter(timeSlots));
                }
            });
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("posSlot")) {
                String selectedTime = intent.getStringExtra("timeSlot");
                for (String tableId : tablesList) {
                    DataSnapshot dataSnapshot = todayBookingData.get(tableId);
                    if (dataSnapshot != null) {

                        isTableAvailableForSlot = !dataSnapshot.child(selectedTime).exists();

                    } else isTableAvailableForSlot = true;
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("posSlot"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private LocationRequest locationRequest;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1005 && resultCode == 1008){
            dataName_Phone = data.getData().toString();
            if (nameBott != null) nameBott.setText(dataName_Phone);
        }

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                getFusedLocationProviderClient(getActivity())
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        LocationUpdater.getInstance(getContext()).saveCoOrdinates(new LatLng(locationResult.getLastLocation().getLatitude(),
                                                locationResult.getLastLocation().getLongitude()));

                                    }
                                },
                                Looper.myLooper());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(getActivity(), "Location is required to get current location", Toast.LENGTH_SHORT).show();

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

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);


                    getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            // do work here
                                            //SAVE CO_ORDINATE
                                            LocationUpdater.getInstance(getContext()).saveCoOrdinates(new LatLng(locationResult.getLastLocation().getLatitude(),
                                                    locationResult.getLastLocation().getLongitude()));
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

    boolean isTableFetched = false ,
            isTimeSlotFetched = false,
            isBookingFetched = false;

    private void fetchData(Date date) {
        isTableFetched=false;
        isBookingFetched = false;
        isTimeSlotFetched = false;
        List<String> tables,timeSlots;

        HashMap<String,DataSnapshot> bookingData = new HashMap<>();
        //List<Object> bookingData = new ArrayList<>();
        tables = new ArrayList<>();
        timeSlots = new ArrayList<>();


        FirebaseDatabase
                .getInstance()
                .getReference("ALL_TABLES")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot data : snapshot.getChildren()){
                                tables.add(data.getValue(String.class));
                            }
                        }
                        isTableFetched = true;
                        if (tableDataInteractior != null && isBookingFetched && isTimeSlotFetched){
                            tableDataInteractior.onDataFetchSuccess(tables,timeSlots,bookingData);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference("ALL_TIME_SLOTS")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot data : snapshot.getChildren()){
                                timeSlots.add(data.getValue(String.class));
                            }
                        }
                        isTimeSlotFetched = true;
                        if (tableDataInteractior != null && isBookingFetched && isTableFetched){
                            tableDataInteractior.onDataFetchSuccess(tables,timeSlots,bookingData);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference("BOOKED_TABLES")
                .child(TimeUtils.getDateFromTimeStamp(date.getTime()))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot data : snapshot.getChildren()){
                                bookingData.put(data.getKey(),data);
                            }
                        }
                        isBookingFetched = true;
                        if (tableDataInteractior != null && isTableFetched && isTimeSlotFetched){
                            tableDataInteractior.onDataFetchSuccess(tables,timeSlots,bookingData);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void bindTableDataListener(TableDataInteractior tableDataInteractior){
        this.tableDataInteractior = tableDataInteractior;
    }

    private void bindListener(DateSelectedListener dateSelectedListener){
        this.dateSelectedListener = dateSelectedListener;
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_table_booking_main, container, false);
        initializeViews(view);

        createLocationRequest();

        ((TextView)view.findViewById(R.id.profile_email)).setText("Indian, Italian,  Thai, Chinese\n"+ Resturant.resturant+", Bangalore");
        view.findViewById(R.id.profilepic).setOnClickListener(h -> startActivity(new Intent(getActivity(),TableBookingHistory.class)));

        tableGuestCounter.setNoOfGuestChangeListener(new GuestChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onGuestAdded(int numberOfNewGuests, int totalNumberOfGuests) {
                guestCount = totalNumberOfGuests;
                display_total_guest.setText("Guest/s "+guestCount+" Table/s : "+tablesCount );            }

            @Override
            public void onGuestRemoved(int numberOfRemovedGuests, int totalNumberOfGuests) {
                guestCount = totalNumberOfGuests;
                display_total_guest.setText("Guest/s "+guestCount+" Table/s : "+tablesCount );            }
        });


        tableGuestCounter.setOnTableChangeListener(noOfTables -> {
            tablesCount = noOfTables;
            display_total_guest.setText("Guest/s "+guestCount+" Table/s : "+tablesCount );
        });

        Date date = new Date();
        month_display.setText(TimeUtils.getMonthName(date.getMonth())+", "+(date.getYear()+1900));

        compactCalendar_view.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                if (dateClicked.getTime() < date.getTime() && dateClicked.getDate() != date.getDate()){
                    Toast.makeText(getActivity(), "You cannot book for past dates select dates greater than or equal to today.", Toast.LENGTH_SHORT).show();
                    compactCalendar_view.setCurrentDate(date);
                    month_display.setText(TimeUtils.getMonthName(date.getMonth())+", "+(date.getYear()+1900));

                }else if (dateClicked.getTime() > TimeUtils.getTimeStampOfOriginDaysBeforeAfter(7)){
                    compactCalendar_view.setCurrentDate(date);
                    Toast.makeText(getActivity(), "Booking cant be more than 7 days in advance.", Toast.LENGTH_SHORT).show();
                    month_display.setText(TimeUtils.getMonthName(date.getMonth())+", "+(date.getYear()+1900));

                }else {
                    bookingDate = dateClicked;
                    display_data.setText("I want a reservation on  " + TimeUtils.getDateFromTimeStamp(dateClicked.getTime()));
                    compactCalendar_view.hideCalendarWithAnimation();
                    toggle_cal = true;
                    month_display.setVisibility(View.GONE);
                    dateSelectedListener.onDateSelected(bookingDate);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month_display.setText(TimeUtils.getMonthName(firstDayOfNewMonth.getMonth())+", "+(firstDayOfNewMonth.getYear()+1900));
            }
        });

        calendar_toggle.setOnClickListener(n->{
            if (!compactCalendar_view.isAnimating()) {
                if (toggle_cal) {
                    compactCalendar_view.showCalendarWithAnimation();
                    month_display.setVisibility(View.VISIBLE);
                    toggle_cal = false;
                } else {
                    compactCalendar_view.hideCalendarWithAnimation();
                    month_display.setVisibility(View.GONE);
                    toggle_cal = true;
                }
            }
        });

        drop_icon.setOnClickListener(y->{
            if (toggle_no_of_guest){
                guest_container.setVisibility(View.VISIBLE);
                drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
                toggle_no_of_guest = false;
            }else {
                guest_container.setVisibility(View.GONE);
                drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ccp_ic_arrow_drop_down));
                toggle_no_of_guest = true;            }
        });

        view.findViewById(R.id.relative_container).setOnClickListener(b -> {

            if (toggle_no_of_guest){
                guest_container.setVisibility(View.VISIBLE);
                drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
                toggle_no_of_guest = false;
            }else {
                guest_container.setVisibility(View.GONE);
                drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ccp_ic_arrow_drop_down));
                toggle_no_of_guest = true;
            }
        });

        view.findViewById(R.id.relative_container_cal).setOnClickListener(b -> {
            if (!compactCalendar_view.isAnimating()) {
                if (toggle_cal) {
                    compactCalendar_view.showCalendarWithAnimation();
                    month_display.setVisibility(View.VISIBLE);
                    toggle_cal = false;
                } else {
                    compactCalendar_view.hideCalendarWithAnimation();
                    month_display.setVisibility(View.GONE);
                    toggle_cal = true;
                }
            }
        });

        slot_Icon.setOnClickListener(n->{
            if (toggle_time_slot){
                time_slots.setVisibility(View.VISIBLE);
                toggle_time_slot = false;
            }else {
                time_slots.setVisibility(View.GONE);
                toggle_time_slot = true;
            }
        });


        view.findViewById(R.id.time_container).setOnClickListener(n->{
            if (toggle_time_slot){
                time_slots.setVisibility(View.VISIBLE);
                toggle_time_slot = false;
            }else {
                time_slots.setVisibility(View.GONE);
                toggle_time_slot = true;
            }
        });

        book_button.setOnClickListener((t) -> {

            if (bookingDate == null){

                Snackbar snackbar = Snackbar.make(getContext(),book_button,"Please select date of your booking !!",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();

            }else if (time_slot == null){

                Snackbar snackbar = Snackbar.make(getContext(),book_button,"Please select a time slot among the available ones !!",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();

            }else if(tablesCount != 1){

                Snackbar snackbar = Snackbar.make(getContext(),book_button,"Currently "+tablesCount+" Tables are unavailable... reduce table count and retry !",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();

            }else if (!isTableAvailableForSlot){
                Snackbar snackbar = Snackbar.make(getContext(),book_button,"Currently Tables are unavailable for "+time_slot.toString()+" !",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }else {
                totalCartAmount = 0;
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());

                ExtraChargesAdapter extraChargesAdapter = new ExtraChargesAdapter(new ArrayList<CartOptionsModel>());

                View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.botomsheet_table_booking_cart, null, false);
                RecyclerView recyclerView = bottomView.findViewById(R.id.rec1);
                TextView guest_and_table_view = bottomView.findViewById(R.id.delevering_to_address);
                TextView booking_date = bottomView.findViewById(R.id.booking_date);
                TextView booking_time_slot = bottomView.findViewById(R.id.est_time);
                TextView unit_price = bottomView.findViewById(R.id.item_tot);
                TextView total_price = bottomView.findViewById(R.id.del_charges);
                TextView grand_total = bottomView.findViewById(R.id.grand_tot);
                RecyclerView recycler_view_extra_charges = bottomView.findViewById(R.id.recycler_view_extra_charges);
                AppCompatButton pay = bottomView.findViewById(R.id.paybtn);

                ExtraServiceRequestAdapter extraServiceRequestAdapter = new ExtraServiceRequestAdapter(getTableData());

                recyclerView.setAdapter(extraServiceRequestAdapter);
                recycler_view_extra_charges.setAdapter(extraChargesAdapter);

                booking_date.setText("Date of Booking "+TimeUtils.getDateFromTimeStamp(bookingDate.getTime()));
                guest_and_table_view.setText("Total number of guests "+guestCount+" Total number of Tables "+tablesCount);

                String startTime = time_slot.toString().substring(0,time_slot.toString().indexOf("-"));
                String endTime = time_slot.toString().substring(time_slot.toString().indexOf("-")+1);

                if (Integer.parseInt(startTime) > 12) {
                    booking_time_slot.setText("Selected Time Slot : "+(Integer.parseInt(startTime) - 12) + "PM - " + (Integer.parseInt(endTime) - 12)+"PM");
                }else if (Integer.parseInt(startTime) == 12){
                    booking_time_slot.setText("Selected Time Slot : "+startTime + "PM - " + (Integer.parseInt(endTime) - 12)+"PM");
                }else {
                    booking_time_slot.setText("Selected Time Slot : "+startTime + "AM - " + endTime+"AM");
                }

                extraServiceRequestAdapter
                        .setItemInteractionListener(new ExtraServiceRequestAdapter.ItemInteractionListener() {

                    @Override
                    public void onServiceItemAdded(CompoundButton compoundButton, double cartValueOut) {
                        totalCartAmount += cartValueOut;
                        grand_total.setText("\u20B9 "+totalCartAmount);
                        pay.setText("Pay \u20B9 "+totalCartAmount);
                        extraChargesAdapter.addItem(new CartOptionsModel(compoundButton.getText().toString(),cartValueOut));

                        Snackbar snackbar = Snackbar.make(getContext(),bottomView.getRootView(),"Added : "+compoundButton.getText(),Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.show();
                    }

                    @Override
                    public void onServiceItemRemoved(CompoundButton compoundButton, double cartValue) {
                        totalCartAmount -= cartValue;
                        grand_total.setText("\u20B9 "+totalCartAmount);
                        pay.setText("Pay \u20B9 "+totalCartAmount);
                        extraChargesAdapter.removeItem(new CartOptionsModel(compoundButton.getText().toString(),cartValue));

                        Snackbar snackbar = Snackbar.make(getContext(),bottomView.getRootView(),"Removed : "+compoundButton.getText(),Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();

                    }
                });


                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.TABLES_DATA)
                        .child("UNIT_PRICE")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){
                                    data = snapshot.getValue(Integer.class);
                                    unit_price.setText("\u20B9 "+data);
                                    total_price.setText("\u20B9 "+(data * tablesCount)+" ("+tablesCount+" X "+data+")");
                                    grand_total.setText("\u20B9 "+(data * tablesCount));
                                    pay.setText("Pay \u20B9 "+(data * tablesCount));
                                    totalCartAmount += (data*tablesCount);

                                }else {
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(getActivity(), "Table booking not available for now.", Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Cannot proceed further : "+error.toString(), Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });

                bottomView.findViewById(R.id.ch_date).setOnClickListener((V) -> {
                    bottomSheetDialog.dismiss();
                    if (!compactCalendar_view.isAnimating() && month_display.getVisibility() == View.GONE) {
                        compactCalendar_view.showCalendarWithAnimation();
                        month_display.setVisibility(View.VISIBLE);
                        toggle_cal = false;
                    }
                });

                pay.setOnClickListener((V) -> {

                    PaymentsRequestOptions.Builder paymentsRequestOptionsData = PaymentsRequestOptions
                            .initlizeBuilder()
                            .setCustomerEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .setCustomerPhone(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                            .setTransactionAmount(totalCartAmount)
                            .setMerchantTittle("Table Booking")
                            .build();

                    Payments.initialize(getActivity(),paymentsRequestOptionsData,this);

                });

                bottomView.findViewById(R.id.offersl).setOnClickListener((cc) -> {
                    Snackbar snackbar = Snackbar.make(getContext(),bottomView.getRootView(),"Currently no coupons available for you :(",Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                });

                nameBott = bottomView.findViewById(R.id.name_phone);nameBott.setText(dataName_Phone);
                bottomView.findViewById(R.id.change_Name).setOnClickListener(j->startActivityForResult(new Intent(getActivity(),ConfirmPhoneName.class),1005));
                bottomView.findViewById(R.id.ch_address).setOnClickListener((ClickListener) -> bottomSheetDialog.dismiss());

                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
            }

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setCancelable(true);

        parking.setOnClickListener(n->{
            builder.setMessage("Free parking for costumers and delivery valets available.");
            builder.create();
            builder.show();
        });

        bar.setOnClickListener(n->{
            builder.setMessage("Separate bar and resturant available in the same campus.");
            builder.create();
            builder.show();
        });

        cigarette.setOnClickListener(n->{
            builder.setMessage("Smoking area available.");
            builder.create();
            builder.show();
        });

        couple.setOnClickListener(n->{
            builder.setMessage("Romantic and couple friendly environment available");
            builder.create();
            builder.show();
        });

        roof.setOnClickListener(n->{
            builder.setMessage("Roof top dinner available.");
            builder.create();
            builder.show();
        });

        wifi.setOnClickListener(n->{
            builder.setMessage("Complimentary wifi access available.");
            builder.create();
            builder.show();
        });

        return view;
    }

    private void initializeViews(View view) {
        bindListener(this);
        tableGuestCounter = view.findViewById(R.id.tableGuestCounter);
        display_total_guest = view.findViewById(R.id.display_total_guest);
        month_display = view.findViewById(R.id.months_display);
        display_data = view.findViewById(R.id.display_data);
        cal_container = view.findViewById(R.id.cal_container);
        compactCalendar_view = view.findViewById(R.id.compactcalendar_view);
        drop_icon = view.findViewById(R.id.drop_icon);
        calendar_toggle = view.findViewById(R.id.calendar_toggle);
        guest_container = view.findViewById(R.id.guest_container);
        time_slots = view.findViewById(R.id.time_slots);
        slot_Icon = view.findViewById(R.id.slot_icon);
        book_button = view.findViewById(R.id.book_button);

        parking = view.findViewById(R.id.parking);
        bar = view.findViewById(R.id.bar);
        cigarette = view.findViewById(R.id.cigreete_bar);
        couple = view.findViewById(R.id.romantic);
        roof = view.findViewById(R.id.terrace);
        wifi = view.findViewById(R.id.free_wifi);

        toggle_cal = true;
        toggle_no_of_guest = true;
        guest_container.setVisibility(View.GONE);
        compactCalendar_view.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendar_view.hideCalendar();
    }
    

    @NonNull
    private List<CartOptionsModel> getTableData(){
        List<CartOptionsModel> data = new ArrayList<>();
        data.add(new CartOptionsModel("Request separate smoking room for guests (Subjects to availability)",0));
        data.add(new CartOptionsModel("Request Rooftop table setup (Rooftop table charges of Rs. 245 will be levied)",245));
        data.add(new CartOptionsModel("Romantic environment setup (Extra decoration charges of Rs. 385/- will be applied)",385));
        data.add(new CartOptionsModel("Free wifi access",0));
        return data;
    }


    @Override
    public void OnPaymentSuccess(String s, PaymentData paymentData) {
        String payId = s;
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(),TableBookingConfirmation.class));
    }

    @Override
    public void OnPaymentFailed(int i, String s, PaymentData paymentData) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            Toast.makeText(getActivity(), "Error code : "+jsonObject.getJSONObject("error").getString("code")+"\nDescription : "+jsonObject.getJSONObject("error").getString("description"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Internal error : "+s, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}

interface DateSelectedListener {
    void onDateSelected(Date date);
}

interface TableDataInteractior{
    void onDataFetchSuccess(List<String> tablesList, List<String> timeSlots, HashMap<String,DataSnapshot> todayBookingData);
}