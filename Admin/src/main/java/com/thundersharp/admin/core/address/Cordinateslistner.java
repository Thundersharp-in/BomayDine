package com.thundersharp.admin.core.address;

import com.google.android.gms.maps.model.LatLng;

public interface Cordinateslistner {

    void fetchAllCoordinates();

    interface fetchSuccessListener{
        void onCordinatesSuccess(LatLng... coOrdinates);
        void onCordinatesFailure(Exception exception);
    }

}
