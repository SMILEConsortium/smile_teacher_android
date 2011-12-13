package com.razortooth.smile.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Student;
import com.razortooth.smile.domain.StudentQuestionDetail;
import com.razortooth.smile.ui.adapter.StudentQuestionDetailAdapter;

public class StudentStatusDetailsActivity extends Activity {

    public static String PARAM_STUDENT = "student";

    private Student student;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_details);

        student = (Student) getIntent().getSerializableExtra(PARAM_STUDENT);

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(getString(R.string.scoreboard_of) + " " + student.getName());

        String score = String.valueOf(student.getScore());
        String total = String.valueOf(student.getAnswered());

        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        tvScore.setText(getString(R.string.score) + " " + score + "/" + total);

        List<StudentQuestionDetail> studentQuestionDetails = student.getDetails();
        StudentQuestionDetailAdapter adapter = new StudentQuestionDetailAdapter(this, studentQuestionDetails);
        ListView lvListQuestionDetails = (ListView) findViewById(R.id.list);
        lvListQuestionDetails.setAdapter(adapter);
    }
}
