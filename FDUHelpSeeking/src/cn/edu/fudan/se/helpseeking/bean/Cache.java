package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.ui.texteditor.CaseAction;
import org.htmlparser.filters.AndFilter;

import cn.edu.fudan.se.helpseeking.bean.Basic.DebugAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.processing.CacheProcessing;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import edu.mit.jwi.data.compare.CommentComparator;

public class Cache {

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
	List<ConsoleInformationListCopy> oldConsolesCopy = null;
	private int consolesSize = 0;

	ClassModel currentClassModel = new ClassModel();
	int lastClassModelID = 0;// 初始为0，只有覆盖后调整如果值不相同;没写了classmodel
								// 就值相同current赋给last;写了就current+1
	int currentClassModelID = 0;// 累加只在新加入代码时累加 在classmodel内部保留设置值
	int currentID = 0;
	// 这个值为每次加入到cache中的信息进行编号，只加不减；
	// 当对动作进行处理时，可以根据这个编号检索信息编号

	List<KeyWord> currentKeywordsList = new ArrayList<KeyWord>();
	List<WindowTotalKeyWords> historyKeyWords = new ArrayList<WindowTotalKeyWords>();

	List<DebugCodeCache> debugCodes = new ArrayList<DebugCodeCache>();
	List<EditCodeCache> editCodes = new ArrayList<EditCodeCache>();

	List<ExplorerRelatedCache> explorerRelateds = new ArrayList<ExplorerRelatedCache>();
	List<IDEOutputCache> ideOutputs = new ArrayList<IDEOutputCache>();

	List<InformationQueue> informations = new ArrayList<InformationQueue>();

	ProblemInformationList problems = ProblemInformationList.getInstance();
	ProblemInformationListCopy oldProblemsCopy = null;
	List<String> taskDescription;

	
	List<KeyWordsCandidates> cacheCandWordTrees = new ArrayList<>();
	int cacheCandWordTreesIndex = 0;
	List<SearchResults> cacheAutoSearchResults = new ArrayList<>();
	int cacheAutoSearchResultsIndex = 0;
	


	//记录最近的N个检索词
	int countAutoTry=0;//自动检索词窗口太小使用的词重复时被拒绝检索次数 3次 则打开普通检索
	
	public int getCountAutoTry() {
		return countAutoTry;
	}

	public void setCountAutoTry(int countAutoTry) {
		this.countAutoTry = countAutoTry;
	}

	//每隔一段时间将历史查询词汇汇总 自动检索一次的信号  间隔2分钟  
	int timerAutoSearchmode=0;  //值为0 正常态， 值为1需要检索， 值为2刚做过人工检索
	Timestamp lastAutoSearchTime=new Timestamp(System.currentTimeMillis());
	String lastAutoSearchWords="";
	
	

	public String getLastAutoSearchWords() {
		return lastAutoSearchWords;
	}

	public void setLastAutoSearchWords(String lastAutoSearchWords) {
		this.lastAutoSearchWords = lastAutoSearchWords;
	}

	public int getTimerAutoSearchmode() {
		return timerAutoSearchmode;
	}

	public void setTimerAutoSearchmode(int timerAutoSearchmode) {
		this.timerAutoSearchmode = timerAutoSearchmode;
	}

	public Timestamp getLastAutoSearchTime() {
		return lastAutoSearchTime;
	}

	public void setLastAutoSearchTime(Timestamp lastAutoSearchTime) {
		this.lastAutoSearchTime = lastAutoSearchTime;
	}

	private int problemsSize = 0;

	public List<WindowTotalKeyWords> getHistoryKeyWords() {
		return historyKeyWords;
	}

	public void setHistoryKeyWords(List<WindowTotalKeyWords> historyKeyWords) {
		this.historyKeyWords = historyKeyWords;
	}

	public List<KeyWordsCandidates> getCacheCandWordTrees() {
		return cacheCandWordTrees;
	}

	public void setCacheCandWordTrees(
			List<KeyWordsCandidates> cacheCandWordTrees) {
		this.cacheCandWordTrees = cacheCandWordTrees;
	}

	public List<SearchResults> getCacheAutoSearchResults() {
		return cacheAutoSearchResults;
	}

	public void setCacheAutoSearchResults(
			List<SearchResults> cacheAutoSearchResults) {
		this.cacheAutoSearchResults = cacheAutoSearchResults;
	}

	public int getCacheCandWordTreesIndex() {
		return cacheCandWordTreesIndex;
	}

	public void setCacheCandWordTreesIndex(int cacheCandWordTreesIndex) {
		this.cacheCandWordTreesIndex = cacheCandWordTreesIndex;
	}

	public int getCacheAutoSearchResultsIndex() {
		return cacheAutoSearchResultsIndex;
	}

	public void setCacheAutoSearchResultsIndex(int cacheAutoSearchResultsIndex) {
		this.cacheAutoSearchResultsIndex = cacheAutoSearchResultsIndex;
	}

