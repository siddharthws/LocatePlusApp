package com.lplus.activities.Extras;

/**
 * Created by CHANDEL on 3/30/2018.
 */

public class GetMapImage {
    public static String MapURL(String lat, String lng, String color) {
        String URL = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=13&size=600x300&maptype=roadmap&markers=color:green%7Clabel:G%7C"+lat+","+lng+"&key=AIzaSyBdDH73DMCqSxaq_bdgsntFmbzxFAMMiwc";

        return URL;
    }
}
