package com.thundersharp.admin.core.orders;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface OrderContract {

    void fetchRecentOrders();

    interface onOrderFetch {
        void onOrderFetchSuccess(DataSnapshot data, boolean isNew);
        void onDataFetchFailure(Exception e);
    }

    interface Status {
        void setStatus(String order_id , int value,String custUid);
    }

    interface StatusSuccessFailure {
        void onSuccess(@NonNull Task<Void> task);
        void onFailure(Exception e);
    }

}
