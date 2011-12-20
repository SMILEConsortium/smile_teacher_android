package com.razortooth.smile.domain;

import org.json.JSONArray;

public class Results {

    private int winnerScore;
    private float winnerRating;
    private JSONArray bestScoredStudentNames;
    private JSONArray bestRatedQuestionStudentNames;
    private int numberOfQuestions;
    private JSONArray rightAnswers;
    private JSONArray averageRatings;
    private JSONArray questionsCorrectPercentage;

    public Results(int winnerScore, float winnerRating, JSONArray bestScoredStudentNames,
        JSONArray bestRatedQuestionStudentNames, int numberOfQuestions, JSONArray rightAnswers,
        JSONArray averageRatings, JSONArray questionsCorrectPercentage) {
        super();
        this.winnerScore = winnerScore;
        this.winnerRating = winnerRating;
        this.bestScoredStudentNames = bestScoredStudentNames;
        this.bestRatedQuestionStudentNames = bestRatedQuestionStudentNames;
        this.numberOfQuestions = numberOfQuestions;
        this.rightAnswers = rightAnswers;
        this.averageRatings = averageRatings;
        this.questionsCorrectPercentage = questionsCorrectPercentage;
    }

    public int getWinnerScore() {
        return winnerScore;
    }

    public void setWinnerScore(int winnerScore) {
        this.winnerScore = winnerScore;
    }

    public float getWinnerRating() {
        return winnerRating;
    }

    public void setWinnerRating(float winnerRating) {
        this.winnerRating = winnerRating;
    }

    public JSONArray getBestScoredStudentNames() {
        return bestScoredStudentNames;
    }

    public void setBestScoredStudentNames(JSONArray bestScoredStudentNames) {
        this.bestScoredStudentNames = bestScoredStudentNames;
    }

    public JSONArray getBestRatedQuestionStudentNames() {
        return bestRatedQuestionStudentNames;
    }

    public void setBestRatedQuestionStudentNames(JSONArray bestRatedQuestionStudentNames) {
        this.bestRatedQuestionStudentNames = bestRatedQuestionStudentNames;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public JSONArray getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(JSONArray rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public JSONArray getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(JSONArray averageRatings) {
        this.averageRatings = averageRatings;
    }

    public JSONArray getQuestionsCorrectPercentage() {
        return questionsCorrectPercentage;
    }

    public void setQuestionsCorrectPercentage(JSONArray questionsCorrectPercentage) {
        this.questionsCorrectPercentage = questionsCorrectPercentage;
    }
}
