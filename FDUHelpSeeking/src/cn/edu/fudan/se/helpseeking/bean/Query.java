package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;

public class Query {	
	List<KeyWord> queryKeyWords=null;
	QueryLevel queryLevel=QueryLevel.Other;         
	// 这组关键词的重要程度： “high” 表示当前动作触发的查询， “middle”表示上下文条件下触发的
	
	int inforID;
	Timestamp time;
	String candidateKeywords=null;  //候选中的前300个词
	String useKeywords=null;

	//查询编号：
//	P+inforID人工的查询 ; 
//	A+qlist index+HIGH+inforID  A+qlist index+MIDDLE+inforID,...等  A+1+queryLevel+inforID
//	表示自动化查询附有它们的在查询上的索引和等级'
	String searchID;  
	long costtime;//消耗的时间 秒为单位
	
	boolean isbyuser=false;  //表示默认为自动检索
	
	int mode=0;//0 是初值不处理      1 是每个动作可能产生的查询，需要对query改写                           2 为了自动检索准备的查询不能改写，表示新增      

	
	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
	}


	public void makeCandidateKeywords(List<KeyWord> currentKeywordsList, int limit)
	{
		setCandidateKeywords(null);
		
		String mycandidateKeyWords=null;
		if (currentKeywordsList!=null) {
			for (int i = 0; i < currentKeywordsList.size(); i++) {
				if (i==0) {
					mycandidateKeyWords=currentKeywordsList.get(i).getKeywordName();
				}else
				{
					mycandidateKeyWords=mycandidateKeyWords+";"+currentKeywordsList.get(i).getKeywordName();
				}
				if (i==limit) {
					break;
				}
			}

		}
		
		setCandidateKeywords(mycandidateKeyWords);
		
	}
	
	
	public boolean isIsbyuser() {
		return isbyuser;
	}
	public void setIsbyuser(boolean isbyuser) {
		this.isbyuser = isbyuser;
	}
	public int getInforID() {
		return inforID;
	}
	public void setInforID(int inforID) {
		this.inforID = inforID;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getCandidateKeywords() {
		return candidateKeywords;
	}
	public void setCandidateKeywords(String candidateKeywords) {
		this.candidateKeywords = candidateKeywords;
	}
	public String getUseKeywords() {
		return useKeywords;
	}
	public void setUseKeywords(String useKeywords) {
		this.useKeywords = useKeywords;
	}
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	public long getCosttime() {
		return costtime;
	}
	public void setCosttime(long costtime) {
		this.costtime = costtime;
	}
	public List<KeyWord> getQueryKeyWords() {
		return queryKeyWords;
	}
	public void setQueryKeyWords(List<KeyWord> queryKeyWords) {
		this.queryKeyWords = queryKeyWords;
	}
	public QueryLevel getQueryLevel() {
		return queryLevel;
	}
	public void setQueryLevel(QueryLevel queryLevel) {
		this.queryLevel = queryLevel;
	}

	
	
}
