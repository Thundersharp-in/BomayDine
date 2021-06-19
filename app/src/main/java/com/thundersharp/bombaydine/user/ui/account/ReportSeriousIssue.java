package com.thundersharp.bombaydine.user.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.thundersharp.admin.ui.edits.EditItemActivity;
import com.thundersharp.bombaydine.R;

import java.util.ArrayList;
import java.util.List;

public class ReportSeriousIssue extends AppCompatActivity {

    private AutoCompleteTextView reportType;
    private TextInputLayout fullName,emailAddress,mobileNo,typeText;
    AppCompatButton submitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_serious_issue);

        reportType = findViewById(R.id.offer_text);
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        mobileNo = findViewById(R.id.mobileNo);
        typeText = findViewById(R.id.typrText);
        submitbtn = findViewById(R.id.submitbtn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,detData());
        reportType.setAdapter(adapter);

        submitbtn.setOnClickListener(n->{
            if (fullName.getEditText().getText().toString().isEmpty()){

                fullName.setError("Name can't be blank");

            }else if (emailAddress.getEditText().getText().toString().isEmpty()){

            }else if (typeText.getEditText().getText().toString().isEmpty()){

            }else {

            }
        });

    }

    private List<String> detData() {
        List<String> da = new ArrayList<>();
        da.add("Report a accident");
        da.add("Report a incident");
        return da;
    }
}