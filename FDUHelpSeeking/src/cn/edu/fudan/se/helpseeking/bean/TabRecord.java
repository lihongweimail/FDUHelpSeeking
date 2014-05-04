package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.INTERNAL;

public class TabRecord {
	
	long totalTime;
	String tabName;
	String urlRecord;
	Timestamp starTimestamp;
	Timestamp endTimestamp;
	String solutionID;// 唯一编号 actionid + SERCHTYPE+SEARCHID+POSTION
	String searchID;
	int currentActionID;
	int searchResultsListPostion;
	int totallistnumber;
	List<Long> useTimeList=new ArrayList<Long>();
	String titleRecord;  //真实标题名
	String contentRecord;
	
	String searchType;  // auto or  manual
	
	int reOpenIndex; //url相同的再次打开  分开记录 但是记录下这个 
	
	
	
	
	
	
	public int getReOpenIndex() {
		return reOpenIndex;
	}
	public void setReOpenIndex(int reOpenIndex) {
		this.reOpenIndex = reOpenIndex;
	}
	public int getTotallistnumber() {
		return totallistnumber;
	}
	public void setTotallistnumber(int totallistnumber) {
		this.totallistnumber = totallistnumber;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
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
