package com.razortooth.smile.ui;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.SmilePlugServerManager;
import com.razortooth.smile.util.ActivityUtil;
import com.razortooth.smile.util.DialogUtil;
import com.razortooth.smile.util.IPAddressValidatorUtil;
import com.razortooth.smile.util.ui.ProgressDialogAsyncTask;

public class LoginActivity extends Activity implements OnClickListener {

    private TextView ip;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ip = (TextView) findViewById(R.id.et_server_ip);
        connect = (Button) findViewById(R.id.bt_connect);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FormattingIPAddress();

        connect.setEnabled(false);
        connect.setOnClickListener(this);

        ip.setText("");
        ip.addTextChangedListener(new TextChanged());

        this.setVisible(true);
    }

    @Override
    public void onClick(View v) {
        new LoadTask(this).execute();
    }

    private void FormattingIPAddress() {
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

    private class TextChanged implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // nothing
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            IPAddressValidatorUtil ipUtil = new IPAddressValidatorUtil();
            if (ipUtil.validate(ip.getText().toString())) {
                connect.setEnabled(true);
            } else {
                connect.setEnabled(false);
            }
        }
    }

    private void loading() {
        Intent intent = new Intent(this, ChooseActivityFlowDialog.class);
        intent.putExtra(GeneralActivity.IP, ip.getText().toString());
        startActivity(intent);
        ActivityUtil.showLongToast(this, R.string.connection_established);

        this.setVisible(false);
    }

    private class LoadTask extends ProgressDialogAsyncTask<Void, Boolean> {

        private Context context;

        public LoadTask(Activity context) {
            super(context);

            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                new SmilePlugServerManager().connect(ip.getText().toString(), context);

                return true;
            } catch (NetworkErrorException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            super.onPostExecute(connected);
            if (connected == false) {
                DialogUtil.checkConnection(LoginActivity.this);
            } else {
                LoginActivity.this.loading();
            }
        }
    }
}