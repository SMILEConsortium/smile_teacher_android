package com.razortooth.smile.domain;

public class ScoreBoardItem implements Comparable<ScoreBoardItem> {

    private int questionNumber;
    private int correctAnswer;
    private int chosenAnswer;
    private int givenRating;

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    public int getGivenRating() {
        return givenRating;
    }

    public void setGivenRating(int givenRating) {
        this.givenRating = givenRating;
    }

    public boolean isCorrect() {
        return correctAnswer == chosenAnswer;
    }

    @Override
    public int compareTo(ScoreBoardItem other) {
        return questionNumber - other.questionNumber;
    }

}