	private boolean compareProblemsAndCopy() {
		boolean result = true;

		ProblemInformationListCopy tempProblemsCopy = new ProblemInformationListCopy();
		ArrayList<ProblemInformation> errorList = new ArrayList<>();
		ArrayList<ProblemInformation> warningList = new ArrayList<>();

		boolean resultError = true;

		for (ProblemInformation pifE : problems.getErrorList()) {

			if (problems.getErrorList().size() <= 0) {
				tempProblemsCopy.setErrorList(null);
				break;
			}

			if (oldProblemsCopy != null
					&& oldProblemsCopy.getErrorList() != null) {
				boolean flagError = true;

				for (ProblemInformation pifError : oldProblemsCopy
						.getErrorList()) {
					if (pifE.getSeverity() == pifError.getSeverity()) {
						if (pifE.getDescription().equals(
								pifError.getDescription())) {
							if (pifE.getRelatedMethod().equals(
									pifError.getRelatedMethod())) {

								if (pifE.getLineNumber() == pifError
										.getLineNumber()) {

									flagError = false;

								}
							}

						}

					}
				}

				if (flagError) {
					resultError = false;
					ProblemInformation errorInformation = new ProblemInformation();
					errorInformation.setCause(pifE.getCause());
					errorInformation.setCharEnd(pifE.getCharEnd());
					errorInformation.setCharStart(pifE.getCharStart());
					errorInformation.setDescription(pifE.getDescription());
					errorInformation.setLineNumber(pifE.getLineNumber());
					errorInformation.setPath(pifE.getPath());
					errorInformation.setRelatedMethod(pifE.getRelatedMethod());
					errorInformation.setResource(pifE.getResource());
					errorInformation.setSeverity(pifE.getSeverity());
					errorInformation.setSource(pifE.getSource());

					errorList.add(errorInformation);

				}

			} else {
				resultError = false;
				ProblemInformation errorInformation = new ProblemInformation();
				errorInformation.setCause(pifE.getCause());
				errorInformation.setCharEnd(pifE.getCharEnd());
				errorInformation.setCharStart(pifE.getCharStart());
				errorInformation.setDescription(pifE.getDescription());
				errorInformation.setLineNumber(pifE.getLineNumber());
				errorInformation.setPath(pifE.getPath());
				errorInformation.setRelatedMethod(pifE.getRelatedMethod());
				errorInformation.setResource(pifE.getResource());
				errorInformation.setSeverity(pifE.getSeverity());
				errorInformation.setSource(pifE.getSource());
				errorList.add(errorInformation);
			}

		}

		if (!resultError) {
			tempProblemsCopy.setErrorList(errorList);
		}

		boolean resultWarning = true;

		for (ProblemInformation pifE : problems.getWarningList()) {
			if (problems.getWarningList().size() <= 0) {
				tempProblemsCopy.setWarningList(null);
				break;
			}

			if (oldProblemsCopy != null
					&& oldProblemsCopy.getWarningList() != null) {
				boolean flagWarning = true;

				for (ProblemInformation pifWarning : oldProblemsCopy
						.getWarningList()) {
					if (pifE.getSeverity() == pifWarning.getSeverity()) {
						if (pifE.getDescription().equals(
								pifWarning.getDescription())) {
							if (pifE.getRelatedMethod().equals(
									pifWarning.getRelatedMethod())) {

								if (pifE.getLineNumber() == pifWarning
										.getLineNumber()) {

									flagWarning = false;

								}
							}

						}

					}
				}

				if (flagWarning) {
					resultWarning = false;
					ProblemInformation warningInformation = new ProblemInformation();
					warningInformation.setCause(pifE.getCause());
					warningInformation.setCharEnd(pifE.getCharEnd());
					warningInformation.setCharStart(pifE.getCharStart());
					warningInformation.setDescription(pifE.getDescription());
					warningInformation.setLineNumber(pifE.getLineNumber());
					warningInformation.setPath(pifE.getPath());
					warningInformation
							.setRelatedMethod(pifE.getRelatedMethod());
					warningInformation.setResource(pifE.getResource());
					warningInformation.setSeverity(pifE.getSeverity());
					warningInformation.setSource(pifE.getSource());

					warningList.add(warningInformation);

				}

			} else {
				resultWarning = false;
				ProblemInformation warningInformation = new ProblemInformation();
				warningInformation.setCause(pifE.getCause());
				warningInformation.setCharEnd(pifE.getCharEnd());
				warningInformation.setCharStart(pifE.getCharStart());
				warningInformation.setDescription(pifE.getDescription());
				warningInformation.setLineNumber(pifE.getLineNumber());
				warningInformation.setPath(pifE.getPath());
				warningInformation.setRelatedMethod(pifE.getRelatedMethod());
				warningInformation.setResource(pifE.getResource());
				warningInformation.setSeverity(pifE.getSeverity());
				warningInformation.setSource(pifE.getSource());
				warningList.add(warningInformation);
			}

		}

		if (!resultWarning) {
			tempProblemsCopy.setWarningList(warningList);
		}

		if (!resultWarning || !resultError) {
			result = false;
		}

		return result;
	}

	private boolean compareConsolesAndCopy() {
		boolean result = true;

		List<ConsoleInformationListCopy> tempConsolesCopy = new ArrayList<>();

		for (ConsoleInformation cif : consoles.getExceptionList()) {

			if (oldConsolesCopy != null) {

				boolean flag = true;
				for (ConsoleInformationListCopy ciflc : oldConsolesCopy) {
					if (cif.getExceptionName().equals(ciflc.getExceptionName())) {
						if (cif.getDescription() != null
								&& ciflc.getDescription() != null) {
							if (cif.getDescription().equals(
									ciflc.getDescription())) {
								flag = false;
							}
						}
					}

				}

				if (flag) {

					result = false;
					ConsoleInformationListCopy newNode = new ConsoleInformationListCopy();
					newNode.setDescription(cif.getDescription());
					newNode.setExceptionName(cif.getExceptionName());
					tempConsolesCopy.add(newNode);

				}

			} else {
				result = false;
				ConsoleInformationListCopy newNode = new ConsoleInformationListCopy();
				newNode.setDescription(cif.getDescription());
				newNode.setExceptionName(cif.getExceptionName());
				tempConsolesCopy.add(newNode);
			}

		}

		if (!result) {
			oldConsolesCopy = tempConsolesCopy;
		}

		return result;
	}

	// 单例实现
	private Cache() {

		// System.out.println("Cache.Cache()");
	}

	public List<String> getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(List<String> taskDescription) {
		this.taskDescription = taskDescription;
	}

