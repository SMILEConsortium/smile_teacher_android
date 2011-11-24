package com.razortooth.smile.domain;

public class QuestionStatus implements Comparable<QuestionStatus> {

    private int number;
    private String owner;
    private double correct;
    private double rating;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getCorrect() {
        return correct;
    }

    public void setCorrect(double correct) {
        this.correct = correct;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public int compareTo(QuestionStatus other) {
        return number - other.number;
    }

}
