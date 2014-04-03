package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class Cache {

	ActionSlideWindow actionSlideWindow=new ActionSlideWindow();
	private static int actionSlideWindowLimitSize=10;   //这个值今后要在配置中获取。
	List <Information>  informations=new ArrayList<Information>();
	List <DebugCode> debugCodes=new ArrayList<DebugCode>();;
	List<EditCode> editCodes=new ArrayList<EditCode>();
	List<IDEOutput> ideOutputs=new ArrayList<IDEOutput>();
	List<ExplorerRelated> explorerRelateds=new ArrayList<ExplorerRelated>();
	ClassModel currentClassModel=new ClassModel();


	private Cache()
	{
		System.out.println("Cache.Cache()");
	}
	private static class CacheHolder{
		public static Cache instance=new Cache();
	}
	public static Cache getInstance()
	{
		return CacheHolder.instance;
	}
	public static String getString() {
		return Cache.class.getName();
	}


	public void addInformation(Information information)
	{

		informations.add(information);

		addActionAndSlideWindow(information.getAction());

		if (information.getDebugCode()!=null) {

			addDebugCode(information.getDebugCode());
			setCurrentClassModel(information.getDebugCode().getClassModel());
		}

		if (information.getDebugCode()!=null) {

			addEditCode(information.getEditCode());
			setCurrentClassModel(information.getEditCode().getClassModel());
		}

		if (information.getIdeOutput()!=null) {
			addIDEOutput(information.getIdeOutput());
		}

		if(information.getExplorerRelated()!=null)
		{
			addExplorerRelated(information.getExplorerRelated());
		}
	}

	private void addExplorerRelated(ExplorerRelated explorerRelated) {
		explorerRelateds.add(explorerRelated);

	}
	private void addIDEOutput(IDEOutput ideOutput) {
		ideOutputs.add(ideOutput);

	}
	private void addEditCode(EditCode editCode) {
		editCodes.add(editCode);


	}
	private void addDebugCode(DebugCode debugCode) {
		debugCodes.add(debugCode);


	}
	private void addActionAndSlideWindow(Action action) {
		if (actionSlideWindow.getActionListSize()<actionSlideWindowLimitSize) {
			actionSlideWindow.actionList.add(action);
			actionSlideWindow.addSize();
		}
		else {
			
		}
		

	}
	public ActionSlideWindow getActionSlideWindow() {
		return actionSlideWindow;
	}
	public List<Information> getInformations() {
		return informations;
	}
	public List<DebugCode> getDebugCodes() {
		return debugCodes;
	}
	public List<EditCode> getEditCodes() {
		return editCodes;
	}
	public List<IDEOutput> getIdeOutputs() {
		return ideOutputs;
	}
	public List<ExplorerRelated> getExplorerRelateds() {
		return explorerRelateds;
	}
	public ClassModel getCurrentClassModel() {
		return currentClassModel;
	}
	public void setCurrentClassModel(ClassModel currentClassModel) {
		this.currentClassModel = currentClassModel;
	}


}
