package com.razortooth.smile.ui;

import java.util.List;
import java.util.Vector;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.razortooth.smile.ui.adapter.PagerAdapter;
import com.razortooth.smile.ui.fragment.AbstractFragment;
import com.razortooth.smile.ui.fragment.QuestionsFragment;
import com.razortooth.smile.ui.fragment.StudentsFragment;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class GeneralActivity extends FragmentActivity {

    private static final int AUTO_UPDATE_TIME = 5000;
    private static final String PARAM_BOARD = "board";
    private Board board;

    private Handler boardHandler;
    private Runnable boardRunnable;

    public static final String PARAM_IP = "ip";
    public static final String PARAM_HOURS = "hours";
    public static final String PARAM_MINUTES = "minutes";
    public static final String PARAM_SECONDS = "seconds";

    private String ip;
    private String hours;
    private String minutes;
    private String seconds;

    private Button btSolve;
    private Button btResults;

    private TextView tvTime;
    private TextView tvRemaining;

    private final List<Fragment> fragments = new Vector<Fragment>();
    private AbstractFragment activeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);

        ip = this.getIntent().getStringExtra(PARAM_IP);
        hours = this.getIntent().getStringExtra(PARAM_HOURS);
        minutes = this.getIntent().getStringExtra(PARAM_MINUTES);
        seconds = this.getIntent().getStringExtra(PARAM_SECONDS);

        btSolve = (Button) findViewById(R.id.bt_solve);
        btResults = (Button) findViewById(R.id.bt_results);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvRemaining = (TextView) findViewById(R.id.tv_remaining_time);

        if (savedInstanceState != null) {
            Board tmp = (Board) savedInstanceState.getSerializable(PARAM_BOARD);

            updateCurrentFragment(tmp);

        } else {
            new LoadBoardTask(this).execute();
        }

        boardHandler = new Handler();
        boardRunnable = new UpdateBoardRunnable();

        this.initialisePaging();
    }

    private void updateCurrentFragment(Board newBoard) {
        if (newBoard != null) {
            board = newBoard;
            activeFragment.updateFragment(newBoard);

            boardHandler.postDelayed(boardRunnable, AUTO_UPDATE_TIME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        btSolve.setOnClickListener(new SolveButtonListener());
        btResults.setOnClickListener(new ResultsButtonListener());
        btResults.setEnabled(false);
        tvTime.setText("00:00:00");

        if (hours != null | seconds != null | minutes != null) {
            countDownTimer();
        } else {
            tvTime.setVisibility(View.GONE);
            tvRemaining.setVisibility(View.GONE);
        }
    }

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
            tvTime.setText("Done!");
            btResults.setEnabled(true);
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

        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_about:
                Dialog aboutDialog = new Dialog(this, R.style.Dialog);
                aboutDialog.setContentView(R.layout.about);
                aboutDialog.show();
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

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_awesomepager);
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new FragmentOnPageChangeListener());
    }

    private class SolveButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                new SmilePlugServerManager().startSolvingQuestions(ip, GeneralActivity.this);

                ActivityUtil.showLongToast(GeneralActivity.this, R.string.solving);

                btResults.setEnabled(true);
                btSolve.setEnabled(false);
            } catch (NetworkErrorException e) {
                new NetworkErrorException("Connection errror: " + e.getMessage(), e);
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
            }
        }
    }

    private class ResultsButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                new SmilePlugServerManager().showResults(ip, GeneralActivity.this);

                ActivityUtil.showLongToast(GeneralActivity.this, R.string.showing);

                TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, 150);

                ListView lvListStudents = (ListView) GeneralActivity.this
                    .findViewById(R.id.lv_students);
                lvListStudents.setLayoutParams(layoutParams);
                lvListStudents.setPadding(5, 0, 0, 5);

                TextView tvTopTitle = (TextView) GeneralActivity.this
                    .findViewById(R.id.tv_top_scorers);
                tvTopTitle.setVisibility(View.VISIBLE);

                LinearLayout llTopScorersContainer = (LinearLayout) GeneralActivity.this
                    .findViewById(R.id.ll_top_scorers);
                llTopScorersContainer.setVisibility(View.VISIBLE);

            } catch (NetworkErrorException e) {
                new NetworkErrorException("Connection errror: " + e.getMessage(), e);
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
            }
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
            // nothing
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // nothing
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
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
            } catch (DataAccessException e) {
                Log.e(Constants.LOG_CATEGORY, "Erro: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Board result) {
            updateCurrentFragment(result);
        }

    }

}
