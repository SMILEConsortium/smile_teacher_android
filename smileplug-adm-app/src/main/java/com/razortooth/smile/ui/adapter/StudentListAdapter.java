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
import com.razortooth.smile.domain.Student;

public class StudentListAdapter extends ArrayAdapter<Student> {

    public StudentListAdapter(Context context, List<Student> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Student student = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.students_item, parent, false);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(student.getName());

        CheckBox tv_question = (CheckBox) convertView.findViewById(R.id.tv_question);
        tv_question.setChecked(student.isMade());
        tv_question.setClickable(false);

        CheckBox tv_answers = (CheckBox) convertView.findViewById(R.id.tv_answers);
        tv_answers.setChecked(student.isSolved());
        tv_answers.setClickable(false);

        TextView tv_score = (TextView) convertView.findViewById(R.id.tv_score);
        tv_score.setText(String.valueOf(student.getScore()));

        return convertView;

    }

}
