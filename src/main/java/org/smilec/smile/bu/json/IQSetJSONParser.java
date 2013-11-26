package org.smilec.smile.bu.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smilec.smile.domain.IQSet;
import org.smilec.smile.domain.Question;

public class IQSetJSONParser {
	
	private static final String ALL_THE_IQSETS = "rows";
    private static final String NUMBER_OF_IQSETS = "total_rows";
    private static final String ID_OF_IQSET = "id";
    private static final String KEY_OF_IQSET = "key";
    private static final String VALUE_OF_IQSET = "value";
    
    
    public static final boolean rowsExist(JSONObject object) {
    	
    	return object.optInt(NUMBER_OF_IQSETS) != 0? true : false;
    }
    
    /**
     * Used to parse the JSON request when we need to fill the ListView of all IQSet  #UsingPreparedQuestions
     * 
     */
    public static final List<IQSet> parseListOfIQSet(JSONObject object) {

    	List<IQSet> iqsets = new ArrayList<IQSet>();
    	
    	JSONArray rows = object.optJSONArray(ALL_THE_IQSETS);
    	
    	for(int i=0; i<rows.length();) {
    		
    		try {
				JSONObject row = rows.getJSONObject(i++);
				JSONArray value = row.getJSONArray(VALUE_OF_IQSET); 
				
				IQSet iqset = new IQSet(
						row.getString(ID_OF_IQSET),
						row.getString(KEY_OF_IQSET),
						value.getString(0),
						value.getString(1),
						value.getString(2)
						);
				
				iqsets.add(iqset);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	return iqsets;
    }
    
    /**
     * Used to return the iqset ID when the teacher select a pool of questions  #UsingPreparedQuestions
     * 
     */
    public static final String getIdIQSetByPosition(JSONObject object, int position) {
    	
    	JSONArray rows = object.optJSONArray(ALL_THE_IQSETS);
    	
    	try {
			JSONObject row = rows.getJSONObject(position);
			return row.getString(ID_OF_IQSET);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return "IQSET NOT FOUND";
    }

    /**
     * Used to retrieve all the questions of a IQSet before starting GeneralActivity  #UsingPreparedQuestions
     * 
     */
    public static final Collection<Question> parseIQSet(JSONObject object) {
    	
    	Collection<Question> questions = new ArrayList<Question>();
    	JSONArray rows = object.optJSONArray("iqdata");
    	
    	for(int i=0; i<rows.length();i++) {
    		
    		try {
				JSONObject row = rows.getJSONObject(i);
				
				// TODO: Careful! imageUrl field is always empty when using prepared questions!
				Question q = new Question(i,
						row.getString("NAME"),
						row.getString("IP"),
						row.getString("Q"),
						row.getString("O1"),
						row.getString("O2"),
						row.getString("O3"),
						row.getString("O4"),
						row.getInt("A"),
						"");
				questions.add(q);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	return questions;
    }
}
