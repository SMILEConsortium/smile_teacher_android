package com.razortooth.smile.ui;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class ChooseActivityFlowDialog extends Activity {

    private String ip;

    private Button btStart;
    private Button btUse;

    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);

        btStart = (Button) findViewById(R.id.bt_start);
        btUse = (Button) findViewById(R.id.bt_use_prerared);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("ip", ip);
        outState.putSerializable("results", results);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ip = savedInstanceState.getString("ip");
        results = (Results) savedInstanceState.getSerializable("results");
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
            new LoadTask(ChooseActivityFlowDialog.this).execute();
            ActivityUtil.showLongToast(ChooseActivityFlowDialog.this, R.string.starting);
        }
    }

    private class UsePreparedQuestionsButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChooseActivityFlowDialog.this,
                UsePreparedQuestionsActivity.class);
            intent.putExtra(GeneralActivity.PARAM_IP, ip);
            intent.putExtra(GeneralActivity.PARAM_RESULTS, results);
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
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e);
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e);
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
        startActivity(intent);

        this.finish();
    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, Boolean> {

        public LoadTask(Activity context) {
            super(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                new SmilePlugServerManager().startMakingQuestions(ip, context);
                return true;
            } catch (NetworkErrorException e) {
                handleException(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            super.onPostExecute(connected);
            if (!connected) {
                DialogUtil.checkConnection(ChooseActivityFlowDialog.this);
            } else {
                ChooseActivityFlowDialog.this.openGeneralActivity();
            }
        }
    }
}