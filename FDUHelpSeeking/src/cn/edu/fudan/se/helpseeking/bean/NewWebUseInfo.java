package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;



public class NewWebUseInfo {
	//记录网页的使用信息
	String useWebId="";
	String topicId="";
	String topicName="";
	String webPagesId="";
	String webURL="";
	Timestamp openTime;
	
	Timestamp closeTime;
	
	
	

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getWebPagesId() {
		return webPagesId;
	}

	public void setWebPagesId(String webPagesId) {
		this.webPagesId = webPagesId;
	}

	public String getUseWebId() {
		return useWebId;
	}

	public void setUseWebId(String useWebId) {
		this.useWebId = useWebId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp timestamp) {
		this.openTime = timestamp;
	}

	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}
	
	
	
	
	

}
