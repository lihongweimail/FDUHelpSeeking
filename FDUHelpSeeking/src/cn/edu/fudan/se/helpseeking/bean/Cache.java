package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class Cache {
	
	ActionSlideWindow actionSlideWindow;
	List <DebugCode> debugCodes;
	List<EditCode> editCodes;
	List<IDEOutput> ideOutputs;
	List<ExplorerRelated> explorerRelateds;
	ClassModel currentClassModel;
	public ActionSlideWindow getActionSlideWindow() {
		return actionSlideWindow;
	}
	public void setActionSlideWindow(ActionSlideWindow actionSlideWindow) {
		this.actionSlideWindow = actionSlideWindow;
	}
	public List<DebugCode> getDebugCodes() {
		return debugCodes;
	}
	public void setDebugCodes(List<DebugCode> debugCodes) {
		this.debugCodes = debugCodes;
	}
	public List<EditCode> getEditCodes() {
		return editCodes;
	}
	public void setEditCodes(List<EditCode> editCodes) {
		this.editCodes = editCodes;
	}
	public List<IDEOutput> getIdeOutputs() {
		return ideOutputs;
	}
	public void setIdeOutputs(List<IDEOutput> ideOutputs) {
		this.ideOutputs = ideOutputs;
	}
	public List<ExplorerRelated> getExplorerRelateds() {
		return explorerRelateds;
	}
	public void setExplorerRelateds(List<ExplorerRelated> explorerRelateds) {
		this.explorerRelateds = explorerRelateds;
	}
	public ClassModel getCurrentClassModel() {
		return currentClassModel;
	}
	public void setCurrentClassModel(ClassModel currentClassModel) {
		this.currentClassModel = currentClassModel;
	}
	

}
