package com.razortooth.smile.ui;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;

public class LoginActivity extends Activity implements OnClickListener {

    private EditText ip;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ip = (EditText) findViewById(R.id.et_server_ip);
        connect = (Button) findViewById(R.id.bt_connect);
    }

    @Override
    protected void onResume() {
        super.onResume();

        validateIPAddress();

        connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            new SmilePlugServerManager().connect(ip.toString(), this);
            Intent intent = new Intent(this, GeneralActivity.class);
            intent.putExtra(GeneralActivity.IP, ip.toString());
            startActivity(intent);
            ActivityUtil.showLongToast(this, "Connection successfully established");
        } catch (NetworkErrorException e) {
            DialogUtil.checkConnection(this);
        }
    }

    private void validateIPAddress() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                        + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt
                        .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        String lastSplit = splits[splits.length - 1];
                        if (Integer.valueOf(lastSplit) > 255) {
                            return "";
                        } else {
                            if (lastSplit.length() == 3) {
                                if (splits.length == 4) {
                                    return null;
                                }
                                return source.subSequence(start, end) + ".";
                            } else if (lastSplit.length() == 2) {
                                String s = lastSplit + "0";
                                if (Integer.valueOf(s) > 255) {
                                    return source.subSequence(start, end)
                                        + (splits.length == 4 ? "" : ".");
                                }
                            }
                        }
                    }
                }
                return null;
            }
        };
        ip.setFilters(filters);
    }
}