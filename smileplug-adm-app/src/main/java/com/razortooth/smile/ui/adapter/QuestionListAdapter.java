package com.razortooth.smile.ui.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.ui.QuestionStatusDetailsActivity;

public class QuestionListAdapter extends ArrayAdapter<Question> {
    private Results results;
    private Context context;

    public QuestionListAdapter(Context context, List<Question> items, Results results) {
        super(context, android.R.layout.simple_list_item_multiple_choice, items);

        this.results = results;
        this.context = context;
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

        try {
            TextView tvHitAverage = (TextView) convertView.findViewById(R.id.tv_hit_average);
            String sQuestionsCorrectPercentage = results == null ? "[0]" : results
                .getQuestionsCorrectPercentage();
            JSONArray questionsCorrectPercentage = new JSONArray(sQuestionsCorrectPercentage);
            tvHitAverage.setText(String.valueOf(questionsCorrectPercentage.length() <= position ? 0
                : questionsCorrectPercentage.get(position)));
        } catch (JSONException e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        }

        TextView tvRating = (TextView) convertView.findViewById(R.id.tv_rating);
        tvRating.setText(String.valueOf(question.getRating()));

        ImageView ivDetails = (ImageView) convertView.findViewById(R.id.iv_details);
        ivDetails.setOnClickListener(new OpenItemDetailsListener(question));

        return convertView;
    }

    @Override
    public Question getItem(int position) {
        return super.getItem(position);
    }

    private class OpenItemDetailsListener implements OnClickListener {

        private Question questions;

        public OpenItemDetailsListener(Question question) {
            this.questions = question;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, QuestionStatusDetailsActivity.class);
            intent.putExtra(QuestionStatusDetailsActivity.PARAM_QUESTION, questions);
            context.startActivity(intent);
        }
    }
}
