package com.thundersharp.bombaydine.user.core.address;

import com.thundersharp.bombaydine.user.core.Model.AddressData;

import java.util.HashMap;

public interface SharedPrefUpdater {

    void SaveDataToSharedPref(AddressData addressData);

    void updatehomelocationData();

    AddressData getSavedHomeLocationData();

    interface OnSharedprefUpdated{

        void onSharedPrefUpdate(AddressData addressData);
    }

    interface AccountSwitch{

        void saveSwitchedUser(HashMap<String , String> data);

        interface lisetner{
            void onSaveSuccess(String employeeCode,String name);
            void onSaveFailure(Exception e);
        }

    }
}
