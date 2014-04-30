package cn.edu.fudan.se.helpseeking.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.ActionCache;
import cn.edu.fudan.se.helpseeking.bean.ActionInformation;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;
import cn.edu.fudan.se.helpseeking.bean.DebugCode;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.EditorInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerRelated;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.KeyWordsCandidates;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.QueryList;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView;


public class CacheProcessing extends Thread  {

	static Object obj = new Object();


	List <ActionInformation> actionInformations;	
	//	获得动作窗口内相同动作的频率fi  : 出现次数除以总次数
	//	对资源信息的权重最简单的安排： 就是频率高的动作的资源权重高  

	//获取单例
	Cache currentCache=Cache.getInstance();


	IViewPart part ;

	public CacheProcessing()
	{


	}



	public void constructAndcountActionFrequency() {

		//遍历动作滑动窗口并统计
		for (ActionCache  ac: (currentCache.getActionSlideWindow().getActionList())) 
		{
			boolean flage=false;
			for (ActionInformation af : actionInformations)
			{
				if (af.getActionKind()==ac.getAction().getActionKind()  && (af.getActionName()==ac.getAction().getActionName())) 
				{
					int actionID=ac.getActionID();
					af.setCount(af.getCount()+1);//计数
					af.getActionIDList().add(actionID);
					//TODO:  20140409   策略太简单，先试试；  简单地添加信息到串中
					//					从currentcache中获得actionID相同的资源加入inforString相应的串中
					//				    几类信息的字符串 
					//				    0 对应Debugcode
					//				    1对应Editcode
					//				    2对应ideOutput
					//				    3对应explorerRelated
					//				    4对应当前类模型CruuentClassModel
					String[] inforString=new String[5];

					//TODO 
					//				    inforString[0]=inforString[0]+" "+currentCache.findDebugCodeString(actionID);
					//				    inforString[1]=inforString[1]+" "+currentCache.findEditCodeString(actionID);
					//				    inforString[2]=inforString[2]+" "+currentCache.findIDEOutputString(actionID);
					//				    inforString[3]=inforString[3]+" "+currentCache.findExplorerRelatedString(actionID);
					//				    inforString[4]=inforString[4]+" "+currentCache.findCurrentClassModel();

					af.setInforString(inforString);


					flage=true;

				}								
			}
			//没有找到相同的动作则添加
			if (!flage)   
			{
				ActionInformation tempAF=new ActionInformation();
				tempAF.setActionKind(ac.getAction().getActionKind());
				tempAF.setActionName(ac.getAction().getActionName());
				tempAF.setCount(1);
				tempAF.setFrequency((float)0.0);
				tempAF.getActionIDList().add(ac.getActionID());
				//TODO:  20140409   策略太简单，先试试；  简单地添加信息到串中


				actionInformations.add(tempAF);

			}
		}

		//		计算频率比值
		//		求和
		int sumCount=0;
		for (ActionInformation af : actionInformations) {
			sumCount=sumCount+af.getCount();

		}

		ActionInformation[] farray=new  ActionInformation[actionInformations.size()];

		//		 求频率， 复制
		for (int i=0;i<actionInformations.size();i++) {
			actionInformations.get(i).setFrequency( actionInformations.get(i).getCount()*(float)1.0/sumCount);
			farray[i]=actionInformations.get(i);
		}

		//清理
		actionInformations.clear();
		int bigestIndex=0;
		float bigestvalue=0;
		//排序
		for(int j=0;j<farray.length-1;j++)
		{    bigestIndex=j;
		bigestvalue=farray[j].getFrequency();
		//		       检查是否越界
		for (int i=j+1;i<farray.length;i++) 
		{
			//			System.out.println("j="+j+"\t i="+i+"\n");
			if (farray[i].getFrequency()>bigestvalue) {
				bigestIndex=i;
			}
		}
		//			 交换
		if (bigestIndex!=j) {
			ActionInformation tempA=farray[j];
			farray[j]=farray[bigestIndex];
			farray[bigestIndex]=tempA;
		}
		}
		//		 复原actionFrequencies
		for (int i = 0; i < farray.length; i++) {
			actionInformations.add(farray[i]);
		}

	}




