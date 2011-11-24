package com.razortooth.smile.domain;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

    private String owner;
    private final List<ScoreBoardItem> items = new ArrayList<ScoreBoardItem>();

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ScoreBoardItem> getItems() {
        return items;
    }

    public int getCorrects() {

        int corrects = 0;

        for (ScoreBoardItem item : items) {
            if (item.isCorrect()) {
                corrects++;
            }
        }

        return corrects;

    }

    public int getTotal() {
        return items.size();
    }

}
