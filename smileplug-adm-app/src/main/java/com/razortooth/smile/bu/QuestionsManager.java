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

import net.iharder.Base64;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.util.DeviceUtil;
import com.razortooth.smile.util.IOUtil;
import com.razortooth.smile.util.IPAddressUtil;

public class QuestionsManager {

    private static final String JPG = ".jpg";
    private static final String QUESTIONS_DIR = Constants.APP_ID;
    private static final String QUESTIONS_FILE = "jq_export.txt";
    private static final String MARKER = "_@JSQ%_";
    public static final String TEACHER_NAME = "teacher";
    public static final String TEACHER_IP = "127.0.0.1";

    private final File questionsDir;

    public QuestionsManager() throws DataAccessException {
        questionsDir = createDir();
    }

    public File[] getSavedQuestions() throws DataAccessException {

        if (!DeviceUtil.isExternalStorageAvailable()) {
            throw new DataAccessException("External storage unavailable");
        }

        return questionsDir.listFiles();

    }

    private File createDir() throws DataAccessException {

        if (!DeviceUtil.isExternalStorageAvailable()) {
            throw new DataAccessException("External storage unavailable");
        }

        File external = Environment.getExternalStorageDirectory();
        File dir = new File(external, QUESTIONS_DIR);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;

    }

    public void saveQuestions(Context context, String name, Collection<Question> questions)
        throws DataAccessException {

        if (!DeviceUtil.isExternalStorageWriteable()) {
            throw new DataAccessException("External storage unavailable");
        }

        Log.d("QuestionsManager", "Exporting Questions: " + name);

        File dir = new File(questionsDir, name);
        boolean ok = dir.mkdirs();

        System.out.println(ok);

        File file = new File(dir, QUESTIONS_FILE);

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
                String ip = IPAddressUtil.getIPAddress();
                pw.println(ip);

                if (q.hasImage()) {
                    File img = new File(dir, q.getNumber() + JPG);
                    byte[] imgContent = Base64.decode(q.getImage());
                    IOUtil.saveBytes(img, imgContent);
                }
            }

            pw.println(MARKER);
            pw.close();

        } catch (Exception e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        } finally {
            IOUtil.silentClose(pw);
        }

    }

    public Collection<Question> loadQuestions(String name) {

        Collection<Question> result = new ArrayList<Question>();
        Log.d("QuestionsManager", "Importing Questions: " + name);

        File dir = new File(questionsDir, name);
        File file = new File(dir, QUESTIONS_FILE);

        if (!file.exists()) {
            Log.d("QuestionsManager", "Questions file doesn't exists: " + name);
            return result;
        }

        do {

            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException e1) {
                Log.e(Constants.LOG_CATEGORY, "Error: ", e1);
                return result;
            }

            BufferedReader br = new BufferedReader(fr, 8 * 1024);

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

                    String sNumber = readUntilMarker(br);
                    int number = Integer.valueOf(sNumber);

                    String question = readUntilMarker(br);
                    String option1 = readUntilMarker(br);
                    String option2 = readUntilMarker(br);
                    String option3 = readUntilMarker(br);
                    String option4 = readUntilMarker(br);

                    String sHasImage = readUntilMarker(br);
                    boolean hasImage = "Y".equals(sHasImage);

                    String image = null;
                    if (hasImage) {
                        File img = new File(dir, number + JPG);
                        byte[] originalArray = IOUtil.loadBytes(img);
                        String encodedString = Base64.encodeBytes(originalArray);
                        image = encodedString;
                    }

                    String sAnswer = readUntilMarker(br);
                    int answer = Integer.valueOf(sAnswer);

                    String ip = readUntilMarker(br);

                    Question q = new Question(number, TEACHER_NAME, ip, question, option1, option2,
                        option3, option4, answer, image);

                    result.add(q);
                }

            } catch (IOException e) {
                Log.e(Constants.LOG_CATEGORY, "Error: ", e);
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
