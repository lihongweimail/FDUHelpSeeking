package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class AroundWordsBean {

	String  keywords;
	List<String> arourndwords=new ArrayList<String>();
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public List<String> getArourndwords() {
		return arourndwords;
	}
	public void setArourndwords(List<String> arourndwords) {
		this.arourndwords = arourndwords;
	}
	
	
	
}
