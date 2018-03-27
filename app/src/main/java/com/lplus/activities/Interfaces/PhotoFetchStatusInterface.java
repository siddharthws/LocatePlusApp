package com.lplus.activities.Interfaces;

import android.graphics.Bitmap;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public interface PhotoFetchStatusInterface {
    void onPhotoFetched(Bitmap bitmap);
    void onPhotoFetchFailed();
}
