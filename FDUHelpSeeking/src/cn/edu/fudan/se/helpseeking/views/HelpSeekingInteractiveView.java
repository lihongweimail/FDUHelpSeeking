package cn.edu.fudan.se.helpseeking.views;

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
    public static final String browserviewID="cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView";
	
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
	// private static IViewPart tagcloudpart;
	private static IViewPart browserpart;

	private static List<KeyWord> currentSearchWords = new ArrayList<>();
	private static List<KeyWord> sendfromselectSearchWords = new ArrayList<>();

	private static int currentActionID = 0;
	private static String currentSearchID = "0";
	private static int searchFlag=0;//大于0就是第二次检索
	

	
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
	private Browser foamtreeBrowser;
	private Browser topicFilterBrowser;

	private String currentTopicName = "";
	// private Browser urlBrowser;

	Composite foamtreeComposite;

	String foamTreeFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	String foamTreeTopicFilterFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/topicfilter.html";
	String searchHTMLPath = CommUtil.getFDUHelpseekingPluginWorkingPath() + "/search.html";

	public static List<TopicWEBPagesBean> allWebPages = new ArrayList<TopicWEBPagesBean>();

	@Override
	public void createPartControl(Composite arg0) {

		arg0.setLayout(new FillLayout());

		sashComposite = new SashForm(arg0, SWT.VERTICAL);

		// foamtreeComposite=new Composite(sashComposite, SWT.NONE);
		// foamtreeComposite.setLayout(new FillLayout());

		
		
		
		
		// 生成foamtree需要的js脚本，并将它们和显示内容文件foamtreetest.html放在一个目录下。
		// TODO: 考虑是否生成一次？
		initFoamTreeEnv(CommUtil.getFDUHelpseekingPluginWorkingPath());

					
	
		
		foamtreeBrowser = new Browser(sashComposite, SWT.BORDER);
		foamtreeBrowser
				.setToolTipText("Double Click to ROLL OUT!  Shift + Double Click to PULL BACK!");

		String foamTreeContent = ""; // 使用工具生成foamtree的内容
		genFoamTree(300, 200, foamTreeFileNamePath, foamTreeContent,
				"HelloHongwei");

		foamtreeBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);

				String title=e.title.trim();
				
				
				
   doFoamtreeClick(title,foamtreeBrowser.getBounds().width,foamtreeBrowser.getBounds().height);
					
				}
			
		});

		foamtreeBrowser.setUrl(foamTreeFileNamePath);
		// browser.refresh();

		if (Basic.mockup==2) {

	foamtreeBrowser.setVisible(false);
		}
		
		
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
		
		
	if (Basic.mockup==2) {
		
		//以下是所需的UI Mockups
		//1. 添加一个“Tweat ranking by API DOIs” checkbox，可以添加到searchbox下方。
	//	2. 添加一个“Select preferred web site category” dropdown list，也可以添加到searchbox下方。
	//	3. 对搜索结果用foamtree中APIEntities标注，不用使用foamtreeAPIEntities相同颜色，
		//只要把网页中所出现的APIEntities排个序，显示出top3or4就可以。比如 xxxx，yyyy，zzzz，more。more代表还有其他，
		// 用户可以click to see more。当然我们不用实际实现，但在UI中应该有。
	//	4. 添加一个我前面email提到的overview view。在这个SNAER paper中不用真正实现这个view的功能，
		//只要有一个view壳子就可以。可以把这个view与foamtree view并列放置，这样就可以show我们有这个view，但不用show view's content。

		//==== 如果不需要highlight webpage content的话，会不会简单一些。
	//	我是这样想的：我们可否用一个小的overview view来提供对网页内容的浏览支持。
	//	比如这个overview view可以在一个treeview列出网页中出现的foamtree中的APIEntities。APIEntities可以按出现次数排序。
	//	每一个APIEntity之下，按照出现位置先后，列出这个APIEntity在网页中出现位置的一个小片段（比如前后5个词）。
	//	这样一个treeview应该也可以对网页内容提供一个很好的总结作用，并且能够支持快速选择网页相关内容。
	//	这样一个overview view可否实现？

		//以上四个Mockups中，1/2/4应该很容易实现。
	//	3会麻烦一些，但我们已有webpage crawling实现以及webpage content分析，3也应该doable for SANER paper。实现3时，不用考虑webpage下载速度问题，只要能show出来就好了。

		Composite  muckuicoComposite=new Composite(sashComposite,SWT.None);
		muckuicoComposite.setLayout(new GridLayout(2, false));
		
		
		Label tweatlabel=new Label(muckuicoComposite, SWT.None);
		tweatlabel.setText("Tweat ranking by API DOIs");
		tweatlabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		
		Button checkbox=new Button(muckuicoComposite, SWT.CHECK);
		checkbox.setToolTipText("Tweat ranking by API DOIs");
		checkbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true, 1, 1));

		
		Label websitelabel = new Label(muckuicoComposite, SWT.None);
		websitelabel.setText("Select preferred web site category");
		websitelabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true, 1, 1));
		
		Combo websiteList = new Combo(muckuicoComposite, SWT.MULTI | SWT.V_SCROLL);
		websiteList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		websiteList.add("Technical Blogs (TB)");
		websiteList.add("Code Examples (CE)");
		websiteList.add("Discussion Forum (DF)");
		websiteList.add("Q&A web site (QA)");
		
		
	}	
	
		

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
	Composite topicCom;
	
	if (Basic.mockup==2) {
		topicCom = new Composite(sashComposite, SWT.NONE);
	}else
		 topicCom = new Composite(topicSashForm, SWT.NONE);
		
		
		
		topicCom.setLayout(new GridLayout(2, false));

		// topic list
		topictree = new Tree(topicCom, SWT.BORDER | SWT.CENTER);

		topictree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		topictree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		
		
				
		topictree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;

			if	(FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage().findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView") instanceof HelpSeekingBrowserView)
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					return;
				}
				
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
				
					//注销
					//genTopicWordsFoamTree(allWebPages.get(currentTopicindex).getMyfudanTopicWords());


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
							if (currentTopicInfoRecs.get(i).equals(currentTopicName)) {
								topicId=currentTopicInfoRecs.get(i).getTopicId();
							}
						}

						
						List<String> containString=CommUtil.stringToList(txtSearch.getText(), "[ ]");
						
						
						
						
						
						
						
						bv.genUrlTree(currentFoamtreeString,currentTopicName,allWebPages.get(currentTopicindex)
								.getPages(),SearchList,currentSearchID,topicId);
						

						NewWebUseInfo nwuiInfo=new NewWebUseInfo();
						
						nwuiInfo.setTopicName(currentTopicName);
						nwuiInfo.setTopicId(topicId);
					
						
						nwuiInfo.setOpenTime(new Timestamp(System.currentTimeMillis()));
						
						DatabaseUtil.addNewWebUseInfo(nwuiInfo);
						
						
					}

					
					
					// sendoutURLlist
				} 
				
				else

				// 检查是否是topic

				{
					if (item.getData() == "TOPIC") {
						
						//注销替代语句
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

						curTopicWEBPages
								.setTopicName(item.getText().trim());
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
							String topicId="";
							
							for (int i = 0; i < currentTopicInfoRecs.size(); i++) {
								if (currentTopicInfoRecs.get(i).equals(currentTopicName)) {
									topicId=currentTopicInfoRecs.get(i).getTopicId();
								}
							}
							
							bv.genUrlTree(currentFoamtreeString, currentTopicName, curTopicWEBPages.getPages(),SearchList,currentSearchID,topicId);
						
							
							
							NewWebUseInfo nwuiInfo=new NewWebUseInfo();
							
							nwuiInfo.setTopicName(currentTopicName);
							nwuiInfo.setTopicId(topicId);
						
							
							nwuiInfo.setOpenTime(new Timestamp(System.currentTimeMillis()));
							
							DatabaseUtil.addNewWebUseInfo(nwuiInfo);

														
							
							
						}

						allWebPages.add(curTopicWEBPages);
						setCurrentTopicItem(item);
						setCurrrentTopicWEBPages(curTopicWEBPages);
						
						
						
						
						
//注销前代码						
/*						//刷新
						String foamTreeTopicFilterContent = "dataObject: {"
								+ "groups: ["
								+ "{ label: \"Plase Waiting\", weight: 6.0 ,type: \"node\" },"
								+ "{ label: \"Full Working\", weight: 3.0 ,type: \"node\"},"
								+ "{ label: \"Friends\", weight: 2.0 ,type: \"node\" },"
									+ "]" + "}"; // 使用工具生成foamtree的内容
						genFoamTree(300, 200, foamTreeTopicFilterFileNamePath,
								foamTreeTopicFilterContent, "HelloHongwei");
						topicFilterBrowser.setUrl(foamTreeTopicFilterFileNamePath);

									// 两个模式同时处理，以提高效率，一方面 可以 就topic有关的URL
						// 先给出；另一方面，用户也可以在二次过滤后的foamtree中选择URL。

						// 新处理模式： 点击topic后，使用雪娇的LDA 提取topic和详细的词， 并生成新的foamtree，
						// 展示

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

						curTopicWEBPages
								.setTopicName(item.getText().trim());
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
							bv.genUrlTree(curTopicWEBPages.getPages(),SearchList);
						}

						allWebPages.add(curTopicWEBPages);

						if (!curTopicWEBPages.getPages().isEmpty()) {
							// 调用LDA
							setCurrentTopicItem(item);
							setCurrrentTopicWEBPages(curTopicWEBPages);
							
							final String currentTopicName=getCurrentTopicItem().getText().trim();
							
										
							 Job job = new Job("ClawWEBAndLDA"){   
								 protected IStatus run(IProgressMonitor monitor){   
								    // 在此添加获取数据的代码   
										List<FudanTopicWithWordsListBean> myfudanTopicWords = LdaGibbsSampling
												.fduTopicURLfilter(currentTopicName, allWebPages);
									
										
										
										getCurrrentTopicWEBPages()
										.setMyfudanTopicWords(myfudanTopicWords);
								
										

									 final List<FudanTopicWithWordsListBean> usefulFTWLB=new ArrayList<FudanTopicWithWordsListBean>();
									 for (int j = 0; j < myfudanTopicWords.size(); j++) {
								
										 
										usefulFTWLB.add(myfudanTopicWords.get(j));
										
									}
									 
								    Display.getDefault().asyncExec(new Runnable(){   
								        public void run(){   
								        // 在此添加更新界面的代码   
								    		// 传给foamtree 并显示
											genTopicWordsFoamTree(usefulFTWLB);
											item.setData("TOPICX");
											item.setForeground(Display.getDefault().getSystemColor(
													SWT.COLOR_GREEN));

								                 
								        }      
								            
								    });  
								    
								   return Status.OK_STATUS;      
								 } 
								 
								 };   
							             job.setRule(Schedule_RULE);
								         job.schedule();  
						}  //注销前代码
	*/

						
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
			

			
		});
		
		
		//注销 filterbrowser
