package com.lplus.activities.Interfaces;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public interface ReviewsStatusInterface {
    void onUpdateRequired(int reviewResponse, int photoResponse);
    void onUpdateNotRequired();
}
