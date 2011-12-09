package com.razortooth.smile.domain;

import java.io.Serializable;

public class QuestionStatus implements Comparable<QuestionStatus>, Serializable {

    private static final long serialVersionUID = 1L;

    private int number;
    private String owner;
    private double hitAverage;
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

    public double getHitAverage() {
        return hitAverage;
    }

    public void setHitAverage(double hitAverage) {
        this.hitAverage = hitAverage;
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
