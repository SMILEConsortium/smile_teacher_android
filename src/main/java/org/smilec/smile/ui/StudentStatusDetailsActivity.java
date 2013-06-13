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
package org.smilec.smile.ui;

import java.util.Comparator;
import java.util.List;

import org.smilec.smile.R;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.QuestionList;
import org.smilec.smile.domain.Student;
import org.smilec.smile.domain.StudentQuestionDetail;
import org.smilec.smile.ui.adapter.StudentQuestionDetailAdapter;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.ImageLoader;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StudentStatusDetailsActivity extends MainActivity {

    public static final String PARAM_QUESTIONS = "questions";
    public static final String PARAM_STUDENT = "student";

    private Student student;
    private QuestionList questions;
    private String ip;

    private ListView lvListQuestionDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_details);
        Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
        getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());

        Bundle b = getIntent().getExtras();
        questions = b.getParcelable(PARAM_QUESTIONS);
        ip = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);

        student = (Student) getIntent().getSerializableExtra(PARAM_STUDENT);

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(getString(R.string.scoreboard_of) + " " + student.getName());

        String score = String.valueOf(student.getScore());
        String total = String.valueOf(student.getAnswered());

        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        tvScore.setText(getString(R.string.score) + " " + score + "/" + total);

        List<StudentQuestionDetail> studentQuestionDetails = student.getDetails();

        StudentQuestionDetailAdapter adapter = new StudentQuestionDetailAdapter(this,
            studentQuestionDetails);

        adapter.sort(new Comparator<StudentQuestionDetail>() {
            @Override
            public int compare(StudentQuestionDetail arg0, StudentQuestionDetail arg1) {
                return (arg0.getNumber() - arg1.getNumber());
            }
        });

        lvListQuestionDetails = (ListView) findViewById(R.id.list);
        lvListQuestionDetails.setAdapter(adapter);
        lvListQuestionDetails.setOnItemClickListener(new OpenItemDetailsListener());
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        private static final int ALTERNATIVE_1 = 1;
        private static final int ALTERNATIVE_2 = 2;
        private static final int ALTERNATIVE_3 = 3;
        private static final int ALTERNATIVE_4 = 4;

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Dialog detailsDialog = new Dialog(StudentStatusDetailsActivity.this, R.style.Dialog);
            detailsDialog.setContentView(R.layout.student_question_details);
            Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
            detailsDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
            detailsDialog.show();

            StudentQuestionDetail studentQuestionDetail = (StudentQuestionDetail) lvListQuestionDetails
                .getItemAtPosition(position);
            if (!questions.isEmpty()) {
                for (Question question : questions) {
                    if (question.getNumber() == studentQuestionDetail.getNumber()) {
                        loadDetails(detailsDialog, question,
                            studentQuestionDetail.getChosenAnswer(),
                            studentQuestionDetail.getChosenRating());
                    }
                }
            }
        }

        private void loadDetails(Dialog detailsDialog, Question question, int chosenAnswer,
            int chosenRating) {

            ImageView ivAlt1 = (ImageView) detailsDialog.findViewById(R.id.iv_alternative1);
            ImageView ivAlt2 = (ImageView) detailsDialog.findViewById(R.id.iv_alternative2);
            ImageView ivAlt3 = (ImageView) detailsDialog.findViewById(R.id.iv_alternative3);
            ImageView ivAlt4 = (ImageView) detailsDialog.findViewById(R.id.iv_alternative4);

            switch (chosenAnswer) {
                case ALTERNATIVE_1:
                    ivAlt1.setVisibility(View.VISIBLE);
                    break;
                case ALTERNATIVE_2:
                    ivAlt2.setVisibility(View.VISIBLE);
                    break;
                case ALTERNATIVE_3:
                    ivAlt3.setVisibility(View.VISIBLE);
                    break;
                case ALTERNATIVE_4:
                    ivAlt4.setVisibility(View.VISIBLE);
                    break;
            }

            TextView tvQuestionNumber = (TextView) detailsDialog
                .findViewById(R.id.tv_question_number);
            tvQuestionNumber.setText("Question No." + question.getNumber());

            TextView tvOwner = (TextView) detailsDialog.findViewById(R.id.tv_create_by);
            tvOwner.setText("( " + StudentStatusDetailsActivity.this.getString(R.string.create_by)
                + " " + question.getOwner() + " )");

            ImageView tvImage = (ImageView) detailsDialog.findViewById(R.id.iv_image);

            Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
            float percentWidth = (float) (displaySize.getWidth() * 0.6);
            float percentHeight = (float) (displaySize.getHeight() * 0.6);
            int width = (int) (displaySize.getWidth() - percentWidth);
            int height = (int) (displaySize.getHeight() - percentHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,
                height);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_question);
            layoutParams.setMargins(35, 20, 50, 0);
            tvImage.setLayoutParams(layoutParams);

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
                tvQuestion.setText("Question: " + question.getQuestion());
            } else {
                tvQuestion.setVisibility(View.GONE);
            }

            TextView tvAlternative1 = (TextView) detailsDialog.findViewById(R.id.tv_alternative1);
            if (!question.getOption1().equals("")) {
                tvAlternative1.setText(StudentStatusDetailsActivity.this
                    .getString(R.string.alternative1) + " " + question.getOption1());
            } else {
                tvAlternative1.setVisibility(View.GONE);
            }

            TextView tvAlternative2 = (TextView) detailsDialog.findViewById(R.id.tv_alternative2);
            if (!question.getOption1().equals("")) {
                tvAlternative2.setText(StudentStatusDetailsActivity.this
                    .getString(R.string.alternative2) + " " + question.getOption2());
            } else {
                tvAlternative2.setVisibility(View.GONE);
            }

            TextView tvAlternative3 = (TextView) detailsDialog.findViewById(R.id.tv_alternative3);
            if (!question.getOption1().equals("")) {
                tvAlternative3.setText(StudentStatusDetailsActivity.this
                    .getString(R.string.alternative3) + " " + question.getOption3());
            } else {
                tvAlternative3.setVisibility(View.GONE);
            }

            TextView tvAlternative4 = (TextView) detailsDialog.findViewById(R.id.tv_alternative4);
            if (!question.getOption1().equals("")) {
                tvAlternative4.setText(StudentStatusDetailsActivity.this
                    .getString(R.string.alternative4) + " " + question.getOption4());
            } else {
                tvAlternative4.setVisibility(View.GONE);
            }

            TextView tvCorrectAnswer = (TextView) detailsDialog
                .findViewById(R.id.tv_correct_answer);
            tvCorrectAnswer.setText(StudentStatusDetailsActivity.this
                .getString(R.string.correct_answer) + ": " + question.getAnswer());

            TextView tvAverageRating = (TextView) detailsDialog
                .findViewById(R.id.tv_average_rating);
            tvAverageRating.setText("Average rating: " + chosenRating);

            final RatingBar rbRatingBar = (RatingBar) detailsDialog.findViewById(R.id.rb_ratingbar);
            rbRatingBar.setRating(chosenRating);
        }
    }
}
