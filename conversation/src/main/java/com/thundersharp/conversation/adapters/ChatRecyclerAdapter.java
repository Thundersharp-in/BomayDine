package com.thundersharp.conversation.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.conversation.ChatFragmentInternal;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.FirebaseChatMainApp;
import com.thundersharp.conversation.R;
import com.thundersharp.conversation.model.Chat;
import com.thundersharp.conversation.model.OrederBasicDetails;
import com.thundersharp.conversation.utils.DbConstants;
import com.thundersharp.conversation.utils.Resturant;

import java.util.ArrayList;
import java.util.List;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;
    private Context context;
    private int chatType;
    long initalMessageCount;

    public ChatRecyclerAdapter(Context context ,List<Chat> chats, int ChatType, long initialMessageCount) {
        mChats = chats;
        this.context = context;
        this.chatType = ChatType;
        this.initalMessageCount = initialMessageCount;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (chatType == ChatStarter.MODE_CHAT_ADMIN){
            if (TextUtils.equals(mChats.get(position).senderUid, Resturant.RESTURANT_SUPPORT_ID)) {

                configureMyChatViewHolder((MyChatViewHolder) holder, position);

            } else {
                configureOtherChatViewHolder((OtherChatViewHolder) holder, position);

            }

        }else {

            if (position == getItemCount() -1) {

                if (!mChats.get(position).message.equals("/end") && !mChats.get(position).message.equals("::Chatchooser"))
                    ChatFragmentInternal.sendmessageRecycler.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(mChats.get(position).senderUid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                configureMyChatViewHolder((MyChatViewHolder) holder, position);

            } else {
                configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
            }


        }


        if (position == (initalMessageCount-1)){

            if (mChats.get(position).message.equals("/end")) {

                context.sendBroadcast(new Intent("initialMessage"));

            }
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        //String alphabet = chat.receiver.substring(0, 1);


        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.name.setText("Me");
        myChatViewHolder.txtChatMessage.setMovementMethod(LinkMovementMethod.getInstance());
        if (position != 0) {
            if (getItemViewType(position) == getItemViewType(position - 1)) {
                myChatViewHolder.name.setVisibility(View.GONE);
            } else myChatViewHolder.name.setVisibility(View.VISIBLE);
        }else {
            myChatViewHolder.name.setText("Me");
            myChatViewHolder.name.setVisibility(View.VISIBLE);
        }
        //myChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        if (position != 0) {

            if (getItemViewType(position) == getItemViewType(position - 1)) {
                otherChatViewHolder.name.setVisibility(View.GONE);

            } else otherChatViewHolder.name.setVisibility(View.VISIBLE);

        }else {
            otherChatViewHolder.name.setText(chat.sender);
            otherChatViewHolder.name.setVisibility(View.VISIBLE);

        }


        if (mChats.get(position).message.equals("::Chatchooser")){

            otherChatViewHolder.txtChatMessage.setText("Select an order on which you need assistance on, If you need other assistance or on more past orders then click 'Others' option");
            otherChatViewHolder.otherRecycler.setVisibility(View.VISIBLE);

            FirebaseDatabase
                    .getInstance()
                    .getReference(DbConstants.DATABASE_NODE_ALL_USERS)
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(DbConstants.DATABASE_NODE_ORDERS)
                    .child(DbConstants.DATABASE_NODE_OVERVIEW)
                    .limitToLast(3)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                List<OrederBasicDetails> details = new ArrayList<>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    details.add(dataSnapshot.getValue(OrederBasicDetails.class));
                                }


                                otherChatViewHolder.otherRecycler.setAdapter(new OrderSelecterAdapter(details));
                                //TODO UPDATE AFTER SELECTION

                            }else Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }else {

            otherChatViewHolder.otherRecycler.setVisibility(View.GONE);
            otherChatViewHolder.txtChatMessage.setText(chat.message);
            otherChatViewHolder.txtChatMessage.setMovementMethod(LinkMovementMethod.getInstance());

            otherChatViewHolder.name.setText(chat.sender);
        }


    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatType == ChatStarter.MODE_CHAT_ADMIN){
            if (TextUtils.equals(mChats.get(position).senderUid, "SUPPORT56065")) {
                return VIEW_TYPE_ME;
            } else {
                return VIEW_TYPE_OTHER;
            }
        }else {
            if (TextUtils.equals(mChats.get(position).senderUid,
                    FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return VIEW_TYPE_ME;
            } else {
                return VIEW_TYPE_OTHER;
            }
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView txtChatMessage;
        private TextView txtUserAlphabet,name;

        public MyChatViewHolder(View itemView) {
            super(itemView);

            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            name = itemView.findViewById(R.id.name);
            //txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);

            txtChatMessage.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(itemView.getContext(),"long pressed ", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtChatMessage,name;
        private RecyclerView otherRecycler;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            name=itemView.findViewById(R.id.name);
            otherRecycler = itemView.findViewById(R.id.recycler_chat_other);
            txtChatMessage.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"long pressed ", Toast.LENGTH_LONG).show();

        }
    }
}
