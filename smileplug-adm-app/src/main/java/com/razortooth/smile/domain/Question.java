package com.razortooth.smile.domain;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private final int number;
    private final String owner;
    private final String question;
    private final String option1;
    private final String option2;
    private final String option3;
    private final String option4;
    private final int answer;
    private final String image;
    private final List<Integer> answers = new ArrayList<Integer>();
    private final List<Integer> ratings = new ArrayList<Integer>();

    public Question(int number, String owner, String question, String option1, String option2,
        String option3, String option4, int answer, String image) {
        super();
        this.number = number;
        this.owner = owner;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getAnswer() {
        return answer;
    }

    public String getImage() {
        return image;
    }

    public boolean hasImage() {
        return image != null && image.trim().length() > 0;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void addAnswer(int answer) {
        answers.add(answer);
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

}
