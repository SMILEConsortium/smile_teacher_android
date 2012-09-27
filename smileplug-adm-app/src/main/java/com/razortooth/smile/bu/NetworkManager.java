package com.razortooth.smile.bu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.razortooth.smile.util.ActivityUtil;

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
