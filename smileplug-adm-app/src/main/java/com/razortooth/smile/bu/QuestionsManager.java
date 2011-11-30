package com.razortooth.smile.bu;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;

import android.util.Log;

import com.razortooth.smile.domain.Question;
import com.razortooth.smile.util.IOUtil;

public class QuestionsManager {

    private static final String MARKER = "_@JSQ%_";

    protected void exportQuestions(String filePath, Collection<Question> questions) {

        Log.d("QuestionsManager", "Exporting Questions: " + filePath);

        File file = new File(filePath);

        PrintWriter pw = null;

        try {
            FileWriter fw = new FileWriter(file);
            pw = new PrintWriter(fw);

            pw.println(MARKER);
            pw.println(String.valueOf(questions.size()));

            for (Question q : questions) {
                pw.println(MARKER);
                pw.println(String.valueOf(q.getNumber()));
                pw.println(MARKER);
                pw.println(q.getQuestion());
                pw.println(MARKER);
                pw.println(q.getOption1());
                pw.println(MARKER);
                pw.println(q.getOption2());
                pw.println(MARKER);
                pw.println(q.getOption3());
                pw.println(MARKER);
                pw.println(q.getOption4());
                pw.println(MARKER);
                pw.println(q.hasImage() ? "Y" : "N");
                pw.println(MARKER);
                pw.println(q.getAnswer());
                pw.println(MARKER);
                pw.println(q.getOwner());

                if (q.hasImage()) {
                    // Copy image
                }
            }

            pw.println(MARKER);
            pw.close();

        } catch (Exception e) {
            // TODO: Exception?
        } finally {
            IOUtil.silentClose(pw);
        }

    }

}
