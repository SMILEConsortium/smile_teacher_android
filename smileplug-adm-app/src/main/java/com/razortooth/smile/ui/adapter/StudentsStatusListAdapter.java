package com.razortooth.smile.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.StudentStatus;

public class StudentsStatusListAdapter extends BaseListAdapter<StudentStatus> {

    private static final int LAYOUT = R.layout.students;
    private static final int[] LAYOUT_ITEMS = { R.id.tv_name, R.id.tv_question, R.id.tv_answers,
        R.id.tv_score };

    public StudentsStatusListAdapter(Activity context, List<StudentStatus> items) {
        super(context, items, LAYOUT, LAYOUT_ITEMS);
    }

    @Override
    protected String getValue(StudentStatus item, int layoutItem) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final StudentStatus studentStatus = getItem(position);

        if (convertView == null) {
            convertView = context.getLayoutInflater()
                .inflate(R.layout.students_item, parent, false);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(studentStatus.getName());

        RadioButton tv_question = (RadioButton) convertView.findViewById(R.id.tv_question);
        tv_question.setChecked(studentStatus.isMade());
        tv_question.setClickable(false);

        RadioButton tv_answers = (RadioButton) convertView.findViewById(R.id.tv_answers);
        tv_answers.setChecked(studentStatus.isSolved());
        tv_answers.setClickable(false);

        TextView tv_score = (TextView) convertView.findViewById(R.id.tv_score);
        tv_score.setText(String.valueOf(studentStatus.getScore()));

        return convertView;

    }

    @Override
    protected boolean hasAsynchronousImageLoading() {
        return false;
    }
}
