package com.razortooth.smile.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.ScoreBoardItem;

public class ScoreBoardListAdapter extends ArrayAdapter<ScoreBoardItem> {

    public ScoreBoardListAdapter(Activity context, List<ScoreBoardItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ScoreBoardItem scoreBoardItem = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.students_details_item, parent, false);
        }

        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        tv_number.setText(String.valueOf(scoreBoardItem.getQuestionNumber()));

        TextView tv_correct = (TextView) convertView.findViewById(R.id.tv_correct);
        tv_correct.setText(String.valueOf(scoreBoardItem.getCorrectAnswer()));

        TextView tv_chosen = (TextView) convertView.findViewById(R.id.tv_chosen);
        tv_chosen.setText(String.valueOf(scoreBoardItem.getChosenAnswer()));

        TextView tv_rating = (TextView) convertView.findViewById(R.id.tv_rating);
        tv_rating.setText(String.valueOf(scoreBoardItem.getGivenRating()));

        return convertView;
    }
}
