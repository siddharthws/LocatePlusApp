package com.lplus.activities.Objects;

/**
 * Created by CHANDEL on 3/27/2018.
 */

public class RateObject {
    String placeId;
    Double placerate;
    int rateusers;

    public RateObject(String placeId, Double placerate, int rateusers) {
        this.placeId = placeId;
        this.rateusers = rateusers;
    }

    public RateObject() {

    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getPlacerate() {
        return placerate;
    }

    public void setPlacerate(Double placerate) {
        this.placerate = placerate;
    }

    public int getRateusers() {
        return rateusers;
    }

    public void setRateusers(int rateusers) {
        this.rateusers = rateusers;
    }
}
