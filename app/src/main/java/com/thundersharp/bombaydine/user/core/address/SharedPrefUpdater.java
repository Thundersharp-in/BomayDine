package com.thundersharp.bombaydine.user.core.address;

import com.thundersharp.bombaydine.user.core.Model.AddressData;

public interface SharedPrefUpdater {

    void SaveDataToSharedPref(AddressData addressData);

    void updatehomelocationData();

    interface OnSharedprefUpdated{

        void onSharedPrefUpdate(AddressData addressData);
    }
}
