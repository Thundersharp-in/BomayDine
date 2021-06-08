package com.thundersharp.admin.ui.orders;

import com.thundersharp.admin.core.Model.OrderModel;

import java.util.List;

public interface OrderDetail {

    void FetchOrder(String ID);

    interface OrderListner{
        void onSuccess(List<OrderModel> model);
        void onFailure(Exception e);
    }
}
