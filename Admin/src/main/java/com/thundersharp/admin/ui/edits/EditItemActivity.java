package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.gallary.FirebaseGallary;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    AutoCompleteTextView customerAutoTV,foodType;
    MaterialCardView onlineGalary;
    List<String> nameCat,idCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameCat=new ArrayList<>();
        idCat = new ArrayList<>();

        customerAutoTV = findViewById(R.id.cat_text);
        foodType = findViewById(R.id.food_text);
        onlineGalary = findViewById(R.id.imageGallary);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditItemActivity.this, android.R.layout.simple_spinner_dropdown_item, getFoodTypeList());
        foodType.setAdapter(adapter);

        onlineGalary.setOnClickListener(n->{
            startActivityForResult(new Intent(this, FirebaseGallary.class),1356);
        });
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_CATEGORY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                nameCat.add(dataSnapshot.child("NAME").getValue(String.class));
                                idCat.add(dataSnapshot.child("ID").getValue(String.class));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditItemActivity.this, android.R.layout.simple_spinner_dropdown_item, nameCat);
                            customerAutoTV.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private ArrayList<String> getCustomerList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("food1");
        customers.add("food2");
        customers.add("food3");
        customers.add("food4");
        customers.add("food5");
        customers.add("food6");
        customers.add("food7");
        customers.add("food9");
        customers.add("food8");
        return customers;
    }


    private ArrayList<String> getFoodTypeList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("Vegetarian");
        customers.add("Non-vegetarian");
        customers.add("Eggs");
        return customers;
    }


}