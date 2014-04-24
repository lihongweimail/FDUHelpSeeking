package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;

public class Query {
	
	List<KeyWord> queryKeyWords=null;
	QueryLevel queryLevel=QueryLevel.Other;         // 这组关键词的重要程度： “high” 表示当前动作触发的查询， “middle”表示上下文条件下触发的  
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
