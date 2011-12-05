package com.razortooth.smile.bu;

import java.io.InputStream;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.razortooth.smile.util.DeviceUtil;
import com.razortooth.smile.util.HttpUtil;
import com.razortooth.smile.util.IOUtil;
import com.razortooth.smile.util.SmilePlugUtil;

public class SmilePlugServerManager {

    public void connect(String ip, Context context) throws NetworkErrorException {
        checkConnection(context);
        checkServer(ip);
    }

    public void startMakingQuestions(String ip, Context context) throws NetworkErrorException {
        connect(ip, context);

        InputStream is = null;
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_MAKING_QUESTIONS_URL);

        try {
            is = HttpUtil.executeGet(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection errror: " + e.getMessage(), e);
        } finally {
            IOUtil.silentClose(is);
        }

        if (is == null) {
            throw new NetworkErrorException("Service unavailable");
        }

    }

    public void startSolvingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_SOLVING_QUESTIONS_URL);
        put(ip, context, url);
    }

    public void showResults(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.SHOW_RESULTS_URL);
        put(ip, context, url);
    }

    private void checkServer(String ip) throws NetworkErrorException {

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

    private void checkConnection(Context context) throws NetworkErrorException {

        boolean isConnected = DeviceUtil.isConnected(context);

        if (!isConnected) {
            throw new NetworkErrorException("Connection unavailable");
        }

    }

    private void put(String ip, Context context, String url) throws NetworkErrorException {
        connect(ip, context);

        InputStream is = null;

        try {
            is = HttpUtil.executePut(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Connection errror: " + e.getMessage(), e);
        } finally {
            IOUtil.silentClose(is);
        }

        if (is == null) {
            throw new NetworkErrorException("Service unavailable");
        }
    }

}
