package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class ActionSlideWindow {
	int actionListSize=0;
	
	List<Action> actionList=new ArrayList<Action>();
	
	int windowSize=0;
	int windowLeft=0;
	int windowRight=0;
	
	
	
	
  public void addSize()
  {
	  actionListSize=actionListSize+1;
  }
  public void reduceSize()
  {
	  actionListSize=actionListSize-1;
  }
  public int getActionListSize() {
		return actionListSize;
	}
  
	public List<Action> getActionList() {
		return actionList;
	}
	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}

}
