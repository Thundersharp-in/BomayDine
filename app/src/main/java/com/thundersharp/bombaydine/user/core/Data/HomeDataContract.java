package com.thundersharp.bombaydine.user.core.Data;

import java.util.HashMap;
import java.util.List;

public interface HomeDataContract {

    void fetchTopSelling();

    void fetchAllCategories();

    void fetchHomeallItem();

    void fetchAllitems();

    interface categoryFetch{

        void onCategoryFetchSuccess(List<Object> data);

    }

    interface topSellingFetch{
        void onTopSellingfetchSuccess(List<Object> data);
    }

    interface DataLoadFailure{
        void onDataLoadFailure(Exception e);
    }

    interface HomeAllItems{
        void OnHomeAlldataFetchSucess(List<HashMap<String,Object>> data);
    }

    interface AllItems{
        void OnallItemsFetchSucess(List<Object>data);
    }
}
