package com.razortooth.smile.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.QuestionsManager;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.ui.adapter.FilesQuestionListAdapter;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class UsePreparedQuestionsActivity extends ListActivity {

    private Button btOk;

    private CheckBox cbQuestions;

    private Spinner spinnerHours;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;

    private String ip;

    private File[] fileQuestionsList;
    private File fileQuestion;

    private ArrayAdapter<File> adapter;

    private ListView lvListQuestions;
    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.use_prepared_questions_dialog);

        ip = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) this.getIntent().getSerializableExtra(GeneralActivity.PARAM_RESULTS);

        btOk = (Button) findViewById(R.id.bt_ok);
        cbQuestions = (CheckBox) findViewById(R.id.cb_questions);
        spinnerHours = (Spinner) findViewById(R.id.sp_hours);
        spinnerMinutes = (Spinner) findViewById(R.id.sp_minutes);
        spinnerSeconds = (Spinner) findViewById(R.id.sp_seconds);

        lvListQuestions = getListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btOk.setOnClickListener(new OkButtonListener());
        btOk.setEnabled(false);

        cbQuestions.setOnClickListener(new CbQuestionsButtonListener());
        cbQuestions.setClickable(false);

        spinnerHours.setEnabled(false);
        spinnerMinutes.setEnabled(false);
        spinnerSeconds.setEnabled(false);

        loadValuesTime();

        new LoadBoardTask(this).execute();
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
        spinnerHours.setAdapter(adapterHours);

        ArrayAdapter<String> adapterMinutes = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listMinutes);
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinutes.setAdapter(adapterMinutes);

        ArrayAdapter<?> adapterSeconds = ArrayAdapter.createFromResource(this, R.array.seconds,
            android.R.layout.simple_spinner_item);
        adapterSeconds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeconds.setAdapter(adapterSeconds);
    }

    private class OkButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            new LoadTask(UsePreparedQuestionsActivity.this).execute();
        }

    }

    private class CbQuestionsButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (cbQuestions.isChecked()) {
                spinnerHours.setEnabled(true);
                spinnerMinutes.setEnabled(true);
                spinnerSeconds.setEnabled(true);
            } else {
                spinnerHours.setEnabled(false);
                spinnerMinutes.setEnabled(false);
                spinnerSeconds.setEnabled(false);
            }
        }

    }

    private void openGeneralActivity() {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.PARAM_IP, ip);
        intent.putExtra(GeneralActivity.PARAM_RESULTS, results);
        intent.putExtra(GeneralActivity.PARAM_HOURS, spinnerHours.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.PARAM_MINUTES, spinnerMinutes.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.PARAM_SECONDS, spinnerSeconds.getSelectedItem().toString());
        startActivity(intent);

        ActivityUtil.showLongToast(this, R.string.starting);

        this.finish();
    }

    private File[] getQuestions() throws DataAccessException {
        return new QuestionsManager().getSavedQuestions();
    }

    private void loadQuestionsList() {
        clearSelection();
        adapter = new FilesQuestionListAdapter(this, fileQuestionsList);
        lvListQuestions.setAdapter(adapter);
        lvListQuestions.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvListQuestions.setItemsCanFocus(false);
        lvListQuestions.setOnItemClickListener(new ItemClickListener());
    }

    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            btOk.setEnabled(true);
            cbQuestions.setClickable(true);
            fileQuestion = adapter.getItem(position);
        }

    }

    private void clearSelection() {
        final int itemCount = lvListQuestions.getCount();
        for (int i = 0; i < itemCount; ++i) {
            lvListQuestions.setItemChecked(i, false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (fileQuestionsList.length == 0) {
            Intent intent = new Intent(this, ChooseActivityFlowDialog.class);
            intent.putExtra(GeneralActivity.PARAM_IP, ip);
            intent.putExtra(GeneralActivity.PARAM_RESULTS, results);
            startActivity(intent);
        }

        this.finish();
    }

    private class LoadBoardTask extends ProgressDialogAsyncTask<Void, File[]> {

        public LoadBoardTask(Activity context) {
            super(context);
        }

        @Override
        protected File[] doInBackground(Void... params) {

            try {
                return getQuestions();
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(File[] fileQuestions) {
            if (fileQuestions != null) {
                UsePreparedQuestionsActivity.this.fileQuestionsList = fileQuestions;
                UsePreparedQuestionsActivity.this.loadQuestionsList();
                if (fileQuestions.length == 0) {
                    btOk.setEnabled(false);
                    cbQuestions.setClickable(false);

                    ActivityUtil.showLongToast(UsePreparedQuestionsActivity.this,
                        "No Results Found");
                }
            }
            super.onPostExecute(fileQuestions);
        }

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
                Collection<Question> newQuestions = null;
                newQuestions = new QuestionsManager().loadQuestions(fileQuestion.getName());

                new SmilePlugServerManager().startUsingPreparedQuestions(ip, context, newQuestions);

                return true;
            } catch (NetworkErrorException e) {
                handleException(e);
                return false;
            } catch (DataAccessException e) {
                handleException(e);
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