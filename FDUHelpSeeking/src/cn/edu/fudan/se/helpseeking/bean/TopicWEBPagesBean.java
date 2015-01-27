package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

public class TopicWEBPagesBean {
	
	String topicName;
	List<WEBPageBean> pages=new ArrayList<WEBPageBean>();
	List<FudanTopicWithWordsListBean>  myfudanTopicWords=new ArrayList<FudanTopicWithWordsListBean>();
	
	
	
	
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public List<WEBPageBean> getPages() {
		return pages;
	}
	public void setPages(List<WEBPageBean> pages) {
		this.pages = pages;
	}
	public List<FudanTopicWithWordsListBean> getMyfudanTopicWords() {
		return myfudanTopicWords;
	}
	public void setMyfudanTopicWords(
			List<FudanTopicWithWordsListBean> myfudanTopicWords) {
		this.myfudanTopicWords = myfudanTopicWords;
	}
	
	

}
