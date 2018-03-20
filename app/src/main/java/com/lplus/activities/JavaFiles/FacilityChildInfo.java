package com.lplus.activities.JavaFiles;

/**
 * Created by CHANDEL on 3/20/2018.
 */

public class FacilityChildInfo {
    private String facility_sequence = null;
    private String facility_name = null;
    private boolean state = false;

    public FacilityChildInfo(String facility_sequence, String facility_name) {
        this.facility_sequence = facility_sequence;
        this.facility_name = facility_name;
    }

    public String getFacility_sequence() {
        return facility_sequence;
    }

    public void setFacility_sequence(String facility_sequence) {
        this.facility_sequence = facility_sequence;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
