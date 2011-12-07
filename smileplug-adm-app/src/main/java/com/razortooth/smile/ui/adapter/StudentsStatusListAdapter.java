package com.razortooth.smile.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.StudentStatus;

public class StudentsStatusListAdapter extends ArrayAdapter<StudentStatus> {

    public StudentsStatusListAdapter(Context context, List<StudentStatus> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final StudentStatus studentStatus = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.students_item, parent, false);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(studentStatus.getName());

        CheckBox tv_question = (CheckBox) convertView.findViewById(R.id.tv_question);
        tv_question.setChecked(studentStatus.isMade());
        tv_question.setClickable(false);

        CheckBox tv_answers = (CheckBox) convertView.findViewById(R.id.tv_answers);
        tv_answers.setChecked(studentStatus.isSolved());
        tv_answers.setClickable(false);

        TextView tv_score = (TextView) convertView.findViewById(R.id.tv_score);
        tv_score.setText(String.valueOf(studentStatus.getScore()));

        return convertView;

    }

}
