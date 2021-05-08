package com.thundersharp.bombaydine.user.ui.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapterMailAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.AllOfferAdapters;
import com.thundersharp.bombaydine.user.core.Data.HomeDataContract;
import com.thundersharp.bombaydine.user.core.Data.HomeDataProvider;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.OfflineDataSync.OfflineDataProvider;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;

import java.util.List;

public class AllItemsActivity extends AppCompatActivity implements
        HomeDataContract.AllItems,
        HomeDataContract.DataLoadFailure{

    private RecyclerView recyclermain;
    private AllItemAdapterMailAdapter allItemAdapterMailAdapter;
    private HomeDataProvider homeDataProvider;
    private RecyclerView offerscroll;
    private ShimmerFrameLayout shl;
    private RelativeLayout bottomholder;
    private OfflineDataProvider offlineDataProvider;
    private CartProvider cartProvider;
    private TextView noofItems,totalamt;
    private ImageView filter;
    boolean isfilerOpen = false;
    private LinearLayout radiogroup;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        homeDataProvider = new HomeDataProvider(this,this,this);

        offlineDataProvider = OfflineDataProvider.getInstance(this);
        recyclermain = findViewById(R.id.recyclermain);
        offerscroll = findViewById(R.id.offerscroll);
        shl = findViewById(R.id.shl);
        shl.setVisibility(View.VISIBLE);
        recyclermain.setVisibility(View.GONE);
        shl.startShimmer();

        bottomholder = findViewById(R.id.bottomholder);
        noofItems = findViewById(R.id.text2);
        totalamt = findViewById(R.id.totalamt);
        filter = findViewById(R.id.filter);
        radiogroup = findViewById(R.id.chkbox);
        radiogroup.setVisibility(View.GONE);
        bottomholder.setVisibility(View.INVISIBLE);

        bottomholder.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
            View bottomview = LayoutInflater.from(this).inflate(R.layout.botomsheet_cart, view.findViewById(R.id.botomcontainer));

            bottomSheetDialog.setContentView(bottomview);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            bottomSheetDialog.show();
        });

        homeDataProvider.fetchAllitems();

        filter.setOnClickListener(view -> {
            if (isfilerOpen){
                isfilerOpen = false;
                radiogroup.setVisibility(View.GONE);

            }else {
                isfilerOpen = true;
                radiogroup.setVisibility(View.VISIBLE);

            }
        });

        if (offlineDataProvider.doSharedPrefExists()){
            String dataraw = offlineDataProvider.fetchitemfromStorage();
            if (!dataraw.equalsIgnoreCase("[]")){

                List<CartItemModel> data = offlineDataProvider.returnDataFromString(dataraw);

                double totalamount = 0.0;
                for (int i = 0; i<data.size(); i++){
                    if (data.get(i).getQUANTITY() > 1){
                        totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                    }else totalamount = totalamount+data.get(i).getAMOUNT();
                }
                totalamt.setText("Rs. "+totalamount+" plus taxes");

                noofItems.setText(data.size()+" ITEMS");

                Animator.initializeAnimator().slideUp(bottomholder);

            }else {
                offlineDataProvider.clearSharedPref();
            }
        }

        OffersProvider.initializeOffersProvider(new OfferListner.getOfferListner() {

            @Override
            public void OnGetOfferSuccess(List<Object> data) {
                AllOfferAdapters allOfferAdapters = AllOfferAdapters.getInstance(AllItemsActivity.this,data);
                offerscroll.setAdapter(allOfferAdapters);
            }

            @Override
            public void OnOfferFetchFailure(Exception e) {
                Toast.makeText(AllItemsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }).fetchAllOffers();


         cartProvider = CartProvider.initialize(this, new CartHandler.cart() {

            @Override
            public void onItemAddSuccess(boolean isAdded, List<CartItemModel> data) {
                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                //refreshAdapter();
                double totalamount = 0.0;

                if (data == null || data.isEmpty()){
                    Animator.initializeAnimator().slideDown(bottomholder);

                }else {

                    for (int i = 0; i<data.size(); i++){
                        if (data.get(i).getQUANTITY() > 1){
                            totalamount = totalamount + (data.get(i).getQUANTITY() * data.get(i).getAMOUNT());
                        }else totalamount = totalamount+data.get(i).getAMOUNT();
                    }
                    totalamt.setText("Rs. "+totalamount+" plus taxes");

                    noofItems.setText(data.size()+" ITEMS");
                    if (bottomholder.getVisibility() == View.GONE || bottomholder.getVisibility() ==View.INVISIBLE){
                        Animator.initializeAnimator().slideUp(bottomholder);
                    }
                }
            }

            @Override
            public void addFailure(Exception exception) {
                Toast.makeText(AllItemsActivity.this, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


       registerReceiver(broadcastReceiver,new IntentFilter("updated"));
    }

    BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cartProvider.syncData();
               /* if (intent!=null){
                        allItemAdapter.notifyItemChanged(intent.getIntExtra("adapterPos",0));
                }*/
        }

    };

    @Override
    public void onDataLoadFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnallItemsFetchSucess(List<Object> data) {
        allItemAdapterMailAdapter = new AllItemAdapterMailAdapter(data,this);
        recyclermain.setHasFixedSize(true);
        recyclermain.setAdapter(allItemAdapterMailAdapter);

        recyclermain.setVisibility(View.VISIBLE);
        Animator
                .initializeAnimator()
                .runRecyclerSlideRightAnimation(recyclermain);
        shl.stopShimmer();
        shl.setVisibility(View.GONE);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }




}