package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.fudan.se.helpseeking.processing.CacheProcessing;
import cn.edu.fudan.se.helpseeking.util.CommUtil;

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
	List<ConsoleInformationListCopy> oldConsolesCopy=null;
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
	ProblemInformationListCopy oldProblemsCopy=null;
	List<String> taskDescription;
	

	private int problemsSize=0;


	private boolean  compareProblemsAndCopy() {
		boolean result=true;

		
		ProblemInformationListCopy  tempProblemsCopy=new ProblemInformationListCopy();
		 ArrayList<ProblemInformation> errorList= new ArrayList<>();
		 ArrayList<ProblemInformation> warningList=new ArrayList<>();
		
		
		boolean resultError=true;
		
		
		for (ProblemInformation pifE : problems.getErrorList()) {
			
			if (problems.getErrorList().size()<=0) {
				tempProblemsCopy.setErrorList(null);
				break;
			}
			
			if (oldProblemsCopy!=null && oldProblemsCopy.getErrorList()!=null) {
				boolean flagError=true;
                
				for (ProblemInformation pifError : oldProblemsCopy.getErrorList()) {
					if (pifE.getSeverity()==pifError.getSeverity()) {
						if (pifE.getDescription().equals(pifError.getDescription())) {
							if (pifE.getRelatedMethod().equals(pifError.getRelatedMethod())) {
								
								if (pifE.getLineNumber()==pifError.getLineNumber()) {
									
									flagError=false;
									
									
								}
							}
							
							
						}
						
					}
				}
				
				if (flagError) {
					resultError=false;
					ProblemInformation errorInformation=new ProblemInformation();
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
				
				
			}else {
				resultError=false;
				ProblemInformation errorInformation=new ProblemInformation();
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

		
		boolean resultWarning=true;	 
		
		for (ProblemInformation pifE : problems.getWarningList()) {
			if (problems.getWarningList().size()<=0) {
				tempProblemsCopy.setWarningList(null);
				break;
			}
			
			if (oldProblemsCopy!=null && oldProblemsCopy.getWarningList()!=null) {
				boolean flagWarning=true;
                
				for (ProblemInformation pifWarning : oldProblemsCopy.getWarningList()) {
					if (pifE.getSeverity()==pifWarning.getSeverity()) {
						if (pifE.getDescription().equals(pifWarning.getDescription())) {
							if (pifE.getRelatedMethod().equals(pifWarning.getRelatedMethod())) {
								
								if (pifE.getLineNumber()==pifWarning.getLineNumber()) {
									
									flagWarning=false;
									
									
								}
							}
							
							
						}
						
					}
				}
				
				if (flagWarning) {
					resultWarning=false;
					ProblemInformation warningInformation=new ProblemInformation();
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
				
				
			}else {
				resultWarning=false;
				ProblemInformation warningInformation=new ProblemInformation();
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
			result=false;
		}
		
		
		
		return result;
	}
	
	

	private boolean  compareConsolesAndCopy() {
		boolean result=true;

		
		List<ConsoleInformationListCopy>  tempConsolesCopy=new ArrayList<>();
		
		for (ConsoleInformation cif : consoles.getExceptionList()) {
			
			if (oldConsolesCopy!=null) {
			
			boolean flag=true;
			for (ConsoleInformationListCopy ciflc : oldConsolesCopy) {
				if (cif.getExceptionName().equals(ciflc.getExceptionName())) {
					if (cif.getDescription().equals(ciflc.getDescription())) {
						flag=false;
					}
				}
				
				
			}
			
			if (flag) {

				result=false;
				ConsoleInformationListCopy newNode=new ConsoleInformationListCopy();
				newNode.setDescription(cif.getDescription());
				newNode.setExceptionName(cif.getExceptionName());
				tempConsolesCopy.add(newNode);		
				
			}
			
				
			}  
			else {
				result=false;
				ConsoleInformationListCopy newNode=new ConsoleInformationListCopy();
				newNode.setDescription(cif.getDescription());
				newNode.setExceptionName(cif.getExceptionName());
				tempConsolesCopy.add(newNode);
			}
			
			
		}		
		
		if (!result) {
			oldConsolesCopy=tempConsolesCopy;
		}
		
		
		return result;
	}
	
	
	// 单例实现
	private Cache() {

//		System.out.println("Cache.Cache()");
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
		
//		增加频率
	
		boolean flagCount=false;
		for (int j1 = 0; j1 < acFrequencysList.size(); j1++) {
			
			if (acFrequencysList.get(j1).getActionKind().equals(action.getActionKind()) && acFrequencysList.get(j1).getActionName().equals(action.getActionName()) ) {
				
				float fqc=acFrequencysList.get(j1).getFrequency();
				fqc=fqc+1;
				acFrequencysList.get(j1).setFrequency(fqc);
				
				flagCount=true;
			}
		}
		
		if (flagCount) {
			ActionInformation aif=new ActionInformation();
			aif.setActionID(id);
			aif.setActionKind(action.getActionKind());
			aif.setActionName(action.getActionName());
			aif.setFrequency((float)1.0);
			
		}
	
		
		int removeActionID = actions.addSizeReturnRemoveActionID();
		
		if (removeActionID != -1) // 则说明动作已经放满了滑动窗口 ， 这时考虑不在窗口内的信息从信息集合中移除
		{
		

			
			// 最简单的策略直接删除， 后续需要可配置功能
			removeInformationNaive(removeActionID);
			
		//			移除频率			
		
			for (int j1 = 0; j1 < acFrequencysList.size(); j1++) {
				
				if (acFrequencysList.get(j1).getActionKind().equals(action.getActionKind()) && acFrequencysList.get(j1).getActionName().equals(action.getActionName()) ) {
					
					float fqc=acFrequencysList.get(j1).getFrequency();
					fqc=fqc-1;
					acFrequencysList.get(j1).setFrequency(fqc);
					
					break;
				}
			}

		}
		
		//求同类动作频率

		
		

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
		
//		
//		//添加到cache1中
//		Cache1 myCache1=Cache1.getInstance();
//		myCache1.addInformationToCache1(information, actionID);
		
	
			

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

		//移除候选关键词
				
//				List<KeyWordsCandidates> classmodelKeyWords=new ArrayList<>();
////				List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
////				List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
//				List<KeyWordsCandidates> codeKeyWords=new ArrayList<>();
//				List<KeyWordsCandidates> relatedExplorerKeyWords=new ArrayList<>();
				
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
		currentID =actionID;
		infq.setId(currentID);
		infq.setInformation(information);

		informations.add(infq);

//		System.out.println("显示连续的编辑动作的动作名："+information.getAction().getActionKind().toString()+":"+information.getAction().getActionName()+"\n动作细节："+information.getAction().getDescription());
		
		addActions(information.getAction(), currentID);

		if (information.getDebugCode() != null) {
			addDebugCode(information.getDebugCode(), currentID);
			addDebugCodeKeywordsToCodeKeyWords(information.getDebugCode());
			if (information.getDebugCode().getClassModel()!=null) {
				setCurrentClassModel(information.getDebugCode().getClassModel());
			addCurrentClassModelKeywordsToClassmodelKeyWords(1);//mode=1 取callee below class
			}
			
		}

		if (information.getEditCode() != null) {

			addEditCode(information.getEditCode(), currentID);
			addEditCodeKeywordsToCodeKeyWords(information.getEditCode());
			if (information.getEditCode().getClassModel()!=null) {
				setCurrentClassModel(information.getEditCode().getClassModel());
				addCurrentClassModelKeywordsToClassmodelKeyWords(1);//mode=1 取callee below class
			}
			
		}

//		if (information.getIdeOutput() != null) {
//			addIDEOutput(information.getIdeOutput(), currentID);
//			addKeywordsToConsoleviewKeywords(information.getIdeOutput().getRuntimeInformation());
//			addKeywordsToProblemViewKeywords(information.getIdeOutput().getCompileInformation());
//		}

		if (information.getExplorerRelated() != null) {
			addExplorerRelated(information.getExplorerRelated(), currentID);
			addKeywordsEditorInfoToRelatedExplorerKeywords(information.getExplorerRelated().getEditorInfo());
			addKeywordsExplorerInfoToRelatedExplorerKeyWords(information.getExplorerRelated().getExplorerInfo());
		}
		
		adjustConsolesKeywords();
		adjustProblemsKeywords();

		// 这里记得安排对每个新加入信息后的预处理处理(如有必要)

		// 使用线程
		CacheProcessing cpThread = new CacheProcessing();
		cpThread.run();
	}

	
	

	List<ActionInformation> acFrequencysList=new ArrayList<>();





	public List<ActionInformation> getAcFrequencysList() {
		return acFrequencysList;
	}
	public void setAcFrequencysList(List<ActionInformation> acFrequencysList) {
		this.acFrequencysList = acFrequencysList;
	}


	List<KeyWordsCandidates> consoleCacheKeyWords=new ArrayList<>();
	List<KeyWordsCandidates> problemCacheKeyWords=new ArrayList<>();
	List<KeyWordsCandidates> classmodelKeyWords=new ArrayList<>();
//	List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
//	List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
	List<KeyWordsCandidates> codeKeyWords=new ArrayList<>();
	List<KeyWordsCandidates> relatedExplorerKeyWords=new ArrayList<>();


	
	
	
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
	public void setClassmodelKeyWords(List<KeyWordsCandidates> classmodelKeyWords) {
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
		
		KeyWordsCandidates  explorerInfoCodeCandidates=new KeyWordsCandidates();

		List<KeyWord> tempKeyWords=new ArrayList<>();

		if (explorerInfo!=null)
		{
			if (explorerInfo.getSelectObjectNameList()!=null|| explorerInfo.getSelectObjectNameList().size()>0) {


				// 取消息
				for (String str : explorerInfo.getSelectObjectNameList()) 
				{
					if ((str==null) ||str.equals("")) {
						break;
					}
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Other");
					tempKeyWords.add(kw);
				}
			}

			explorerInfoCodeCandidates.setActionID(currentID); //不跟随动作删除
			explorerInfoCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Seven);
			explorerInfoCodeCandidates.setOldStep(3);
			explorerInfoCodeCandidates.setKeyWords(tempKeyWords);
			relatedExplorerKeyWords.add(explorerInfoCodeCandidates);
			
		}
		
	}
	
	
	private void addKeywordsEditorInfoToRelatedExplorerKeywords(
			EditorInfo editorInfo) {
		
		KeyWordsCandidates  editorInfoCodeCandidates=new KeyWordsCandidates();

		List<KeyWord> tempKeyWords=new ArrayList<>();

		if (editorInfo!=null)
		{
			if (editorInfo.getClassQualifiedNameList()!=null|| editorInfo.getClassQualifiedNameList().size()>0) {
				// 取消息
				for (String str : editorInfo.getClassQualifiedNameList()) 
				{
					if ((str==null) ||str.equals("")) {
						break;
					}
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Other");
					tempKeyWords.add(kw);
				}
			}

			editorInfoCodeCandidates.setActionID(currentID); //不跟随动作删除
			editorInfoCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Seven);
			editorInfoCodeCandidates.setOldStep(3);
			editorInfoCodeCandidates.setKeyWords(tempKeyWords);
			relatedExplorerKeyWords.add(editorInfoCodeCandidates);
			
		}
		
	}
	
	
	
	private void addEditCodeKeywordsToCodeKeyWords(EditCode editCode) {

		KeyWordsCandidates  editCodeCandidates=new KeyWordsCandidates();

		List<KeyWord> tempKeyWords=new ArrayList<>();

		if (editCode!=null && editCode.getSyntacticBlock()!=null)
		{
			// 取代码等信息
			//代码需要过滤停用词
			String codeString=editCode.getSyntacticBlock().getCode();
			
			codeString=CommUtil.removeStopWordsAsString(codeString);
			
if (codeString==null) {
	return;
}
			String exceptionalString=editCode.getSyntacticBlock().getExceptionName();
			String rString=codeString+";"+exceptionalString;

			for (String str : (rString.split(Basic.SPLIT_STRING))) 
			{
				KeyWord kw=new KeyWord();
				kw.setKeywordName(str.trim());
				kw.setWeightOne(2);
				kw.setTagName("Other");

				for (String jestr : Basic.javaExceptionalNameList) {
					if (str.equals(jestr)) {
						kw.setWeightTwo(2);
						kw.setTagName("Exception");
						break;
					}
				}
				kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
				tempKeyWords.add(kw);

			}
			
			editCodeCandidates.setActionID(currentID); //不跟随动作删除
			editCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Six);
			editCodeCandidates.setOldStep(5);
			editCodeCandidates.setKeyWords(tempKeyWords);
			codeKeyWords.add(editCodeCandidates);
			
		}

		
	}

	
	private void addDebugCodeKeywordsToCodeKeyWords(DebugCode debugCode) {

		KeyWordsCandidates  debugCodeCandidates=new KeyWordsCandidates();
	    
		List<KeyWord> tempKeyWords=new ArrayList<>();

		if (debugCode!=null && debugCode.getSyntacticBlock()!=null)
		{
			// 取代码等信息
			//代码需要过滤停用词
			
			String codeString=debugCode.getSyntacticBlock().getCode();
			
			if (codeString!=null&& !codeString.equals("")) {
				codeString=CommUtil.removeStopWordsAsString(codeString);

				String exceptionalString=debugCode.getSyntacticBlock().getExceptionName();
				String rString=codeString+";"+exceptionalString;

				for (String str : (rString.split(Basic.SPLIT_STRING))) 
				{
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(2);
					kw.setTagName("Other");

					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.equals(jestr)) {
							kw.setWeightTwo(2);
							kw.setTagName("Exception");
							break;
						}
					}
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					tempKeyWords.add(kw);

				}
				
				debugCodeCandidates.setActionID(currentID); //不跟随动作删除
				debugCodeCandidates.setLevel(Basic.KeyWordsLevel.Level_Six);
				debugCodeCandidates.setOldStep(5);
				debugCodeCandidates.setKeyWords(tempKeyWords);
				codeKeyWords.add(debugCodeCandidates);
				
			}
			
			
		}

		
	}
	
	

	private void addCurrentClassModelKeywordsToClassmodelKeyWords(int mode) {

		//从classmodel中获得调用的API信息
		//	  目前先只关注 当前代码调用的API以及其下一个类

		KeyWordsCandidates  classModelCandidates=new KeyWordsCandidates();
		
		List<KeyWord> tempKeyWords=new ArrayList<>();

		if (getCurrentClassModel()!=null)
		{
			// 取消息  mode=1; // 意思是暂时取callee 和 belowclass

//			String  caller=CommUtil.ListToString(getCurrentClassModel().getInternalCaller());
			String  callee=CommUtil.ListToString(getCurrentClassModel().getInternalCallee());
//			String upclass=CommUtil.ListToString(getCurrentClassModel().getUpClass());
			String belowclass=CommUtil.ListToString(getCurrentClassModel().getBelowClass());

			if (mode==1) 
			{

				//方法的qualified name 中，intercallee中包含自己的类名信息需要去除
				String finalcalleestr="";

				if (callee!=null && !callee.equals("")) {


					String[] calleearray=callee.split("[;]");
					if (calleearray.length>0) {
						for (String calleestr :calleearray ) {
							String[] methodnamestr=calleestr.split(Basic.SPLIT_STRING);
							int lastindex=methodnamestr.length;
							for (int i =lastindex-1; i >0; i--) {
								String laststr=methodnamestr[i];
								if (!laststr.trim().equals("")) {
									finalcalleestr=finalcalleestr.trim()+laststr+";";
									break;
								}
							}

						}
					}
				}

				String result1=finalcalleestr;

				if (belowclass!=null && !belowclass.equals("")) {
					result1=result1+belowclass;
				}
				



				for (String str : CommUtil.arrayToList(result1.split("[;]"))) 
				{
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(6);
					for (String jestr : Basic.javaExceptionalNameList)
					{
						if (str.equals(jestr))
						{
							kw.setWeightTwo(2);
							break;
						}
					}
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("API");
					tempKeyWords.add(kw);
				}

				classModelCandidates.setActionID(currentID); 
				classModelCandidates.setLevel(Basic.KeyWordsLevel.Level_Three);
				classModelCandidates.setOldStep(8);
				classModelCandidates.setKeyWords(tempKeyWords);
				
				
	          
				classmodelKeyWords.add(classModelCandidates);
				

			}
		}
		
		
	}
	
	private void adjustProblemsKeywords() {
		//problem消息的权重其次，第一个error权重最高7，其次是warning 6
		
		if (problems==null) {
			return;
		}
		
		if (!compareProblemsAndCopy()) {
			
		return;
			
		}
		if (oldProblemsCopy==null) {
			return;
		}
		
		KeyWordsCandidates  problemCandidates=new KeyWordsCandidates();
		
		List<KeyWord> tempKeyWords=new ArrayList<>();
		
		ArrayList<ProblemInformation> errorList=oldProblemsCopy.getErrorList();
		ArrayList<ProblemInformation> warningList=oldProblemsCopy.getWarningList();
		
				if (errorList!=null && errorList.size()>0) {

					for (ProblemInformation pif : errorList) {
						
					String des=pif.getDescription();
					if (des!=null && !des.trim().equals(""))
					{
						// 取消息
						for (String str : des.split(Basic.SPLIT_STRING)) 
						{
							if (str.trim().equals("")) {
								continue;
							}
							KeyWord kw=new KeyWord();
							kw.setKeywordName(str.trim());
							kw.setWeightOne(7);
							kw.setWeightTwo(2);
							kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
							kw.setTagName("Error");
							tempKeyWords.add(kw);
						}
					}
					}

					
				}
				
			if (warningList!=null && warningList.size()>0) {
				for (ProblemInformation pif : warningList) {
					
				
						
						String des=pif.getDescription();
						if (des!=null && !des.trim().equals(""))
						{
							// 取消息
							for (String str : des.split(Basic.SPLIT_STRING)) 
							{
								if (str.trim().equals("")) {
									continue;
								}
								KeyWord kw=new KeyWord();
								kw.setKeywordName(str.trim());
								kw.setWeightOne(6);
								kw.setWeightTwo(1);
								kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
								kw.setTagName("Other");
								tempKeyWords.add(kw);
							}
						}
				}
					}
			
			problemCandidates.setActionID(-1); //不跟随动作删除
			problemCandidates.setLevel(Basic.KeyWordsLevel.Level_Two);
			problemCandidates.setOldStep(5);
			problemCandidates.setKeyWords(tempKeyWords);
			
			problemCacheKeyWords.add(problemCandidates);

		
	}
	
	
	
	
	
	private void adjustConsolesKeywords() {
		//console消息的exceptional权重最高7 (8)
	if (consoles==null) {
			return ;
		}
	//false 表示有新行加入
		if (!compareConsolesAndCopy()) {
			return;
			}
			
		if (oldConsolesCopy==null) {
			return;
		}
			
	KeyWordsCandidates  consoleCandidates=new KeyWordsCandidates();
    
	List<KeyWord> tempKeyWords=new ArrayList<>();
	
//	setConsolesSize(consoles.getExceptionList().size());
			
			for ( ConsoleInformationListCopy kwc : oldConsolesCopy) {
				String exceptionName=kwc.getExceptionName();
				
				for (String str : exceptionName.split(Basic.SPLIT_STRING)) 
				{
					if (str.trim().equals("")) {
						continue;
					}
				
				if (!str.trim().equals("")) {
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(8);
					
					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.trim().equals(jestr)) {
							kw.setWeightTwo(3);
					     break;		
						}
					}

						kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
						kw.setTagName("Exception");
						tempKeyWords.add(kw);
					}
				
				
				}
				
				String description=kwc.getDescription();

				if (description!=null && !description.trim().equals(""))
				{
      				//替换为直接的异常消息文本串
					KeyWord kw2=new KeyWord();
					
					String filteredterms=doTermFilter(description);
					
					kw2.setKeywordName(filteredterms);
					kw2.setWeightOne(7);
					kw2.setWeightTwo(2);
					kw2.setScore(kw2.getWeightOne()*kw2.getWeightTwo());
					kw2.setTagName("Exception");
					tempKeyWords.add(kw2);
				}
			
		}
		
            consoleCandidates.setActionID(-1); //不跟随动作删除
			consoleCandidates.setLevel(Basic.KeyWordsLevel.Level_One);
			consoleCandidates.setOldStep(3);
			consoleCandidates.setKeyWords(tempKeyWords);
			consoleCacheKeyWords.add(consoleCandidates);
			
			
			
		}


	

	
	private String doTermFilter(String description) {
		
		String des;
		
		List<String> mydesList=CommUtil.arrayToList(description.split("[ ]"));
		//查路径 //check file path string  3 "\" or 3 "/"
		for (int i=0; i<mydesList.size();i++) {
			String[] checkslahoneList=mydesList.get(i).split("\\\\");
			if (checkslahoneList.length>2) {
				mydesList.remove(i);
				
			}
		//查包名
			
		}
		//TODO  今后去掉更多的异常格式数据
		
		
		des=CommUtil.ListToString(mydesList);
		
	
		
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

}
