package com.thundersharp.bombaydine.user.core.login;

import android.content.Context;

import java.util.HashMap;

public class AccountHelper implements AccountSwitcher.OnAccountSwitchInternal{

    public static AccountHelper accountHelper;
    public EmployeeObserver employeeObserver;
    private String uid;
    static Context contexts;

    public static AccountHelper getInstance(Context context){
        accountHelper = new AccountHelper();
        contexts = context;
        return accountHelper;
    }

    public EmployeeObserver setUid(String uid){
        employeeObserver = new EmployeeObserver(uid,contexts);
        employeeObserver.setInternalListner(this);
        return employeeObserver;
    }


    @Override
    public void OnAccountsSwitched(HashMap<String,String> data) {
        employeeObserver.saveSwitchedUser(data);
    }

    @Override
    public void switchFailure(Exception e) {
        if (e.getMessage().equalsIgnoreCase("ER1")){
            //TODO UPDATE HERE
            employeeObserver.overrideFailure(e);
        }
    }
}

