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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;

import org.smile.smilec.R;
import org.smile.smilec.bu.Constants;
import org.smile.smilec.domain.Question;
import org.smile.smilec.domain.Results;
import org.smile.smilec.util.ActivityUtil;
import org.smile.smilec.util.ImageLoader;
import org.smile.smilec.ui.widget.checkbox.InertCheckBox;

public class QuestionListAdapter extends ArrayAdapter<Question> {
    private Context context;
    private String ip;

    public QuestionListAdapter(Context context, List<Question> items, Results results, String ip) {
        super(context, android.R.layout.simple_list_item_multiple_choice, items);

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

		InertCheckBox cbox = (org.smile.smilec.ui.widget.checkbox.InertCheckBox) convertView.findViewById(R.id.cb_item_checkbox);
		cbox.setChecked(true);
		
        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        tvNumber.setText(String.valueOf(question.getNumber()));

        TextView tvOwner = (TextView) convertView.findViewById(R.id.tv_owner);
        tvOwner.setText(question.getOwner());

        TextView tvIp = (TextView) convertView.findViewById(R.id.tv_ip);
        tvIp.setText(question.getIp());

        TextView tvHitAverage = (TextView) convertView.findViewById(R.id.tv_hit_average);

        tvHitAverage.setText(String.valueOf(question.getPerCorrect()));

        final float rating = (float) question.getRating();

        Button ivDetails = (Button) convertView.findViewById(R.id.iv_details);
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
            Display displaySize = ActivityUtil.getDisplaySize(getContext());
            detailsDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
            detailsDialog.show();

            loadDetails(detailsDialog, questions);
        }
    }

    private void loadDetails(Dialog detailsDialog, Question question) {
        TextView tvOwner = (TextView) detailsDialog.findViewById(R.id.tv_create_by);
        tvOwner.setText("( " + context.getString(R.string.create_by) + " " + question.getOwner()
            + " )");

        ImageView tvImage = (ImageView) detailsDialog.findViewById(R.id.iv_image);

        Display displaySize = ActivityUtil.getDisplaySize(context);
        float percentWidth = (float) (displaySize.getWidth() * 0.6);
        float percentHeight = (float) (displaySize.getHeight() * 0.6);
        int width = (int) (displaySize.getWidth() - percentWidth);
        int height = (int) (displaySize.getHeight() - percentHeight);
        LayoutParams lp = new LayoutParams(width, height);
        lp.setMargins(35, 20, 50, 0);
        tvImage.setLayoutParams(lp);

        if (question.hasImage()) {
            byte[] data = ImageLoader.loadBitmap(Constants.HTTP + ip + question.getImageUrl());

            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                if (bitmap != null) {
                    tvImage.setImageBitmap(bitmap);
                }
            }

        } else {
            tvImage.setVisibility(View.GONE);
        }

        TextView tvQuestion = (TextView) detailsDialog.findViewById(R.id.tv_question);
        if (!question.getQuestion().equals("")) {
            tvQuestion.setText(question.getQuestion());
        } else {
            tvQuestion.setVisibility(View.GONE);
        }

        TextView tvAlternative1 = (TextView) detailsDialog.findViewById(R.id.tv_alternative1);
        if (!question.getOption1().equals("")) {
            tvAlternative1.setText(context.getString(R.string.alternative1) + " "
                + question.getOption1());
        } else {
            tvAlternative1.setVisibility(View.GONE);
        }

        TextView tvAlternative2 = (TextView) detailsDialog.findViewById(R.id.tv_alternative2);
        if (!question.getOption1().equals("")) {
            tvAlternative2.setText(context.getString(R.string.alternative2) + " "
                + question.getOption2());
        } else {
            tvAlternative2.setVisibility(View.GONE);
        }

        TextView tvAlternative3 = (TextView) detailsDialog.findViewById(R.id.tv_alternative3);
        if (!question.getOption1().equals("")) {
            tvAlternative3.setText(context.getString(R.string.alternative3) + " "
                + question.getOption3());
        } else {
            tvAlternative3.setVisibility(View.GONE);
        }

        TextView tvAlternative4 = (TextView) detailsDialog.findViewById(R.id.tv_alternative4);
        if (!question.getOption1().equals("")) {
            tvAlternative4.setText(context.getString(R.string.alternative4) + " "
                + question.getOption4());
        } else {
            tvAlternative4.setVisibility(View.GONE);
        }

        TextView tvCorrectAnswer = (TextView) detailsDialog.findViewById(R.id.tv_correct_answer);
        tvCorrectAnswer.setText(context.getString(R.string.correct_answer) + ": "
            + question.getAnswer());
    }

}
