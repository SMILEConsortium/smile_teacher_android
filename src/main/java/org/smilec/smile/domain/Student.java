/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
package org.smile.smilec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student implements Serializable, Comparable<Student> {

    private static final long serialVersionUID = 1L;

    private final String ip;
    private final String name;
    private boolean made = false;
    private boolean solved = false;
    private final Map<Question, Integer> answers = new HashMap<Question, Integer>();
    private final Map<Question, Integer> ratings = new HashMap<Question, Integer>();

    public Student(String ip, String name) {
        super();
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public boolean isMade() {
        return made;
    }

    public void setMade(boolean made) {
        this.made = made;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Map<Question, Integer> getAnswers() {
        return answers;
    }

    public void addAnswer(Question question, Integer answer) {
        answers.put(question, answer);
    }

    public Map<Question, Integer> getRatings() {
        return ratings;
    }

    public void addRating(Question question, Integer answer) {
        ratings.put(question, answer);
    }

    public int getAnswered() {
        return answers.size();
    }

    public int getScore() {

        int score = 0;
        for (Question q : answers.keySet()) {

            int answer = q.getAnswer();
            int giveAnswer = answers.get(q);

            score += (answer == giveAnswer) ? 1 : 0;

        }

        return score;

    }

    public List<StudentQuestionDetail> getDetails() {

        List<StudentQuestionDetail> result = new ArrayList<StudentQuestionDetail>();

        for (Question q : answers.keySet()) {

            Integer a = answers.get(q);
            Integer r = ratings.get(q);

            StudentQuestionDetail sqd;
            sqd = new StudentQuestionDetail(q.getNumber(), q.getOwner(), q.getAnswer(), a, r);

            result.add(sqd);

        }

        return result;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        if (ip == null) {
            if (other.ip != null) {
                return false;
            }
        } else if (!ip.equals(other.ip)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Student other) {
        return name.compareTo(other.name);
    }

}
