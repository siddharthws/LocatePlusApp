package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/30/2018.
 */

public class PhotoObject {
    String place_id;
    ArrayList<String> photo_uuids;
    ArrayList<String> photo_paths;

    public PhotoObject(String place_id, ArrayList<String> photo_uuids, ArrayList<String> photo_paths) {
        this.place_id = place_id;
        this.photo_uuids = photo_uuids;
        this.photo_paths = photo_paths;
    }

    public PhotoObject() {

    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public ArrayList<String> getPhoto_uuids() {
        return photo_uuids;
    }

    public void setPhoto_uuids(ArrayList<String> photo_uuids) {
        this.photo_uuids = photo_uuids;
    }

    public ArrayList<String> getPhoto_paths() {
        return photo_paths;
    }

    public void setPhoto_paths(ArrayList<String> photo_paths) {
        this.photo_paths = photo_paths;
    }
}
