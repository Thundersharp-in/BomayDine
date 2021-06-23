package com.thundersharp.admin.core.notification;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef({NotificationType.MODE_NOTIFY_NEW_ORDER,
        NotificationType.MODE_NOTIFY_ORDER_UPDATE,
        NotificationType.MODE_NOTIFY_CHAT_UPDATE,
        NotificationType.MODE_NOTIFY_NEW_CHAT,
        NotificationType.MODE_NOTIFY_OFFER,
        NotificationType.MODE_NOTIFY_ITEM_IN_CART})

public @interface NotificationType {
    int MODE_NOTIFY_NEW_ORDER = 0;
    int MODE_NOTIFY_ORDER_UPDATE = 1;
    int MODE_NOTIFY_CHAT_UPDATE = 2;
    int MODE_NOTIFY_ITEM_IN_CART  = 3;
    int MODE_NOTIFY_OFFER  = 4;
    int MODE_NOTIFY_NEW_CHAT  = 5;

}
