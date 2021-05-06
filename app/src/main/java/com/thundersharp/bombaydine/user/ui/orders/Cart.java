package com.thundersharp.bombaydine.user.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;

import java.util.List;


public class Cart extends Fragment {

    private CartProvider cartProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);




        return view;
    }


}