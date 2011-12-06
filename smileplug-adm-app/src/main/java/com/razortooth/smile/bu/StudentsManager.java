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
        s1.setName("Dean");
        s1.setScore(5);
        s1.setSolved(true);
        s1.setUsername("dean");

        StudentStatus s2 = new StudentStatus();
        s2.setMade(true);
        s2.setName("Peter");
        s2.setScore(4);
        s2.setSolved(false);
        s2.setUsername("peter");

        StudentStatus s3 = new StudentStatus();
        s3.setMade(true);
        s3.setName("Jhon");
        s3.setScore(3);
        s3.setSolved(false);
        s3.setUsername("jhon");

        StudentStatus s4 = new StudentStatus();
        s4.setMade(true);
        s4.setName("Tom");
        s4.setScore(4);
        s4.setSolved(false);
        s4.setUsername("tom");

        StudentStatus s5 = new StudentStatus();
        s5.setMade(true);
        s5.setName("James");
        s5.setScore(5);
        s5.setSolved(true);
        s5.setUsername("james");

        result.add(s1);
        result.add(s2);
        result.add(s3);
        result.add(s4);
        result.add(s5);

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
