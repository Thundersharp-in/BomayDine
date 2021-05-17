package com.thundersharp.bombaydine.user.core.login;

import com.thundersharp.bombaydine.user.core.Model.SavedAccountData;

import java.util.HashMap;

public interface AccountSwitcher{

    void switchToEmployeeAccount(String uid);

    interface DataEmployee {

        boolean isSavedAdministrativeDataAvailable();

        AccountHelper fetchSavedAdminData();
    }


    interface OnAccountSwitchInternal{

        void OnAccountsSwitched(HashMap<String,String> data);
        void switchFailure(Exception e);
    }

    interface onGetAdministrativeData{
        void fetchSuccess(SavedAccountData accountData);
        void fetchFailure();
    }
}
