package cn.edu.fudan.se.helpseeking.views;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import liuyang.nlp.lda.main.LdaGibbsSampling;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.handlers.WizardHandler.New;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.htmlparser.util.ParserException;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWithWordsListBean;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWordsBean;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
import cn.edu.fudan.se.helpseeking.bean.TopicWEBPagesBean;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.views.Images;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.test.SampleWebResults;
import cn.edu.fudan.se.helpseeking.util.CarrotTopic;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

public class HelpSeekingInteractiveView extends ViewPart {

	public HelpSeekingInteractiveView() {
	}

	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView"; //$NON-NLS-1$

	private static String javaExceptionalFileName = "/StopResource/javaExceptionalName.txt";
	private static Resource myResource = new Resource();
	private static String javaExceptionalName = myResource
			.getResource(javaExceptionalFileName);
	private static List<String> javaExceptionalNameList = CommUtil
			.arrayToList((javaExceptionalName).split(Basic.SPLIT_STRING));

	// 为了测试 private static Tree tree;

	// 2014.10 新加
	private static Tree topictree;
	// public static AmAssitBrowser myBrowser;
	// private static Tree urlTree;
	// 2014.10 end.

	private Text txtSearch;
	// private static IViewPart tagcloudpart;
	private static IViewPart browserpart;

	private static List<KeyWord> currentSearchWords = new ArrayList<>();
	private static List<KeyWord> sendfromselectSearchWords = new ArrayList<>();

	private static int currentActionID = 0;
	private static String currentSearchID = "0";

	public void setTxtSeachText(List<KeyWord> selectedKeyWords) {
		sendfromselectSearchWords = selectedKeyWords;
	}

	public String getCurrentSearchID() {
		return currentSearchID;
	}

	public void setCurrentSearchID(String currentSearchID) {
		this.currentSearchID = currentSearchID;
	}

	public int getCurrentActionID() {
		return currentActionID;
	}

	public void setCurrentActionID(int currentActionID) {
		this.currentActionID = currentActionID;
	}

	public Text getTxtSearch() {
		return txtSearch;
	}

	public void setTxtSearch(Text txtSearch) {
		this.txtSearch = txtSearch;
	}

	private SashForm sashComposite;
	private Browser foamtreeBrowser;
	private Browser topicFilterBrowser;

	private String currentTopicName = "";
	// private Browser urlBrowser;

	Composite foamtreeComposite;

	String foamTreeFileNamePath = CommUtil.getFDUPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	String foamTreeTopicFilterFileNamePath = CommUtil.getFDUPluginWorkingPath()
			+ "/topicfilter.html";
	String searchHTMLPath = CommUtil.getFDUPluginWorkingPath() + "/search.html";

	public static List<TopicWEBPagesBean> allWebPages = new ArrayList<TopicWEBPagesBean>();

	@Override
	public void createPartControl(Composite arg0) {

		arg0.setLayout(new FillLayout());

		sashComposite = new SashForm(arg0, SWT.VERTICAL);

		// foamtreeComposite=new Composite(sashComposite, SWT.NONE);
		// foamtreeComposite.setLayout(new FillLayout());

		// 生成foamtree需要的js脚本，并将它们和显示内容文件foamtreetest.html放在一个目录下。
		// TODO: 考虑是否生成一次？
		initFoamTreeEnv(CommUtil.getFDUPluginWorkingPath());

		foamtreeBrowser = new Browser(sashComposite, SWT.BORDER);
		foamtreeBrowser
				.setToolTipText("Double Click to ROLL OUT!  Shift + Double Click to PULL BACK!");

		String foamTreeContent = ""; // 使用工具生成foamtree的内容
		genFoamTree(300, 200, foamTreeFileNamePath, foamTreeContent,
				"HelloHongwei");

		foamtreeBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);

				System.out.println("select group label is : " + e.title);
				if (e.title.trim().equals("foamtreetest.html")) {
					return;

				}

				if (txtSearch.getText().trim().equals("foamtreetest.html")) {
					txtSearch.setText("");
					e.title = "HelloHongwei";
				}

