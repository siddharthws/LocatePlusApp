package com.lplus.activities.Objects;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public class NewPlace {

    public static final String TABLE_NAME = "newPlaces";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LATITUDE = "newLat";
    public static final String COLUMN_LONGITUDE = "newLong";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_FACILITIES = "facilities";
    public static final String COLUMN_PHOTOS = "photos";



    private int id;
    private String name;
    private String address;
    private String category;
    private String facilities;
    private String photos;
    private double latitude;
    private double longitude;
    private long timestamp;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_LATITUDE       + " REAL,"
                    + COLUMN_LONGITUDE      + " REAL,"
                    + COLUMN_TIMESTAMP      + " INTEGER,"
                    + COLUMN_NAME           + " TEXT,"
                    + COLUMN_ADDRESS        + " TEXT,"
                    + COLUMN_CATEGORY       + " TEXT,"
                    + COLUMN_FACILITIES     + " TEXT,"
                    + COLUMN_PHOTOS         + " TEXT,"
                    + ")";

    public NewPlace() {}

    public NewPlace(String name, String address, String category, String facilities, String photos, double latitude, double longitude, long timestamp)
    {
        this.name = name;
        this.address = address;
        this.category = category;
        this.facilities = facilities;
        this.photos = photos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
