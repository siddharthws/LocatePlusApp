package com.lplus.activities.Models;

/**
 * Created by Sai_Kameswari on 20-03-2018.
 */

public class TempNewPlaceObject {

    private String name;
    private String address;
    private String category;
    private String facilities;
    private String photos;
    private double latitude;
    private double longitude;
    private long timestamp;

    public TempNewPlaceObject(String name, String address, String category, String facilities, double latitude, double longitude)
    {
        this.name = name;
        this.address = address;
        this.category = category;
        this.facilities = facilities;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
