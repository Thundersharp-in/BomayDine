package com.thundersharp.admin.core.address;


import com.thundersharp.admin.core.Model.AddressData;

import java.util.List;

public interface AddressLoader {

    void loaduseraddress();

    void refreshAddress();

    interface onAddresLoadListner{
        void onAddressLoaded(List<AddressData> addressData);

        void onAddressLoadFailure(Exception e);
    }
}
