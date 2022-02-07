package com.thundersharp.bombaydine.user.core.Model;

public class CartOptionsModel {

    public CartOptionsModel(){}

    public String REQUEST_OPTION_NAME;
    public double CART_VALUE_CHANGE;

    public CartOptionsModel(String REQUEST_OPTION_NAME, double CART_VALUE_CHANGE) {
        this.REQUEST_OPTION_NAME = REQUEST_OPTION_NAME;
        this.CART_VALUE_CHANGE = CART_VALUE_CHANGE;
    }


}
