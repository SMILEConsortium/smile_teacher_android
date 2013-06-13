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
package org.smilec.smile.util.ui;

import org.smilec.smile.R;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public abstract class ProgressDialogAsyncTask<Params, Result> extends
    AsyncTask<Params, Void, Result> {

    public static interface TaskFinishedListener {
        void onTaskEnd();
    }

    private ProgressDialog pd;
    protected final Activity context;
    private final String title;
    private final String message;
    private Exception e;
    private TaskFinishedListener listener;
    private boolean showDialog;
    private Integer errorMessageStringId;

    public ProgressDialogAsyncTask(Activity context, boolean showDialog,
        TaskFinishedListener listener) {
        this(context, showDialog, listener, null);
    }

    public ProgressDialogAsyncTask(Activity context, boolean showDialog,
        TaskFinishedListener listener, Integer errorMessageStringId) {
        this.context = context;
        this.title = null;
        this.message = context.getString(R.string.loading);
        this.listener = listener;
        this.showDialog = showDialog;
        this.errorMessageStringId = errorMessageStringId;
    }

    public ProgressDialogAsyncTask(Activity context) {
        this(context, true, null);
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            this.pd = ProgressDialog.show(ActivityUtil.getRoot(context), title, message);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (pd != null) {
            pd.hide();
            pd.dismiss();
            pd = null;
        }
        if (e != null) {

            String msg;

            if (errorMessageStringId == null) {
                msg = "Update the IP address and try again.";
            } else {
                msg = context.getString(errorMessageStringId);
            }

            String exMsg = e.getMessage();
            String erro = null;

            if (!StringUtils.isEmpty(exMsg)) {
                erro = exMsg;
            } else {

                Throwable cause = e.getCause();

                if (cause != null) {
                    exMsg = cause.getMessage();
                }

                if (!StringUtils.isEmpty(exMsg)) {
                    erro = exMsg;
                }

            }

            Log.e(Constants.LOG_CATEGORY, erro, e);
            ActivityUtil.showLongToast(context, msg);

        } else if (listener != null) {
            listener.onTaskEnd();
        }
    };

    protected void handleException(Exception e) {
        this.e = e;
    }
}
