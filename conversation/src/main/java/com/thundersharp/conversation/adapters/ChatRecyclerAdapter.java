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

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.R;
import com.thundersharp.conversation.model.Chat;
import com.thundersharp.conversation.utils.Resturant;

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

            if (TextUtils.equals(mChats.get(position).senderUid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                configureMyChatViewHolder((MyChatViewHolder) holder, position);

            } else {
                configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
            }

            if (position == initalMessageCount){

                if (mChats.get(position).message.equals("/end")){

                    context.sendBroadcast(new Intent("initialMessage"));

                }else if (mChats.get(position).message.equalsIgnoreCase("Hello on which order you need help today ?")){

                    Chat chat = new Chat(Resturant.RESTURANT_SUPPORT_NAME,"You",Resturant.RESTURANT_SUPPORT_ID,FirebaseAuth.getInstance().getUid(),"::Chatchooser",System.currentTimeMillis());
                    add(chat);

                }


            }

        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        //String alphabet = chat.receiver.substring(0, 1);


        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.name.setText("Me");
        myChatViewHolder.txtChatMessage.setMovementMethod(LinkMovementMethod.getInstance());
        //myChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        if (mChats.get(position).message.equals("::Chatchooser")){

            //TODO CHAT STARTER CHOOSER

        }else {

            otherChatViewHolder.txtChatMessage.setText(chat.message);
            otherChatViewHolder.txtChatMessage.setMovementMethod(LinkMovementMethod.getInstance());
            otherChatViewHolder.name.setText(chat.receiver);
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

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            name=itemView.findViewById(R.id.name);

            txtChatMessage.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"long pressed ", Toast.LENGTH_LONG).show();

        }
    }
}
