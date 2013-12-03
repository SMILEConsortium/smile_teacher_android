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
package org.smilec.smile.ui.fragment;

import java.io.ObjectInputStream.GetField;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.smilec.smile.R;
import org.smilec.smile.bu.BoardManager;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.QuestionsManager;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.bu.json.CurrentMessageJSONParser;
import org.smilec.smile.domain.Board;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Results;
import org.smilec.smile.ui.GeneralActivity;
import org.smilec.smile.ui.adapter.QuestionListAdapter;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.CloseClickListenerUtil;
import org.smilec.smile.util.SendEmailAsyncTask;

import android.R.bool;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsFragment extends AbstractFragment {

    private final List<Question> mQuestions = new ArrayList<Question>();
    private List<Question> listQuestionsSelected = new ArrayList<Question>();

    private ArrayAdapter<Question> adapter;

    private Button btSave;
    private Button btWriteReport;

    private ListView lvListQuestions;
    private TextView tvServer;

    private String ip;
    private Results results;

    private boolean mRun;
    private boolean loadItems;

	private Object mQuestionsMutex = new Object();
	private TextView tvTopTitle;

    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btWriteReport = (Button) getActivity().findViewById(R.id.bt_write_report);
        btWriteReport.setEnabled(true);
        btSave = (Button) getActivity().findViewById(R.id.bt_save);
        btSave.setEnabled(false);
		lvListQuestions = (ListView) getActivity().findViewById(R.id.lv_questions);
        tvServer = (TextView) getActivity().findViewById(R.id.tv_server);
        tvTopTitle = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
    }

    @Override
    public void onResume() {
        super.onResume();

        btSave.setEnabled(false);
        btSave.setOnClickListener(new SaveButtonListener());
        btWriteReport.setOnClickListener(new SendReportButtonListener());

        ip = getActivity().getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) getActivity().getIntent().getSerializableExtra(
            GeneralActivity.PARAM_RESULTS);

        tvServer.setText("CONNECTED TO\n" + ip);

        listQuestions();

        mRun = true;
        loadItems = true;
    }

    private void setPercCorrect() {
        int position = 0;

        for (position = 0; position < mQuestions.size(); position++) {
            try {
                String sQuestionsCorrectPercentage = results == null ? "[0]" : results
                    .getQuestionsCorrectPercentage();
                JSONArray questionsCorrectPercentage;
                questionsCorrectPercentage = new JSONArray(sQuestionsCorrectPercentage);

                NumberFormat numberFormat = new DecimalFormat("####0.00");
                double amount = new Double(
                    String.valueOf(questionsCorrectPercentage.length() <= position ? 0
                        : questionsCorrectPercentage.get(position)));

                numberFormat.format(amount);

                mQuestions.get(position).setPerCorrect(amount);

            } catch (JSONException e) {
            	new SendEmailAsyncTask(e.getMessage(),JSONException.class.getName(),QuestionsFragment.class.getName()).execute();
                Log.e(Constants.LOG_CATEGORY, "Error: ", e);
            }
        }

    }

    private boolean checkQuestionChanges(List<Question> questionsOld, List<Question> questionsNew) {

        if (questionsNew.size() != questionsOld.size()) {
            return true;
        }

        for (int i = 0; i < questionsOld.size(); i++) {
            for (int j = 0; j < questionsNew.size(); j++) {
                if (questionsOld.get(i).getNumber() == questionsNew.get(j).getNumber()) {
                    if (questionsOld.get(i).getPerCorrect() != questionsNew.get(j).getPerCorrect()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void listQuestions() {
        adapter = new QuestionListAdapter(getActivity(), mQuestions, results, ip);

        adapter.sort(new Comparator<Question>() {
            @Override
            public int compare(Question arg0, Question arg1) {
                return (int) (arg1.getPerCorrect() - arg0.getPerCorrect());
            }
        });

        lvListQuestions.setAdapter(adapter);
        listQuestionsSelected.clear();
        lvListQuestions.setOnItemClickListener(new CheckedItemListener());
        lvListQuestions.setItemsCanFocus(false);
        lvListQuestions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onStop() {
        super.onStop();

        mRun = false;
    }

    private void loadSelections() {
		Log.d(Constants.LOG_CATEGORY, "loadSelections()");

        if (!mQuestions.isEmpty() && !listQuestionsSelected.isEmpty()) {
            btSave.setEnabled(true);
        } else {
            btSave.setEnabled(false);
        }
    }

    private class CheckedItemListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Log.d(Constants.LOG_CATEGORY, "onItemClicked()");
            if (lvListQuestions.isItemChecked(position)) {
                Question question = adapter.getItem(position);
                listQuestionsSelected.add(question);
            } else {
                Question question = adapter.getItem(position);
                for (Iterator<Question> iterator = listQuestionsSelected.iterator(); iterator
                    .hasNext();) {
                    Question item = iterator.next();
                    if (item.getNumber() == question.getNumber()) {
                        iterator.remove();
                    }
                }
            }
            
            if (!listQuestionsSelected.isEmpty()) {
                btSave.setEnabled(true);
            } else {
                btSave.setEnabled(false);
            }
        }
    }

    @Override
    public void updateFragment(final Board board) {
        List<Question> questionsOld = new ArrayList<Question>();

		//
		// XXX TODO: Debug this code, there is a bug lurking here
		//
		synchronized(mQuestionsMutex) {
			questionsOld.addAll(mQuestions);
			mQuestions.clear();

			if (mRun) {
				Collection<Question> newQuestions = null;
				newQuestions = board.getQuestions();

				if (newQuestions != null) {
					mQuestions.addAll(newQuestions);
				}

				new UpdateResultsTask(getActivity()).execute();

				if (loadItems) {
					listQuestionsSelected.clear();

					loadSelections();
					loadItems = false;
				}

				setPercCorrect();

				if (checkQuestionChanges(questionsOld, mQuestions)) {
					listQuestions();
				}

				SortQuestionList();
			}
		}
		adapter.notifyDataSetChanged();
	}

	private void SortQuestionList() {
		Collections.sort(mQuestions, new Comparator<Question>() {
			@Override
			public int compare(Question arg0, Question arg1) {
				if (tvTopTitle.getVisibility() == View.VISIBLE) {
					return Double.compare(arg1.getRating(), arg0.getRating());
				}
				return (int) (arg1.getPerCorrect() - arg0.getPerCorrect());
			}
		});
	}

    private class SaveButtonListener implements OnClickListener, TextWatcher {
	  	TextView _fname = null;
		Button _saveButton = null;

		@Override
		public void onTextChanged (CharSequence s, int start, int before, int count) {
			Log.d(Constants.LOG_CATEGORY, "SaveButtonListener.onTextChanged");
			if (count > 0) {
				if (_saveButton != null) {
					_saveButton.setEnabled(true);
				}
			} else {
				_saveButton.setEnabled(false);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.d(Constants.LOG_CATEGORY, "SaveButtonListener.afterTextChanged");
		}

		@Override
		public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			Log.d(Constants.LOG_CATEGORY, "SaveButtonListener.beforeTextChanged");
		}

        @Override
        public void onClick(View v) {
			Log.d(Constants.LOG_CATEGORY, "SaveButtonListener.onClick");
            FragmentActivity activity = QuestionsFragment.this.getActivity();

            Dialog saveDialog = new Dialog(activity, R.style.Dialog);
            saveDialog.setContentView(R.layout.save);
            Display displaySize = ActivityUtil.getDisplaySize(activity);
            saveDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
            saveDialog.show();

            Button save = (Button) saveDialog.findViewById(R.id.bt_save_file);

			TextView fname = (TextView) saveDialog.findViewById(R.id.et_name_file);
			fname.addTextChangedListener(this);
            save.setOnClickListener(new SaveFileDialogListener(saveDialog));
			_fname = fname;
			_saveButton = save;
        }

        public class SaveFileDialogListener implements OnClickListener {
            private Dialog saveFileDialog;

            public SaveFileDialogListener(Dialog aboutDialog) {
                this.saveFileDialog = aboutDialog;
            }

            @Override
            public void onClick(View v) {
                TextView name = (TextView) saveFileDialog.findViewById(R.id.et_name_file);

				//
				// We need to have a name set before we enable the button
				//
                if (name.getText().toString().equals("")) {
                    name.setText("Questions_file");
                }

				//
				// Throw up check to see if the name exists, if the file exists, bomb out and
				// leave up a tost notification
				//
                new SaveTask(getActivity(), listQuestionsSelected, ip, name.getText().toString()).execute();

                saveFileDialog.dismiss();
            }
        }
    }

    private class SendReportButtonListener implements OnClickListener, TextWatcher {
	  	
    	//TextView _message = null;
		Button _sendToAdminBtn = null;
		final StringBuilder body = new StringBuilder();
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		
        @Override
        public void onClick(View v) {
        	
			Log.d(Constants.LOG_CATEGORY, "SendReportButtonListener.onClick");
            FragmentActivity activity = QuestionsFragment.this.getActivity();

            Dialog feedbackDialog = new Dialog(activity, R.style.Dialog);
            feedbackDialog.setContentView(R.layout.feedback);
            Display displaySize = ActivityUtil.getDisplaySize(activity);
            feedbackDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
            feedbackDialog.show();

            Button sendToAdmin = (Button) feedbackDialog.findViewById(R.id.bt_send_report);
			TextView message = (TextView) feedbackDialog.findViewById(R.id.et_message);
			final ImageButton btClose = (ImageButton) feedbackDialog.findViewById(R.id.bt_close);
			
			message.addTextChangedListener(this);
			sendToAdmin.setOnClickListener(new SendReportListener(feedbackDialog));
			
            //_message = message;
			_sendToAdminBtn = sendToAdmin;
			
			// Closing the popup if the user click on the cross
			btClose.setOnClickListener(new CloseClickListenerUtil(feedbackDialog));
			
			feedbackDialog.show();
        }
		
		@Override
		public void onTextChanged (CharSequence s, int start, int before, int count) {
			Log.d(Constants.LOG_CATEGORY, "SendReportButtonListener.onTextChanged");
			if (count > 0) {
				if (_sendToAdminBtn != null) {
					_sendToAdminBtn.setEnabled(true);
				}
			} else {
				_sendToAdminBtn.setEnabled(false);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.d(Constants.LOG_CATEGORY, "SendReportButtonListener.afterTextChanged");
		}

		@Override
		public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			Log.d(Constants.LOG_CATEGORY, "SendReportButtonListener.beforeTextChanged");
		}
		
		public class SendReportListener implements OnClickListener {
            private Dialog messageDialog;

            public SendReportListener(Dialog aboutDialog) {
                this.messageDialog = aboutDialog;
            }

            @Override
            public void onClick(View v) {
                TextView message = (TextView) messageDialog.findViewById(R.id.et_message);
    			
    			// Getting all the values from node server
    			String data = GeneralActivity.getContents("http://"+ip+"/smile/all");
    			data = data.replace("{", "\n{");
                
                body.append("Dear Administrator,\n\n\n");
    			body.append(message.getText().toString()+"\n\n");
    			body.append("   A SMILE teacher\n\n\n\n\n");
    			body.append("----- JSON data -----\n\n");
    			body.append(data+"\n");
                
				// We need to have a feedback set before sending it
                if (message.getText().toString().equals("")) {
                	message.setText("ERROR: The message is empty. The \"Send\" button should be desactivated!");
                }

                // We redirect to mail app with these settings
                emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "reply+i-17909211-432eb6ae1acca189ce0ff1dc90b206bf0a62ae4d-64202@reply.github.com" });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Re: [smile_teacher_android] Add ability for Teacher App to send a stacktrace on fatal exception via email (#25)");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
				
				getActivity().startActivity(emailIntent);
                messageDialog.dismiss();
            }
        }
    }
    
    private class UpdateResultsTask extends AsyncTask<Void, Void, Results> {

        private Context context;

        private UpdateResultsTask(Context context) {
            this.context = context;
        }

        @Override
        protected Results doInBackground(Void... arg0) {
            Results retrieveResults = null;
            try {
                retrieveResults = new BoardManager().retrieveResults(ip, context);
                return retrieveResults;
            } catch (NetworkErrorException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }

            return retrieveResults;
        }

        @Override
        protected void onPostExecute(Results results) {
            if (results != null) {
                QuestionsFragment.this.results = results;
            }
        }

    }

    private class SaveTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private List<Question> listQuestions;
        private String ip;
        private String name;

        private SaveTask(Activity context, List<Question> listQuestions, String ip, String name) {

            this.context = context;
            this.listQuestions = listQuestions;
            this.ip = ip;
            this.name = name;
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            try {
            	// We wrap the set of questions in an IQSet
                new SmilePlugServerManager().saveQuestionsAsAnIQSet(ip, name.trim(), context, listQuestions);

                return true;
            } catch (NetworkErrorException e) {
				e.printStackTrace();
			}

            return false;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            if (ok) {
                ActivityUtil.showLongToast(QuestionsFragment.this.getActivity(), R.string.saved);
            } else {
				ActivityUtil.showLongToast(QuestionsFragment.this.getActivity(), R.string.not_saved_because_file_name);
            }
        }

    }
}
