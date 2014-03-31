package cn.edu.fudan.se.helpseeking.bean;

public class Information {
	
	String type;  //?? 信息类型， 是否使用？
	DebugCode debugCode;
	EditCode editCode;
	IDEOutput ideOutput;
	ExplorerRelated explorerRelated;
	Action action;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DebugCode getDebugCode() {
		return debugCode;
	}
	public void setDebugCode(DebugCode debugCode) {
		this.debugCode = debugCode;
	}
	public EditCode getEditCode() {
		return editCode;
	}
	public void setEditCode(EditCode editCode) {
		this.editCode = editCode;
	}
	public IDEOutput getIdeOutput() {
		return ideOutput;
	}
	public void setIdeOutput(IDEOutput ideOutput) {
		this.ideOutput = ideOutput;
	}
	public ExplorerRelated getExplorerRelated() {
		return explorerRelated;
	}
	public void setExplorerRelated(ExplorerRelated explorerRelated) {
		this.explorerRelated = explorerRelated;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}

}
