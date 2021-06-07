package com.thundersharp.admin.core;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminHelpers {

    private Context context;

    public AdminHelpers(Context context) {
        this.context = context;
    }

    public static AdminHelpers getInstance(Context context){
        return new AdminHelpers(context);
    }

    public void clearAllData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("EmpAccount",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
