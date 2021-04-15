package com.thundersharp.bombaydine.user.core.address;

import com.google.android.gms.tasks.Task;
import com.thundersharp.bombaydine.user.core.Model.AddressData;

public interface AddressUpdater {

    interface onAddressDataUpdateToFirbase{
        void dataUpdate(AddressData adata);
    }

    interface OnAddressUpdateListner{
        void onAddressUpdate(Task<Void> task, boolean isTaskSucessful);

        void onAddressUpdateFailure(Exception e);
    }

}
