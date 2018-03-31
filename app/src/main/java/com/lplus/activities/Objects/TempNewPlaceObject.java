package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 20-03-2018.
 */

public class TempNewPlaceObject {

    private String name;
    private String address;
    private String category;
    private ArrayList<String> facilities;
    private ArrayList<String> uuids;
    private String photos;
    private double latitude;
    private double longitude;
    private String description;
    private String contact;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public TempNewPlaceObject(String name, String address, String category, ArrayList<String> facilities, ArrayList<String> uuids, double latitude, double longitude, String description, String contact) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.facilities = facilities;
        this.uuids = uuids;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.contact = contact;
    }
}
