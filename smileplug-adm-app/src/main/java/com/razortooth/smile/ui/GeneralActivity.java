package com.razortooth.smile.ui;

import java.util.List;
import java.util.Vector;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.razortooth.smile.R;
import com.razortooth.smile.ui.adapter.PagerAdapter;
import com.razortooth.smile.ui.fragment.QuestionsFragment;
import com.razortooth.smile.ui.fragment.StudentsFragment;

public class GeneralActivity extends FragmentActivity implements OnClickListener {

    public static final String IP = "ip";
    public static final String HOURS = "hours";
    public static final String MINUTES = "minutes";
    public static final String SECONDS = "seconds";

    // private String ip;
    // private String hours;
    // private String minutes;
    // private String seconds;

    private PagerAdapter mPagerAdapter;

    private Button solve;
    private Button results;

    // private TextView time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);

        // ip = this.getIntent().getStringExtra(IP);
        // hours = this.getIntent().getStringExtra(HOURS);
        // minutes = this.getIntent().getStringExtra(MINUTES);
        // seconds = this.getIntent().getStringExtra(SECONDS);

        solve = (Button) findViewById(R.id.bt_solve);
        results = (Button) findViewById(R.id.bt_results);
        // time = (TextView) findViewById(R.id.tv_time);

        this.initialisePaging();
    }

    @Override
    protected void onResume() {
        super.onResume();

        solve.setOnClickListener(this);
        results.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_solve:
                break;
            case R.id.bt_results:
                break;
        }
    }
}
