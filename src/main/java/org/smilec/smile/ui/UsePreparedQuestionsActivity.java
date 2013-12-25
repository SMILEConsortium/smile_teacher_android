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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.smilec.smile.R;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.QuestionsManager;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.domain.IQSet;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Results;
import org.smilec.smile.ui.adapter.IQSetListAdapter;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.CloseClickListenerUtil;
import org.smilec.smile.util.DialogUtil;
import org.smilec.smile.util.ui.ProgressDialogAsyncTask;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

public class UsePreparedQuestionsActivity extends ListActivity {

	private Button btOk;

	private ImageButton btClose;

    private CheckBox cbQuestions;

    private Spinner spinnerHours;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;

    private String ip;
    private String status;

    private List<IQSet> iqsets;
    private IQSet iqset;
    private IQSetListAdapter iqsetListAdapter;

    private ListView lvListQuestions;
    private Results results;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.use_prepared_questions_dialog);
        Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
        getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());

        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getText(R.string.use_prepared).toString().toUpperCase());

        ip = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) this.getIntent().getSerializableExtra(GeneralActivity.PARAM_RESULTS);
        status = this.getIntent().getStringExtra(GeneralActivity.PARAM_STATUS);

        btOk = (Button) findViewById(R.id.bt_ok);
        cbQuestions = (CheckBox) findViewById(R.id.cb_questions);
        spinnerHours = (Spinner) findViewById(R.id.sp_hours);
        spinnerMinutes = (Spinner) findViewById(R.id.sp_minutes);
        spinnerSeconds = (Spinner) findViewById(R.id.sp_seconds);
        btClose = (ImageButton) findViewById(R.id.bt_close);

        lvListQuestions = getListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btOk.setOnClickListener(new LoadButtonListener());
        btOk.setEnabled(false);

        cbQuestions.setOnClickListener(new CbQuestionsButtonListener());
        cbQuestions.setClickable(false);

        spinnerHours.setEnabled(false);
        spinnerMinutes.setEnabled(false);
        spinnerSeconds.setEnabled(false);
        
        btClose.setOnClickListener(new CloseClickListenerUtil(this));

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

    private class LoadButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (status != null) {
                Log.d("SMILE Teacher", "Status = " + status);
                if (!status.equals("") && !status.equals("START_MAKE")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                        UsePreparedQuestionsActivity.this);
                    builder.setMessage(R.string.game_running).setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                new LoadTask(UsePreparedQuestionsActivity.this).execute();
                            }
                        });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    new LoadTask(UsePreparedQuestionsActivity.this).execute();
                }
            } else {
                new LoadTask(UsePreparedQuestionsActivity.this).execute();
            }
            ActivityUtil.showLongToast(UsePreparedQuestionsActivity.this, R.string.toast_starting);
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
        intent.putExtra(GeneralActivity.PARAM_STATUS, status);
        intent.putExtra(GeneralActivity.PARAM_HOURS, spinnerHours.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.PARAM_MINUTES, spinnerMinutes.getSelectedItem().toString());
        intent.putExtra(GeneralActivity.PARAM_SECONDS, spinnerSeconds.getSelectedItem().toString());
        startActivity(intent);

        ActivityUtil.showLongToast(this, R.string.toast_starting);

        this.finish();
    }

    private File[] getQuestions() throws DataAccessException {
        return new QuestionsManager().getSavedQuestions();
    }

    private void loadQuestionsList() {
        clearSelection();

		List<IQSet> iqsets = new ArrayList<IQSet>();
		try {
			iqsets = new SmilePlugServerManager().getIQSets(ip, UsePreparedQuestionsActivity.this);
		} catch (NetworkErrorException e) {
			e.printStackTrace();
		}
        
		iqsetListAdapter = new IQSetListAdapter(UsePreparedQuestionsActivity.this, iqsets,ip);
		lvListQuestions.setAdapter(iqsetListAdapter);
		
        lvListQuestions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvListQuestions.setItemsCanFocus(false);
        lvListQuestions.setOnItemClickListener(new ItemClickListener());
    }

    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            boolean itemChecked = lvListQuestions.isItemChecked(position);

            if (lvListQuestions.getCheckedItemPositions().size() > 1 && currentPosition != position) {
                lvListQuestions.setItemChecked(currentPosition, false);
            }
            currentPosition = position;

            btOk.setEnabled(itemChecked);
            cbQuestions.setClickable(itemChecked);
            if (!itemChecked || cbQuestions.isChecked()) {
                cbQuestions.setChecked(false);
                spinnerHours.setEnabled(false);
                spinnerMinutes.setEnabled(false);
                spinnerSeconds.setEnabled(false);
            }
            iqset = iqsetListAdapter.getItem(position);
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

        this.finish();
    }

    private class LoadBoardTask extends ProgressDialogAsyncTask<Void, File[]> {
    	
    	private Context context;

        public LoadBoardTask(Activity context) {
            super(context);
            
            this.context = context;
        }

        @Override
        protected File[] doInBackground(Void... params) {

            try {
                return getQuestions();
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }
            return null;
        }

        /* 
         * File[] fileQuestions contains the 4 preloaded local files, but this is not used anymore
         * and it is replaced by the iqsets
         */
        @Override
        protected void onPostExecute(File[] fileQuestions) {
        	
        	boolean iqsetsExist = false;
        	
        	try {
				iqsetsExist = new SmilePlugServerManager().iqsetsExist(ip, context);
			} catch (NetworkErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            if (iqsetsExist) {
                UsePreparedQuestionsActivity.this.loadQuestionsList();

            } else {
            	btOk.setEnabled(false);
                cbQuestions.setClickable(false);
                ActivityUtil.showLongToast(UsePreparedQuestionsActivity.this, "No Results Found");
            }
            super.onPostExecute(fileQuestions);
        }

    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, String> {

        private Context context;

        public LoadTask(Activity context) {
            super(context);

            this.context = context;
        }
        
        @Override
        protected String doInBackground(Void... params) {
            try {
                Collection<Question> newQuestions = null;
                
                String idCheckedIQSet = new SmilePlugServerManager().getIdIQSetByPosition(ip, context, currentPosition);
                
                newQuestions = new SmilePlugServerManager().getListOfQuestions(ip, context, idCheckedIQSet);
                
                new SmilePlugServerManager().startUsingPreparedQuestions(ip, context, newQuestions);

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
                DialogUtil.checkConnection(UsePreparedQuestionsActivity.this, message);
            } else {
                UsePreparedQuestionsActivity.this.openGeneralActivity();
            }
        }
    }
}