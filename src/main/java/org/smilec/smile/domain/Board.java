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
package org.smilec.smile.domain;

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
