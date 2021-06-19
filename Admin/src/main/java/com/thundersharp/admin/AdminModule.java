package com.thundersharp.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.core.Errors.ModuleError;
import com.thundersharp.admin.ui.AdminMain;

import java.io.Serializable;

public class AdminModule {

    static AdminModule adminModule;
    Boolean whichDataUsed;
    private final Context context;
    private Serializable serializedData;
    private Bundle dataBundle;

    public AdminModule(Context context) {
        this.context = context;
    }

    public static AdminModule getInstance(Context context){
        adminModule = new AdminModule(context);
        return adminModule;
    }

    public AdminModule useFirebaseServices(boolean value){
        return adminModule;
    }

    public AdminModule setSupportiveData(Serializable data){
        this.whichDataUsed = true;
        this.serializedData = data;
        return adminModule;
    }

    public AdminModule setSupportiveData(Bundle data){
        this.whichDataUsed = false;
        this.dataBundle = data;
        return adminModule;
    }

    public void startAdmin() throws ModuleError {
        //TODO ADD DATA PARSER UTIL
        //TODO UPDATE SEAR,BUND DATA LOGIC
        if (whichDataUsed == null){
            throw new ModuleError("No data found , have you invoked setSupportiveData(...) ?");
        }else if (context == null){
            throw new ModuleError("Context seems to be null.");
        }else {
            if (whichDataUsed){
                AdminMain.startActivity(serializedData,context);
            }else AdminMain.startActivity(dataBundle,context);
        }
    }

    public static void signOutAndRestartApp(Context context){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) FirebaseAuth.getInstance().signOut();
        Intent i = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void switchAndRestartApp(Context context){
        Intent i = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