	private void addActions(Action action, int id) {

		ActionCache a = new ActionCache();
		a.setAction(action);
		a.setActionID(id);
		actions.actionList.add(a);

		// 增加频率  同类动作

		boolean flagCount = false;
		for (int j1 = 0; j1 < acFrequencysList.size(); j1++) {

			if (acFrequencysList.get(j1).getActionKind()
					.equals(action.getActionKind())
					&& acFrequencysList.get(j1).getActionName()
							.equals(action.getActionName())) {

				int fqc = acFrequencysList.get(j1).getFrequency();
				fqc = fqc + 1;
				acFrequencysList.get(j1).setFrequency(fqc);
				acFrequencysList.get(j1).getActionIDList().add(id);

				flagCount = true;
			}
		}

		if (flagCount) {
			ActionInformation aif = new ActionInformation();
			aif.setActionID(id);
			aif.setActionKind(action.getActionKind());
			aif.setActionName(action.getActionName());
			aif.getActionIDList().add(id);
			aif.setFrequency(1);
			acFrequencysList.add(aif);

		}

		int removeActionID = actions.addSizeReturnRemoveActionID();

		if (removeActionID != -1) // 则说明动作已经放满了滑动窗口 ， 这时考虑不在窗口内的信息从信息集合中移除
		{

			// 最简单的策略直接删除最早的动作， 后续需要可配置功能
			removeInformationNaive(removeActionID);

			// 移除频率

			for (int j1 = 0; j1 < acFrequencysList.size(); j1++) {

				if (acFrequencysList.get(j1).getActionKind()
						.equals(action.getActionKind())
						&& acFrequencysList.get(j1).getActionName()
								.equals(action.getActionName())) {

					int fqc = acFrequencysList.get(j1).getFrequency();
					fqc = fqc - 1;
					acFrequencysList.get(j1).setFrequency(fqc);

					break;
				}
			}

		}

		// 求同类动作频率

		actions.resetDistence();

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

	public void addInformationToCache(Information information, int actionID) {

		
		if (checkInformation(information)) {
			System.out.println("refuse one more time dupplicate action");
			return;
		}
		

		
		// 添加到cache中
		doAddInformationToCache(information, actionID);

	}

	// 简单的连续相同不记录模式
	private boolean checkInformation(Information information) {
		boolean result = false;
		
		
		
		if (actions.getActionList() == null) {
			// System.out.println("actons is null");
			return false;
		}
		
		
		Action newEnterAction = information.getAction();
		Action lastCacheAction = null;
		List<ActionCache> acs = actions.getActionList();
		for (int i = acs.size() - 1; i >= 0; i--) {
			if (acs.get(i).getActionID() == currentID) {
				lastCacheAction = acs.get(i).getAction();
				break;

			}
		}
		

		if (newEnterAction.getActionKind().equals(Kind.SELECTIONFOCUS)) {
			if (newEnterAction.getActionName().toLowerCase().trim().equals(lastCacheAction.getActionName().toLowerCase().trim())) {
				return true;
			}
			return false;
		}
		

	



		if (lastCacheAction == null) {
			// System.out.println("not find action");
			return false;
		}
		
		
		
	
		if (informations.size() > 0) {
			

			
			int size=(informations.size()<Basic.Mini_Actions) ? informations.size():Basic.Mini_Actions;

			// 如果历史上在5秒内有相同的动作和描述也禁止出现;倒数的5个动作中有相同的 则不计入
			for (int i=informations.size()-1;i>informations.size()-size;i--) {
			
				Action testAction = informations.get(i).getInformation().getAction();
				if (newEnterAction.getActionKind().equals(testAction.getActionKind())
						&& newEnterAction.getActionName().toLowerCase().trim().equals(testAction.getActionName().toLowerCase().trim())) {
//					    result=true;
					    
            if (newEnterAction.getDescription().toLowerCase().trim().equals(testAction.getDescription().toLowerCase().trim())) {
	                                      result=true;
	                                      break;
                                  }
            
			if (newEnterAction.getTime().getTime()
							- testAction.getTime().getTime() < Basic.MINI_Action_Time) {
						result = true;
						break;
					}

				}
			}

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

	// 用于处理当编辑状态下，一些程序元素删除；或类修改后保存该类的文件后，该类编译通过
	// 后在problem view中没有该类的编译错误和警告信息（再考虑是否只是没有错误信息）
	// 在光标中有记录该类的类名文件
	public void removeEditCodeAsDeleteOrProblemViewChange(String classFileName) {
		// TODO 还有待实现

		classFileName = classFileName.trim();
		for (int i = 0; i < informations.size(); i++) {
			DebugCode dc = informations.get(i).getInformation().getDebugCode();
			EditCode ec = informations.get(i).getInformation().getEditCode();
			if (dc != null) {
				if (dc.getBreakpoint() != null) {

					if (dc.getBreakpoint().getFileName()!=null)
					{if (dc.getBreakpoint().getFileName().trim()
							.equals(classFileName)) {
						informations.get(i).getInformation().setDebugCode(null);

					}
					}
				}
			}

			if (ec != null) {
				if (ec.getCursor() != null) {
                     if (ec.getCursor().getFileName()!=null) {
						
				
					if (ec.getCursor().getFileName().trim()
							.equals(classFileName)) {
						informations.get(i).getInformation().setEditCode(null);
						

					}	
					}
				}
			}

		}

		for (int j1 = 0; j1 < debugCodes.size(); j1++) {
			if (debugCodes.get(j1).getDebugCode()!=null) {
				if (debugCodes.get(j1).getDebugCode().getBreakpoint()!=null) {
					if (debugCodes.get(j1).getDebugCode().getBreakpoint().getFileName()!=null) {
							if (debugCodes.get(j1).getDebugCode().getBreakpoint().getFileName()
					.trim().equals(classFileName)) {
				debugCodes.remove(j1);
			}
					}
					
				}
				
			}
			
		
		}

		for (int j1 = 0; j1 < editCodes.size(); j1++) {
			if (editCodes.get(j1).getEditCode()!=null) {
				if (editCodes.get(j1).getEditCode().getCursor()!=null) {
					if (editCodes.get(j1).getEditCode().getCursor().getFileName()!=null) {
							if (editCodes.get(j1).getEditCode().getCursor().getFileName()
					.trim().equals(classFileName)) {
				editCodes.remove(j1);
				break;
			}
					}
					
				}
				
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

		// 移除候选关键词

		// List<KeyWordsCandidates> consoleCacheKeyWords=new ArrayList<>();
		// List<KeyWordsCandidates> problemCacheKeyWords=new ArrayList<>();
		// List<KeyWordsCandidates> classmodelKeyWords=new ArrayList<>();
		// // List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
		// // List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
		// List<KeyWordsCandidates> codeKeyWords=new ArrayList<>();
		// List<KeyWordsCandidates> relatedExplorerKeyWords=new ArrayList<>();
		// 新增对历史上console和problem view 对应信息的删除动作 revise 80后新增的
		for (int j1 = 0; j1 < consoleCacheKeyWords.size(); j1++) {

			if (consoleCacheKeyWords.get(j1).getActionID() == removeActionID) {
				consoleCacheKeyWords.remove(j1);

				break;
			}
		}

		for (int j1 = 0; j1 < problemCacheKeyWords.size(); j1++) {

			if (problemCacheKeyWords.get(j1).getActionID() == removeActionID) {
				problemCacheKeyWords.remove(j1);

				break;
			}
		}

		// revise 80 版本的内容
		for (int j1 = 0; j1 < classmodelKeyWords.size(); j1++) {

			if (classmodelKeyWords.get(j1).getActionID() == removeActionID) {
				classmodelKeyWords.remove(j1);

				break;
			}
		}

		for (int j1 = 0; j1 < codeKeyWords.size(); j1++) {

			if (codeKeyWords.get(j1).getActionID() == removeActionID) {
				codeKeyWords.remove(j1);

				break;
			}
		}

		for (int j1 = 0; j1 < relatedExplorerKeyWords.size(); j1++) {

			if (relatedExplorerKeyWords.get(j1).getActionID() == removeActionID) {
				relatedExplorerKeyWords.remove(j1);

				break;
			}
		}

	}

	public void doAddInformationToCache(Information information, int actionID) {
		InformationQueue infq = new InformationQueue();
		currentID = actionID;
		infq.setId(currentID);
		infq.setInformation(information);

		informations.add(infq);

		// System.out.println("显示连续的编辑动作的动作名："+information.getAction().getActionKind().toString()+":"+information.getAction().getActionName()+"\n动作细节："+information.getAction().getDescription());

		addActions(information.getAction(), currentID);

		if (information.getDebugCode() != null) {
			addDebugCode(information.getDebugCode(), currentID);
			addDebugCodeKeywordsToCodeKeyWords(information.getDebugCode());
			if (information.getDebugCode().getClassModel() != null) {
				currentClassModelID = currentClassModelID + 1;
				setCurrentClassModel(information.getDebugCode().getClassModel());
				addCurrentClassModelKeywordsToClassmodelKeyWords(1);// mode=1
																	// 取callee
																	// below
																	// class

			} else {
				lastClassModelID = currentClassModelID;
			}

		}

		if (information.getEditCode() != null) {

			addEditCode(information.getEditCode(), currentID);
			addEditCodeKeywordsToCodeKeyWords(information.getEditCode());
			if (information.getEditCode().getClassModel() != null) {
				currentClassModelID = currentClassModelID + 1;
				setCurrentClassModel(information.getEditCode().getClassModel());
				addCurrentClassModelKeywordsToClassmodelKeyWords(1);// mode=1
																	// 取callee
																	// below
																	// class
			} else {
				lastClassModelID = currentClassModelID;
			}

		}

		if (Basic.ALGORITHMSELECTION==2) {
			
	
		
//		 if (information.getIdeOutput() != null) {
//		 addIDEOutput(information.getIdeOutput(), currentID);
//		 addKeywordsToConsoleviewKeywords(information.getIdeOutput().getRuntimeInformation());
//		 addKeywordsToProblemViewKeywords(information.getIdeOutput().getCompileInformation());
//		 }

		 if (information.getExplorerRelated() != null) {
		 addExplorerRelated(information.getExplorerRelated(), currentID);
		 addKeywordsEditorInfoToRelatedExplorerKeywords(information.getExplorerRelated().getEditorInfo());
		 addKeywordsExplorerInfoToRelatedExplorerKeyWords(information.getExplorerRelated().getExplorerInfo());
		 }
			
		}

		adjustConsolesKeywords();
		adjustProblemsKeywords();

		// 这里记得安排对每个新加入信息后的预处理处理(如有必要)
		

		//预处理方式一，定时检查并检索（满足条件后在cacheproecessing中处理词汇，从历史上的K组候选词中求词是否来源于某个包的信息多，然后加上 API example等词汇检索）
        Timestamp curtime=new Timestamp(System.currentTimeMillis());
        if (curtime.getTime()-getLastAutoSearchTime().getTime()>Basic.Auto_Search_Timer) {        	
			setTimerAutoSearchmode(1);
			setLastAutoSearchTime(curtime);
		}   
        
		
		// 使用线程
		CacheProcessing cpThread = new CacheProcessing();
		cpThread.run();
		
		
	}
	
	
	
	
	static List<KeyWord> lastKeyWordsforQuery=new ArrayList<KeyWord>();
	
	

	public static List<KeyWord> getLastKeyWordsforQuery() {
		return lastKeyWordsforQuery;
	}

	public static  void setLastKeyWordsforQuery(List<KeyWord> lastKeyWordsforQuery) {
	  Cache.lastKeyWordsforQuery = lastKeyWordsforQuery;
	}

	List<ActionInformation> acFrequencysList = new ArrayList<>();

	public List<ActionInformation> getAcFrequencysList() {
		return acFrequencysList;
	}

	public void setAcFrequencysList(List<ActionInformation> acFrequencysList) {
		this.acFrequencysList = acFrequencysList;
	}

	List<KeyWordsCandidates> consoleCacheKeyWords = new ArrayList<>();
	List<KeyWordsCandidates> problemCacheKeyWords = new ArrayList<>();
	List<KeyWordsCandidates> classmodelKeyWords = new ArrayList<>();
	// List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
	// List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
	List<KeyWordsCandidates> codeKeyWords = new ArrayList<>();
	List<KeyWordsCandidates> relatedExplorerKeyWords = new ArrayList<>();

	public List<KeyWordsCandidates> getConsoleCacheKeyWords() {
		return consoleCacheKeyWords;
	}

	public void setConsoleCacheKeyWords(
			List<KeyWordsCandidates> consoleCacheKeyWords) {
		this.consoleCacheKeyWords = consoleCacheKeyWords;
	}

	public List<KeyWordsCandidates> getProblemCacheKeyWords() {
		return problemCacheKeyWords;
	}

	public void setProblemCacheKeyWords(
			List<KeyWordsCandidates> problemCacheKeyWords) {
		this.problemCacheKeyWords = problemCacheKeyWords;
	}

	public List<KeyWordsCandidates> getClassmodelKeyWords() {
		return classmodelKeyWords;
	}

	public void setClassmodelKeyWords(
			List<KeyWordsCandidates> classmodelKeyWords) {
		this.classmodelKeyWords = classmodelKeyWords;
	}

	public List<KeyWordsCandidates> getCodeKeyWords() {
		return codeKeyWords;
	}

	public void setCodeKeyWords(List<KeyWordsCandidates> codeKeyWords) {
		this.codeKeyWords = codeKeyWords;
	}

	public List<KeyWordsCandidates> getRelatedExplorerKeyWords() {
		return relatedExplorerKeyWords;
	}

	public void setRelatedExplorerKeyWords(
			List<KeyWordsCandidates> relatedExplorerKeyWords) {
		this.relatedExplorerKeyWords = relatedExplorerKeyWords;
	}

	private void addKeywordsExplorerInfoToRelatedExplorerKeyWords(
			ExplorerInfo explorerInfo) {

		KeyWordsCandidates explorerInfoCodeCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		if (explorerInfo != null) {
			if (explorerInfo.getSelectObjectNameList() != null
					|| explorerInfo.getSelectObjectNameList().size() > 0) {

				// 取消息
				for (String str : explorerInfo.getSelectObjectNameList()) {
					if ((str == null) || str.equals("") || str.contains(".java")) {
						break;
					}
					
					if (str.contains("helpseeking")) {
						System.out.println("here here 5");
					}

					
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					
				    if (Basic.ALGORITHMSELECTION==1) {

					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Other");
				    }
				    
				    if (Basic.ALGORITHMSELECTION==2) {
				    	kw.setWeightOne(Basic.action_select);
				    	kw.setWeightTwo(Basic.api_normal);
						kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
						kw.setTagName("select # package explorer outlier");
						Action ac = actions.getActionCachewithActionID(currentID).getAction();
						if (ac != null) {
						if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
							kw.setWeightOne(Basic.action_selectfocus);
					           if (ac.getActionName().toLowerCase().contains("compile")) {
								kw.setWeightTwo(Basic.api_hasCompileError);
								}
							if (ac.getActionName().toLowerCase().contains("console")) {
							     kw.setWeightTwo(Basic.api_causeException);
								}
							if (ac.getActionName().toLowerCase().contains("insertcursor")) {
							     kw.setWeightTwo(Basic.api_hasCompileError);
								}
					       }

						}
				    }

					
					tempKeyWords.add(kw);
				}
			}

			explorerInfoCodeCandidates.setActionID(currentID); // 不跟随动作删除
			explorerInfoCodeCandidates
					.setLevel(Basic.KeyWordsLevel.Level_Seven);
			explorerInfoCodeCandidates.setOldStep(3);
			explorerInfoCodeCandidates.setKeyWords(tempKeyWords);
			relatedExplorerKeyWords.add(explorerInfoCodeCandidates);

		}

	}

	private void addKeywordsEditorInfoToRelatedExplorerKeywords(
			EditorInfo editorInfo) {

		KeyWordsCandidates editorInfoCodeCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		if (editorInfo != null) {
			if (editorInfo.getClassQualifiedNameList() != null
					|| editorInfo.getClassQualifiedNameList().size() > 0) {
				// 取消息
				for (String str : editorInfo.getClassQualifiedNameList()) {
					if ((str == null) || str.equals("")  || str.contains(".java")) {
						break;
					}
					
					if (str.contains("helpseeking")) {
						System.out.println("here here 4");
					}

					
					
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
				
				    if (Basic.ALGORITHMSELECTION==1) {

					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Other");
				    }
				    
				    if (Basic.ALGORITHMSELECTION==2) {
				    	kw.setWeightOne(Basic.action_reveal);
				    	kw.setWeightTwo(Basic.api_normal);
						kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
						kw.setTagName("other #  select edit code reveal api ");
						Action ac = actions.getActionCachewithActionID(currentID).getAction();
						if (ac != null) {
						if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
							kw.setWeightOne(Basic.action_selectfocus);
					           if (ac.getActionName().toLowerCase().contains("compile")) {
								kw.setWeightTwo(Basic.api_hasCompileError);
								}
							if (ac.getActionName().toLowerCase().contains("console")) {
							     kw.setWeightTwo(Basic.api_causeException);
								}
							if (ac.getActionName().toLowerCase().contains("insertcursor")) {
							     kw.setWeightTwo(Basic.api_hasCompileError);
								}

					       }

						}
				    }

					
					
					tempKeyWords.add(kw);
				}
			}

			editorInfoCodeCandidates.setActionID(currentID); // 不跟随动作删除
			editorInfoCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Seven);
			editorInfoCodeCandidates.setOldStep(3);
			editorInfoCodeCandidates.setKeyWords(tempKeyWords);
			relatedExplorerKeyWords.add(editorInfoCodeCandidates);

		}

	}

	private void addEditCodeKeywordsToCodeKeyWords(EditCode editCode) {

		KeyWordsCandidates editCodeCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		if (editCode != null && editCode.getSyntacticBlock() != null) {
			// 取代码等信息
			// 代码需要过滤停用词

			String codeString = editCode.getSyntacticBlock().getCode();

			codeString = CommUtil.removeStopWordsAsString(codeString);
 
			if (codeString == null) {
				return;
			}
			String exceptionalString = editCode.getSyntacticBlock()
					.getExceptionName();
			String rString = codeString + " ; " + exceptionalString;
			rString = CommUtil.removeDuplicateWords(CommUtil
					.stringToList(rString));

	

			String[][] apinameMap = getVarialToAPImap(editCode.getClassModel());

			for (String str : (rString.split("[;]"))) {
				
				if (str.contains(".java") || str.trim().equals("")) {
					continue;
				}

				if (str.contains("helpseeking")) {
					System.out.println("here here 3");
				}

				
				boolean flag = false;
				if (apinameMap!=null) {
					
				
				for (int i = 0; i < apinameMap.length; i++) {

					String keyvalue = apinameMap[i][0];
					String value = apinameMap[i][1];
					// System.out.println(keyvalue+":"+value+":"+str);
					if (keyvalue.toLowerCase().equals(str.trim().toLowerCase())) {
						str = value;
						flag = true;
						break;
					}

				}

			     }
				
				
				KeyWord kw = new KeyWord();
				kw.setKeywordName(str.trim());
				

				if (Basic.ALGORITHMSELECTION==2) {
					
					kw.setWeightOne(Basic.action_select); // interest level action  save or debug 
					Action ac=actions.getActionCachewithActionID(currentID).getAction();
					if (ac!=null)
					kw.setTagName(genTagName(ac,ac.actionName.toLowerCase()));
					
					kw.setWeightTwo(Basic.api_normal); //interest level api  normal
					kw.setTagName("Other");
					
					if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
						kw.setWeightOne(Basic.action_selectfocus);
				           if (ac.getActionName().toLowerCase().contains("compile")) {
							kw.setWeightTwo(Basic.api_hasCompileError);
							}
						if (ac.getActionName().toLowerCase().contains("console")) {
						     kw.setWeightTwo(Basic.api_causeException);
							}
						if (ac.getActionName().toLowerCase().contains("insertcursor")) {
						     kw.setWeightTwo(Basic.api_hasCompileError);
							}

				       }

					
					
				}
	
				
				if (Basic.ALGORITHMSELECTION==1) {				
				kw.setWeightOne(2);
				kw.setTagName("Other");
				}
				
				for (String jestr : Basic.javaExceptionalNameList) {

					if (str.trim().toLowerCase().contains("Exception".toLowerCase())) {
						if (Basic.ALGORITHMSELECTION==1) {	
						kw.setWeightOne(4);
						}
						kw.setTagName("Exception");
						flag = true;
						break;
					}

					if (str.trim().equals(jestr)) {
						if (Basic.ALGORITHMSELECTION==1) {	
						kw.setWeightOne(4);
						}
						kw.setTagName("Exception");
						flag = true;
						break;
					}
				}
				if (flag) {
					
					if (Basic.ALGORITHMSELECTION==1)	
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					
					if (Basic.ALGORITHMSELECTION==2)	
						kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
					
					
					tempKeyWords.add(kw);

				}

			}

			editCodeCandidates.setActionID(currentID); // 不跟随动作删除
			editCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Six);
			editCodeCandidates.setOldStep(5);
			editCodeCandidates.setKeyWords(tempKeyWords);
			codeKeyWords.add(editCodeCandidates);

		}

	}

