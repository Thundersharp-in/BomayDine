package com.thundersharp.bombaydine.user.ui.orders;

import com.thundersharp.bombaydine.user.core.Model.OrderModel;

import java.util.List;

public interface OrderDetail {

    void FetchOrder(String ID);

    interface OrderListner{
        void onSuccess(List<OrderModel> model);
        void onFailure(Exception e);
    }
}
