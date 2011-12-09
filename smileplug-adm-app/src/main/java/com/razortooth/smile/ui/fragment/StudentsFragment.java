package com.razortooth.smile.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.StudentsManager;
import com.razortooth.smile.domain.StudentStatus;
import com.razortooth.smile.ui.GeneralActivity;
import com.razortooth.smile.ui.StudentsStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentsStatusListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class StudentsFragment extends MainFragment {

    private static final int AUTO_UPDATE_TIME = 5000;

    private static final String PARAM_STATUS = "status";
    private final List<StudentStatus> statusList = new ArrayList<StudentStatus>();

    private ArrayAdapter<StudentStatus> adapter;

    private boolean run;

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            List<StudentStatus> tmp = (List<StudentStatus>) savedInstanceState
                .getSerializable(PARAM_STATUS);
            updateListAndListView(tmp);
        } else {
            new LoadStatusListTask(getActivity()).execute();
        }

        adapter = new StudentsStatusListAdapter(getActivity(), statusList);
        ListView list = (ListView) getActivity().findViewById(R.id.list_students);
        if (adapter != null) {
            list.setAdapter(adapter);
            list.setOnItemClickListener(new OpenItemDetailsListener());
        }

        TextView tv_top_title = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
        tv_top_title.setVisibility(View.GONE);

        LinearLayout ll_top = (LinearLayout) getActivity().findViewById(R.id.top_scorers);
        ll_top.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        run = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        run = false;
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PARAM_STATUS, (Serializable) statusList);
        super.onSaveInstanceState(outState);
    }

    private List<StudentStatus> loadList() {
        StudentsManager statusManager = new StudentsManager();
        return statusManager.getStudentStatusList(getActivity());
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            StudentStatus studentsStatus = statusList.get(position);

            Intent intent = new Intent(getActivity(), StudentsStatusDetailsActivity.class);
            intent.putExtra(StudentsStatusDetailsActivity.PARAM_STUDENTS_STATUS, studentsStatus);
            startActivity(intent);
        }
    }

    private void updateListAndListView(List<StudentStatus> newContent) {

        statusList.clear();
        if (newContent != null) {
            statusList.addAll(newContent);
        }

        if (run && GeneralActivity.pageViewIndex == 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView tv_name = (TextView) getActivity().findViewById(R.id.tl_students);
                    tv_name.setText(getString(R.string.students) + ": " + statusList.size());

                    int x = 0, y = 0;
                    for (StudentStatus element : statusList) {
                        StudentStatus studentStatus = element;
                        x = x + (studentStatus.isMade() ? 1 : 0);
                        y = y + (studentStatus.isSolved() ? 1 : 0);
                    }

                    TextView tv_question = (TextView) getActivity().findViewById(R.id.tl_questions);
                    tv_question.setText(getString(R.string.questions) + ": " + x);

                    TextView tv_answers = (TextView) getActivity().findViewById(R.id.tl_answers);
                    tv_answers.setText(getString(R.string.answers) + ": " + y);
                }
            });
        }

        adapter.notifyDataSetChanged();

        new Handler().postDelayed(new UpdateStudentsRunnable(), AUTO_UPDATE_TIME);

    }

    private class LoadStatusListTask extends ProgressDialogAsyncTask<Void, List<StudentStatus>> {

        public LoadStatusListTask(Activity context) {
            super(context);
        }

        @Override
        protected List<StudentStatus> doInBackground(Void... params) {
            return loadList();
        }

        @Override
        protected void onPostExecute(List<StudentStatus> statusList) {
            if (statusList != null) {
                updateListAndListView(statusList);
            }
            super.onPostExecute(statusList);
        }
    }

    private final class UpdateStudentsRunnable implements Runnable {
        @Override
        public void run() {
            new UpdateStudentsTask().execute();
        }
    }

    private class UpdateStudentsTask extends AsyncTask<Void, Void, List<StudentStatus>> {

        @Override
        protected List<StudentStatus> doInBackground(Void... arg0) {
            return loadList();
        }

        @Override
        protected void onPostExecute(List<StudentStatus> result) {
            updateListAndListView(result);
        }

    }

}
