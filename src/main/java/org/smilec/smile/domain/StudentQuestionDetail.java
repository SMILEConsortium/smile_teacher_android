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
