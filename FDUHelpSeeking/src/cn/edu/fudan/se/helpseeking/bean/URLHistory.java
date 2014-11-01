package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class URLHistory {
	
	String LDATopicStr;
	List<LDAURLbean> URLlist;
	int HistoryId;
	double weight;
	
	
	
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getLDATopicStr() {
		return LDATopicStr;
	}
	public void setLDATopicStr(String lDATopicStr) {
		LDATopicStr = lDATopicStr;
	}
	public List<LDAURLbean> getURLlist() {
		return URLlist;
	}
	public void setURLlist(List<LDAURLbean> uRLlist) {
		URLlist = uRLlist;
	}
	public int getHistoryId() {
		return HistoryId;
	}
	public void setHistoryId(int historyId) {
		HistoryId = historyId;
	}
	
	

}
