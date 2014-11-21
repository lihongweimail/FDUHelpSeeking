package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

public class TopicWEBPagesBean {
	
	String topicName;
	List<WEBPageBean> pages=new ArrayList<WEBPageBean>();
	List<FudanTopicWithWordsListBean>  myfudanTopicWords=new ArrayList<FudanTopicWithWordsListBean>();
	boolean selected=false;//初始时该值为假；标示所有URL都平等，当在topictree中选取toPIC后就设置为假true;撤销选择后又设置为假；显示时将值为真的结果放到前面显示
	
	
	
	
	
	
	
	
	
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
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
