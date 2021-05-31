package com.thundersharp.conversation;

import android.content.Context;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.thundersharp.conversation.utils.Resturant;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ChatStarter {

    private Context context;
    static ChatStarter chatStarter;
    private Integer chatMode;
    private String senderUid;
    private String senderName;
    private String customerUiid;
    private String orderId;


    public ChatStarter(Context context){
        this.context = context;
    }

    public static ChatStarter initializeChat(Context context){
        chatStarter = new ChatStarter(context);
        return chatStarter;
    }

    @Retention(SOURCE)
    @IntDef({MODE_CHAT_FROM_ORDERS,
            MODE_CHAT_FROM_ORDERS_PRE_DELIVERY,
            MODE_CHAT_FROM_SPECIFIC_ORDER,
            MODE_CHAT_FROM_SPECIFIC_ORDER_PRE_DELIVERY,
            MODE_CHAT_FROM_PROFILE_HELP_N_FEEDBACK,
            MODE_CHAT_FROM_PROFILE_REPORT,
            MODE_CHAT_ADMIN})
    public @interface ChatMode {}
    public static final int MODE_CHAT_FROM_ORDERS = 0;
    public static final int MODE_CHAT_FROM_ORDERS_PRE_DELIVERY = 1;
    public static final int MODE_CHAT_FROM_SPECIFIC_ORDER = 2;
    public static final int MODE_CHAT_FROM_SPECIFIC_ORDER_PRE_DELIVERY  = 3;
    public static final int MODE_CHAT_FROM_PROFILE_HELP_N_FEEDBACK = 4;
    public static final int MODE_CHAT_FROM_PROFILE_REPORT = 5;

    public static final int MODE_CHAT_ADMIN = 6;

    public void setChatType(@ChatMode Integer chatMode){
        this.chatMode = chatMode;
    }

    @ChatMode
    public Integer getChatMode(){
        return chatMode;
    }

    public void setSenderUid(String senderUid){
        this.senderUid = senderUid;
    }

    public String getSenderUid(){
        return senderUid;
    }

    @NonNull
    public void setCostomerName(@NonNull String costomerName){
        this.senderName = costomerName;
    }

    public String getCostomerName(){
        return senderName;
    }

    @NonNull
    public void setCustomerId(@NonNull String uid){
        this.customerUiid = uid;
    }

    public String getCostumerUid(){
        return customerUiid;
    }

    public void setOrderId(@NonNull String orderId){
        this.orderId = orderId;
    }

    public String getOrderId(){
        return orderId;
    }

    public void startChat() throws ParametersMissingException{

        if (getChatMode() == null || getSenderUid() == null || getCostomerName() == null){

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to start chat\n");
            stringBuilder.append("Possible cause : Have you called all the functions serially ? ie.\n");
            stringBuilder.append("1. setChatType(@ChatMode Integer chatMode)\n");
            stringBuilder.append("2. setSenderUid(String senderUid)\n");
            stringBuilder.append("3. setSenderName(@NonNull String senderName)\n");
            stringBuilder.append("\n\n");
            stringBuilder.append("Current values =======================================================");
            stringBuilder.append("Chat mode : ").append(getChatMode()).append("\n");
            stringBuilder.append("Chat Sender Uid : ").append(getSenderUid()).append("\n");
            stringBuilder.append("Chat Sender Name : ").append(getCostomerName()).append("\n");
            throw new ParametersMissingException(stringBuilder.toString());

        }else {
            switch (getChatMode()){
                case ChatStarter.MODE_CHAT_FROM_ORDERS:
                    ChatActivity
                            .startActivity(context,
                                    Resturant.RESTURANT_SUPPORT_NAME,
                                    Resturant.RESTURANT_SUPPORT_ID,
                                    getCostomerName(),
                                    getSenderUid(),
                                    getChatMode());
                    break;

                case ChatStarter.MODE_CHAT_FROM_ORDERS_PRE_DELIVERY:
                    break;

                case ChatStarter.MODE_CHAT_FROM_PROFILE_HELP_N_FEEDBACK:
                    if (getCostomerName() != null)
                    ChatActivity
                            .startActivity(context,
                                    Resturant.RESTURANT_SUPPORT_NAME,
                                    Resturant.RESTURANT_SUPPORT_ID,
                                    getCostomerName(),
                                    getSenderUid(),
                                    getChatMode());
                    break;

                case ChatStarter.MODE_CHAT_FROM_PROFILE_REPORT:
                    if (getOrderId() == null){}
                    ChatActivity
                            .startActivity(context,
                                    Resturant.RESTURANT_SUPPORT_NAME,
                                    Resturant.RESTURANT_SUPPORT_ID,
                                    getCostomerName(),
                                    getSenderUid(),
                                    getChatMode());

                    break;

                case ChatStarter.MODE_CHAT_FROM_SPECIFIC_ORDER:
                    if (getOrderId() == null){
                        throw new ParametersMissingException("Costumer id not defined. Have you called setOrderId() ?");

                    }else ChatActivity
                            .startActivity(context,
                                    Resturant.RESTURANT_SUPPORT_NAME,
                                    Resturant.RESTURANT_SUPPORT_ID,
                                    getCostomerName(),
                                    getSenderUid(),
                                    getChatMode(),
                                    getOrderId());
                    break;

                case ChatStarter.MODE_CHAT_FROM_SPECIFIC_ORDER_PRE_DELIVERY:
                    ChatActivity
                            .startActivity(context,
                                    Resturant.RESTURANT_SUPPORT_NAME,
                                    Resturant.RESTURANT_SUPPORT_ID,
                                    getCostomerName(),
                                    getSenderUid(),
                                    getChatMode(),
                                    getOrderId());
                    break;

                case ChatStarter.MODE_CHAT_ADMIN:
                    if (getCostumerUid() == null){

                        throw new ParametersMissingException("Costumer id not defined. Have you called setCostumerId() ?");

                    }else ChatActivity.startActivity(context,getCostomerName(),getCostumerUid(),Resturant.RESTURANT_SUPPORT_NAME,getSenderUid(),getChatMode());
                    break;

                default:
                    throw new ParametersMissingException("Chat type not defined. Have you called setChatType() ?");
            }

        }

    }

}
