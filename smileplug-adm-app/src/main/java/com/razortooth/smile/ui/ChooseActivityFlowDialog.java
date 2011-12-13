package com.razortooth.smile.ui;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class ChooseActivityFlowDialog extends Activity {

    private String ip;

    private Button btStart;
    private Button btUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);

        btStart = (Button) findViewById(R.id.bt_start);
        btUse = (Button) findViewById(R.id.bt_use_prerared);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ip = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);

        btStart.setOnClickListener(new StartButtonListener());
        btUse.setOnClickListener(new UsePreparedQuestionsButtonListener());
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
            startActivity(intent);
            ChooseActivityFlowDialog.this.finish();
        }
    }

    private void openGeneralActivity() {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.PARAM_IP, ip);
        startActivity(intent);

        this.finish();
    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, Boolean> {

        private Context context;

        public LoadTask(Activity context) {
            super(context);

            this.context = context;
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
            if (connected == false) {
                DialogUtil.checkConnection(ChooseActivityFlowDialog.this);
            } else {
                ChooseActivityFlowDialog.this.openGeneralActivity();
            }
        }
    }
}