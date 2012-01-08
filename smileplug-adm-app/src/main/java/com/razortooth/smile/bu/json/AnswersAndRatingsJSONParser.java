package com.razortooth.smile.bu.json;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Student;

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
