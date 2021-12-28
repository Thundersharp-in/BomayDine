package com.thundersharp.bombaydine.user.ui.tableBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.RecentTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class TableBookingHistory extends AppCompatActivity {

    private RecyclerView orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking_history);

        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(i -> finish());
        orderHistory = findViewById(R.id.recycler_view_table_booking);
        orderHistory.setLayoutManager(new LinearLayoutManager(this));

        orderHistory.setAdapter(RecentTableAdapter.initialize(this,getData()));

    }
    private List<Object> getData(){
        List<Object> da = new ArrayList<>();
        da.add("");
        da.add("");
        da.add("");
        da.add("");
        da.add("");
        return da;
    }
}