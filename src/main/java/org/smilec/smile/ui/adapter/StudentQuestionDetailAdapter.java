/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
package org.smile.smilec.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import org.smile.smilec.R;
import org.smile.smilec.domain.StudentQuestionDetail;

public class StudentQuestionDetailAdapter extends ArrayAdapter<StudentQuestionDetail> {

    public StudentQuestionDetailAdapter(Activity context, List<StudentQuestionDetail> items) {
        super(context, 0, items);
    }

    @SuppressLint("ResourceAsColor")
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

        setCorrectAnswer(convertView, item);

        return convertView;

    }

    private void setCorrectAnswer(View convertView, final StudentQuestionDetail item) {
        if (item.correct()) {
            convertView.setBackgroundColor(Color.GREEN);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        convertView.refreshDrawableState();
    }

}