	public String removeChacter(String para) {
		String result = para.trim();
		if (result.charAt(0) == 'L' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}

		if (result.charAt(0) == 'T' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}
		if (result.charAt(0) == 'I' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}
		if (result.charAt(0) == 'Q' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}

		if (result.charAt(0) == 'E' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}
		if (result.trim().charAt(0) == 'Z' && result.charAt(result.length() - 1) == ';') {
			result = result.substring(1, result.length() - 1);
		}
		if (result.trim().equals("Z") || result.trim().equals("TE")) {
			result = "";
		}
if (result.contains(";")) {
	result.replace(';', ' ');
}
		

		return result;
	}

	public String combileMethodQurifyname(String returnname, String name,
			String[] paraType) {
if (returnname==null) returnname="";

String paratypes = "";
if (paraType!=null) {
	


if (paraType.length>0 )
{	
	

		for (int i = 0; i < paraType.length; i++) {
			String para = paraType[i];
			String mypara = removeChacter(para);

			if (paratypes.equals("")) {
				paratypes = mypara;
			} else {
				paratypes = paratypes + "," + mypara;
			}

		}
}
}
		if (returnname.equals("void")) {
			returnname = "";
		}
		if (returnname.equals("String")) {
			returnname = "";
		}
		if (returnname.equals("int")) {
			returnname = "";
		}
		if (returnname.equals("float")) {
			returnname = "";
		}
		if (returnname.equals("Integer")) {
			returnname = "";
		}
		if (returnname.equals("double")) {
			returnname = "";
		}
		if (returnname.equals("boolean")) {
			returnname = "";
		}
		if (returnname.equals("byte")) {
			returnname = "";
		}
		if (returnname.equals("char")) {
			returnname = "";
		}
		if (returnname.equals("object")) {
			returnname = "";
		}
		if (returnname.equals("char")) {
			returnname = "";
		}
		if (returnname.equals("V")) {
			returnname = "";
		}
		if (returnname.equals("Z")) {
			returnname = "";
		}
		if (returnname.equals("I")) {
			returnname = "";
		}
		if (returnname.equals("K")) {
			returnname = "";
		}

		return returnname + " " + name + "(" + paratypes + ")";

	}

