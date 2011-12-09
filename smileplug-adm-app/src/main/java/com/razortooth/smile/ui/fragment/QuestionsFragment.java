package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.QuestionStatus;
import com.razortooth.smile.ui.adapter.QuestionsStatusListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class QuestionsFragment extends MainFragment implements OnClickListener {

    private static final int AUTO_UPDATE_TIME = 5000;

    private static final String PARAM_STATUS = "status";
    private final List<QuestionStatus> statusList = new ArrayList<QuestionStatus>();

    private ArrayAdapter<QuestionStatus> adapter;

    private Button save;

    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            List<QuestionStatus> tmp = (List<QuestionStatus>) savedInstanceState
                .getSerializable(PARAM_STATUS);
            updateListAndListView(tmp);
        } else {
            new LoadStatusListTask(getActivity()).execute();
        }

        adapter = new QuestionsStatusListAdapter(getActivity(), statusList);
        ListView list = (ListView) getActivity().findViewById(R.id.list_questions);
        if (adapter != null) {
            list.setAdapter(adapter);
        }

        save = (Button) getActivity().findViewById(R.id.bt_save);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        save.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().finish();
    }

    private void updateListAndListView(List<QuestionStatus> newContent) {

        statusList.clear();
        if (newContent != null) {
            statusList.addAll(newContent);
        }

        adapter.notifyDataSetChanged();

        new Handler().postDelayed(new UpdateQuestionsRunnable(), AUTO_UPDATE_TIME);

    }

    private List<QuestionStatus> loadList() {
        return null;
    }

    private class LoadStatusListTask extends ProgressDialogAsyncTask<Void, List<QuestionStatus>> {

        public LoadStatusListTask(Activity context) {
            super(context);
        }

        @Override
        protected List<QuestionStatus> doInBackground(Void... params) {
            return loadList();
        }

        @Override
        protected void onPostExecute(List<QuestionStatus> statusList) {
            if (statusList != null) {
                updateListAndListView(statusList);
            }
            super.onPostExecute(statusList);
        }
    }

    private final class UpdateQuestionsRunnable implements Runnable {
        @Override
        public void run() {
            new UpdateQuestionsTask().execute();
        }
    }

    private class UpdateQuestionsTask extends AsyncTask<Void, Void, List<QuestionStatus>> {

        @Override
        protected List<QuestionStatus> doInBackground(Void... arg0) {
            return loadList();
        }

        @Override
        protected void onPostExecute(List<QuestionStatus> result) {
            updateListAndListView(result);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_save) {
            // save questions
        }
    }
}
