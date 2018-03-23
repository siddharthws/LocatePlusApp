package com.lplus.activities.Objects;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/23/2018.
 */

public class TempNewPhotoObject {
    private ArrayList<String> photoUID;
    private ArrayList<String> photoPath;

    public TempNewPhotoObject(ArrayList<String> photoUID, ArrayList<String> photoPath) {
        this.photoUID = photoUID;
        this.photoPath = photoPath;
    }

    public ArrayList<String> getPhotoUID() {
        return photoUID;
    }

    public void setPhotoUID(ArrayList<String> photoUID) {
        this.photoUID = photoUID;
    }

    public ArrayList<String> getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(ArrayList<String> photoPath) {
        this.photoPath = photoPath;
    }
}
