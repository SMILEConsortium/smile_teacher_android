package org.smilec.smile.util;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import org.smilec.smile.BuildConfig;

import android.os.AsyncTask;
import android.util.Log;

public class SendEmailAsyncTask extends AsyncTask <Void, Void, Boolean> {
	
	public static final String FROM = "consortiumsmile@gmail.com";
	public static final String PASSWORD = "razortooth";
	
	//public static final String TO = "consortiumsmile@gmail.com";
	public static final String TO = "reply+i-17909211-432eb6ae1acca189ce0ff1dc90b206bf0a62ae4d-64202@reply.github.com";
	public static final String TAG_SUBJECT = "[smile_teacher_android] ";
	
	Mail m = new Mail(FROM, PASSWORD);

    public SendEmailAsyncTask(String message, String exception, String currentClass) {
        
        String[] toArr = {TO};
        String body = new String();
        
        m.setTo(toArr);
        m.setFrom(FROM);
        
        if(exception != null) 
        	m.setSubject(TAG_SUBJECT+exception);
        else
        	m.setSubject(TAG_SUBJECT+"New message");
        
        body = currentClass+".java\n\n.\t\t"+exception+"\n\n\t\t\t\t"+message;
        m.setBody("> "+body);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        
    	return true;
//    	if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "Trying to send the email...");
//
//        try {
//            m.send();
//            Log.v(SendEmailAsyncTask.class.getName(), "JSONException sent to administrator!");
//            
//            return true;
//        } catch (AuthenticationFailedException e) {
//            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details (wrong password?)");
//            e.printStackTrace();
//            return false;
//        } catch (MessagingException e) {
//            Log.e(SendEmailAsyncTask.class.getName(), m.getBody() + "failed");
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }
}