	public String[][] getVarialToAPImap(ClassModel classModel) {

		if (classModel == null || classModel.getCalleeInfo()==null) {
			return null;
		}
			
	

		Set<String> apiName = classModel.getCalleeInfo().keySet();

		String[][] result = new String[apiName.size()][2];
		int i = 0;

		for (String str : apiName) {
			String[] key = str.split("[.]");
			int index = key.length;
			String keyword = key[index - 1];

			String paratypes = "";
			for (int j = 0; j < classModel.getCalleeInfo().get(str)
					.getParaTypes().length; j++) {
				String para = paratypes
						+ classModel.getCalleeInfo().get(str).getParaTypes()[j];

				para = removeChacter(para);

				if (paratypes.equals("")) {
					paratypes = para.toString();
				} else {
					paratypes = paratypes + "," + para;
				}

			}
			String returntype = classModel.getCalleeInfo().get(str)
					.getReturnType();

			if (returntype.equals("void")) {
				returntype = "";
			}
			if (returntype.equals("String")) {
				returntype = "";
			}
			if (returntype.equals("int")) {
				returntype = "";
			}
			if (returntype.equals("float")) {
				returntype = "";
			}
			if (returntype.equals("Integer")) {
				returntype = "";
			}
			if (returntype.equals("double")) {
				returntype = "";
			}
			if (returntype.equals("boolean")) {
				returntype = "";
			}
			if (returntype.equals("byte")) {
				returntype = "";
			}
			if (returntype.equals("char")) {
				returntype = "";
			}
			if (returntype.equals("object")) {
				returntype = "";
			}
			if (returntype.equals("char")) {
				returntype = "";
			}
			if (returntype.equals("V")) {
				returntype = "";
			}
			if (returntype.equals("Z")) {
				returntype = "";
			}
			if (returntype.equals("I")) {
				returntype = "";
			}
			if (returntype.equals("K")) {
				returntype = "";
			}

			result[i][0] = keyword;
			result[i][1] = returntype + " " + str + "(" + returntype + ")";
			i = i + 1;

		}
		
		
		return result;

	}

