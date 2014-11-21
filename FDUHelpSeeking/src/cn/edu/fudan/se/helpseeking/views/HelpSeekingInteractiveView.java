package cn.edu.fudan.se.helpseeking.views;

import java.security.AllPermission;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWithWordsListBean;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWordsBean;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.NewQueryRec;
import cn.edu.fudan.se.helpseeking.bean.NewTopicInfoRec;
import cn.edu.fudan.se.helpseeking.bean.NewTopicWebPagesInfo;
import cn.edu.fudan.se.helpseeking.bean.NewWebUseInfo;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
import cn.edu.fudan.se.helpseeking.bean.TopicWEBPagesBean;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.views.Images;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.util.CarrotTopic;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;

public class HelpSeekingInteractiveView extends ViewPart {

	public HelpSeekingInteractiveView() {
	}

	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView"; //$NON-NLS-1$
	public static final String browserviewID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView";

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

	private static Text txtSearch;
	private static IViewPart dOIViewPart;
	private static IViewPart browserpart;

	private static List<KeyWord> currentSearchWords = new ArrayList<>();
	private static List<KeyWord> sendfromselectSearchWords = new ArrayList<>();

	private static int currentActionID = 0;
	private static String currentSearchID = "0";
	private static int searchFlag = 0;// 大于0就是第二次检索

	public static int getSearchFlag() {
		return searchFlag;
	}

	public static void setSearchFlag(int searchFlag) {
		HelpSeekingInteractiveView.searchFlag = searchFlag;
	}

	public void setTxtSeachText(List<KeyWord> selectedKeyWords) {
		sendfromselectSearchWords = selectedKeyWords;
	}

	public static String getCurrentSearchID() {
		return currentSearchID;
	}

	public void setCurrentSearchID(String currentSearchID) {
		this.currentSearchID = currentSearchID;
	}

	public static int getCurrentActionID() {
		return currentActionID;
	}

	public static void setCurrentActionID(int currentAID) {
		currentActionID = currentAID;
	}

	public Text getTxtSearch() {
		return txtSearch;
	}

	public void setTxtSearch(Text txtSearch) {
		this.txtSearch = txtSearch;
	}

	private SashForm sashComposite;

	private String currentTopicName = "";

	String foamTreeFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	// String foamTreeTopicFilterFileNamePath =
	// CommUtil.getFDUHelpseekingPluginWorkingPath()
	// + "/topicfilter.html";
	String searchHTMLPath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/search.html";

	public static List<TopicWEBPagesBean> allWebPages = new ArrayList<TopicWEBPagesBean>();
	public static List<WEBPageBean> allpageslist = new ArrayList<WEBPageBean>();

