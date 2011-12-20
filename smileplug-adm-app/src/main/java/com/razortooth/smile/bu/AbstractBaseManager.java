package com.razortooth.smile.bu;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.razortooth.smile.util.DeviceUtil;
import com.razortooth.smile.util.HttpUtil;
import com.razortooth.smile.util.IOUtil;
import com.razortooth.smile.util.SmilePlugUtil;

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

    protected static InputStream get(String ip, Context context, String url)
        throws NetworkErrorException {

        connect(ip, context);

        try {
            return HttpUtil.executeGet(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection error: " + e.getMessage(), e);
        }

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
            throw new RuntimeException(e);
        } finally {
            IOUtil.silentClose(is);
        }

        if (is == null) {
            throw new NetworkErrorException("Service unavailable");
        }

    }

    public static void connect(String ip, Context context) throws NetworkErrorException {
        checkConnection(context);
        checkServer(ip);
    }

}
