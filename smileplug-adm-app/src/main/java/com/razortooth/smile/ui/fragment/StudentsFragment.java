package com.razortooth.smile.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.StudentsManager;
import com.razortooth.smile.domain.StudentStatus;
import com.razortooth.smile.ui.StudentsStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentsStatusListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class StudentsFragment extends MainFragment {

    private static final int SLEEP_TIME = 2000;

    private static final String PARAM_STATUS = "status";
    private final List<StudentStatus> statusList = new ArrayList<StudentStatus>();

    private UpdateListHandler handler;
    private ArrayAdapter<StudentStatus> adapter;

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
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OpenItemDetailsListener());

        handler = new UpdateListHandler();
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

        adapter.notifyDataSetChanged();

        handler.sleep(SLEEP_TIME);
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

    private class UpdateListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            List<StudentStatus> tmp = loadList();
            updateListAndListView(tmp);
        }

        public void sleep(long delayMillis) {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }

    }
}
