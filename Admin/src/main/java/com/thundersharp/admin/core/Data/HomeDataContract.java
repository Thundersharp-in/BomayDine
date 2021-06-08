package com.thundersharp.admin.core.Data;

import java.util.List;

public interface HomeDataContract {

    void fetchTopSelling();

    void fetchTopSellingAll();

    void fetchAllCategories();

    void fetchhomeAllCategories();

    void fetchHomeallItem();

    void fetchAllitems();

    interface topSellingAllFetch{
        void onAllTopSellingfetchSuccess(List<Object> data);
    }

    interface categoryFetch{

        void onCategoryFetchSuccess(List<Object> data);

    }

    interface HomeAllCategoriesFetch{
        void onCategoryFetchSuccess(List<Object> data);
    }

    interface topSellingFetch{
        void onTopSellingfetchSuccess(List<Object> data);
    }

    interface DataLoadFailure{
        void onDataLoadFailure(Exception e);
    }

    interface HomeAllItems{
        void OnHomeAlldataFetchSucess(List<Object> data);
    }

    interface AllItems{
        void OnallItemsFetchSucess(List<Object>data);
    }
}
