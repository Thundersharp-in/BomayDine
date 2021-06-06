package com.thundersharp.bombaydine.Delevery.core;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface DeliveryOrderContract {
    void onOrderFetchSuccess(DataSnapshot data, boolean isNew);
    void onDataFetchFailure(Exception e);
}
