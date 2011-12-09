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
import com.razortooth.smile.domain.QuestionStatus;

public class QuestionsStatusListAdapter extends ArrayAdapter<QuestionStatus> {

    public QuestionsStatusListAdapter(Context context, List<QuestionStatus> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final QuestionStatus questionsStatus = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.questions_item, parent, false);
        }

        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        tv_number.setText(String.valueOf(questionsStatus.getNumber()));

        TextView tv_owner = (CheckBox) convertView.findViewById(R.id.tv_owner);
        tv_owner.setText(questionsStatus.getOwner());

        TextView tv_hit_average = (CheckBox) convertView.findViewById(R.id.tv_hit_average);
        tv_hit_average.setText(String.valueOf(questionsStatus.getHitAverage()));

        TextView tv_rating = (TextView) convertView.findViewById(R.id.tv_rating);
        tv_rating.setText(String.valueOf(questionsStatus.getRating()));

        return convertView;
    }
}
