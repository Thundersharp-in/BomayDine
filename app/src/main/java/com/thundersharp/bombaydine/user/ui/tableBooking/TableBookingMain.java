package com.thundersharp.bombaydine.user.ui.tableBooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.ExtraChargesAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.ExtraServiceRequestAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.SlotTimeHolderAdapter;
import com.thundersharp.bombaydine.user.core.Model.CartOptionsModel;
import com.thundersharp.bombaydine.user.core.Model.PaymentsRequestOptions;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.bombaydine.user.ui.account.Payments;
import com.thundersharp.bombaydine.user.ui.orders.ConfirmPhoneName;
import com.thundersharp.payments.payments.PaymentObserver;
import com.thundersharp.tableactions.listeners.GuestChangeListener;
import com.thundersharp.tableactions.view.TableGuestCounter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TableBookingMain extends Fragment implements PaymentObserver {

    private TableGuestCounter tableGuestCounter;
    private TextView display_data,month_display,display_total_guest;
    private LinearLayout cal_container;
    private RelativeLayout guest_container;
    private CompactCalendarView compactCalendar_view;
    private ImageView calendar_toggle,drop_icon,slot_Icon;
    private ImageView parking,bar, cigarette,couple,roof,wifi;
    private boolean toggle_cal,toggle_no_of_guest, toggle_time_slot;
    private RecyclerView time_slots;
    private AppCompatButton book_button;
    private Integer data;

    private TextView nameBott;
    private String dataName_Phone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+", "+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();


    /**
     * Variables to be passed to the bottom sheet
     */
    private Date bookingDate;
    private int tablesCount,guestCount;
    private double totalCartAmount = 0;

    public static Object time_slot;


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_table_booking_main, container, false);

        initializeViews(view);

        ((TextView)view.findViewById(R.id.profile_email)).setText("Indian, Italian,  Thai, Chinese\n"+ Resturant.resturant+", Bangalore");
        view.findViewById(R.id.profilepic).setOnClickListener(h->startActivity(new Intent(getActivity(),TableBookingHistory.class)));

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
                bookingDate = dateClicked;
                display_data.setText("I want a reservation on  "+TimeUtils.getDateFromTimeStamp(dateClicked.getTime()));
                compactCalendar_view.hideCalendarWithAnimation();
                toggle_cal = true;
                month_display.setVisibility(View.GONE);
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
                booking_time_slot.setText("Selected Time Slot : "+time_slot.toString());

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



        time_slots.setLayoutManager(new GridLayoutManager(getActivity(),3));

        //TODO: UPDATE FROM DATABASE
        time_slots.setAdapter(new SlotTimeHolderAdapter(getData()));

        return view;
    }

    private void initializeViews(View view) {
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

    private List<String> getData(){
        List<String> data = new ArrayList<>();
        for (int i= 0; i<8;i++){
            data.add(i+1+" AM - "+(i+3)+" PM");
        }

        return data;
    }

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
    }

    @Override
    public void OnPaymentFailed(int i, String s, PaymentData paymentData) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            Toast.makeText(getActivity(), "Error code : "+jsonObject.getJSONObject("error").getString("code")+"\nDescription : "+jsonObject.getJSONObject("error").getString("description"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Internal error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005 && resultCode == 1008){
            dataName_Phone = data.getData().toString();
            if (nameBott != null) nameBott.setText(dataName_Phone);
        }
    }
}