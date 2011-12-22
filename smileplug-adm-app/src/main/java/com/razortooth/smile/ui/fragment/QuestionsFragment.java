package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.accounts.NetworkErrorException;
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
    }

    @Override
    public void onResume() {
        super.onResume();

        btSave.setEnabled(true);
        btSave.setOnClickListener(new SaveButtonListener());

        ip = getActivity().getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) getActivity().getIntent().getSerializableExtra(
            GeneralActivity.PARAM_RESULTS);
        Log.i("test", results == null ? "null" : results.toString());

        adapter = new QuestionListAdapter(getActivity(), questions, results);

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
                try {
                    if (name.getText().toString().equals("")) {
                        name.setText("Questions_file");
                    }
                    new QuestionsManager().saveQuestions(name.getText().toString(),
                        listQuestionsSelected);
                    aboutDialog.dismiss();
                } catch (DataAccessException e) {
                    Log.e(Constants.LOG_CATEGORY, "Error: ", e);
                }
                ActivityUtil.showLongToast(QuestionsFragment.this.getActivity(), R.string.saved);
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
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
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
}
