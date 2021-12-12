package com.thundersharp.bombaydine.user.ui.tableBooking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.tableactions.view.TableGuestCounter;

public class TableBookingMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_table_booking_main, container, false);

        ((TextView)view.findViewById(R.id.profile_email)).setText("Indian, Italian,  Thai, Chinese\n"+ Resturant.resturant+", Bangalore");


        return view;
    }
}