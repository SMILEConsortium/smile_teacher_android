package com.razortooth.smile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.razortooth.smile.R;

public class ChooseActivityFlowDialog extends Activity implements OnClickListener {

    private String ip;

    private Button start;
    private Button use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);

        start = (Button) findViewById(R.id.button1);
        use = (Button) findViewById(R.id.button2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ip = this.getIntent().getStringExtra(GeneralActivity.IP);

        start.setOnClickListener(this);
        use.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GeneralActivity.class);
        switch (v.getId()) {
            case R.id.button1:
                intent.putExtra(GeneralActivity.IP, ip);
                startActivity(intent);
                break;

            case R.id.button2:
                intent = new Intent(this, UsePreparedQuestionsActivity.class);
                intent.putExtra(GeneralActivity.IP, ip);
                startActivity(intent);
                break;
        }
    }
}