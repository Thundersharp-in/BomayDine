package com.thundersharp.admin.core.notification;

import android.content.Context;

import com.thundersharp.admin.core.Model.CartItemModel;
import com.thundersharp.admin.core.Model.OfferModel;
import com.thundersharp.admin.core.Model.OrderModel;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.conversation.model.Chat;

public class Notifier {

    public static Notifier getInstance(Context context){
        return new Notifier(context);
    }

    private Context context;
    private int notificationType,dataType;
    private CartItemModel cartItemModel;
    private OrederBasicDetails orederBasicDetails;
    private  Chat chat;
    private OfferModel offerModel;
    private boolean sound;


    public Notifier(Context context) {
        this.context = context;
    }

    public Notifier setNotificationType(@NotificationType int notificationType){
        this.notificationType=notificationType;
        return this;
    }


    public Notifier setData(CartItemModel cartItemModel){
        this.cartItemModel = cartItemModel;
        dataType = 0;
        return this;
    }

    public Notifier setData(OrederBasicDetails orederBasicDetails){
        this.orederBasicDetails = orederBasicDetails;
        dataType = 1;
        return this;
    }

    public Notifier setData(Chat chat){
        this.chat = chat;
        dataType = 2;
        return this;
    }

    public Notifier setData(OfferModel offerModel){
        this.offerModel = offerModel;
        dataType = 3;
        return this;
    }

    public Notifier playSound(boolean sound){
        this.sound = sound;
        return this;
    }

    public void notifyNow(){

    }

}
