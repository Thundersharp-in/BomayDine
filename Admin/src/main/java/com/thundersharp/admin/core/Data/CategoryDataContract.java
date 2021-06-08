package com.thundersharp.admin.core.Data;

import com.thundersharp.admin.core.Model.CategoryData;

import java.util.List;

public interface CategoryDataContract {

    void fetchCategoryData(CategoryData categoryData);

    interface OnCategoryDataFetch{
        void OnCategoryDataSuccess(List<Object> data);
        void OnCategoryDataFetchFailure(Exception exception);
    }

}
