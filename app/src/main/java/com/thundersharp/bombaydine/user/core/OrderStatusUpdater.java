package com.thundersharp.bombaydine.user.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OrderStatusUpdater extends Service {
    public OrderStatusUpdater() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}