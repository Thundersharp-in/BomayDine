package com.thundersharp.bombaydine.user.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ReportSeriousIssue extends AppCompatActivity {

    private AutoCompleteTextView reportType;
    private TextInputLayout fullName,emailAddress,mobileNo,typeText;
    AppCompatButton submitbtn;
    String ph_no;

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
            if (mobileNo.getEditText().getText().toString().isEmpty()) ph_no = "";else ph_no = mobileNo.getEditText().getText().toString();
            if (fullName.getEditText().getText().toString().isEmpty()){

                fullName.setError("Name can't be blank");

            }else if (emailAddress.getEditText().getText().toString().isEmpty()){
                emailAddress.setError("Email can't be empty");

            }else if (typeText.getEditText().getText().toString().isEmpty()){
                typeText.setError("Message field can't be blank");

            }else if (reportType.getText().toString().isEmpty()){
                Toast.makeText(this, "Mention Report type !", Toast.LENGTH_SHORT).show();
            }else if (!isValidEmail(emailAddress.getEditText().getText().toString())){
                Toast.makeText(this, "Provided email is not valid!", Toast.LENGTH_SHORT).show();
            }else {

                HashMap<String, Object> model = new HashMap<>();
                model.put("NAME", fullName.getEditText().getText().toString());
                model.put("EMAIL", emailAddress.getEditText().getText().toString());
                model.put("UID", FirebaseAuth.getInstance().getUid());
                model.put("PHONE", ph_no);
                model.put("MESSAGE", typeText.getEditText().getText().toString());
                model.put("ID", String.valueOf(System.currentTimeMillis()));
                model.put("TYPE", reportType.getText().toString());

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_REPORT)
                        .child(model.get("ID").toString())
                        .setValue(model)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                mobileNo.getEditText().setText("");
                                fullName.getEditText().setText("");
                                emailAddress.getEditText().setText("");
                                typeText.getEditText().setText("");
                                reportType.setText("");
                                Toast.makeText(ReportSeriousIssue.this, "Your Report has been registered", Toast.LENGTH_SHORT).show();

                            }
                            else
                                Toast.makeText(ReportSeriousIssue.this, "Sorry you can't report right now", Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }



    private List<String> detData() {
        List<String> da = new ArrayList<>();
        da.add("Report a accident");
        da.add("Report a incident");
        return da;
    }
}