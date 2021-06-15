package com.thundersharp.admin.ui.edits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;
import com.thundersharp.admin.core.Model.OfferModel;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateOffer extends AppCompatActivity {

    AppCompatButton upload_btn;
    TextInputLayout offer_t_c, offer_off_upto, offer_percent, offerdesc, offer_type, offercode;
    Integer type = 1;
    List<Integer> types;
    AutoCompleteTextView offer_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        initViews();
        //types = new ArrayList<>();
        types = getCustomerList();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(CreateOffer.this, android.R.layout.simple_spinner_item, types);
        offer_text.setAdapter(adapter);


        upload_btn.setOnClickListener(view ->{

            if (offercode.getEditText().getText().toString().isEmpty()){
                offercode.getEditText().setError("Required!");
                offercode.getEditText().requestFocus();

            }else if (offer_percent.getEditText().getText().toString().isEmpty()){
                offer_percent.getEditText().setError("Required!");
                offer_percent.getEditText().requestFocus();

            }else if (offer_off_upto.getEditText().getText().toString().isEmpty()){
                offer_off_upto.getEditText().setError("Required!");
                offer_off_upto.getEditText().requestFocus();

            }else if (offer_text.getText().toString().isEmpty()){
                Toast.makeText(this, "Select offer type", Toast.LENGTH_SHORT).show();
            }else {



                Toast.makeText(this, "All set ready to upload with offer type "+ Integer.valueOf(offer_text.getText().toString()), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private List<Integer> getCustomerList() {
        List<Integer> typ = new ArrayList<>();
        typ.add(1);
        typ.add(2);
        typ.add(3);
        return typ;
    }

    private void initViews() {
        upload_btn = findViewById(R.id.upload_btn);
        offer_t_c = findViewById(R.id.offer_t_c);
        offer_off_upto = findViewById(R.id.offer_off_upto);
        offer_percent = findViewById(R.id.offer_percent);
        offerdesc = findViewById(R.id.offerdesc);
        offer_type = findViewById(R.id.offer_type);
        offercode = findViewById(R.id.offercode);
        offer_text = findViewById(R.id.offer_text);
    }
}