package com.thundersharp.admin.ui.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    List<Integer> types;
    AutoCompleteTextView offer_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        initViews();

        if (getIntent().getBooleanExtra("edit", false)){
            if (getIntent().getSerializableExtra("data") != null){
               OfferModel offerModel = ((OfferModel)getIntent().getSerializableExtra("data"));
                offer_t_c.getEditText().setText(offerModel.TNC);
                offer_off_upto.getEditText().setText(String.valueOf(offerModel.UPTO));
                offer_percent.getEditText().setText(String.valueOf(offerModel.PERCENT));
                offerdesc.getEditText().setText(offerModel.DESC);
                offer_type.getEditText().setText(String.valueOf(offerModel.TYPE));
                offercode.getEditText().setText(offerModel.CODE);
                upload_btn.setText("UPDATE");
            }
        }else {
            upload_btn.setText("UPLOAD");
        }

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

                int type = 1;
                String desc ,tc;

                if (offer_t_c.getEditText().getText().toString().isEmpty() || offer_t_c.getEditText().getText().toString() ==null) tc = "";else tc=offer_t_c.getEditText().getText().toString();
                if (offerdesc.getEditText().getText().toString().isEmpty() || offerdesc.getEditText().getText().toString() ==null) desc = "";else desc=offerdesc.getEditText().getText().toString();
                if (offer_text.getText().toString().isEmpty() || offer_text.getText().toString()==null)type=1;else type= Integer.parseInt(offer_text.getText().toString());

                String code = offercode.getEditText().getText().toString();
                if ( code.contains(" ")
                        ||code.contains(".")
                        ||code.contains("#")
                        ||code.contains("$")
                        ||code.contains("[")
                        ||code.contains("]")) {
                    code = code.replace(" ", "");
                    code = code.replace(".", "");
                    code = code.replace("#", "");
                    code = code.replace("$", "");
                    code = code.replace("[", "");
                    code = code.replace("]", "");

                }
                OfferModel offerModel = new OfferModel(code,
                        desc,
                        tc,
                        Integer.valueOf(offer_percent.getEditText().getText().toString()),
                        type,
                        Integer.valueOf(offer_off_upto.getEditText().getText().toString()));

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                        .child(offerModel.CODE)
                        .setValue(offerModel)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                offer_t_c.getEditText().setText("");
                                offercode.getEditText().setText("");
                                offerdesc.getEditText().setText("");
                                offer_percent.getEditText().setText("");
                                offer_off_upto.getEditText().setText("");
                                offer_text.setText("");
                                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                            }else
                                Toast.makeText(this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        });



                //Toast.makeText(this, "All set ready to upload with offer type "+ Integer.valueOf(offer_text.getText().toString()), Toast.LENGTH_SHORT).show();
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