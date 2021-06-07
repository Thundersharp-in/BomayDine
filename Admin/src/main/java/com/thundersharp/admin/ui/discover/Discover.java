package com.thundersharp.admin.ui.discover;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Adapters.RecentAdapter;
import com.thundersharp.admin.core.animation.Animator;
import com.thundersharp.admin.core.orders.OrderHistoryProvider;

import java.util.Collections;
import java.util.List;

public class Discover extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        Animator.initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,view.findViewById(R.id.containermain));



        return view;
    }


}