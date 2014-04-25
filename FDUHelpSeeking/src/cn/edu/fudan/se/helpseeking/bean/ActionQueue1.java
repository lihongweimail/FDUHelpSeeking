package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class ActionQueue1 {
	int actionListSize=0; //整个队列大小
	
	List<ActionCache> actionList=new ArrayList<ActionCache>();
	int currentWindowSize=10;//当前窗口的大小，windowRight-windowLeft<=actionSlideWindowLimitSize
	private static int actionSlideWindowLimitSize=0;   //这个值今后要在配置中获取。

	int windowLeft=0;
	int windowRight=0;
	
	ActionQueue1()
	{
		super();
	}
	
	ActionQueue1(int windowSize)
	{
		this.currentWindowSize=windowSize;
	}
	
	public int getCurrentWindowSize() {
		return currentWindowSize;
	}
	public void setCurrentWindowSize(int currentWindowSize) {
		this.currentWindowSize = currentWindowSize;
	}
	
	public void resizeActionSlideWindowLimitSize()
	{
		int oldSize=actionSlideWindowLimitSize;
		int newSize=actionSlideWindowLimitSize/2;
		newSize=newSize<=(actionList.size()/2)? newSize : (actionList.size()/2);
		actionSlideWindowLimitSize=newSize;
		for (int i = 0; i <oldSize-newSize; i++) {
			actionList.remove(0);
			windowLeft=windowLeft+1;
			actionListSize=actionListSize-1;
		}
			
	}
	
	public int  moveUpWindow()
	{
		int removeActionID=-1;
		windowRight=windowRight+1;
		actionSlideWindowLimitSize=actionSlideWindowLimitSize+1;
		if (actionSlideWindowLimitSize>=currentWindowSize) {
			actionSlideWindowLimitSize=currentWindowSize;
		}
	
		if (windowRight-windowLeft>=currentWindowSize) {
			removeActionID=actionList.get(0).getActionID();
			windowLeft=windowLeft+1;
		   
			for (int i = 0; i < actionList.size(); i++) {
				if (actionList.get(i).getActionID()== removeActionID) {
					actionList.remove(i);
				    break;
				}
				}
			
			actionListSize=actionListSize-1;
			
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
	ActionQueue1.actionSlideWindowLimitSize = actionSlideWindowLimitSize;
}
  


}
