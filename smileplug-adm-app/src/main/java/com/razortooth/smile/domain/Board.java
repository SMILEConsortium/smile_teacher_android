package com.razortooth.smile.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Collection<Student> students;
    private final Collection<Question> questions;

    public Board(Collection<Student> students, Collection<Question> questions) {
        super();
        this.students = new ArrayList<Student>(students);
        this.questions = new ArrayList<Question>(questions);
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public int getQuestionsNumber() {
        return questions.size();
    }

    public int getAnswersNumber() {

        int number = 0;
        for (Student s : students) {
            number += s.isSolved() ? 1 : 0;
        }

        return number;

    }

}
