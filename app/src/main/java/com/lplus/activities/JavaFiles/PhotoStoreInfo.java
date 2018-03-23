package com.lplus.activities.JavaFiles;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/21/2018.
 */

public class PhotoStoreInfo {
    private int maxImageCount = 5;
    private ArrayList<String> photo_array;
    private ArrayList<String> photo_uuid_array;

    public PhotoStoreInfo() {
        this.maxImageCount = maxImageCount;
    }

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    public ArrayList<String> getPhoto_array() {
        return photo_array;
    }

    public void setPhoto_array(ArrayList<String> photo_array) {
        this.photo_array = photo_array;
    }

    public ArrayList<String> getPhoto_uuid_array() {
        return photo_uuid_array;
    }

    public void setPhoto_uuid_array(ArrayList<String> photo_uuid_array) {
        this.photo_uuid_array = photo_uuid_array;
    }
}
