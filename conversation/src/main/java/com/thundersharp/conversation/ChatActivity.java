package com.thundersharp.conversation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.thundersharp.conversation.utils.Constants;


public class ChatActivity extends AppCompatActivity {

    FrameLayout frameLayout;

    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String name,
                                     String senderUid,
                                     int chatType) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_NAME,name);
        intent.putExtra(Constants.ARG_SENDER_UID,senderUid);
        intent.putExtra("CHAT_TYPE",chatType);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_starter);

        frameLayout = findViewById(R.id.chatcontainer);
        init();
    }

    private void init() {
        // set the toolbar

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.chatcontainer,
                ChatFragmentInternal.newInstance(
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constants.ARG_NAME),
                        getIntent().getExtras().getString(Constants.ARG_SENDER_UID),
                        getIntent().getExtras().getInt("CHAT_TYPE"),
                        ChatFragmentInternal.class.getSimpleName()));
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Intent intent = new Intent();
        //intent.setAction("restartservice");
        //intent.setClass(this, Restarter.class);
        //this.sendBroadcast(intent);
        //stopService(new Intent(this, TimeCounterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit the Chat ??").setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                finish();
            }
        });
        builder.show();
    }
}