	public void run() {





		synchronized (obj) {


			if (PlatformUI.getWorkbench() == null)
			{   return;}
			if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()== null)
			{ return; }
			if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()== null	) 
			{
				return;		
			}


			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				part=page.showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");


			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// 同步块中！防止 出错！  //调整好keyword生成后，将这部分处理修改为老化策略处理
//			simpleTacticProcessing();
			
			//TODO  新算法： 完成20140430:03:37
			
			 newTacticProcessing();
			
			//放置在searchView
			simpleTacticQuery();




		}
	}


	
	private void newTacticProcessing() {
		// TODO 还需要写
		
		//老化处理： 
		List<KeyWordsCandidates> consoleCacheKeyWords=currentCache.getConsoleCacheKeyWords();
		List<KeyWordsCandidates> problemCacheKeyWords=currentCache.getProblemCacheKeyWords();
		List<KeyWordsCandidates> classmodelKeyWords=currentCache.getClassmodelKeyWords();
//		List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
//		List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
		List<KeyWordsCandidates> codeKeyWords=currentCache.getCodeKeyWords();
		List<KeyWordsCandidates> relatedExplorerKeyWords=currentCache.getRelatedExplorerKeyWords();
		
		doOldStep(consoleCacheKeyWords);
		doOldStep(problemCacheKeyWords);
		doOldStep(classmodelKeyWords);
		doOldStep(codeKeyWords);
		doOldStep(relatedExplorerKeyWords);
		
		
		
		//拼接 候选词
		
		boolean flage=false;
		List<KeyWord> totallKeyWords=new ArrayList<>();

		if (consoleCacheKeyWords!=null) {
			for (int i = 0; i < consoleCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords=consoleCacheKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size()>0) {
					totallKeyWords.addAll(tempKeyWords);
				flage=true;
				}
				
			}
		}
		
		if (problemCacheKeyWords!=null) {
			for (int i = 0; i < problemCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords=problemCacheKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size()>0) {
					totallKeyWords.addAll(tempKeyWords);
				flage=true;
				}
				
			}
		}
		
		if (classmodelKeyWords!=null) {
			for (int i = 0; i < classmodelKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords=classmodelKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size()>0) {
					totallKeyWords.addAll(tempKeyWords);
				flage=true;
				}
				
			}
		}
		
		if (codeKeyWords!=null) {
			for (int i = 0; i < codeKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords=codeKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size()>0) {
					totallKeyWords.addAll(tempKeyWords);
				flage=true;
				}
				
			}
		}
		
		if (relatedExplorerKeyWords!=null) {
			for (int i = 0; i < relatedExplorerKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords=relatedExplorerKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size()>0) {
					totallKeyWords.addAll(tempKeyWords);
				flage=true;
				}
				
			}
		}
		
	
		
