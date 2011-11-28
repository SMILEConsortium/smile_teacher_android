package com.razortooth.smile.util;

import com.razortooth.smile.bu.Constants;

public class SmilePlugUtil {

    private static final String SMILEPLUG_SERVER_DIR = Constants.SERVER_DIR;
    private static final String SMILEPLUG_SERVER_URL = "http://%s/" + SMILEPLUG_SERVER_DIR + "/";

    private SmilePlugUtil() {
        // Empty
    }

    public static final String createUrl(String ip, String request) {

        String url = createUrl(ip);
        return url + request;

    }

    public static final String createUrl(String ip) {

        // Just for tests
        ip = "www.marcorocha.net";

        return String.format(SMILEPLUG_SERVER_URL, ip);

    }

}
