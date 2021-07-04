package com.thundersharp.bombaydine.user.core.address;

import com.google.android.gms.maps.model.LatLng;

public interface Cordinateslistner {

    void fetchAllCoordinates();

    void fetchAllCoordinatesFromStorage();

    void saveCoordinatesToStorage(LatLng... coOrdinates);

    interface fetchSuccessListener{
        void onCordinatesSuccess(LatLng... coOrdinates);
        void onCordinatesFailure(Exception exception);
    }

}
