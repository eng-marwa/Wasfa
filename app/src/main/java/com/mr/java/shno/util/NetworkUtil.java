package com.mr.java.shno.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by java on 10/11/2017.
 */

public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

}
