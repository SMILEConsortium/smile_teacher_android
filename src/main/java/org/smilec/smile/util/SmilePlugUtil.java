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
package org.smilec.smile.util;

import org.smilec.smile.bu.Constants;

public class SmilePlugUtil {

    private static final String SMILEPLUG_SERVER_DIR = Constants.SERVER_DIR;
    private static final String SMILEPLUG_SERVER_URL = "http://%s/" + SMILEPLUG_SERVER_DIR + "/";

    public static final String START_MAKING_QUESTIONS_URL = "startmakequestion";
    public static final String START_SOLVING_QUESTIONS_URL = "startsolvequestion";
    public static final String RETAKE_QUESTIONS_URL = "junctionserverexecution/pushmsg.php";
//    public static final String RETAKE_QUESTIONS_URL = "junctionserverexecution/current/MSG/smsg.txt";

    public static final String SHOW_RESULTS_URL = "sendshowresults";
    public static final String ALL_DATA_URL = "all";
    public static final String QUESTION_URL = "question";
    public static final String RESULTS_URL = "results";
    public static final String RESET_URL = "reset";
    public static final String CURRENT_MESSAGE_URL = "currentmessage";

    public static final String JSON = "application/json";
    public static final String FORM = "application/x-www-form-urlencoded";

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