				if (!e.title.trim().toLowerCase()
						.equals("HelloHongwei".toLowerCase())) {
					// if (!CommUtil.compareString(Cache.getInstance()
					// .getCurrentBrowserTitle(), e.title)) {

					String searchtext = txtSearch.getText();
					boolean titleaddflag = true;

					String candidateWord = (e.title.trim()).replace(" ", ".");
					String temp2 = "";
					// ???? titleaddflag = true;
					for (String str : searchtext.split("[ ]")) {
						if (!str.trim().toLowerCase()
								.equals(candidateWord.toLowerCase())) {
							if (temp2.equals("")) {
								temp2 = str.trim();
							} else {
								temp2 = temp2 + " " + str.trim();
							}
						} else {
							titleaddflag = false;
						}

					}

					if (titleaddflag) {
						if (temp2.equals("")) {
							temp2 = candidateWord;

						} else {
							temp2 = temp2 + " " + candidateWord;
						}
					}

					txtSearch.setText(temp2);

				}
			}
		});

		foamtreeBrowser.setUrl(foamTreeFileNamePath);
		// browser.refresh();

		// ============

		SashForm topicSashForm = new SashForm(sashComposite, SWT.VERTICAL);

		// sashComposit 水平分割处
		Composite SearchComposite = new Composite(topicSashForm, SWT.NONE);
		// SearchComposite.setLayoutData(BorderLayout.CENTER);
		SearchComposite.setLayout(new GridLayout(2, false));

		txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL
				| SWT.MULTI);
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		gd_txtSearch.heightHint = -1;
		gd_txtSearch.widthHint = -1;
		txtSearch.setLayoutData(gd_txtSearch);
		txtSearch.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		txtSearch.setForeground(SWTResourceManager.getColor(255, 0, 0));
		txtSearch.setToolTipText("Input keyworks for searching");
		txtSearch.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.LF || e.character == SWT.CR) {
					manualSearch();

				}

			}
		});

		Button btnSearchGoogle = new Button(SearchComposite, SWT.NONE);
		btnSearchGoogle.setFont(SWTResourceManager.getFont(
				"Microsoft YaHei UI", 10, SWT.BOLD));
		btnSearchGoogle.setForeground(SWTResourceManager.getColor(0, 0, 0));
		btnSearchGoogle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		// btnSearchGoogle.setText("Search");
		btnSearchGoogle.setToolTipText("Search");
		btnSearchGoogle.setImage(Images.SEARCH2.createImage());
		btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				manualSearch();
				// 在这里放雪娇的 LDA的处理。使用JS脚本的技术 （目前放弃使用）
				// genSearchHTML(txtSearch.getText());
				// urlBrowser.setUrl(searchHTMLPath);
				// urlBrowser.refresh();
			}
		});

		// urlBrowser=new Browser(SearchComposite, SWT.None);
		// urlBrowser.setLayoutData(new
		// GridData(SWT.CENTER,SWT.CENTER,false,false,2,1));
		// urlBrowser.setEnabled(false);
		// urlBrowser.setVisible(false);
		//
		// urlBrowser.addTitleListener(new TitleListener() {
		//
		// @Override
		// public void changed(TitleEvent event) {
		// System.out.println("current search result:"+ event.title);
		// genURLlistforLingfengTopic(event.title);
		// }
		// });

		// SashForm topicComposite = new SashForm(SearchComposite,
		// SWT.VERTICAL);
		// topicComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 2, 1));

		Composite topicCom = new Composite(topicSashForm, SWT.NONE);
		topicCom.setLayout(new GridLayout(2, false));

		// topic list
		topictree = new Tree(topicCom, SWT.BORDER | SWT.CENTER);

		topictree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		topictree.setForeground(SWTResourceManager.getColor(0, 0, 0));

		topictree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;

				// 检查是否点击过这个topic
				if (item.getData() == "TOPICX") { // 从 allpages 中读数据

					int currentTopicindex = 0;
					// 定位 topic
					for (int i = 0; i < allWebPages.size(); i++) {
						if (item.getText()
								.trim()
								.equals(allWebPages.get(i).getTopicName()
										.trim())) {
							currentTopicindex = i;
						}
					}

					currentTopicName = item.getText().trim();
					
					genTopicWordsFoamTree(allWebPages.get(currentTopicindex).getMyfudanTopicWords());


					browserpart = FDUHelpSeekingPlugin
							.getDefault()
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.findView(
									"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

					if ((browserpart instanceof HelpSeekingBrowserView)) {
						HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;
						bv.getTopicContentText().setText(currentTopicName);
						bv.genUrlTree(allWebPages.get(currentTopicindex)
								.getPages());
					}

					
					
					// sendoutURLlist
				} else

				// 检查是否是topic

				{
					if (item.getData() == "TOPIC") {
						item.setData("TOPICX");
						// 两个模式同时处理，以提高效率，一方面 可以 就topic有关的URL
						// 先给出；另一方面，用户也可以在二次过滤后的foamtree中选择URL。

						// 新处理模式： 点击topic后，使用雪娇的LDA 提取topic和详细的词， 并生成新的foamtree，
						// 展示

						// 获得URLlist
						ArrayList<String> urlList = new ArrayList<String>();
						List<WEBPageBean> currentWebPages = new ArrayList<WEBPageBean>();
						TopicWEBPagesBean currentTopicWEBPages = new TopicWEBPagesBean();

						TreeItem[] treeItemSet = item.getItems();
						for (int i = 0; i < treeItemSet.length; i++) {
							WEBPageBean webpage = new WEBPageBean();
							urlList.add(treeItemSet[i].getData().toString());
							webpage.setTitle(treeItemSet[i].getItem(0)
									.getText());
							webpage.setUrl(treeItemSet[i].getItem(1).getText());
							webpage.setSummary(treeItemSet[i].getItem(2)
									.getText());

							currentWebPages.add(webpage);

						}

						currentTopicWEBPages
								.setTopicName(item.getText().trim());
						currentTopicWEBPages.setPages(currentWebPages);

						currentTopicName = item.getText().trim();

						// //老处理模式，点击topic 后在browser view处 显示该topic下的URL.
						// // 将topic的所有数据传到browser窗口

						browserpart = FDUHelpSeekingPlugin
								.getDefault()
								.getWorkbench()
								.getActiveWorkbenchWindow()
								.getActivePage()
								.findView(
										"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

						if ((browserpart instanceof HelpSeekingBrowserView)) {
							HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;
							bv.getTopicContentText().setText(currentTopicName);
							bv.genUrlTree(currentTopicWEBPages.getPages());
						}

						allWebPages.add(currentTopicWEBPages);

						if (!currentTopicWEBPages.getPages().isEmpty()) {
							// 调用LDA
							try {
								List<FudanTopicWithWordsListBean> myfudanTopicWords = LdaGibbsSampling
										.fduTopicURLfilter(item.getText()
												.trim(), allWebPages);
								currentTopicWEBPages
										.setMyfudanTopicWords(myfudanTopicWords);

								// 传给foamtree 并显示
								genTopicWordsFoamTree(myfudanTopicWords);

							} catch (ParserException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		topicFilterBrowser = new Browser(topicSashForm, SWT.BORDER);
		topicFilterBrowser
				.setToolTipText("Double Click to ROLL OUT!  Shift + Double Click to PULL BACK!");

		topicSashForm.setWeights(new int[] { 100, 300, 300 });

		String foamTreeTopicFilterContent = ""; // 使用工具生成foamtree的内容
		genFoamTree(300, 200, foamTreeTopicFilterFileNamePath,
				foamTreeTopicFilterContent, "HelloHongwei");

		topicFilterBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);
				System.out.println("select topic filter label is : " + e.title);
				if (e.title.trim().equals("topicfilter.html")) {
					return;
				}
				
				if (!e.title.equals("HelloHongwei")) {
					System.out
							.println("???? 添加 topicFilterBrowser 处理选择的topic 过滤后的关键词，以获得URL列表");

					int currentTopicindex = 0;
					// 定位 topic
					for (int i = 0; i < allWebPages.size(); i++) {
						if (currentTopicName.trim().equals(
								allWebPages.get(i).getTopicName().trim())) {
							currentTopicindex = i;
						}
					}

					// 将词汇去选择过滤URL
					List<WEBPageBean> usefulWebpages = new ArrayList<WEBPageBean>();
					List<WEBPageBean> usefulTopicWEBPages = new ArrayList<WEBPageBean>();
					usefulTopicWEBPages = (allWebPages.get(currentTopicindex)).getPages();
					for (int j = 0; j < usefulTopicWEBPages.size(); j++) {
						if (usefulTopicWEBPages.get(j).getContent()
								.contains(e.title.trim())) {
							usefulWebpages.add(usefulTopicWEBPages.get(j));
						}

					}

					// 传到amAssistbrowser view 中
					browserpart = FDUHelpSeekingPlugin
							.getDefault()
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.findView(
									"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

					if ((browserpart instanceof HelpSeekingBrowserView)) {
						HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;
						// bv.getTopicContentText().setText(currentTopicName);
						bv.genUrlTree(usefulWebpages);
					}

				}
			}

		});

		topicFilterBrowser.setUrl(foamTreeTopicFilterFileNamePath);
		// topicFilterBrowser.refresh();

		sashComposite.setWeights(new int[] { 200, 400 });

	}

	protected void genSearchHTML(String querystr) {

		// 在此处从14个定制引擎中随机选择一个
		int temp = CommUtil.randomInt(CommUtil.getKeyCxList().size() - 1, 0);
		String cse_key = CommUtil.getKeyCxList().get(temp).getKey();
		String cse_cx = CommUtil.getKeyCxList().get(temp).getCx();

		String searchhtmlcontent = "<!DOCTYPE html> \n <html>\n"
				+ "<head> \n"
				+ "<title>HelloHongweiCustomSearchAPI</title> \n"
				+ "</head> \n"
				+ "<body> \n"
				+ "<div id=\"content\"></div> \n"
				+ "<script> \n"
				+ "var s=\"\";\n"
				+ " function hndlr(response) {\n"
				+ " for (var i = 0; i < response.items.length; i++) {\n"
				+ " var item = response.items[i];\n"
				// +
				// " s +=\"item \"+i+\"<br>\"+item.htmlTitle+\"<br>\"+item.link+\"<br>\"+item.snippet+ \"<br>\" ;"
				+ "document.getElementById(\"content\").innerHTML+=\"###hongwei###\"+item.htmlTitle+\"###hongwei###\"+item.link+\"###hongwei###\"+item.snippet ;\n"

				+ " s +=\"###hongwei###\"+item.htmlTitle+\"###hongwei###\"+item.link+\"###hongwei###\"+item.snippet ;\n"
				+ "}\n"
				+ "window.document.title=s;\n"
				+ " }\n"
				+ "</script>\n"

				+ "<script src=\"https://www.googleapis.com/customsearch/v1?key="
				+ cse_key
				+ "&amp;cx="
				+ cse_cx
				+ "&amp;q="
				+ querystr
				+ "&amp;filter=1&amp;startIndex=0&amp;itemsPerPage=20&amp;callback=hndlr\">"
				+ "</script>\n" + "</body>\n" + "</html>";

		FileHelper.writeNewFile(searchHTMLPath, searchhtmlcontent);

	}

	protected void genURLlistforLingfengTopic(String title) {
		// TODO Auto-generated method stub
		// TODO BUG

		genURLlist(title);

		if (googlesearchList.size() > 0) {

			for (int i = resultsForTopicList.size() - 1; i >= 0; i--) {
				resultsForTopicList.remove(i);
			}
			// end of 2014.10.15

			int indexResultslist = 0;
			for (WEBResult webResult : googlesearchList) {
				String titleNoFormating = webResult.getTitle();
				titleNoFormating = titleNoFormating.replaceAll("&quot;", "\"");
				titleNoFormating.replaceAll("&#39;", "\'");
				titleNoFormating.replaceAll("<b>", " ");
				titleNoFormating.replaceAll("</", " ");
				titleNoFormating.replaceAll("b>", " ");

				// 2014.10.15
				webResult.setTitle(titleNoFormating);
				WEBResult forTopicPrepareItem = new WEBResult();
				forTopicPrepareItem.setContent(webResult.getContent());
				forTopicPrepareItem.setUrl(webResult.getUrl());
				forTopicPrepareItem.setTitle(webResult.getTitle());
				resultsForTopicList.add(forTopicPrepareItem);

			}

			genTopicTree();

		}

	}

	private void genURLlist(String title) {

		List<String> liString = CommUtil.stringToList(title, "[###hongwei###]");
		if (liString.size() > 0) {
			if (liString.size() % 3 == 0) {

				// TODO BUG
				for (int i = googlesearchList.size() - 1; i >= 0; i--) {
					googlesearchList.remove(i);
				}

				for (int i = 0; i < liString.size();) {
					WEBResult webResult = new WEBResult();
					webResult.setTitle(liString.get(i).trim());
					i = i + 1;
					webResult.setUrl(liString.get(i).trim());
					i = i + 1;
					webResult.setContent(liString.get(i).trim());
					i = i + 1;
					googlesearchList.add(webResult);

				}

			} else {
				MessageDialog
						.openInformation(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getShell(),
								"Search google get wrong data! ",
								"Sorry search get wrong or no results! Please search agin wait for a while!");

			}
		}

	}

	protected void genTopicWordsFoamTree(
			List<FudanTopicWithWordsListBean> myfudanTopicWords) {

		// dataObject: {
		// groups: [
		// { label:"Group 1", groups: [
		// { label:"Group 1.1" },
		// { label:"Group 1.2" },
		// { label:"Group 1.3" }
		// ]},
		// { label:"Group 2", groups: [
		// { label:"Group 2.1" },
		// { label:"Group 2.2" }
		// ]},
		// { label:"Group 3" }
		// ]
		// }

		 //替换代码一
		 //两层展示topic以及topic内的词
		 String labelWeight="";
		 for (int i = 0; i < myfudanTopicWords.size(); i++) {
		 if (labelWeight.equals("")) {
		 labelWeight=myfudanTopicWords.get(i).genFoamTreeGroupString();
		 }
		 else {
		 labelWeight=labelWeight+", "+myfudanTopicWords.get(i).genFoamTreeGroupString();
		
		 }
		
		 }
		 //结束一

		// 替换代码二
		// 一层展示 将topic 中所有的词 收集在一起，
//		List<FudanTopicWordsBean> wordsList = new ArrayList<FudanTopicWordsBean>();
//
//		for (int i = 0; i < myfudanTopicWords.size(); i++) {
//			for (int j = 0; j < myfudanTopicWords.get(i).getWordsList().size(); j++) {
//				String candidateTerm = myfudanTopicWords.get(i).getWordsList()
//						.get(j).toString().toLowerCase();
//				boolean testexists = true;
//				int indexSameWord = 0;
//				for (int k = 0; k < wordsList.size(); k++) {
//					if (candidateTerm.equals(wordsList.get(k).getWordName()
//							.toLowerCase())) {
//						testexists = false;
//						indexSameWord = k;
//					}
//				}
//
//				if (testexists) {
//					wordsList.add(myfudanTopicWords.get(i).getWordsList()
//							.get(j));
//				} else {
//					double wordWeightpre = wordsList.get(indexSameWord)
//							.getWordWeight();
//					wordsList.get(indexSameWord).setWordWeight(
//							wordWeightpre
//									+ myfudanTopicWords.get(i).getWordsList()
//											.get(j).getWordWeight());
//				}
//
//			}
//
//		}
//		// 生成一层展示的代码：
//
//		String labelWeight = "";
//		for (int i = 0; i < wordsList.size(); i++) {
//			if (labelWeight.equals("")) {
//				labelWeight = wordsList.get(i).genFoamTreeObjectString();
//			} else {
//				labelWeight = labelWeight + ", "
//						+ wordsList.get(i).genFoamTreeObjectString();
//
//			}
//
//		}
//
//		// 结束二

		String foamTreeContent = "";
		foamTreeContent = "dataObject: {" + "groups: [" + labelWeight + "]"
				+ "}";
		int width = topicFilterBrowser.getBounds().width;
		int height = topicFilterBrowser.getBounds().height;
		genFoamTree(width, height, foamTreeTopicFilterFileNamePath,
				foamTreeContent, "HelloHongwei");

		// 装载网页
		topicFilterBrowser.setUrl(foamTreeTopicFilterFileNamePath);
		// topicFilterBrowser.refresh();

	}

	public void genFoamTree(int width, int height, String foamtreeFileNamePath,
			String foamTreeContent, String title) {

		if (foamTreeContent.equals("")) {
			foamTreeContent = "dataObject: {"
					+ "groups: ["
					+ "{ label: \"Welcome\", weight: 2.0 ,type: \"node\" },"
					+ "{ label: \"HelpSeeking\", weight: 4.0 ,type: \"node\" },"
					+ "{ label: \"To\", weight: 0.5 ,type: \"node\"},"
					+ "{ label: \"Plugin\", weight: 3.0 ,type: \"node\"},"
					+ "{ label: \"tool\", weight: 1.0 ,type: \"node\"},"
					+ "{ label: \"Double Click Rollout\", weight: 4.0 ,type: \"node\"},"
					+ "{ label: \"Shift + Double Click Pullback\", weight: 4.0 ,type: \"node\"}"
					+ "]" + "}";

			title = "HelloHongwei";
		}

		// File htmlFile = new File(foamtreeFileNamePath); //
		// CommUtil.getPluginCurrentPath()+"/foamtreetest.html"
		String foamtreehtmlcontent = "<!DOCTYPE html> \n" + "<html>\n"
				+ "<head>\n" + "<title>\n"
				+ title
				+ "</title>\n"
				+ "<meta charset=\"utf-8\" />\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<div id=\"visualization\" style=\"width: "
				+ String.valueOf(width < 100 ? 300 : width)
				+ "px; height: "
				+ String.valueOf(height < 100 ? 200 : height)
				+ "px\"></div> \n"
				+ "<script src=\"carrotsearch.foamtree.js\"></script>\n"
				+ "<script language=\"javascript\">\n"
				+ " window.addEventListener(\"load\", function() {\n"
				+ "var foamtree = new CarrotSearchFoamTree({\n"
				+ "id: \"visualization\""
				+ "\n,\n"
				+ foamTreeContent
				// + "\n,\n"
				// + "onGroupDoubleClick: function(event) { \n"
				// + "window.document.title=event.group.label;\n"
				// + "}\n"
				+ "\n,\n"
				+ "onGroupClick: function (event) {\n"
				+ "if (event.group.type==\"leaf\") {"
				+ "window.document.title=event.group.label;}\n"
				+ "}\n"
				+ "});\n" + "});\n" + "</script>\n" + "</body>\n" + "</html>\n";

		FileHelper.writeNewFile(foamtreeFileNamePath, foamtreehtmlcontent);
	}

	public void initFoamTreeEnv(String foamtreeFilesPath) {
		Resource foamtreeJsResource = new Resource();
		String foamtreejscontent;

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.asserts.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.asserts.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.js", true);
		FileHelper.writeNewFile(
				foamtreeFilesPath + "/carrotsearch.foamtree.js",
				foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.hints.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.hints.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.loading.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.loading.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.relaxationprogress.js",
				true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.relaxationprogress.js",
				foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.treemodel.js", true);
		FileHelper
				.writeNewFile(foamtreeFilesPath
						+ "/carrotsearch.foamtree.util.treemodel.js",
						foamtreejscontent);
	}

	private void manualSearch() {
		System.out.println("Say start manual search ...");

		String queryText = txtSearch.getText().trim();

		if (!queryText.equals("")) {

			String searchID = "P" + getCurrentSearchID();

			setCurrentSearchID(searchID);

			try {
				dosearch(queryText);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	static SearchResults sResults;
	static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();
	static List<WEBResult> resultsForTopicList = new ArrayList<WEBResult>();

	private static void dosearch(String search) throws InterruptedException {

		
		
		
		Timestamp starttime;
		String searchResultOutput = "\n============\n";
		starttime = new Timestamp(System.currentTimeMillis());

		// IPreferenceStore
		// ps=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
		// String cse_key=ps.getString(PreferenceConstants.CSE_KEY);
		// String cse_cx=ps.getString(PreferenceConstants.CSE_CX);

		// 在此处从14个定制引擎中随机选择一个
		int temp = CommUtil.randomInt(CommUtil.getKeyCxList().size() - 1, 0);
		String cse_key = CommUtil.getKeyCxList().get(temp).getKey();
		String cse_cx = CommUtil.getKeyCxList().get(temp).getCx();

		LoopGoogleAPICall apiCall = new LoopGoogleAPICall(cse_key, cse_cx,
				search);

		apiCall.start();
		apiCall.join();

		googlesearchList = apiCall.getCurrentResults();

		if (googlesearchList.size() > 0) {
			// 清除tree tree.removeAll();
			// 2014.10.15

			// TODO BUG
			for (int i = resultsForTopicList.size() - 1; i >= 0; i--) {
				resultsForTopicList.remove(i);
			}
			// end of 2014.10.15

			int indexResultslist = 0;
			for (WEBResult webResult : googlesearchList) {
				String titleNoFormating = webResult.getTitle();
				titleNoFormating = titleNoFormating.replaceAll("&quot;", "\"");
				titleNoFormating.replaceAll("&#39;", "\'");
				titleNoFormating.replaceAll("<b>", " ");
				titleNoFormating.replaceAll("</", " ");
				titleNoFormating.replaceAll("b>", " ");

				searchResultOutput = searchResultOutput + "\n"
						+ webResult.toString();

				// 2014.10.15
				webResult.setTitle(titleNoFormating);
				WEBResult forTopicPrepareItem = new WEBResult();
				forTopicPrepareItem.setContent(webResult.getContent());
				forTopicPrepareItem.setUrl(webResult.getUrl());
				forTopicPrepareItem.setTitle(webResult.getTitle());
				resultsForTopicList.add(forTopicPrepareItem);

				// end of 2014.10.15

			} // end foreach googlesearchlist

			Timestamp endtime = new Timestamp(System.currentTimeMillis());

			// 生成选择和自己输入的关键词并记录数据库

		} // if googlesearchlist.size > 0
		else {
			System.out.println("No return results!");
			MessageDialog
					.openInformation(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"Search google faild! ",
							"Sorry connection wrong or no results! Please search agin wait for a while!");
		}
		// 2014.10.15
		// 调用topic API 生成topictree

		genTopicTree();

		// end of 2014.10.15

	} // end dosearch

	public static void genTopicTree() {

		List<Cluster> clusters = CarrotTopic.fromWebResults(Collections
				.unmodifiableList(resultsForTopicList));

		// ???? 测试试用句子 记得删除！！！

//		List<WEBResult> results = SampleWebResults.WEB_RESULTS;
//		if (clusters.isEmpty()) {
//			clusters = CarrotTopic.fromWebResults(results);
//			System.out.println("clusters is null");
//		}

		//
		// 生成topic tree
		// 只考虑第一层， level=0时 topic的数量 每个topic下面的子topic暂时不给出； cluster中已有，需要时可以去取
		topictree.removeAll();
		// urlTree.removeAll();

		// 重新生成新检索，新topics 因而需要移除allwebpages
		for (int i = allWebPages.size(); i > 0; i--) {
			allWebPages.remove(i - 1);

		}

		TreeItem topicsumm = new TreeItem(topictree, SWT.NONE);
		topicsumm.setData("TOPIC_SUMMARY");

		int totalurlnum = 0;

		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			totalurlnum = totalurlnum + c.size();

			TreeItem topicitem = new TreeItem(topicsumm, SWT.NONE);
			topicitem.setText(c.getLabel() + " (" + c.size() + ")");
			topicitem.setData("TOPIC");
			// 只有点击topic后，以该topic为单位，生成URL list 调用雪娇给出的LDA处理
			// 实现代码见 topictree的选择事件
			topicitem.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_BLUE));

			for (int j = 0; j < c.getAllDocuments().size(); j++) {
				Document doc = c.getAllDocuments().get(j);
				TreeItem itemoftopic = new TreeItem(topicitem, SWT.NONE);
				itemoftopic.setText(doc.getTitle());
				itemoftopic.setData(doc.getContentUrl());

				TreeItem itemtitle = new TreeItem(itemoftopic, SWT.NONE);
				itemtitle.setText(doc.getTitle());
				itemtitle.setData(doc.getContentUrl());

				TreeItem itemurl = new TreeItem(itemoftopic, SWT.NONE);
				itemurl.setText(doc.getContentUrl());
				itemurl.setData(doc.getContentUrl());

				TreeItem itemsummary = new TreeItem(itemoftopic, SWT.NONE);
				itemsummary.setText(doc.getSummary());
				itemsummary.setData(doc.getContentUrl());

			}

		}

		// topicitem.setExpanded(true);
		topicsumm.setText("All topics ( Topics: " + clusters.size() + " URLs: "
				+ totalurlnum + ")");
		topicsumm.setExpanded(true);

	}

	public static void savekeywordsToDatabase(Query query, String searchID,
			String search, Timestamp starttime, String searchResultOutput) {
		for (int i = 0; i < currentSearchWords.size(); i++) {
			currentSearchWords.remove(i);
		}

		List<String> searchWords = CommUtil.arrayToList(search.split("[ ]"));

		for (int i = 0; i < searchWords.size(); i++) {
			boolean flag = true;
			for (int j = 0; j < sendfromselectSearchWords.size(); j++) {
				if (sendfromselectSearchWords.get(j).getKeywordName().trim()
						.equals(searchWords.get(i).trim())) {
					sendfromselectSearchWords.get(j).setPositionInUseString(j);
					sendfromselectSearchWords.get(j).setRecommand(true);
					sendfromselectSearchWords.get(j).setTimes(starttime);
					sendfromselectSearchWords.get(j).setLastSearchID(searchID);
					currentSearchWords.add(sendfromselectSearchWords.get(j));
					flag = false;
				}
			}
			if (flag) {
				KeyWord word = new KeyWord();
				word.setKeywordName(searchWords.get(i).trim());
				word.setLastSearchID(searchID);
				word.setPositionInUseString(i);
				word.setTimes(starttime);
				word.setSearchID("NO");// 意味着这个是人工词汇
				word.setRecommand(false);
				currentSearchWords.add(word);
			}
		}// end for i

		DatabaseUtil.addSearchWordsToDataBase(currentSearchWords);

		// 需要保存关键词和当前cache到数据库中：
		DatabaseUtil.addKeyWordsToDataBase(query);
		for (SearchNode snNode : sResults.getSearchNode()) {
			DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(),
					snNode);
		}
		searchResultOutput = query.toString() + searchResultOutput
				+ "\n============\n";
		FileHelper.appendContentToFile("result.txt", searchResultOutput);

		Cache.getInstance().setTimerAutoSearchmode(0);
		Cache.getInstance().setLastAutoSearchTime(
				new Timestamp(System.currentTimeMillis()));
	}
	
	public static String getSimpleWords(String tempstr) {
		String resultstr="";
    	if (tempstr.contains("(")) {
						
			int firstpartlastIndex=tempstr.indexOf('(');
			String firstPart=tempstr.substring(0, firstpartlastIndex);
			System.out.println("firstpart: "+firstPart);
			List<String> namePart=CommUtil.stringToList(firstPart, "[.]");
			String name=namePart.get(namePart.size()-1);
			
			String secondPart=tempstr.substring(firstpartlastIndex+1,tempstr.length()-1);
			System.out.println("secondpart: "+ secondPart);
			List<String> secondkeywordparts=new ArrayList<String>();
			secondkeywordparts=CommUtil.stringToList(secondPart, "[,]");
			
			String parameterList="";
			
			for (int i = 0; i < secondkeywordparts.size(); i++) {
				if (secondkeywordparts.get(i).trim().equals("I")  ) {
					continue;
				}

				if (secondkeywordparts.get(i).trim().equals("Z")  ) {
					continue;
				}

				
				if (secondkeywordparts.get(i).trim().contains(" ")) {
					List<String> para=new ArrayList<String>();
					para=CommUtil.stringToList(secondkeywordparts.get(i), "[ ]");
					
					if (parameterList.equals("")) {
						parameterList=para.get(0);
					}else
					{parameterList=parameterList+" , "+para.get(0);}
					
				}
				else
				{
				List<String> para=new ArrayList<String>();
				para=CommUtil.stringToList(secondkeywordparts.get(i), "[.]");

				if (parameterList.equals("")) {
					parameterList=para.get(para.size()-1);
				}else
				{
					parameterList=parameterList+" , "+para.get(para.size()-1);}
				}
			}
			
			if (parameterList.equals("")) {
				resultstr=name;
			}else
			resultstr=name +" ("+parameterList+")";
 
			}
			else 
			{
				List<String> packageClassName=new ArrayList<String>();
				packageClassName=CommUtil.stringToList(tempstr, "[.]");
					resultstr=packageClassName.get(packageClassName.size()-1);
				
				
			}
		
        System.out.println("last formal: result" + resultstr);
        return resultstr;
	}


	public void setNewWordsAndMode(List<KeyWord> keyWordsforQuery, int mode) {
		// 生成foamtree的候选词和权重字符串
		// + "{ label: \"Welcome\", weight: 2.0 },"
		// + "{ label: \"HelpSeeking\", weight: 4.0 },"
		// + "{ label: \"To\", weight: 1.0},"
		// + "{ label: \"Plugin\", weight: 2.0 },"
		// + "{ label: \"tool\", weight: 1.0 }"
		

		//试着处理给出的词汇截短符号：
		//java.io.xxx   java.io.xx.yy(zzz)
		for (int i = 0; i < keyWordsforQuery.size(); i++) {
			String tempstr=keyWordsforQuery.get(i).getKeywordName();
			keyWordsforQuery.get(i).setKeywordName(getSimpleWords(tempstr));
		}
		
		
		
		
		
		
		
		

		String searchwords = "";
		// String currentWord="";
		String labelWeight = "";
		for (int i = 0; i < keyWordsforQuery.size(); i++) {

			String labels = keyWordsforQuery.get(i).getKeywordName()
					.replace(".", " ");

			labelWeight = labelWeight
					+ "{ label: \""
					// + keyWordsforQuery.get(i).getKeywordName() +
					// "\", weight: "
					+ labels + "\", weight: "
					+ keyWordsforQuery.get(i).getScore() + " ,type: \"leaf\"},";
			searchwords = searchwords + " "
					+ keyWordsforQuery.get(i).getKeywordName();
			System.out.println("candidate keyword No." + i + " : "
					+ keyWordsforQuery.get(i).getKeywordName());

		}

		String foamTreeContent = "dataObject: {" + "groups: [" + labelWeight
				+ "]" + "}";

		int width = foamtreeBrowser.getBounds().width;
		int height = foamtreeBrowser.getBounds().height;
		System.out.println("width & height:" + width + ":" + height);

		// 生成网页
		genFoamTree(width, height, foamTreeFileNamePath, foamTreeContent,
				"HelloHongwei"); // Cache.getInstance().getCurrentBrowserTitle();

		// 装载网页
		foamtreeBrowser.setUrl(foamTreeFileNamePath);
		// browser.refresh();

		// mode=1时，不自动查询， mode=2时自动查询

		// txtSearch.setText(searchwords);

		if (mode == 2) {
			try {
				dosearch(searchwords);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
