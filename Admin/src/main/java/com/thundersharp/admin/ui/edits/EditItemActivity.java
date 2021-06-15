package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.utils.AlertCreater;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.gallary.FirebaseGallary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EditItemActivity extends AppCompatActivity {

    private AutoCompleteTextView customerAutoTV,foodType;
    private MaterialCardView onlineGalary,choosefromdevice;
    private List<String> nameCat,idCat;
    private TextInputLayout imageurl,nameFood,description,amount;
    private ImageView homeImage;
    private CheckBox foodAvailable;
    private FoodItemAdapter foodItemModel;
    private AppCompatButton submitbtn;
    private boolean isEdit = false;
    private Integer catPos;
    private Dialog dialog;

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
        submitbtn = findViewById(R.id.submitbtn);
        choosefromdevice = findViewById(R.id.dinner);
        dialog = AlertCreater.initialize(this).createAlert("Please Hold on a while we process your request.Patience is bitter, but its fruit is sweet !!");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;

        choosefromdevice.setOnClickListener(v->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 10089);
        });

        foodItemModel = (FoodItemAdapter) getIntent().getSerializableExtra("dataMain");

        if (foodItemModel != null){
            isEdit = true;
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
        customerAutoTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                catPos = i;
            }
        });

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

        submitbtn.setOnClickListener( view -> {
            if (isEdit){
                if (amount.getEditText().getText().toString().isEmpty()){
                    amount.setError("Amount cannot be empty");
                    amount.requestFocus();
                }else if (catPos == null){
                    customerAutoTV.setError("Category required");
                    customerAutoTV.requestFocus();
                }else if (nameFood.getEditText().getText().toString().isEmpty()){
                    nameFood.setError("Name cannot be blank");
                    nameFood.requestFocus();
                }else if (imageurl.getEditText().getText().toString().isEmpty()){
                    imageurl.setError("Image url is required");
                    imageurl.requestFocus();
                }else if (description.getEditText().getText().toString().isEmpty()){
                    description.setError("Description is required");
                    description.requestFocus();
                }else if (foodType.getText().toString().isEmpty()){
                    foodType.setError("Select food type");
                    foodType.requestFocus();
                }else {
                    dialog.show();
                    int foodTypen;
                    if (foodType.getText().toString().equalsIgnoreCase("Vegetarian")) {
                        foodTypen = 0;
                    } else if (foodType.getText().toString().equalsIgnoreCase("Non-vegetarian")) {
                        foodTypen = 1;
                    } else if (foodType.getText().toString().equalsIgnoreCase("Eggs")) {
                        foodTypen = 2;
                    } else foodTypen = 0;

                    if (catPos != null) {

                        FoodItemAdapter itemUpdate = new FoodItemAdapter(Double.parseDouble(amount.getEditText().getText().toString()), foodAvailable.isChecked(), customerAutoTV.getText().toString() + "%&" + idCat.get(catPos), description.getEditText().getText().toString(), foodTypen, imageurl.getEditText().getText().toString(), nameFood.getEditText().getText().toString(), foodItemModel.ID);

                        AdminHelpers
                                .getInstance(this)
                                .setExternalUpdatePaths(CONSTANTS.DATABASE_NODE_ALL_ITEMS + "/" + itemUpdate.ID, CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS + "/" + getCatID(itemUpdate.CAT_NAME_ID) + "/" + itemUpdate.ID)
                                .setListner(new AdminHelpers.Update() {
                                    @Override
                                    public void updateSuccess() {
                                        if (!getCatID(foodItemModel.CAT_NAME_ID).equals(getCatID(itemUpdate.CAT_NAME_ID))) {
                                            AdminHelpers
                                                    .getInstance(EditItemActivity.this)
                                                    .setExternalDeletePaths(CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS + "/" + getCatID(foodItemModel.CAT_NAME_ID) + "/" + foodItemModel.ID)
                                                    .deletePaths();

                                        }

                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void updateFailure() {
                                        dialog.dismiss();
                                    }
                                })
                                .updateTOPaths(itemUpdate);


                    } else {
                        Toast.makeText(this, "Category not selected please select.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }

            }else {

                if (amount.getEditText().getText().toString().isEmpty()){
                    amount.setError("Amount cannot be empty");
                    amount.requestFocus();
                }else if (catPos == null){
                    customerAutoTV.setError("Category required");
                    customerAutoTV.requestFocus();
                }else if (nameFood.getEditText().getText().toString().isEmpty()){
                    nameFood.setError("Name cannot be blank");
                    nameFood.requestFocus();
                }else if (imageurl.getEditText().getText().toString().isEmpty()){
                    imageurl.setError("Image url is required");
                    imageurl.requestFocus();
                }else if (description.getEditText().getText().toString().isEmpty()){
                    description.setError("Description is required");
                    description.requestFocus();
                }else if (foodType.getText().toString().isEmpty()){
                    foodType.setError("Select food type");
                    foodType.requestFocus();
                }else {
                    dialog.show();
                    int foodTypen;
                    if (foodType.getText().toString().equalsIgnoreCase("Vegetarian")) {
                        foodTypen = 0;
                    } else if (foodType.getText().toString().equalsIgnoreCase("Non-vegetarian")) {
                        foodTypen = 1;
                    } else if (foodType.getText().toString().equalsIgnoreCase("Eggs")) {
                        foodTypen = 2;
                    } else foodTypen = 0;

                    Random random = new Random();
                    FoodItemAdapter itemUpdate = new FoodItemAdapter(Double.parseDouble(amount.getEditText().getText().toString()), foodAvailable.isChecked(), customerAutoTV.getText().toString() + "%&" + idCat.get(catPos), description.getEditText().getText().toString(), foodTypen, imageurl.getEditText().getText().toString(), nameFood.getEditText().getText().toString(),String.valueOf(random.nextInt(99999999)));
                    AdminHelpers
                            .getInstance(this)
                            .setExternalUpdatePaths(CONSTANTS.DATABASE_NODE_ALL_ITEMS + "/" + itemUpdate.ID,
                                    CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS + "/" + getCatID(itemUpdate.CAT_NAME_ID) + "/" + itemUpdate.ID)
                            .setListner(new AdminHelpers.Update() {
                                @Override
                                public void updateSuccess() {
                                    Toast.makeText(EditItemActivity.this,"Updated",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                                @Override
                                public void updateFailure() {
                                    Toast.makeText(EditItemActivity.this,"Cannot update",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            })
                            .updateTOPaths(itemUpdate);


                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1356){
            if (data != null) {
                imageurl.getEditText().setText(data.getParcelableExtra("data").toString());
                Glide.with(EditItemActivity.this).load(imageurl.getEditText().getText().toString()).into(homeImage);

            }else Toast.makeText(EditItemActivity.this,"Error in receiving image data",Toast.LENGTH_LONG).show();
        }else if (requestCode ==10089 && resultCode ==RESULT_OK){
            Uri pickedImage = data.getData();
            upLoadImageToStorage(pickedImage);
        }
    }

    private void upLoadImageToStorage(Uri storageImgUri){
        dialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("CategoryImages/"+System.currentTimeMillis()+".jpg");
        storageReference.putFile(storageImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    String path = task.getResult()
                            .getMetadata()
                            .getPath();
                    task.getResult()
                            .getStorage()
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap<String,Object> data = new HashMap<>();
                                    data.put("path",path);
                                    data.put("uri",uri.toString());

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference("UPLOADED_IMAGES")
                                            .child(String.valueOf(System.currentTimeMillis()))
                                            .updateChildren(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        imageurl.getEditText().setText(uri.toString());
                                                        Glide.with(EditItemActivity.this).load(uri.toString()).into(homeImage);
                                                        dialog.dismiss();

                                                    }else {
                                                        FirebaseDatabase.getInstance().getReference(path).removeValue();
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                }else {
                    dialog.dismiss();
                    Toast.makeText(EditItemActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    
    private ArrayList<String> getFoodTypeList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("Vegetarian");
        customers.add("Non-vegetarian");
        customers.add("Eggs");
        return customers;
    }


}