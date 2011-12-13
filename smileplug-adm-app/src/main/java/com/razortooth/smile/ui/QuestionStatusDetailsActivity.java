package com.razortooth.smile.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.util.IOUtil;

public class QuestionStatusDetailsActivity extends Activity {

    public static String PARAM_QUESTION = "question";

    private Question question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.question_details);

        question = (Question) getIntent().getSerializableExtra(PARAM_QUESTION);

        TextView tvOwner = (TextView) findViewById(R.id.tv_create_by);
        tvOwner.setText("( " + getString(R.string.create_by) + " " + question.getOwner() + " )");

        ImageView tvImage = (ImageView) findViewById(R.id.iv_image);
        if (question.hasImage()) {
            // Bitmap bmp = IOUtil.loadBitmapFromUrl(question.getImage());
            // Just for tests
            Bitmap bmp = IOUtil
                .loadBitmapFromUrl("http://t1.gstatic.com/images?q=tbn:ANd9GcSL9LKsICD7PDcC74QBfE8lW_asswywLtYHabQTG_FVSxLpZWbE");
            tvImage.setImageBitmap(bmp);
        } else {
            tvImage.setVisibility(View.GONE);
        }

        TextView tvQuestion = (TextView) findViewById(R.id.tv_question);
        if (!question.getQuestion().equals("")) {
            tvQuestion.setText(question.getQuestion());
        } else {
            tvQuestion.setVisibility(View.GONE);
        }

        TextView tvAlternative1 = (TextView) findViewById(R.id.tv_alternative1);
        if (!question.getOption1().equals("")) {
            tvAlternative1.setText(getString(R.string.alternative1) + " " + question.getOption1());
        } else {
            tvAlternative1.setVisibility(View.GONE);
        }

        TextView tvAlternative2 = (TextView) findViewById(R.id.tv_alternative2);
        if (!question.getOption1().equals("")) {
            tvAlternative2.setText(getString(R.string.alternative2) + " " + question.getOption2());
        } else {
            tvAlternative2.setVisibility(View.GONE);
        }

        TextView tvAlternative3 = (TextView) findViewById(R.id.tv_alternative3);
        if (!question.getOption1().equals("")) {
            tvAlternative3.setText(getString(R.string.alternative3) + " " + question.getOption3());
        } else {
            tvAlternative3.setVisibility(View.GONE);
        }

        TextView tvAlternative4 = (TextView) findViewById(R.id.tv_alternative4);
        if (!question.getOption1().equals("")) {
            tvAlternative4.setText(getString(R.string.alternative4) + " " + question.getOption4());
        } else {
            tvAlternative4.setVisibility(View.GONE);
        }

        TextView tvCorrectAnswer = (TextView) findViewById(R.id.tv_correct_answer);
        tvCorrectAnswer.setText(getString(R.string.correct_answer) + ": " + question.getAnswer());
    }
}
