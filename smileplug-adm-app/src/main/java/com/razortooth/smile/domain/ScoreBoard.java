package com.razortooth.smile.domain;

import java.util.ArrayList;
import java.util.Collection;
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

    public void addItem(ScoreBoardItem item) {
        this.items.add(item);
    }

    public void addItems(Collection<ScoreBoardItem> items) {
        this.items.addAll(items);
    }

    public void addItems(ScoreBoardItem... items) {
        if (items != null && items.length > 0) {
            for (ScoreBoardItem item : items) {
                addItem(item);
            }
        }
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
