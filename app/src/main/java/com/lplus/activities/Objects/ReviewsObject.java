package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public class ReviewsObject {

    private String placeId;

    public String getPlaceId() {
        return placeId;
    }
    public ReviewsObject ()
    {

    }

    public ReviewsObject(String placeId, ArrayList<String> reviews) {
        this.placeId = placeId;
        this.reviews = reviews;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    private ArrayList<String> reviews;
}
