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
package org.smile.smilec.bu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.smile.smilec.util.ActivityUtil;

public class NetworkManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent
            .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            DisplayWifiState(context, intent);
        }
    }

    private void DisplayWifiState(Context context, Intent intent) {
        NetworkInfo currentNetworkInfo = (NetworkInfo) intent
            .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        NetworkInfo otherNetworkInfo = (NetworkInfo) intent
            .getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

        if (currentNetworkInfo.equals(otherNetworkInfo)) {
            ActivityUtil.showLongToast(context, "IP changed! Current is " + currentNetworkInfo);
        }
    }
}
