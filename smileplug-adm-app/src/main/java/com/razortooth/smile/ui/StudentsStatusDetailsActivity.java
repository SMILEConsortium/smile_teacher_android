package com.razortooth.smile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.StudentsManager;
import com.razortooth.smile.domain.ScoreBoard;
import com.razortooth.smile.domain.StudentStatus;
import com.razortooth.smile.ui.adapter.ScoreBoardListAdapter;

public class StudentsStatusDetailsActivity extends Activity {

    public static String PARAM_STUDENTS_STATUS = "studentsStatus";

    private StudentStatus studentsStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_details);

        studentsStatus = (StudentStatus) getIntent().getSerializableExtra(PARAM_STUDENTS_STATUS);

        StudentsManager statusManager = new StudentsManager();
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard = statusManager.getScoreBoard(this, studentsStatus.getName());

        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(getString(R.string.scoreboard_of) + " " + scoreBoard.getOwner());

        TextView tv_score = (TextView) findViewById(R.id.tv_score);
        tv_score.setText(getString(R.string.score) + " " + String.valueOf(scoreBoard.getCorrects())
            + "/" + String.valueOf(scoreBoard.getTotal()));

        ScoreBoardListAdapter adapter = new ScoreBoardListAdapter(this, scoreBoard.getItems());
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }
}
