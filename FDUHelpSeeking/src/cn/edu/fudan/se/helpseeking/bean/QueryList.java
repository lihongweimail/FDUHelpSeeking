package cn.edu.fudan.se.helpseeking.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;



public class QueryList  extends Observable {

	private List <Query> querys;

	QueryList()
	{
		querys=new ArrayList<Query>();
	}
	
	public List<Query> getQuerys() {
		return querys;
	}
	
public void setQuerys(List<Query> newQuerys) {
		
		this.querys = newQuerys;
		this.querysChanged();
	
	}

public void querysChanged()
{
	this.setChanged();
	notifyObservers();
}
	
public static QueryList getInstance() {
	return Singleton.queryList;
}

private static class Singleton {
	 static QueryList queryList = new QueryList();
}
}
