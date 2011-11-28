package com.razortooth.smile.bu;

import java.io.InputStream;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.razortooth.smile.util.DeviceUtil;
import com.razortooth.smile.util.HttpUtil;
import com.razortooth.smile.util.IOUtil;
import com.razortooth.smile.util.SmilePlugUtil;

public class SmilePlugServerManager {

    public boolean connect(String ip, Context context) throws NetworkErrorException {

        // Check connection
        boolean isConnected = DeviceUtil.isConnected(context);
        if (!isConnected) {
            throw new NetworkErrorException("Connection unavailable");
        }

        // Check the server
        InputStream is = null;
        String url = SmilePlugUtil.createUrl(ip);
        try {
            is = HttpUtil.executeGet(url);
        } catch (NetworkErrorException e) {
            throw new NetworkErrorException("Server unavailable");
        } finally {
            IOUtil.silentClose(is);
        }

        return is != null;

    }

}
