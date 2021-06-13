package com.thundersharp.admin.ui.edits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.thundersharp.admin.R;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        init();
    }

    private void init() {
        final AutoCompleteTextView customerAutoTV = findViewById(R.id.cat_text);
        ArrayList<String> customerList = getCustomerList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditItemActivity.this, android.R.layout.simple_spinner_item, customerList);
        customerAutoTV.setAdapter(adapter);
    }

    private ArrayList<String> getCustomerList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("food1");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        customers.add("food2");
        return customers;
    }
}