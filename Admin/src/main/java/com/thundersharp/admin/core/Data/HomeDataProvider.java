package com.thundersharp.admin.core.Data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeDataProvider implements HomeDataContract{

    private Context context;
    private HomeDataContract.topSellingFetch topSellingFetch;
    private HomeDataContract.HomeAllCategoriesFetch categoryFetch;
    private HomeDataContract.DataLoadFailure dataLoadFailure;
    private HomeDataContract.HomeAllItems homeAllItems;
    private HomeDataContract.AllItems allItems;
    private HomeDataContract.topSellingAllFetch topSellingAllFetch;
    private HomeDataContract.categoryFetch categoryFetchall;


    private List<Object> datalist = new ArrayList<>();


    public HomeDataProvider(Context context, HomeDataContract.topSellingFetch topSellingFetch, HomeDataContract.HomeAllCategoriesFetch categoryFetch, DataLoadFailure dataLoadFailure, HomeDataContract.HomeAllItems homeAllItems, AllItems allItems) {
        this.context = context;
        this.topSellingFetch = topSellingFetch;
        this.categoryFetch = categoryFetch;
        this.dataLoadFailure = dataLoadFailure;
        this.homeAllItems = homeAllItems;
        this.allItems = allItems;
    }

    public HomeDataProvider(Context context, HomeDataContract.topSellingFetch topSellingFetch, HomeDataContract.HomeAllCategoriesFetch categoryFetch, DataLoadFailure dataLoadFailure, HomeAllItems homeAllItems) {
        this.context = context;
        this.topSellingFetch = topSellingFetch;
        this.categoryFetch = categoryFetch;
        this.dataLoadFailure = dataLoadFailure;
        this.homeAllItems = homeAllItems;
    }

    public HomeDataProvider(Context context, DataLoadFailure dataLoadFailure, AllItems allItems) {
        this.context = context;
        this.dataLoadFailure = dataLoadFailure;
        this.allItems = allItems;
    }

    public HomeDataProvider(Context context, DataLoadFailure dataLoadFailure, HomeDataContract.topSellingAllFetch topSellingAllFetch) {
        this.context = context;
        this.dataLoadFailure = dataLoadFailure;
        this.topSellingAllFetch = topSellingAllFetch;
    }

    public HomeDataProvider(Context context, DataLoadFailure dataLoadFailure, HomeDataContract.categoryFetch categoryFetchall) {
        this.context = context;
        this.dataLoadFailure = dataLoadFailure;
        this.categoryFetchall = categoryFetchall;
    }

    @Override
    public void fetchTopSelling() {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_TOP_SELLING).limitToFirst(6);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Object> list = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                list.add((HashMap<String, Object>) dataSnapshot.getValue());
                            }
                            topSellingFetch.onTopSellingfetchSuccess(list);

                        }else {
                            Exception exception = new Exception("NO DATA FOUND");
                            dataLoadFailure.onDataLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dataLoadFailure.onDataLoadFailure(error.toException());
                    }
                });

    }

    @Override
    public void fetchTopSellingAll() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_TOP_SELLING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<Object> list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add((HashMap<String, Object>) dataSnapshot.getValue());
                    }
                    topSellingAllFetch.onAllTopSellingfetchSuccess(list);

                }else {
                    Exception exception = new Exception("NO DATA FOUND");
                    dataLoadFailure.onDataLoadFailure(exception);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataLoadFailure.onDataLoadFailure(error.toException());
            }
        });
    }

    @Override
    public void fetchAllCategories() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_CATEGORY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Object> list = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                list.add((HashMap<String, String>) dataSnapshot.getValue());
                            }
                            categoryFetchall.onCategoryFetchSuccess(list);

                        }else {
                            Exception exception = new Exception("NO DATA FOUND");
                            dataLoadFailure.onDataLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dataLoadFailure.onDataLoadFailure(error.toException());
                    }
                });
    }

    @Override
    public void fetchhomeAllCategories() {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_CATEGORY).limitToFirst(5);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Object> list = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                list.add((HashMap<String, String>) dataSnapshot.getValue());
                            }
                            categoryFetch.onCategoryFetchSuccess(list);

                        }else {
                            Exception exception = new Exception("NO DATA FOUND");
                            dataLoadFailure.onDataLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dataLoadFailure.onDataLoadFailure(error.toException());
                    }
                });

    }

    @Override
    public void fetchHomeallItem() {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                .limitToFirst(6);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                datalist.add(dataSnapshot.getValue(FoodItemAdapter.class));

                            }
                            homeAllItems.OnHomeAlldataFetchSucess(datalist);
                        }else {
                            Exception exception = new Exception("ERROR 28HR45 : NO HOME DATA FOUND");
                            dataLoadFailure.onDataLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void fetchAllitems() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                .orderByChild("CAT_NAME_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                datalist.add(dataSnapshot.getValue(FoodItemAdapter.class));
                            }
                            allItems.OnallItemsFetchSucess(datalist);
                        }else {
                            Exception exception = new Exception("ERROR 404 : NO DATA FOUND");
                            dataLoadFailure.onDataLoadFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
