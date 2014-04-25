package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;



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

ActionQueue1 actions = new ActionQueue1(2000);// 包含滑动窗口，一个足够大的窗口，当实际窗口大于这个值时 调整窗口为这个值
 
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
	
	// 在 problem view 更新 时  Attention动作类型  动作名称"Problem View Changed"
	if (information.getAction().getActionKind()==Kind.ATTENTION   && information.getAction().getActionName().equals("Problem View Changed")) {
		actions.resizeActionSlideWindowLimitSize();
		//滑动窗口减半
	}
	
	
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

//	用于处理当编辑状态下，一些程序元素删除；或类修改后保存该类的文件后，该类编译通过
//	后在problem view中没有该类的编译错误和警告信息（再考虑是否只是没有错误信息）
//	在光标中有记录该类的类名文件
public void removeEditCodeAsDeleteOrProblemViewChange(String classFileName) {
	//TODO   还有待实现
	
	classFileName=classFileName.trim();
	for (int i = 0; i < informations.size(); i++) {
		DebugCode dc=informations.get(i).getInformation().getDebugCode();
		EditCode ec=informations.get(i).getInformation().getEditCode();
		if (dc!=null) {
			if (dc.getBreakpoint()!=null) {
				
				if (dc.getBreakpoint().getFileName().trim().equals(classFileName)) {
					informations.get(i).getInformation().setDebugCode(null);
					
				}
			}
		}
		
		if (ec!=null) {
			if (ec.getCursor()!=null) {
				
				if (ec.getCursor().getFileName().trim().equals(classFileName)) {
					informations.get(i).getInformation().setEditCode(null);;
					
				}
			}
		}	

		
		
		}
	



}

public ActionQueue1 getActions() {
	return actions;
}

public void setActions(ActionQueue1 actions) {
	this.actions = actions;
}

public ConsoleInformationList getConsoles() {
	return consoles;
}

public void setConsoles(ConsoleInformationList consoles) {
	this.consoles = consoles;
}

public int getConsolesSize() {
	return consolesSize;
}

public void setConsolesSize(int consolesSize) {
	this.consolesSize = consolesSize;
}

public ClassModel getCurrentClassModel() {
	return currentClassModel;
}

public void setCurrentClassModel(ClassModel currentClassModel) {
	this.currentClassModel = currentClassModel;
}

public int getCurrentID() {
	return currentID;
}

public void setCurrentID(int currentID) {
	this.currentID = currentID;
}

public List<KeyWord> getCurrentKeywordsList() {
	return currentKeywordsList;
}

public void setCurrentKeywordsList(List<KeyWord> currentKeywordsList) {
	this.currentKeywordsList = currentKeywordsList;
}

public List<InformationQueue> getInformations() {
	return informations;
}

public void setInformations(List<InformationQueue> informations) {
	this.informations = informations;
}

public ProblemInformationList getProblems() {
	return problems;
}

public void setProblems(ProblemInformationList problems) {
	this.problems = problems;
}

public int getProblemsSize() {
	return problemsSize;
}

public void setProblemsSize(int problemsSize) {
	this.problemsSize = problemsSize;
}




}
