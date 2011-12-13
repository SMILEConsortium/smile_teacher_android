package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Student;
import com.razortooth.smile.ui.StudentStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentListAdapter;

public class StudentsFragment extends AbstractFragment {

    private final List<Student> students = new ArrayList<Student>();

    private ArrayAdapter<Student> adapter;

    private boolean run;

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new StudentListAdapter(getActivity(), students);
        ListView lvListStudents = (ListView) getActivity().findViewById(R.id.lv_students);
        lvListStudents.setAdapter(adapter);
        lvListStudents.setOnItemClickListener(new OpenItemDetailsListener());

        TextView tvTopTitle = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
        tvTopTitle.setVisibility(View.GONE);

        LinearLayout llTopScorersConatainer = (LinearLayout) getActivity().findViewById(
            R.id.ll_top_scorers);
        llTopScorersConatainer.setVisibility(View.GONE);

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
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Student student = students.get(position);

            Intent intent = new Intent(getActivity(), StudentStatusDetailsActivity.class);
            intent.putExtra(StudentStatusDetailsActivity.PARAM_STUDENT, student);
            startActivity(intent);
        }
    }

    @Override
    public void updateFragment(final Board board) {

        students.clear();

        if (run) {
            Collection<Student> newStudents = board.getStudents();
            if (newStudents != null) {
                students.addAll(newStudents);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView tvName = (TextView) getActivity().findViewById(R.id.tv_total_students);
                    tvName.setText(getString(R.string.students) + ": " + students.size());

                    TextView tvQuestion = (TextView) getActivity().findViewById(
                        R.id.tv_total_questions);
                    tvQuestion.setText(getString(R.string.questions) + ": "
                        + board.getQuestionsNumber());

                    TextView tvAnswers = (TextView) getActivity().findViewById(
                        R.id.tv_total_answers);
                    tvAnswers.setText(getString(R.string.answers) + ": " + board.getAnswersNumber());
                }

            });
        }

        adapter.notifyDataSetChanged();

    }

}
