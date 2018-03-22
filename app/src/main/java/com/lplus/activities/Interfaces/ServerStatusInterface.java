package com.lplus.activities.Interfaces;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public interface ServerStatusInterface {
    void onStatusSuccess(int serverResponseFC, int serverResponseGP);
    void onStatusFailure();
}
