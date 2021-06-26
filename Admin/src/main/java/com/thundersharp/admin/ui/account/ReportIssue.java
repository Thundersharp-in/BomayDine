package com.thundersharp.admin.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.ReportAdapter;
import com.thundersharp.admin.core.Model.ReportModel;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

public class ReportIssue extends AppCompatActivity {

    RecyclerView rv_report;
    List<ReportModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        rv_report = findViewById(R.id.rv_report);
        modelList = new ArrayList<>();
        /*
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));
        modelList.add(new ReportModel("Name","Email","uid","phone","msg","id","type"));

         */

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_REPORT)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                modelList.add(snapshot1.getValue(ReportModel.class));
                            }
                            rv_report.setAdapter(new ReportAdapter(ReportIssue.this, modelList));
                        }else {
                            Toast.makeText(ReportIssue.this, "No Report found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReportIssue.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}