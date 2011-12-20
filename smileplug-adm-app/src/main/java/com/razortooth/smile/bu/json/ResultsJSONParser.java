package com.razortooth.smile.bu.json;

import org.json.JSONArray;
import org.json.JSONObject;

import com.razortooth.smile.domain.Results;

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
        JSONArray bestScoredStudentNames = object.optJSONArray(BEST_SCORED_STUDENT_NAMES);
        JSONArray bestRatedQuestionStudentNames = object
            .optJSONArray(BEST_RATED_QUESTION_STUDENT_NAMES);
        JSONArray rightAnswers = object.optJSONArray(RIGHT_ANSWERS);
        int numberOfQuestions = object.optInt(NUMBER_OF_QUESTIONS);
        JSONArray averageRatings = object.optJSONArray(AVERAGE_RATINGS);
        JSONArray questionsCorrectPercentage = object.optJSONArray(QUESTIONS_CORRECT_PERCENTAGE);

        Results results = new Results(winnerScore, winnerRating, bestScoredStudentNames,
            bestRatedQuestionStudentNames, numberOfQuestions, rightAnswers, averageRatings,
            questionsCorrectPercentage);

        return results;

    }
}
