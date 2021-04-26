package com.thundersharp.bombaydine.user.core.cart;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class CartProvider extends Fragment implements CartHandler{




    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void AddItemToCart(Object data) {

    }
}
