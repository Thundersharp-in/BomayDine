package com.thundersharp.bombaydine.user.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;


public class Cart extends CartProvider implements CartHandler.cart {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);

        return view;
    }

    @Override
    public void onItemAddSuccess(boolean isAdded, Object data) {

    }

    @Override
    public void addFailure(Exception exception) {

    }
}