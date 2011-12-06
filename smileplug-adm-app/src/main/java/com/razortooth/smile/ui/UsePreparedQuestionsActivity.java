package com.razortooth.smile.ui;

import java.util.ArrayList;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class UsePreparedQuestionsActivity extends Activity implements OnClickListener {

    private Button ok;
    private CheckBox checkBox;

    private Spinner hours;
    private Spinner minutes;
    private Spinner seconds;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.use_prepared_questions_dialog);

        ip = this.getIntent().getStringExtra(GeneralActivity.IP);

        ok = (Button) findViewById(R.id.bt_ok);
        checkBox = (CheckBox) findViewById(R.id.cb_questions);
        hours = (Spinner) findViewById(R.id.sp_hours);
        minutes = (Spinner) findViewById(R.id.sp_minutes);
        seconds = (Spinner) findViewById(R.id.sp_seconds);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ok.setOnClickListener(this);

        checkBox.setOnClickListener(this);

        hours.setEnabled(false);
        minutes.setEnabled(false);
        seconds.setEnabled(false);

        loadValuesTime();
    }

    private void loadValuesTime() {
        List<String> listHours = new ArrayList<String>();
        for (int i = 0; i <= 24; i++) {
            listHours.add("" + i);
        }

        List<String> listMinutes = new ArrayList<String>();
        for (int i = 0; i <= 60; i++) {
            listMinutes.add("" + i);
        }

        ArrayAdapter<String> adapterHours = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listHours);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours.setAdapter(adapterHours);

        ArrayAdapter<String> adapterMinutes = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listMinutes);
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutes.setAdapter(adapterMinutes);

        ArrayAdapter<?> adapterSeconds = ArrayAdapter.createFromResource(this, R.array.seconds,
            android.R.layout.simple_spinner_item);
        adapterSeconds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seconds.setAdapter(adapterSeconds);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_questions:
                if (checkBox.isChecked()) {
                    hours.setEnabled(true);
                    minutes.setEnabled(true);
                    seconds.setEnabled(true);
                } else {
                    hours.setEnabled(false);
                    minutes.setEnabled(false);
                    seconds.setEnabled(false);
                }
                break;
            case R.id.bt_ok:
                new LoadTask(this).execute();
                break;
        }
    }

    private void openGeneralActivity() {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.IP, ip);
        intent.putExtra(GeneralActivity.HOURS, hours.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.MINUTES, minutes.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.SECONDS, seconds.getSelectedItem().toString());
        startActivity(intent);

        ActivityUtil.showLongToast(this, R.string.starting);

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

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            super.onPostExecute(connected);
            if (connected == false) {
                DialogUtil.checkConnection(UsePreparedQuestionsActivity.this);
            } else {
                UsePreparedQuestionsActivity.this.openGeneralActivity();
            }
        }
    }
}