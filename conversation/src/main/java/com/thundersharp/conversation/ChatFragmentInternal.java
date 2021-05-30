package com.thundersharp.conversation;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragmentInternal extends Fragment implements ChatContract.View{

    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ProgressDialog mProgressDialog;
    SharedPreferences data;
    public RecyclerView recyclerView;

    ImageButton buttonattach;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ChatPresenter mChatPresenter;

    ImageButton send_sms;
    int currentSessionMessageCounter=0;

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
        args.putString("ORDER_ID",orderId);
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
        bindViews(fragmentView);
        
        

        return fragmentView;
    }


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


    private void sendMessage(String message) {

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



    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat,long initialMessageCount) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(getActivity(),new ArrayList<Chat>(),getArguments().getInt("CHAT_TYPE"),initialMessageCount);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
        Toast.makeText(getContext(), "l", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesFailure(String message) {
        if (message.equalsIgnoreCase("NoData")){
            sendMessage("Hello on which order you need help today ?");
        }
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
