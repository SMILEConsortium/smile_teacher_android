package com.razortooth.smile.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.accounts.NetworkErrorException;

public class HttpUtil {

    private HttpUtil() {
        // Empty
    }

    private static final InputStream executeMethod(HttpUriRequest request)
        throws NetworkErrorException {

        HttpClient client = new DefaultHttpClient();

        // Execute
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            throw new NetworkErrorException(
                "Unexpected error executing request: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new NetworkErrorException("Unexpected error request: " + e.getMessage(), e);
        }

        // Check the status code
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
            throw new NetworkErrorException("Unexpected HTTP Status Code: " + statusCode);
        }

        // Returning the content
        HttpEntity entity = response.getEntity();
        try {
            return entity.getContent();
        } catch (IllegalStateException e) {
            throw new NetworkErrorException("Unexpected error returnig the request content: "
                + e.getMessage(), e);
        } catch (IOException e) {
            throw new NetworkErrorException("Unexpected error returnig the request content: "
                + e.getMessage(), e);
        }

    }

    public static final InputStream executePut(String url, String json)
        throws NetworkErrorException, UnsupportedEncodingException, JSONException {
        HttpPut put = new HttpPut(url);
        put.setHeader("Content-Type", "application/json");
        put.setEntity(new StringEntity(json));
        return executeMethod(put);
    }

    public static final InputStream executeGet(String url) throws NetworkErrorException {
        HttpGet get = new HttpGet(url);
        return executeMethod(get);
    }

}
