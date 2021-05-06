package com.thundersharp.bombaydine.user.core.orders;

import java.util.List;

public interface OrderContract {

    void fetchRecentOrders();

    interface onOrderFetch{
        void onOrderFetchSuccess(List<Object> data);
        void onDataFetchFailure(Exception e);
    }

}
