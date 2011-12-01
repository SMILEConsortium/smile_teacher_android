package com.razortooth.smile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.razortooth.smile.R;

public class UsePreparedQuestionsActivity extends Activity implements OnClickListener {

    private Button ok;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.use_prepared_questions_dialog);

        ip = this.getIntent().getStringExtra(GeneralActivity.IP);

        ok = (Button) findViewById(R.id.bt_ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GeneralActivity.class);
        intent.putExtra(GeneralActivity.IP, ip);
        startActivity(intent);
    }
}