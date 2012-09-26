package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.QuestionList;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.domain.Student;
import com.razortooth.smile.ui.GeneralActivity;
import com.razortooth.smile.ui.StudentStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentListAdapter;
import com.razortooth.smile.util.ActivityUtil;

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
        tvTopTitle.setVisibility(View.GONE);

        LinearLayout llTopScorersConatainer = (LinearLayout) getActivity().findViewById(
            R.id.ll_top_scorers);
        llTopScorersConatainer.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        ip = getActivity().getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        results = (Results) getActivity().getIntent().getSerializableExtra(
            GeneralActivity.PARAM_RESULTS);

        adapter = new StudentListAdapter(getActivity(), students);
        ListView lvListStudents = (ListView) getActivity().findViewById(R.id.lv_students);
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

            if (countAnswers > 0) {
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

        adapter.notifyDataSetChanged();

    }

    private class UpdateResultsTask extends AsyncTask<Void, Void, Results> {

        private Context context;

        private UpdateResultsTask(Context context) {
            this.context = context;
        }

        @Override
        protected Results doInBackground(Void... arg0) {

            try {
                Results retrieveResults = new BoardManager().retrieveResults(ip, context);
                return retrieveResults;
            } catch (NetworkErrorException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }

            return null;
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
