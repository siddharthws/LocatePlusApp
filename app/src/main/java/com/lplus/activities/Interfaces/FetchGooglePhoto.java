package com.lplus.activities.Interfaces;

import android.graphics.Bitmap;

/**
 * Created by CHANDEL on 3/30/2018.
 */

public interface FetchGooglePhoto {
    void onMapPhotoFetched(Bitmap bitmap);
    void onMapPhotoFailed();
}

