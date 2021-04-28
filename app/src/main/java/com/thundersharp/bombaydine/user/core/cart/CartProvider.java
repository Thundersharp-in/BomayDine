package com.thundersharp.bombaydine.user.core.cart;

import android.content.Context;


public class CartProvider implements CartHandler{

    /**
     * Creates a new istance of the cart provider with fetchCartData listener.
     * @param context
     * @param fetchCartData
     * @return
     */
    public static CartProvider initialize(Context context,fetchCartData fetchCartData){
        return new CartProvider(context,fetchCartData);
    }

    /**
     * Creates a new istance of the cart provider with <>Cartholder.cart</> listener.
     * @param context
     * @param cartlistners
     * @return
     */
    public static CartProvider initialize(Context context, CartHandler.cart cartlistners){
        return new CartProvider(context,cartlistners);
    }

    private Context context;
    private CartHandler.cart cartlistners;
    private CartHandler.fetchCartData fetchCartData;

    public CartProvider(Context context, cart cartlistners) {
        this.context = context;
        this.cartlistners = cartlistners;
    }

    public CartProvider(Context context, CartHandler.fetchCartData fetchCartData) {
        this.context = context;
        this.fetchCartData = fetchCartData;
    }

    /**
     * This method adds the given item object to server and stores it locally to avoid loading screens in the cart.
     * @param data
     */
    @Override
    public void AddItemToCart(Object data) {

    }

    /**
     * This is a internal method which writes the data stored to database in persistence.
     * @param data
     */
    @Override
    public void writetolocalStorage(Object data) {

    }

    /**
     * Fetch Data from server and syncs across the Persistence.
     */
    @Override
    public void fetchItemfromServer() {

    }


    /**
     * This is a internal method which fetch data from Persistence if exists.
     */
    @Override
    public void fetchitemfromStorage() {

    }

    /**
     * Forced Re-sync of all data
     */
    @Override
    public void syncData() {
        fetchItemfromServer();
    }

}
