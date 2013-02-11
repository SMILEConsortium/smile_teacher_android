package com.razortooth.smile.ui.fragment;

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

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.QuestionsManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.ui.GeneralActivity;
import com.razortooth.smile.ui.adapter.QuestionListAdapter;
import com.razortooth.smile.util.ActivityUtil;

public class QuestionsFragment extends AbstractFragment {

    private final List<Question> mQuestions = new ArrayList<Question>();
    private List<Question> listQuestionsSelected = new ArrayList<Question>();

    private ArrayAdapter<Question> adapter;

    private Button btSave;

    private ListView lvListQuestions;
    private TextView tvServer;

    private String ip;
    private Results results;

    private boolean mRun;
    private boolean loadItems;

	private Object mQuestionsMutex = new Object();
	
    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btSave = (Button) getActivity().findViewById(R.id.bt_save);
		btSave.setEnabled(false);
        lvListQuestions = (ListView) getActivity().findViewById(R.id.lv_questions);
        tvServer = (TextView) getActivity().findViewById(R.id.tv_server);
    }

    @Override
    public void onResume() {
        super.onResume();

        btSave.setEnabled(false);
        btSave.setOnClickListener(new SaveButtonListener());

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
				
				Collections.sort(mQuestions, new Comparator<Question>() {
					@Override
					public int compare(Question arg0, Question arg1) {
						return (int) (arg1.getPerCorrect() - arg0.getPerCorrect());
					}
				});
			}
		}
		adapter.notifyDataSetChanged();
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
            private Dialog aboutDialog; // XXX Why is this called aboutDialog???

            public SaveFileDialogListener(Dialog aboutDialog) {
                this.aboutDialog = aboutDialog;
            }

            @Override
            public void onClick(View v) {
                TextView name = (TextView) aboutDialog.findViewById(R.id.et_name_file);

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

                aboutDialog.dismiss();
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
                new QuestionsManager().saveQuestions(context, name.trim(), listQuestions, ip);

                return true;
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
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