	@Override
	public void createPartControl(Composite arg0) {

		arg0.setLayout(new FillLayout());

		sashComposite = new SashForm(arg0, SWT.VERTICAL);

		Composite SearchComposite = new Composite(sashComposite, SWT.NONE);
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

			}
		});

		Composite checkComposite = new Composite(sashComposite, SWT.NONE);
		// SearchComposite.setLayoutData(BorderLayout.CENTER);
		checkComposite.setLayout(new GridLayout(2, false));

		Label tweatlabel = new Label(checkComposite, SWT.None);
		tweatlabel.setText("Tweat ranking by API DOIs");
		tweatlabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		Button checkbox = new Button(checkComposite, SWT.CHECK);
		checkbox.setToolTipText("Tweat ranking by API DOIs");
		checkbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				1, 1));
		checkbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				// 还需要写代码 当选上这一项后 修改Google的检索设置
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				// 还需要写代码 当选上这一项后 修改Google的检索设置

			}
		});

		Label websitelabel = new Label(checkComposite, SWT.None);
		websitelabel.setText("Select preferred web site category");
		websitelabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true, 1, 1));

		final Combo websiteList = new Combo(checkComposite, SWT.MULTI
				| SWT.V_SCROLL | SWT.READ_ONLY);

		websiteList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		websiteList.add("All");
		websiteList.add("Technical Blogs (TB)");
		websiteList.add("Code Examples (CE)");
		websiteList.add("Discussion Forum (DF)");
		websiteList.add("Q&A web site (QA)");

		websiteList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				// System.out.println(websiteList.getSelectionIndex());
				searchEngineSelect = websiteList.getSelectionIndex();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Composite topicComposite = new Composite(sashComposite, SWT.NONE);
		// SearchComposite.setLayoutData(BorderLayout.CENTER);
		topicComposite.setLayout(new GridLayout(2, false));

		// topic list
		topictree = new Tree(topicComposite, SWT.BORDER | SWT.CHECK
				| SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);

		topictree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		topictree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		TreeItem it1 = new TreeItem(topictree, SWT.None);
		it1.setText("welcome ....");

		topictree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;

				if (FDUHelpSeekingPlugin
						.getDefault()
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.findView(
								"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView") instanceof HelpSeekingBrowserView) {
					try {
						PlatformUI
								.getWorkbench()
								.getActiveWorkbenchWindow()
								.getActivePage()
								.showView(
										"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
					} catch (PartInitException e1) {
						// TODO Auto-generated catch block
						return;
					}
				}

				if (item.getData().equals("TOPIC")
						|| item.getData().equals("TOPICX")) {

					TreeItem[] treeItemSet = item.getItems();
					List<WEBPageBean> newAllpagelist = new ArrayList<WEBPageBean>();// 每次点击时都将最新选择的对象放到前面
					List<WEBPageBean> leftpageslist = new ArrayList<WEBPageBean>();// 不是本次选择的对象

					// 是topic 时 修正 所有URL的排序 调整 allpageslist的真假值
					if (item.getChecked()) {
						for (int i = 0; i < treeItemSet.length; i++) {
							// webpage.setUrl(treeItemSet[i].getItem(1).getText());
							for (int j = 0; j < allpageslist.size(); j++) {

								if (allpageslist
										.get(j)
										.getUrl()
										.equals(treeItemSet[i].getItem(1)
												.getText())) {
									allpageslist.get(j).setSelect(true);
									newAllpagelist.add(allpageslist.get(j));
									break;
								}
							}
						}

					} else {
						for (int i = 0; i < treeItemSet.length; i++) {
							// webpage.setUrl(treeItemSet[i].getItem(1).getText());
							for (int j = 0; j < allpageslist.size(); j++) {

								if (allpageslist
										.get(j)
										.getUrl()
										.equals(treeItemSet[i].getItem(1)
												.getText())) {
									allpageslist.get(j).setSelect(false);
									break;
								}
							}
						}
					}

//					for (int i = 0; i < allpageslist.size(); i++) {
//						for (int j = 0; j < newAllpagelist.size(); j++) {
//							if (!allpageslist.get(i).getUrl()
//									.equals(newAllpagelist.get(j).getUrl())) {
//								leftpageslist.add(allpageslist.get(i));
//
//							}
//						}
//
//					}
//
//					for (int i = 0; i < leftpageslist.size(); i++) {
//						newAllpagelist.add(leftpageslist.get(i));
//					}
//
//					allpageslist = newAllpagelist;

				}

				// 检查是否点击过这个topic
				if (item.getData().equals("TOPICX")) { // 从 allpages 中读数据

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

					// 注销
					// genTopicWordsFoamTree(allWebPages.get(currentTopicindex).getMyfudanTopicWords());

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
						String topicId = "";

						for (int i = 0; i < currentTopicInfoRecs.size(); i++) {
							if (currentTopicInfoRecs.get(i).equals(
									currentTopicName)) {
								topicId = currentTopicInfoRecs.get(i)
										.getTopicId();
							}
						}

						bv.genUrlTree(currentTopicName, allpageslist,
								SearchList, currentSearchID, topicId);

						NewWebUseInfo nwuiInfo = new NewWebUseInfo();

						nwuiInfo.setTopicName(currentTopicName);
						nwuiInfo.setTopicId(topicId);

						nwuiInfo.setOpenTime(new Timestamp(System
								.currentTimeMillis()));

						DatabaseUtil.addNewWebUseInfo(nwuiInfo);

					}

					// sendoutURLlist
				}

				else

				// 检查是否是topic

				{
					if (item.getData() == "TOPIC") {

						// 注销替代语句
						item.setData("TOPICX");
						item.setForeground(Display.getDefault().getSystemColor(
								SWT.COLOR_GREEN));
						// 获得URLlist
						ArrayList<String> urlList = new ArrayList<String>();
						List<WEBPageBean> currentWebPages = new ArrayList<WEBPageBean>();
						TopicWEBPagesBean curTopicWEBPages = new TopicWEBPagesBean();

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

						curTopicWEBPages.setTopicName(item.getText().trim());
						curTopicWEBPages.setPages(currentWebPages);

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
							String topicId = "";

							for (int i = 0; i < currentTopicInfoRecs.size(); i++) {
								if (currentTopicInfoRecs.get(i).equals(
										currentTopicName)) {
									topicId = currentTopicInfoRecs.get(i)
											.getTopicId();
								}
							}

							bv.genUrlTree(currentTopicName, allpageslist,
									SearchList, currentSearchID, topicId);

							NewWebUseInfo nwuiInfo = new NewWebUseInfo();

							nwuiInfo.setTopicName(currentTopicName);
							nwuiInfo.setTopicId(topicId);

							nwuiInfo.setOpenTime(new Timestamp(System
									.currentTimeMillis()));

							DatabaseUtil.addNewWebUseInfo(nwuiInfo);

						}

						allWebPages.add(curTopicWEBPages);
						setCurrentTopicItem(item);
						setCurrrentTopicWEBPages(curTopicWEBPages);

					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		sashComposite.setWeights(new int[] { 100, 100, 400 });

	}

	TopicWEBPagesBean currrentTopicWEBPages = new TopicWEBPagesBean();
	TreeItem currentTopicItem;

	public TopicWEBPagesBean getCurrrentTopicWEBPages() {
		return currrentTopicWEBPages;
	}

	public void setCurrrentTopicWEBPages(TopicWEBPagesBean currrentTopicWEBPages) {
		this.currrentTopicWEBPages = currrentTopicWEBPages;
	}

	public TreeItem getCurrentTopicItem() {
		return currentTopicItem;
	}

	public void setCurrentTopicItem(TreeItem currentTopicItem) {
		this.currentTopicItem = currentTopicItem;
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
				forTopicPrepareItem.setSummary(webResult.getSummary());
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
					webResult.setSummary(liString.get(i).trim());
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
				+ "window.document.title=event.group.id;}\n"
				+ "}\n"
				+ "});\n"
				+ "});\n" + "</script>\n" + "</body>\n" + "</html>\n";

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

	public String currentQueryText = "";

	public String getCurrentQueryText() {
		return currentQueryText;
	}

	public void setCurrentQueryText(String currentQueryText) {
		this.currentQueryText = currentQueryText;
	}

	private static NewQueryRec queryRecsfordatabase = new NewQueryRec();

	private void manualSearch() {
		System.out.println("Say start manual search ...");

		topictree.removeAll();
		TreeItem it1 = new TreeItem(topictree, SWT.None);
		it1.setText("start searching ...");
		TreeItem it2 = new TreeItem(topictree, SWT.None);
		it2.setText("Please waiting...");
		it2.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

		String queryText = txtSearch.getText().trim();

		if (!queryText.equals("")) {

			setSearchFlag(getSearchFlag() + 1);

			String searchID;
			if (getSearchFlag() > 0) {
				searchID = "P" + getCurrentActionID() + ("A" + getSearchFlag());
			} else {
				searchID = "P" + getCurrentActionID();
			}

			setCurrentSearchID(searchID);

			setCurrentQueryText(queryText);

			dosearch(getCurrentQueryText());

		}
	}

	static SearchResults sResults;
	static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();
	static List<WEBResult> resultsForTopicList = new ArrayList<WEBResult>();
	static List<KeyWord> SearchList = new ArrayList<KeyWord>();

	// 防止两个同类job同时执行 myjob1.setRule(Schedule_RULE);
	// myjob2.setRule(Schedule_RULE);
	private static ISchedulingRule Schedule_RULE = new ISchedulingRule() {
		public boolean contains(ISchedulingRule rule) {
			return this.equals(rule);
		}

		public boolean isConflicting(ISchedulingRule rule) {
			return this.equals(rule);
		}
	};

	// private void perform(){
	// Job job = new Job("jobname获取数据"){
	// protected IStatus run(IProgressMonitor monitor){
	// // 在此添加获取数据的代码
	// Display.getDefault().asyncExec(new Runnable(){
	// public void run(){
	// // 在此添加更新界面的代码
	// }
	// });
	// return Status.OK_STATUS;
	// }
	// };
	// job.setRule(Schedule_RULE);
	// job.schedule();
	// }

	private static void dosearch(String searchtxt) {

		String tagNameforsearch = "";

		startTimestamp = new Timestamp(System.currentTimeMillis());
		preTimePoint = startTimestamp;
		queryRecsfordatabase.setStarttime(startTimestamp);

		String tempsearchtxt = searchtxt;

		for (int i = SearchList.size() - 1; i >= 0; i--) {
			if (tempsearchtxt.toLowerCase().contains(
					SearchList.get(i).getKeywordName().toLowerCase().trim())) {
				SearchList.remove(i);
			}

		}

		List<Integer> collectcount = new ArrayList<Integer>();
		collectcount.add(0);
		collectcount.add(0);
		collectcount.add(0);
		collectcount.add(0);
		// 0 exceptioncount=0;
		// 1 errorcount=0;
		// 2 apicount=0;
		// 3 othercount=0;

		for (int i = 0; i < SearchList.size(); i++) {
			String simpleStr = SearchList.get(i).getKeywordName();
			tagNameforsearch = SearchList.get(i).getTagName();

			if (tagNameforsearch.contains("exception")) {
				collectcount.set(0, collectcount.get(0) + 1);
				break;
			} else if (tagNameforsearch.contains("api")) {
				collectcount.set(0, collectcount.get(0) + 1);
				break;
			} else if (tagNameforsearch.contains("error")) {
				collectcount.set(0, collectcount.get(0) + 1);
				break;
			} else if (tagNameforsearch.contains("other")) {
				collectcount.set(0, collectcount.get(0) + 1);
				break;
			} else {
				collectcount.set(3, collectcount.get(3) + 1);

			}

		}

		int countindex = 0;
		int max = collectcount.get(0);
		for (int i = 1; i < collectcount.size(); i++) {
			if (collectcount.get(i) > max) {
				max = collectcount.get(i);
				countindex = i;
			}
		}

		tagNameforsearch = " ";// other
		if (countindex == 0)
			tagNameforsearch = "exception";
		else if (countindex == 1)
			tagNameforsearch = "api example";
		else if (countindex == 2)
			tagNameforsearch = "error";
		else if (countindex == 3)
			tagNameforsearch = " ";// other

		// final String search=(searchtxt +" "+ tagNameforsearch).trim();
		final String search = (searchtxt).trim();

		// ========================== 记录检索信息数据库

		// ??nqr.setQuerywords(querywords);
		// ??nqr.setSelectFromFoamtreeWords(selectFromFoamtreeWords);
		// ??nqr.setTime(time);

		String tempsearch = search;
		queryRecsfordatabase.setSelectFromFoamtreeWords(SearchList);
		for (int i = 0; i < SearchList.size(); i++) {
			if (tempsearch.contains(CommUtil.getNewSimpleWords(
					SearchList.get(i).getKeywordName()).trim())) {
				tempsearch.replace(
						CommUtil.getNewSimpleWords(
								SearchList.get(i).getKeywordName()).trim(), "")
						.trim();
			}
		}

		String[] inputString = tempsearch.split("[ ]");
		List<KeyWord> inputkeyKeyWords = new ArrayList<KeyWord>();

		for (String keys : inputString) {

			KeyWord kWord = new KeyWord();
			kWord.setKeywordName(keys);
			kWord.setTagName("manual");
			inputkeyKeyWords.add(kWord);
		}
		queryRecsfordatabase.setInputWords(inputkeyKeyWords);

		List<KeyWord> inFoamtreeKeyWords = queryRecsfordatabase
				.getFoamtreeWords();
		List<KeyWord> apiKeyWordsinQuery = new ArrayList<KeyWord>();
		List<KeyWord> errorKeyWordsinQuery = new ArrayList<KeyWord>();
		List<KeyWord> exceptionKeyWordsinQuery = new ArrayList<KeyWord>();
		List<KeyWord> otherKeyWordsinQuery = new ArrayList<KeyWord>();
		for (int i = 0; i < inFoamtreeKeyWords.size(); i++) {
			if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase()
					.contains("api")) {
				apiKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
			}
			if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase()
					.contains("error")) {
				errorKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
			}
			if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase()
					.contains("exception")) {
				exceptionKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
			}
			if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase()
					.contains("other")) {
				otherKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
			}
		}

		queryRecsfordatabase.setApiKeyWordsinQuery(apiKeyWordsinQuery);

		queryRecsfordatabase.setErrorKeyWordsinQuery(errorKeyWordsinQuery);

		queryRecsfordatabase
				.setExceptionKeyWordsinQuery(exceptionKeyWordsinQuery);

		queryRecsfordatabase.setOtherKeyWordsinQuery(otherKeyWordsinQuery);

		for (int i = allpageslist.size(); i > 0; i--) {
			allpageslist.remove(i - 1);
		}

		// 多任务实施检索

		Job job = new Job("GetDatafromGoogle") {
			protected IStatus run(IProgressMonitor monitor) {
				// 在此添加获取数据的代码
				Timestamp starttime;
				String searchResultOutput = "\n============\n";
				starttime = new Timestamp(System.currentTimeMillis());

				// IPreferenceStore
				// ps=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
				// String cse_key=ps.getString(PreferenceConstants.CSE_KEY);
				// String cse_cx=ps.getString(PreferenceConstants.CSE_CX);

				String cse_key;
				String cse_cx;
				// 在此处从14个定制引擎中随机选择一个
				int temp = CommUtil.randomInt(
						CommUtil.getKeyCxList().size() - 1, 0);
				cse_key = CommUtil.getKeyCxList().get(temp).getKey();
				cse_cx = CommUtil.getKeyCxList().get(temp).getCx();

				if (searchEngineSelect == 1) {
					// CxKeyPair ckp11 = new CxKeyPair(
					// "AIzaSyCEw3qtyVRYJ9QlhKgQN2FMwRa4N6zxt78",
					// );
					// FDUHelpseeking9
					// 1标示 websiteList.add("Technical Blogs (TB)");
					// 2表示 websiteList.add("Code Examples (CE)");
					// 3标示 websiteList.add("Discussion Forum (DF)");
					// 4标示 websiteList.add("Q&A web site (QA)");
					cse_key = "AIzaSyCEw3qtyVRYJ9QlhKgQN2FMwRa4N6zxt78";
					cse_cx = "005635559766885752621:fqacawephkk";

				}

				if (searchEngineSelect == 2) {
					// CxKeyPair ckp12 = new CxKeyPair(
					// "AIzaSyARXdH3_gBhG3zg3mmCp6NH-RLCqs9w_h8",
					// "005635559766885752621:rjqlrd92ema");
					// FDUHelpseeking10
					// 1标示 websiteList.add("Technical Blogs (TB)");
					// 2表示 websiteList.add("Code Examples (CE)");
					// 3标示 websiteList.add("Discussion Forum (DF)");
					// 4标示 websiteList.add("Q&A web site (QA)");
					cse_key = "AIzaSyARXdH3_gBhG3zg3mmCp6NH-RLCqs9w_h8";
					cse_cx = "005635559766885752621:rjqlrd92ema";
					// FDUHelpseeking10;

				}

				if (searchEngineSelect == 3) {
					// CxKeyPair ckp13 = new CxKeyPair(
					// "AIzaSyA2aCykyQf1nZP2ZlA54Nrswliy0kThJ5w",
					// "005635559766885752621:mkhzdvvqmdc");
					// FDUHelpseeking11

					// 1标示 websiteList.add("Technical Blogs (TB)");
					// 2表示 websiteList.add("Code Examples (CE)");
					// 3标示 websiteList.add("Discussion Forum (DF)");
					// 4标示 websiteList.add("Q&A web site (QA)");
					cse_key = "AIzaSyA2aCykyQf1nZP2ZlA54Nrswliy0kThJ5w";
					cse_cx = "005635559766885752621:mkhzdvvqmdc";

				}

				if (searchEngineSelect == 4) {
					// CxKeyPair ckp14 = new CxKeyPair(
					// "AIzaSyAlOeQI4kAlSHvxGpCA7oj9r4ZCzCAsXho",
					// "005635559766885752621:qurwj2b9mrw");
					// FDUHelpseeking12

					// 1标示 websiteList.add("Technical Blogs (TB)");
					// 2表示 websiteList.add("Code Examples (CE)");
					// 3标示 websiteList.add("Discussion Forum (DF)");
					// 4标示 websiteList.add("Q&A web site (QA)");
					cse_key = "AIzaSyAlOeQI4kAlSHvxGpCA7oj9r4ZCzCAsXho";
					cse_cx = "005635559766885752621:qurwj2b9mrw";

				}

				LoopGoogleAPICall apiCall = new LoopGoogleAPICall(cse_key,
						cse_cx, search);

				apiCall.start();
				try {
					apiCall.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				googlesearchList = apiCall.getCurrentResults();

				if (googlesearchList.size() > 0) {
					// 清除tree tree.removeAll();
					// 2014.10.15

					// TODO BUG
					for (int i = resultsForTopicList.size() - 1; i >= 0; i--) {
						resultsForTopicList.remove(i);
					}
					// end of 2014.10.15
					List<String> foamtreestrlist = CommUtil.stringToList(
							currentFoamtreeString.trim(), "[ ]");

					int indexResultslist = 0;
					for (WEBResult webResult : googlesearchList) {
						String titleNoFormating = webResult.getTitle();
						titleNoFormating = titleNoFormating.replaceAll(
								"&quot;", "\"");
						titleNoFormating.replaceAll("&#39;", "\'");
						titleNoFormating.replaceAll("<b>", " ");
						titleNoFormating.replaceAll("</", " ");
						titleNoFormating.replaceAll("b>", " ");

						searchResultOutput = searchResultOutput + "\n"
								+ webResult.toString();

						// 2014.10.15
						webResult.setTitle(titleNoFormating);

						WEBResult forTopicPrepareItem = new WEBResult();
						forTopicPrepareItem.setSummary(webResult.getSummary());
						forTopicPrepareItem.setUrl(webResult.getUrl());
						forTopicPrepareItem.setTitle(webResult.getTitle());

						resultsForTopicList.add(forTopicPrepareItem);

						WEBPageBean webpb = new WEBPageBean();
						webpb.setSummary(webResult.getSummary());
						webpb.setContent(webResult.getSummary());
						webpb.setTitle(webResult.getTitle());
						webpb.setUrl(webResult.getUrl());
						webpb.setSelect(false);

						for (int i1 = 0; i1 < foamtreestrlist.size(); i1++) {
														
							
//					System.out.println("code split:# "+CommUtil.getTokensfromCodeStr(foamtreestrlist.get(i1)));
							System.out.println("origin words:#  "+foamtreestrlist.get(i1));
						System.out.println("new simple words:#  "+CommUtil.getNewSimpleWords(foamtreestrlist.get(i1)));
//							System.out.println("simple words:#  "+CommUtil.getSimpleWords(foamtreestrlist.get(i1)));
							
							String tempstr=CommUtil.getNewSimpleWords(foamtreestrlist.get(i1));
							if (tempstr.contains("(")) {
								tempstr=tempstr.replace("(", " ");
							}
							if (tempstr.contains(")")) {
								tempstr=tempstr.replace(")", " ");
							}
							if (tempstr.contains(".")) {
								tempstr=tempstr.replace(".", " ");
							}
							
							List<String> tempstrList=CommUtil.stringToList(tempstr, " ", 2);
			boolean flag=false;				

			for (int i = 0; i < tempstrList.size(); i++) {
				String titleString=webpb
						.getTitle()
						.toLowerCase()
						.trim();
				
				if (titleString.contains("(")) {
					titleString=titleString.replace("(", " ");
				}
				if (titleString.contains(")")) {
					titleString=titleString.replace(")", " ");
				}
				if (titleString.contains(".")) {
					titleString=titleString.replace(".", " ");
				}
				if (titleString.contains(";")) {
					titleString=titleString.replace(";", " ");
				}
				if (titleString.contains(",")) {
					titleString=titleString.replace(",", " ");
				}

				String summaryString=webpb
						.getSummary()
						.toLowerCase()
						.trim();
				
				
				if (summaryString.contains("(")) {
					summaryString=summaryString.replace("(", " ");
				}
				if (summaryString.contains(")")) {
					summaryString=summaryString.replace(")", " ");
				}
				if (summaryString.contains(".")) {
					summaryString=summaryString.replace(".", " ");
				}
				if (summaryString.contains(";")) {
					summaryString=summaryString.replace(";", " ");
				}
				if (summaryString.contains(",")) {
					summaryString=summaryString.replace(",", " ");
				}


				
				
				if (titleString
						.contains(
								tempstrList.get(i)
										.toLowerCase().trim())) {
					if (webpb.getContainsStr().equals("----")) {
						webpb.setContainsStr("");
					}
					
					String labels = webpb.getContainsStr();
					if (!labels.toLowerCase().trim().contains(foamtreestrlist.get(i1).trim())) {
						labels=(labels+"  #   "+foamtreestrlist.get(i1)).trim();
					}

					webpb.setContainsStr(labels.trim());
					flag=true;
//					break;
				} 
				

					if (summaryString
							.contains(
									tempstrList.get(i)
											.toLowerCase().trim())) {
						if (webpb.getContainsStr().equals("----")) {
							webpb.setContainsStr("");
						}
						String labels = webpb.getContainsStr();
						if (!labels.toLowerCase().trim().contains(foamtreestrlist.get(i1).trim())) {
							labels=(labels+"  #   "+foamtreestrlist.get(i1)).trim();
						}
							

						webpb.setContainsStr(labels.trim());
						flag=true;
//						break;
					}

				
			}

//			if (flag) {
//				break;
//			}

		}

						allpageslist.add(webpb);

						// end of 2014.10.15

		} // end foreach googlesearchlist

					Timestamp endtime = new Timestamp(
							System.currentTimeMillis());

					// 生成选择和自己输入的关键词并记录数据库

				} // if googlesearchlist.size > 0
				else {
					System.out.println("No return results!");
					browserpart = FDUHelpSeekingPlugin
							.getDefault()
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.findView(
									"cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");

					if ((browserpart instanceof HelpSeekingInteractiveView)) {
						HelpSeekingInteractiveView bv = (HelpSeekingInteractiveView) browserpart;
						MessageDialog
								.openInformation(
										bv.getSite().getShell(),
										"Search google faild! ",
										"Sorry connection wrong or no results! Please waiting for a while search again or new keywords!");

					}

				}

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {

						browserpart = FDUHelpSeekingPlugin
								.getDefault()
								.getWorkbench()
								.getActiveWorkbenchWindow()
								.getActivePage()
								.findView(
										"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

						if (browserpart instanceof HelpSeekingBrowserView)
							try {
								PlatformUI
										.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.showView(
												"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
								HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;

								bv.doGenUrlTree("", allpageslist);

							} catch (PartInitException e1) {
								// TODO Auto-generated catch block
								System.out
										.println("sorry ! please show view HelpSeekingBrowserView.");
								;
							}

						genTopicTree();

						// end of 2014.10.15
						// 在此添加更新界面的代码

						// 在此添加更新界面的代码
						// 2014.10.15
						// 调用topic API 生成topictree
						searchEngineSelect = 0;

						for (int i = SearchList.size() - 1; i >= 0; i--) {
							SearchList.remove(i);
						}

						DatabaseUtil.addNewQueryRec(queryRecsfordatabase);

					}

				});
				return Status.OK_STATUS;
			}

		};

		job.setRule(Schedule_RULE);
		job.schedule();

	} // end dosearch

	public static int searchEngineSelect = 0;

	// 0标示不选
	// 1标示 websiteList.add("Technical Blogs (TB)");
	// 2表示 websiteList.add("Code Examples (CE)");
	// 3标示 websiteList.add("Discussion Forum (DF)");
	// 4标示 websiteList.add("Q&A web site (QA)");
	//

	public static int getSearchEngineSelect() {
		return searchEngineSelect;
	}

	public static void setSearchEngineSelect(int searchEngineSelect) {
		HelpSeekingInteractiveView.searchEngineSelect = searchEngineSelect;
	}

	public static void genTopicTree() {

		List<Cluster> clusters = CarrotTopic.fromWebResults(Collections
				.unmodifiableList(resultsForTopicList));

		// ???? 测试试用句子 记得删除！！！

		// List<WEBResult> results = SampleWebResults.WEB_RESULTS;
		// if (clusters.isEmpty()) {
		// clusters = CarrotTopic.fromWebResults(results);
		// System.out.println("clusters is null");
		// }

		//
		// 生成topic tree
		// 只考虑第一层， level=0时 topic的数量 每个topic下面的子topic暂时不给出； cluster中已有，需要时可以去取
		topictree.removeAll();
		// urlTree.removeAll();

		// 重新生成新检索，新topics 因而需要移除allwebpages
		for (int i = allWebPages.size(); i > 0; i--) {
			allWebPages.remove(i - 1);

		}

		// 移除topicid信息
		for (int i = currentTopicInfoRecs.size(); i > 0; i--) {
			currentTopicInfoRecs.remove(i - 1);
		}

		TreeItem topicsumm = new TreeItem(topictree, SWT.NONE);
		topicsumm.setData("TOPIC_SUMMARY");
		topicsumm.setChecked(false);

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

			// 写入数据库

			NewTopicInfoRec ntif = new NewTopicInfoRec();
			ntif.setTopicName(topicitem.getText().trim());
			ntif.setSearchId(getCurrentSearchID());
			ntif.setURLcount(c.size());
			// ntif.setClickTopicTime(new
			// Timestamp(System.currentTimeMillis()));
			// ntif.setTopicId(topicId);
			DatabaseUtil.addNewTopicInfoRectoDatabase(ntif);

			List<NewTopicWebPagesInfo> ntwpiList = new ArrayList<NewTopicWebPagesInfo>();
			String topicId = DatabaseUtil.getNewTopicInfoRecTopicId();
			NewTopicInfoRec nifforlist = new NewTopicInfoRec();
			nifforlist.setTopicId(topicId);
			nifforlist.setTopicName(topicitem.getText().trim());
			currentTopicInfoRecs.add(nifforlist);

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

				NewTopicWebPagesInfo ntwpi = new NewTopicWebPagesInfo();
				ntwpi.setTopicId(topicId);
				ntwpi.setWebTitle(doc.getTitle());
				ntwpi.setWebSummary(doc.getSummary());
				ntwpi.setWebURL(doc.getContentUrl());

				ntwpiList.add(ntwpi);
			}

			DatabaseUtil.addNewTopicWEbPagesInfotoDatabase(ntwpiList);

		}

		// topicitem.setExpanded(true);
		topicsumm.setText("All topics ( Topics: " + clusters.size() + " URLs: "
				+ totalurlnum + ")");
		topicsumm.setExpanded(true);

	}

	static List<NewTopicInfoRec> currentTopicInfoRecs = new ArrayList<NewTopicInfoRec>();// 记录话题的编号

	static String currentFoamtreeString = "";

	private static List<KeyWord> currentSearchKeyWords201411 = new ArrayList<KeyWord>();
	private static int currentQueryID = 0;

	public void setNewWordsAndMode(List<KeyWord> snapShotAllKeyWords,
			List<KeyWord> keyWordsforQuery, int mode) {

		for (int i = currentSearchKeyWords201411.size() - 1; i >= 0; i--) {
			currentSearchKeyWords201411.remove(i);
		}

		// 处理异常字符 如“；”等，并截短

		// 试着处理给出的词汇截短符号：
		// java.io.xxx java.io.xx.yy(zzz)
		for (int i = 0; i < keyWordsforQuery.size(); i++) {

			currentSearchKeyWords201411.add(keyWordsforQuery.get(i));

		}

		currentFoamtreeString = "";
		String searchwords = "";
		// String currentWord="";
		String labelWeight = "";
		for (int i = 0; i < currentSearchKeyWords201411.size(); i++) {

			// ???? 因为为了最大限度显示，已经将串中的包信息去除，只留下了最后的类名和方法名。因此不适用替换包分隔符“.”

			String labels = "";

			labels = currentSearchKeyWords201411.get(i).getKeywordName();
			labels = CommUtil.getNewSimpleWords(labels);

			currentFoamtreeString = currentFoamtreeString + " " + labels;

			if (labels.contains(".")) {
				labels = labels.replaceAll("[.]", ". ");
			}

			if (labels.contains("(")) {
				labels = labels.replaceAll("[(]", " (");
			}

			labelWeight = labelWeight
					+ "{ id: \""
					+ i
					+ "\" , label: \""
					// + keyWordsforQuery.get(i).getKeywordName() +
					// "\", weight: "
					+ labels + "\", weight: "
					+ Math.log10(currentSearchKeyWords201411.get(i).getScore())
					+ " ,type: \"leaf\"},";

			searchwords = searchwords + " "
					+ currentSearchKeyWords201411.get(i).getKeywordName();
			System.out.println("candidate keyword No." + i + " : "
					+ currentSearchKeyWords201411.get(i).getKeywordName());

		}

		String foamTreeContent = "dataObject: {" + "groups: [" + labelWeight
				+ "]" + "}";

		int width = 300;
		int height = 200;

		dOIViewPart = FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingDOIModelView");
		if (dOIViewPart == null)
			try {
				FDUHelpSeekingPlugin
						.getDefault()
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(
								"cn.edu.fudan.se.helpseeking.views.HelpSeekingDOIModelView");
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		if ((dOIViewPart instanceof HelpSeekingDOIModelView)) {
			HelpSeekingDOIModelView bv = (HelpSeekingDOIModelView) dOIViewPart;

			width = bv.getFoamtreeBrowser().getBounds().width;
			height = bv.getFoamtreeBrowser().getBounds().height;

			System.out.println("width & height:" + width + ":" + height);

			// 生成网页
			genFoamTree(width, height, foamTreeFileNamePath, foamTreeContent,
					"HelloHongwei"); // Cache.getInstance().getCurrentBrowserTitle();

			// 装载网页
			bv.getFoamtreeBrowser().setUrl(foamTreeFileNamePath);

		}

		// 记录数据准备存盘到数据库

		// queryRecsfordatabase.setApiKeyWordsinQuery(apiKeyWordsinQuery);
		//
		// queryRecsfordatabase.setErrorKeyWordsinQuery(errorKeyWordsinQuery);
		//
		// queryRecsfordatabase.setExceptionKeyWordsinQuery(exceptionKeyWordsinQuery);
		//
		// queryRecsfordatabase.setOtherKeyWordsinQuery(otherKeyWordsinQuery);

		queryRecsfordatabase.setFoamtreeWords(keyWordsforQuery);

		queryRecsfordatabase.setPretimepoint(preTimePoint);

		queryRecsfordatabase.setQueryId(getCurrentSearchID());

		// queryRecsfordatabase.setInputWords(inputWords);
		// queryRecsfordatabase.setSelectFromFoamtreeWords();

		queryRecsfordatabase.setSnapshotWords(snapShotAllKeyWords);
		// queryRecsfordatabase.setStarttime(starttime);
		// queryRecsfordatabase.setUser(user);

	}

	static Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
	static Timestamp preTimePoint = new Timestamp(System.currentTimeMillis());

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void doFoamtreeClick(String title, int width, int height) {
		// System.out.println("select group label is : " + e.title);
		if (title.equals("foamtreetest.html")) {
			return;

		}
		if (title.equals("")) {
			return;
		}
		// 新增是否为数字字符
		boolean bl = true; // 存放是否全为数字
		char[] c = title.toCharArray(); // 把输入的字符串转成字符数组
		for (int i = 0; i < c.length; i++) {
			if (!Character.isDigit(c[i])) { // 判断是否为数字
				bl = false;
				break;
			}
		}
		if (!bl) {
			return;
		}

		if (!title.toLowerCase().equals("HelloHongwei".toLowerCase())) {

			boolean isuseword = true;
			for (int i = 0; i < SearchList.size(); i++) {
				if (SearchList
						.get(i)
						.getKeywordName()
						.trim()
						.toLowerCase()
						.equals(currentSearchKeyWords201411
								.get(Integer.valueOf(title)).getKeywordName()
								.trim().toLowerCase())) {
					isuseword = false;
					SearchList.remove(i);
					break;
				}

			}

			if (isuseword) {

				SearchList.add(currentSearchKeyWords201411.get(Integer
						.valueOf(title)));
			}

		}

		String searchtext = "";
		for (int i = 0; i < SearchList.size(); i++) {
			searchtext = searchtext.trim()
					+ " "
					+ CommUtil.getNewSimpleWords(SearchList.get(i)
							.getKeywordName());
		}

		txtSearch.setText(searchtext);
	}

}
