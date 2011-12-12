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

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new StudentListAdapter(getActivity(), students);
        ListView list = (ListView) getActivity().findViewById(R.id.list_students);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OpenItemDetailsListener());

        TextView tv_top_title = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
        tv_top_title.setVisibility(View.GONE);

        LinearLayout ll_top = (LinearLayout) getActivity().findViewById(R.id.top_scorers);
        ll_top.setVisibility(View.GONE);

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

        Collection<Student> newStudents = board.getStudents();
        if (newStudents != null) {
            students.addAll(newStudents);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView tv_name = (TextView) getActivity().findViewById(R.id.tl_students);
                tv_name.setText(getString(R.string.students) + ": " + students.size());

                TextView tv_question = (TextView) getActivity().findViewById(R.id.tl_questions);
                tv_question.setText(getString(R.string.questions) + ": "
                    + board.getQuestionsNumber());

                TextView tv_answers = (TextView) getActivity().findViewById(R.id.tl_answers);
                tv_answers.setText(getString(R.string.answers) + ": " + board.getAnswersNumber());
            }

        });

        adapter.notifyDataSetChanged();

    }

}
