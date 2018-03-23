package com.lplus.activities.Objects;

/**
 * Created by CHANDEL on 3/23/2018.
 */

public class UnSyncObject {
    String photo_ids;
    String photo_paths;
    String place_name;
    String place_addres;
    Double place_lat;
    Double place_lng;
    String place_category;
    String place_facilities;
    String place_decription;

    public UnSyncObject(String photo_ids, String photo_paths, String place_name, String place_addres,Double place_lat, Double place_lng, String place_category, String place_facilities, String place_decription) {
        this.photo_ids = photo_ids;
        this.photo_paths = photo_paths;
        this.place_name = place_name;
        this.place_addres = place_addres;
        this.place_lat = place_lat;
        this.place_lng = place_lng;
        this.place_category = place_category;
        this.place_facilities = place_facilities;
        this.place_decription = place_decription;
    }
    public UnSyncObject() {

    }

    public Double getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(Double place_lat) {
        this.place_lat = place_lat;
    }

    public Double getPlace_lng() {
        return place_lng;
    }

    public void setPlace_lng(Double place_lng) {
        this.place_lng = place_lng;
    }

    public String getPhoto_ids() {
        return photo_ids;
    }

    public void setPhoto_ids(String photo_ids) {
        this.photo_ids = photo_ids;
    }

    public String getPhoto_paths() {
        return photo_paths;
    }

    public void setPhoto_paths(String photo_paths) {
        this.photo_paths = photo_paths;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_addres() {
        return place_addres;
    }

    public void setPlace_addres(String place_addres) {
        this.place_addres = place_addres;
    }

    public String getPlace_category() {
        return place_category;
    }

    public void setPlace_category(String place_category) {
        this.place_category = place_category;
    }

    public String getPlace_facilities() {
        return place_facilities;
    }

    public void setPlace_facilities(String place_facilities) {
        this.place_facilities = place_facilities;
    }

    public String getPlace_decription() {
        return place_decription;
    }

    public void setPlace_decription(String place_decription) {
        this.place_decription = place_decription;
    }
}
