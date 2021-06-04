package com.thundersharp.bombaydine.user.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingData {

    SharedPreferences sharedPreferences;
    Context context;

    public SettingData(Context context) {
        sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        this.context = context;
    }

    public void setTheme(Boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("theme",status);
        editor.apply();
    }

    public Boolean loadTheme(){
        return sharedPreferences.getBoolean("theme",false);
    }
}
