package com.thundersharp.admin.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class TokenVerificationAdmin {

    Context context;

    public static final String SHARED_PREF_EMAIL_ADMIN = "email";
    public static final String SHARED_PREF_UID_ADMIN = "uid";
    public static final String SHARED_PREF_PASSWORD_ADMIN = "password";


    public TokenVerificationAdmin(Context context) {
        this.context = context;
    }

    public static TokenVerificationAdmin getInstance(Context context){
        return new TokenVerificationAdmin(context);
    }

    public void saveCredentials(String email,String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_EMAIL_ADMIN,email);
        editor.putString(SHARED_PREF_UID_ADMIN,uid);
        editor.apply();
    }

    public boolean reVerifyAdminCurrentToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){

            return FirebaseAuth.getInstance().getCurrentUser().getUid().equals(sharedPreferences.getString(SHARED_PREF_UID_ADMIN, "")) &&
                    FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(sharedPreferences.getString(SHARED_PREF_EMAIL_ADMIN, ""));

        }else return false;
    }

    public SharedPreferences getSharedPrefs(){
        return context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
    }

    public boolean isDataNullOrEmpty(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);

        if (sharedPreferences != null){
            return !sharedPreferences.getString(SHARED_PREF_EMAIL_ADMIN, "").isEmpty() &&
                    !sharedPreferences.getString(SHARED_PREF_PASSWORD_ADMIN, "").isEmpty();
        }return false;
    }

    public void setAdminPassword(String passWord){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_PASSWORD_ADMIN,passWord);
        editor.apply();
    }

}
