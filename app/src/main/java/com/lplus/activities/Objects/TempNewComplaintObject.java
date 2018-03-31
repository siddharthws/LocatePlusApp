package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by Siddharth on 31-03-2018.
 */

public class TempNewComplaintObject {

    private String name;
    private String address;
    private ArrayList<String> uuids;
    private String photos;
    private double latitude;
    private double longitude;
    private String description;

    public ArrayList<String> getUuids() {
        return uuids;
    }

    public void setUuids(ArrayList<String> uuids) {
        this.uuids = uuids;
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

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TempNewComplaintObject(String name, String address, ArrayList<String> uuids,double latitude, double longitude, String description) {
        this.name = name;
        this.address = address;
        this.uuids = uuids;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }
}
