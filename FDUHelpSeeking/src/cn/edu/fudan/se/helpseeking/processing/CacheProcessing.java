package cn.edu.fudan.se.helpseeking.processing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;

import org.eclipse.jdt.ui.actions.FindAction;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.ActionCache;
import cn.edu.fudan.se.helpseeking.bean.ActionInformation;
import cn.edu.fudan.se.helpseeking.bean.AutoSearchWordsStruct;
import cn.edu.fudan.se.helpseeking.bean.AutoTimerSearchString;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;
import cn.edu.fudan.se.helpseeking.bean.CountString;
import cn.edu.fudan.se.helpseeking.bean.DebugCode;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.EditorInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerRelated;
import cn.edu.fudan.se.helpseeking.bean.FindTimerAutoSearchText;
import cn.edu.fudan.se.helpseeking.bean.HistoryWordString;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.KeyWordsCandidates;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.QueryList;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;
import cn.edu.fudan.se.helpseeking.bean.WindowTotalKeyWords;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView;
import cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView;
import cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView;

public class CacheProcessing extends Thread {

	static Object obj = new Object();

	List<ActionInformation> actionInformations;
	// 获得动作窗口内相同动作的频率fi : 出现次数除以总次数
	// 对资源信息的权重最简单的安排： 就是频率高的动作的资源权重高

	// 获取单例
	Cache currentCache = Cache.getInstance();

	IViewPart part;
	IViewPart partSolutionView;
	IViewPart partInteractiveView;

	public CacheProcessing() {

	}

