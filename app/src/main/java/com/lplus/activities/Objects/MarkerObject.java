package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 21-03-2018.
 */

public class MarkerObject {

    private String markerID;
    private String markerName;
    private String markerAddress;
    private ArrayList<String> markerFacilities;
    private String markerCategory;
    private double markerLatitude;
    private double markerLongitude;
    private ArrayList<String> reviews_list;

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getMarkerAddress() {
        return markerAddress;
    }

    public void setMarkerAddress(String markerAddress) {
        this.markerAddress = markerAddress;
    }



    public String getMarkerCategory() {
        return markerCategory;
    }

    public void setMarkerCategory(String markerCategory) {
        this.markerCategory = markerCategory;
    }

    public double getMarkerLatitude() {
        return markerLatitude;
    }

    public void setMarkerLatitude(double markerLatitude) {
        this.markerLatitude = markerLatitude;
    }

    public double getMarkerLongitude() {
        return markerLongitude;
    }

    public void setMarkerLongitude(double markerLongitude) {
        this.markerLongitude = markerLongitude;
    }

    public ArrayList<String> getMarkerFacilities() {
        return markerFacilities;
    }

    public ArrayList<String> getReviews_list() {
        return reviews_list;
    }

    public void setReviews_list(ArrayList<String> reviews_list) {
        this.reviews_list = reviews_list;
    }

    public void setMarkerFacilities(ArrayList<String> markerFacilities) {
        this.markerFacilities = markerFacilities;
    }

    public MarkerObject()
    {

    }
}
