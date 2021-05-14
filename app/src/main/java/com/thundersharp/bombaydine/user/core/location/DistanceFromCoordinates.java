package com.thundersharp.bombaydine.user.core.location;

import com.google.android.gms.maps.model.LatLng;

public final class DistanceFromCoordinates {

    public static DistanceFromCoordinates getInstance(){
        return new DistanceFromCoordinates();
    }

    public double convertLatLongToDistance(LatLng startPoints, LatLng endPoints){
        return distance(startPoints.latitude,startPoints.longitude,endPoints.latitude,endPoints.longitude);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist)*1.6;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
