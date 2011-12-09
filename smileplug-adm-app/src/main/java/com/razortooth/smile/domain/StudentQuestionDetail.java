package com.razortooth.smile.domain;

import java.io.Serializable;

public class StudentQuestionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int number;
    private final int answer;
    private final int chosenAnswer;
    private final int chosenRating;

    public StudentQuestionDetail(int number, int answer, int chosenAnswer, int chosenRating) {
        super();
        this.number = number;
        this.answer = answer;
        this.chosenAnswer = chosenAnswer;
        this.chosenRating = chosenRating;
    }

    public int getNumber() {
        return number;
    }

    public int getAnswer() {
        return answer;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    public int getChosenRating() {
        return chosenRating;
    }

}
