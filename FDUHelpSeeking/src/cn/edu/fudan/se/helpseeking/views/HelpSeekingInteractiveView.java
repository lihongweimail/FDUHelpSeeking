package cn.edu.fudan.se.helpseeking.views;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
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
	public static AmAssitBrowser myBrowser;
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
	private Browser browser;
	Composite foamtreeComposite;
	String foamTreeFileNamePath = CommUtil.getFDUPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";

	@Override
	public void createPartControl(Composite arg0) {

		arg0.setLayout(new FillLayout());

		sashComposite = new SashForm(arg0, SWT.VERTICAL);

		
		
		
		
//		foamtreeComposite=new Composite(sashComposite, SWT.NONE);
//		foamtreeComposite.setLayout(new FillLayout());

		// 生成foamtree需要的js脚本，并将它们和显示内容文件foamtreetest.html放在一个目录下。
		// TODO: 考虑是否生成一次？
		initFoamTreeEnv(CommUtil.getFDUPluginWorkingPath());


		browser = new Browser(sashComposite, SWT.BORDER);
		
       
		String foamTreeContent = ""; // 使用工具生成foamtree的内容
		genFoamTree(200, 200, foamTreeFileNamePath, foamTreeContent,"HelloHongwei");

		

		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);
				System.out.println("select group label is : " + e.title);
				String searchtext = txtSearch.getText();
if(!e.title.equals("HelloHongwei"))
{ if (!CommUtil.compareString(Cache.getInstance()
						.getCurrentBrowserTitle(), e.title)) {

	
					String candidateWord = (e.title).replace(" ", ".");
					String temp2 = "";
					boolean flag = true;
					for (String str : searchtext.split("[ ]")) {
						if (!str.equals(candidateWord)) {
							if (temp2.equals("")) {
								temp2 = str;
							} else {
								temp2 = temp2 + " " + str;
							}
						} else {
							flag = false;
						}

					}

					if (flag) {
						if (temp2.equals("")) {
							temp2 = candidateWord;

						} else {
							temp2 = temp2 + " " + candidateWord;
						}
					}

					txtSearch.setText(temp2);

				}
			}
			}
		});

		browser.setUrl(foamTreeFileNamePath);
		browser.refresh();

		// ============

		
		SashForm topicSashForm=new SashForm(sashComposite, SWT.VERTICAL);
		
		// sashComposit 水平分割处
		Composite SearchComposite = new Composite(topicSashForm, SWT.NONE);
		// SearchComposite.setLayoutData(BorderLayout.CENTER);
		SearchComposite.setLayout(new GridLayout(2, false));

		txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL
				| SWT.MULTI);
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
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
		btnSearchGoogle.setText("Search");
		btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				manualSearch();
				//在这里放雪娇的  LDA的处理。 （）

			}
		});

		// SashForm topicComposite = new SashForm(SearchComposite,
		// SWT.VERTICAL);
		// topicComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 2, 1));
		
		
		
