package com.lplus.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sai_Kameswari on 18-03-2018.
 */

public class InternetConnectivityCheck {

    public static boolean isConnectedToNetwork(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // Validate network info and connection status and return result
        if ((networkInfo == null)  && !networkInfo.isConnected())
        {
            return false;
        }
        return true;
    }
}
