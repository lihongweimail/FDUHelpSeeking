package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;

public class KeyWord {

	String keywordName=null;
	String tagName="Other";
	double  score=1.0;
	double weightOne=1.0;//基本权重
	double  weightTwo=1.0;//附加权重
	
	String searchID;   //从候选的推荐组号  一般是U开头
	int positionInRecommandlist;
	int positionInUseString;
	boolean isRecommand=true;
	
	String lastSearchID;//最后 真正 使用search button时的searchID    一般是A开头
	
	Timestamp times;
	
	
	
	
	public Timestamp getTimes() {
		return times;
	}
	public void setTimes(Timestamp times) {
		this.times = times;
	}
	public boolean isRecommand() {
		return isRecommand;
	}
	public void setRecommand(boolean isRecommand) {
		this.isRecommand = isRecommand;
	}
	public int getPositionInUseString() {
		return positionInUseString;
	}
	public void setPositionInUseString(int positionInUseString) {
		this.positionInUseString = positionInUseString;
	}
	public String getLastSearchID() {
		return lastSearchID;
	}
	public void setLastSearchID(String lastSearchID) {
		this.lastSearchID = lastSearchID;
	}
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	public int getPositionInRecommandlist() {
		return positionInRecommandlist;
	}
	public void setPositionInRecommandlist(int positionInRecommandlist) {
		this.positionInRecommandlist = positionInRecommandlist;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public double getWeightTwo() {
		return weightTwo;
	}
	public void setWeightTwo(double weightTwo) {
		this.weightTwo = weightTwo;
	}
	public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getWeightOne() {
		return weightOne;
	}
	public void setWeightOne(double weightOne) {
		this.weightOne = weightOne;
	} 
	
	
}
