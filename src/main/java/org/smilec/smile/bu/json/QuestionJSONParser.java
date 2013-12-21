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
package org.smilec.smile.bu.json;

import java.util.Map;

import org.json.JSONObject;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Student;
import org.smilec.smile.util.IPAddressUtil;

public class QuestionJSONParser {

    public static final Question process(int number, JSONObject object,
        Map<String, Student> students) {

        String ip = object.optString(Constants.IP);

        if (!(ip.equals("") || ip.equals(IPAddressUtil.getIPAddress()))) {
            Student owner = students.get(ip);
            if (owner != null) {
                owner.setMade(true);
            }
        }

        String name = object.optString(Constants.NAME);
        String question = object.optString(Constants.QUESTION);
        String o1 = object.optString(Constants.OPTION_1);
        String o2 = object.optString(Constants.OPTION_2);
        String o3 = object.optString(Constants.OPTION_3);
        String o4 = object.optString(Constants.OPTION_4);
        int answer = Integer.valueOf(object.optString(Constants.ANSWER));
        String image = object.optString(Constants.IMAGE_URL);

        return new Question(number, name, ip, question, o1, o2, o3, o4, answer, image);
    }
}
