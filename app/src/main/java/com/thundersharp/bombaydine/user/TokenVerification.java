package com.thundersharp.bombaydine.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class TokenVerification {

    Context context;

    public TokenVerification(Context context) {
        this.context = context;
    }

    public static TokenVerification getInstance(Context context){
        return new TokenVerification(context);
    }

    public void saveCredentials(String email,String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("uid",uid);
        editor.apply();
    }

    public boolean reVerifyAdminCurrentToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdminAuth",Context.MODE_PRIVATE);
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){

            return FirebaseAuth.getInstance().getCurrentUser().getUid().equals(sharedPreferences.getString("uid", "")) &&
                    FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(sharedPreferences.getString("email", ""));

        }else return false;
    }

}
