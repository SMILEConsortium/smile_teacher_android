package com.razortooth.smile.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Question;

public class QuestionListAdapter extends ArrayAdapter<Question> {

    public QuestionListAdapter(Context context, List<Question> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Question question = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.questions_item, parent, false);
        }

        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        tvNumber.setText(String.valueOf(question.getNumber()));

        TextView tvOwner = (TextView) convertView.findViewById(R.id.tv_owner);
        tvOwner.setText(question.getOwner());

        TextView tvHitAverage = (TextView) convertView.findViewById(R.id.tv_hit_average);
        tvHitAverage.setText(String.valueOf(question.getHitAverage()));

        TextView tvRating = (TextView) convertView.findViewById(R.id.tv_rating);
        tvRating.setText(String.valueOf(question.getRating()));

        return convertView;
    }
}
