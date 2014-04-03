package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class ActionQueue {
	int actionListSize=0; //整个队列大小
	
	List<ActionCache> actionList=new ArrayList<ActionCache>();
	int currentWindowSize=0;//当前窗口的大小，windowRight-windowLeft<=actionSlideWindowLimitSize
	private static int actionSlideWindowLimitSize=10;   //这个值今后要在配置中获取。

	int windowLeft=0;
	int windowRight=0;
	
	public int getCurrentWindowSize() {
		return currentWindowSize;
	}
	public void setCurrentWindowSize(int currentWindowSize) {
		this.currentWindowSize = currentWindowSize;
	}
	
	public int  moveUpWindow()
	{
		int removeActionID=-1;
		windowRight=windowRight+1;
	
		if (windowRight-windowLeft>=currentWindowSize) {
			removeActionID=actionList.get(windowLeft).getActionID();
			windowLeft=windowLeft+1;
		}
		return removeActionID;
	}
	

	
  public int  addSizeReturnRemoveActionID()
  {
	  
	  actionListSize=actionListSize+1;
	  
	  return moveUpWindow();
  }
  

  public int getActionListSize() {
		return actionListSize;
	}

public int getWindowLeft() {
	return windowLeft;
}

public int getWindowRight() {
	return windowRight;
}

public List<ActionCache> getActionList() {
	return actionList;
}
public void setActionListSize(int actionListSize) {
	this.actionListSize = actionListSize;
}
public static int getActionSlideWindowLimitSize() {
	return actionSlideWindowLimitSize;
}
public static void setActionSlideWindowLimitSize(int actionSlideWindowLimitSize) {
	ActionQueue.actionSlideWindowLimitSize = actionSlideWindowLimitSize;
}
  


}
