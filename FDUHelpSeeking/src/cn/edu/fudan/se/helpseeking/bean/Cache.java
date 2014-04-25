package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.eclipse.debug.internal.core.commands.ForEachCommand;
import org.eclipse.jdt.internal.ui.workingsets.EditorTracker;

import cn.edu.fudan.se.helpseeking.processing.CacheProcessing;

public class Cache  {


	private static class CacheHolder {
		public static Cache instance = new Cache();

	}

	public static Cache getInstance() {
		
		return CacheHolder.instance;
	}
	public static String getString() {
		return Cache.class.getName();
	}

	ActionQueue actions = new ActionQueue();// 给动作一个足够大的窗口
	ConsoleInformationList consoles = ConsoleInformationList.getInstance();
	private int consolesSize=0;
	
	ClassModel currentClassModel = new ClassModel();
	int currentID = 0;
	// 这个值为每次加入到cache中的信息进行编号，只加不减；
	// 当对动作进行处理时，可以根据这个编号检索信息编号
	
	List<KeyWord> currentKeywordsList = new ArrayList<KeyWord>();
	List<DebugCodeCache> debugCodes = new ArrayList<DebugCodeCache>();
	List<EditCodeCache> editCodes = new ArrayList<EditCodeCache>();

	
	List<ExplorerRelatedCache> explorerRelateds = new ArrayList<ExplorerRelatedCache>();
	List<IDEOutputCache> ideOutputs = new ArrayList<IDEOutputCache>();
	

	List<InformationQueue> informations = new ArrayList<InformationQueue>();

	ProblemInformationList problems = ProblemInformationList.getInstance();

	private int problemsSize=0;

