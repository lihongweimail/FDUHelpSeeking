package cn.edu.fudan.se.helpseeking.bean;

public class CxKeyPair {
	
	String cx;
	String key;
	String cseName;
	String projectID;
	
	
	
	public String getCseName() {
		return cseName;
	}
	public void setCseName(String cseName) {
		this.cseName = cseName;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getCx() {
		return cx;
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String toString() {
		String toString;
		toString="\n[cx="+getCx()+"]"+"\n[key="+getKey()+"]";
		
		
		return toString;
	}

}
