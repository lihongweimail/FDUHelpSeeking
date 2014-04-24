package cn.edu.fudan.se.helpseeking.bean;

public class KeyWord {

	String keywordName=null;
	String tagName=null;
	double  score=1.0;
	double weightOne=1.0;//基本权重
	double  weightTwo=1.0;//附加权重
	
	
	
	
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
