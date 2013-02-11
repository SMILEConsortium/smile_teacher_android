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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public class DialogUtil {

    public static void checkConnection(final Activity activity, String message) {

        Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Connection error!");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.show();
    }
}
