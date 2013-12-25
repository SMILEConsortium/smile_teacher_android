package org.smilec.smile.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class IQSet {

	private String id;
	private String key;
	private String sessionTitle;
	private String teacherName;
	private String groupName;
	
	public IQSet() { }
	
	public IQSet(String id, String key, String sessionTitle, String teacherName, String groupName) {
		
		this.id = id;
		this.key = key;
		this.sessionTitle = sessionTitle;
		this.teacherName = teacherName;
		this.groupName = groupName;
	}

	public String getId() { 
		return id;
	}
	public void setId(String id) { 
		this.id = id;
	}

	public String getDate() {
		
	    DateFormat formatDecrypt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	    DateFormat formatDesired = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
	    
		// key example: 2013-12-25T07:20:45.656Z
		String date_s = key.substring(0, Math.min(key.length(), 10));
	    
	    Date date = null;
	    
		try
			{ date = formatDecrypt.parse(date_s); } 
		catch (ParseException e)
			{ e.printStackTrace(); }  
	    
		System.out.println(date);
		
		return formatDesired.format(date);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) { 
		this.key = key;
	}

	public String getSessionTitle() { 
		return sessionTitle;
	}
	public void setSessionTitle(String sessionTitle) { 
		this.sessionTitle = sessionTitle;
	}

	public String getTeacherName() { 
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) { 
		this.groupName = groupName;
	}
}
