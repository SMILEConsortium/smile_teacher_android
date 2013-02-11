/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
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
