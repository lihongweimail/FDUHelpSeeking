package cn.edu.fudan.se.helpseeking.bean;

import java.awt.RenderingHints.Key;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NewQueryRec {
	
	int ids;
	Timestamp starttime = null;
	String user=System.getProperties().getProperty("user.name");
	//keywords: name , type (api or exception , other ,error)(keyword.gettagname())  
	List<KeyWord> querywords=new ArrayList<KeyWord>(); //finnaly query   	// one is select words,  one is programmer input words  and the other exception or  api words from tagname:  please tag them

	List<KeyWord> snapshotWords=new ArrayList<KeyWord>();//current all keywords
	List<KeyWord> foamtreeWords=new ArrayList<KeyWord>();//words in foamtree
	List<KeyWord> selectFromFoamtreeWords=new ArrayList<KeyWord>();//or means not programmer input words
	
	
	
	
	
	public Timestamp getStartTime() {
		return starttime;
	}
	public int getIds() {
		return ids;
	}
	public void setIds(int ids) {
		this.ids = ids;
	}
	public void setStartTime(Timestamp time) {
		this.starttime = time;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<KeyWord> getQuerywords() {
		return querywords;
	}
	public void setQuerywords(List<KeyWord> querywords) {
		this.querywords = querywords;
	}
	public List<KeyWord> getSnapshotWords() {
		return snapshotWords;
	}
	public void setSnapshotWords(List<KeyWord> snapshotWords) {
		this.snapshotWords = snapshotWords;
	}
	public List<KeyWord> getFoamtreeWords() {
		return foamtreeWords;
	}
	public void setFoamtreeWords(List<KeyWord> foamtreeWords) {
		this.foamtreeWords = foamtreeWords;
	}
	public List<KeyWord> getSelectFromFoamtreeWords() {
		return selectFromFoamtreeWords;
	}
	public void setSelectFromFoamtreeWords(List<KeyWord> selectFromFoamtreeWords) {
		this.selectFromFoamtreeWords = selectFromFoamtreeWords;
	}
	
	
	
	
	
	

}
