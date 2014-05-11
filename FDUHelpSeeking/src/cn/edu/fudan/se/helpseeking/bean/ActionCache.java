package cn.edu.fudan.se.helpseeking.bean;

public class ActionCache {
	int actionID;
	Action action;
	int distence=1;
   public int getActionID() {
		return actionID;
	}
	public void setActionID(int actionID) {
		this.actionID = actionID;
	}


	public int getDistence() {
		return distence;
	}
	public void setDistence(int distence) {
		this.distence = distence;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}


}
