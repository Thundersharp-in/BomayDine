package com.thundersharp.bombaydine.user.core.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.thundersharp.bombaydine.user.core.Model.SavedAccountData;

import java.util.HashMap;

public class AccountHelper implements
        AccountSwitcher.OnAccountSwitchInternal,AccountSwitcher.DataEmployee{

    private AccountSwitcher.onGetAdministrativeData onGetAdministrativeData;
    public static AccountHelper accountHelper;
    private EmployeeObserver employeeObserver;
    private String uid;
    static Context contexts;
    private SavedAccountData accountData;

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

    public void clearAllData(){
        SharedPreferences sharedPreferences = contexts.getSharedPreferences("EmpAccount",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setAdminDataListener(AccountSwitcher.onGetAdministrativeData adminDataHelper){
        this.onGetAdministrativeData = adminDataHelper;

        if (accountData.getUid() == null ||
                accountData.getId() == null ||
                accountData.getType() == null){
            onGetAdministrativeData.fetchFailure();
        }else {
            onGetAdministrativeData.fetchSuccess(accountData);
        }
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


    @Override
    public boolean isSavedAdministrativeDataAvailable() {
        String string = contexts.getSharedPreferences("EmpAccount",Context.MODE_PRIVATE).getString("TYPE",null);
        if (string == null) return false;else return true;
    }

    @Override
    public AccountHelper fetchSavedAdminData() {

        SharedPreferences sharedPreferences = contexts.getSharedPreferences("EmpAccount",Context.MODE_PRIVATE);
        this.accountData = SavedAccountData.start(sharedPreferences.getString("NAME",null),sharedPreferences.getString("EMPLOYEECODE",null),sharedPreferences.getString("UID",null),sharedPreferences.getString("TYPE",null));
        return accountHelper;
    }
}

