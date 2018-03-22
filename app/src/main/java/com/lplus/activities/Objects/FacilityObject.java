package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class FacilityObject {

    private ArrayList<String> facility_ids;
    private ArrayList<String> facility_values;

    public ArrayList<String> getFacility_ids() {
        return facility_ids;
    }

    public void setFacility_ids(ArrayList<String> facility_ids) {
        this.facility_ids = facility_ids;
    }

    public ArrayList<String> getFacility_values() {
        return facility_values;
    }

    public void setFacility_values(ArrayList<String> facility_values) {
        this.facility_values = facility_values;
    }
}
