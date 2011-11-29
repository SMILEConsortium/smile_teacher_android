package com.razortooth.smile.util;

import android.content.Context;
import android.widget.Toast;

public class ActivityUtil {

    public static void showLongToast(Context context, int stringId) {
        showLongToast(context, context.getString(stringId));
    }

    public static void showLongToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

}
