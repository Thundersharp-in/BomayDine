package com.thundersharp.bombaydine.user.core.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.util.HashMap;

public final class EmployeeObserver implements AccountSwitcher
        ,SharedPrefUpdater.AccountSwitch{

    private String uid;
    private AccountSwitcher.OnAccountSwitchInternal onAccountSwitchInternal;
    private SharedPrefUpdater.AccountSwitch.lisetner accountSwitchListner;
    private Context context;

    public EmployeeObserver(String uid,Context context){
        this.uid = uid;
        this.context = context;
    }

    public void setInternalListner(AccountSwitcher.OnAccountSwitchInternal internalListner){
        this.onAccountSwitchInternal = internalListner;
    }

    public void setListner(SharedPrefUpdater.AccountSwitch.lisetner accountSwitchListner){
        switchToEmployeeAccount(uid);
        this.accountSwitchListner = accountSwitchListner;
    }

    public void overrideFailure(Exception e){
        if (accountSwitchListner != null) accountSwitchListner.onSaveFailure(e);
    }

    @Override
    public void switchToEmployeeAccount(String uid) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_EMP_CODES)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            HashMap hashMap = new HashMap();
                            hashMap.put("EMPLOYEECODE",snapshot.child("EMPLOYEECODE").getValue().toString());
                            hashMap.put("NAME",snapshot.child("NAME").getValue().toString());
                            hashMap.put("TYPE",snapshot.child("TYPE").getValue().toString());

                            onAccountSwitchInternal.OnAccountsSwitched(hashMap);

                        }else {
                            Exception exception = new Exception("ER1");
                            onAccountSwitchInternal.switchFailure(exception);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        accountSwitchListner.onSaveFailure(error.toException());
                    }
                });

    }



    @Override
    public void saveSwitchedUser(HashMap<String, String> data) {
        if (data != null){
            SharedPreferences sharedPreferences = context.getSharedPreferences("EmpAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putString("EMPLOYEECODE",data.get("EMPLOYEECODE"));
            editor.putString("NAME",data.get("NAME"));
            editor.putString("UID",uid);
            editor.putString("TYPE",data.get("TYPE"));
            editor.apply();
            accountSwitchListner.onSaveSuccess(data.get("EMPLOYEECODE"),data.get("NAME"),data.get("TYPE"));
        }

        else accountSwitchListner.onSaveFailure(new Exception("ERROR"));
    }
}
