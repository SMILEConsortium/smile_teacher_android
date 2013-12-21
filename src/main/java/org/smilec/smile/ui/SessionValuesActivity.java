package org.smilec.smile.ui;

import org.smilec.smile.R;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.NetworkManager;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.CloseClickListenerUtil;
import org.smilec.smile.util.DialogUtil;
import org.smilec.smile.util.ui.ProgressDialogAsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SessionValuesActivity extends Activity {
	
	private String ip_smileplug;
	private String status;
	
	private TextView tv_teacherName;
	private TextView tv_sessionTitle;
	private TextView tv_groupName;
	private Button btnCreateSession;
	private ImageButton btnBack;
	
	private Context context;
	private static final int MSG_OK = 1;
	
	private NetworkManager receiver;
	public SessionValuesActivity() {
        receiver = new NetworkManager();
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ip_smileplug = this.getIntent().getStringExtra(GeneralActivity.PARAM_IP);
        
        status = this.getIntent().getStringExtra(GeneralActivity.PARAM_STATUS);
        
        setContentView(R.layout.session_values);
        Display displaySize = ActivityUtil.getDisplaySize(getApplicationContext());
        getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());

        context = this;
        try {
            PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView title = (TextView) findViewById(R.id.tv_title);
            title.setText(getText(R.string.app_name) + " " + pinfo.versionName);
        } catch (NameNotFoundException e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        }

        tv_teacherName = (TextView) findViewById(R.id.teacher_name);
        tv_sessionTitle = (TextView) findViewById(R.id.session_title);
        tv_groupName = (TextView) findViewById(R.id.group_name);
        btnCreateSession = (Button) findViewById(R.id.btn_create_session);
        btnBack = (ImageButton) findViewById(R.id.bt_back);

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

        btnCreateSession.setEnabled(false);
        btnCreateSession.setOnClickListener(new CreateSessionButtonListener());
        btnBack.setOnClickListener(new CloseClickListenerUtil(context));

//        tv_teacherName.addTextChangedListener(new TextChanged());
//        tv_sessionTitle.addTextChangedListener(new TextChanged());
//        tv_groupName.addTextChangedListener(new TextChanged());

        this.setVisible(true);

//        if ( ) {
//        	btnCreateSession.setEnabled(true);
//        } else {
//        	btnCreateSession.setEnabled(false);
//        }
        btnCreateSession.setEnabled(true);
    }
	
	private class CreateSessionButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            new LoadTask(SessionValuesActivity.this).execute();
        }
    }
	
	private void loading() {
        Intent intent = new Intent(this, ChooseActivityFlowDialog.class);
        intent.putExtra(GeneralActivity.PARAM_IP, ip_smileplug);
        intent.putExtra(GeneralActivity.PARAM_STATUS, status);
        
//        ActivityUtil.showLongToast(this, R.string.connection_established);
        
        // Display toast through the handler
    	Message msg = null;
		msg = mHandler.obtainMessage(MSG_OK, getResources().getString(R.string.creating_session));
		mHandler.sendMessage(msg);
		
		// Starting ChooseActivityFlowDialog
        startActivity(intent);
        
        // Closing SessionValuesActivity
        this.setVisible(false);
//      this.finish();
    }
    
	// To manage messages outside onCreate() method
    Handler mHandler = new Handler() { 
    	@Override        
        public void handleMessage(Message msg) {
    		String text2display = null;
  			switch (msg.what) {
  			case MSG_OK:
  				text2display = (String) msg.obj;
  				Toast.makeText(context, text2display, Toast.LENGTH_LONG).show();
  				break;
  			default: // should never happen
  				break;
  			}
        } 
     };
	
	private class LoadTask extends ProgressDialogAsyncTask<Void, String> {

        public LoadTask(Activity context) {
            super(context);
        }

        @Override
        protected String doInBackground(Void... params) {
        	
        	String teacherName = tv_teacherName.getText().toString(); 
        	String sessionTitle = tv_sessionTitle.getText().toString();
        	String groupName = tv_groupName.getText().toString();
        	
        	if(teacherName.equals("")) 	teacherName = "Default Teacher";
        	if(sessionTitle.equals("")) sessionTitle = "Default Session";
        	if(groupName.equals("")) 	groupName = "Default Group";
            
        	try {
            	SmilePlugServerManager spsm = new SmilePlugServerManager();

            	spsm.connect(ip_smileplug, context);
                spsm.createSession(ip_smileplug,teacherName,sessionTitle,groupName,context);

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
                DialogUtil.checkConnection(SessionValuesActivity.this, message);
            } else {
            	SessionValuesActivity.this.loading();
            }
        }
    }

}
