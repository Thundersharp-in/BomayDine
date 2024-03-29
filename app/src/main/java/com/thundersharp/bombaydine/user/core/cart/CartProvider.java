package com.thundersharp.bombaydine.user.core.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CartProvider implements CartHandler {

    /**
     * Creates a new istance of the cart provider with fetchCartData listener.
     *
     * @param fetchCartData Requires CartHandler.cart
     * @return returns instance Object of CartProvider
     */
    public static CartProvider initialize(cart fetchCartData) {
        return new CartProvider(fetchCartData);
    }

    public static CartProvider initialize(Context context) {
        return new CartProvider(context);
    }

    /**
     * Creates a new instance of the cart provider with <>Cartholder.cart</> listener.
     *
     * @param context
     * @param cartlistners
     * @return
     */
    public static CartProvider initialize(Context context, CartHandler.cart cartlistners) {
        return new CartProvider(context, cartlistners);
    }

    private Context context;
    private CartHandler.cart cartlistners;


    public CartProvider(Context context, cart cartlistners) {
        this.context = context;
        this.cartlistners = cartlistners;
    }

    public CartProvider(cart cartlistners) {
        this.cartlistners = cartlistners;
    }

    public CartProvider(Context context) {
        this.context = context;
    }

    /**
     * This method adds the given item object to server and stores it locally to avoid loading screens in the cart.
     *
     * @param data
     */
    @Override
    public void AddItemToCart(CartItemModel data, int qty) {
        if (!doSharedPrefExists()) {

            List<CartItemModel> cartItem = new ArrayList<>();
            cartItem.add(data);
            writetolocalStorage(convertToString(cartItem));

        } else {

            if (fetchitemfromStorage() != null) {

                List<CartItemModel> dataexisting = returnDataFromString(fetchitemfromStorage());

                if (dataexisting.isEmpty()) {
                    List<CartItemModel> cartItem = new ArrayList<>();
                    cartItem.add(data);
                    writetolocalStorage(convertToString(cartItem));
                } else {

                    for (int i = 0; i < dataexisting.size(); i++) {

                        CartItemModel cartItemModel = dataexisting.get(i);

                        if (cartItemModel.getID().equalsIgnoreCase(data.getID())) {

                            if (qty == 0) {
                                dataexisting.remove(i);
                                writetolocalStorage(convertToString(dataexisting));
                            } else {
                                dataexisting.set(i, data);
                                writetolocalStorage(convertToString(dataexisting));
                            }
                            break;


                        } else if (i == (dataexisting.size() - 1)) {
                            dataexisting.add(data);
                            writetolocalStorage(convertToString(dataexisting));
                            break;
                        }
                    }
                }

            } else {
                //Toast.makeText(context, "RC", Toast.LENGTH_SHORT).show();
                clearCart();
            }

        }
    }

    public void clearCart() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * This is a internal method which writes the data stored to database in persistence.
     *
     * @param data
     */
    @Override
    public void writetolocalStorage(String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CONSTANTS.CART_SHARED_PREFERENCES_DATA, data);
        editor.putBoolean(CONSTANTS.CART_SHARED_PREFERENCES_EXISTS, true);
        editor.apply();

        if (cartlistners != null)
            cartlistners.onItemAddSuccess(true, returnDataFromString(data));
        context.sendBroadcast(new Intent("updated"));
    }

    /**
     * Fetch Data from server and syncs across the Persistence.
     */
    @Override
    public void fetchItemfromServer() {
        if (fetchitemfromStorage().isEmpty() || fetchitemfromStorage() == null) {
            cartlistners.addFailure(new Exception("Error data corrupted"));
        } else cartlistners.onItemAddSuccess(false, returnDataFromString(fetchitemfromStorage()));
    }


    /**
     * This is a internal method which fetch data from Persistence if exists.
     */

    @Override
    public String fetchitemfromStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CONSTANTS.CART_SHARED_PREFERENCES_DATA, null);
    }

    /**
     * Forced Re-sync of all data
     */
    @Override
    public void syncData() {
        fetchItemfromServer();
    }

    private String convertToString(List<CartItemModel> data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    private List<CartItemModel> returnDataFromString(String data) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItemModel>>() {
        }.getType();
        return gson.fromJson(data, type);
    }

    private boolean doSharedPrefExists() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CONSTANTS.CART_SHARED_PREFERENCES_EXISTS, false);
    }


}
