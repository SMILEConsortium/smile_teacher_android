package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Student;
import com.razortooth.smile.ui.GeneralActivity;
import com.razortooth.smile.ui.StudentsStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.StudentListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class StudentsFragment extends MainFragment {

    private static final int AUTO_UPDATE_TIME = 5000;

    private static final String PARAM_BOARD = "board";

    private final List<Student> students = new ArrayList<Student>();

    private ArrayAdapter<Student> adapter;
    private Board board;

    private boolean run;

    @Override
    protected int getLayout() {
        return R.layout.students;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Board tmp = (Board) savedInstanceState.getSerializable(PARAM_BOARD);
            updateListAndListView(tmp);
        } else {
            new LoadBoardTask(getActivity()).execute();
        }

        adapter = new StudentListAdapter(getActivity(), students);
        ListView list = (ListView) getActivity().findViewById(R.id.list_students);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OpenItemDetailsListener());

        TextView tv_top_title = (TextView) getActivity().findViewById(R.id.tv_top_scorers);
        tv_top_title.setVisibility(View.GONE);

        LinearLayout ll_top = (LinearLayout) getActivity().findViewById(R.id.top_scorers);
        ll_top.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        run = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        run = false;
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PARAM_BOARD, board);
        super.onSaveInstanceState(outState);
    }

    private Board loadBoard() throws NetworkErrorException, DataAccessException {
        String ip = null; // TODO: IP
        return new BoardManager().getBoard(ip, getActivity());
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Student s = students.get(position);

            Intent intent = new Intent(getActivity(), StudentsStatusDetailsActivity.class);
            intent.putExtra(StudentsStatusDetailsActivity.PARAM_STUDENTS, s);
            startActivity(intent);
        }
    }

    private void updateListAndListView(Board newBoard) {

        students.clear();
        board = newBoard;

        if (run && GeneralActivity.pageViewIndex == 0) {
            Collection<Student> newStudents = newBoard.getStudents();
            if (newStudents != null) {
                students.addAll(newStudents);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView tv_name = (TextView) getActivity().findViewById(R.id.tl_students);
                    tv_name.setText(getString(R.string.students) + ": " + students.size());

                    TextView tv_question = (TextView) getActivity().findViewById(R.id.tl_questions);
                    tv_question.setText(getString(R.string.questions) + ": "
                        + board.getQuestionsNumber());

                    TextView tv_answers = (TextView) getActivity().findViewById(R.id.tl_answers);
                    tv_answers.setText(getString(R.string.answers) + ": "
                        + board.getAnswersNumber());
                }

            });
        }

        adapter.notifyDataSetChanged();

        new Handler().postDelayed(new UpdateStudentsRunnable(), AUTO_UPDATE_TIME);

    }

    private class LoadBoardTask extends ProgressDialogAsyncTask<Void, Board> {

        public LoadBoardTask(Activity context) {
            super(context);
        }

        @Override
        protected Board doInBackground(Void... params) {

            try {
                return loadBoard();
            } catch (NetworkErrorException e) {
                handleException(e);
            } catch (DataAccessException e) {
                handleException(e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Board board) {
            if (board != null) {
                updateListAndListView(board);
            }
            super.onPostExecute(board);
        }
    }

    private final class UpdateStudentsRunnable implements Runnable {
        @Override
        public void run() {
            new UpdateStudentsTask().execute();
        }
    }

    private class UpdateStudentsTask extends AsyncTask<Void, Void, Board> {

        @Override
        protected Board doInBackground(Void... arg0) {

            try {
                if (run && GeneralActivity.pageViewIndex == 0) {
                    return loadBoard();
                }
            } catch (NetworkErrorException e) {

            } catch (DataAccessException e) {}

            return null;
        }

        @Override
        protected void onPostExecute(Board result) {
            updateListAndListView(result);
        }

    }

}
