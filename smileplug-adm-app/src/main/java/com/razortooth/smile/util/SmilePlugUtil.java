package com.razortooth.smile.util;

import com.razortooth.smile.bu.Constants;

public class SmilePlugUtil {

    private static final String SMILEPLUG_SERVER_DIR = Constants.SERVER_DIR;
    private static final String SMILEPLUG_SERVER_URL = "http://%s/" + SMILEPLUG_SERVER_DIR + "/";

    public static final String START_MAKING_QUESTIONS_URL = "startmakequestion";
    public static final String START_SOLVING_QUESTIONS_URL = "startsolvequestion";

    public static final String SHOW_RESULTS_URL = "sendshowresults";
    public static final String ALL_DATA_URL = "all";
    public static final String QUESTION = "question";
    public static final String RESULTS = "results";

    private SmilePlugUtil() {
        // Empty
    }

    public static final String createUrl(String ip, String request) {

        String url = createUrl(ip);
        return url + request;

    }

    public static final String createUrl(String ip) {

        return String.format(SMILEPLUG_SERVER_URL, ip);

    }

}
