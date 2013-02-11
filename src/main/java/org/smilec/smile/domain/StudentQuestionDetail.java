package org.smile.smilec.domain;

import java.io.Serializable;

public class StudentQuestionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int number;
    private final int answer;
    private final int chosenAnswer;
    private final int chosenRating;
    private final String owner;

    public StudentQuestionDetail(int number, String owner, int answer, int chosenAnswer,
        int chosenRating) {
        super();
        this.number = number;
        this.answer = answer;
        this.chosenAnswer = chosenAnswer;
        this.chosenRating = chosenRating;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public boolean correct() {
        if (getAnswer() == getChosenAnswer()) {
            return true;
        }
        return false;
    }

}
