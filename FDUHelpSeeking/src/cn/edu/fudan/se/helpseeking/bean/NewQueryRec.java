package cn.edu.fudan.se.helpseeking.bean;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class NewQueryRec {
	
	int ids;
	
	String user=System.getProperties().getProperty("user.name");
	//keywords: name , type (api or exception , other ,error)(keyword.gettagname())  
	
	String QueryId="";//检索的编号
	List<KeyWord> querywords=new ArrayList<KeyWord>(); //finnaly query   	// one is select words,  one is programmer input words  and the other exception or  api words from tagname:  please tag them
	List<KeyWord> selectFromFoamtreeWords=new ArrayList<KeyWord>();//or means not programmer input words
	List<KeyWord> inputWords=new ArrayList<KeyWord>(); //输入的词
	List<KeyWord> apiKeyWordsinQuery=new ArrayList<KeyWord>();//tagname wei API 的词
	List<KeyWord> exceptionKeyWordsinQuery=new ArrayList<KeyWord>();
	List<KeyWord> errorKeyWordsinQuery=new ArrayList<KeyWord>();
	List<KeyWord> otherKeyWordsinQuery=new ArrayList<KeyWord>();
	Timestamp starttime = null;//这次的时间
	Timestamp pretimepoint=null;//上一个查询的时间，初始为启动时间
	List<KeyWord> snapshotWords=new ArrayList<KeyWord>();//current all keywords
	List<KeyWord> foamtreeWords=new ArrayList<KeyWord>();//words in foamtree
	
	int countQueryKeywords=0;
	
	
	
	public int getCountQueryKeywords() {
		return countQueryKeywords;
	}

	public void setCountQueryKeywords() {
		if (querywords==null) {
			countQueryKeywords=0;
		}
		 countQueryKeywords=querywords.size(); 
	}



	public String getStringsformKeywords(List<KeyWord> fromKeyWords) {
		if (fromKeyWords==null) {
			return "";
		}
		if (fromKeyWords.size()==0) {
			return "";
		}
		String result="";
		
		for (int i = 0; i < fromKeyWords.size(); i++) {
			String tempstr=CommUtil.getNewSimpleWords(fromKeyWords.get(i).getKeywordName());
			
			if (result.equals("")) {
				result=tempstr;
			}else
			result=result+"###"+tempstr;
		}
		
		return result;
	}
	
	
	
	
	
	
	
	public String getQueryId() {
		return QueryId;
	}
	public void setQueryId(String queryId) {
		QueryId = queryId;
	}
	public List<KeyWord> getInputWords() {
		return inputWords;
	}
	public void setInputWords(List<KeyWord> inputWords) {
		this.inputWords = inputWords;
	}
	public List<KeyWord> getApiKeyWordsinQuery() {
		return apiKeyWordsinQuery;
	}
	public void setApiKeyWordsinQuery(List<KeyWord> apiKeyWordsinQuery) {
		this.apiKeyWordsinQuery = apiKeyWordsinQuery;
	}
	public List<KeyWord> getExceptionKeyWordsinQuery() {
		return exceptionKeyWordsinQuery;
	}
	public void setExceptionKeyWordsinQuery(List<KeyWord> exceptionKeyWordsinQuery) {
		this.exceptionKeyWordsinQuery = exceptionKeyWordsinQuery;
	}
	public List<KeyWord> getErrorKeyWordsinQuery() {
		return errorKeyWordsinQuery;
	}
	public void setErrorKeyWordsinQuery(List<KeyWord> errorKeyWordsinQuery) {
		this.errorKeyWordsinQuery = errorKeyWordsinQuery;
	}
	public List<KeyWord> getOtherKeyWordsinQuery() {
		return otherKeyWordsinQuery;
	}
	public void setOtherKeyWordsinQuery(List<KeyWord> otherKeyWordsinQuery) {
		this.otherKeyWordsinQuery = otherKeyWordsinQuery;
	}
	public Timestamp getStarttime() {
		return starttime;
	}
	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}
	public Timestamp getPretimepoint() {
		return pretimepoint;
	}
	public void setPretimepoint(Timestamp pretimepoint) {
		this.pretimepoint = pretimepoint;
	}
	
    public int getIds() {
		return ids;
	}
	public void setIds(int ids) {
		this.ids = ids;
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
