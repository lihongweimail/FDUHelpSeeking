package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;



public class Cache1 {

   private Cache1()
	{
		super();
	}
	
public static Cache1 getInstance() {
	return Singleton.cache1;
}

private static class Singleton {
	 static Cache1 cache1 = new Cache1();
}

ActionQueue actions = new ActionQueue();// 包含滑动窗口
ConsoleInformationList consoles = ConsoleInformationList.getInstance();
private int consolesSize=0;

ClassModel currentClassModel = new ClassModel();
int currentID = 0;
List<KeyWord> currentKeywordsList = new ArrayList<KeyWord>();
List<InformationQueue> informations = new ArrayList<InformationQueue>();

ProblemInformationList problems = ProblemInformationList.getInstance();

private int problemsSize=0;

public void addInformationToCache1(Information information,int actionID) {
	
	if (checkInformation(information))
	{
		System.out.println("refuse one more time dupplicate action");
		return;
	}
	

	InformationQueue infq = new InformationQueue();
	currentID =actionID;
	infq.setId(currentID);
	infq.setInformation(information);

	informations.add(infq);
	
	addActions(information.getAction(), currentID);
	
	
	
}

private void addActions(Action action, int id) {

	ActionCache a = new ActionCache();
	a.setAction(action);
	a.setActionID(id);
	actions.actionList.add(a);
	int removeActionID = actions.addSizeReturnRemoveActionID();
	if (removeActionID != -1) // 则说明动作已经放满了滑动窗口 ， 这时考虑不在窗口内的信息从信息集合中移除
	{
		// 最简单的策略直接删除， 后续需要可配置功能
		removeInformationNaive(removeActionID);

	}

}

private void removeInformationNaive(int removeActionID) {

	for (int i = 0; i < informations.size(); i++) {
		if (informations.get(i).getId() == removeActionID) {
			informations.remove(i);
		    break;
		}
		}


}

//	简单的连续相同不记录模式
private boolean checkInformation(Information information) {
	boolean result=false;
	if (actions.getActionList()==null) {
		//    	System.out.println("actons is null");
		return false;
	}
	Action newEnterAction=information.getAction();
	Action lastCacheAction=null;	
	for (ActionCache ac : actions.getActionList()) {
		if (ac.getActionID()==currentID) {
			lastCacheAction=ac.getAction();
		}
	}

	if (lastCacheAction==null) {
		//    	System.out.println("not find action");
		return false;
	}

	if (lastCacheAction.getActionKind().equals(newEnterAction.getActionKind())  
			&& lastCacheAction.getActionName().equals(newEnterAction.getActionName())
			&& lastCacheAction.getDescription().equals(newEnterAction.getDescription())) {
		result=true;
		//	      System.out.println("the same action: "+ newEnterAction.getActionKind().toString()+" : "+newEnterAction.getActionName());
	}else 
	{
		result=false; 
		//		System.out.println("the action is not same");
	}		
	return result;

}


}
