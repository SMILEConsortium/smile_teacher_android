package com.razortooth.smile.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.NetworkErrorException;

public class HttpUtil {

	private HttpUtil() {
        // Empty
	}

	public static final InputStream executeGet(String url)
			throws NetworkErrorException {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// Execute the request
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			throw new NetworkErrorException(
					"Unexpected error executing HTTP Get:" + e.getMessage(), e);
		} catch (IOException e) {
			throw new NetworkErrorException(
					"Unexpected error executing HTTP Get" + e.getMessage(), e);
		}

		// Check the status code
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();

		if (statusCode != HttpStatus.SC_OK) {
			throw new NetworkErrorException("Unexpected HTTP Status Code: "
					+ statusCode);
		}

		// Returning the content
		HttpEntity entity = response.getEntity();
		try {
			return entity.getContent();
		} catch (IllegalStateException e) {
			throw new NetworkErrorException(
					"Unexpected error returnig the request content: "
							+ e.getMessage(), e);
		} catch (IOException e) {
			throw new NetworkErrorException(
					"Unexpected error returnig the request content: "
							+ e.getMessage(), e);
		}

	}

}
