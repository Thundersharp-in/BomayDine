package com.thundersharp.bombaydine.kitchen.core.helper;

import com.google.firebase.database.DataSnapshot;

public interface KitchenOrder {
    void onOrderFetchSuccess(DataSnapshot data, boolean isNew);
    void onDataFetchFailure(Exception e);
}
