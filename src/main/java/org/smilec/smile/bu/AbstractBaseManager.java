/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
package org.smilec.smile.bu;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.smilec.smile.bu.json.CurrentMessageJSONParser;
import org.smilec.smile.util.DeviceUtil;
import org.smilec.smile.util.HttpUtil;
import org.smilec.smile.util.IOUtil;
import org.smilec.smile.util.SendEmailAsyncTask;
import org.smilec.smile.util.SmilePlugUtil;

import android.accounts.NetworkErrorException;
import android.content.Context;

public abstract class AbstractBaseManager {

    protected static void checkServer(String ip) throws NetworkErrorException {

        InputStream is = null;
        String url = SmilePlugUtil.createUrl(ip);

        try {
            is = HttpUtil.executeGet(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection errror: " + e.getMessage(), e);
        } finally {
            IOUtil.silentClose(is);
        }

        if (is == null) {
            throw new NetworkErrorException("Server unavailable");
        }

    }

    protected static void checkConnection(Context context) throws NetworkErrorException {

        boolean isConnected = DeviceUtil.isConnected(context);

        if (!isConnected) {
            throw new NetworkErrorException("Connection unavailable");
        }

    }

    protected InputStream get(String ip, Context context, String url) throws NetworkErrorException {

        connect(ip, context);

        try {
            return HttpUtil.executeGet(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection error: " + e.getMessage(), e);
        }

    }

    protected void post(String ip, Context context, String url, String json) throws NetworkErrorException {
            
    	connect(ip, context);
      	HttpUtil.executePost(url, json);
    }
    
    protected InputStream delete(String ip, Context context, String url) throws NetworkErrorException {
        
    	connect(ip, context);
    	
        try { 
        	InputStream is = HttpUtil.executeDelete(url);
        	return is;
        			 
    	}
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }
        catch (JSONException e) { e.printStackTrace(); }
        
		return null;

//    	URL urlObject = null;
//		try { urlObject  = new URL(url); } 
//		catch (MalformedURLException exception) {
//    	    exception.printStackTrace();
//    	}
//    	HttpURLConnection httpURLConnection = null;
//    	try {
//    	    httpURLConnection = (HttpURLConnection) urlObject.openConnection();
//    	    httpURLConnection.setRequestProperty("Content-Type","application/json");
//    	    httpURLConnection.setRequestMethod("DELETE");
//    	    // httpURLConnection.setDoOutput(true);
//    	    
//    	    int result = httpURLConnection.getResponseCode();
//    	    
//    	    System.out.println(">> Answer of Smileplug => Code:"+result);
//    	    
//    	} catch (IOException exception) {
//    		exception.printStackTrace();
//    	} finally {         
//    	    if (httpURLConnection != null) {
//    	        httpURLConnection.disconnect();
//    	    }
//    	} 
    }

    protected void put(String ip, Context context, String url, String json)
        throws NetworkErrorException {
        connect(ip, context);

        InputStream is = null;

        try {
            is = HttpUtil.executePut(url, json);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection error: " + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
        	new SendEmailAsyncTask(e.getMessage(),JSONException.class.getName(),AbstractBaseManager.class.getName()).execute();
            throw new RuntimeException(e);
        } finally {
            IOUtil.silentClose(is);
        }

        if (is == null) {
            throw new NetworkErrorException("Service unavailable");
        }

    }

    public void connect(String ip, Context context) throws NetworkErrorException {
        checkConnection(context);
        checkServer(ip);
    }

}
