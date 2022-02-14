package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.AllItemAdapterMainCreateAdapter;
import com.thundersharp.admin.core.Adapters.AllSelectItemAdapter;
import com.thundersharp.admin.core.Data.HomeDataContract;
import com.thundersharp.admin.core.Data.HomeDataProvider;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.Model.TopSellingModel;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.progress.ProgressDilog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditDailyFoodActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recycler;
    private AppCompatButton update;
    private AllSelectItemAdapter adapter;
    private int data;
    private String allotment_data;
    private List<String> availableIds;
    private List<Object> datalist;
    public static HashMap<String,FoodItemAdapter> selected_foodItemAdapterList;

    ProgressDilog progressDilog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_food);

        toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycler);
        update = findViewById(R.id.update);
        availableIds = new ArrayList<>();
        datalist = new ArrayList<>();
        selected_foodItemAdapterList = new HashMap<>();

        progressDilog = new ProgressDilog(EditDailyFoodActivity.this);

        if (getIntent().getIntExtra("foodType",-1) != -1){
            data = getIntent().getIntExtra("foodType",-1);
        }else {
            Toast.makeText(EditDailyFoodActivity.this, "Unexpected ERROR!", Toast.LENGTH_SHORT).show();
            finish();
        }

        switch (data){
            case 0:
                allotment_data = CONSTANTS.DATABASE_NODE_BREAKFAST;
                fetchFromDB(allotment_data);
                break;
            case 1:
                allotment_data = CONSTANTS.DATABASE_NODE_LUNCH;
                fetchFromDB(allotment_data);
                break;
            case 2:
                allotment_data = CONSTANTS.DATABASE_NODE_DINNER;
                fetchFromDB(allotment_data);
                break;
            default:
                Toast.makeText(EditDailyFoodActivity.this, "Internal ERROR!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        LoadDataFromDB();


        update.setOnClickListener(v -> {
            progressDilog.show();
            Toast.makeText(EditDailyFoodActivity.this, "Size:"+selected_foodItemAdapterList.size(), Toast.LENGTH_SHORT).show();
            HashMap<String,Object> data = new HashMap<>();
            data.put(allotment_data,selected_foodItemAdapterList);
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .updateChildren(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(EditDailyFoodActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                                selected_foodItemAdapterList = new HashMap<>();
                                progressDilog.stop();
                                finish();
                            }else {
                                Toast.makeText(EditDailyFoodActivity.this, "OOPS! Sorry something unusual happened", Toast.LENGTH_SHORT).show();
                                progressDilog.stop();
                            }
                        }
                    });
        });


    }

    private void LoadDataFromDB() {
        progressDilog.show();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                datalist.add(dataSnapshot.getValue(FoodItemAdapter.class));
                            }
                            adapterData(datalist);
                        }else {
                            Toast.makeText(EditDailyFoodActivity.this, "ERROR 404 : NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void adapterData(List<Object> datalist) {
        adapter = new AllSelectItemAdapter(datalist,availableIds, EditDailyFoodActivity.this);
        progressDilog.stop();
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
    }

    private void fetchFromDB(String data) {
        progressDilog.show();
        FirebaseDatabase
                .getInstance()
                .getReference(data)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                availableIds.add(snapshot1.getKey());
                            }
                        }
                        progressDilog.stop();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDilog.stop();
                        Toast.makeText(EditDailyFoodActivity.this, "ERROR:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}