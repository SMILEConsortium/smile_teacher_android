package com.razortooth.smile.bu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.razortooth.smile.domain.ScoreBoard;
import com.razortooth.smile.domain.ScoreBoardItem;
import com.razortooth.smile.domain.StudentStatus;

public class StudentsManager {

    public List<StudentStatus> getStudentStatusList(Context context) {

        // TODO: Temporary implementation. Only for testing purposes
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {}

        List<StudentStatus> result = new ArrayList<StudentStatus>();

        StudentStatus s1 = new StudentStatus();
        s1.setMade(false);
        s1.setName("Joaozinho");
        s1.setScore(5);
        s1.setSolved(true);
        s1.setUsername("joaozinho");

        StudentStatus s2 = new StudentStatus();
        s1.setMade(true);
        s1.setName("Zezinho");
        s1.setScore(4);
        s1.setSolved(false);
        s1.setUsername("zezinho");

        result.add(s1);
        result.add(s2);

        return result;

    }

    public ScoreBoard getScoreBoard(Context context, String owner) {

        // TODO: Temporary implementation. Only for testing purposes
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {}

        ScoreBoard result = new ScoreBoard();
        result.setOwner(owner);

        ScoreBoardItem item1 = new ScoreBoardItem();
        item1.setChosenAnswer(1);
        item1.setCorrectAnswer(2);
        item1.setGivenRating(4);
        item1.setQuestionNumber(2);

        ScoreBoardItem item2 = new ScoreBoardItem();
        item2.setChosenAnswer(1);
        item2.setCorrectAnswer(2);
        item2.setGivenRating(4);
        item2.setQuestionNumber(2);

        result.addItem(item1);
        result.addItem(item2);

        return result;

    }

}
