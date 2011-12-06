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

public class ChooseActivityFlowDialog extends Activity implements OnClickListener {

    private String ip;

    private Button start;
    private Button use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);

        start = (Button) findViewById(R.id.bt_start);
        use = (Button) findViewById(R.id.bt_use_prerared);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ip = this.getIntent().getStringExtra(GeneralActivity.IP);

        start.setOnClickListener(this);
        use.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                new LoadTask(this).execute();
                ActivityUtil.showLongToast(this, R.string.starting);
                break;

            case R.id.bt_use_prerared:
                Intent intent = new Intent(this, UsePreparedQuestionsActivity.class);
                intent.putExtra(GeneralActivity.IP, ip);
                startActivity(intent);
                break;
        }
    }

    private void openGeneralActivity() {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.IP, ip);
        startActivity(intent);
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