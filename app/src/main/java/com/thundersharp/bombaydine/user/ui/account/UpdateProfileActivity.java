package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    ImageView cover_pic;
    TextView location_type, profile_nme, profile_eml, last_address;
    CircleImageView profile_img;
    SharedPreferences sharedPreferences, cover_url;
    Uri profile_uri=null, cover_uri=null;
    ImageView location_img;
    String UID;
    TextView desc;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        cover_url = getSharedPreferences("cover_url",MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(CONSTANTS.PROFILE_NODE_PROFILEPICURI, Context.MODE_PRIVATE);

        initViews();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().reload();
            UID = FirebaseAuth.getInstance().getUid();
            profile_eml.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profile_nme.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profile_img);
                profile_uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            }else {
                Glide.with(this).load(R.mipmap.ic_launcher_round).into(profile_img);
                profile_uri = null;
            }
            
            sharedPrefHelper = new SharedPrefHelper(this);
            location_type.setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME());
            last_address.setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1()+" "+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE2());

            if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME().equalsIgnoreCase("Home")){
                location_img.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_outline_home_24));
            }else if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME().equalsIgnoreCase("Office")) {
                location_img.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_outline_home_work_24));
            }else if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME().equalsIgnoreCase("Others")){
                location_img.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_outline_share_location_24));
            }

            /*
            String cover = cover_url.getString("cover_pic", null);
            if (cover != null) {
                //cover_pic.setImageURI(Uri.parse(cover));
                //Bitmap bitmap = BitmapFactory.decodeFile(cover);
               // cover_pic.setImageBitmap(BitmapFactory.decodeFile(cover));
                Glide.with(this).load(cover).into(cover_pic);
                Toast.makeText(this, ""+cover, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
                Glide.with(this).load(R.drawable.download).into(cover_pic);
            }

             */
        }else {
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        ((MaterialCardView)findViewById(R.id.edit_profile_pic)).setOnClickListener(v -> {
            Intent photo = new Intent(Intent.ACTION_PICK);
            photo.setType("image/*");
            startActivityForResult(photo, 1);
        });

        ((MaterialCardView)findViewById(R.id.edit_profile_details)).setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() !=null){
                startActivity(new Intent(UpdateProfileActivity.this, EditProfileDetails.class));
                finish();
            }else {
                Toast.makeText(this, "Login First!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ((MaterialCardView)findViewById(R.id.add_review)).setOnClickListener(v -> {
            //startActivity(new Intent());
            //TODO ADD REVIEW ACTIVITY
        });

        ((MaterialCardView)findViewById(R.id.add_images)).setOnClickListener(v -> {
            //TODO ADD EDIT ACTIVITY
        });

        ((CardView)findViewById(R.id.btn_location_overview)).setOnClickListener(v -> startActivity(new Intent(UpdateProfileActivity.this, AddAddressActivity.class)));

        ((TextView)findViewById(R.id.edit_cover_pic)).setOnClickListener(v -> {
            Intent coverphoto = new Intent(Intent.ACTION_PICK);
            coverphoto.setType("image/*");
            startActivityForResult(coverphoto, 2);
        });

        ((CardView)findViewById(R.id.delete_account)).setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser()!=null)
            DeleteAccount();else Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
        });

    }

    private void initViews() {
        profile_nme = findViewById(R.id.profile_nme);
        profile_eml = findViewById(R.id.profile_eml);
        cover_pic = findViewById(R.id.cover_pic);
        location_type = findViewById(R.id.location_type);
        last_address = findViewById(R.id.last_address);
        profile_img = findViewById(R.id.profile_img);
        location_img = findViewById(R.id.location_img);
        desc = findViewById(R.id.desc);
    }

    private void DeleteAccount() {//TODO Why Login Again
        Toast.makeText(this, "Deleting...", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().getCurrentUser().reload();
        FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase
                                .getInstance().getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                .child(UID)
                                .removeValue()
                                .addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()){
                                        if (profile_uri!=null) {
                                            FirebaseStorage
                                                    .getInstance()
                                                    .getReference("Profile/"+FirebaseAuth.getInstance().getUid()+".jpg")
                                                    .delete()
                                                    //.getReferenceFromUrl(profile_uri.toString())
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(this, "sTorage cleared", Toast.LENGTH_SHORT).show();
                                                            Delete();
                                                        }else {
                                                            Toast.makeText(this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Delete();
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(this, "Storage not found", Toast.LENGTH_SHORT).show();
                                            Delete();
                                        }
                                    }else {
                                        Toast.makeText(UpdateProfileActivity.this, "Close the Application !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        Toast.makeText(this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onResume() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        FirebaseAuth.getInstance().getCurrentUser().reload();
        super.onResume();

    }

    private void Delete() {
        sharedPrefHelper.clearSavedHomeLocationData();
        Toast.makeText(UpdateProfileActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
        this.finishAffinity();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                profile_uri = data.getData();
                profile_img.setImageURI(profile_uri);//data.put("PROFILEPICURI",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

                StoreOnStorage(profile_uri);

            }else if (requestCode == 2){
                cover_pic.setImageURI(null);
                cover_uri = data.getData();
                cover_pic.setImageURI(cover_uri);
                Toast.makeText(this, ""+cover_uri, Toast.LENGTH_SHORT).show();
                //saveCoverPicToSF(cover_uri);
            }
        }else {
            Toast.makeText(this, "Image not found !", Toast.LENGTH_SHORT).show();
        }
    }

    private void StoreOnStorage(Uri profile_url) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile/"+FirebaseAuth.getInstance().getUid()+".jpg");
        storageReference.putFile(profile_url).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    String path = task.getResult()
                            .getMetadata()
                            .getPath();
                    task.getResult()
                            .getStorage()
                            .getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                HashMap<String,Object> data = new HashMap<>();
                                data.put("/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_PERSONAL_DATA+"/PROFILEPICURI",uri.toString());
                                profile_uri = uri;
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                        .updateChildren(data)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {

                                                UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                                                builder.setPhotoUri(Uri.parse(uri.toString()));
                                                UserProfileChangeRequest userProfileChangeRequest = builder.build();

                                                FirebaseAuth
                                                        .getInstance()
                                                        .getCurrentUser()
                                                        .updateProfile(userProfileChangeRequest)
                                                        .addOnCompleteListener(task2 -> {

                                                        });

                                                Toast.makeText(UpdateProfileActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(UpdateProfileActivity.this, "Unsuccessful! Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            });
                }else {
                    Toast.makeText(UpdateProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    private void saveCoverPicToSF(Uri cover_uri) {
        if (cover_uri != null){
            SharedPreferences.Editor editor = cover_url.edit();
            editor.clear();
            editor.putString("cover_pic",cover_uri.toString());
            editor.apply();
        }
    }

     */


}