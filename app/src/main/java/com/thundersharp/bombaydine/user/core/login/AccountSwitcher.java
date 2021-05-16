package com.thundersharp.bombaydine.user.core.login;

import java.util.HashMap;

public interface AccountSwitcher{

    void switchToEmployeeAccount(String uid);

    interface OnAccountSwitchInternal{

        void OnAccountsSwitched(HashMap<String,String> data);
        void switchFailure(Exception e);
    }
}
