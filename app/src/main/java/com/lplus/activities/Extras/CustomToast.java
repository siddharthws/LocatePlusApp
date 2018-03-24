package com.lplus.activities.Extras;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import es.dmoral.toasty.Toasty;

/**
 * Created by CHANDEL on 3/24/2018.
 */

public class CustomToast {
    public static void configToast() {
        Toasty.Config.getInstance()
        .apply(); // required
    }
}