Composite  topicCom = new Composite(topicSashForm, SWT.NONE);
topicCom.setLayout(new GridLayout(2, false));

		// topic list
		topictree = new Tree(topicCom,  SWT.BORDER | SWT.CENTER);
		
		
		topictree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		topictree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		topicSashForm.setWeights(new int[] {100, 300});
		
		
		topictree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				System.out.println("click:" + item.getData().toString());

				// if (item.getData()=="TOPIC")
				// {
				// tagcloudpart = FDUHelpSeekingPlugin
				// .getDefault()
				// .getWorkbench()
				// .getActiveWorkbenchWindow()
				// .getActivePage()
				// .findView(
				// "cn.edu.fudan.se.helpseeking.views.HelpSeekingTagCloundView");
				//
				// if ((tagcloudpart instanceof HelpSeekingTagCloundView)) {
				//
				// String content= "<!DOCTYPE html> <html>  "
				// + "<head> "
				// + "<title> Test interact word </title>"
				// + " </head>"
				// + "<body>"
				// + " <p>  "+ item.getText().toString() + " </p>"
				// + " </body>"
				// + "</html>";
				//
				// String fileurl=CommUtil.getPluginCurrentPath()+"Tag.html";
				// FileHelper.createFile(fileurl, content);
				//
				// HelpSeekingTagCloundView tcv = (HelpSeekingTagCloundView)
				// tagcloudpart;
				// tcv.getMyBrowser().setNewUrl(fileurl);
				// //bv.getMyBrowser().getMyComposite().pack();
				// }
				//
				// }

				// TODO 选择一个topic 给出一组list 得到 2014.10

				// urlTree.removeAll();

				if (item.getData() == "TOPIC") {
					// 将topic的所有数据传到browser窗口

					TreeItem[] sendoutURLlist = item.getItems();

					browserpart = FDUHelpSeekingPlugin
							.getDefault()
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.findView(
									"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

					if ((browserpart instanceof HelpSeekingBrowserView)) {
						HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;
						bv.getTopicContentText().setText(item.getText());
						bv.genUrlTree(sendoutURLlist);
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		sashComposite.setWeights(new int[] { 200, 400 });

	}

	public void genFoamTree(int width, int height, String foamtreeFileNamePath,
			String foamTreeContent, String title) {

		if (foamTreeContent.equals(""))
		{
		foamTreeContent = "dataObject: {" + "groups: ["
		+ "{ label: \"Welcome\", weight: 2.0 },"
		+ "{ label: \"HelpSeeking\", weight: 4.0 },"
		+ "{ label: \"To\", weight: 0.5},"
		+ "{ label: \"Plugin\", weight: 3.0 },"
		+ "{ label: \"tool\", weight: 1.0 }" + "]" + "}";

		title="HelloHongwei";
		}
		
		//File htmlFile = new File(foamtreeFileNamePath); // CommUtil.getPluginCurrentPath()+"/foamtreetest.html"
		String foamtreehtmlcontent = "<!DOCTYPE html>" + "<html>" + "<head>"
				+ "<title>"
				+ title
				+ "</title>"
				+ "<meta charset=\"utf-8\" />"
				+ "</head>"
				+ "<body>"
				+ "<div id=\"visualization\" style=\"width: "
				+ String.valueOf(width<100?200:width)
				+ "px; height: "
				+ String.valueOf(height<100?200:height)
				+ "px\"></div>"
				+ "<script src=\"carrotsearch.foamtree.js\"></script>"
				+ "<script language=\"javascript\">"
				+ " window.addEventListener(\"load\", function() {"
				+ "var foamtree = new CarrotSearchFoamTree({"
				+ "id: \"visualization\""
				+ ","
				+ foamTreeContent
				+ ","
				+ "onGroupDoubleClick: function(event) { "
				+ "window.document.title=event.group.label;"
				+ "}"
				+ ","
				+ "onGroupClick: function (event) {"
				+ "window.document.title=event.group.label;"
				+ "}"
				+ "});"
				+ "});" + "</script>" + "</body>" + "</html>";

		FileHelper.writeNewFile(foamtreeFileNamePath, foamtreehtmlcontent);
	}

	public void initFoamTreeEnv(String foamtreeFilesPath) {
		Resource foamtreeJsResource = new Resource();
		String foamtreejscontent;
		System.out.println("path getfducurrentpath()  in initfoamtreeEnv(): "
				+ foamtreeFilesPath);
		System.out
				.println("path getplugincurrentpath()  in initfoamtreeEnv(): "
						+ CommUtil.getFDUPluginWorkingPath());

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.asserts.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.asserts.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath + "/carrotsearch.foamtree.js",
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
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.treemodel.js", foamtreejscontent);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

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
				String titleNoFormating = webResult.getTitleNoFormatting();
				titleNoFormating = titleNoFormating.replaceAll("&quot;", "\"");
				titleNoFormating.replaceAll("&#39;", "\'");
				titleNoFormating.replaceAll("<b>", " ");
				titleNoFormating.replaceAll("</", " ");
				titleNoFormating.replaceAll("b>", " ");

				searchResultOutput = searchResultOutput + "\n"
						+ webResult.toString();

				// 2014.10.15
				webResult.setTitleNoFormatting(titleNoFormating);
				WEBResult forTopicPrepareItem = new WEBResult();
				forTopicPrepareItem.setContent(webResult.getContent());
				forTopicPrepareItem.setUrl(webResult.getUrl());
				forTopicPrepareItem.setTitle(webResult.getTitleNoFormatting());
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

		// 测试试用句子 记得删除！！！

		List<WEBResult> results = SampleWebResults.WEB_RESULTS;
		if (clusters.isEmpty()) {
			clusters = CarrotTopic.fromWebResults(results);
			System.out.println("clusters is null");
		}

		//
		// 生成topic tree
		// 只考虑第一层， level=0时 topic的数量 每个topic下面的子topic暂时不给出； cluster中已有，需要时可以去取
		topictree.removeAll();
		// urlTree.removeAll();

		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);

			TreeItem topicitem = new TreeItem(topictree, SWT.NONE);
			topicitem.setText(c.getLabel());
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

			// topicitem.setExpanded(true);

		}
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

	
	
	
	public void setNewWordsAndMode(List<KeyWord> keyWordsforQuery, int mode) {
		// 生成foamtree的候选词和权重字符串
		// + "{ label: \"Welcome\", weight: 2.0 },"
		// + "{ label: \"HelpSeeking\", weight: 4.0 },"
		// + "{ label: \"To\", weight: 1.0},"
		// + "{ label: \"Plugin\", weight: 2.0 },"
		// + "{ label: \"tool\", weight: 1.0 }"
		
		

		String searchwords = "";
		// String currentWord="";
		String labelWeight = "";
		for (int i = 0; i < keyWordsforQuery.size(); i++) {
			
			String labels=keyWordsforQuery.get(i).getKeywordName().replace(".", " ");
			
			labelWeight = labelWeight + "{ label: \""
					//+ keyWordsforQuery.get(i).getKeywordName() + "\", weight: "
					+ labels+ "\", weight: "
					+ keyWordsforQuery.get(i).getScore() + " },";
			searchwords = searchwords + " "
					+ keyWordsforQuery.get(i).getKeywordName();
			System.out.println("candidate keyword No."+i+" : "+keyWordsforQuery.get(i).getKeywordName());

		}

		String foamTreeContent = "dataObject: {" + "groups: [" + labelWeight
				+ "]" + "}";

        int width=browser.getBounds().width;
        int height=browser.getBounds().height;
        System.out.println("width & height:"+width+":"+height);

		
         //生成网页
		genFoamTree(width, height, foamTreeFileNamePath, foamTreeContent,Cache.getInstance().getCurrentBrowserTitle());
		
		//装载网页
		browser.setUrl(foamTreeFileNamePath);
		

		// mode=1时，不自动查询， mode=2时自动查询

		txtSearch.setText(searchwords);

		if (mode == 2) {
			try {
				dosearch(searchwords);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
