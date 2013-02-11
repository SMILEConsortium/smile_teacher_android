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
package org.smile.smilec.bu.json;

import java.util.Map;

import org.json.JSONObject;

import org.smile.smilec.domain.Question;
import org.smile.smilec.domain.Student;
import org.smile.smilec.util.IPAddressUtil;

public class QuestionJSONParser {

    private static final String IP = "IP";
    private static final String QUESTION = "Q";
    private static final String OPTION_1 = "O1";
    private static final String OPTION_2 = "O2";
    private static final String OPTION_3 = "O3";
    private static final String OPTION_4 = "O4";
    private static final String ANSWER = "A";
    private static final String IMAGE = "PICURL";
    private static final String OWNER_NAME = "NAME";

    public static final Question process(int number, JSONObject object,
        Map<String, Student> students) {

        String ip = object.optString(IP);

        if (!(ip.equals("") || ip.equals(IPAddressUtil.getIPAddress()))) {
            Student owner = students.get(ip);
            if (owner != null) {
                owner.setMade(true);
            }
        }

        String name = object.optString(OWNER_NAME);
        String question = object.optString(QUESTION);
        String o1 = object.optString(OPTION_1);
        String o2 = object.optString(OPTION_2);
        String o3 = object.optString(OPTION_3);
        String o4 = object.optString(OPTION_4);
        int answer = Integer.valueOf(object.optString(ANSWER));
        String image = object.optString(IMAGE);

        return new Question(number, name, ip, question, o1, o2, o3, o4, answer, image);

    }
}
