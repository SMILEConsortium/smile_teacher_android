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
package org.smilec.smile.ui;

import org.smilec.smile.R;
import org.smilec.smile.bu.BoardManager;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.domain.Results;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.DialogUtil;
import org.smilec.smile.util.ui.ProgressDialogAsyncTask;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseActivityFlowDialog extends Activity {

    private String ip;
    private String status;

    private Button btStart;
    private Button btUse;

    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);
        Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
        getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());

        btStart = (Button) findViewById(R.id.bt_start);
        btUse = (Button) findViewById(R.id.bt_use_prerared);

        status = this.getIntent().getStringExtra(GeneralActivity.PARAM_STATUS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GeneralActivity.PARAM_IP, ip);
        outState.putSerializable(GeneralActivity.PARAM_RESULTS, results);
        outState.putSerializable(GeneralActivity.PARAM_STATUS, status);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ip = savedInstanceState.getString(GeneralActivity.PARAM_IP);
        results = (Results) savedInstanceState.getSerializable(GeneralActivity.PARAM_RESULTS);
        status = savedInstanceState.getString(GeneralActivity.PARAM_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ip = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        
        btStart.setOnClickListener(new StartButtonListener());
        btUse.setOnClickListener(new UsePreparedQuestionsButtonListener());

        if (results == null) {
            new UpdateResultsTask(ip, this).execute();
        }
    }

    private class StartButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (status != null) {
                if (!status.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                        ChooseActivityFlowDialog.this);
                    builder.setMessage(R.string.game_running).setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                new LoadTask(ChooseActivityFlowDialog.this).execute();
                            }
                        });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    new LoadTask(ChooseActivityFlowDialog.this).execute();
                }
            } else {
                new LoadTask(ChooseActivityFlowDialog.this).execute();
            }
            ActivityUtil.showLongToast(ChooseActivityFlowDialog.this, R.string.starting);
        }
    }

    private class UsePreparedQuestionsButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChooseActivityFlowDialog.this, UsePreparedQuestionsActivity.class);
            intent.putExtra(GeneralActivity.PARAM_IP, ip);
            intent.putExtra(GeneralActivity.PARAM_RESULTS, results);
            intent.putExtra(GeneralActivity.PARAM_STATUS, status);
            startActivity(intent);
            ChooseActivityFlowDialog.this.finish();
        }
    }

    private class UpdateResultsTask extends AsyncTask<Void, Void, Results> {

        private String ip;
        private Context context;

        private UpdateResultsTask(String ip, Context context) {
            this.ip = ip;
            this.context = context;
        }

        @Override
        protected Results doInBackground(Void... arg0) {

            try {
                Results retrieveResults = new BoardManager().retrieveResults(ip, context);
                return retrieveResults;
            } catch (NetworkErrorException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Results results) {
            if (results != null) {
                ChooseActivityFlowDialog.this.results = results;
            }
        }

    }

    private void openGeneralActivity() {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.PARAM_IP, ip);
        intent.putExtra(GeneralActivity.PARAM_RESULTS, results);
        intent.putExtra(GeneralActivity.PARAM_STATUS, status);
        startActivity(intent);
        this.finish();
    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                new SmilePlugServerManager().startMakingQuestions(ip, context);

                return "";
            } catch (Exception e) {
                handleException(e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            if (!message.equals("")) {
                DialogUtil.checkConnection(ChooseActivityFlowDialog.this, message);
            } else {
                ChooseActivityFlowDialog.this.openGeneralActivity();
            }
        }
    }
}