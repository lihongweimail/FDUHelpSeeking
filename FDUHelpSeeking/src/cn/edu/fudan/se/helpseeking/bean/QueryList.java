package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView;

public class QueryList extends Observable {

	private List<Query> querys;

	private QueryList() {
		querys = new ArrayList<Query>();
	}

	public List<Query> getQuerys() {
		return querys;
	}

	public void setQuerys(List<Query> newQuerys) {
		this.querys = newQuerys;
		HelpSeekingSearchView.searchQueryList();
	}

	public static QueryList getInstance() {
		return Singleton.queryList;
	}

	private static class Singleton {
		static QueryList queryList = new QueryList();
	}
}
