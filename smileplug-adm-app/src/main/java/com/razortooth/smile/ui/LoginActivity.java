package com.razortooth.smile.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.Constants;
import com.razortooth.smile.bu.NetworkManager;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.IPAddressValidatorUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class LoginActivity extends Activity {

    private TextView tvIp;
    private Button btConnect;

    private String status;

    private NetworkManager receiver;

    public LoginActivity() {
        receiver = new NetworkManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
        getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());

        try {
            PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView title = (TextView) findViewById(R.id.tv_title);
            title.setText(getText(R.string.app_name) + " " + pinfo.versionName);
        } catch (NameNotFoundException e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        }

        tvIp = (TextView) findViewById(R.id.et_server_ip);
        btConnect = (Button) findViewById(R.id.bt_connect);

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        formattingIPAddress();

        btConnect.setEnabled(false);
        btConnect.setOnClickListener(new ConnectButtonListener());

        tvIp.addTextChangedListener(new TextChanged());

        this.setVisible(true);

        IPAddressValidatorUtil ipUtil = new IPAddressValidatorUtil();
        if (ipUtil.validate(tvIp.getText().toString())) {
            btConnect.setEnabled(true);
        } else {
            btConnect.setEnabled(false);
        }
    }

    private class ConnectButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            new LoadTask(LoginActivity.this).execute();
        }
    }

    private void formattingIPAddress() {
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
        tvIp.setFilters(filters);
    }

    private class TextChanged implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // Empty
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Empty
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            IPAddressValidatorUtil ipUtil = new IPAddressValidatorUtil();
            boolean ok = ipUtil.validate(tvIp.getText().toString());
            btConnect.setEnabled(ok);
        }
    }

    private void loading() {
        Intent intent = new Intent(this, ChooseActivityFlowDialog.class);
        intent.putExtra(GeneralActivity.PARAM_IP, tvIp.getText().toString());
        intent.putExtra(GeneralActivity.PARAM_STATUS, status);
        startActivity(intent);
        ActivityUtil.showLongToast(this, R.string.connection_established);

        this.setVisible(false);
    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                new SmilePlugServerManager().connect(tvIp.getText().toString(), context);

                status = new SmilePlugServerManager().currentMessageGame(tvIp.getText().toString(),
                    context);

                return "";
            } catch (Exception e) {
                handleException(e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            if (!message.equals("")) {
                DialogUtil.checkConnection(LoginActivity.this, message);
            } else {
                LoginActivity.this.loading();
            }
        }
    }
}