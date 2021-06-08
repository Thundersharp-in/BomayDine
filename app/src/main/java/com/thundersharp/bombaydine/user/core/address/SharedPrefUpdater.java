package com.thundersharp.bombaydine.user.core.address;

import com.thundersharp.bombaydine.user.core.Model.AddressData;

import java.util.HashMap;

public interface SharedPrefUpdater {

    void SaveDataToSharedPref(AddressData addressData);

    void updatehomelocationData();

    AddressData getSavedHomeLocationData();

    void saveNamePhoneData(String name, String phone);

    interface OnSharedprefUpdated{

        void onSharedPrefUpdate(AddressData addressData);
    }

    interface namePhoneUpdate{
        void namePhoneUpdated(String name, String phone);
    }

    interface AccountSwitch{

        void saveSwitchedUser(HashMap<String , String> data);

        interface lisetner{
            void onSaveSuccess(String employeeCode,String name,String type);
            void onSaveFailure(Exception e);
        }

    }
}
