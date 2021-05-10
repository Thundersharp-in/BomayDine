package com.thundersharp.bombaydine.user.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.thundersharp.bombaydine.R;

public class Profile extends Fragment {

    private RelativeLayout bottomHolderprofile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_main, container, false);

        bottomHolderprofile = view.findViewById(R.id.bottomHolderprofile);



        return view;

    }


}

