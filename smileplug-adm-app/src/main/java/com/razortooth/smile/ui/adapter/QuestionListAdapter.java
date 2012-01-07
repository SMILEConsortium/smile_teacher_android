package com.razortooth.smile.ui.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.util.ImageLoader;

public class QuestionListAdapter extends ArrayAdapter<Question> {
    private Results results;
    private Context context;
    private String ip;

    public QuestionListAdapter(Context context, List<Question> items, Results results, String ip) {
        super(context, android.R.layout.simple_list_item_multiple_choice, items);

        this.results = results;
        this.context = context;
        this.ip = ip;
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

        TextView tvIp = (TextView) convertView.findViewById(R.id.tv_ip);
        tvIp.setText(question.getIp());

        try {
            TextView tvHitAverage = (TextView) convertView.findViewById(R.id.tv_hit_average);
            String sQuestionsCorrectPercentage = results == null ? "[0]" : results
                .getQuestionsCorrectPercentage();
            JSONArray questionsCorrectPercentage = new JSONArray(sQuestionsCorrectPercentage);

            NumberFormat numberFormat = new DecimalFormat("####0.00");
            double amount = new Double(
                String.valueOf(questionsCorrectPercentage.length() <= position ? 0
                    : questionsCorrectPercentage.get(position)));

            numberFormat.format(amount);

            tvHitAverage.setText(String.valueOf(amount));
        } catch (JSONException e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        }

        final float rating = (float) question.getRating();

        ImageView ivDetails = (ImageView) convertView.findViewById(R.id.iv_details);
        ivDetails.setOnClickListener(new OpenItemDetailsListener(question));

        final RatingBar rbRatingBar = (RatingBar) convertView.findViewById(R.id.rb_ratingbar);
        rbRatingBar.setRating(rating);

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
            Dialog detailsDialog = new Dialog(context, R.style.Dialog);
            detailsDialog.setContentView(R.layout.question_details);
            detailsDialog.show();

            loadDetails(detailsDialog, questions);
        }
    }

    private void loadDetails(Dialog detailsDialog, Question questions) {
        TextView tvOwner = (TextView) detailsDialog.findViewById(R.id.tv_create_by);
        tvOwner.setText("( " + context.getString(R.string.create_by) + " " + questions.getOwner()
            + " )");

        ImageView tvImage = (ImageView) detailsDialog.findViewById(R.id.iv_image);
        if (questions.hasImage()) {
            Bitmap bmp = ImageLoader.loadBitmap(Constants.HTTP + ip + questions.getImageUrl());
            if (bmp != null) {
                tvImage.setImageBitmap(bmp);
            }

        } else {
            tvImage.setVisibility(View.GONE);
        }

        TextView tvQuestion = (TextView) detailsDialog.findViewById(R.id.tv_question);
        if (!questions.getQuestion().equals("")) {
            tvQuestion.setText(questions.getQuestion());
        } else {
            tvQuestion.setVisibility(View.GONE);
        }

        TextView tvAlternative1 = (TextView) detailsDialog.findViewById(R.id.tv_alternative1);
        if (!questions.getOption1().equals("")) {
            tvAlternative1.setText(context.getString(R.string.alternative1) + " "
                + questions.getOption1());
        } else {
            tvAlternative1.setVisibility(View.GONE);
        }

        TextView tvAlternative2 = (TextView) detailsDialog.findViewById(R.id.tv_alternative2);
        if (!questions.getOption1().equals("")) {
            tvAlternative2.setText(context.getString(R.string.alternative2) + " "
                + questions.getOption2());
        } else {
            tvAlternative2.setVisibility(View.GONE);
        }

        TextView tvAlternative3 = (TextView) detailsDialog.findViewById(R.id.tv_alternative3);
        if (!questions.getOption1().equals("")) {
            tvAlternative3.setText(context.getString(R.string.alternative3) + " "
                + questions.getOption3());
        } else {
            tvAlternative3.setVisibility(View.GONE);
        }

        TextView tvAlternative4 = (TextView) detailsDialog.findViewById(R.id.tv_alternative4);
        if (!questions.getOption1().equals("")) {
            tvAlternative4.setText(context.getString(R.string.alternative4) + " "
                + questions.getOption4());
        } else {
            tvAlternative4.setVisibility(View.GONE);
        }

        TextView tvCorrectAnswer = (TextView) detailsDialog.findViewById(R.id.tv_correct_answer);
        tvCorrectAnswer.setText(context.getString(R.string.correct_answer) + ": "
            + questions.getAnswer());
    }
}
