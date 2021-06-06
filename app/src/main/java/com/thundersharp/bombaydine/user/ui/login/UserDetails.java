package com.thundersharp.bombaydine.user.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.home.MainPage;

import java.util.HashMap;

public class UserDetails extends AppCompatActivity {

    TextInputLayout name,email,password;
    AppCompatButton buttonsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
        buttonsubmit = findViewById(R.id.buttonsubmit);


        buttonsubmit.setOnClickListener(btn -> {
            if (email.getEditText().getText().toString().isEmpty()){
                email.getEditText().setError("Enter your name");
                email.getEditText().requestFocus();

            }else if (name.getEditText().getText().toString().isEmpty()){
                name.getEditText().setError("Enter your name");
                name.getEditText().requestFocus();
            }else if (password.getEditText().getText().toString().isEmpty()){
                password.getEditText().setError("Enter your name");
                password.getEditText().requestFocus();
            }else {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("NAME",name.getEditText().getText().toString());
                hashMap.put("EMAIL",email.getEditText().getText().toString());
                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(CONSTANTS.DATABASE_NODE_PERSONAL_DATA)
                        .updateChildren(hashMap)
                        .addOnCompleteListener(ts ->{
                           if (ts.isSuccessful()){
                               AuthCredential authCredential = EmailAuthProvider
                                               .getCredential(email.getEditText().getText().toString(),password.getEditText().getText().toString());
                               UserProfileChangeRequest builder= new UserProfileChangeRequest
                                       .Builder()
                                       .setDisplayName(name.getEditText().getText().toString())
                                       .build();
                               FirebaseAuth
                                       .getInstance()
                                       .getCurrentUser()
                                       .linkWithCredential(authCredential)
                                       .addOnCompleteListener(tsw -> {
                                          if (tsw.isSuccessful()){
                                              //UPDATE STATUS IN PROGRESS
                                              FirebaseAuth
                                                      .getInstance()
                                                      .getCurrentUser()
                                                      .sendEmailVerification()
                                                      .addOnCompleteListener(task -> {
                                                          if (task.isSuccessful()) Toast.makeText(this,"Email verification link sent",Toast.LENGTH_LONG).show();
                                                      });
                                              FirebaseAuth
                                                      .getInstance()
                                                      .getCurrentUser()
                                                      .updateProfile(builder)
                                                      .addOnCompleteListener(con->{
                                                          if (con.isSuccessful()){

                                                              startActivity(new Intent(this, MainPage.class));
                                                          }else
                                                              Toast.makeText(this, con.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                      });

                                          }else Toast.makeText(this, tsw.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                       });
                           }else {
                               Toast.makeText(this, ts.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                        });
            }
        });


    }
}