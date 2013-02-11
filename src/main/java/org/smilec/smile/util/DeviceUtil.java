package org.smile.smilec.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class DeviceUtil {

    private DeviceUtil() {
        // Empty
    }

    public static final boolean isConnected(Context context) {

        // Check the ConnectivityManager
        ConnectivityManager cm = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            Log.e("DeviceUtil", "ConnectivityManager is null");
            return false;
        }

        // Check the NetworkInfo
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni == null) {
            Log.e("DeviceUtil", "NetworkInfo is null");
            return false;
        }

        return ni.isConnected();

    }

    public static final boolean isExternalStorageAvailable() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;

    }

    public static final boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
