package com.razortooth.smile.ui.fragment;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.StudentsManager;
import com.razortooth.smile.domain.StudentStatus;
import com.razortooth.smile.ui.adapter.StudentsStatusListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class StudentsFragment extends MainFragment {

    private static final String PARAM_STATUS = "status";
    private List<StudentStatus> statusList;

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            statusList = (List<StudentStatus>) savedInstanceState.getSerializable(PARAM_STATUS);
        } else {
            statusList = null;
            new LoadStatusListTask(getActivity()).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PARAM_STATUS, (Serializable) statusList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void LoadList() {
        StudentsStatusListAdapter adapter;
        adapter = new StudentsStatusListAdapter(getActivity(), statusList);

        ListView list = (ListView) getActivity().findViewById(R.id.list_students);
        list.setAdapter(adapter);
        // list.setOnItemClickListener(new OpenItemDetailsListener());
    }

    // private class OpenItemDetailsListener implements OnItemClickListener {
    //
    // @Override
    // public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
    // StudentStatus studentsStatus = statusList.get(position);
    //
    // Intent intent = new Intent(getActivity(), StudentsStatusDetailsActivity.class);
    // intent.putExtra(StudentsStatusDetailsActivity.PARAM_STUDENTS_STATUS,
    // (Serializable) studentsStatus);
    // startActivity(intent);
    // }
    // }

    private class LoadStatusListTask extends ProgressDialogAsyncTask<Void, List<StudentStatus>> {

        private Context context;

        public LoadStatusListTask(Activity context) {
            super(context);

            this.context = context;
        }

        @Override
        protected List<StudentStatus> doInBackground(Void... params) {
            List<StudentStatus> studentsList = null;

            StudentsManager statusManager = new StudentsManager();
            studentsList = statusManager.getStudentStatusList(context);

            return studentsList;
        }

        @Override
        protected void onPostExecute(List<StudentStatus> statusList) {
            if (statusList != null) {
                StudentsFragment.this.statusList = statusList;
                StudentsFragment.this.LoadList();
            }
            super.onPostExecute(statusList);
        }
    }
}
