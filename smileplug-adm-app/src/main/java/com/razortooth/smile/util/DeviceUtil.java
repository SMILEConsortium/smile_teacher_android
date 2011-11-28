package com.razortooth.smile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DeviceUtil {

	private DeviceUtil(){}

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

}
