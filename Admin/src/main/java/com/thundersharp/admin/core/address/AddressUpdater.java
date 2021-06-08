package com.thundersharp.admin.core.address;

import com.google.android.gms.tasks.Task;
import com.thundersharp.admin.core.Model.AddressData;

public interface AddressUpdater {

    interface onAddressDataUpdateToFirbase{
        void dataUpdate(AddressData adata);
    }

    interface OnAddressUpdateListner{
        void onAddressUpdate(Task<Void> task, boolean isTaskSucessful);

        void onAddressUpdateFailure(Exception e);
    }

}
