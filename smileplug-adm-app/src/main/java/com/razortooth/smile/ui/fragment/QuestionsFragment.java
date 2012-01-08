package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    private final List<Question> questions = new ArrayList<Question>();
    private List<Question> listQuestionsSelected = new ArrayList<Question>();

    private ArrayAdapter<Question> adapter;

    private Button btSave;

    private ListView lvListQuestions;
    private TextView tvServer;

    private String ip;
    private Results results;

    private boolean run;
    private boolean loadItems;

    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btSave = (Button) getActivity().findViewById(R.id.bt_save);
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

        tvServer.setText(ip);

        adapter = new QuestionListAdapter(getActivity(), questions, results, ip);

        lvListQuestions.setAdapter(adapter);
        lvListQuestions.setOnItemClickListener(new CheckedItemListener());
        lvListQuestions.setItemsCanFocus(false);
        lvListQuestions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        run = true;
        loadItems = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        run = false;
    }

    private void loadSelections() {
        for (int i = 0; i < questions.size(); i++) {
            if (!lvListQuestions.isItemChecked(i)) {
                lvListQuestions.setItemChecked(i, true);
                listQuestionsSelected.add(questions.get(i));
            }
        }

        if (!questions.isEmpty() && !listQuestionsSelected.isEmpty()) {
            btSave.setEnabled(true);
        } else {
            btSave.setEnabled(false);
        }
    }

    private class CheckedItemListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

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

        questions.clear();

        if (run) {
            Collection<Question> newQuestions = null;
            newQuestions = board.getQuestions();

            if (newQuestions != null) {
                questions.addAll(newQuestions);
            }

            new UpdateResultsTask(getActivity()).execute();

            if (loadItems) {
                listQuestionsSelected.clear();

                loadSelections();
                loadItems = false;
            }
        }

        adapter.notifyDataSetChanged();

    }

    private class SaveButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Dialog saveDialog = new Dialog(QuestionsFragment.this.getActivity(), R.style.Dialog);
            saveDialog.setContentView(R.layout.save);
            saveDialog.show();

            Button save = (Button) saveDialog.findViewById(R.id.bt_save_file);
            save.setOnClickListener(new SaveFileDialogListener(saveDialog));
        }

        public class SaveFileDialogListener implements OnClickListener {
            private Dialog aboutDialog;

            public SaveFileDialogListener(Dialog aboutDialog) {
                this.aboutDialog = aboutDialog;
            }

            @Override
            public void onClick(View v) {
                TextView name = (TextView) aboutDialog.findViewById(R.id.et_name_file);

                if (name.getText().toString().equals("")) {
                    name.setText("Questions_file");
                }

                new SaveTask(getActivity(), listQuestionsSelected, ip, name.getText().toString())
                    .execute();

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
                ActivityUtil
                    .showLongToast(QuestionsFragment.this.getActivity(), R.string.not_saved);
            }
        }

    }
}
