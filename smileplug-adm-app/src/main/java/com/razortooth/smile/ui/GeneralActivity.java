package com.razortooth.smile.ui;

import java.util.List;
import java.util.Vector;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.CurrentMessageStatus;
import com.razortooth.smile.ui.adapter.PagerAdapter;
import com.razortooth.smile.ui.fragment.AbstractFragment;
import com.razortooth.smile.ui.fragment.QuestionsFragment;
import com.razortooth.smile.ui.fragment.StudentsFragment;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class GeneralActivity extends FragmentActivity {

    private static final String GAME_STATUS = "GAME STATUS\n";
    private static final String PARAM_BOARD = "board";

    private static final int AUTO_UPDATE_TIME = 5000;

    private Handler boardHandler;
    private Runnable boardRunnable;

    public static final String PARAM_IP = "ip";
    public static final String PARAM_HOURS = "hours";
    public static final String PARAM_MINUTES = "minutes";
    public static final String PARAM_SECONDS = "seconds";
    public static final String PARAM_RESULTS = "results";
    public static final String PARAM_STATUS = "status";

    private String ip;
    private String hours, minutes, seconds;
    private String status;

    private Button btSolve, btResults;

    private TextView tvTime, tvRemaining;
    private TextView btnStudents, btnQuestions;
    private TextView tvStatus;

    private final List<Fragment> fragments = new Vector<Fragment>();

    private Board board;

    private AbstractFragment activeFragment;

    private ViewPager viewPagerFragments;

    private boolean solve;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);

        ip = this.getIntent().getStringExtra(PARAM_IP);
        hours = this.getIntent().getStringExtra(PARAM_HOURS);
        minutes = this.getIntent().getStringExtra(PARAM_MINUTES);
        seconds = this.getIntent().getStringExtra(PARAM_SECONDS);
        status = this.getIntent().getStringExtra(PARAM_STATUS);

        btSolve = (Button) findViewById(R.id.bt_solve);
        btResults = (Button) findViewById(R.id.bt_results);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvRemaining = (TextView) findViewById(R.id.tv_remaining_time);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        if (savedInstanceState != null) {
            Board tmp = (Board) savedInstanceState.getSerializable(PARAM_BOARD);

            updateCurrentFragment(tmp);

        } else {
            new LoadBoardTask(this).execute();
        }

        boardHandler = new Handler();
        boardRunnable = new UpdateBoardRunnable();

        this.initialisePaging();

        tvTime.setText("00:00:00");

        if (hours != null | seconds != null | minutes != null) {
			// Log.d(Constants.LOG_CATEGORY, "hours = " + hours + " seconds = " + seconds + " minutes = " + minutes);
			if ((!hours.equals("0")) || (!minutes.equals("0")) || (!seconds.equals("0"))) {
            	countDownTimer();
			}
        } else {
            tvTime.setVisibility(View.GONE);
            tvRemaining.setVisibility(View.GONE);
        }

        btnStudents = (TextView) findViewById(R.id.tv_students);
        btnStudents.setOnClickListener(btnFragmentOnClickListener);

        btnQuestions = (TextView) findViewById(R.id.tv_questions);
        btnQuestions.setOnClickListener(btnFragmentOnClickListener);

        btResults.setEnabled(false);
        solve = true;

        if (status != null) {
            if (status.equals("") || !status.equals(CurrentMessageStatus.START_MAKE.name())) {
                status = CurrentMessageStatus.START_MAKE.name();
            }
            tvStatus.setText(GAME_STATUS + CurrentMessageStatus.valueOf(status).getStatus());
        } else {
            new LoadStatusTask(this).execute();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("solve", solve);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        solve = savedInstanceState.getBoolean("solve");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builderBack = new AlertDialog.Builder(GeneralActivity.this);
                builderBack
                    .setMessage(
                        "Are you sure you want to exit? This operation will reset the game and exit.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                new SmilePlugServerManager().resetGame(ip, GeneralActivity.this);
                            } catch (NetworkErrorException e) {
                                Log.e(Constants.LOG_CATEGORY, "Error: ", e);
                            }
                            GeneralActivity.this.finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                AlertDialog alertBack = builderBack.create();
                alertBack.show();

                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void updateCurrentFragment(Board newBoard) {
        if (newBoard != null) {
            board = newBoard;
            activeFragment.updateFragment(newBoard);

            boardHandler.postDelayed(boardRunnable, AUTO_UPDATE_TIME);

            solve = !newBoard.getQuestions().isEmpty();

            if (!solve || btResults.isEnabled()) {
                btSolve.setEnabled(false);
            } else {
                btSolve.setEnabled(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        btSolve.setOnClickListener(new SolveButtonListener());
        btResults.setOnClickListener(new ResultsButtonListener());

        btSolve.setEnabled(solve);
        if (btResults.isEnabled()) {
            btSolve.setEnabled(false);
        }
    }

    Button.OnClickListener btnFragmentOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (v.equals(btnStudents)) {
                viewPagerFragments.setCurrentItem(0);
            } else {
                viewPagerFragments.setCurrentItem(1);
            }
        }
    };

    public String formatTime(long millis) {
        String output = "00:00:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10) {
            secondsD = "0" + seconds;
        }
        if (minutes < 10) {
            minutesD = "0" + minutes;
        }
        if (hours < 10) {
            hoursD = "0" + hours;
        }

        output = hoursD + " : " + minutesD + " : " + secondsD;
        return output;
    }

    private void countDownTimer() {
		Log.d(Constants.LOG_CATEGORY, "Init countDownTimer()");
        long hr = (Integer.parseInt(hours) * 3600) * 1000;
        long min = (Integer.parseInt(minutes) * 60) * 1000;
        long sec = Integer.parseInt(seconds) * 1000;
        long count = hr + min + sec;

        new countDownTimerListener(count, 1000).start();
    }

    private class countDownTimerListener extends CountDownTimer {

        public countDownTimerListener(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tvTime.setText(R.string.session_time_expired);
			btResults.setText(R.string.hide_results);
            new LoadToResultsTask(GeneralActivity.this).execute();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvTime.setText("" + formatTime(millisUntilFinished));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        boardHandler.removeCallbacks(boardRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_restart:
                AlertDialog.Builder builderRestart = new AlertDialog.Builder(this);
                builderRestart.setMessage(R.string.restart_game).setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                new SmilePlugServerManager().resetGame(ip, GeneralActivity.this);
                                GeneralActivity.this.finish();
                            } catch (NetworkErrorException e) {
                                Log.e(Constants.LOG_CATEGORY, "Error: ", e);
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                AlertDialog alertRestart = builderRestart.create();
                alertRestart.show();
                break;
            case R.id.bt_about:
                Dialog aboutDialog = new Dialog(this, R.style.Dialog);
                aboutDialog.setContentView(R.layout.about);
                Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
                aboutDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
                aboutDialog.show();
                break;
            case R.id.bt_exit:
                AlertDialog.Builder builderExit = new AlertDialog.Builder(this);
                builderExit.setMessage(R.string.exit).setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            try {
                                new SmilePlugServerManager().resetGame(ip, GeneralActivity.this);
                            } catch (NetworkErrorException e) {
                                Log.e(Constants.LOG_CATEGORY, "Error: ", e);
                            }

                            GeneralActivity.this.finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                AlertDialog alertExit = builderExit.create();
                alertExit.show();
                break;
        }
        return true;
    }

    private void initialisePaging() {

        fragments.clear();
        fragments.add(Fragment.instantiate(this, StudentsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QuestionsFragment.class.getName()));

        activeFragment = (AbstractFragment) fragments.get(0);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        viewPagerFragments = (ViewPager) findViewById(R.id.vp_awesomepager);
        viewPagerFragments.setAdapter(pagerAdapter);
        viewPagerFragments.setCurrentItem(0);
        viewPagerFragments.setOnPageChangeListener(new FragmentOnPageChangeListener());
    }

    private class SolveButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            new LoadToSolvingTask(GeneralActivity.this).execute();
        }
    }

    private class ResultsButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (btResults.getText().equals(getString(R.string.show_results))) {
                btResults.setText(R.string.hide_results);

                new LoadToResultsTask(GeneralActivity.this).execute();
            } else {
                btResults.setText(R.string.show_results);

                TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, 275);

                ListView lvListStudents = (ListView) GeneralActivity.this
                    .findViewById(R.id.lv_students);

                lvListStudents.setPadding(5, 0, 0, 0);

                TextView tvTopTitle = (TextView) GeneralActivity.this
                    .findViewById(R.id.tv_top_scorers);
                tvTopTitle.setVisibility(View.INVISIBLE);

                LinearLayout llTopScorersContainer = (LinearLayout) GeneralActivity.this
                    .findViewById(R.id.ll_top_scorers);
                llTopScorersContainer.setVisibility(View.INVISIBLE);

                TableLayout tlTotal = (TableLayout) findViewById(R.id.tl_total);
                tlTotal.setVisibility(View.VISIBLE);

                TextView tvTotal = (TextView) findViewById(R.id.tv_total);
                tvTotal.setVisibility(View.VISIBLE);

                View vTotal = findViewById(R.id.view_separator_total);
                vTotal.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startSolvingQuestion() {
        ActivityUtil.showLongToast(GeneralActivity.this, R.string.solving);

        btResults.setEnabled(true);

        solve = false;
        btSolve.setEnabled(solve);
    }

    private void showResults() {
        TableLayout tlTotal = (TableLayout) findViewById(R.id.tl_total);
        tlTotal.setVisibility(View.GONE);

        TextView tvTotal = (TextView) findViewById(R.id.tv_total);
        tvTotal.setVisibility(View.GONE);

        View vTotal = findViewById(R.id.view_separator_total);
        vTotal.setVisibility(View.GONE);

        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, 315);

        ListView lvListStudents = (ListView) GeneralActivity.this.findViewById(R.id.lv_students);
        lvListStudents.setLayoutParams(layoutParams);
        lvListStudents.setPadding(5, 0, 0, 0);

        TextView tvTopTitle = (TextView) GeneralActivity.this.findViewById(R.id.tv_top_scorers);
        tvTopTitle.setTextColor(Color.BLACK);
        tvTopTitle.setVisibility(View.VISIBLE);

        LinearLayout llTopScorersContainer = (LinearLayout) GeneralActivity.this
            .findViewById(R.id.ll_top_scorers);
        llTopScorersContainer.setVisibility(View.VISIBLE);
    }

    private class LoadStatusTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadStatusTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return new SmilePlugServerManager().currentMessageGame(ip, context);
            } catch (NetworkErrorException e) {
                handleException(e);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            tvStatus.setText(GAME_STATUS
                + CurrentMessageStatus.valueOf(
                    status.equals("") ? CurrentMessageStatus.START_SHOW.name() : status)
                    .getStatus());
        }
    }

    private class LoadToSolvingTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadToSolvingTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                new SmilePlugServerManager().startSolvingQuestions(ip, context);

                return new SmilePlugServerManager().currentMessageGame(ip, context);
            } catch (NetworkErrorException e) {
                handleException(e);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            tvStatus.setText(GAME_STATUS
                + CurrentMessageStatus.valueOf(
                    status.equals("") ? CurrentMessageStatus.START_SOLVE.name() : status)
                    .getStatus());
            GeneralActivity.this.startSolvingQuestion();
        }
    }

    private class LoadToResultsTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadToResultsTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                new SmilePlugServerManager().showResults(ip, context);

                return new SmilePlugServerManager().currentMessageGame(ip, context);
            } catch (NetworkErrorException e) {
                handleException(e);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            tvStatus.setText(GAME_STATUS
                + CurrentMessageStatus.valueOf(
                    status.equals("") ? CurrentMessageStatus.START_SHOW.name() : status)
                    .getStatus());

            GeneralActivity.this.showResults();

        }
    }

    private final class FragmentOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int currentIndex) {
            activeFragment = (AbstractFragment) fragments.get(currentIndex);
            updateCurrentFragment(board);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Empty
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Empty
        }
    }

    private Board loadBoard() throws NetworkErrorException, DataAccessException {
        return new BoardManager().getBoard(ip, this);
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
                updateCurrentFragment(board);
            }
            super.onPostExecute(board);
        }

    }

    private final class UpdateBoardRunnable implements Runnable {

        @Override
        public void run() {
            new UpdateBoardTask().execute();
        }

    }

    private class UpdateBoardTask extends AsyncTask<Void, Void, Board> {

        @Override
        protected Board doInBackground(Void... arg0) {

            try {
                return loadBoard();
            } catch (NetworkErrorException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Board result) {
            updateCurrentFragment(result);
        }

    }

}
