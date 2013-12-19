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
package org.smilec.smile.bu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.iharder.Base64;

import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.domain.Question;
import org.smilec.smile.util.DeviceUtil;
import org.smilec.smile.util.IOUtil;
import org.smilec.smile.util.IPAddressUtil;
import org.smilec.smile.util.ImageLoader;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class QuestionsManager {

	private static final String JPG = ".jpg";
	private static final String QUESTIONS_DIR = Constants.APP_ID;
	private static final String QUESTIONS_FILE = "jq_export.txt";
	private static final String MARKER = "_@JSQ%_";
	public static final String TEACHER_NAME = "teacher";
	public static final String TEACHER_IP = "127.0.0.1";
    public static final String IQ_MIME_EXT_JSON = ".json";
    public static final String IQ_MIME_EXT_CSV = ".csv";

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
	
	public static void resetListOfDeletedQuestions(Context context) {
		
		OutputStreamWriter osw;
		try {
			osw = new OutputStreamWriter(
					context.openFileOutput("filter_delete", Context.MODE_PRIVATE)
				);
			osw.write("");
			osw.close();
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * This method get from internal file named 'filter_delete' the numbers of all questions deleted
	 * @return something which has this aspect: '3;4;1;2;0;'
	 */
	private static String getDeletedQuestionInLocalFile(Context context) {
		
		String inputString;
		StringBuffer stringBuffer = new StringBuffer();
		
		try {
		    BufferedReader inputReader = new BufferedReader(
		    		new InputStreamReader(context.openFileInput("filter_delete"))
		    );
		    
			while ((inputString = inputReader.readLine()) != null) {
				stringBuffer.append(inputString);
				System.out.println(">"+inputString); // TEMP: to remove
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return stringBuffer.toString();
	}
	
	/**
	 * Add the number of the question to consider as 'deleted' in the internal file 'filter_delete' 
	 * @param context the context, the activity
	 * @param currentQuestion the number of the question to consider as 'deleted'
	 */
	public static void addDeletedQuestionInLocalFile(Context context, int currentQuestion) {
		
		// final
		String filter =  getDeletedQuestionInLocalFile(context) + currentQuestion+";";
		FileOutputStream fOut = null;
		   
	   try {
			fOut = context.openFileOutput("filter_delete", Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
		
		// Write the string to the file and ensure that everything is really written out and close
			osw.write(filter);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get all the numbers of questions considered as 'deleted'
	 * @param context the context, the activity
	 * @return an array of all numbers 
	 */
	public static String[] getDeletedQuestionsInLocalFile(Context context) {
		
		return getDeletedQuestionInLocalFile(context).split(";");
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

//	public void saveQuestions(Context context, String name, Collection<Question> questions,
//		String ipServer) throws DataAccessException {
//
//		if (!DeviceUtil.isExternalStorageWriteable()) {
//			throw new DataAccessException("External storage unavailable");
//		}
//
//        if (name.endsWith(".json") == false) {
//            name = name + ".json";
//        }
//
//		Log.d("QuestionsManager", "Exporting Questions: " + name);
//
//		File dir = new File(questionsDir, name);
//		boolean ok = dir.mkdirs();
//
//		//
//		// Hello please make sure we don't overwrite the same file
//		//
//		File file = new File(dir, QUESTIONS_FILE);
//
//		if (file.isDirectory() || file.exists()) {
//			throw new DataAccessException("File = " + name + " already exists in " + questionsDir);
//		}
//		PrintWriter pw = null;
//
//		try {
//			FileWriter fw = new FileWriter(file);
//			pw = new PrintWriter(fw);
//
//			pw.println(MARKER);
//			pw.println(String.valueOf(questions.size()));
//
//			for (Question q : questions) {
//				pw.println(MARKER);
//				pw.println(String.valueOf(q.getNumber()));
//				pw.println(MARKER);
//				pw.println(q.getQuestion());
//				pw.println(MARKER);
//				pw.println(q.getOption1());
//				pw.println(MARKER);
//				pw.println(q.getOption2());
//				pw.println(MARKER);
//				pw.println(q.getOption3());
//				pw.println(MARKER);
//				pw.println(q.getOption4());
//				pw.println(MARKER);
//				pw.println(q.hasImage() ? "Y" : "N");
//				pw.println(MARKER);
//				pw.println(q.getAnswer());
//				pw.println(MARKER);
//				String ip = IPAddressUtil.getIPAddress();
//				pw.println(ip);
//				pw.println(MARKER);
//				pw.println(q.getOwner());
//				if (q.hasImage()) {
//					File img = new File(dir, q.getNumber() + JPG);
//
//					byte[] s = ImageLoader.loadBitmap(Constants.HTTP + ipServer + q.getImageUrl());
//
//					IOUtil.saveBytes(img, s);
//				}
//			}
//
//			pw.println(MARKER);
//			pw.close();
//
//		} catch (Exception e) {
//			Log.e(Constants.LOG_CATEGORY, "Error: ", e);
//		} finally {
//			IOUtil.silentClose(pw);
//		}
//
//	}
	
// USELESS: Previously used when the prepared questions came from local files 
	
//	public Collection<Question> loadQuestions(String name) {
//        int number = 1;
//		Collection<Question> result = new ArrayList<Question>();
//		Log.d("QuestionsManager", "Importing Questions: " + name);
//
//		File dir = new File(questionsDir, name);
//		File file = new File(dir, QUESTIONS_FILE);
//		
//		System.out.println("file.getAbsolutePath()="+file.getAbsolutePath());
//
//		if (!file.exists()) {
//			Log.d("QuestionsManager", "JSON Questions file doesn't exist: " + file.getAbsolutePath());
//            file = new File(dir, dir.getName());
//		    if (!file.exists()) {
//			    Log.d("QuestionsManager", "CSV Questions file doesn't exist: " + file.getAbsolutePath());
//			    return result;
//            } else {
//			    Log.d("QuestionsManager", "CSV Questions file exists: " + file.getAbsolutePath());
//            }
//		}
//
//        // XXX candidate for refactoring
//        if (file.getAbsolutePath().endsWith(IQ_MIME_EXT_CSV) == true) {
//		  Log.d("QuestionsManager", "Load CSV Questions from: " + file.getAbsolutePath());
//          //
//          // Handle the .csv load
//          //
//          try {
//            CsvReader csvQuestions = new CsvReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//            // CsvReader csvQuestions = new CsvReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
//            csvQuestions.readHeaders();
//
//            while (csvQuestions.readRecord())
//            {
//              try {
//                String num = csvQuestions.get("num");
//                String question = csvQuestions.get("question");
//                String option1 = csvQuestions.get("choice1");
//                String option2 = csvQuestions.get("choice2");
//                String option3 = csvQuestions.get("choice3");
//                String option4 = csvQuestions.get("choice4");
//                String hasImage = csvQuestions.get("has_image");
//				String image = null;
//				Log.d("QuestionsManager", "Loading question: " + question + "(a) " + option1 + "(b)" + option2 + "(c)" + option3 + "(d)" + option4);
//				if (hasImage != null && hasImage.length() > 0) {
//					File img = new File(dir, number + JPG);
//					byte[] originalArray = IOUtil.loadBytes(img);
//					String encodedString = Base64.encodeBytes(originalArray);
//					image = encodedString;
//				}
//                int answer = Integer.valueOf(csvQuestions.get("answers"));
//                String owner = csvQuestions.get("owner_name");
//                if (owner == null || owner.length() == 0) {
//                    owner= TEACHER_NAME;
//                }
//                String ip = csvQuestions.get("owner_IP");
//                if (ip == null || ip.length() == 0) {
//                    ip = TEACHER_IP;
//                }
//				Question q = new Question(number, owner, ip, question, option1, option2, option3, option4, answer, image);
//				result.add(q);
//                number = number + 1;
//              } catch(IOException ioe) {
//                  // XXX Log the error or do something
//				  Log.e(Constants.LOG_CATEGORY, "CSV Load Error: ", ioe);
//              }
//            }
//		    Log.d("QuestionsManager", "Total Number of Questions: " + (number - 1));
//          } catch(FileNotFoundException fnfe) {
//                  // XXX Log the error or do something
//				  Log.e(Constants.LOG_CATEGORY, "CSV Load Error, cannot find file: ", fnfe);
//          } catch(UnsupportedEncodingException uee) {
//                  // XXX Log the error or do something
//				  Log.e(Constants.LOG_CATEGORY, "CSV Load Error, unsupported encoding: ", uee);
//          } catch(IOException ioe) {
//                  // XXX Log the error or do something
//				  Log.e(Constants.LOG_CATEGORY, "CSV Load Error: ", ioe);
//          }
//        } else {
//		  Log.d("QuestionsManager", "Load JSON Questions from: " + file.getAbsolutePath());
//		  do {
//
//			FileReader fr = null;
//			try {
//				fr = new FileReader(file);
//			} catch (FileNotFoundException e1) {
//				Log.e(Constants.LOG_CATEGORY, "File Load Error, not found: ", e1);
//				return result;
//			}
//
//			BufferedReader br = new BufferedReader(fr, 8 * 1024);
//
//			try {
//
//				// Read Marker
//				if (!readMarker(br)) {
//					break;
//				}
//
//				// Number of Questions
//				int n = Integer.valueOf(br.readLine());
//				if (!readMarker(br)) {
//					break;
//				}
//
//				if (n <= 0) {
//					return result;
//				}
//
//
//				for (int i = 0; i < n; i++) {
//
//					String sNumber = readUntilMarker(br);
//					number = Integer.valueOf(sNumber);
//
//					String question = readUntilMarker(br);
//					String option1 = readUntilMarker(br);
//					String option2 = readUntilMarker(br);
//					String option3 = readUntilMarker(br);
//					String option4 = readUntilMarker(br);
//
//					String sHasImage = readUntilMarker(br);
//					boolean hasImage = "Y".equals(sHasImage);
//
//					String image = null;
//					if (hasImage) {
//						File img = new File(dir, number + JPG);
//						byte[] originalArray = IOUtil.loadBytes(img);
//						String encodedString = Base64.encodeBytes(originalArray);
//						image = encodedString;
//					}
//
//					String sAnswer = readUntilMarker(br);
//					int answer = Integer.valueOf(sAnswer);
//
//					String ip = readUntilMarker(br);
//
//					String owner = null;
//
//					// XXX We can't really use owner yet
//					/*
//					owner = readUntilMarker(br);
//
//					*/
//					if (owner == null) {
//						owner = TEACHER_NAME;
//					}
//
//					Question q = new Question(number, owner, ip, question, option1, option2,
//					option3, option4, answer, image);
//
//					result.add(q);
//				}
//
//			} catch (IOException e) {
//				Log.e(Constants.LOG_CATEGORY, "JSON File Load Error: ", e);
//			} finally {
//				IOUtil.silentClose(br);
//			}
//
//		  } while (false);
//        }
//
//		return result;
//	}

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
