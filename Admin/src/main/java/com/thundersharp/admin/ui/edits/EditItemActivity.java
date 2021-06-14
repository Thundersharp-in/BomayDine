package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.gallary.FirebaseGallary;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private AutoCompleteTextView customerAutoTV,foodType;
    private MaterialCardView onlineGalary;
    private List<String> nameCat,idCat;
    private TextInputLayout imageurl,nameFood,description,amount;
    private ImageView homeImage;
    private CheckBox foodAvailable;
    private FoodItemAdapter foodItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameCat =new ArrayList<>();
        idCat = new ArrayList<>();

        customerAutoTV = findViewById(R.id.cat_text);
        foodType = findViewById(R.id.food_text);
        onlineGalary = findViewById(R.id.imageGallary);
        imageurl = findViewById(R.id.imgurl);
        nameFood = findViewById(R.id.nameFood);
        description = findViewById(R.id.description);
        amount = findViewById(R.id.amount);
        foodAvailable = findViewById(R.id.foodAvailable);
        homeImage = findViewById(R.id.imagehome);

        foodItemModel = (FoodItemAdapter) getIntent().getSerializableExtra("dataMain");

        if (foodItemModel != null){

            imageurl.getEditText().setText(foodItemModel.ICON_URL);
            nameFood.getEditText().setText(foodItemModel.NAME);
            description.getEditText().setText(foodItemModel.DESC);
            amount.getEditText().setText(String.valueOf(foodItemModel.AMOUNT));
            foodAvailable.setChecked(foodItemModel.AVAILABLE);
            customerAutoTV.setText(getcatName(foodItemModel.CAT_NAME_ID));
            Glide.with(this).load(foodItemModel.ICON_URL).into(homeImage);
            switch (foodItemModel.FOOD_TYPE){
                case 0:
                    foodType.setText("Vegetarian");
                    break;
                case 1:
                    foodType.setText("Non-vegetarian");
                    break;
                case 2:
                    foodType.setText("Eggs");
                    break;
            }

        }


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
        if (resultCode == 1356){
            if (data != null) {
                imageurl.getEditText().setText(data.getParcelableExtra("data").toString());
                Glide.with(EditItemActivity.this).load(data.getData()).into(homeImage);

            }else Toast.makeText(EditItemActivity.this,"Error in receiving image data",Toast.LENGTH_LONG).show();
        }else Toast.makeText(EditItemActivity.this,"Error in receiving image data 3",Toast.LENGTH_LONG).show();
    }

    public String getcatName(String key){
        if (key.contains("%&")){
            return key.substring(0,key.indexOf("%&")).toLowerCase();
        }else return null;
    }

    public String getCatID(String key){
        if (key.contains("&")){
            return key.substring(key.indexOf("&")+1);
        }else return null;
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