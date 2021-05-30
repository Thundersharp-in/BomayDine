package com.thundersharp.bombaydine.user.ui.discover;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.RecentAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.ViewPagerAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.orders.OrderHistoryProvider;
import com.thundersharp.bombaydine.user.ui.orders.RecentOrders;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.conversation.ParametersMissingException;

import java.util.List;


public class Discover extends Fragment implements OrderContract.onOrderFetch {

    private RecyclerView recyclerView;
    private CardView support;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        Animator.initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,view.findViewById(R.id.containermain));

        recyclerView = view.findViewById(R.id.recyclerview);
        support = view.findViewById(R.id.dinner);

        support.setOnClickListener(view1 -> {
            ChatStarter chatStarter = ChatStarter.initializeChat(getActivity());
            chatStarter.setChatType(ChatStarter.MODE_CHAT_FROM_ORDERS);
            chatStarter.setSenderUid(FirebaseAuth.getInstance().getUid());
            chatStarter.setSenderName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            try {
                chatStarter.startChat();
            } catch (ParametersMissingException e) {
                e.printStackTrace();
            }
        });

        OrderHistoryProvider
                .getOrderInstance(0)
                .setOnOrderFetchListener(this)
                .fetchRecentOrders();


        return view;
    }

    @Override
    public void onOrderFetchSuccess(List<Object> data) {
        recyclerView.setAdapter(RecentAdapter.initialize(getActivity(),data));
    }

    @Override
    public void onDataFetchFailure(Exception e) {

    }
}