package com.razortooth.smile.domain;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionList extends ArrayList<Question> implements Parcelable {

    private static final long serialVersionUID = 1L;

    public QuestionList() {
        // Empty
    }

    public QuestionList(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.clear();

        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            Question question = new Question();
            question.setAnswer(in.readInt());
            question.setOwner(in.readString());
            question.setImageUrl(in.readString());
            question.setNumber(in.readInt());
            question.setOption1(in.readString());
            question.setOption2(in.readString());
            question.setOption3(in.readString());
            question.setOption4(in.readString());
            question.setQuestion(in.readString());

            this.add(question);
        }

    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel in) {
            return new QuestionList(in);
        }

        @Override
        public Object[] newArray(int size) {
            return null;
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();

        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            Question question = this.get(i);

            dest.writeInt(question.getAnswer());
            dest.writeString(question.getOwner());
            dest.writeString(question.getImageUrl());
            dest.writeInt(question.getNumber());
            dest.writeString(question.getOption1());
            dest.writeString(question.getOption2());
            dest.writeString(question.getOption3());
            dest.writeString(question.getOption4());
            dest.writeString(question.getQuestion());

        }
    }

}
