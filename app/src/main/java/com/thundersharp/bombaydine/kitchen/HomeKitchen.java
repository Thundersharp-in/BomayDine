package com.thundersharp.bombaydine.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.login.AccountHelper;
import com.thundersharp.bombaydine.user.ui.startup.MainActivity;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;

public class HomeKitchen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kitchen);

        findViewById(R.id.acccswitch).setOnClickListener(view -> {

            ChatStarter chatStarter = ChatStarter.initializeChat(this);
            chatStarter.setChatType(ChatStarter.MODE_CHAT_ADMIN);
            chatStarter.setSenderName("Hrishikesh Prateek");
            chatStarter.setCustomerId(FirebaseAuth.getInstance().getUid());
            chatStarter.setSenderUid("SUPPORT56065");

            try {
                chatStarter.startChat();
            } catch (ParametersMissingException e) {
                e.printStackTrace();
            }

         /*   AccountHelper
                    .getInstance(this)
                    .clearAllData();
            startActivity(new Intent(this, MainActivity.class));
            finish();*/

        });


    }
}