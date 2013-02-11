package com.razortooth.smile.domain;

import java.io.Serializable;

import org.json.JSONArray;

public class Results implements Serializable {

    private static final long serialVersionUID = 1L;
    private int winnerScore;
    private float winnerRating;
    private String bestScoredStudentNames;
    private String bestRatedQuestionStudentNames;
    private int numberOfQuestions;
    private String rightAnswers;
    private String averageRatings;
    private String questionsCorrectPercentage;

    public Results(int winnerScore, float winnerRating, JSONArray bestScoredStudentNames,
        JSONArray bestRatedQuestionStudentNames, int numberOfQuestions, JSONArray rightAnswers,
        JSONArray averageRatings, JSONArray questionsCorrectPercentage) {
        super();
        this.winnerScore = winnerScore;
        this.winnerRating = winnerRating;
        this.bestScoredStudentNames = bestScoredStudentNames.toString();
        this.bestRatedQuestionStudentNames = bestRatedQuestionStudentNames.toString();
        this.numberOfQuestions = numberOfQuestions;
        this.rightAnswers = rightAnswers.toString();
        this.averageRatings = averageRatings.toString();
        this.questionsCorrectPercentage = questionsCorrectPercentage.toString();
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

    public String getBestScoredStudentNames() {
        return bestScoredStudentNames;
    }

    public void setBestScoredStudentNames(String bestScoredStudentNames) {
        this.bestScoredStudentNames = bestScoredStudentNames;
    }

    public String getBestRatedQuestionStudentNames() {
        return bestRatedQuestionStudentNames;
    }

    public void setBestRatedQuestionStudentNames(String bestRatedQuestionStudentNames) {
        this.bestRatedQuestionStudentNames = bestRatedQuestionStudentNames;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(String rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public String getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(String averageRatings) {
        this.averageRatings = averageRatings;
    }

    public String getQuestionsCorrectPercentage() {
        return questionsCorrectPercentage;
    }

    public void setQuestionsCorrectPercentage(String questionsCorrectPercentage) {
        this.questionsCorrectPercentage = questionsCorrectPercentage;
    }
}
