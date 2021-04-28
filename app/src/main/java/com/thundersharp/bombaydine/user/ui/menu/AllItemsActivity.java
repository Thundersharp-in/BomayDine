package com.thundersharp.bombaydine.user.ui.menu;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;

import java.util.List;

public class AllItemsActivity extends AppCompatActivity implements HomeDataContract.AllItems,HomeDataContract.DataLoadFailure {

    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this,this,this);

        recyclermain = findViewById(R.id.recyclermain);
        homeDataProvider.fetchAllitems();

    }


    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnallItemsFetchSucess(List<Object> data) {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);
    }
}