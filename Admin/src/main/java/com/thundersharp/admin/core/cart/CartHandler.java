package com.thundersharp.admin.core.cart;


import com.thundersharp.admin.core.Model.CartItemModel;

import java.util.List;

public interface CartHandler {

    void AddItemToCart(CartItemModel data, int qty);

    void writetolocalStorage(String data);

    void fetchItemfromServer();

    String fetchitemfromStorage();

    void syncData();



    interface cart{
        void onItemAddSuccess(boolean isAdded, List<CartItemModel> data);
        void addFailure(Exception exception);
    }

    interface OnCartEmpty{
        void OnCartEmptyListener();
    }
}
