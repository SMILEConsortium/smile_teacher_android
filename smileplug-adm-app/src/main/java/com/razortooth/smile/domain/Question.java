package com.razortooth.smile.domain;

public class Question {

    private String owner;
    private String question;
    private byte[] image;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answer;

    public Question(String owner, String question, String option1, String option2, String option3,
        String option4, int answer) {
        this(owner, question, null, option1, option2, option3, option4, answer);
    }

    public Question(String owner, String question, byte[] image, String option1, String option2,
        String option3, String option4, int answer) {
        super();
        this.owner = owner;
        this.question = question;
        this.image = image;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

}
