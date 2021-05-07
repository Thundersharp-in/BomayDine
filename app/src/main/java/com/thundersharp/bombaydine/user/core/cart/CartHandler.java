package com.thundersharp.bombaydine.user.core.cart;

import com.thundersharp.bombaydine.user.core.Model.CartItemModel;

import java.util.List;

public interface CartHandler {

    void AddItemToCart(CartItemModel data, int qty, int adapterPos);

    void writetolocalStorage(String data, CartItemModel changedData,int adapterPos);

    void fetchItemfromServer();

    String fetchitemfromStorage();

    void syncData();



    interface cart{
        void onItemAddSuccess(boolean isAdded, List<CartItemModel> data);
        void addFailure(Exception exception);
    }
}
