package com.lplus.activities.Interfaces;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public interface PhotoFetchStatusInterface {
    void onPhotoFetched(ArrayList<String> images, ArrayList<String> uuids);
    void onPhotoFetchFailed();
}
