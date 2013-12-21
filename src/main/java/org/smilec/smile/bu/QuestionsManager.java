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

	private static final String QUESTIONS_DIR = Constants.APP_ID;
	private static final String MARKER = "_@JSQ%_";

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
	
	public static void resetListOfDeletedQuestions(Context context) { // XXX REMOVE
		
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
	 * XXX: Why do we need to store anything to disk?  REMOVE
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
				System.out.println(">"+inputString); // XXX: to remove
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return stringBuffer.toString();
	}
	
	/**
	   XXX REMOVE
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
	
	/** XXX REMOVE
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

	public boolean readMarker(BufferedReader br) throws IOException {
		String s = br.readLine();
		return s != null && s.equals(MARKER);
	}

	public String readUntilMarker(BufferedReader in) throws IOException {

		String s = "";
		boolean first = true;
		while (true) {

			String t = in.readLine();
			
			if (t == null) 			{ return s; }
			if (t.equals(MARKER)) 	{ return s; }
			if (first) 				{ s += t; } 
			else 					{ s += "\n" + t; }
		}

	}

}
