package com.thundersharp.bombaydine.user.core.Data;

import com.thundersharp.bombaydine.user.core.Model.CategoryData;

import java.util.List;

public interface CategoryDataContract {

    void fetchCategoryData(CategoryData categoryData);

    interface OnCategoryDataFetch{
        void OnCategoryDataSuccess(List<Object> data);
        void OnCategoryDataFetchFailure(Exception exception);
    }

}
