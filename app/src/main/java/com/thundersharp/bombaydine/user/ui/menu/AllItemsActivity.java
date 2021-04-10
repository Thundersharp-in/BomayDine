package com.thundersharp.bombaydine.user.ui.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity {

    private RecyclerView recyclermain;
    private List<Object> objectList = new ArrayList<>();
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);


        recyclermain = findViewById(R.id.recyclermain);
        loadtempdata();

    }

    public void loadtempdata(){
        objectList.clear();

        for (int i=0; i<= 15; i++){
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("image","url");
            objectList.add(hashMap);
        }
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(objectList,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);
    }
}