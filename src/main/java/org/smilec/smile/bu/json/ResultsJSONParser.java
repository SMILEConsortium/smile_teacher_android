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

import org.json.JSONArray;
import org.json.JSONObject;
import org.smilec.smile.domain.Results;

public class ResultsJSONParser {

    private static final String QUESTIONS_CORRECT_PERCENTAGE = "questionsCorrectPercentage";
    private static final String AVERAGE_RATINGS = "averageRatings";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String RIGHT_ANSWERS = "rightAnswers";
    private static final String BEST_RATED_QUESTION_STUDENT_NAMES = "bestRatedQuestionStudentNames";
    private static final String BEST_SCORED_STUDENT_NAMES = "bestScoredStudentNames";
    private static final String WINNER_RATING = "winnerRating";
    private static final String WINNER_SCORE = "winnerScore";

    public static final Results process(JSONObject object) {

        int winnerScore = object.optInt(WINNER_SCORE);
        float winnerRating = object.optLong(WINNER_RATING);

        JSONArray optJSONArray = object.optJSONArray(BEST_SCORED_STUDENT_NAMES);
        JSONArray bestScoredStudentNames = optJSONArray == null ? new JSONArray() : optJSONArray;

        JSONArray optJSONArray2 = object.optJSONArray(BEST_RATED_QUESTION_STUDENT_NAMES);
        JSONArray bestRatedQuestionStudentNames = optJSONArray2 == null ? new JSONArray()
            : optJSONArray2;

        JSONArray optJSONArray3 = object.optJSONArray(RIGHT_ANSWERS);
        JSONArray rightAnswers = optJSONArray3 == null ? new JSONArray() : optJSONArray3;

        int numberOfQuestions = object.optInt(NUMBER_OF_QUESTIONS);

        JSONArray optJSONArray4 = object.optJSONArray(AVERAGE_RATINGS);
        JSONArray averageRatings = optJSONArray4 == null ? new JSONArray() : optJSONArray4;

        JSONArray optJSONArray5 = object.optJSONArray(QUESTIONS_CORRECT_PERCENTAGE);
        JSONArray questionsCorrectPercentage = optJSONArray5 == null ? new JSONArray()
            : optJSONArray5;

        Results results = new Results(winnerScore, winnerRating, bestScoredStudentNames,
            bestRatedQuestionStudentNames, numberOfQuestions, rightAnswers, averageRatings,
            questionsCorrectPercentage);

        return results;

    }
}