/*
		topicFilterBrowser = new Browser(topicSashForm, SWT.BORDER);
		topicFilterBrowser
				.setToolTipText("Double Click to ROLL OUT!  Shift + Double Click to PULL BACK!");
		topicFilterBrowser.setVisible(false);

*/		
		
    if (Basic.mockup!=2) {
    	topicSashForm.setWeights(new int[] { 50, 300 });

	}
		
		
/*		String foamTreeTopicFilterContent = ""; // 使用工具生成foamtree的内容
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
//					System.out
//							.println("???? 添加 topicFilterBrowser 处理选择的topic 过滤后的关键词，以获得URL列表");

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
						bv.genUrlTree(usefulWebpages,SearchList);
					}

				}
			}

		});

		topicFilterBrowser.setUrl(foamTreeTopicFilterFileNamePath);
		// topicFilterBrowser.refresh();
*/
    
    if (Basic.mockup!=2) {
    	sashComposite.setWeights(new int[] { 200, 400 });
	}
		
		
		
		
		//为中文切词准备数据：  
		//

//		Resource models_arRes = new Resource();
//		models_arRes.getResource("/models/ar.m", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/ar.m");
//
//		Resource models_depRes = new Resource();
//		models_depRes.getResource("/models/dep.m", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/dep.m");
//
//		Resource models_dictambiguityRes = new Resource();
//		models_dictambiguityRes.getResource("/models/dict_ambiguity.txt", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath()
//						+ "/models/dict_ambiguity.txt");
//
//		Resource models_dicdepRes = new Resource();
//		models_dicdepRes.getResource("/models/dict_dep.txt", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/dict_dep.txt");
//
//		Resource models_dictRes = new Resource();
//		models_dictRes.getResource("/models/dict.txt", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/dict.txt");
//
//		Resource models_ExtractPatternRes = new Resource();
//		models_ExtractPatternRes.getResource("/models/ExtractPattern.txt",
//				true, CommUtil.getFDUHelpseekingPluginWorkingPath()
//						+ "/models/ExtractPattern.txt");
//
//		Resource models_posRes = new Resource();
//		models_posRes.getResource("/models/pos.m", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/pos.m");
//
//		Resource models_segRes = new Resource();
//		models_segRes.getResource("/models/seg.m", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/seg.m");
//
//		Resource models_stocktreeRes = new Resource();
//		models_stocktreeRes.getResource("/models/Stock-Tree.txt", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/Stock-Tree.txt");
//
//		Resource models_timeRes = new Resource();
//		models_timeRes.getResource("/models/time.m", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/time.m");
//
//		Resource models_wordgraphRes = new Resource();
//		models_wordgraphRes.getResource("/models/wordgraph.txt", true,
//				CommUtil.getFDUHelpseekingPluginWorkingPath() + "/models/wordgraph.txt");
		
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

	protected void genTopicWordsFoamTree(
			List<FudanTopicWithWordsListBean> myfudanTopicWords) {

		int displaymode=Basic.DISPLAYTOPICFOAMTREEPLAN;
		
		
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
		 String labelWeight="";
		 
			if (displaymode==1)
				{
		 labelWeight="";
       //替换代码一
		 
		 
	//两层展示topic以及topic内的词
		 for (int i = 0; i < myfudanTopicWords.size(); i++) {
				 if (labelWeight.equals("")) {
				 labelWeight=myfudanTopicWords.get(i).genFoamTreeGroupString();
				 }
				 else {
				 labelWeight=labelWeight+", "+myfudanTopicWords.get(i).genFoamTreeGroupString();
				
				 }
				
				 }
				 //结束一
				}
	 
			
			

	if (displaymode==2) {
		
		labelWeight="";
			// 替换代码二
			// 一层展示 将topic 中所有的词 收集在一起，
			List<FudanTopicWordsBean> wordsList = new ArrayList<FudanTopicWordsBean>();
	
			for (int i = 0; i < myfudanTopicWords.size(); i++) {
				for (int j = 0; j < myfudanTopicWords.get(i).getWordsList().size(); j++) {
					String candidateTerm = myfudanTopicWords.get(i).getWordsList()
							.get(j).getWordName().toString().toLowerCase().trim();
					boolean testexists = true;
					int indexSameWord = 0;
					for (int k = 0; k < wordsList.size(); k++) {
						if (candidateTerm.equals(wordsList.get(k).getWordName()
								.toLowerCase().trim())) {
							testexists = false;
							indexSameWord = k;
							break;
						}
					}
	
					if (testexists) {
						wordsList.add(myfudanTopicWords.get(i).getWordsList()
								.get(j));
					} else {
						double wordWeightpre = wordsList.get(indexSameWord)
								.getWordWeight();
						wordsList.get(indexSameWord).setWordWeight(
								wordWeightpre
										+ myfudanTopicWords.get(i).getWordsList()
												.get(j).getWordWeight());
					}
	
				}
	
			}
			// 生成一层展示的代码：
	
					for (int i = 0; i < wordsList.size(); i++) {
				if (labelWeight.equals("")) {
					labelWeight = wordsList.get(i).genFoamTreeObjectString();
				} else {
					labelWeight = labelWeight + ", "
							+ wordsList.get(i).genFoamTreeObjectString();
	
				}
	
			}
	
			// 结束二
	}
		

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
				+ "window.document.title=event.group.id;}\n"
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

	public String currentQueryText="";
	
	
	
	public String getCurrentQueryText() {
		return currentQueryText;
	}

	public void setCurrentQueryText(String currentQueryText) {
		this.currentQueryText = currentQueryText;
	}

	
private static	NewQueryRec queryRecsfordatabase=new NewQueryRec();
	
	
	private void manualSearch() {
		System.out.println("Say start manual search ...");

		topictree.removeAll();
		TreeItem it1=new TreeItem(topictree, SWT.None);
		it1.setText("start searching ...");
		TreeItem it2=new TreeItem(topictree, SWT.None);
		it2.setText("Please waiting...");
		it2.setForeground(Display.getDefault()
						.getSystemColor(SWT.COLOR_RED));
		
		
		
		
		String queryText = txtSearch.getText().trim();

		if (!queryText.equals("")) {

			setSearchFlag(getSearchFlag()+1);
			
			String searchID ;
			if (getSearchFlag()>0) {
				searchID= "P"+getCurrentActionID()+("A"+getSearchFlag());
			}else {
				searchID= "P"+getCurrentActionID();
			}
			

			setCurrentSearchID(searchID);
			
			
           setCurrentQueryText(queryText);
           
           
		
		   dosearch(getCurrentQueryText());
						

											
		}
	}

	static SearchResults sResults;
	static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();
	static List<WEBResult> resultsForTopicList = new ArrayList<WEBResult>();
	static List<KeyWord> SearchList=new ArrayList<KeyWord>();
	
//防止两个同类job同时执行  myjob1.setRule(Schedule_RULE);  myjob2.setRule(Schedule_RULE); 
	private static ISchedulingRule Schedule_RULE = new ISchedulingRule() {   
		public boolean contains(ISchedulingRule rule) {   
		return this.equals(rule);   
		}   
		public boolean isConflicting(ISchedulingRule rule) {   
		return this.equals(rule);   
		}   
		}; 

//		 private void perform(){   
//			 Job job = new Job("jobname获取数据"){   
//			 protected IStatus run(IProgressMonitor monitor){   
//			    // 在此添加获取数据的代码   
//			    Display.getDefault().asyncExec(new Runnable(){   
//			        public void run(){   
//			        // 在此添加更新界面的代码   
//			                 }      
//			             });
//		            return Status.OK_STATUS;
//			         }
//			 };   
//		             job.setRule(Schedule_RULE);
//			         job.schedule();   
//			    }   
		
		
		private static void dosearch( String searchtxt)  {
			
			
	        String tagNameforsearch="";
	        
	        startTimestamp=new Timestamp(System.currentTimeMillis());
	        preTimePoint=startTimestamp;
	        queryRecsfordatabase.setStarttime(startTimestamp);
	        
	        String tempsearchtxt=searchtxt;

	        for (int i = SearchList.size()-1; i >=0; i--) {
				if (tempsearchtxt.contains(SearchList.get(i).getKeywordName().trim())) {
					SearchList.remove(i);
				}
				
			}
	       
	        
	        
	        
	        
	        List<Integer> collectcount=new ArrayList<Integer>();
	        collectcount.add(0);
	        collectcount.add(0);
	        collectcount.add(0);
	        collectcount.add(0);
//	        0 exceptioncount=0;
//	        1 errorcount=0;
//	        2 apicount=0;
//	        3 othercount=0;
	        
	        for (int i = 0; i < SearchList.size(); i++) {
				String simpleStr=SearchList.get(i).getKeywordName();
                   tagNameforsearch=SearchList.get(i).getTagName();
				
                   if (tagNameforsearch.contains("exception")) {
						collectcount.set(0,collectcount.get(0)+1);
						break;
				}else
                   if (tagNameforsearch.contains("api")) {
 						collectcount.set(0,collectcount.get(0)+1);
 						break;
 				}else
                   if (tagNameforsearch.contains("error")) {
 						collectcount.set(0,collectcount.get(0)+1);
 						break;
 				}else
                   if (tagNameforsearch.contains("other")) {
 						collectcount.set(0,collectcount.get(0)+1);
 						break;
 				}else {
 					collectcount.set(3,collectcount.get(3)+1);

					
				}
											
			}
				
				

	        
	        int countindex=0;
	        int max=collectcount.get(0); 
	        for(int i=1;i<collectcount.size();i++){ 
	         if(collectcount.get(i)>max) 
	         { max=collectcount.get(i);
	         countindex=i;
	         }
	         }
	         
		
	        tagNameforsearch=" ";//other
		if(countindex==0)
			tagNameforsearch="exception";
		else
			if(countindex==1)
				tagNameforsearch="api example";
			else
				if(countindex==2)
					tagNameforsearch="error";
				else
					if(countindex==3)
						tagNameforsearch=" ";//other

	        final String search=(searchtxt +" "+ tagNameforsearch).trim();
	        
	        
	        //========================== 记录检索信息数据库
	        
			//??nqr.setQuerywords(querywords);
			//??nqr.setSelectFromFoamtreeWords(selectFromFoamtreeWords);
			//??nqr.setTime(time);

	       
		    
	        
			
			
			String tempsearch=search;
			queryRecsfordatabase.setSelectFromFoamtreeWords(SearchList);
			for (int i = 0; i < SearchList.size(); i++) {
				if (tempsearch.contains(CommUtil.getNewSimpleWords(SearchList.get(i).getKeywordName()).trim())) {
					tempsearch.replace(CommUtil.getNewSimpleWords(SearchList.get(i).getKeywordName()).trim(), "").trim();
				}
			}
			
				String[] inputString=tempsearch.split("[ ]");
				List<KeyWord> inputkeyKeyWords=new ArrayList<KeyWord>();
			
			for (String keys : inputString) {
				
				KeyWord kWord=new KeyWord();
				kWord.setKeywordName(keys);
				kWord.setTagName("manual");
				inputkeyKeyWords.add(kWord);	
			}
			queryRecsfordatabase.setInputWords(inputkeyKeyWords);
			
			List<KeyWord> inFoamtreeKeyWords=queryRecsfordatabase.getFoamtreeWords();
			List<KeyWord> apiKeyWordsinQuery=new ArrayList<KeyWord>();
			List<KeyWord> errorKeyWordsinQuery=new ArrayList<KeyWord>();
			List<KeyWord> exceptionKeyWordsinQuery=new ArrayList<KeyWord>();
			List<KeyWord> otherKeyWordsinQuery=new ArrayList<KeyWord>();
			for (int i = 0; i < inFoamtreeKeyWords.size(); i++) {
				if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase().contains("api")) {
					apiKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
				}
				if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase().contains("error")) {
					errorKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
				}
				if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase().contains("exception")) {
					exceptionKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
				}
				if (inFoamtreeKeyWords.get(i).getTagName().toLowerCase().contains("other")) {
					otherKeyWordsinQuery.add(inFoamtreeKeyWords.get(i));
				}
			}
			
			queryRecsfordatabase.setApiKeyWordsinQuery(apiKeyWordsinQuery);
			
			queryRecsfordatabase.setErrorKeyWordsinQuery(errorKeyWordsinQuery);
			
			queryRecsfordatabase.setExceptionKeyWordsinQuery(exceptionKeyWordsinQuery);
			
			queryRecsfordatabase.setOtherKeyWordsinQuery(otherKeyWordsinQuery);
			

			
			
	        
	        
	        //多任务实施检索
	        
			
			 Job job = new Job("GetDatafromGoogle"){   
				 protected IStatus run(IProgressMonitor monitor){   
				    // 在此添加获取数据的代码   
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
								forTopicPrepareItem.setSummary(webResult.getSummary());
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
								.openInformation(bv.getSite().getShell(),
										"Search google faild! ",
										"Sorry connection wrong or no results! Please waiting for a while search again or new keywords!");
				
								
							}

							
						}

						
						
						
					 
					 
				    Display.getDefault().asyncExec(new Runnable(){   
				        public void run()
				        {   
				        	
							
				        	
				    		genTopicTree();

				    		// end of 2014.10.15
				        // 在此添加更新界面的代码   
				    		
				    		// 在此添加更新界面的代码
				        	// 2014.10.15
				    		// 调用topic API 生成topictree

				        	for (int i = SearchList.size()-1; i >=0; i--) {
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
		
		//移除topicid信息
		for (int i = currentTopicInfoRecs.size(); i >0; i--) {
			currentTopicInfoRecs.remove(i-1);
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
			
//写入数据库
			
			NewTopicInfoRec ntif=new NewTopicInfoRec();
			ntif.setTopicName(topicitem.getText().trim());
			ntif.setSearchId(getCurrentSearchID());
			ntif.setURLcount(c.size());
			//ntif.setClickTopicTime(new Timestamp(System.currentTimeMillis()));
			//ntif.setTopicId(topicId);
			DatabaseUtil.addNewTopicInfoRectoDatabase(ntif);
			
			
			List<NewTopicWebPagesInfo> ntwpiList=new ArrayList<NewTopicWebPagesInfo>();
			String topicId=DatabaseUtil.getNewTopicInfoRecTopicId();
			NewTopicInfoRec  nifforlist=new NewTopicInfoRec();
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
							
				
				
				NewTopicWebPagesInfo ntwpi=new NewTopicWebPagesInfo();
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

	
static	List<NewTopicInfoRec> currentTopicInfoRecs=new ArrayList<NewTopicInfoRec>();//记录话题的编号
	
String currentFoamtreeString="";

private static List<KeyWord> currentSearchKeyWords201411=new ArrayList<KeyWord>();
private static int currentQueryID=0;

public void setNewWordsAndMode(List<KeyWord> snapShotAllKeyWords, List<KeyWord> keyWordsforQuery, int mode) {
		// 生成foamtree的候选词和权重字符串
		// + "{ label: \"Welcome\", weight: 2.0 },"
		// + "{ label: \"HelpSeeking\", weight: 4.0 },"
		// + "{ label: \"To\", weight: 1.0},"
		// + "{ label: \"Plugin\", weight: 2.0 },"
		// + "{ label: \"tool\", weight: 1.0 }"
		
		
		
		for (int i =currentSearchKeyWords201411.size()-1;i>=0; i--) {
			currentSearchKeyWords201411.remove(i);
		}
		
		
		//处理异常字符 如“；”等，并截短
		
			
		//试着处理给出的词汇截短符号：
		//java.io.xxx   java.io.xx.yy(zzz)
		for (int i = 0; i < keyWordsforQuery.size(); i++) {
			
			currentSearchKeyWords201411.add(keyWordsforQuery.get(i));
		
			
//			2014.11.07 暂时注销 前面context trace中已经处理过
//			String tempstr=currentSearchKeyWords201411.get(i).getKeywordName();			
//			//处理异常字符，得到截短符号，切词
//			String simplestr=(CommUtil.getSimpleWords(tempstr)).trim();		
//		//	赋值返回
//			currentSearchKeyWords201411.get(i).setKeywordName(simplestr);
//			if (simplestr.trim().equals("")) {
//				currentSearchKeyWords201411.remove(i);
//			}
			
			
			
		}
				
		
		//去除重复词  不必重复
//		List<KeyWord> noDupkeyworksforquery=new ArrayList<KeyWord>();
//		
//		for (int i = 0; i < currentSearchKeyWords201411.size(); i++) {
//			
//			boolean samekeyworks=false;
//			int nodupkeyworksqueryindex=0;
//			for (int j = 0; j < noDupkeyworksforquery.size(); j++) {
//				if (currentSearchKeyWords201411.get(i).getKeywordName().trim().equals(noDupkeyworksforquery.get(j).getKeywordName().trim())) {
//					samekeyworks=true;
//					nodupkeyworksqueryindex=j;
//					break;
//				}
//				
//			}
//			
//			if (samekeyworks) {
//				double score1=noDupkeyworksforquery.get(nodupkeyworksqueryindex).getScore();
//				double score2=currentSearchKeyWords201411.get(i).getScore();
//				noDupkeyworksforquery.get(nodupkeyworksqueryindex).setScore(score1+score2);
//			}else 
//			{
//				noDupkeyworksforquery.add(currentSearchKeyWords201411.get(i));
//			}
//			
//			
//		}
		
		
		//List<KeyWord> noDupkeyworksforquery=currentSearchKeyWords201411;
	
currentFoamtreeString="";
		String searchwords = "";
		// String currentWord="";
		String labelWeight = "";
		for (int i = 0; i < currentSearchKeyWords201411.size(); i++) {

			
			//???? 因为为了最大限度显示，已经将串中的包信息去除，只留下了最后的类名和方法名。因此不适用替换包分隔符“.”
			
			String labels ="";
			
						labels=currentSearchKeyWords201411.get(i).getKeywordName();
			labels=CommUtil.getNewSimpleWords(labels);
			
			currentFoamtreeString=currentFoamtreeString+" "+labels;

			
			if (labels.contains(".")) {
			    labels=labels.replaceAll("[.]", ". ");
			}
			
			if (labels.contains("(")) {
				labels=labels.replaceAll("[(]", " (");
			}
						
			//停用		
			//CommUtil.getSimpleWords(noDupkeyworksforquery.get(i).getKeywordName());
					//noDupkeyworksforquery.get(i).getKeywordName();
			// labels=CommUtil.getTokensfromCodeStr(labels,true);
			
			
					//.replace(".", " ");

			labelWeight = labelWeight
					+ "{ id: \"" + i 
					+ "\" , label: \""
					// + keyWordsforQuery.get(i).getKeywordName() +
					// "\", weight: "
					+ labels + "\", weight: "
					+ Math.log10(currentSearchKeyWords201411.get(i).getScore()) + " ,type: \"leaf\"},";
			
			searchwords = searchwords + " "
					+ currentSearchKeyWords201411.get(i).getKeywordName();
			System.out.println("candidate keyword No." + i + " : "
					+ currentSearchKeyWords201411.get(i).getKeywordName());

		}

		String foamTreeContent = "dataObject: {" + "groups: [" + labelWeight
				+ "]" + "}";

		
		int width ;
		int height ;
		
		
		 width = foamtreeBrowser.getBounds().width;
		 height = foamtreeBrowser.getBounds().height;
			
		
		System.out.println("width & height:" + width + ":" + height);

		// 生成网页
		genFoamTree(width, height, foamTreeFileNamePath, foamTreeContent,
				"HelloHongwei"); // Cache.getInstance().getCurrentBrowserTitle();

		// 装载网页
		foamtreeBrowser.setUrl(foamTreeFileNamePath);
		// browser.refresh();

		// mode=1时，不自动查询， mode=2时自动查询

		// txtSearch.setText(searchwords);
		

//		if (mode == 2) {
//			setCurrentQueryText(searchwords);
//
//							dosearch(getCurrentQueryText());
//				
//		}
		
		
		
		
		//记录数据准备存盘到数据库
		
//				queryRecsfordatabase.setApiKeyWordsinQuery(apiKeyWordsinQuery);
//				
//				queryRecsfordatabase.setErrorKeyWordsinQuery(errorKeyWordsinQuery);
//				
//				queryRecsfordatabase.setExceptionKeyWordsinQuery(exceptionKeyWordsinQuery);
//				
//				queryRecsfordatabase.setOtherKeyWordsinQuery(otherKeyWordsinQuery);
				
				
				queryRecsfordatabase.setFoamtreeWords(keyWordsforQuery);
				
			
				
				queryRecsfordatabase.setPretimepoint(preTimePoint);
				
				queryRecsfordatabase.setQueryId(getCurrentSearchID());
				
//				queryRecsfordatabase.setInputWords(inputWords);
//				queryRecsfordatabase.setSelectFromFoamtreeWords();
				
				queryRecsfordatabase.setSnapshotWords(snapShotAllKeyWords);
				//queryRecsfordatabase.setStarttime(starttime);
				//queryRecsfordatabase.setUser(user);
				
		

	}


static Timestamp startTimestamp=new Timestamp(System.currentTimeMillis());
static Timestamp preTimePoint=new Timestamp(System.currentTimeMillis());


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void doFoamtreeClick(String title, int width, int height) {
		//				System.out.println("select group label is : " + e.title);
						if (title.equals("foamtreetest.html")) {
							return;
		
						}
						if (title.equals("")) {
							return;
						}
		//新增是否为数字字符
						boolean bl = true;    //存放是否全为数字
						  char[] c = title.toCharArray();    //把输入的字符串转成字符数组
						  for(int i=0;i<c.length;i++){
						   if(!Character.isDigit(c[i])){   //判断是否为数字
						    bl = false;
						    break;
						   }
						  }
						  if(!bl){
							  return;
						  }
			
		
						if (!title.toLowerCase()
								.equals("HelloHongwei".toLowerCase())) {
			
							
							
							
							
							boolean isuseword=true;
							for (int i = 0; i < SearchList.size(); i++) {
								if (SearchList.get(i).getKeywordName().trim().toLowerCase().equals(currentSearchKeyWords201411.get(Integer.valueOf(title)).getKeywordName().trim().toLowerCase())) {
									isuseword=false;
									SearchList.remove(i);
									break;
								}
								
							}
							
							
							if (isuseword) {
								
								SearchList.add(currentSearchKeyWords201411.get(Integer.valueOf(title)));
							}		
								
								
							}
							
							
							String searchtext="";
							for (int i = 0; i < SearchList.size(); i++) {
								searchtext=searchtext.trim()+" "+CommUtil.getNewSimpleWords(SearchList.get(i).getKeywordName());
							}
							
							
							txtSearch.setText(searchtext);
	}

}