	private void addDebugCodeKeywordsToCodeKeyWords(DebugCode debugCode) {

		KeyWordsCandidates debugCodeCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		if (debugCode != null && debugCode.getSyntacticBlock() != null) {
			// 取代码等信息
			// 代码需要过滤停用词

			String codeString = debugCode.getSyntacticBlock().getCode();

			if (codeString != null && !codeString.equals("")) {
				codeString = CommUtil.removeStopWordsAsString(codeString);

				if (codeString == null || codeString.equals("")) {
					return;
				}

	
				String exceptionalString = debugCode.getSyntacticBlock()
						.getExceptionName();
				String rString = codeString + " ; " + exceptionalString;

				rString = CommUtil.removeDuplicateWords(CommUtil
						.stringToList(rString));

				String[][] apinameMap = getVarialToAPImap(debugCode
						.getClassModel());

				for (String str : (rString.split("[;]"))) {
					
					if (str.contains("helpseeking")) {
						System.out.println("here here 1");
					}
					
					boolean flag = false;
					
					if (str.contains(".java")) {
						continue;
					}
					
                if (apinameMap!=null) {
					
				
					for (int i = 0; i < apinameMap.length; i++) {

						String keyvalue = apinameMap[i][0];
						String value = apinameMap[i][1];
						// System.out.println(keyvalue+":"+value+":"+str);
						if (keyvalue.toLowerCase().contains(str.trim().toLowerCase())) {
							str = value;
							flag = true;
							break;
						}

					}
                }

                
                
					KeyWord kw = new KeyWord();
					
					kw.setKeywordName(str.trim());
					

					if (Basic.ALGORITHMSELECTION==2) {
						
						kw.setWeightOne(Basic.action_saveOrDebug); // interest level action  save or debug 
						Action ac=actions.getActionCachewithActionID(currentID).getAction();
						if (ac!=null)
						kw.setTagName(genTagName(ac,ac.actionName.toLowerCase()));
						
						kw.setWeightTwo(Basic.api_normal); //interest level api  normal
						kw.setTagName("Other");
						
						if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
							kw.setWeightOne(Basic.action_selectfocus);
							
							if (ac.getActionName().toLowerCase().contains("compile")) {
								kw.setWeightTwo(Basic.api_hasCompileError);
							}
							if (ac.getActionName().toLowerCase().contains("console")) {
								kw.setWeightTwo(Basic.api_causeException);
							}
							if (ac.getActionName().toLowerCase().contains("insertcursor")) {
							     kw.setWeightTwo(Basic.api_hasCompileError);
								}


						}
						
					}else
					if (Basic.ALGORITHMSELECTION==1) {				

					kw.setWeightOne(2);
					kw.setTagName("Other");
					}
					
					for (String jestr : Basic.javaExceptionalNameList) {

						if (str.trim().toLowerCase().contains(
								"Exception".toLowerCase())) {
							
							if (Basic.ALGORITHMSELECTION==1) {				
							kw.setWeightOne(4);
							}
							
							kw.setTagName("Exception");

							flag = true;
							break;
						}

						if (str.trim().equals(jestr)) {
							if (Basic.ALGORITHMSELECTION==1) {				
							kw.setWeightOne(4);
							}
							
							kw.setTagName("Exception");
							flag = true;
							break;
						}
					}

					
					//赋初值  
					if (flag) {
						if (Basic.ALGORITHMSELECTION==1)	
						kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
						
						if (Basic.ALGORITHMSELECTION==2)	
							kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
						
						tempKeyWords.add(kw);

					}
					

				}
				
				
				debugCodeCandidates.setActionID(currentID); // 不跟随动作删除
				debugCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Six);
				debugCodeCandidates.setOldStep(5);
				debugCodeCandidates.setKeyWords(tempKeyWords);
				codeKeyWords.add(debugCodeCandidates);		
				}


			

		}

	}

	
	//actionname 请小写
	private String genTagName(Action ac, String actionName) {
		String tagName="other # other";

		switch (ac.getActionKind().toString().toLowerCase().trim()) {
		case "selection":
		case "select":
		case "edit":
			
			tagName="select # api "+actionName;
			break;
			
		case "debug":
					
			tagName="select # api "+actionName;
			if (actionName.contains("Run Java Application".toLowerCase())) {
				tagName="exception # "+actionName;
						
			}
			
			break;
		
		case "command":
			if (actionName.contains("save")) {
				tagName="error #"+actionName;

			}


		default:
			tagName="other # "+actionName;
			break;
		}

		 return null;
	}

	public  double vinit(double weightOne, double weightTwo) {

		double result=1.0;
		result=Math.pow(Basic.gama, weightOne)*weightTwo;
		
		return result;
	}

	private void addCurrentClassModelKeywordsToClassmodelKeyWords(int mode) {

		// 从classmodel中获得调用的API信息
		// 目前先只关注 当前代码调用的API以及其下一个类

		KeyWordsCandidates classModelCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		if (lastClassModelID < currentClassModelID) {

			if (getCurrentClassModel() != null) {
				// 取消息 mode=1; // 意思是暂时取callee 和 belowclass

				// String
				// caller=CommUtil.ListToString(getCurrentClassModel().getInternalCaller());
				List<String> callee = getCurrentClassModel()
						.getInternalCallee();
				// String
				// upclass=CommUtil.ListToString(getCurrentClassModel().getUpClass());
				
				String qualifiedName=getCurrentClassModel().getQualifiedName();
												
				List<String> belowclass = getCurrentClassModel()
						.getBelowClass();

				if (mode == 1) {

					// 方法的qualified name 中，intercallee中包含自己的类名信息需要去除
					String finalcalleestr = "";
					// 将自己加入到 处理中
					if (qualifiedName!=null  && !qualifiedName.trim().equals("")) {
						finalcalleestr=qualifiedName;

					}

					if (callee != null )
						if ( callee.size() > 0) {

						for (String calleestr : callee) {
//							String[] methodnamestr = calleestr
//									.split(Basic.SPLIT_STRING);
							
						


							if (getCurrentClassModel().calleeInfo!=null && getCurrentClassModel().calleeInfo.get(calleestr)!=null){
							MethodInfo mif = getCurrentClassModel().calleeInfo
									.get(calleestr);
							
							String calleequrifyname="";
							if (mif!=null) {
								calleequrifyname = combileMethodQurifyname(
									mif.getReturnType(), calleestr,
									mif.getParaTypes());
							}

						

							if (finalcalleestr.equals("")) {
								finalcalleestr = calleequrifyname;
							} else {
								finalcalleestr = finalcalleestr + ";"
										+ calleequrifyname;
							}
							}

						}
					}

					String result1 = finalcalleestr;

					if (belowclass != null && belowclass.size() > 0) {
						for (int i = 0; i < belowclass.size(); i++) {
							result1 = result1 + ";" + belowclass.get(0);
						}

					}

					result1 = CommUtil.removeDuplicateWords(CommUtil
							.stringToList(result1, "[;]"));

					for (String str : CommUtil
							.arrayToList(result1.split("[;]"))) {

						if (str.contains(".java") || str.trim().equals("")) {
							continue;
						}

						if (str.contains("helpseeking")) {
							System.out.println("here here 2");
						}

						
						
						KeyWord kw = new KeyWord();
						kw.setKeywordName(str.trim());
						
						if (Basic.ALGORITHMSELECTION==1) {				
						kw.setWeightOne(2);
						}
						
						if (Basic.ALGORITHMSELECTION==2) {				
							kw.setWeightOne(Basic.action_reveal);
							kw.setWeightTwo(Basic.api_normal);
							Action ac = actions.getActionCachewithActionID(
									currentID).getAction();
							if (ac != null) {
								if (ac.getActionKind().equals(
										Kind.SELECTIONFOCUS)) {
									kw.setWeightOne(Basic.action_selectfocus); //Basic.action_reveal

									if (ac.getActionName().toLowerCase()
											.contains("compile")) {
										kw.setWeightTwo(Basic.api_hasCompileError);
									}
									if (ac.getActionName().toLowerCase()
											.contains("console")) {
										kw.setWeightTwo(Basic.api_causeException);
									}

								}

							}

						}

						
						for (String jestr : Basic.javaExceptionalNameList) {
							if (str.equals(jestr)) {
								
								if (Basic.ALGORITHMSELECTION==1) {				
								kw.setWeightOne(4);
								}
								
								break;
							}
						}
						
						if (Basic.ALGORITHMSELECTION==1) {				
						kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
						}
						
						if (Basic.ALGORITHMSELECTION==2) {				
							kw.setScore(vinit(kw.getWeightOne(), kw.getWeightTwo()));
						}
						
						kw.setTagName("API");
						tempKeyWords.add(kw);
					}

					classModelCandidates.setActionID(currentID);
					classModelCandidates
							.setLevel(Basic.KeyWordsLevel.Level_Three);
					classModelCandidates.setOldStep(8);
					classModelCandidates.setKeyWords(tempKeyWords);

					classmodelKeyWords.add(classModelCandidates);

				}
			}
		}

	}

	private void adjustProblemsKeywords() {
		// problem消息的权重其次，第一个error权重最高7，其次是warning 6

		if (problems == null) {
			return;
		}

		if (!compareProblemsAndCopy()) {

			return;

		}
		if (oldProblemsCopy == null) {
			return;
		}

		KeyWordsCandidates problemCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		ArrayList<ProblemInformation> errorList = oldProblemsCopy
				.getErrorList();
		ArrayList<ProblemInformation> warningList = oldProblemsCopy
				.getWarningList();

		if (errorList != null && errorList.size() > 0) {

			for (ProblemInformation pif : errorList) {

				String des = pif.getDescription();
				if (des != null && !des.trim().equals("")) {
					// 取消息
					for (String str : des.split(Basic.SPLIT_STRING)) {
						if (str.trim().equals("")) {
							continue;
						}
						
						if (str.contains("helpseeking")) {
							System.out.println("here here 8");
						}

						
						KeyWord kw = new KeyWord();
						kw.setKeywordName(str.trim());

						
						   if (Basic.ALGORITHMSELECTION==1) {
						kw.setWeightOne(4);
						kw.setWeightTwo(1);
						kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
						kw.setTagName("Error");
						   }
						   
						    if (Basic.ALGORITHMSELECTION==2) {
						    	kw.setWeightOne(Basic.action_saveOrDebug);
						    	kw.setWeightTwo(Basic.api_hasCompileError);
								kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
								kw.setTagName("error #  has compile error  edit save debug");
								
								Action ac = actions.getActionCachewithActionID(currentID).getAction();
								if (ac != null) {
								if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
									kw.setWeightOne(Basic.action_selectfocus);
							           if (ac.getActionName().toLowerCase().contains("compile")) {
										kw.setWeightTwo(Basic.api_hasCompileError);
										}
									if (ac.getActionName().toLowerCase().contains("console")) {
									     kw.setWeightTwo(Basic.api_causeException);
										}
									if (ac.getActionName().toLowerCase().contains("insertcursor")) {
									     kw.setWeightTwo(Basic.api_hasCompileError);
										}

							       }

								}
						    }

						
						tempKeyWords.add(kw);
					}
				}
			}

		}

		if (warningList != null && warningList.size() > 0) {
			for (ProblemInformation pif : warningList) {

				String des = pif.getDescription();
				if (des != null && !des.trim().equals("")) {
					// 取消息
					for (String str : des.split(Basic.SPLIT_STRING)) {
						if (str.trim().equals("")) {
							continue;
						}
						
						if (str.contains("helpseeking")) {
							System.out.println("here here 9");
						}

						
						KeyWord kw = new KeyWord();
												
						
						kw.setKeywordName(str.trim());
						
					    if (Basic.ALGORITHMSELECTION==1) {

						kw.setWeightOne(4);
						kw.setWeightTwo(1);
						kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
						kw.setTagName("Other");
					    }
					    
					    if (Basic.ALGORITHMSELECTION==2) {
					    	kw.setWeightOne(Basic.action_select);
					    	kw.setWeightTwo(Basic.api_hasCompileError);
							kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
							kw.setTagName("warning #  has compile warning  edit save debug");
							
							Action ac = actions.getActionCachewithActionID(currentID).getAction();
							if (ac != null) {
							if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
								kw.setWeightOne(Basic.action_selectfocus);
						           if (ac.getActionName().toLowerCase().contains("compile")) {
									kw.setWeightTwo(Basic.api_hasCompileError);
									}
								if (ac.getActionName().toLowerCase().contains("console")) {
								     kw.setWeightTwo(Basic.api_causeException);
									}
								if (ac.getActionName().toLowerCase().contains("insertcursor")) {
								     kw.setWeightTwo(Basic.api_hasCompileError);
									}

						       }

							}
					    }
						
						tempKeyWords.add(kw);
					}
				}
			}
		}

		problemCandidates.setActionID(currentID); // 不跟随动作删除
		problemCandidates.setLevel(Basic.KeyWordsLevel.Level_Two);
		problemCandidates.setOldStep(5);
		problemCandidates.setKeyWords(tempKeyWords);

		problemCandidates.setFrequency(findyoufrequency(currentID));

		problemCacheKeyWords.add(problemCandidates);

	}

	public int findyoufrequency(int currentid) {
		int result = 1;
		boolean flag = false;
		for (int i = 0; i < acFrequencysList.size(); i++) {
			List<Integer> actionIn = acFrequencysList.get(i).getActionIDList();
			for (int j = 0; j < actionIn.size(); j++) {
				if (currentid == actionIn.get(j)) {
					flag = true;
					break;
				}
			}
			if (flag) {
				result = acFrequencysList.get(i).getFrequency();
			}

		}
		return result;
	}

	static String lastsearchwords = "";
	List<KeyWordsCandidates> historysearchlist=new ArrayList<KeyWordsCandidates>();
	

	public List<KeyWordsCandidates> getHistorysearchlist() {
		return historysearchlist;
	}

	public void setHistorysearchlist(List<KeyWordsCandidates> historysearchlist) {
		this.historysearchlist = historysearchlist;
	}

	public static String getLastsearchwords() {
		return lastsearchwords;
	}

	public static void setLastsearchwords(String lastsearchwords) {
		Cache.lastsearchwords = lastsearchwords;
	}

	private void adjustConsolesKeywords() {
		// console消息的exceptional权重最高7 (8)
		if (consoles == null) {
			return;
		}
		// false 表示有新行加入
		if (!compareConsolesAndCopy()) {
			return;
		}

		if (oldConsolesCopy == null) {
			return;
		}

		KeyWordsCandidates consoleCandidates = new KeyWordsCandidates();

		List<KeyWord> tempKeyWords = new ArrayList<>();

		// setConsolesSize(consoles.getExceptionList().size());

		for (ConsoleInformationListCopy kwc : oldConsolesCopy) {
			String exceptionName = kwc.getExceptionName();

			exceptionName = CommUtil.removeStopWordsAsString(exceptionName);

			if (exceptionName == null) {
				break;
			}

			exceptionName = CommUtil.removeDuplicateWords(CommUtil
					.stringToList(exceptionName));

			for (String str : exceptionName.split("[;]")) {
				if (str.trim().equals("") ) {
					continue;
				}

				if (str.contains("helpseeking")) {
					System.out.println("here here 6");
				}

				
				if (!str.trim().equals("")) {
					KeyWord kw = new KeyWord();
					
				    kw.setKeywordName(str.trim());
					
				    if (Basic.ALGORITHMSELECTION==1) {

					kw.setWeightOne(4);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Exception");
				    }
				    
				    if (Basic.ALGORITHMSELECTION==2) {
				    	kw.setWeightOne(Basic.action_execute);
				    	kw.setWeightTwo(Basic.api_causeException);
						kw.setScore(vinit(kw.getWeightOne(),kw.getWeightTwo()));
						kw.setTagName("exception # execute cause Exception");
						Action ac = actions.getActionCachewithActionID(currentID).getAction();
						if (ac != null) {
						if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
							kw.setWeightOne(Basic.action_selectfocus);
					           if (ac.getActionName().toLowerCase().contains("compile")) {
								kw.setWeightTwo(Basic.api_hasCompileError);
								}
							if (ac.getActionName().toLowerCase().contains("console")) {
							     kw.setWeightTwo(Basic.api_causeException);
								}
							if (ac.getActionName().toLowerCase().contains("insertcursor")) {
							     kw.setWeightTwo(Basic.api_hasCompileError);
								}

					       }

						}
				    }
					
				    tempKeyWords.add(kw);
				}

			}

			String description = kwc.getDescription();

			if (description != null && !description.trim().equals("")) {
				// 替换为直接的异常消息文本串
				KeyWord kw2 = new KeyWord();

				String filteredterms = doTermFilter(description);
				filteredterms = CommUtil.removeStopWordsAsString(filteredterms);

				if (filteredterms == null) {
					continue;
				}

				
				if (filteredterms.contains("helpseeking")) {
					System.out.println("here here 7");
				}

				
				filteredterms = CommUtil.removeDuplicateWords(CommUtil
						.stringToList(filteredterms));

				kw2.setKeywordName(filteredterms);
				
				//普通描述文本 视为 代码级别
				 if (Basic.ALGORITHMSELECTION==1) {

				kw2.setWeightOne(4);
				kw2.setWeightTwo(1);
				kw2.setScore(kw2.getWeightOne() * kw2.getWeightTwo());
				kw2.setTagName("Exception");
				 }
				    
				 if (Basic.ALGORITHMSELECTION==2) {
				    	kw2.setWeightOne(Basic.action_reveal);
				    	kw2.setWeightTwo(Basic.api_normal);
						kw2.setScore(vinit(kw2.getWeightOne(),kw2.getWeightTwo()));
						kw2.setTagName("exception # execute cause Exception");
						
						Action ac = actions.getActionCachewithActionID(currentID).getAction();
						if (ac != null) {
						if (ac.getActionKind().equals(Kind.SELECTIONFOCUS)) {
							kw2.setWeightOne(Basic.action_selectfocus);
					           if (ac.getActionName().toLowerCase().contains("compile")) {
								kw2.setWeightTwo(Basic.api_hasCompileError);
								}
							if (ac.getActionName().toLowerCase().contains("console")) {
							     kw2.setWeightTwo(Basic.api_causeException);
								}
							if (ac.getActionName().toLowerCase().contains("insertcursor")) {
							     kw2.setWeightTwo(Basic.api_hasCompileError);
								}

					       }

						}
				    }
			 

			tempKeyWords.add(kw2);

			}

		}

		consoleCandidates.setActionID(currentID); // 不跟随动作删除
		consoleCandidates.setLevel(Basic.KeyWordsLevel.Level_One);
		consoleCandidates.setOldStep(3);
		consoleCandidates.setKeyWords(tempKeyWords);
		consoleCacheKeyWords.add(consoleCandidates);

	}

	private String doTermFilter(String description) {

		String des;

		List<String> mydesList = CommUtil.arrayToList(description.split("[ ]"));
		// 查路径 //check file path string 3 "\" or 3 "/"
		for (int i = 0; i < mydesList.size(); i++) {
			String[] checkslahoneList = mydesList.get(i).split("\\\\");
			if (checkslahoneList.length > 2) {
				mydesList.remove(i);

			}
			// 查包名

		}
		// TODO 今后去掉更多的异常格式数据

		des = CommUtil.ListToString(mydesList);

		
		return des;
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

	public int findyouDistance(int actionID) {
		int index = actions.actionList.size();
		int distance = index;

		for (int i = 0; i < index; i++) {
			if (actions.actionList.get(i).getActionID() == actionID) {
				distance = actions.actionList.get(i).getDistence();
			}
		}

		return distance;
	}

}
