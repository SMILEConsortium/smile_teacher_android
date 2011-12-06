package com.razortooth.smile.ui;

import android.app.Activity;
import android.os.Bundle;

import com.razortooth.smile.R;

public class StudentsStatusDetailsActivity extends Activity {

    public static String PARAM_STUDENTS_STATUS = "studentsStatus";

    // private StudentStatus studentsStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // studentsStatus = (StudentStatus) getIntent().getSerializableExtra(PARAM_STUDENTS_STATUS);
    }
}
