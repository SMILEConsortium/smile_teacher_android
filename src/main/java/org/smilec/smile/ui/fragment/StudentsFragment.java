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
package org.smilec.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.smilec.smile.R;
import org.smilec.smile.bu.BoardManager;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.domain.Board;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.QuestionList;
import org.smilec.smile.domain.Results;
import org.smilec.smile.domain.Student;
import org.smilec.smile.ui.GeneralActivity;
import org.smilec.smile.ui.StudentStatusDetailsActivity;
import org.smilec.smile.ui.adapter.StudentListAdapter;
import org.smilec.smile.util.ActivityUtil;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StudentsFragment extends AbstractFragment {

    private final List<Student> students = new ArrayList<Student>();
    private List<Question> questions = new ArrayList<Question>();

    private ArrayAdapter<Student> adapter;

    private Results results;

    private boolean run;

    private String ip;

    private int countAnswers;

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        TextView tvTopTitle = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
        tvTopTitle.setTextColor(Color.WHITE);

        RelativeLayout rlTopScorersConatainer = (RelativeLayout) getActivity().findViewById(
            R.id.rl_top_scorers);
        rlTopScorersConatainer.setVisibility(View.INVISIBLE);
        
        Button btSendResults = (Button) getActivity().findViewById(R.id.bt_send_results);
		btSendResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        ip = getActivity().getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) getActivity().getIntent().getSerializableExtra(
            GeneralActivity.PARAM_RESULTS);

        adapter = new StudentListAdapter(getActivity(), students);

        ListView lvListStudents = (ListView) getActivity().findViewById(R.id.lv_students);
        lvListStudents.setPadding(5, 0, 0, 0);
        lvListStudents.setAdapter(adapter);
        lvListStudents.setOnItemClickListener(new OpenItemDetailsListener());

        run = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        run = false;
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Student student = students.get(position);

            if (countAnswers > 0) {
                QuestionList questionList = new QuestionList();
                questionList.addAll(questions);

                Bundle b = new Bundle();
                b.putParcelable(StudentStatusDetailsActivity.PARAM_QUESTIONS, questionList);

                Intent intent = new Intent(getActivity(), StudentStatusDetailsActivity.class);
                intent.putExtra(StudentStatusDetailsActivity.PARAM_STUDENT, student);
                intent.putExtras(b);
                intent.putExtra(GeneralActivity.PARAM_IP, ip);
                startActivity(intent);
            } else {
                ActivityUtil.showLongToast(getActivity(),
                    "This student has not answered a question.");
            }
        }
    }

    @Override
    public void updateFragment(final Board board) {

        students.clear();
        questions.clear();

        if (run) {
            Collection<Student> newStudents = board.getStudents();
            if (newStudents != null) {
                students.addAll(newStudents);
            }

            if (countAnswers > 0 || board.getAnswersNumber() > 0) {
                Collection<Question> newQuestions = board.getQuestions();
                if (newQuestions != null) {
                    questions.addAll(newQuestions);
                }
            }

            new UpdateResultsTask(getActivity()).execute();

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TextView tvName = (TextView) getActivity().findViewById(R.id.tv_total_students);
                    tvName.setText(getString(R.string.students) + ": " + students.size());

                    TextView tvQuestion = (TextView) getActivity().findViewById(
                        R.id.tv_total_questions);
                    tvQuestion.setText(getString(R.string.questions) + ": "
                        + board.getQuestionsNumber());

                    TextView tvAnswers = (TextView) getActivity().findViewById(
                        R.id.tv_total_answers);
                    countAnswers = board.getAnswersNumber();
                    tvAnswers.setText(getString(R.string.answers) + ": " + countAnswers);

                    if (results != null) {
                        setTopScorersArea(results, board);
                    }
                }

            });
        }

        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student arg0, Student arg1) {
                return (arg1.getScore() - arg0.getScore());
            }
        });

        adapter.notifyDataSetChanged();

    }

    private class UpdateResultsTask extends AsyncTask<Void, Void, Results> {

        private Context context;

        private UpdateResultsTask(Context context) {
            this.context = context;
        }

        @Override
        protected Results doInBackground(Void... arg0) {
            Results retrieveResults = null;
            try {
                retrieveResults = new BoardManager().retrieveResults(ip, context);
                return retrieveResults;
            } catch (NetworkErrorException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }

            return retrieveResults;
        }

        @Override
        protected void onPostExecute(Results results) {
            if (results != null) {
                StudentsFragment.this.results = results;
            }
        }

    }

    private void setTopScorersArea(Results results, Board board) {
        try {
            TextView tvTopScorersTop = (TextView) getActivity().findViewById(
                R.id.tv_top_scorers_top);

            String sBestScoredStudentNames = results.getBestScoredStudentNames();
            JSONArray bestScoredStudentNames = new JSONArray(sBestScoredStudentNames == null ? ""
                : sBestScoredStudentNames);
            tvTopScorersTop.setText(getString(R.string.top_scorer) + ": "
                + bestScoredStudentNames.join(", ").replaceAll("\"", ""));

            TextView tvTopScorersRating = (TextView) getActivity().findViewById(
                R.id.tv_top_scorers_rating);
            tvTopScorersRating.setText(getString(R.string.rating) + ": "
                + results.getWinnerRating());

            final RatingBar rbRatingBar = (RatingBar) getActivity().findViewById(R.id.rb_ratingbar);
            rbRatingBar.setRating(results.getWinnerRating());
        } catch (JSONException e) {
            Log.e("StudentsFragment", "Error: " + e);
        }
    }
}
