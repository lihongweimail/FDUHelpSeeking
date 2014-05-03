package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;
import java.util.List;

public class TabRecord {
	
	long totalTime;
	String tabName;
	String urlRecord;
	Timestamp starTimestamp;
	Timestamp endTimestamp;
	String solutionID;
	String searchID;
	int currentActionID;
	int searchResultsListPostion;
	List<Long> useTimeList;
	String titleRecord;
	String contentRecord;
	
	
	
	
	
	
	public String getTitleRecord() {
		return titleRecord;
	}
	public void setTitleRecord(String titleRecord) {
		this.titleRecord = titleRecord;
	}
	public String getContentRecord() {
		return contentRecord;
	}
	public void setContentRecord(String contentRecord) {
		this.contentRecord = contentRecord;
	}
	public int getCurrentActionID() {
		return currentActionID;
	}
	public void setCurrentActionID(int currentActionID) {
		this.currentActionID = currentActionID;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public String getUrlRecord() {
		return urlRecord;
	}
	public void setUrlRecord(String urlRecord) {
		this.urlRecord = urlRecord;
	}
	public Timestamp getStarTimestamp() {
		return starTimestamp;
	}
	public void setStarTimestamp(Timestamp starTimestamp) {
		this.starTimestamp = starTimestamp;
	}
	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	public String getSolutionID() {
		return solutionID;
	}
	public void setSolutionID(String solutionID) {
		this.solutionID = solutionID;
	}
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	public int getSearchResultsListPostion() {
		return searchResultsListPostion;
	}
	public void setSearchResultsListPostion(int searchResultsListPostion) {
		this.searchResultsListPostion = searchResultsListPostion;
	}
	public List<Long> getUseTimeList() {
		return useTimeList;
	}
	public void setUseTimeList(List<Long> useTimeList) {
		this.useTimeList = useTimeList;
	}
	
	
	
	
	

}
