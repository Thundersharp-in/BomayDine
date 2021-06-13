package com.thundersharp.bombaydine.user.core.Data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.Model.CategoryData;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryDataProvider implements CategoryDataContract{

    private CategoryDataContract.OnCategoryDataFetch onCategoryDataFetch;

    public CategoryDataProvider(OnCategoryDataFetch onCategoryDataFetch) {
        this.onCategoryDataFetch = onCategoryDataFetch;
    }

    @Override
    public void fetchCategoryData(CategoryData categoryData) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS)
                .child(categoryData.getID())
                .limitToFirst(6).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Object> list = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                list.add(dataSnapshot);
                                onCategoryDataFetch.OnCategoryDataSuccess(list);
                            }
                        }else {
                            Exception e = new Exception("ERROR 404 : NO DATA FOUND");
                            onCategoryDataFetch.OnCategoryDataFetchFailure(e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onCategoryDataFetch.OnCategoryDataFetchFailure(error.toException());
                    }
                });
    }
}
