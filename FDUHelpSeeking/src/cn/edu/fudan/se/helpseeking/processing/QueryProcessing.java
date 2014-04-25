package cn.edu.fudan.se.helpseeking.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.QueryList;
import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class QueryProcessing implements Observer {

	Observable ober;
	QueryProcessing(Observable ob)
	{
		this.ober=ob;
	}

	public void dotest(String queryText)
	{
		// 以下是测试 查询词 变更的监听器用的，使用后可以清除
		QueryList queryList=QueryList.getInstance();
		Query query=new Query();
		 List<KeyWord> kwl=new ArrayList<KeyWord>();
		 List <String> test =CommUtil.arrayToList(queryText.split("[ ]"));
		 for (String string : test) {
			KeyWord kw=new KeyWord();
			kw.setKeywordName(string);
			//kw.setTagName("API");
			kwl.add(kw);
		}
		 query.setQueryKeyWords(kwl);
		 List<Query> ql=new ArrayList<Query>();
		 ql.add(query);
		 queryList.setQuerys(ql);
		 
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		//使用观察者模式，在query值发生变更时  处理 各个查询！！0425
		if (o instanceof QueryList) {
				System.out.println("hello observe change!");
		}
	
		
		
		
	}
	
}
