package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView;
import cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView;




public class QueryList extends Observable {

	private List<Query> querys;
 	
	private QueryList() {
		querys = new ArrayList<Query>();
	}

	
	public int findIndexofModeOne() {
		int index=-1;
		if (querys!=null && querys.size()>0) {
			
		for (int i = 0; i < querys.size(); i++) {
			Query q=querys.get(i);
			if (q.getMode()==1) {
				index=i;
				break;
			}
			
			
		}
			
			
		}
		
		
		return index;
	}
	
	public void clear() {
		
		this.querys= new ArrayList<Query>();
	}

	public List<Query> getQuerys() {
		return querys;
	}

	public void setQuerys(List<Query> newQuerys) {
		this.querys = newQuerys;
		
	}
	
	


	public static QueryList getInstance() {
		return Singleton.queryList;
	}

	private static class Singleton {
		static QueryList queryList = new QueryList();
	}
}
