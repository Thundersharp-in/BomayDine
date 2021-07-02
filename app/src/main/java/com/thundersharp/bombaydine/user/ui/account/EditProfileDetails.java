package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.ChangeNumber;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.home.MainPage;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileDetails extends AppCompatActivity {

    TextInputLayout name, phone_no, address, email;
    TextView change_phone, edit_cover_pic;
    AppCompatButton save;
    ImageView cover_pic, edit_profile_pic;
    CircleImageView profile_pic;
    Uri profile_uri = null;
    String s_name = null;
    ChangeNumber model;
    SharedPrefHelper sharedPrefHelper;
    HashMap<String , Object> data;
    UserProfileChangeRequest.Builder builder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_details);
        sharedPrefHelper = new SharedPrefHelper(this);
        initViews();

        /*
        if (getIntent().getSerializableExtra("profile_data") != null){
            //name.getEditText().setText();
        }else {

        }

         */

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null)
            email.getEditText().setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            else email.getEditText().setText("");

            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null){
                s_name =FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                name.getEditText().setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }else s_name = null;


            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profile_pic);
                //profile_uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            }else {
                Glide.with(this).load(R.mipmap.ic_launcher_round).into(profile_pic);
                //profile_uri = null;
            }
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
            if (sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1() != null) address.getEditText().setText(sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1()); else address.setError("Set Location");

            address.getEditText().setEnabled(false);
            email.getEditText().setEnabled(false);

            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                email.setStartIconVisible(true);
                email.setError("Email not verified");
            }else email.setStartIconVisible(false);

            if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null){
                change_phone.setText("Change");
                phone_no.getEditText().setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            }else {
                change_phone.setText("Add");
                phone_no.setError("Add Number");
            }
        }else {
        Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
        finish();
        }

        address.setStartIconOnClickListener(v -> startActivityForResult(new Intent(EditProfileDetails.this, AddAddressActivity.class),300));

        email.setStartIconOnClickListener(view ->{
            if (email.getEditText().getText().toString().isEmpty()){
                Toast.makeText(this, "Email not found send to register page with sign out", Toast.LENGTH_SHORT).show();
            }else {
                if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    EmailLinkMethod();
                }else Toast.makeText(this, "Email can't be changed", Toast.LENGTH_SHORT).show();
            }
        });

        edit_profile_pic.setOnClickListener(view->{
            Intent photo = new Intent(Intent.ACTION_PICK);
            photo.setType("image/*");
            startActivityForResult(photo, 1);
        });

        change_phone.setOnClickListener(view ->{
            if (change_phone.getText().toString().equals("Change")){
                startActivityForResult(new Intent(EditProfileDetails.this, AddNumber.class),1009);
                Toast.makeText(this, "Change", Toast.LENGTH_SHORT).show();
            }else if (change_phone.getText().toString().equals("Add")){
                startActivityForResult(new Intent(EditProfileDetails.this, AddNumber.class),1009);
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(view ->{
            if (name.getEditText().getText().toString().isEmpty()){
                name.setError("Required!");
                name.requestFocus();
            }else {
                data = new HashMap<>();
                builder = new UserProfileChangeRequest.Builder();
                if (profile_uri != null){
                    StoreOnStorage(profile_uri);
                }else {
                    if (model!=null){
                        changeNumber(model);
                    }else {
                       startFlow();
                    }
                }

                /*
                if (s_name!=null){
                    if (s_name.equals(name.getEditText().getText().toString())){
                        if (profile_uri != null){
                            StoreOnStorage(profile_uri);
                        }else {
                            if (model!=null){
                                changeNumber(model);
                            }else {
                                Toast.makeText(this, "No changes found to update!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            //startFlow(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                        }
                    }else {
                        if (profile_uri != null){
                            StoreOnStorage(profile_uri);
                        }else {
                            if (model!=null){
                                changeNumber(model);
                            }else {
                                Toast.makeText(this, "No changes found to update!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            //startFlow(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                        }
                    }
                }else Toast.makeText(this, "Close the app . Again open it again for performing desired operation", Toast.LENGTH_SHORT).show();

                 */
                //sharedPrefHelper.saveNamePhoneData();
            }
        });

    }

    private void startFlow() {
        if (s_name!=null){
            if (s_name.equals(name.getEditText().getText().toString())) {
                if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null){
                    data.put("NAME", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    UpdateData(data);
                }else {
                    data.put("NAME", null);
                    UpdateData(data);
                }
            }else {
                builder.setDisplayName(name.getEditText().getText().toString());
                data.put("NAME", name.getEditText().getText().toString());
                UpdateData(data);
            }
        }else Toast.makeText(this, "Close the app . Again open it again for performing desired operation", Toast.LENGTH_SHORT).show();
    }

    private void UpdateData(HashMap<String, Object> data) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                .updateChildren(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){


                       UserProfileChangeRequest profileChangeRequest = builder.build();


                        UpdateUserProfile(profileChangeRequest);
                    }else {
                        Toast.makeText(EditProfileDetails.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void UpdateUserProfile(UserProfileChangeRequest builder){
       /*
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        //builder.setPhotoUri(Uri.parse(uri.toString()));
        UserProfileChangeRequest userProfileChangeRequest = builder.build();

        FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(task2 -> {

                });
        */
        FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .updateProfile(builder)
                .addOnCompleteListener(con->{
                    if (con.isSuccessful()){
                        Toast.makeText(EditProfileDetails.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, con.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (data!=null){
                profile_uri = data.getData();
                profile_pic.setImageURI(profile_uri);//data.put("PROFILEPICURI",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

            }else {
                Toast.makeText(this, "Image not found try again!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 300){
            address.getEditText().setText(String.valueOf(data.getData()));
        }
        if (requestCode == 1009){
            if (data != null) {
                model = (ChangeNumber) data.getSerializableExtra("no_verification");
                phone_no.getEditText().setText(model.getNumber());
            }

            //address.getEditText().setText(String.valueOf(data.getData()));
        }

    }

    public void changeNumber(@NonNull ChangeNumber model) {
        if (model.getVerified()) {
            FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .updatePhoneNumber(model.getPhoneAuthCredential())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                data.put("PHONE", model.getNumber());
                                startFlow();
                                /*
                                HashMap<String , Object> data = new HashMap<>();
                                data.put("PHONE", model.getNumber());
                                data.put("NAME", name);

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                        .updateChildren(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    UserProfileChangeRequest builder= new UserProfileChangeRequest
                                                            .Builder()
                                                            .setDisplayName(name)
                                                            .build();
                                                    UpdateUserProfile(builder);
                                                }else {

                                                }
                                            }
                                        });
                                 */
                            } else {
                                Toast.makeText(EditProfileDetails.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() !=null){
                data.put("PHONE", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                startFlow();
            }else {
                data.put("PHONE", null);
                startFlow();
            }
            Toast.makeText(this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }

        /*
        FirebaseAuth
                .getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//(Activity) context
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            //setLoginSucessListner(task,true,true);

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                    .child("UID")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                //setLoginSucessListner(task,true,true);
                                            }else {
                                                HashMap<String,Object> data = new HashMap<>();
                                                data.put("NAME",null);
                                                data.put("EMAIL",null);
                                                data.put("PHONE",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                                data.put("PROFILEPICURI",null);
                                                data.put("CREATEDON",System.currentTimeMillis());
                                                data.put("UID",FirebaseAuth.getInstance().getUid());

                                                FirebaseDatabase
                                                        .getInstance()
                                                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                                                        .setValue(data)
                                                        .addOnCompleteListener(task1 -> {

                                                            if (task1.isSuccessful()){
                                                                //setLoginSucessListner(task,true,true);
                                                            }

                                                        }).addOnFailureListener(e -> {
                                                    Toast.makeText(AddNumber.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        } else {
                            // Sign in failed, display a message and update the UI
                            throwfailure(task.getException(),1);

                        }
                    }
                });

         */
    }

    private void StoreOnStorage(Uri profile_url) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile/"+FirebaseAuth.getInstance().getUid()+".jpg");
        storageReference.putFile(profile_url).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String path = task.getResult()
                        .getMetadata()
                        .getPath();
                task.getResult()
                        .getStorage()
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            if (uri!=null){
                                profile_uri = uri;
                                builder.setPhotoUri(profile_uri);
                                if (model!=null){
                                    changeNumber(model);
                                }else {
                                    data.put("PROFILEPICURI", profile_uri.toString());
                                    //data.put("/"+FirebaseAuth.getInstance().getUid()+"/"+ CONSTANTS.DATABASE_NODE_PERSONAL_DATA+"/PROFILEPICURI",profile_uri);
                                    startFlow();
                                }
                            }else {
                                if (model!=null){
                                    changeNumber(model);
                                }else {
                                    if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                                        data.put("PROFILEPICURI", FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                                        //data.put("/"+FirebaseAuth.getInstance().getUid()+"/"+ CONSTANTS.DATABASE_NODE_PERSONAL_DATA+"/PROFILEPICURI",profile_uri);
                                        startFlow();
                                    }else {
                                        data.put("PROFILEPICURI", null);
                                        //data.put("/"+FirebaseAuth.getInstance().getUid()+"/"+ CONSTANTS.DATABASE_NODE_PERSONAL_DATA+"/PROFILEPICURI",profile_uri);
                                        startFlow();
                                    }
                                }
                            }

                        });
            }else {
                Toast.makeText(EditProfileDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void EmailLinkMethod() {
        FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileDetails.this, "Email verification link sent,\nCheckout your registered mail id", Toast.LENGTH_SHORT).show();
                        //FirebaseAuth.getInstance().signOut();
                        //startActivity(new Intent(EditProfileDetails.this, LoginActivity.class));
                        //finish();

                    } else {
                        Toast.makeText(EditProfileDetails.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                /*
                ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        //.setUrl("https://www.thundersharp.in/")
                        //.setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        //.setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.thundersharp.bombaydine",
                                true,
        "1"    )
                        .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email.getEditText().getText().toString(), actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileDetails.this, "Sent!", Toast.LENGTH_SHORT).show();
                            Intent mailClient = new Intent(Intent.ACTION_VIEW);
                            mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                            startActivity(mailClient);
                            // Log.d(TAG, "Email sent.");
                        }else
                            Toast.makeText(EditProfileDetails.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                 */
    }

    private void initViews() {
        name = findViewById(R.id.name);
        phone_no = findViewById(R.id.phone_no);
        address = findViewById(R.id.address);
        change_phone = findViewById(R.id.change_phone);
        edit_cover_pic = findViewById(R.id.edit_cover_pic);
        save = findViewById(R.id.save);
        cover_pic = findViewById(R.id.cover_pic);
        edit_profile_pic = findViewById(R.id.edit_profile_pic);
        profile_pic = findViewById(R.id.profile_pic);
        email = findViewById(R.id.email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}