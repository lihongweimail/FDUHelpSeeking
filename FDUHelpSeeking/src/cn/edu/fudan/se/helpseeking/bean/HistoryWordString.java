package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class HistoryWordString {
	List<String> wordlist=new ArrayList<>();
	int id;
	public List<String> getWordlist() {
		return wordlist;
	}
	public void setWordlist(List<String> wordlist) {
		this.wordlist = wordlist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
