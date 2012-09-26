package com.razortooth.smile.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.StudentQuestionDetail;

public class StudentQuestionDetailAdapter extends ArrayAdapter<StudentQuestionDetail> {

    public StudentQuestionDetailAdapter(Activity context, List<StudentQuestionDetail> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final StudentQuestionDetail item = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.students_details_item, parent, false);
        }

        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        tvNumber.setText(String.valueOf(item.getNumber()));

        TextView tvCorrect = (TextView) convertView.findViewById(R.id.tv_correct);
        tvCorrect.setText(String.valueOf(item.getAnswer()));

        TextView tvChosen = (TextView) convertView.findViewById(R.id.tv_chosen);
        tvChosen.setText(String.valueOf(item.getChosenAnswer()));

        final RatingBar rbRatingBar = (RatingBar) convertView.findViewById(R.id.rb_ratingbar);
        rbRatingBar.setRating(item.getChosenRating());

        return convertView;
    }
}
