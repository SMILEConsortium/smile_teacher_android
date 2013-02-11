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

import org.json.JSONArray;
import org.json.JSONObject;

import org.smile.smilec.domain.Question;
import org.smile.smilec.domain.Student;

public class AnswersAndRatingsJSONParser {

    private static final String IP = "IP";
    private static final String ANSWERS = "MYANSWER";
    private static final String RATINGS = "MYRATING";

    public static final void process(JSONObject object, Map<String, Student> students,
        Map<Integer, Question> questions) {

        String ip = object.optString(IP);

        Student student = students.get(ip);
        if (student != null) {
            student.setSolved(true);
        }

        JSONArray answersArray = object.optJSONArray(ANSWERS);
        JSONArray ratingsArray = object.optJSONArray(RATINGS);

        for (int i = 0; i < answersArray.length(); i++) {

            Question question = questions.get(i + 1);

            String sRating = ratingsArray.optString(i);
            String sAnswer = answersArray.optString(i);

            if (sRating != null && sRating.trim().length() > 0) {

                Integer rating = Integer.valueOf(sRating);

                question.addRating(rating);
                student.addRating(question, rating);
            }

            if (sAnswer != null && sAnswer.trim().length() > 0) {

                Integer answer = Integer.valueOf(sAnswer);

                question.addAnswer(answer);
                student.addAnswer(question, answer);

            }

        }

    }
}
