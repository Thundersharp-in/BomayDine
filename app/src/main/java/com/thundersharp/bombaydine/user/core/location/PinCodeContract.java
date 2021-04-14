package com.thundersharp.bombaydine.user.core.location;

import androidx.annotation.NonNull;

import com.thundersharp.bombaydine.user.core.Model.DataresponsePojo;

import org.json.JSONObject;

public interface PinCodeContract {

    interface PinccodeInteractor{
        void getdetailsfromPincode(@NonNull String pinCode);
    }

    interface onPinDatafetchListner{
        void onDataFetch(JSONObject data);

        void onDataFetchFailureListner(Exception e);
    }

}
