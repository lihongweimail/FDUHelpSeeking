package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class HistoryUrlSearch {
	
	int id;
	String topicName;
	List<WEBPageBean> webpageList;
	List<KeyWord> searchList;
	
	
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<WEBPageBean> getWebpageList() {
		return webpageList;
	}
	public void setWebpageList(List<WEBPageBean> webpageList) {
		this.webpageList = webpageList;
	}
	public List<KeyWord> getSearchList() {
		return searchList;
	}
	public void setSearchList(List<KeyWord> searchList) {
		this.searchList = searchList;
	}
	
	
	

}
