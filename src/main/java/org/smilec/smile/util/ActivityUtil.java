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
package org.smilec.smile.util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class ActivityUtil {

    public static final Activity getRoot(Activity activity) {
        Activity ctx = activity;
        Activity parent;

        while ((parent = ctx.getParent()) != null) {
            ctx = parent;
        }

        return ctx;
    }

    public static void showLongToast(Context context, int stringId) {
        showLongToast(context, context.getString(stringId));
    }

    public static void showLongToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static Display getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

}