	public void run() {

		synchronized (obj) {

			if (PlatformUI.getWorkbench() == null) {
				return;
			}
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
				return;
			}
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage() == null) {
				return;
			}

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				part = page
						.findView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
				partSolutionView = page
						.findView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
				partInteractiveView = page
						.findView("cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// 同步块中！防止 出错！ //调整好keyword生成后，将这部分处理修改为老化策略处理
			// simpleTacticProcessing();

			
			if (Basic.ALGORITHMSELECTION==1) {
				// TODO 新算法： 完成20140430:03:37

				newTacticProcessing();
			}
		
			if (Basic.ALGORITHMSELECTION==2) {
				
				// 20141107新算法：
					tacticNewAlgrithmProcessing();
			}
			


			// 放置在searchView
			// 14.10.25 将上下文 信息 传到 interactive view 的formtree中去

			simpleTacticQuery();


		}
	}

	// 20141107新算法：
	// hongwei add  是否可以理解为对每个词word的分值： 每个动作进来后，
	
	//1、它带来的words按照动作序列号，动作类型（Interest level action），
	//词的来源(Interest level API), 给出 初值； 接着给每个动作进行老化计算一次动作中词的分值；  
	
	//2、将focus窗口中的每个动作中的有与新动作相同的词计算一个权重，例如累加后计算F得到方差，
	// 将该方差作为权重（目前使用这个方式）加到新进入动作中该词的分值中
	//（每个新动作才有的权利，以对应新动作更关注）


	private void tacticNewAlgrithmProcessing() {

		// 老化处理：
		List<KeyWordsCandidates> consoleCacheKeyWords = currentCache
				.getConsoleCacheKeyWords();
		List<KeyWordsCandidates> problemCacheKeyWords = currentCache
				.getProblemCacheKeyWords();
		List<KeyWordsCandidates> classmodelKeyWords = currentCache
				.getClassmodelKeyWords();
//		 List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
//		 List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
		List<KeyWordsCandidates> codeKeyWords = currentCache.getCodeKeyWords();
		List<KeyWordsCandidates> relatedExplorerKeyWords = currentCache
				.getRelatedExplorerKeyWords();

		// System.out.println("console");
		doNewAlgrithmOldStep(consoleCacheKeyWords);
		// System.out.println("problem");
		doNewAlgrithmOldStep(problemCacheKeyWords);
		// System.out.println("classmodel");
		doNewAlgrithmOldStep(classmodelKeyWords);
		doNewAlgrithmOldStep(codeKeyWords);
		doNewAlgrithmOldStep(relatedExplorerKeyWords);

		// 拼接 候选词

		boolean flage = false;
		List<KeyWord> totallKeyWords = new ArrayList<>();

		if (consoleCacheKeyWords != null) {
			for (int i = 0; i < consoleCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = consoleCacheKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (problemCacheKeyWords != null) {
			for (int i = 0; i < problemCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = problemCacheKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (classmodelKeyWords != null) {
			for (int i = 0; i < classmodelKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = classmodelKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (codeKeyWords != null) {
			for (int i = 0; i < codeKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = codeKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (relatedExplorerKeyWords != null) {
			for (int i = 0; i < relatedExplorerKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = relatedExplorerKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		// 对所有词处理

		// 按照keyword排序
		Collections.sort(totallKeyWords, new Comparator<KeyWord>() {

			@Override
			public int compare(KeyWord o1, KeyWord o2) {
				// TODO Auto-generated method stub
				return o2.getKeywordName().compareTo(o1.getKeywordName());
			}
		});

		// 移入历史库中

		WindowTotalKeyWords mytemp = new WindowTotalKeyWords();
		mytemp.setTotallKeyWords(totallKeyWords);
		mytemp.setId(currentCache.getCurrentID());
		currentCache.getHistoryKeyWords().add(mytemp);
		resetHistroyWindow(Basic.SLIDE_WINDOW_SIZE);

		// 计算历史信息，的重合程度，得到频率再次计算各个词的权重。

		totallKeyWords = docontextweightforWords(Basic.CountWindowSize);

		// 重写totallkeywords

		List<KeyWord> deDupilcateTotallKeyWords = new ArrayList<>();

		if (flage) {
			// 按照keyword排序
			Collections.sort(totallKeyWords, new Comparator<KeyWord>() {

				@Override
				public int compare(KeyWord o1, KeyWord o2) {
					// TODO Auto-generated method stub
					return o2.getKeywordName().compareTo(o1.getKeywordName());
				}
			});

			// 去除重复，保留score大的

			for (int i = 0; i < totallKeyWords.size(); i++) {
				boolean flage1 = false;
				KeyWord oldWord = totallKeyWords.get(i);

				if (deDupilcateTotallKeyWords.size() == 0) {
					deDupilcateTotallKeyWords.add(oldWord);
					continue;
				}

				for (int j = 0; j < deDupilcateTotallKeyWords.size(); j++) {
					KeyWord newWord = deDupilcateTotallKeyWords.get(j);

					if (newWord.getKeywordName().equals(
							oldWord.getKeywordName())) {
						flage1 = true;

						if (newWord.getScore() < oldWord.getScore()) {
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

			// score降序排序keyword
			Collections.sort(deDupilcateTotallKeyWords,
					new Comparator<KeyWord>() {
						public int compare(KeyWord arg0, KeyWord arg1) {
							if (arg1.getScore() - arg0.getScore() < 0)
								return -1;
							else if (arg1.getScore() - arg0.getScore() > 0) {
								return 1;
							} else
								return 0;

						}
					});

			// 取前k个单词作为查询词
			currentCache.setCurrentKeywordsList(deDupilcateTotallKeyWords);
			System.out.println("newTacticProcessing function\n deDupilcateTotallKeyWords: "+deDupilcateTotallKeyWords.toString());
		}

	}
	
	
	// TODO 新算法： 完成20140430:03:37


	private void newTacticProcessing() {

		// 老化处理：
		List<KeyWordsCandidates> consoleCacheKeyWords = currentCache
				.getConsoleCacheKeyWords();
		List<KeyWordsCandidates> problemCacheKeyWords = currentCache
				.getProblemCacheKeyWords();
		List<KeyWordsCandidates> classmodelKeyWords = currentCache
				.getClassmodelKeyWords();
		// List<KeyWordsCandidates> consoleViewKeyWords=new ArrayList<>();
		// List<KeyWordsCandidates> problemViewKeyWords=new ArrayList<>();
		List<KeyWordsCandidates> codeKeyWords = currentCache.getCodeKeyWords();
		List<KeyWordsCandidates> relatedExplorerKeyWords = currentCache
				.getRelatedExplorerKeyWords();

		// System.out.println("console");
		doOldStep(consoleCacheKeyWords);
		// System.out.println("problem");
		doOldStep(problemCacheKeyWords);
		// System.out.println("classmodel");
		doOldStep(classmodelKeyWords);
		doOldStep(codeKeyWords);
		doOldStep(relatedExplorerKeyWords);

		// 拼接 候选词

		boolean flage = false;
		List<KeyWord> totallKeyWords = new ArrayList<>();

		if (consoleCacheKeyWords != null) {
			for (int i = 0; i < consoleCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = consoleCacheKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (problemCacheKeyWords != null) {
			for (int i = 0; i < problemCacheKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = problemCacheKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (classmodelKeyWords != null) {
			for (int i = 0; i < classmodelKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = classmodelKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (codeKeyWords != null) {
			for (int i = 0; i < codeKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = codeKeyWords.get(i).getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		if (relatedExplorerKeyWords != null) {
			for (int i = 0; i < relatedExplorerKeyWords.size(); i++) {
				List<KeyWord> tempKeyWords = relatedExplorerKeyWords.get(i)
						.getKeyWords();

				if (tempKeyWords.size() > 0) {
					totallKeyWords.addAll(tempKeyWords);
					flage = true;
				}

			}
		}

		// 对所有词处理

		// 按照keyword排序
		Collections.sort(totallKeyWords, new Comparator<KeyWord>() {

			@Override
			public int compare(KeyWord o1, KeyWord o2) {
				// TODO Auto-generated method stub
				return o2.getKeywordName().compareTo(o1.getKeywordName());
			}
		});

		// 移入历史库中

		WindowTotalKeyWords mytemp = new WindowTotalKeyWords();
		mytemp.setTotallKeyWords(totallKeyWords);
		mytemp.setId(currentCache.getCurrentID());
		currentCache.getHistoryKeyWords().add(mytemp);
		resetHistroyWindow(Basic.SLIDE_WINDOW_SIZE);

		// 计算历史信息，的重合程度，得到频率再次计算各个词的权重。

		totallKeyWords = docontextweightforWords(Basic.CountWindowSize);

		// 重写totallkeywords

		List<KeyWord> deDupilcateTotallKeyWords = new ArrayList<>();

		if (flage) {
			// 按照keyword排序
			Collections.sort(totallKeyWords, new Comparator<KeyWord>() {

				@Override
				public int compare(KeyWord o1, KeyWord o2) {
					// TODO Auto-generated method stub
					return o2.getKeywordName().compareTo(o1.getKeywordName());
				}
			});

			// 去除重复，保留score大的

			for (int i = 0; i < totallKeyWords.size(); i++) {
				boolean flage1 = false;
				KeyWord oldWord = totallKeyWords.get(i);

				if (deDupilcateTotallKeyWords.size() == 0) {
					deDupilcateTotallKeyWords.add(oldWord);
					continue;
				}

				for (int j = 0; j < deDupilcateTotallKeyWords.size(); j++) {
					KeyWord newWord = deDupilcateTotallKeyWords.get(j);

					if (newWord.getKeywordName().equals(
							oldWord.getKeywordName())) {
						flage1 = true;

						if (newWord.getScore() < oldWord.getScore()) {
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

			// score降序排序keyword
			Collections.sort(deDupilcateTotallKeyWords,
					new Comparator<KeyWord>() {
						public int compare(KeyWord arg0, KeyWord arg1) {
							if (arg1.getScore() - arg0.getScore() < 0)
								return -1;
							else if (arg1.getScore() - arg0.getScore() > 0) {
								return 1;
							} else
								return 0;

						}
					});

			// 取前k个单词作为查询词
			currentCache.setCurrentKeywordsList(deDupilcateTotallKeyWords);
			System.out.println("newTacticProcessing function\n deDupilcateTotallKeyWords: "+deDupilcateTotallKeyWords.toString());
		}

	}

	private List<KeyWord> docontextweightforWords(int countwindowsizeLimited) {
		List<KeyWord> result = new ArrayList<KeyWord>();

		// 在最近的窗中出现的次数
		List<CountString> hws = new ArrayList<CountString>();

		// 放到若干个列中
		int size = currentCache.getHistoryKeyWords().size();
		System.out.println("fuction docontextweightforwords:\n current window size: " +countwindowsizeLimited +"\n working window size: "+size);

		int totalwords = 0;

		if (countwindowsizeLimited < size) {

			for (int i = size - 1; i >= size - countwindowsizeLimited; i--) {

				List<KeyWord> tempKeyWords = currentCache.getHistoryKeyWords()
						.get(i).getTotallKeyWords();

				for (int j = 0; j < tempKeyWords.size(); j++) {

					CountString cs = new CountString();
					cs.setCount(1);
					cs.setKewordname(tempKeyWords.get(j).getKeywordName());

					for (int k = 0; k < hws.size(); k++) {

						if (hws.get(k).getKewordname()
								.equals(cs.getKewordname())) {
							hws.get(k).setCount(hws.get(k).getCount() + 1);

						} else {

							hws.add(cs);
						}

						totalwords = totalwords + 1;
					}

				}

				result.addAll(tempKeyWords);
			}

			for (int i = 0; i < hws.size(); i++) {

				for (int j = size - 1; j >= size - countwindowsizeLimited; j--) {

					List<KeyWord> tempKeyWords = currentCache
							.getHistoryKeyWords().get(j).getTotallKeyWords();

					for (int k = 0; k < tempKeyWords.size(); k++) {
						if (hws.get(i).getKewordname()
								.equals(tempKeyWords.get(k).getKeywordName())) {
							int fre = hws.get(i).getCount();
							tempKeyWords.get(k).setFrequency(fre);
							double myscore = tempKeyWords.get(k).getScore();
							tempKeyWords.get(k).setScore(
									myscore * 1.0 * fre / totalwords);
						}

					}

				}

			}

		}

		return result;
	}

	private void resetHistroyWindow(int slideWindowSize) {

		if (currentCache.getHistoryKeyWords().size() > slideWindowSize) {
			currentCache.getHistoryKeyWords().remove(0);
		}

	}

	// 20141107 新算法老化实现
	private void doNewAlgrithmOldStep(List<KeyWordsCandidates> doKeyWords) {

		if (doKeyWords.size() > 0) {

			int indexzerodistance = 0;
			for (int j1 = 0; j1 < doKeyWords.size(); j1++) {

				int distance = currentCache.findyouDistance(doKeyWords.get(j1)
						.getActionID());

				doKeyWords.get(j1).setDistance(distance);

				// 老化
				if (distance > 0) {
					for (int i = 0; i < doKeyWords.get(j1).getKeyWords().size(); i++) {
						double score = doKeyWords.get(j1).getKeyWords().get(i)
								.getScore()
								/ (Math.pow(Basic.gama, distance));
						doKeyWords.get(j1).getKeyWords().get(i).setScore(score);
					}
				}

				// 保留 新入的动作位置，用于定位该动作的词 以用于加权
				if (distance == 0) {
					indexzerodistance = j1;
				}

			}

			// 完成老化后 对新入的词汇处置累加权 这里使用方差X 选用 标准差？
			if (doKeyWords.get(indexzerodistance).getKeyWords().size() > 0) {

				for (int j = 0; j < doKeyWords.get(indexzerodistance)
						.getKeyWords().size(); j++) {
					String newenterword = doKeyWords.get(indexzerodistance)
							.getKeyWords().get(j).getKeywordName()
							.toLowerCase().trim();
					double score = doKeyWords.get(indexzerodistance)
							.getKeyWords().get(j).getScore();
					double standarddeviation = 0.0;

					List<Double> scorelist = new ArrayList<Double>();
					double sumscore = 0.0;

					for (int i = 0; i < doKeyWords.size(); i++) {
						
							for (int k = 0; k < doKeyWords.get(i).getKeyWords()
									.size(); k++) {
								String curword = doKeyWords.get(i)
										.getKeyWords().get(k).getKeywordName();
								if (curword.toLowerCase().trim()
										.equals(newenterword)) {
									double myscore = doKeyWords.get(i)
											.getKeyWords().get(k).getScore();
									scorelist.add(myscore);
									sumscore = sumscore + myscore;

								}
							}
						

					}

					if (scorelist.size() > 0) {

						double avg = sumscore / scorelist.size();
						double sumsqurediff = 0.0;
						for (int i = 0; i < scorelist.size(); i++) {
							double myscore = scorelist.get(i);
							sumsqurediff = sumsqurediff
									+ Math.pow(myscore - avg, 2);
						}
						if (scorelist.size() == 1) {
							standarddeviation = 0.0;
						} else {
							standarddeviation = Math.sqrt(sumsqurediff
									/ (scorelist.size() - 1));
						}

						doKeyWords.get(indexzerodistance).getKeyWords().get(j)
								.setScore(score + standarddeviation);
					}
				}
			}
		}
	}
	
	private void doOldStep(List<KeyWordsCandidates> doKeyWords) {

		for (int j1 = 0; j1 < doKeyWords.size(); j1++) {

			int distance = currentCache.findyouDistance(doKeyWords.get(j1)
					.getActionID());

			doKeyWords.get(j1).setDistance(distance);

			int old = doKeyWords.get(j1).getOldStep();

			old = old - 1;
			doKeyWords.get(j1).setOldStep(old);

			// 老化
			if (old <= -1) {
				int acid = doKeyWords.get(j1).getActionID();
				int freq = currentCache.findyoufrequency(acid);
				int windowsize = currentCache.getActions().getActionList()
						.size();

				for (int i = 0; i < (doKeyWords.size()>doKeyWords.get(j1).getKeyWords().size()?doKeyWords.get(j1).getKeyWords().size():doKeyWords.size()); i++) {
					System.out.println("doOldStep function : j1 is "+j1+"  i is "+i );

					double weight = doKeyWords.get(j1).getKeyWords().get(i)
							.getWeightOne();

					double score = weight * (freq * 1.0 / windowsize)
							* (1 / Math.pow(2.0, (distance - 1)));

					// //距离变化
					// //
					// score=score-doKeyWords.get(j1).getDistance()*Basic.DISTANCE_RATIO;
					//
					// //频率加权
					// double frequency=doKeyWords.get(j1).getFrequency();
					System.out.println("doOldStep fuction \n score: "+score);
					System.out.println("dokeyWords: "+doKeyWords.get(i).getKeyWords().toString()+" mytagname: "+(doKeyWords.get(i).getKeyWords().size()>0?doKeyWords.get(i).getKeyWords().get(0).getTagName():"no words FOR TAGNAME"));

					doKeyWords.get(j1).getKeyWords().get(i).setScore(score);
					doKeyWords.get(j1).getKeyWords().get(i).setFrequency(freq);

				}

				doKeyWords.get(j1).setFrequency(freq);

			}

		}

	}

	public void simpleTacticProcessing() {

		List<KeyWord> consoleCacheKeyWords = new ArrayList<>();
		List<KeyWord> problemCacheKeyWords = new ArrayList<>();
		List<KeyWord> classmodelKeyWords = new ArrayList<>();
		List<KeyWord> consoleViewKeyWords = new ArrayList<>();
		List<KeyWord> problemViewKeyWords = new ArrayList<>();
		List<KeyWord> codeKeyWords = new ArrayList<>();
		List<KeyWord> relatedExplorerKeyWords = new ArrayList<>();

		consoleCacheKeyWords = genSimpleConsoleCacheKeyWords();
		problemCacheKeyWords = genSimpleProblemCacheKeyWords();

		// console消息的exceptional权重最高7
		// problem消息的权重其次，第一个error权重最高7，其次的error和warning是 6
		// 直接在cache中保存的这两个消息的权重分别设置为：

		// 最简单的策略是，检索时用于更关注console中的异常信息，weightOne基本权重5
		// 其次是problem中的error信息，weightOne基本权重为4；如果为warning信息基本权重降为2；
		// 再次是调用了哪些API的方法名以及包名，weightOne基本权重6放大API权重
		// 代码本身具有的一些词汇权重weightOne为2
		// 再就是用户在一段时间内explorer的信息，权重weightOne为1
		int currentID = currentCache.getCurrentID();

		// 生成当前的运行时信息，console的异常消息
		consoleViewKeyWords = genSimpleConsoleViewKeyWords(currentID);

		// 生成当前的编译时信息，problem的错误消息
		problemViewKeyWords = genSimpleProblemViewKeyWords(currentID);

		// 从classmodel中获得调用的API信息
		// 目前先只关注 当前代码调用的API以及其下一个类
		int mode = 1; // 意思是暂时取callee 和 belowclass
		// 用作可配置项目， 20140410
		classmodelKeyWords = genSimpleClassModelKeyWords(mode);

		// 生成当前的代码(编辑的和调试的)相关的关键词信息
		codeKeyWords = genSimpleCodeKeyWords(currentID);

		// 生成当前编辑和选择的对象或类信息的名字词汇
		relatedExplorerKeyWords = genSimpleRelatedExplorerKeyWords(currentID);

		boolean flage = false;
		List<KeyWord> totallKeyWords = new ArrayList<>();

		if (consoleCacheKeyWords != null) {
			totallKeyWords.addAll(consoleCacheKeyWords);
			flage = true;
		}

		if (problemCacheKeyWords != null) {
			totallKeyWords.addAll(problemCacheKeyWords);
			flage = true;
		}

		if (consoleViewKeyWords != null) {
			totallKeyWords.addAll(consoleViewKeyWords);
			flage = true;
		}
		if (problemViewKeyWords != null) {
			totallKeyWords.addAll(problemViewKeyWords);
			flage = true;
		}
		if (classmodelKeyWords != null) {
			totallKeyWords.addAll(classmodelKeyWords);
			flage = true;
		}
		if (codeKeyWords != null) {
			totallKeyWords.addAll(codeKeyWords);
			flage = true;
		}
		if (relatedExplorerKeyWords != null) {
			totallKeyWords.addAll(relatedExplorerKeyWords);
			flage = true;
		}
		List<KeyWord> deDupilcateTotallKeyWords = new ArrayList<>();

		if (flage) {
			// 按照keyword排序
			Collections.sort(totallKeyWords, new Comparator<KeyWord>() {

				@Override
				public int compare(KeyWord o1, KeyWord o2) {
					// TODO Auto-generated method stub
					return o2.getKeywordName().compareTo(o1.getKeywordName());
				}
			});

			// 去除重复，保留score大的

			for (int i = 0; i < totallKeyWords.size(); i++) {
				boolean flage1 = false;
				KeyWord oldWord = totallKeyWords.get(i);

				if (deDupilcateTotallKeyWords.size() == 0) {
					deDupilcateTotallKeyWords.add(oldWord);
					continue;
				}

				for (int j = 0; j < deDupilcateTotallKeyWords.size(); j++) {
					KeyWord newWord = deDupilcateTotallKeyWords.get(j);

					if (newWord.getKeywordName().toLowerCase()
							.equals(oldWord.getKeywordName().toLowerCase())) {
						flage1 = true;

						if (newWord.getScore() < oldWord.getScore()) {
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

			// score降序排序keyword
			Collections.sort(deDupilcateTotallKeyWords,
					new Comparator<KeyWord>() {
						public int compare(KeyWord arg0, KeyWord arg1) {
							if (arg1.getScore() - arg0.getScore() < 0)
								return -1;
							else if (arg1.getScore() - arg0.getScore() > 0) {
								return 1;
							} else
								return 0;
						}
					});

			// 取前k个单词作为查询词
			currentCache.setCurrentKeywordsList(deDupilcateTotallKeyWords);
		}

	}

	public List<KeyWord> genSimpleClassModelKeyWords(int mode) {
		// 从classmodel中获得调用的API信息
		// 目前先只关注 当前代码调用的API以及其下一个类
		List<KeyWord> classmodelKeyWords = new ArrayList<KeyWord>();

		if (currentCache.getCurrentClassModel() != null) {
			// 取消息 mode=1; // 意思是暂时取callee 和 belowclass

			String caller = CommUtil.ListToString(currentCache
					.getCurrentClassModel().getInternalCaller());
			String callee = CommUtil.ListToString(currentCache
					.getCurrentClassModel().getInternalCallee());
			String upclass = CommUtil.ListToString(currentCache
					.getCurrentClassModel().getUpClass());
			String belowclass = CommUtil.ListToString(currentCache
					.getCurrentClassModel().getBelowClass());

			if (mode == 1) {

				// 方法的qualified name 中，intercallee中包含自己的类名信息需要去除
				String finalcalleestr = "";

				if (callee != null) {

					String[] calleearray = callee.split("[;]");
					if (calleearray.length > 0) {
						for (String calleestr : calleearray) {
							String[] methodnamestr = calleestr
									.split(Basic.SPLIT_STRING);
							int lastindex = methodnamestr.length;
							for (int i = lastindex - 1; i > 0; i--) {
								String laststr = methodnamestr[i];
								if (!laststr.trim().equals("")) {
									finalcalleestr = finalcalleestr.trim()
											+ laststr + ";";
									break;
								}
							}

						}
					}
				}

				String result1 = finalcalleestr;

				if (belowclass != null) {
					result1 = result1 + belowclass;
				}

				for (String str : result1.split("[;]")) {
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(6);
					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.equals(jestr)) {
							kw.setWeightTwo(2);
							break;
						}
					}
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("API");
					classmodelKeyWords.add(kw);
				}

			}
		} else {
			classmodelKeyWords = null;
		}
		return classmodelKeyWords;
	}

	public List<KeyWord> genSimpleCodeKeyWords(int currentID) {
		// 生成当前的代码(编辑的和调试的)相关的关键词信息
		List<KeyWord> codeKeyWords = new ArrayList<KeyWord>();

		EditCode eCodeInfo = currentCache.findEditCodeWithID(currentID);
		DebugCode dCodeInfo = currentCache.findDebugCodeWithID(currentID);

		if (eCodeInfo == null && dCodeInfo == null) {

			return null;

		}

		if (eCodeInfo != null) {
			// 取代码等信息
			// 代码需要过滤停用词
			String codeString = eCodeInfo.getSyntacticBlock().getCode();
			if (codeString != null) {
				codeString = CommUtil.removeStopWordsAsString(codeString);

				String exceptionalString = eCodeInfo.getSyntacticBlock()
						.getExceptionName();
				String rString = codeString +" "+ exceptionalString;

				for (String str : (rString.split(Basic.SPLIT_STRING))) {
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(2);
					kw.setTagName("Other");

					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.trim().equals(jestr)) {
							kw.setWeightTwo(2);
							kw.setTagName("Exception");
							break;
						}
					}
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					codeKeyWords.add(kw);

				}
			}
		}

		if (dCodeInfo != null) {
			// 取代码等信息
			// 代码需要过滤停用词
			String codeString = dCodeInfo.getSyntacticBlock().getCode();
			codeString = CommUtil.removeStopWordsAsString(codeString);

			String exceptionalString = dCodeInfo.getSyntacticBlock()
					.getExceptionName();
			String rString = codeString +" "+ exceptionalString;

			for (String str : (rString.split(Basic.SPLIT_STRING))) {
				KeyWord kw = new KeyWord();
				kw.setKeywordName(str.trim());
				kw.setWeightOne(2);
				kw.setTagName("Other");

				for (String jestr : Basic.javaExceptionalNameList) {
					if (str.trim().equals(jestr)) {
						kw.setWeightTwo(2);
						kw.setTagName("Exception");
						break;
					}
				}
				kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
				codeKeyWords.add(kw);

			}
		}

		return codeKeyWords;
	}

	private List<KeyWord> genSimpleConsoleCacheKeyWords() {
		// console消息的exceptional权重最高7 (8)
		List<KeyWord> consoleCacheKeyWords = new ArrayList<>();

		ConsoleInformationList cil = currentCache.getConsoles();
		if (cil == null) {
			return null;
		}
		int lastIndex = cil.getExceptionList().size();
		if (lastIndex <= 0) {
			return null;
		}

		int weight1 = 0;
		int weight2 = 0;

		if (cil.getExceptionList().size() == currentCache.getConsolesSize()) {
			weight1 = 2;
			weight2 = 1;
		} else {
			currentCache.setConsolesSize(cil.getExceptionList().size());
		}
		ConsoleInformation cif = cil.getExceptionList().get(lastIndex - 1);

		String exceptionName = cif.getExceptionName();
		if (!exceptionName.trim().equals("")) {
			KeyWord kw = new KeyWord();
			kw.setKeywordName(exceptionName.trim());
			kw.setWeightOne(8 - weight1);
			for (String jestr : Basic.javaExceptionalNameList) {
				if (exceptionName.trim().equals(jestr)) {
					kw.setWeightTwo(3 - weight1);
					break;
				}

				kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
				kw.setTagName("Exception");
				consoleCacheKeyWords.add(kw);
			}
		}

		String description = cif.getDescription();

		if (description != null && !description.trim().equals("")) {
			// // 取消息；消息内容不需要去切词处理 直接使用这些消息； 注释掉！！
			// for (String str : description.split(Basic.SPLIT_STRING))
			// {
			// if (str.trim().equals("")) {
			// continue;
			// }
			// KeyWord kw=new KeyWord();
			// kw.setKeywordName(str.trim());
			// kw.setWeightOne(7-weight2);
			// kw.setWeightTwo(2-weight2);
			// kw.setScore(kw.getWeightOne()*kw.getWeightTwo());
			// consoleCacheKeyWords.add(kw);
			// }

			// 替换为直接的异常消息文本串
			KeyWord kw = new KeyWord();
			kw.setKeywordName(description.trim());
			kw.setWeightOne(7 - weight2);
			kw.setWeightTwo(2 - weight2);
			kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
			kw.setTagName("Exception");
			consoleCacheKeyWords.add(kw);

		}

		// TODO Auto-generated method stub
		return consoleCacheKeyWords;
	}

	public List<KeyWord> genSimpleConsoleViewKeyWords(int currentID) {
		List<KeyWord> consoleViewKeyWords = new ArrayList<KeyWord>();

		// 生成当前的运行时信息，console的异常消息
		IDEOutput ideoutput = currentCache.findIdeOutputWithID(currentID);

		RuntimeInformation rInfo = null;
		if (ideoutput != null) {
			rInfo = ideoutput.getRuntimeInformation();
		}

		if (rInfo != null) {
			// 3 exceptional message（*这个信息更重要）
			if (rInfo.getType() == RuntimeInfoType.ExceptionalMessage) {
				String rString = rInfo.getContent();
				// 加上已有的exceptional name

				rString = rString + ";" + rInfo.getExceptionName();

				for (String str : (rString.split(Basic.SPLIT_STRING))) {
					if (str.trim().length() == 0) {
						continue;
					}

					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(5);
					for (String jestr : Basic.javaExceptionalNameList) {
						if (str.equals(jestr)) {
							kw.setWeightTwo(2);
							break;
						}
					}
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Exception");
					consoleViewKeyWords.add(kw);
				}
			}
		} else {
			consoleViewKeyWords = null;
		}
		return consoleViewKeyWords;
	}

	private List<KeyWord> genSimpleProblemCacheKeyWords() {
		// problem消息的权重其次，第一个error权重最高7，其次是warning 6
		List<KeyWord> problemCacheKeyWords = new ArrayList<>();
		ArrayList<ProblemInformation> errorList = currentCache.getProblems()
				.getErrorList();
		ArrayList<ProblemInformation> warningList = currentCache.getProblems()
				.getWarningList();
		if (errorList != null && errorList.size() > 0) {
			ProblemInformation pif = errorList.get(0);

			int weight1 = 0;

			if (errorList.size() == currentCache.getProblemsSize()) {
				weight1 = 1;

			} else {
				currentCache.setProblemsSize(errorList.size());
			}

			String des = pif.getDescription();
			if (des != null && !des.trim().equals("")) {
				// 取消息
				for (String str : des.split(Basic.SPLIT_STRING)) {
					if (str.trim().equals("")) {
						continue;
					}
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(7 - weight1);
					kw.setWeightTwo(2 - weight1);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Error");
					problemCacheKeyWords.add(kw);
				}
			}

		} else {
			if (warningList != null && warningList.size() > 0) {
				ProblemInformation pif = warningList.get(0);
				String des = pif.getDescription();
				if (des != null && !des.trim().equals("")) {
					// 取消息
					for (String str : des.split(Basic.SPLIT_STRING)) {
						if (str.trim().equals("")) {
							continue;
						}
						KeyWord kw = new KeyWord();
						kw.setKeywordName(str.trim());
						kw.setWeightOne(6);
						kw.setWeightTwo(1);
						kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
						kw.setTagName("Other");
						problemCacheKeyWords.add(kw);
					}
				}

			} else {
				return null;
			}
		}

		return problemCacheKeyWords;
	}

	public List<KeyWord> genSimpleProblemViewKeyWords(int currentID) {
		// 生成当前的编译时信息，problem的错误消息
		List<KeyWord> problemViewKeyWords = new ArrayList<KeyWord>();

		IDEOutput ideOutput = currentCache.findIdeOutputWithID(currentID);
		CompileInformation pInfo = null;
		if (ideOutput != null) {
			pInfo = ideOutput.getCompileInformation();
		}

		if (pInfo != null) {
			// 取消息

			String rString = pInfo.getContent();
			for (String str : (rString.split(Basic.SPLIT_STRING))) {
				if (str.trim().length() == 0) {
					continue;
				}

				KeyWord kw = new KeyWord();
				kw.setKeywordName(str.trim());
				// 1 error
				// 2 warning
				if (pInfo.getType() == CompileInfoType.ERROR) {
					kw.setWeightOne(4);
					kw.setTagName("Error");
				}
				if (pInfo.getType() == CompileInfoType.WARNING) {
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
				kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
				problemViewKeyWords.add(kw);

			}
		} else {
			problemViewKeyWords = null;
		}
		return problemViewKeyWords;
	}

	public List<KeyWord> genSimpleRelatedExplorerKeyWords(int currentID) {
		// 生成当前编辑和选择的对象或类信息的名字词汇
		List<KeyWord> relatedExplorerKeyWords = new ArrayList<KeyWord>();
		ExplorerRelated eRelated = currentCache
				.findExplorerRelatedWithID(currentID);
		if (eRelated == null) {
			return null;
		}
		EditorInfo edInfo = eRelated.getEditorInfo();
		ExplorerInfo epInfo = eRelated.getExplorerInfo();

		if (edInfo != null) {
			if (edInfo.getClassQualifiedNameList() != null) {
				// 取消息
				for (String str : edInfo.getClassQualifiedNameList()) {
					if ((str == null) || str.equals("")) {
						break;
					}
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Other");
					relatedExplorerKeyWords.add(kw);
				}
			}

		} else {
			relatedExplorerKeyWords = null;
		}

		if (epInfo != null) {
			if (epInfo.getSelectObjectNameList() != null) {

				// 取消息
				for (String str : epInfo.getSelectObjectNameList()) {
					if ((str == null) || str.equals("")) {
						break;
					}
					KeyWord kw = new KeyWord();
					kw.setKeywordName(str.trim());
					kw.setWeightOne(1);
					kw.setScore(kw.getWeightOne() * kw.getWeightTwo());
					kw.setTagName("Other");
					relatedExplorerKeyWords.add(kw);
				}
			}
		}
		return relatedExplorerKeyWords;
	}

	private void notifiyQueryList(List<KeyWord> keyWordsforQuery,
			QueryLevel qLevel, int mode) {

		QueryList qlist = QueryList.getInstance();

		boolean addNewItem = true;

		if (keyWordsforQuery != null) {

			if (mode == 1) {

				int index = qlist.findIndexofModeOne();
				if (index != -1) {
					addNewItem = false;
					Query oldq = qlist.getQuerys().get(index);
					oldq.setQueryKeyWords(keyWordsforQuery);
					oldq.setInforID(currentCache.getCurrentID());

					oldq.makeCandidateKeywords(
							currentCache.getCurrentKeywordsList(),
							Basic.MAX_CANDIDATE_KEYWORDS);

				}

			}

			if (addNewItem) {

				Query myq = new Query();
				List<Query> querys = new ArrayList<Query>();
				myq.setQueryKeyWords(keyWordsforQuery);
				myq.setQueryLevel(qLevel);
				myq.setInforID(currentCache.getCurrentID());
				myq.setMode(mode);
				myq.makeCandidateKeywords(
						currentCache.getCurrentKeywordsList(),
						Basic.MAX_CANDIDATE_KEYWORDS);

				querys.add(myq);
				qlist.setQuerys(querys);

			}

		}
	}

	// 一个动作就有其对应的一组资源； 这些资源可能相同，可能不同；目前系统实现采用一个动作actionid，其相关的资源的ID都相同
	// 获得一个动作使用的资源数量

	@SuppressWarnings("static-access")
	public void simpleTacticQuery() {

		if (partSolutionView instanceof HelpSeekingSolutionView) {
			HelpSeekingSolutionView v2 = (HelpSeekingSolutionView) partSolutionView;
			v2.setCurrentActionID(currentCache.getCurrentID());
		}

		//
		
		
			HelpSeekingSearchView v ;
			HelpSeekingInteractiveView viteractive;
		

		if (partInteractiveView instanceof HelpSeekingInteractiveView) 
		{
			if (part instanceof HelpSeekingSearchView)  
			v = (HelpSeekingSearchView) part;
			else 
			v=null;
			
			viteractive=(HelpSeekingInteractiveView) partInteractiveView;
			
			 String searchText = "";
			List<KeyWord> keyWordsforQuery = new ArrayList<KeyWord>();

			int candidateKeywordNum = currentCache.getCurrentKeywordsList()
					.size();
			int countj = 0;
			
			//取候选词
			
			for (int i = 0; i < candidateKeywordNum; i++)
			{

				if (currentCache.getCurrentKeywordsList().get(i)
						.getKeywordName().trim().equals("")) {
					continue;
				}

				KeyWord kw = currentCache.getCurrentKeywordsList().get(i);
				
				//去除停用词 减少无需要得词汇
				
				//处理异常字符，得到截短符号，切词
				
				String keywordsvalidate=(CommUtil.getSimpleWords(kw.getKeywordName())).trim();
			
				String splitchar="[&#$_.@|{}!*%+-=\\:;,?/\"\'\t\b\r\n\0 ]";
				if (!keywordsvalidate.trim().equals(""))
				 keywordsvalidate=CommUtil.removeStopWordsAsStringwithSplitBlank(keywordsvalidate,splitchar);
				
			
				//去除停用词后， 包名 和 方法签名中的异常信息 重复等情况
				 if (!keywordsvalidate.trim().equals(""))
				keywordsvalidate=CommUtil.removeDuplicateWordsWithBlankSplit(CommUtil.stringToList(keywordsvalidate,splitchar));
			
				if (!keywordsvalidate.trim().equals("")) {
					
					kw.setKeywordName(keywordsvalidate.trim());
				
				keyWordsforQuery.add(kw);

				if (i == 0) {
					searchText = kw.getKeywordName();
				} else {
					searchText = searchText + " " + kw.getKeywordName();
				}

				countj = countj + 1;

				if (countj == Basic.TEMP_K_KEYWORDS) {
					break;
				}
				
				}
			}

			//
			// 定时器开启检索

			KeyWordsCandidates kCandidates = new KeyWordsCandidates();
			kCandidates.setKeyWords(keyWordsforQuery);
			kCandidates.setActionID(currentCache.getCurrentID());
			kCandidates.setDistance(1);
			kCandidates.setFrequency(1);

			currentCache.getHistorysearchlist().add(kCandidates);
			resethistorysearchlist(Basic.History_SearchList_Size);

			// AutoSearchWordsStruct asws=new AutoSearchWordsStruct();
			String searchText2 = searchText;
			int mode = 1;// 1对query改写 表示是动作生成的查询 并不立即查询 2
							// 为新增的查询，准备自动查询，值为2时触发自动查询。 3新增定时检索

			if (currentCache.getTimerAutoSearchmode() == 1) {
				if (currentCache.getCountAutoTry() < Basic.Auto_Search_Try) {
					// Find new search key word
					int count = currentCache.getCountAutoTry();
					count = count + 1;
					currentCache.setCountAutoTry(count);

					// 5-12去除产生畸形词汇的地方
					// asws=getTimerAutoSearchText();
					// searchText=asws.getOriginWords();
					// 这里简要地先处理为 含exception的 和 普通的 API
					// 分别取权重最高的分析
					searchText2 = searchText2.trim();
					String[] tempText = searchText2.split("[ ]");

					boolean flag = true;
					for (int j = 0; j < tempText.length; j++) {
						searchText2 = tempText[j];

						if (!tempText[j].equals("")) {

							if (tempText[j].toLowerCase().contains("exception")
									&& tempText[j].indexOf('.') != -1) {
								searchText2 = tempText[j];
								flag = false;
								break;
							}

							if (tempText[j].trim().toLowerCase()
									.equals("exception"))
								continue;

							if (tempText[j].indexOf('.') != -1
									|| tempText[j].indexOf('(') != -1) {
								searchText2 = tempText[j] + " api example";
								flag = false;
								break;
							}
						}

					}

					if (flag) {
						if (tempText.length > 1) {
							searchText2 = (tempText[0].trim() + " " + tempText[1]
									.trim()).trim();
						} else {
							searchText2 = tempText[0].trim();
						}
					}

					mode = 3;
					// notifiyQueryList(keyWordsforQuery,QueryLevel.High,mode);

				} else {
					currentCache.setCountAutoTry(0);
				}
			}

			if (!searchText.equals("")) {

				String tpsea = "";
				String[] tempString = searchText.split("[ ]");

				if (tempString.length > 0) {

					for (int kj = 0; kj < tempString.length; kj++) {

						if (tempString[kj].length() > 3) {

							if (tempString[kj].subSequence(0, 1).equals("(")) {

								tempString[kj] = tempString[kj].substring(1);
							}
							if (tempString[kj].subSequence(0, 1).equals(".")) {

								tempString[kj] = tempString[kj].substring(1);
							}

							tpsea = tpsea + " " + tempString[kj];
						}

					}

					searchText = tpsea.trim();
				}
			}

			
			//比较如果新词汇是变化了60%以上的词汇时，则推荐新关键词 
			
			if (CommUtil.compareStringwitRatio(searchText, Cache.getLastsearchwords(),Basic.RATIOOFNEWSEARCHSTRING)) 
			{
				if (v != null)
				v.setCurrentActionID(currentCache.getCurrentID());
				
				viteractive.setCurrentActionID(currentCache.getCurrentID());
				
				// 在 problem view 更新 时 Attention动作类型 动作名称"Problem View Changed"
				ActionCache ac = currentCache
						.getActions()
						.getActionCachewithActionID(currentCache.getCurrentID());
				if (ac.getAction().getActionKind() == Kind.ATTENTION
						&& ac.getAction().getActionName()
								.equals("Problem View Changed")) {
					mode = 2;
					notifiyQueryList(keyWordsforQuery, QueryLevel.High, mode);
				} else if (ac.getAction().getActionKind() == Kind.ATTENTION
						&& ac.getAction().getActionName()
								.equals("Console View Changed")) {
					mode = 2;
					notifiyQueryList(keyWordsforQuery, QueryLevel.High, mode);
				} else {
					if (mode == 1) {

						notifiyQueryList(keyWordsforQuery, QueryLevel.Middle,
								mode);

					}

				}

				// TODO 受控实验 开快关 为编译无自动提示功能版本而注释掉自动赋值 代码
				if(v!=null)
				v.setCandidateSearchWords(searchText, keyWordsforQuery);

				System.out.println("say tactic mode = " + mode);

				if (mode == 2) {
					// TODO 受控实验 开快关 为编译无自动提示功能版本而注释掉自动赋值 代码
					if(v!=null)
					v.searchQueryList();

				}

				if (currentCache.getTimerAutoSearchmode() == 1) {
					System.out
							.println("say tatic mode :  timer auto \n use keywords : "
									+ searchText2);

					// String searchtext=getTimerAutoSearchText();
					if (searchText2 != null && !searchText2.equals("")) {
						// TODO 受控实验开关 为编译无自动提示功能版本而注释 掉如下代码
						if (v!=null) 
							v.timerAutoSearch(keyWordsforQuery, searchText2);
					}

				}
				
				
				//2014.10 新版 数据传输处
				
				if (!keyWordsforQuery.isEmpty()) {
					
	
					viteractive.setNewWordsAndMode(keyWordsforQuery, mode);
					viteractive.setCurrentActionID(currentCache.getCurrentID());
				}

				if (!searchText.equals("")) {
					Cache.setLastsearchwords(searchText);
				}

			} else {
				System.out.println("the same searchwords!");
			}

		}

	}

	private void resethistorysearchlist(int historySearchlistSize) {
		// adjust history search words list content with size
		if (currentCache.getHistorysearchlist().size() > historySearchlistSize) {
			currentCache.getHistorysearchlist().remove(0);
		}

	}

	private AutoSearchWordsStruct getTimerAutoSearchText() {
		String result = "";
		List<KeyWordsCandidates> tempLasstsearchList = currentCache
				.getHistorysearchlist();
		List<FindTimerAutoSearchText> ftastAll = new ArrayList<FindTimerAutoSearchText>();

		String originWords = "";
		String firstPart = "";
		String secondPart = "";
		String addinPart = "";

		if (tempLasstsearchList.size() >= Basic.History_SearchList_Size) {

			for (int i = tempLasstsearchList.size() - 1; i > tempLasstsearchList
					.size() - Basic.History_SearchList_Size; i--) {
				int j = i - 1;
				List<FindTimerAutoSearchText> ftast = new ArrayList<FindTimerAutoSearchText>();
				List<KeyWord> upKeyWords = tempLasstsearchList.get(i)
						.getKeyWords();
				List<KeyWord> downKeyWords = tempLasstsearchList.get(j)
						.getKeyWords();

				for (KeyWord mykw : upKeyWords) {

					double differ = 0.0;
					for (int k = 0; k < downKeyWords.size(); k++) {
						if (mykw.getKeywordName()
								.trim()
								.equals(downKeyWords.get(k).getKeywordName()
										.trim())) {

							differ = mykw.getScore()
									- downKeyWords.get(k).getScore();
							break;
						}

					}
					FindTimerAutoSearchText ft = new FindTimerAutoSearchText();
					ft.setChangevalue(differ);
					ft.setName(mykw.getKeywordName());
					ftast.add(ft);

				}

				ftastAll.addAll(ftast);

			}

			// 排序找增长最多的 前5个， 检查它们是否为相同的包，如果不是则是这5个词前2个，如果是则是解出包名 加上 API example
			// 检索

			// score降序排序keyword
			Collections.sort(ftastAll,
					new Comparator<FindTimerAutoSearchText>() {
						public int compare(FindTimerAutoSearchText arg0,
								FindTimerAutoSearchText arg1) {
							if (arg1.getChangevalue() - arg0.getChangevalue() < 0)
								return -1;
							else if (arg1.getChangevalue()
									- arg0.getChangevalue() > 0) {
								return 1;
							} else
								return 0;
						}
					});

			List<String> compareList = new ArrayList<>();
			int m = 0;
			int maxlenindex = 0;
			int index = 0;
			for (FindTimerAutoSearchText fta : ftastAll) {
				if (m == Basic.TEMP_K_KEYWORDS) {
					break;
				}

				compareList.add(fta.getName());

				if (compareList.get(m).length() > maxlenindex) {
					maxlenindex = compareList.get(m).length();
					index = m;
				}

				m = m + 1;
			}

			String[] cans;
			boolean flag = false;

			if (compareList.size() > 0) {

				for (int i = 0; i < compareList.size(); i++) {

					originWords = compareList.get(i);

					int indexleft = compareList.get(i).indexOf('(');
					int indexright = compareList.get(i).lastIndexOf(')');

					if (indexleft != -1 && indexright != -1) {
						result = compareList.get(i).substring(indexleft);
						if (result.lastIndexOf('.') > 0) {
							firstPart = result.substring(result
									.lastIndexOf('.'));
							secondPart = result.substring(
									result.lastIndexOf('.'),
									result.length() - 1);
							result = result.substring(result.lastIndexOf('.'));

						} else {

							firstPart = result;
							secondPart = "";
						}
						flag = true;
						break;
					} else

					{
						cans = compareList.get(i).split("[.]");
						int indexs = compareList.get(i).lastIndexOf('.');
						firstPart = compareList.get(i);
						secondPart = "";

						if (cans.length > 1) {

							result = compareList.get(i).substring(0, indexs);
							firstPart = result;

							result = result + " " + cans[cans.length - 1];
							secondPart = cans[cans.length - 1];

							flag = true;
							break;

						}
					}
				}

				result = result + " api example";
				addinPart = "api example";

				if (!flag) {

					if (compareList.get(0).toLowerCase().contains("exception")) {
						result = compareList.get(0) + " exception";
						firstPart = result;
						addinPart = " exception";
					} else {
						result = compareList.get(0) + " api example";
						firstPart = result;
						addinPart = "api example";

					}

				}

			} else {
				if (ftastAll.size() > 0) {

					String tempString = ftastAll.get(0).getName();
					TokenExtractor tek = new TokenExtractor();
					originWords = tempString;
					firstPart = tempString;
					secondPart = "";
					tempString = CommUtil.tokenListToString(tek
							.getIdentifierOccurenceOfString(tempString));
					if (tempString.toLowerCase().contains("exception")) {
						result = tempString + " exception";
						addinPart = "exception";
					} else {

						addinPart = "api example";
						result = tempString + " api example";
					}

				}

			}

		}

		AutoSearchWordsStruct asws = new AutoSearchWordsStruct();
		asws.setOriginWords(originWords);
		asws.setFirstPart(firstPart);
		asws.setSecondPart(secondPart);
		asws.setAddinPart(addinPart);

		return asws;
	}

	private String wronggetTimerAutoSearchText() {

		String result = "";
		List<String> historylist = null; // =currentCache.getLastsearchlist();
		List<AutoTimerSearchString> atss = new ArrayList<AutoTimerSearchString>();
		List<String> historysearchlist = new ArrayList<String>();

		for (int i = 0; i < historylist.size(); i++) {
			List<String> hsl = CommUtil.stringToList(historylist.get(i), "[;]");
			historysearchlist.addAll(hsl);
		}

		for (String str : historysearchlist) {
			String[] tempstrArr = str.trim().split("[.]");
			String packageClassName = "";
			String methodName = tempstrArr[tempstrArr.length - 1];
			for (int i = 0; i < tempstrArr.length - 1; i++) {
				if (packageClassName.equals("")) {
					packageClassName = tempstrArr[i];
				} else {
					packageClassName = packageClassName + "." + tempstrArr[i];
				}

				if (atss.size() > 0) {
					boolean flagw = false;
					for (int j = 0; j < atss.size(); j++) {
						List<String> tempmethodName = atss.get(j)
								.getMethodName();
						if (packageClassName.equals(atss.get(j)
								.getPackageClassName())) {
							int count = atss.get(j).getCount();
							atss.get(j).setCount(count + 1);
							flagw = true;
							boolean flag = false;
							for (int k = 0; k < tempmethodName.size(); k++) {
								if (methodName.equals(tempmethodName.get(k)
										.trim())) {
									flag = true;
								}

							}
							if (flag) {
								atss.get(j).getMethodName().add(methodName);

							}

						}

					}

					if (flagw) {
						AutoTimerSearchString ats = new AutoTimerSearchString();
						ats.setCount(1);
						if (packageClassName.equals("")) {
							ats.setPackageClassName(methodName);
						} else {
							ats.setPackageClassName(packageClassName);
							ats.getMethodName().add(methodName);
						}
						atss.add(ats);

					}

				} else {
					AutoTimerSearchString ats = new AutoTimerSearchString();
					ats.setCount(1);
					if (packageClassName.equals("")) {
						ats.setPackageClassName(methodName);
					} else {
						ats.setPackageClassName(packageClassName);
						ats.getMethodName().add(methodName);
					}
					atss.add(ats);

				}

			}

		}

		// 完成了收集和统计
		// 下面更具统计结果挑选词汇

		return result;
	}

}
