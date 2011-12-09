package com.razortooth.smile.bu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

    protected Collection<Question> readQuestionsFromFile(String filePath) {

        Collection<Question> result = new ArrayList<Question>();
        Log.d("QuestionsManager", "Importing Questions: " + filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            Log.d("QuestionsManager", "File doesn't exists: " + filePath);
            return result;
        }

        do {

            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException e1) {
                // TODO: Exception?
                return result;
            }

            BufferedReader br = new BufferedReader(fr);

            try {

                // Read Marker
                if (!readMarker(br)) {
                    break;
                }

                // Number of Questions
                int n = Integer.valueOf(br.readLine());
                if (!readMarker(br)) {
                    break;
                }

                if (n <= 0) {
                    return result;
                }

                Log.d("QuestionsManager", "Number of Questions: " + n);

                for (int i = 0; i < n; i++) {

                    int number;
                    String question = "";
                    String option1 = "";
                    String option2 = "";
                    String option3 = "";
                    String option4 = "";
                    int answer;
                    String owner = "";
                    boolean hasImage = false;

                    String sNumber = readUntilMarker(br);
                    number = Integer.valueOf(sNumber);
                    if (number != i) {
                        break;
                    }

                    question = readUntilMarker(br);
                    option1 = readUntilMarker(br);
                    option2 = readUntilMarker(br);
                    option3 = readUntilMarker(br);
                    option4 = readUntilMarker(br);

                    String sAnswer = readUntilMarker(br);
                    answer = Integer.valueOf(sAnswer);

                    owner = readUntilMarker(br);

                    String sHasImage = readUntilMarker(br);
                    hasImage = sHasImage.equals("Y");

                    String image = null;
                    if (hasImage) {
                        // TODO: Load Image
                    }

                    Question q = new Question(number, owner, question, option1, option2, option3,
                        option4, answer, image);

                    result.add(q);
                }

            } catch (IOException e) {
                // TODO: Exception
            } finally {
                IOUtil.silentClose(br);
            }

        } while (false);

        return result;
    }

    public boolean readMarker(BufferedReader br) throws IOException {
        String s = br.readLine();
        return s != null && s.equals(MARKER);
    }

    public String readUntilMarker(BufferedReader in) throws IOException {

        String s = "";
        boolean first = true;
        while (true) {

            String t = in.readLine();
            if (t == null) {
                return s;
            }

            if (t.equals(MARKER)) {
                return s;
            }

            if (first) {
                s += t;
            } else {
                s += "\n" + t;
            }

        }

    }

}
