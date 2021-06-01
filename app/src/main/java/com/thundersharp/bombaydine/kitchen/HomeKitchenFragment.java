package com.thundersharp.bombaydine.kitchen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;


public class HomeKitchenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_kitchen, container, false);

        view.findViewById(R.id.acccswitch).setOnClickListener(viewclk -> {

            ChatStarter chatStarter = ChatStarter.initializeChat(getActivity());
            chatStarter.setChatType(ChatStarter.MODE_CHAT_ADMIN);
            chatStarter.setCostomerName("Hrishikesh Prateek");
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

        return view;
    }

}