package com.thundersharp.bombaydine.user.core.cart;

public interface CartHandler {

    void AddItemToCart(Object data);

    interface cart{
        void onItemAddSuccess(boolean isAdded, Object data);
        void addFailure(Exception exception);
    }
}