	// 单例实现
	private Cache() {

//		System.out.println("Cache.Cache()");
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

	private void addDebugCode(DebugCode debugCode, int informationID) {
		DebugCodeCache d = new DebugCodeCache();
		d.setDebugCode(debugCode);
		d.setId(informationID);
		debugCodes.add(d);

	}

	private void addEditCode(EditCode editCode, int informationID) {
		EditCodeCache e = new EditCodeCache();
		e.setEditCode(editCode);
		e.setId(informationID);
		editCodes.add(e);
	}

	private void addExplorerRelated(ExplorerRelated explorerRelated,
			int informationID) {
		ExplorerRelatedCache e = new ExplorerRelatedCache();
		e.setExplorerRelated(explorerRelated);
		e.setId(informationID);
		explorerRelateds.add(e);

	}

	private void addIDEOutput(IDEOutput ideOutput, int informationID) {
		IDEOutputCache i = new IDEOutputCache();
		i.setId(informationID);
		i.setIdeOutput(ideOutput);
		ideOutputs.add(i);

	}

	public void addInformationToCache(Information information,int actionID) {
		
		if (checkInformation(information))
		{
			System.out.println("refuse one more time dupplicate action");
			return;
		}
		
	//添加到cache中
		doAddInformationToCache(information, actionID);	
		
		
		//添加到cache1中
		Cache1 myCache1=Cache1.getInstance();
		myCache1.addInformationToCache1(information, actionID);
		
	
			

	}
	
	
	public void doAddInformationToCache(Information information, int actionID) {
		InformationQueue infq = new InformationQueue();
		currentID =actionID;
		infq.setId(currentID);
		infq.setInformation(information);

		informations.add(infq);

//		System.out.println("显示连续的编辑动作的动作名："+information.getAction().getActionKind().toString()+":"+information.getAction().getActionName()+"\n动作细节："+information.getAction().getDescription());
		
		addActions(information.getAction(), currentID);

		if (information.getDebugCode() != null) {
			addDebugCode(information.getDebugCode(), currentID);
			if (information.getDebugCode().getClassModel()!=null) {
				setCurrentClassModel(information.getDebugCode().getClassModel());
			}
			
		}

		if (information.getEditCode() != null) {

			addEditCode(information.getEditCode(), currentID);
			if (information.getEditCode().getClassModel()!=null) {
				setCurrentClassModel(information.getEditCode().getClassModel());
			}
			
		}

		if (information.getIdeOutput() != null) {
			addIDEOutput(information.getIdeOutput(), currentID);
		}

		if (information.getExplorerRelated() != null) {
			addExplorerRelated(information.getExplorerRelated(), currentID);
		}

		// 这里记得安排对每个新加入信息后的预处理处理(如有必要)

		// 使用线程
		CacheProcessing cpThread = new CacheProcessing();
		cpThread.run();
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
		List<ActionCache> acs=actions.getActionList();
		for (int i=acs.size()-1;i>=0;i--) {
			if (acs.get(i).getActionID()==currentID) {
				lastCacheAction=acs.get(i).getAction();
				break;
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



	public DebugCode findDebugCodeWithID(int ID) {
		DebugCode dc = null;

		for (DebugCodeCache dcc : getDebugCodes()) {
			if (dcc.id == ID) {
				dc = dcc.getDebugCode();
				break;
			}

		}
		return dc;
	}
	
	
	public EditCode findEditCodeWithID(int ID) {
		EditCode ec = null;
		for (EditCodeCache ecc : getEditCodes()) {
			if (ecc.id == ID) {
				ec = ecc.getEditCode();
				break;
			}

		}
		return ec;
	}

	public ExplorerRelated findExplorerRelatedWithID(int ID) {
		ExplorerRelated er = null;
		for (ExplorerRelatedCache erc : getExplorerRelateds()) {
			if (erc.id == ID) {
				er = erc.getExplorerRelated();
				break;
			}
		}
		return er;
	}


	public IDEOutput findIdeOutputWithID(int ID) {
		IDEOutput ideop = null;
		for (IDEOutputCache ideopc : getIdeOutputs()) {
			if (ideopc.id == ID) {
				ideop = ideopc.getIdeOutput();
				break;

			}
		}
		return ideop;
	}



	public ActionQueue getActions() {
		return actions;
	}



	public ActionQueue getActionSlideWindow() {
		return actions;
	}

	public ConsoleInformationList getConsoles() {
		return consoles;
	}

	public int getConsolesSize() {
		return consolesSize;
	}

	public ClassModel getCurrentClassModel() {
		return currentClassModel;
	}

	public int getCurrentID() {
		return currentID;
	}

	public List<KeyWord> getCurrentKeywordsList() {
		return currentKeywordsList;
	}

	public List<DebugCodeCache> getDebugCodes() {
		return debugCodes;
	}

	public List<EditCodeCache> getEditCodes() {
		return editCodes;
	}

	public List<ExplorerRelatedCache> getExplorerRelateds() {
		return explorerRelateds;
	}

	public List<IDEOutputCache> getIdeOutputs() {
		return ideOutputs;
	}

	public List<InformationQueue> getInformations() {
		return informations;
	}

	
public ProblemInformationList getProblems() {
	return problems;
}
	
	
	public int getProblemsSize() {
		return problemsSize;
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
		
				for (int j1 = 0; j1 < debugCodes.size(); j1++) {
					if (debugCodes.get(j1).getDebugCode().getBreakpoint().getFileName().trim().equals(classFileName)) {
						debugCodes.remove(j1);
					}
				}

				for (int j1 = 0; j1 < editCodes.size(); j1++) {
					if (editCodes.get(j1).getEditCode().getCursor().getFileName().trim().equals(classFileName)) {
						editCodes.remove(j1);
						break;
					}
				}



	}
	

		


	private void removeInformationNaive(int removeActionID) {


		
		for (int i = 0; i < informations.size(); i++) {
			if (informations.get(i).getId() == removeActionID) {
				informations.remove(i);
			    break;
			}
			}
				for (int j1 = 0; j1 < debugCodes.size(); j1++) {
					if (debugCodes.get(j1).getId() == removeActionID) {
						debugCodes.remove(j1);
						break;
					}
				}

				for (int j1 = 0; j1 < editCodes.size(); j1++) {
					if (editCodes.get(j1).getId() == removeActionID) {
						editCodes.remove(j1);
						break;
					}
				}

				for (int j1 = 0; j1 < explorerRelateds.size(); j1++) {
					if (explorerRelateds.get(j1).getId() == removeActionID) {
						explorerRelateds.remove(j1);
						break;
					}
				}

				for (int j1 = 0; j1 < ideOutputs.size(); j1++) {
					if (ideOutputs.get(j1).getId() == removeActionID) {
						ideOutputs.remove(j1);
						break;
					}
				}

		
		

	}

	public void setConsoles(ConsoleInformationList consoles) {
		this.consoles = consoles;
	}

	public void setConsolesSize(int consolesSize) {
		this.consolesSize = consolesSize;
	}

	public void setCurrentClassModel(ClassModel currentClassModel) {
		this.currentClassModel = currentClassModel;
	}

	public void setCurrentID(int currentID) {
		this.currentID = currentID;
	}

	public void setCurrentKeywordsList(List<KeyWord> currentKeywordsList) {
		this.currentKeywordsList = currentKeywordsList;
	}

	public void setProblems(ProblemInformationList problems) {
		this.problems = problems;
	}

	public void setProblemsSize(int problemsSize) {
		this.problemsSize = problemsSize;
	}

}
