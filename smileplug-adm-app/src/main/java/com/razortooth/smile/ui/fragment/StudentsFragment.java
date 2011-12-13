package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import com.razortooth.smile.domain.StudentQuestionDetail;
import com.razortooth.smile.ui.StudentStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentListAdapter;

public class StudentsFragment extends AbstractFragment {

    private final List<Student> students = new ArrayList<Student>();

    private ArrayAdapter<Student> adapter;

    private ArrayList<Integer> arrayListTopScorers;
    private ArrayList<Integer> arrayListRatings;

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

        arrayListTopScorers = new ArrayList<Integer>();
        arrayListRatings = new ArrayList<Integer>();
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

                    setTopScorersArea();
                }

            });
        }

        adapter.notifyDataSetChanged();

    }

    private void setTopScorersArea() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (Student element : students) {
            Student student = element;

            arrayListTopScorers.add(new Integer(student.getScore()));

            for (StudentQuestionDetail element2 : student.getDetails()) {
                StudentQuestionDetail detail = element2;

                arrayListRatings.add(new Integer(detail.getChosenRating()));
                map.put(detail.getChosenRating(), detail.getOwner());
            }
        }

        Integer maxScore = Collections.max(arrayListTopScorers);
        Integer maxRating = Collections.max(arrayListRatings);
        String owner = map.get(maxRating);

        TextView tvTopScorersTop = (TextView) getActivity().findViewById(R.id.tv_top_scorers_top);
        tvTopScorersTop.setText(getString(R.string.top_scorer) + ": " + maxScore);

        TextView tvTopScorersRating = (TextView) getActivity().findViewById(
            R.id.tv_top_scorers_rating);
        tvTopScorersRating.setText(getString(R.string.rating) + ": " + new Double(maxRating));

        TextView tvTopScorersCreator = (TextView) getActivity().findViewById(
            R.id.tv_top_scorers_creator);
        if (owner != null) {
            tvTopScorersCreator.setText(getString(R.string.created_by) + ": " + owner);
        }
    }

}
