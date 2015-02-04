package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;

import org.eclipse.debug.internal.ui.views.memory.SetPaddedStringPreferencePage;

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
	
	String keywordColor;   //使用Basic.cssColor中的字符串值  在获得生成的foamtree时，随机生成整数获得这个值，并将一次foamtree中的词的颜色值不重复
	String keywordColorName;
	

	String lastSearchID;//最后 真正 使用search button时的searchID    一般是A开头
	
	Timestamp times;
	
	int frequency=0;
	
	
	
	public String getKeywordColorName() {
		return keywordColorName;
	}
	public void setKeywordColorName(String keywordColorName) {
		this.keywordColorName = keywordColorName;
	}


	
	
	public String  getKeywordColor() {
		return keywordColor;
	}
	public void setKeywordColor(String  keywordColor) {
		this.keywordColor = keywordColor;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
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
	
	
	public void clonedata(KeyWord kw)
	{
		setFrequency(kw.getFrequency());
		setKeywordName(kw.getKeywordName());
		setLastSearchID(kw.getLastSearchID());
		setPositionInRecommandlist(kw.getPositionInRecommandlist());
		setPositionInUseString(kw.getPositionInUseString());
		
		setScore(kw.getScore());
		setSearchID(kw.getSearchID());
		setTagName(kw.getTagName());
		setTimes(kw.getTimes());
		setWeightOne(kw.getWeightOne());
		setWeightTwo(kw.getWeightTwo());
		
	}
	
}
