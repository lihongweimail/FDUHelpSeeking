package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;


public class NewTopicInfoRec {
	
	//用于记录话题以及话题相关的网页：  话题ID  话题名字 （话题相关的webpagesID）  所属查询ID 点击话题时间 话题URL数量 
	String topicId="";
	String topicName="";
	String searchId="";
	Timestamp clickTopicTime;
	int URLcount=0;
	
	
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	public Timestamp getClickTopicTime() {
		return clickTopicTime;
	}
	public void setClickTopicTime(Timestamp clickTopicTime) {
		this.clickTopicTime = clickTopicTime;
	}
	public int getURLcount() {
		return URLcount;
	}
	public void setURLcount(int uRLcount) {
		URLcount = uRLcount;
	}
	
	
	
	

}
