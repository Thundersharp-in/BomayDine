package com.thundersharp.bombaydine.user.ui.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;

public class ConfirmPhoneName extends AppCompatActivity {

    TextInputLayout name, phone_no;
    AppCompatButton buttonsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone_name);

        name = findViewById(R.id.name);
        phone_no = findViewById(R.id.phone_no);
        buttonsubmit = findViewById(R.id.buttonsubmit);
        /*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

        }
         */

        buttonsubmit.setOnClickListener(view->{
            if (name.getEditText().getText().toString().isEmpty()){
                name.getEditText().setError("Required!");
                name.getEditText().requestFocus();
            }else if (phone_no.getEditText().getText().toString().isEmpty() || phone_no.getEditText().getText().toString().length()!=10){
                phone_no.getEditText().setError("Required!");
                phone_no.getEditText().requestFocus();
            }else if (name.getEditText().getText().toString().contains(",")||phone_no.getEditText().getText().toString().contains(",")){
                name.getEditText().setError("Check format!");
                phone_no.getEditText().setError("Check format!");
            }else{
                setResult(1008,getIntent().setData(Uri.parse(name.getEditText().getText().toString()+",+91"+phone_no.getEditText().getText().toString())));
                finish();
                //Toast.makeText(this, "Procced to buy", Toast.LENGTH_SHORT).show();
            }
        });

    }
}