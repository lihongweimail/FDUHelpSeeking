package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.processing.CacheProcessing;

public class Cache  {

	ActionQueue actions = new ActionQueue();// 包含滑动窗口

	List<InformationQueue> informations = new ArrayList<InformationQueue>();
	int currentID = 0;
	// 这个值为每次加入到cache中的信息进行编号，只加不减；
	// 当对动作进行处理时，可以根据这个编号检索信息编号

	List<DebugCodeCache> debugCodes = new ArrayList<DebugCodeCache>();
	List<EditCodeCache> editCodes = new ArrayList<EditCodeCache>();
	List<IDEOutputCache> ideOutputs = new ArrayList<IDEOutputCache>();
	List<ExplorerRelatedCache> explorerRelateds = new ArrayList<ExplorerRelatedCache>();
	ClassModel currentClassModel = new ClassModel();
	List<KeyWord> currentKeywordsList = new ArrayList<KeyWord>();

	public void setCurrentKeywordsList(List<KeyWord> currentKeywordsList) {
		this.currentKeywordsList = currentKeywordsList;
	}

	// 单例实现
	private Cache() {

		System.out.println("Cache.Cache()");
	}

	private static class CacheHolder {
		public static Cache instance = new Cache();

	}

	public static Cache getInstance() {
		
		return CacheHolder.instance;
	}

	public static String getString() {
		return Cache.class.getName();
	}

	// 实现单例

	public int getCurrentID() {
		return currentID;
	}

	public void setCurrentID(int currentID) {
		this.currentID = currentID;
	}


	public void addInformationToCache(Information information) {

		InformationQueue infq = new InformationQueue();
		currentID = currentID + 1;
		infq.setId(currentID);
		infq.setInformation(information);

		informations.add(infq);

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

	private void addEditCode(EditCode editCode, int informationID) {
		EditCodeCache e = new EditCodeCache();
		e.setEditCode(editCode);
		e.setId(informationID);
		editCodes.add(e);
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

	public String findDebugCodeString(int actionID, int mode) {
		String str = " ";
		for (DebugCodeCache dcc : debugCodes) {
			if (dcc.getId() == actionID) {
				String breakpointstr = dcc.getDebugCode().getBreakpoint()
						.getType()
						+ " "
						+ dcc.getDebugCode().getBreakpoint()
								.getMethodQualifiedName();
				String synctacticblockstr = dcc.getDebugCode()
						.getSyntacticBlock().getType()
						+ " "
						+ dcc.getDebugCode().getSyntacticBlock().getCode();
				String exceptionnamestr = dcc.getDebugCode()
						.getSyntacticBlock().getExceptionName();
				String classmodelstr = dcc.getDebugCode().getClassModel()
						.getType()
						+ " " + dcc.getDebugCode().getClassModel().getCode();
				// TODO
				// str=str+
			}
		}

		return str;

	}

	private void addDebugCode(DebugCode debugCode, int informationID) {
		DebugCodeCache d = new DebugCodeCache();
		d.setDebugCode(debugCode);
		d.setId(informationID);
		debugCodes.add(d);

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

				for (int j1 = 0; j1 < debugCodes.size(); j1++) {
					if (debugCodes.get(i).getId() == removeActionID) {
						debugCodes.remove(i);
						break;
					}
				}

				for (int j1 = 0; j1 < editCodes.size(); j1++) {
					if (editCodes.get(i).getId() == removeActionID) {
						editCodes.remove(i);
						break;
					}
				}

				for (int j1 = 0; j1 < explorerRelateds.size(); j1++) {
					if (explorerRelateds.get(i).getId() == removeActionID) {
						explorerRelateds.remove(i);
						break;
					}
				}

				for (int j1 = 0; j1 < ideOutputs.size(); j1++) {
					if (ideOutputs.get(i).getId() == removeActionID) {
						ideOutputs.remove(i);
						break;
					}
				}

				break;

			}
		}

	}

	public ActionQueue getActionSlideWindow() {
		return actions;
	}

	public List<InformationQueue> getInformations() {
		return informations;
	}

	public ClassModel getCurrentClassModel() {
		return currentClassModel;
	}

	public void setCurrentClassModel(ClassModel currentClassModel) {
		this.currentClassModel = currentClassModel;
	}

	public ActionQueue getActions() {
		return actions;
	}

	public List<DebugCodeCache> getDebugCodes() {
		return debugCodes;
	}

	public List<EditCodeCache> getEditCodes() {
		return editCodes;
	}

	public List<IDEOutputCache> getIdeOutputs() {
		return ideOutputs;
	}

	public List<ExplorerRelatedCache> getExplorerRelateds() {
		return explorerRelateds;
	}

	public List<KeyWord> getCurrentKeywordsList() {
		return currentKeywordsList;
	}

}
