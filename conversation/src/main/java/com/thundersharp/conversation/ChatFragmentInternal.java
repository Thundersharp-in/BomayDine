package com.thundersharp.conversation;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.conversation.adapters.ChatRecyclerAdapter;
import com.thundersharp.conversation.core.chat.ChatContract;
import com.thundersharp.conversation.core.chat.ChatPresenter;
import com.thundersharp.conversation.event.PushNotificationEvent;
import com.thundersharp.conversation.model.Chat;
import com.thundersharp.conversation.utils.Constants;
import com.thundersharp.conversation.utils.Resturant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragmentInternal extends Fragment implements ChatContract.View {

    public static RelativeLayout sendmessageRecycler;
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ProgressDialog mProgressDialog;
    SharedPreferences data;
    public RecyclerView recyclerView;

    ImageButton buttonattach;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ChatPresenter mChatPresenter;

    long initialMessageCount =0;
    ImageButton send_sms;
    int currentSessionMessageCounter = 0;
    private String dataBrodcast;
    private boolean isBroadCasted = false;

    public static ChatFragmentInternal newInstance(String receiver,
                                                   String receiverUid,
                                                   String senderName,
                                                   String simpleName,
                                                   int chat_type,
                                                   String orderId,
                                                   String name) {


        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_NAME, senderName);
        args.putString(Constants.ARG_SENDER_UID, simpleName);
        args.putInt("CHAT_TYPE", chat_type);
        args.putString("ORDER_ID", orderId);
        ChatFragmentInternal fragment = new ChatFragmentInternal();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);

        sendmessageRecycler = fragmentView.findViewById(R.id.tttrrr);
        bindViews(fragmentView);


        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("initialMessage"));
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("updateRequest"));

        return fragmentView;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("initialMessage")){
                sendMessage("Hello on which order you need help today ?");
                if (mChatRecyclerAdapter != null){
                    Chat chat1 = new Chat(Resturant.RESTURANT_SUPPORT_NAME, "", Resturant.RESTURANT_SUPPORT_ID, FirebaseAuth.getInstance().getUid(), "::Chatchooser", System.currentTimeMillis());
                    mChatRecyclerAdapter.add(chat1);
                }
            }else if (intent.getAction().equals("updateRequest")){
                //TODO UPDATE FOR INSTANT UPDATE OF CHAT ELEMENTS
                dataBrodcast= intent.getStringExtra("data");
                sendMessage(dataBrodcast,0);


            }
        }
    };


    public void bindViews(View view) {
        data = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        send_sms = (ImageButton) view.findViewById(R.id.messageSendChat);
        buttonattach = view.findViewById(R.id.buttonattach);


        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mETxtMessage.getText().toString().trim().length() == 0) {
                    return;
                } else {
                    sendMessage();
                }
            }
        });


        buttonattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadlivestudents();
                //startActivity(new Intent(getActivity(), AttendanceActivity.class).putExtra("videokey",videokey));
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        String loading = "Loading";
        String please_wait = "Please wait";
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(loading);
        mProgressDialog.setMessage(please_wait);
        mProgressDialog.setIndeterminate(true);

        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(getArguments().getString(Constants.ARG_SENDER_UID),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }


    private void sendMessage() {

        String message = mETxtMessage.getText().toString();
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = getArguments().getString(Constants.ARG_NAME);
        String senderUid = getArguments().getString(Constants.ARG_SENDER_UID);


        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis());

        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                "");
    }

    private void sendMessage(String Custommesssage,int k) {
        isBroadCasted = true;
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = getArguments().getString(Constants.ARG_NAME);
        String senderUid = getArguments().getString(Constants.ARG_SENDER_UID);


        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                Custommesssage,
                System.currentTimeMillis());

        mChatPresenter.sendMessage(getActivity(),
                chat,
                "");
    }


    private void sendMessage(String message) {

        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = getArguments().getString(Constants.ARG_NAME);
        String senderUid = getArguments().getString(Constants.ARG_SENDER_UID);


        Chat chat = new Chat(
                Resturant.RESTURANT_SUPPORT_NAME,
                getArguments().getString(Constants.ARG_NAME),
                Resturant.RESTURANT_SUPPORT_ID,
                FirebaseAuth.getInstance().getUid(),
                message,
                System.currentTimeMillis());

        mChatPresenter.sendMessage(getActivity(),
                chat,
                "");
    }


    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
//        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat, long initialMessageCount) {
        currentSessionMessageCounter++;

        if (isBroadCasted){
            if (dataBrodcast.equals(chat.message)){
                sendmessageRecycler.setVisibility(View.VISIBLE);
                sendMessage("Please further elaborate your query.");
                isBroadCasted = false;
            }
        }
        if (chat.message.equals("/chat")){
            sendmessageRecycler.setVisibility(View.GONE);
            Toast.makeText(getContext(), "This Chat has been closed", Toast.LENGTH_SHORT).show();
        }
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(getActivity(), new ArrayList<Chat>(), getArguments().getInt("CHAT_TYPE"), initialMessageCount);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        if (initialMessageCount == currentSessionMessageCounter) {
            if (chat.message.equals("Hello on which order you need help today ?")) {
                Chat chat1 = new Chat(Resturant.RESTURANT_SUPPORT_NAME, "", Resturant.RESTURANT_SUPPORT_ID, FirebaseAuth.getInstance().getUid(), "::Chatchooser", System.currentTimeMillis());
                mChatRecyclerAdapter.add(chat);
                mChatRecyclerAdapter.add(chat1);
            }else {
                mChatRecyclerAdapter.add(chat);
            }
        }else {
            mChatRecyclerAdapter.add(chat);
        }
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);


    }

    @Override
    public void onGetMessagesFailure(String message) {
        if (message.equalsIgnoreCase("NoData")) {
            sendmessageRecycler.setVisibility(View.GONE);
            sendMessage("Hello on which order you need help today ?");
        } else
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
