package com.thundersharp.bombaydine.user.ui.orders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.animation.Animator;

public class OrderStatus extends AppCompatActivity {

    public static void showOrderStatus(Context context, OrederBasicDetails orederBasicDetails){
        context.startActivity(new Intent(context,OrderStatus.class).putExtra("data",orederBasicDetails));
    }

    private OrederBasicDetails orederBasicDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        if (getIntent().getSerializableExtra("data") != null) {
            orederBasicDetails = (OrederBasicDetails)getIntent().getSerializableExtra("data");
        }else {
            Toast.makeText(this, "Error in getting details", Toast.LENGTH_SHORT).show();
            finish();
        }



    }


}

