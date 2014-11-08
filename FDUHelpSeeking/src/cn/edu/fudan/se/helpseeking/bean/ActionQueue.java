package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class ActionQueue {
	private static int actionSlideWindowLimitSize=0;   //这个值今后要在配置中获取。 暂时未使用！！！
	
	public static int getActionSlideWindowLimitSize() {
		return actionSlideWindowLimitSize;
	}
	public static void setActionSlideWindowLimitSize(int actionSlideWindowLimitSize) {
		ActionQueue.actionSlideWindowLimitSize = actionSlideWindowLimitSize;
	}
	List<ActionCache> actionList=new ArrayList<ActionCache>();

	int actionListSize=0; //整个队列大小
	int currentWindowSize=Basic.SLIDE_WINDOW_SIZE;//当前窗口的大小，windowRight-windowLeft<=actionSlideWindowLimitSize
	
	int windowLeft=0;
	
	int windowRight=0;
	
	ActionQueue()
	{
		super();
	}
	ActionQueue(int windowSize)
	{
		this.currentWindowSize=windowSize;
	}
	
	public int  addSizeReturnRemoveActionID()
	  {
		  
		  actionListSize=actionListSize+1;
		  
		  return moveUpWindow();
	  }
	
	//使用ACTIONID号找到 动作的缓存信息
public ActionCache getActionCachewithActionID(int actionID)
{
	ActionCache ac=null;
	int size=actionList.size();
	for (int i = size-1; i >=0; i--) {
		if (actionList.get(i).getActionID()==actionID) {
			ac=actionList.get(i);
			break;
		}
	}
	
	
	return ac;
}
  public List<ActionCache> getActionList() {
	return actionList;
}
  

  public int getActionListSize() {
		return actionListSize;
	}

public int getCurrentWindowSize() {
	return currentWindowSize;
}

public int getWindowLeft() {
	return windowLeft;
}

public int getWindowRight() {
	return windowRight;
}
public int  moveUpWindow()
{
	int removeActionID=-1;
	windowRight=windowRight+1;
	

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
public void resetDistence() {

	int index=actionList.size();
	for (int i = 0; i < index; i++) {
		actionList.get(i).setDistence(index-i);
		
	}

	
	
}
public void setActionListSize(int actionListSize) {
	this.actionListSize = actionListSize;
}

public void setCurrentWindowSize(int currentWindowSize) {
	this.currentWindowSize = currentWindowSize;
}
  


}
