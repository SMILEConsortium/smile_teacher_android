package com.razortooth.smile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public class DialogUtil {

    public static void checkConnection(final Activity activity) {

        Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Connection error!");
        builder.setMessage("Please verify the IP and try again.");
        builder.setPositiveButton("Ok", null);
        builder.show();
    }
}
