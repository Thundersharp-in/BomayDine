package com.thundersharp.admin.core.DailyFoodListner;

import java.util.List;

public interface DailyFood {

    void getDailyFood(int foodType);

    interface dailyFoodListener {
        void OnFoodFetchSuccess(List<Object> objects);

        void OnFoodFetchFailure(Exception e);
    }

}
