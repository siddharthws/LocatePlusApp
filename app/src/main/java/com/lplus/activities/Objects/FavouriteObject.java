package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/22/2018.
 */

public class FavouriteObject {
    private String favourite_name;
    private String favourite_address;
    private Double favourite_lat;
    private Double favourite_lng;
    private String favourite_place_id;

    public String getFavourite_name() {
        return favourite_name;
    }

    public void setFavourite_name(String favourite_name) {
        this.favourite_name = favourite_name;
    }

    public String getFavourite_address() {
        return favourite_address;
    }

    public void setFavourite_address(String favourite_address) {
        this.favourite_address = favourite_address;
    }

    public Double getFavourite_lat() {
        return favourite_lat;
    }

    public void setFavourite_lat(Double favourite_lat) {
        this.favourite_lat = favourite_lat;
    }

    public Double getFavourite_lng() {
        return favourite_lng;
    }

    public void setFavourite_lng(Double favourite_lng) {
        this.favourite_lng = favourite_lng;
    }

    public String getFavourite_place_id() {
        return favourite_place_id;
    }

    public void setFavourite_place_id(String favourite_place_id) {
        this.favourite_place_id = favourite_place_id;
    }
}