//
//		if (problemCacheKeyWords!=null) {
//			totallKeyWords.addAll(problemCacheKeyWords);
//			flage=true;
//		}
//
//		if (consoleViewKeyWords!=null) {
//			totallKeyWords.addAll(consoleViewKeyWords);	  
//			flage=true;
//		}
//		if (problemViewKeyWords!=null) {
//			totallKeyWords.addAll(problemViewKeyWords); 
//			flage=true;
//		}		
//		if (classmodelKeyWords!=null) {
//			totallKeyWords.addAll(classmodelKeyWords);
//			flage=true;
//		}		
//		if (codeKeyWords!=null) {
//			totallKeyWords.addAll(codeKeyWords);
//			flage=true;
//		}		
//		if (relatedExplorerKeyWords!=null) {
//			totallKeyWords.addAll(relatedExplorerKeyWords);
//			flage=true;
//		}	
		List<KeyWord> deDupilcateTotallKeyWords=new ArrayList<>();

		if (flage) {
			//按照keyword排序
			Collections.sort(totallKeyWords,new Comparator<KeyWord>() {

				@Override
				public int compare(KeyWord o1, KeyWord o2) {
					// TODO Auto-generated method stub
					return o2.getKeywordName().compareTo(o1.getKeywordName());
				}
			});

			//去除重复，保留score大的

			for (int i = 0; i < totallKeyWords.size(); i++) {
				boolean flage1=false;
				KeyWord oldWord=totallKeyWords.get(i);

				if (deDupilcateTotallKeyWords.size()==0) {
					deDupilcateTotallKeyWords.add(oldWord);
					continue;
				}

				for (int j = 0; j <deDupilcateTotallKeyWords.size(); j++) {
					KeyWord newWord=deDupilcateTotallKeyWords.get(j);

					if (newWord.getKeywordName().equals(oldWord.getKeywordName())) {
						flage1=true;

						if (newWord.getScore()<oldWord.getScore()) {
							newWord.setScore(oldWord.getScore());
							newWord.setWeightOne(oldWord.getWeightOne());
							newWord.setWeightTwo(oldWord.getWeightTwo());
						}
					}

				}

				if (!flage1) {
					deDupilcateTotallKeyWords.add(oldWord);
				}

			}


			//score降序排序keyword
			Collections.sort(deDupilcateTotallKeyWords, new Comparator<KeyWord>() {
				public int compare(KeyWord arg0, KeyWord arg1)
				{
					if(arg1.getScore()-arg0.getScore()<0)
						return -1;
					else if (arg1.getScore()-arg0.getScore()>0) {
						return 1;
					}
					else  
						return 0;  
				
				}
			});


			//取前k个单词作为查询词
			currentCache.setCurrentKeywordsList(deDupilcateTotallKeyWords);
		}		

		
		
	}






	private void doOldStep(List<KeyWordsCandidates> doKeyWords) {


		

		
		
		for (int j1 = 0; j1 < doKeyWords.size(); j1++) {
			
			int distance=doKeyWords.get(j1).getDistance();
			distance=distance-1;
			doKeyWords.get(j1).setDistance(distance);
			
			int old=doKeyWords.get(j1).getOldStep();
			
			old=old-1;
			doKeyWords.get(j1).setOldStep(old);
			
            //老化
			if (old<=-1) {
				for (int i = 0; i < doKeyWords.get(j1).getKeyWords().size(); i++) {
					
					double score=doKeyWords.get(j1).getKeyWords().get(i).getScore();
					
//					if (KeyWords.get(j1).getLevel().getLevelNumber()<5) {
				//10% 增减	
					if (score>8.0) {
							score=score-score*Basic.OLD_RATIO;
						}
						else {
							score=score+score*Basic.OLD_RATIO;
						}
					//距离变化
					score=score-doKeyWords.get(j1).getDistance()*Basic.DISTANCE_RATIO;
					
					//频率加权
					score=score+doKeyWords.get(j1).getFrequency()*Basic.FREQUENCY_RATIO;
					
						

					doKeyWords.get(j1).getKeyWords().get(i).setScore(score);
					
				}
				
				
			}
			
		}
		
		
		
		
		
		
	}



	public void simpleTacticProcessing()
	{
		
		List<KeyWord> consoleCacheKeyWords=new ArrayList<>();
		List<KeyWord> problemCacheKeyWords=new ArrayList<>();
		List<KeyWord> classmodelKeyWords=new ArrayList<>();
		List<KeyWord> consoleViewKeyWords=new ArrayList<>();
		List<KeyWord> problemViewKeyWords=new ArrayList<>();
		List<KeyWord> codeKeyWords=new ArrayList<>();
		List<KeyWord> relatedExplorerKeyWords=new ArrayList<>();
		

		consoleCacheKeyWords=genSimpleConsoleCacheKeyWords();
		problemCacheKeyWords=genSimpleProblemCacheKeyWords();


		//console消息的exceptional权重最高7
		//problem消息的权重其次，第一个error权重最高7，其次的error和warning是 6
		//		直接在cache中保存的这两个消息的权重分别设置为：


		//	最简单的策略是，检索时用于更关注console中的异常信息，weightOne基本权重5
		//其次是problem中的error信息，weightOne基本权重为4；如果为warning信息基本权重降为2；
		//	再次是调用了哪些API的方法名以及包名，weightOne基本权重6放大API权重
		//代码本身具有的一些词汇权重weightOne为2
		// 再就是用户在一段时间内explorer的信息，权重weightOne为1
		int currentID=currentCache.getCurrentID();

		//生成当前的运行时信息，console的异常消息
		consoleViewKeyWords=genSimpleConsoleViewKeyWords(currentID);

		//生成当前的编译时信息，problem的错误消息
		problemViewKeyWords=genSimpleProblemViewKeyWords(  currentID);     

		//从classmodel中获得调用的API信息
		//	  目前先只关注 当前代码调用的API以及其下一个类
		int mode=1; // 意思是暂时取callee 和 belowclass
		//	  用作可配置项目，  20140410
		classmodelKeyWords=genSimpleClassModelKeyWords(mode);     

		//生成当前的代码(编辑的和调试的)相关的关键词信息
		codeKeyWords=genSimpleCodeKeyWords( currentID);     

		//生成当前编辑和选择的对象或类信息的名字词汇
		relatedExplorerKeyWords=genSimpleRelatedExplorerKeyWords(currentID);    


		boolean flage=false;
		List<KeyWord> totallKeyWords=new ArrayList<>();

		if (consoleCacheKeyWords!=null) {
			totallKeyWords.addAll(consoleCacheKeyWords);
			flage=true;
		}

		if (problemCacheKeyWords!=null) {
			totallKeyWords.addAll(problemCacheKeyWords);
			flage=true;
		}

		if (consoleViewKeyWords!=null) {
			totallKeyWords.addAll(consoleViewKeyWords);	  
			flage=true;
		}
		if (problemViewKeyWords!=null) {
			totallKeyWords.addAll(problemViewKeyWords); 
			flage=true;
		}		
		if (classmodelKeyWords!=null) {
			totallKeyWords.addAll(classmodelKeyWords);
			flage=true;
		}		
		if (codeKeyWords!=null) {
			totallKeyWords.addAll(codeKeyWords);
			flage=true;
		}		
		if (relatedExplorerKeyWords!=null) {
			totallKeyWords.addAll(relatedExplorerKeyWords);
			flage=true;
		}	
		List<KeyWord> deDupilcateTotallKeyWords=new ArrayList<>();

		if (flage) {
			//按照keyword排序
			Collections.sort(totallKeyWords,new Comparator<KeyWord>() {

				@Override
				public int compare(KeyWord o1, KeyWord o2) {
					// TODO Auto-generated method stub
					return o2.getKeywordName().compareTo(o1.getKeywordName());
				}
			});

			//去除重复，保留score大的

			for (int i = 0; i < totallKeyWords.size(); i++) {
				boolean flage1=false;
				KeyWord oldWord=totallKeyWords.get(i);

				if (deDupilcateTotallKeyWords.size()==0) {
					deDupilcateTotallKeyWords.add(oldWord);
					continue;
				}

				for (int j = 0; j <deDupilcateTotallKeyWords.size(); j++) {
					KeyWord newWord=deDupilcateTotallKeyWords.get(j);

					if (newWord.getKeywordName().equals(oldWord.getKeywordName())) {
						flage1=true;

						if (newWord.getScore()<oldWord.getScore()) {
							newWord.setScore(oldWord.getScore());
							newWord.setWeightOne(oldWord.getWeightOne());
							newWord.setWeightTwo(oldWord.getWeightTwo());
						}
					}

				}

				if (!flage1) {
					deDupilcateTotallKeyWords.add(oldWord);
				}

			}


			//score降序排序keyword
			Collections.sort(deDupilcateTotallKeyWords, new Comparator<KeyWord>() {
				public int compare(KeyWord arg0, KeyWord arg1)
				{
					if(arg1.getScore()-arg0.getScore()<0)
						return -1;
					else if (arg1.getScore()-arg0.getScore()>0) {
						return 1;
					}
					else  
						return 0;  
				}
			});


			//取前k个单词作为查询词
			currentCache.setCurrentKeywordsList(deDupilcateTotallKeyWords);
		}		


	}


	

	public List<KeyWord> genSimpleClassModelKeyWords(int mode) {
		//从classmodel中获得调用的API信息
		//	  目前先只关注 当前代码调用的API以及其下一个类
		List<KeyWord> classmodelKeyWords=new ArrayList<KeyWord>();

		if (currentCache.getCurrentClassModel()!=null)
		{
			// 取消息  mode=1; // 意思是暂时取callee 和 belowclass

			String  caller=CommUtil.ListToString(currentCache.getCurrentClassModel().getInternalCaller());
			String  callee=CommUtil.ListToString(currentCache.getCurrentClassModel().getInternalCallee());
			String upclass=CommUtil.ListToString(currentCache.getCurrentClassModel().getUpClass());
			String belowclass=CommUtil.ListToString(currentCache.getCurrentClassModel().getBelowClass());

			if (mode==1) 
			{

				//方法的qualified name 中，intercallee中包含自己的类名信息需要去除
				String finalcalleestr="";

				if (callee!=null) {


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

				if (belowclass!=null) {
					result1=result1+belowclass;
				}



				for (String str : result1.split("[;]")) 
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
					classmodelKeyWords.add(kw);
				}



			}
		}
		else {
			classmodelKeyWords=null;
		}
		return classmodelKeyWords;
	}





	public List<KeyWord>  genSimpleCodeKeyWords(int currentID) {
		//生成当前的代码(编辑的和调试的)相关的关键词信息
		List<KeyWord> codeKeyWords=new ArrayList<KeyWord>();

		EditCode  eCodeInfo=currentCache.findEditCodeWithID(currentID);
		DebugCode dCodeInfo=currentCache.findDebugCodeWithID(currentID);

		if (eCodeInfo==null && dCodeInfo==null) {

			return null;

		}

		if (eCodeInfo!=null)
		{
			// 取代码等信息
			//代码需要过滤停用词
			String codeString=eCodeInfo.getSyntacticBlock().getCode();
			if (codeString!=null) {
				codeString=CommUtil.removeStopWordsAsString(codeString);

				String exceptionalString=eCodeInfo.getSyntacticBlock().getExceptionName();
				String rString=codeString+exceptionalString;

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
					codeKeyWords.add(kw);

				}
			}
		}


		if (dCodeInfo!=null)
		{
			// 取代码等信息
			//代码需要过滤停用词
			String codeString=dCodeInfo.getSyntacticBlock().getCode();
			codeString=CommUtil.removeStopWordsAsString(codeString);

			String exceptionalString=dCodeInfo.getSyntacticBlock().getExceptionName();
			String rString=codeString+exceptionalString;

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
				codeKeyWords.add(kw);

			}
		}

		return codeKeyWords;
	}



	private List<KeyWord> genSimpleConsoleCacheKeyWords() {
		//console消息的exceptional权重最高7 (8)
		List<KeyWord> consoleCacheKeyWords=new ArrayList<>();

		ConsoleInformationList cil=currentCache.getConsoles();
		if (cil==null) {
			return null;
		}
		int lastIndex=cil.getExceptionList().size();
		if (lastIndex<=0) {
			return null;
		}

		int weight1=0;
		int weight2=0;

		if (cil.getExceptionList().size()==currentCache.getConsolesSize()) {
			weight1=2;
			weight2=1;
		}else {
			currentCache.setConsolesSize(cil.getExceptionList().size());
		}
		ConsoleInformation cif=cil.getExceptionList().get(lastIndex-1);

		String exceptionName=cif.getExceptionName();
		if (!exceptionName.trim().equals("")) {
			KeyWord kw=new KeyWord();
			kw.setKeywordName(exceptionName.trim());
			kw.setWeightOne(8-weight1);
			for (String jestr : Basic.javaExceptionalNameList) {
				if (exceptionName.trim().equals(jestr)) {
					kw.setWeightTwo(3-weight1);
					break;
				}

				kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
				kw.setTagName("Exception");
				consoleCacheKeyWords.add(kw);
			}
		}

		String description=cif.getDescription();

		if (description!=null && !description.trim().equals(""))
		{
			//			// 取消息；消息内容不需要去切词处理 直接使用这些消息； 注释掉！！
			//			for (String str : description.split(Basic.SPLIT_STRING)) 
			//			{
			//				if (str.trim().equals("")) {
			//					continue;
			//				}
			//				KeyWord kw=new KeyWord();
			//				kw.setKeywordName(str.trim());
			//				kw.setWeightOne(7-weight2);
			//				kw.setWeightTwo(2-weight2);
			//				kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
			//				consoleCacheKeyWords.add(kw);
			//			}

			//替换为直接的异常消息文本串
			KeyWord kw=new KeyWord();
			kw.setKeywordName(description.trim());
			kw.setWeightOne(7-weight2);
			kw.setWeightTwo(2-weight2);
			kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
			kw.setTagName("Exception");
			consoleCacheKeyWords.add(kw);


		}

		// TODO Auto-generated method stub
		return consoleCacheKeyWords;
	}



	public List<KeyWord>  genSimpleConsoleViewKeyWords(int currentID) {
		List<KeyWord> consoleViewKeyWords=new ArrayList<KeyWord>();

		//生成当前的运行时信息，console的异常消息
		IDEOutput ideoutput=currentCache.findIdeOutputWithID(currentID);

		RuntimeInformation rInfo=null;
		if (ideoutput!=null) {
			rInfo=ideoutput.getRuntimeInformation();
		}


		if (rInfo!=null)
		{
			//  3 exceptional message（*这个信息更重要）
			if (rInfo.getType()==RuntimeInfoType.ExceptionalMessage) {
				String rString=rInfo.getContent();
				//加上已有的exceptional name

				rString=rString+";"+rInfo.getExceptionName();


				for (String str : (rString.split(Basic.SPLIT_STRING))) 
				{
					if (str.trim().length()==0) {
						continue;
					}

					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(5);
					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.equals(jestr)) {
							kw.setWeightTwo(2);
							break;
						}
					}
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Exception");
					consoleViewKeyWords.add(kw);
				}
			}
		}
		else {
			consoleViewKeyWords=null;
		}
		return consoleViewKeyWords;
	}



	private List<KeyWord> genSimpleProblemCacheKeyWords() {
		//problem消息的权重其次，第一个error权重最高7，其次是warning 6
		List<KeyWord> problemCacheKeyWords=new ArrayList<>();
		ArrayList<ProblemInformation> errorList=currentCache.getProblems().getErrorList();
		ArrayList<ProblemInformation> warningList=currentCache.getProblems().getWarningList();
		if (errorList!=null && errorList.size()>0) {
			ProblemInformation pif=errorList.get(0);

			int weight1=0;


			if (errorList.size()==currentCache.getProblemsSize()) {
				weight1=1;

			}else {
				currentCache.setProblemsSize(errorList.size());
			}

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
					kw.setWeightOne(7-weight1);
					kw.setWeightTwo(2-weight1);
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Error");
					problemCacheKeyWords.add(kw);
				}
			}


		}else {
			if (warningList!=null && warningList.size()>0) {
				ProblemInformation pif=warningList.get(0);
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
						problemCacheKeyWords.add(kw);
					}
				}

			}
			else
			{
				return null;
			}
		}


		return problemCacheKeyWords;
	}

	public List<KeyWord> genSimpleProblemViewKeyWords(int currentID) {
		//生成当前的编译时信息，problem的错误消息
		List<KeyWord> problemViewKeyWords=new ArrayList<KeyWord>();

		IDEOutput ideOutput=currentCache.findIdeOutputWithID(currentID);
		CompileInformation pInfo=null;
		if (ideOutput!=null) {
			pInfo=ideOutput.getCompileInformation();
		}

		if (pInfo!=null)
		{
			// 取消息

			String rString=pInfo.getContent();
			for (String str : (rString.split(Basic.SPLIT_STRING))) 
			{
				if (str.trim().length()==0) {
					continue;
				}


				KeyWord kw=new KeyWord();
				kw.setKeywordName(str.trim());
				//1 error
				//2 warning
				if (pInfo.getType()==CompileInfoType.ERROR) {
					kw.setWeightOne(4);
					kw.setTagName("Error");
				}
				if (pInfo.getType()==CompileInfoType.WARNING) {
					kw.setWeightOne(2);
					kw.setTagName("Other");
				}

				for (String jestr : Basic.javaExceptionalNameList) {
					if (str.equals(jestr)) {
						kw.setWeightTwo(2);
						kw.setTagName("Exception");
						break;
					}
				}
				kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
				problemViewKeyWords.add(kw);

			}
		}
		else {
			problemViewKeyWords=null;
		}
		return problemViewKeyWords;
	}

	public  List<KeyWord> genSimpleRelatedExplorerKeyWords(int currentID) {
		//生成当前编辑和选择的对象或类信息的名字词汇
		List<KeyWord> relatedExplorerKeyWords=new ArrayList<KeyWord>();
		ExplorerRelated eRelated=currentCache.findExplorerRelatedWithID(currentID);
		if (eRelated==null) {
			return null;
		}
		EditorInfo  edInfo=eRelated.getEditorInfo();
		ExplorerInfo epInfo=eRelated.getExplorerInfo();

		if (edInfo!=null)
		{
			if (edInfo.getClassQualifiedNameList()!=null) {
				// 取消息
				for (String str : edInfo.getClassQualifiedNameList()) 
				{
					if ((str==null) ||str.equals("")) {
						break;
					}
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Other");
					relatedExplorerKeyWords.add(kw);
				}
			}

		}else {
			relatedExplorerKeyWords=null;
		}   

		if (epInfo!=null)
		{
			if (epInfo.getSelectObjectNameList()!=null) {


				// 取消息
				for (String str : epInfo.getSelectObjectNameList()) 
				{
					if ((str==null) ||str.equals("")) {
						break;
					}
					KeyWord kw=new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
					kw.setTagName("Other");
					relatedExplorerKeyWords.add(kw);
				}
			}
		}
		return relatedExplorerKeyWords;
	}

	private void notifiyQueryList(List<KeyWord> keyWordsforQuery, QueryLevel qLevel,int mode) {

		QueryList qlist=QueryList.getInstance();
		boolean addNewItem=true;

		if (keyWordsforQuery!=null) {

			if (mode==1) {

				int index=qlist.findIndexofModeOne();
				if (index!=-1){
					addNewItem=false;
					Query oldq=qlist.getQuerys().get(index);
					oldq.setQueryKeyWords(keyWordsforQuery);
					oldq.setInforID(currentCache.getCurrentID());

					oldq.makeCandidateKeywords(currentCache.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);

				}


			}

			if (addNewItem) {

				Query myq=new Query();
				List<Query> querys= new ArrayList<Query>();
				myq.setQueryKeyWords(keyWordsforQuery);
				myq.setQueryLevel(qLevel);
				myq.setInforID(currentCache.getCurrentID());
				myq.setMode(mode);
				myq.makeCandidateKeywords(currentCache.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);

				querys.add(myq);
				qlist.setQuerys(querys);

			}

		}
	}


	//	一个动作就有其对应的一组资源； 这些资源可能相同，可能不同；目前系统实现采用一个动作actionid，其相关的资源的ID都相同	
	//  获得一个动作使用的资源数量	


	public void simpleTacticQuery() {




		if(part instanceof HelpSeekingSearchView){
			HelpSeekingSearchView v = (HelpSeekingSearchView)part;
			String searchText="";
			List<KeyWord> keyWordsforQuery = new ArrayList<KeyWord>();
			int candidateKeywordNum=currentCache.getCurrentKeywordsList().size();
			for (int i = 0; i <candidateKeywordNum; i++) {

				if (i==Basic.TEMP_K_KEYWORDS) {
					break;
				}
				if (currentCache.getCurrentKeywordsList().get(i).getKeywordName().trim().equals("")) {
					continue;
				}

				KeyWord kw=currentCache.getCurrentKeywordsList().get(i);
				keyWordsforQuery.add(kw);

				if (i==0) {
					searchText=kw.getKeywordName();
				}
				else
				{
					searchText=searchText+" "+kw.getKeywordName();
				}


			}

			//TODO  为编译无自动提示功能版本而注释掉自动赋值 代码
			v.setCandidateSearchWords(searchText);
			System.out.println(searchText);

			v.setCurrentActionID(currentCache.getCurrentID());

			int mode=1;//1对query改写 表示是动作生成的查询 并不立即查询      2 为新增的查询，准备自动查询，值为2时触发自动查询。     


			notifiyQueryList(keyWordsforQuery,QueryLevel.Middle,mode);


		
			// 在 problem view 更新 时  Attention动作类型  动作名称"Problem View Changed"
			ActionCache ac=currentCache.getActions().getActionCachewithActionID(currentCache.getCurrentID());
			if (ac.getAction().getActionKind()==Kind.ATTENTION 
					&& ac.getAction().getActionName().equals("Problem View Changed")) {
				mode=2;
				notifiyQueryList(keyWordsforQuery,QueryLevel.High,mode);

			} 

			if (ac.getAction().getActionKind()==Kind.ATTENTION 
					&& ac.getAction().getActionName().equals("Console View Changed")) {
				mode=2;
				notifiyQueryList(keyWordsforQuery,QueryLevel.High,mode);

			}	

			System.out.println("say tactic mode = " +mode );
			//TODO  为编译无自动提示功能版本而注释 掉如下代码
			if (mode==2)
				v.searchQueryList();

		}


	}

	public void tacticOneProcessing()
	{
		actionInformations=new ArrayList<ActionInformation>();	
		//  从滑动窗口中获得动作给actionFrequencies赋值  并统计频率;最后按照频率值对actionfrequencies的元素降序排序
		constructAndcountActionFrequency();
		//统计资源			
		//TODO ??统计资源和信息	

		//			信息切词


		//			抽取关键词
	}




}
