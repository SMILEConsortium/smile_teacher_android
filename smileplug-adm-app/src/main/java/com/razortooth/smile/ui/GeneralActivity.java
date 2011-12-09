package com.razortooth.smile.ui;

import java.util.List;
import java.util.Vector;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.ui.adapter.PagerAdapter;
import com.razortooth.smile.ui.fragment.QuestionsFragment;
import com.razortooth.smile.ui.fragment.StudentsFragment;
import com.razortooth.smile.util.ActivityUtil;

public class GeneralActivity extends FragmentActivity implements OnClickListener {

    public static final String IP = "ip";
    public static final String HOURS = "hours";
    public static final String MINUTES = "minutes";
    public static final String SECONDS = "seconds";

    private String ip;
    private String hours;
    private String minutes;
    private String seconds;

    private PagerAdapter mPagerAdapter;

    private Button solve;
    private Button results;

    private TextView time;
    private TextView remaining;

    public static int pageViewIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);

        ip = this.getIntent().getStringExtra(IP);
        hours = this.getIntent().getStringExtra(HOURS);
        minutes = this.getIntent().getStringExtra(MINUTES);
        seconds = this.getIntent().getStringExtra(SECONDS);

        solve = (Button) findViewById(R.id.bt_solve);
        results = (Button) findViewById(R.id.bt_results);
        time = (TextView) findViewById(R.id.tv_time);
        remaining = (TextView) findViewById(R.id.tv_remaining_time);

        this.initialisePaging();
    }

    @Override
    protected void onResume() {
        super.onResume();

        solve.setOnClickListener(this);
        results.setOnClickListener(this);
        results.setEnabled(false);
        time.setText("00:00:00");

        if (hours != null | seconds != null | minutes != null) {
            countDownTimer();
        } else {
            time.setVisibility(View.GONE);
            remaining.setVisibility(View.GONE);
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

        new CountDownTimer(count, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("" + formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                time.setText("Done!");
                results.setEnabled(true);
            }
        }.start();
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

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, StudentsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QuestionsFragment.class.getName()));
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager mPager = (ViewPager) super.findViewById(R.id.awesomepager);
        mPager.setAdapter(this.mPagerAdapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int currentIndex) {
                pageViewIndex = currentIndex;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // nothing
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // nothing
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_solve:
                try {
                    new SmilePlugServerManager().startSolvingQuestions(ip, this);

                    ActivityUtil.showLongToast(this, R.string.solving);

                    results.setEnabled(true);
                    solve.setEnabled(false);
                } catch (NetworkErrorException e) {
                    new NetworkErrorException("Connection errror: " + e.getMessage(), e);
                }
                break;
            case R.id.bt_results:
                try {
                    new SmilePlugServerManager().showResults(ip, this);

                    ActivityUtil.showLongToast(this, R.string.showing);

                    TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, 150);

                    ListView list = (ListView) this.findViewById(R.id.list_students);
                    list.setLayoutParams(layoutParams);
                    list.setPadding(5, 0, 0, 0);

                    TextView tv_top_title = (TextView) this.findViewById(R.id.tv_top_scorers);
                    tv_top_title.setVisibility(View.VISIBLE);

                    LinearLayout ll_top = (LinearLayout) this.findViewById(R.id.top_scorers);
                    ll_top.setVisibility(View.VISIBLE);

                } catch (NetworkErrorException e) {
                    new NetworkErrorException("Connection errror: " + e.getMessage(), e);
                }
                break;
        }
    }
}
