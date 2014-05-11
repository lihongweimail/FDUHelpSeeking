package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class WindowTotalKeyWords {
	
	int id;
	List<KeyWord> totallKeyWords=new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<KeyWord> getTotallKeyWords() {
		return totallKeyWords;
	}
	public void setTotallKeyWords(List<KeyWord> totallKeyWords) {
		this.totallKeyWords = totallKeyWords;
	}
	

	

}
