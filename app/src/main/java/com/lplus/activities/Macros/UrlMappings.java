package com.lplus.activities.Macros;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public class UrlMappings {
    private static final String ip = "http://172.16.18.52:8080";

    //functionalities

    public static final String REGISTER_APP         = ip + "/api/auth/registerUser";
    public static final String FILTER_FETCH         = ip + "/api/user/getFC";
    public static final String ADD_PLACE            = ip + "/api/user/addPlace";
    public static final String GET_MARKERS          = ip + "/api/user/getPlaces";
    public static final String ADD_REVIEWS          = ip + "/api/user/addReviews";
    public static final String ADD_PHOTOS           = ip + "/api/user/upload";
    public static final String GET_PHOTOS           = ip + "/api/user/getPhotoApp";
    public static final String GET_REVIEWS          = ip + "/api/user/getReviews";
    public static final String GET_RP_STATUS        = ip + "/api/user/getrpStatus";
    public static final String RATE_PLACE           = ip + "/api/user/addOverallRating";
    public static final String RATE_PHOTOS          = ip + "/api/user/photoRating";
    public static final String RATE_FACILITY        = ip + "/api/user/";
    public static final String RATE_CAN             = ip + "/api/user/infoRating";
    public static final String REGISTER_UDID        = ip + "/api/admin/validateUdid";
}
