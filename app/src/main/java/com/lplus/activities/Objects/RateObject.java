package com.lplus.activities.Objects;

/**
 * Created by CHANDEL on 3/27/2018.
 */

public class RateObject {
    String placeId;
    Double placerate;

    public RateObject(String placeId, Double placerate) {
        this.placeId = placeId;
        this.placerate = placerate;
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
}
