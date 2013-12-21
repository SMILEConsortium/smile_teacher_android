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
import org.smilec.smile.bu.Constants;
import org.smilec.smile.domain.Results;

public class ResultsJSONParser { 

    

    public static final Results process(JSONObject object) {

        int winnerScore = object.optInt(Constants.WINNER_SCORE);
        float winnerRating = object.optLong(Constants.WINNER_RATING);

        JSONArray optJSONArray = object.optJSONArray(Constants.BEST_SCORED_STUDENT_NAMES);
        JSONArray bestScoredStudentNames = optJSONArray == null ? new JSONArray() : optJSONArray;

        JSONArray optJSONArray2 = object.optJSONArray(Constants.BEST_RATED_QUESTION_STUDENT_NAMES);
        JSONArray bestRatedQuestionStudentNames = optJSONArray2 == null ? new JSONArray() : optJSONArray2;

        JSONArray optJSONArray3 = object.optJSONArray(Constants.RIGHT_ANSWERS);
        JSONArray rightAnswers = optJSONArray3 == null ? new JSONArray() : optJSONArray3;

        int numberOfQuestions = object.optInt(Constants.NUMBER_OF_QUESTIONS);

        JSONArray optJSONArray4 = object.optJSONArray(Constants.AVERAGE_RATINGS);
        JSONArray averageRatings = optJSONArray4 == null ? new JSONArray() : optJSONArray4;

        JSONArray optJSONArray5 = object.optJSONArray(Constants.QUESTIONS_CORRECT_PERCENTAGE);
        JSONArray questionsCorrectPercentage = optJSONArray5 == null ? new JSONArray()
            : optJSONArray5;

        Results results = new Results(winnerScore, winnerRating, bestScoredStudentNames,
            bestRatedQuestionStudentNames, numberOfQuestions, rightAnswers, averageRatings,
            questionsCorrectPercentage);

        return results;
    }
}
