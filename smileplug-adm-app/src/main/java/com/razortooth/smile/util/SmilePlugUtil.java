package com.razortooth.smile.util;

import com.razortooth.smile.bu.Constants;

public class SmilePlugUtil {

    private static final String SMILEPLUG_SERVER_DIR = Constants.SERVER_DIR;
    private static final String SMILEPLUG_SERVER_URL = "http://%s/" + SMILEPLUG_SERVER_DIR + "/";

    public static final String START_MAKING_QUESTIONS_URL = "startmakingquestions";
    public static final String START_SOLVING_QUESTIONS_URL = "startsolvingquestions";
    public static final String SHOW_RESULTS_URL = "showresults";
    public static final String ALL_DATA_URL = "all";

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
