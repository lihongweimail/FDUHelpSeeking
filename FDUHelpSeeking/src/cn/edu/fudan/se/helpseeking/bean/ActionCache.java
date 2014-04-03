package cn.edu.fudan.se.helpseeking.bean;

public class ActionCache {
	
	Action action;
	//初值为-1；不指向任何资源
	int informationID=-1;
	int debugCodeID=-1;
	int editorCodeID=-1;
	int ideOutputID=-1;
	int explorerRelatedID=-1;
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public int getInformationID() {
		return informationID;
	}
	public void setInformationID(int informationID) {
		this.informationID = informationID;
	}
	public int getDebugCodeID() {
		return debugCodeID;
	}
	public void setDebugCodeID(int debugCodeID) {
		this.debugCodeID = debugCodeID;
	}
	public int getEditorCodeID() {
		return editorCodeID;
	}
	public void setEditorCodeID(int editorCodeID) {
		this.editorCodeID = editorCodeID;
	}
	public int getIdeOutputID() {
		return ideOutputID;
	}
	public void setIdeOutputID(int ideOutputID) {
		this.ideOutputID = ideOutputID;
	}
	public int getExplorerRelatedID() {
		return explorerRelatedID;
	}
	public void setExplorerRelatedID(int explorerRelatedID) {
		this.explorerRelatedID = explorerRelatedID;
	}
	
	
	

}
