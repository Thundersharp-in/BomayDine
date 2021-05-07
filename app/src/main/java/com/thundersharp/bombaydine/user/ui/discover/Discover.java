package com.thundersharp.bombaydine.user.ui.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;


public class Discover extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}