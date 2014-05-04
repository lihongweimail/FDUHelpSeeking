package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;

public class UseResultsRecord {
	
	String title;
	String url;
	String content;
	String searchID;
	int totallist;
	int position;
	String type;	
	String solutionID;
	
	Timestamp time;
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	public int getTotallist() {
		return totallist;
	}
	public void setTotallist(int totallist) {
		this.totallist = totallist;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSolutionID() {
		return solutionID;
	}
	public void setSolutionID(String solutionID) {
		this.solutionID = solutionID;
	}
	
	

}
