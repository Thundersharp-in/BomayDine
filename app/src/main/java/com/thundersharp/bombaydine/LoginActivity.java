package com.thundersharp.bombaydine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    EditText editTextCarrierNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.pkr);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);

        countryCodePicker.registerCarrierNumberEditText(editTextCarrierNumber);
    }
}