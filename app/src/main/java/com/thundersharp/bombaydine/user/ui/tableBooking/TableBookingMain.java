package com.thundersharp.bombaydine.user.ui.tableBooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.SlotTimeHolderAdapter;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.tableactions.listeners.GuestChangeListener;
import com.thundersharp.tableactions.view.TableGuestCounter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TableBookingMain extends Fragment {

    private TableGuestCounter tableGuestCounter;
    private TextView display_data,month_display,display_total_guest;
    private LinearLayout cal_container;
    private RelativeLayout guest_container;
    private CompactCalendarView compactCalendar_view;
    private ImageView calendar_toggle,drop_icon,slot_Icon;
    private Date bookingDate;
    private ImageView parking,bar, cigarette,couple,roof,wifi;
    private boolean toggle_cal,toggle_no_of_guest, toggle_time_slot;
    private RecyclerView time_slots;

    private int tablesCount,guestCount;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_table_booking_main, container, false);

        initializeViews(view);

        ((TextView)view.findViewById(R.id.profile_email)).setText("Indian, Italian,  Thai, Chinese\n"+ Resturant.resturant+", Bangalore");

        tableGuestCounter.setNoOfGuestChangeListener(new GuestChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onGuestAdded(int numberOfNewGuests, int totalNumberOfGuests) {
                guestCount = totalNumberOfGuests;
                display_total_guest.setText("I/We will be in total "+totalNumberOfGuests+" guest/s, "+tablesCount +" tables required.");
            }

            @Override
            public void onGuestRemoved(int numberOfRemovedGuests, int totalNumberOfGuests) {
                guestCount = totalNumberOfGuests;
                display_total_guest.setText("I/We will be in total "+totalNumberOfGuests+" guest/s, "+tablesCount +" tables required.");
            }
        });

        tableGuestCounter.setOnTableChangeListener(noOfTables -> {
            tablesCount = noOfTables;
            display_total_guest.setText("I/We will be in total "+guestCount+" guest/s, "+tablesCount +" tables required.");
        });

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
}