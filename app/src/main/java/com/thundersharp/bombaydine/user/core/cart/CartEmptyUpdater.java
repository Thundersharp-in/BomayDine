package com.thundersharp.bombaydine.user.core.cart;

public class CartEmptyUpdater {

    public static CartHandler.OnCartEmpty onCartEmpty;
    public static CartEmptyUpdater cartEmptyUpdater;
    public static CartEmptyUpdater cartEmptyUpdaterHOME;

    public static CartEmptyUpdater initializeCartUpdater(){
        cartEmptyUpdater = new CartEmptyUpdater();
        return cartEmptyUpdater;
    }

    public static CartEmptyUpdater initializeHomeCartUpdater(){
        cartEmptyUpdaterHOME = new CartEmptyUpdater();
        return cartEmptyUpdaterHOME;
    }

    public static CartEmptyUpdater getPreviousInstance(){
        return cartEmptyUpdater;
    }

    public void setOnCartEmptyListener(CartHandler.OnCartEmpty onCartEmptyListener){
        onCartEmpty = onCartEmptyListener;
    }

    public void setCartEmpty(){
        if (onCartEmpty!= null) onCartEmpty.OnCartEmptyListener();
    }

}
