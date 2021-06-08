package com.thundersharp.admin.core.orders;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import java.util.List;

public interface OrderContract {

    void fetchRecentOrders();

    interface onOrderFetch {
        void onOrderFetchSuccess(List<Object> data);
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
