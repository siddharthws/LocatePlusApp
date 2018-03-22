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
    private String markerDescription;


    public String getMarkerDescription() {
        return markerDescription;
    }

    public MarkerObject(String markerID, String markerName, String markerAddress, ArrayList<String> markerFacilities, String markerCategory, double markerLatitude, double markerLongitude, String markerDescription) {
        this.markerID = markerID;
        this.markerName = markerName;
        this.markerAddress = markerAddress;
        this.markerFacilities = markerFacilities;
        this.markerCategory = markerCategory;
        this.markerLatitude = markerLatitude;
        this.markerLongitude = markerLongitude;
        this.markerDescription = markerDescription;
    }

    public void setMarkerDescription(String markerDescription) {
        this.markerDescription = markerDescription;
    }

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


    public void setMarkerFacilities(ArrayList<String> markerFacilities) {
        this.markerFacilities = markerFacilities;
    }

    public MarkerObject()
    {

    }
}
