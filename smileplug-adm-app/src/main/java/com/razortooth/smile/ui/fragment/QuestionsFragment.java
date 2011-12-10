package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.ui.GeneralActivity;
import com.razortooth.smile.ui.adapter.QuestionListAdapter;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class QuestionsFragment extends MainFragment implements OnClickListener {

    private static final int AUTO_UPDATE_TIME = 5000;

    private static final String PARAM_BOARD = "board";

    private final List<Question> questions = new ArrayList<Question>();

    private ArrayAdapter<Question> adapter;
    private Board board;

    private boolean run;

    private Button save;

    @Override
    protected int getLayout() {
        return R.layout.questions;
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

        adapter = new QuestionListAdapter(getActivity(), questions);
        ListView list = (ListView) getActivity().findViewById(R.id.list_questions);
        list.setAdapter(adapter);

        save = (Button) getActivity().findViewById(R.id.bt_save);
        save.setOnClickListener(this);
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

    private void updateListAndListView(Board newBoard) {

        questions.clear();
        board = newBoard;

        if (run && GeneralActivity.pageViewIndex == 1) {

            Collection<Question> newQuestions = newBoard.getQuestions();
            if (newQuestions != null) {
                questions.addAll(newQuestions);
            }

        }

        adapter.notifyDataSetChanged();

        new Handler().postDelayed(new UpdateQuestionsRunnable(), AUTO_UPDATE_TIME);

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

    private final class UpdateQuestionsRunnable implements Runnable {
        @Override
        public void run() {
            new UpdateQuestionsTask().execute();
        }
    }

    private class UpdateQuestionsTask extends AsyncTask<Void, Void, Board> {

        @Override
        protected Board doInBackground(Void... arg0) {

            try {
                if (run && GeneralActivity.pageViewIndex == 1) {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_save) {
            // save questions
        }
    }
}
