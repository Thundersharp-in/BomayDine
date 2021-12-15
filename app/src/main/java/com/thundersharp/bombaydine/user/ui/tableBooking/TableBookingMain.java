package com.thundersharp.bombaydine.user.ui.tableBooking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.tableactions.listeners.ChairInteraction;
import com.thundersharp.tableactions.listeners.GuestChangeListener;
import com.thundersharp.tableactions.view.TableGuestCounter;

public class TableBookingMain extends Fragment {

    TableGuestCounter tableGuestCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_table_booking_main, container, false);

        ((TextView)view.findViewById(R.id.profile_email)).setText("Indian, Italian,  Thai, Chinese\n"+ Resturant.resturant+", Bangalore");
        tableGuestCounter = view.findViewById(R.id.tableGuestCounter);

        tableGuestCounter.setNoOfGuestChangeListener(new GuestChangeListener() {

            @Override
            public void onGuestAdded(int numberOfNewGuests, int totalNumberOfGuests) {
                Toast.makeText(getActivity(), "Added : "+numberOfNewGuests+ " Total : " + totalNumberOfGuests, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGuestRemoved(int numberOfRemovedGuests, int totalNumberOfGuests) {
                Toast.makeText(getActivity(), "Removed : "+numberOfRemovedGuests+ " Total : " + totalNumberOfGuests, Toast.LENGTH_SHORT).show();
            }
        });

        tableGuestCounter.setChairInteractionListener(new ChairInteraction() {
            @Override
            public void onChairClicked(int chairPos) {
                Toast.makeText(getActivity(), "Clicked chair on POS : "+chairPos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChairsLongPressed(int chairPos) {
                Toast.makeText(getActivity(), "Long pressed  chair on POS : "+chairPos, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}