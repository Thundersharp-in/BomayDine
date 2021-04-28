package com.thundersharp.bombaydine.user.core.cart;

import java.util.List;

public interface CartHandler {

    void AddItemToCart(Object data);

    void writetolocalStorage(Object data);

    void fetchItemfromServer();

    void fetchitemfromStorage();

    void syncData();

    interface fetchCartData {
        void onFetchCartDataSuccess(List<Object> dataList);
        void onFetchDataFailure(Exception e);
    }

    interface cart{
        void onItemAddSuccess(boolean isAdded, Object data);
        void addFailure(Exception exception);
    }
}
