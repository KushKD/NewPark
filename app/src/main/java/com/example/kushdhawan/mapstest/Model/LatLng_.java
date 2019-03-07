package com.example.kushdhawan.mapstest.Model;

import java.io.Serializable;

public class LatLng_ implements Serializable {

    public Double Latitude;
    public Double Longitude;


    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }


    @Override
    public String toString() {
        return "LatLng{" +
                "Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }
}
