package com.thundersharp.admin.core.address;



import com.thundersharp.admin.core.Model.AddressData;

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
            void onSaveSuccess(String employeeCode,String name,String type);
            void onSaveFailure(Exception e);
        }

    }
}
