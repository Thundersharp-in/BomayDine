package com.thundersharp.bombaydine.user.ui.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.ui.edits.CreateOffer;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationDetail extends AppCompatActivity {

    TextView page_info;
    TextInputLayout completeAddress, address, latlong, city, country, state, local_name, zip, location_type;
    AppCompatButton save_changes;
    AddressData addressData;
    AutoCompleteTextView loc_text;
    List<String> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        initViews();

        if (getIntent().getSerializableExtra("locdet")!=null){
            addressData = (AddressData) getIntent().getSerializableExtra("locdet");
            if (getIntent().getBooleanExtra("edit",false)){
                //isEdit = true;
                save_changes.setText("Save Changes");
            }else {
                //isEdit = false;
                //save_changes.setText("Edit Details");
                //page_info.setText("If you want to modify location detail like Local Name and location type and address then click edit button below to start edit.\n else if you want to edit other details then go back to edit detail page to modify other details.");

            }
            setData(addressData);

        }else {
            Toast.makeText(this, "PROBLEM : ADDRESS DATA NOT FOUND !", Toast.LENGTH_SHORT).show();
            finish();
        }


        types = getLocType();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LocationDetail.this, android.R.layout.simple_spinner_item, types);
        loc_text.setAdapter(adapter);

        loc_text.setOnItemClickListener((parent, view, position, id) -> {
            //loc_text.setText(types.get(position));
            //type = position;
        });

        save_changes.setOnClickListener(v -> {
            if (addressData.getADDRESS_LINE2().equalsIgnoreCase("null")){
                if (loc_text.getText().toString().isEmpty()){
                    Toast.makeText(this, "Select Location type first!", Toast.LENGTH_SHORT).show();
                }else {
                    addressData.setADDRESS_NICKNAME(loc_text.getText().toString());
                    updateLocation(addressData);
                }
            }else {
                if (address.getEditText().getText().toString().isEmpty()){
                    address.getEditText().setError("Required!");
                    address.getEditText().requestFocus();
                }else if (local_name.getEditText().getText().toString().isEmpty()){
                    local_name.getEditText().setError("Required!");
                    local_name.getEditText().requestFocus();
                }else if (loc_text.getText().toString().isEmpty()){
                    Toast.makeText(this, "Select Location type first!", Toast.LENGTH_SHORT).show();
                }else {
                    addressData.setADDRESS_NICKNAME(loc_text.getText().toString());
                    addressData.setADDRESS_LINE2("State: $"+state.getEditText().getText().toString()+"# Country: %"+country.getEditText().getText().toString()+"* Address: @"+address.getEditText().getText().toString()+"^ KnownName: !"+local_name.getEditText().getText().toString());
                    updateLocation(addressData);
                }
                //addressline2.setText("State: $"+state+"# Country: %"+country+"* Address: @"+address+"^ KnownName: !"+knownName);
            }
        });

    }

    private void updateLocation(AddressData addressData) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ADDRESS+"/"+String.valueOf(addressData.getID()), addressData);
        FirebaseDatabase
                .getInstance()
                .getReference()
                .updateChildren(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }else Toast.makeText(this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setData(AddressData addressData) {
        completeAddress.getEditText().setText(addressData.getADDRESS_LINE1());
        latlong.getEditText().setText(addressData.getLAT_LONG());
        city.getEditText().setText(addressData.getCITY());
        zip.getEditText().setText(String.valueOf(addressData.getZIP()));

        if (addressData.getADDRESS_LINE2().equalsIgnoreCase("null")){
            state.getEditText().setText("");
            country.getEditText().setText("");
            local_name.getEditText().setText("");
            address.getEditText().setText("");
            address.getEditText().setEnabled(false);
            local_name.getEditText().setEnabled(false);
            page_info.setText("You can only modify location type else if you want to edit other details then go back to edit detail page to modify other details.");

        }else {
            state.getEditText().setText(addressData.getADDRESS_LINE2().substring(addressData.getADDRESS_LINE2().indexOf("$")+1,addressData.getADDRESS_LINE2().indexOf("#")));
            country.getEditText().setText(addressData.getADDRESS_LINE2().substring(addressData.getADDRESS_LINE2().indexOf("%")+1,addressData.getADDRESS_LINE2().indexOf("*")));
            local_name.getEditText().setText(addressData.getADDRESS_LINE2().substring(addressData.getADDRESS_LINE2().indexOf("!")+1));
            address.getEditText().setText(addressData.getADDRESS_LINE2().substring(addressData.getADDRESS_LINE2().indexOf("@")+1,addressData.getADDRESS_LINE2().indexOf("^")));
            address.getEditText().setEnabled(true);
            local_name.getEditText().setEnabled(true);
            page_info.setText("You can modify location detail like Local Name and location type and address else if you want to edit other details then go back to edit detail page to modify other details.");

        }

        switch (addressData.getADDRESS_NICKNAME()){
            case "Home" :
                loc_text.setText("Home");
                break;
            case "Office" :
                loc_text.setText("Office");break;
            case "Others" :
                loc_text.setText("Others");break;
        }

    }

    private List<String> getLocType() {
        List<String> typ = new ArrayList<>();
        typ.add("Home");
        typ.add("Office");
        typ.add("Others");
        return typ;
    }

    private void initViews() {
        page_info = findViewById(R.id.page_info);
        completeAddress = findViewById(R.id.completeAddress);
        address = findViewById(R.id.address);
        latlong = findViewById(R.id.latlong);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        local_name = findViewById(R.id.local_name);
        zip = findViewById(R.id.zip);
        location_type = findViewById(R.id.location_type);
        save_changes = findViewById(R.id.save_changes);
        loc_text = findViewById(R.id.loc_text);

    }
}