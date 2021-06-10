package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    ImageView cover_pic;
    TextView location_type, profile_nme, profile_eml, last_address;
    CircleImageView profile_img;
    SharedPreferences sharedPreferences, cover_url;
    Uri profile_uri=null, cover_uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        cover_url = getSharedPreferences("cover_url",MODE_PRIVATE);

        profile_nme = findViewById(R.id.profile_nme);
        profile_eml = findViewById(R.id.profile_eml);
        cover_pic = findViewById(R.id.cover_pic);
        location_type = findViewById(R.id.location_type);
        last_address = findViewById(R.id.last_address);
        profile_img = findViewById(R.id.profile_img);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            profile_eml.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profile_nme.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            if (sharedPreferences!=null){
                String profile_url = sharedPreferences.getString(CONSTANTS.DATABASE_NODE_PROFILEPICURI, null);
                if (profile_url != null){
                    Glide.with(this).load(profile_url).into(profile_img);
                }else Glide.with(this).load(R.mipmap.ic_launcher_round).into(profile_img);
            }
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
            location_type.setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_NICKNAME());
            last_address.setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1()+" "+sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE2());

            if (cover_url != null) cover_pic.setImageURI(Uri.parse(cover_url.getString("cover_pic",String.valueOf(R.mipmap.ic_launcher))));
        }

        ((MaterialCardView)findViewById(R.id.edit_profile_pic)).setOnClickListener(v -> {
            Intent photo = new Intent(Intent.ACTION_PICK);
            photo.setType("image/*");
            startActivityForResult(photo, 1);
        });

        ((MaterialCardView)findViewById(R.id.edit_profile_details)).setOnClickListener(v -> {
            //TODO ADD EDIT PROFILE ACTIVITY
        });

        ((MaterialCardView)findViewById(R.id.add_review)).setOnClickListener(v -> {
            //startActivity(new Intent());
            //TODO ADD REVIEW ACTIVITY
        });

        ((MaterialCardView)findViewById(R.id.add_images)).setOnClickListener(v -> {
            //TODO ADD EDIT ACTIVITY
        });

        ((CardView)findViewById(R.id.btn_location_overview)).setOnClickListener(v -> {
            startActivity(new Intent(UpdateProfileActivity.this, AddAddressActivity.class));
        });

        ((TextView)findViewById(R.id.edit_cover_pic)).setOnClickListener(v -> {
            Intent coverphoto = new Intent(Intent.ACTION_PICK);
            coverphoto.setType("image/*");
            startActivityForResult(coverphoto, 2);
        });

        ((CardView)findViewById(R.id.delete_account)).setOnClickListener(v -> {
            //TODO ADD DELETE ACTIVITY WITH OPTIONS
            //FirebaseAuth.getInstance().getCurrentUser().delete();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                profile_uri = data.getData();
            }else if (requestCode == 2){
                cover_uri = data.getData();
                cover_pic.setImageURI(cover_uri);
                saveCoverPicToSF(cover_uri);
            }
        }else {
            Toast.makeText(this, "Image not found !", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCoverPicToSF(Uri cover_uri) {
        if (cover_uri != null){
            SharedPreferences.Editor editor = cover_url.edit();
            editor.clear();
            editor.putString("cover_pic",String.valueOf(cover_uri));
            editor.apply();
        }
    }


}