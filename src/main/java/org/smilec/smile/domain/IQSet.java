package org.smilec.smile.domain;

public class IQSet {

	private String id;
	private String key;
	private String sessionTitle;
	private String teacherName;
	private String groupName;
	
	public IQSet() {
		// TODO Auto-generated constructor stub
	}
	
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
