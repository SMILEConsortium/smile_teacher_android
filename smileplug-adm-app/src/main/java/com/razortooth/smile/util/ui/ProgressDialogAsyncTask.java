package com.razortooth.smile.util.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.StringUtils;

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
