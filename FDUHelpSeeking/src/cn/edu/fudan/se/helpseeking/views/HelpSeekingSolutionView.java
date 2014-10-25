package cn.edu.fudan.se.helpseeking.views;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils.Null;
import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
import cn.edu.fudan.se.helpseeking.bean.TabRecord;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.preferences.PreferenceConstants;
import cn.edu.fudan.se.helpseeking.test.SampleWebResults;
import cn.edu.fudan.se.helpseeking.util.CarrotTopic;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Label;

public class HelpSeekingSolutionView extends ViewPart {
	public HelpSeekingSolutionView() {
	}
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView"; //$NON-NLS-1$


	 
	private static String javaExceptionalFileName ="/StopResource/javaExceptionalName.txt";
	private static  Resource myResource=new Resource();
	private static String javaExceptionalName = myResource.getResource(javaExceptionalFileName );
	private static List<String> javaExceptionalNameList=CommUtil.arrayToList((javaExceptionalName).split(Basic.SPLIT_STRING));

	private static CTabFolder tabFolder;
	private static CTabItem tabItem;
	// 为了测试 private static Tree tree;
	
	//2014.10 新加
	private static Tree topictree;
	private static AmAssitBrowser myBrower ;
	private static Tree urlTree;
	//2014.10 end.
	
	private Text txtSearch;
	
	private static List<KeyWord> currentSearchWords=new ArrayList<>();
	private static List<KeyWord> sendfromselectSearchWords=new ArrayList<>();

	
	private static int currentActionID=0;
	private static String currentSearchID="0";
	private static IViewPart browserpart;
	private static IViewPart tagcloudpart;
	

	public void setTxtSeachText(List<KeyWord> selectedKeyWords)
	{
		sendfromselectSearchWords=selectedKeyWords;
		
		
		
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

//
//	public void useOleBrowser() {
//		// TODO Auto-generated method stub
//	}
	
	static List<TabRecord> myTabRecords=new ArrayList<>();
	
	
	static Timestamp startTabTime;
	static Timestamp lostFocusTime;
	static int selectTabID=0;
	
	
	

	public static CTabFolder getTabFolder() {
		return tabFolder;
	}

	public static void setTabFolder(CTabFolder tabFolder) {
		HelpSeekingSolutionView.tabFolder = tabFolder;
	}

	@Override
	public void createPartControl(Composite arg0) {
		
		arg0.setLayout(new FillLayout());

		
		tabFolder = new CTabFolder(arg0, SWT.NONE);
		TabRecord searchTab=new TabRecord();
		searchTab.setTabName("Search");
		searchTab.setStarTimestamp(new Timestamp(System.currentTimeMillis()));
		
		myTabRecords.add(searchTab);
		
	//TODO 监听器 部分
		
		
		//记录对TAB的选择事件
		tabFolder.addSelectionListener(new SelectionAdapter() {
		
			@Override
			 public void widgetSelected(final SelectionEvent e)
			{
//				if (tabFolder.getSelectionIndex()==0) {
//					System.out.println(tabFolder.getItem(0).getText());
//				}
//				
//				System.out.println("selectTabID"+selectTabID+"\nwidget selected:\n"+tabFolder.getSelectionIndex()+"\n"+e.toString()+"\n"+e.getClass());
//				
				System.out.println("before select tab id"+selectTabID);
				
	     if (selectTabID>=tabFolder.getItemCount()) {
			selectTabID=tabFolder.getSelectionIndex();
		}
	     System.out.println("if the last id the change to :"+selectTabID);
				
				Timestamp selectTabTime=new Timestamp(System.currentTimeMillis());
					
					myTabRecords.get(selectTabID).getUseTimeList().add(selectTabTime.getTime()-startTabTime.getTime());
					
			
				
				startTabTime=selectTabTime;	
				selectTabID=tabFolder.getSelectionIndex();
						
				
			}
		});
		
		//记录对TAB的关闭，打开等事件
		tabFolder.addCTabFolder2Listener(new CTabFolder2Listener() {
			
			@Override
			public void showList(CTabFolderEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void restore(CTabFolderEvent event) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void minimize(CTabFolderEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void maximize(CTabFolderEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close(CTabFolderEvent event) {
				// TODO Auto-generated method stub
//				System.out.println("close:\n" +event.toString()+"\n"+event.getClass()+"\n 选择的是"+ tabFolder.getSelectionIndex());
				
				System.out.println("close current tabid:"+selectTabID);
				Timestamp disposeTime=new Timestamp(System.currentTimeMillis());
				
					selectTabID=tabFolder.getSelectionIndex();	
					
					System.out.println("close select tabid "+selectTabID);
					
					myTabRecords.get(selectTabID).getUseTimeList().add(disposeTime.getTime()-startTabTime.getTime());
					myTabRecords.get(selectTabID).setEndTimestamp(disposeTime);
						DatabaseUtil.addBrowserUseToDataBase(myTabRecords.get(selectTabID));
						myTabRecords.remove(selectTabID);
			
				startTabTime=disposeTime;		
				
			
				
				
				
			}
		});
		
		tabFolder.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				System.out.println("dispose:\n" +e.toString()+"\n"+e.getClass());
				
				Timestamp disposeTime=new Timestamp(System.currentTimeMillis());
				
				
				startTabTime=disposeTime;
				
				if (myTabRecords.size()>0) {
					
				
				for (int i = 1; i < myTabRecords.size(); i++) {
					myTabRecords.get(i).setEndTimestamp(disposeTime);
					myTabRecords.get(i).getUseTimeList().add(disposeTime.getTime()-startTabTime.getTime());
					
					DatabaseUtil.addBrowserUseToDataBase(myTabRecords.get(i));
				}
			
				for (int i = 1; i < myTabRecords.size(); i++) {
					myTabRecords.remove(i);
				}
				}
				selectTabID=tabFolder.getSelectionIndex();
				
				
			}
		});
			
		
		tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("Search");
		
		

	
		Composite SearchComposite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(SearchComposite);
//		new Label(SearchComposite, SWT.NONE);
//		new Label(SearchComposite, SWT.NONE);
		tabFolder.setSelection(tabItem);
//		SearchComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

		
		
		
		SearchComposite.setLayoutData(BorderLayout.NORTH);
		SearchComposite.setLayout(new GridLayout(2, false));
		
		
		txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL
				| SWT.MULTI);
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtSearch.heightHint = 20;
		gd_txtSearch.widthHint = -1;
		txtSearch.setLayoutData(gd_txtSearch);
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
				
				genTopicTree();
			}
		});
		
	    
		//在调用lingfeng给的 topic API后，生成这个topictree树（第一级为topic title，第二级为 url的title ），
		//当点击某个topic时，更新后面的tree，调用雪娇的 LDA API 得到一组词汇，生成HTML文件，然后调用和刷新浏览器对象。
		
		SashForm topicComposite = new SashForm(SearchComposite, SWT.VERTICAL);
		topicComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				
	//topic list	
		topictree = new Tree(topicComposite, SWT.BORDER|SWT.CENTER);
		//topictree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		topictree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		topictree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				System.out.println("click:"+item.getData().toString());
				
				if (item.getData()=="TOPIC") 
				{
				tagcloudpart = FDUHelpSeekingPlugin
						.getDefault()
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.findView(
								"cn.edu.fudan.se.helpseeking.views.HelpSeekingTagCloundView");
			
				if ((tagcloudpart instanceof HelpSeekingTagCloundView)) {
					
		String content= "<!DOCTYPE html> <html>  "
				+ "<head> "
				+ "<title> Test interact word </title>"
				+ " </head>"
				+ "<body>"
				+ " <p>  "+ item.getText().toString() + " </p>"
			    + " </body>"
			    + "</html>";
		
					String fileurl=CommUtil.getPluginCurrentPath()+"Tag.html";
					FileHelper.createFile(fileurl, content);
					
					HelpSeekingTagCloundView tcv = (HelpSeekingTagCloundView) tagcloudpart;
					tcv.getMyBrowser().setNewUrl(fileurl);
					//bv.getMyBrowser().getMyComposite().pack();
				}
		
				}
				
				
				//TODO 选择一个topic  给出一组list  得到 2014.10
				
				urlTree.removeAll();
				if (item.getData()=="TOPIC") 
				{
					
				for (int i = 0; i < item.getItemCount(); i++) {
					TreeItem urlTreeItem= new TreeItem(urlTree, SWT.NONE);
					urlTreeItem.setData(item.getItem(i).getItem(1).getText());
					urlTreeItem.setText(item.getItem(i).getItem(0).getText());
					urlTreeItem.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_RED));
					
					
					TreeItem urlTreeItemofItemtitle =new TreeItem(urlTreeItem, SWT.NONE);
					urlTreeItemofItemtitle.setData(item.getItem(i).getItem(1).getText());
					urlTreeItemofItemtitle.setText(item.getItem(i).getItem(1).getText());
					
					TreeItem urlTreeItemofItemsummary =new TreeItem(urlTreeItem, SWT.NONE);
					urlTreeItemofItemsummary.setData(item.getItem(i).getItem(1).getText());
					urlTreeItemofItemsummary.setText(item.getItem(i).getItem(2).getText());
					
					urlTreeItem.setExpanded(true);
				}
				
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//URL list	
		urlTree = new Tree(topicComposite, SWT.BORDER|SWT.CENTER);
		//urlTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		urlTree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		topicComposite.setWeights(new int[] {214, 105});
		new Label(SearchComposite, SWT.NONE);
		new Label(SearchComposite, SWT.NONE);
		urlTree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				System.out.println("click:"+item.getData().toString());
				
				browserpart = FDUHelpSeekingPlugin
						.getDefault()
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.findView(
								"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
			
				if ((browserpart instanceof HelpSeekingBrowserView)
				&& item.getData() != null) {
					HelpSeekingBrowserView bv = (HelpSeekingBrowserView) browserpart;
					bv.getMyBrowser().setNewUrl(item.getData().toString());
					//bv.getMyBrowser().getMyComposite().pack();
				}
				
				//TODO 选择一个topic  给出一组list  得到 2014.10
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	//topic list 	
		
		
//temporary stop foamtree 
//    
//		Composite browserComposite = new Composite(SearchComposite, SWT.NONE);
//		
////		browserComposite.setLayoutData(BorderLayout.CENTER);
////		browserComposite.setLayout(new GridLayout(2, false));
////		
////	    Browser  browser = new Browser(browserComposite, SWT.NONE);
////	    browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
////	    System.out.println(CommUtil.getFDUPluginCurrentPath()+"foamtree/foamtreetest.html");
////	    browser.setUrl(CommUtil.getFDUPluginCurrentPath()+"foamtree/foamtreetest.html");
//		browserComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
//		
//		myBrower = new AmAssitBrowser();
//		myBrower.setMyComposite(browserComposite);
//		myBrower.setDisplay(browserComposite.getDisplay());
//		myBrower.createShow();
//		myBrower.refreshBrowser();
//		myBrower.setDisableButton(false);
//		
//		 String userpath=CommUtil.getFDUPluginCurrentPath()+"foamtree/foamtreetest.html";
//		 
//		 System.out.println("browser userpath in solutionview or browserview :"+userpath);
//			myBrower.setNewUrl(userpath);
//			myBrower.setRefreshOnly(false);
//			myBrower.getMyComposite().pack();
//			
//
	// temporary  foamtree....		
			
//			tree = new Tree(SearchComposite, SWT.BORDER);
//			
//			//for minimizing change code set tree disable to view 2014.10.15
//			tree.setVisible(false);
//			
//			tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
//			tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
//			tree.addSelectionListener(new SelectionListener() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					TreeItem item = (TreeItem) e.item;
//
//	                String content="";
//					String compData1=item.getText();
//					String compData2=(String)item.getData();
//					
//					int currentindex=0;
//					List <SearchNode> searchNode=sResults.getSearchNode();
//					int totalresults=searchNode.size();
//					
//					
//					for (int i = 0; i < searchNode.size(); i++) {
//						
//						if (compData1.equals(searchNode.get(i).getTitle())) {
//							
//							if (compData2.equals(searchNode.get(i).getLink())) {
//								
//								currentindex=searchNode.get(i).getPositionInResultslist();
//								content=searchNode.get(i).getContents();
//								compData1=searchNode.get(i).getTitle();
//								compData2=searchNode.get(i).getLink();
//									break;
//							}
//							
//							
//						}else {
//							if (compData1.equals(searchNode.get(i).getLink())) {
//								
//								if (compData2.equals(searchNode.get(i).getLink())) {
//									
//									currentindex=searchNode.get(i).getPositionInResultslist();
//									content=searchNode.get(i).getContents();
//									compData1=searchNode.get(i).getTitle();
//									compData2=searchNode.get(i).getLink();
//									break;
//								}
//							
//						}
//						
//						
//					}
//					}
//					
//					UseResultsRecord urr=new UseResultsRecord();
//					urr.setContent(content);
//					urr.setPosition(currentindex);
//					urr.setSearchID(currentSearchID);
//					urr.setSolutionID("0-0");
//					urr.setTitle(compData1);
//					urr.setTotallist(totalresults);
//					urr.setType("Manual");
//					urr.setUrl(compData2);
//					urr.setTime(new Timestamp(System.currentTimeMillis()));
//					
//					openNewTabByURL(urr);
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//				}
//			});
//			/*TreeItem item = new TreeItem(tree, SWT.NONE);
//			item.setText("人人");
//			item.setData("www.renren.com");*/

			
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	
private void manualSearch() {
		System.out.println("Say start manual search ...");
		
		String queryText = txtSearch.getText().trim();
		
		if (!queryText.equals("")) {
			
		
		Query query = new Query();
		// query.setInforID(getCurrentActionID());
		Timestamp starttime = new Timestamp(System.currentTimeMillis());
		query.setTime(starttime);
		query.setIsbyuser(true);
		query.setQueryLevel(QueryLevel.Middle);
		query.setInforID(getCurrentActionID());
		query.setUseKeywords(queryText);
		query.makeCandidateKeywords(Cache.getInstance()
				.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
		String searchID = "P" + query.getInforID();
		query.setSearchID(searchID);
		setCurrentSearchID(searchID);
		query.setIsbyuser(true);

		try {
			dosearch(query, searchID, queryText);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	//	type:   auto   manual 
	public static void openNewTabByURL(UseResultsRecord urr) {
		
		
			if (urr.getUrl()!=null) {
			if (!urr.getUrl().equals("")) {

				final CTabItem tab = new CTabItem(tabFolder, SWT.CLOSE);
				tab.setText(urr.getTitle());
				
				String maxsolutionID=String.valueOf(currentActionID)+urr.getType()+urr.getSearchID()+"-"+String.valueOf(urr.getPosition());
//				tab.setData(maxsolutionID);// 唯一编号 actionid + SERCHTYPE+SEARCHID+POSTION

				Timestamp firstOpenTime=new Timestamp(System.currentTimeMillis());
				Composite tabComposite = new Composite(tabFolder, SWT.NONE);
				tabComposite.setLayoutData(BorderLayout.NORTH);
				tabComposite.setLayout(new GridLayout(2, false));
				AmAssitBrowser myBrower = new AmAssitBrowser();
				
				
				myBrower.setMyComposite(tabComposite);
				myBrower.setDisplay(tabComposite.getDisplay());
				myBrower.createShow();
				myBrower.refreshBrowser();
				myBrower.setDisableButton(true);

				myBrower.setNewUrl(urr.getUrl());
				myBrower.getMyComposite().pack();
				
				tab.setControl(tabComposite);

//				tabFolder.setSelection(tab);	
				
				
				
				
			
				TabRecord mytabR=new TabRecord();
				
				int reopenindex=0;
				mytabR.setStarTimestamp(firstOpenTime);
				mytabR.setContentRecord(urr.getContent());
				mytabR.setCurrentActionID(currentActionID);
				mytabR.setSearchID(currentSearchID);
				mytabR.setSearchResultsListPostion(urr.getPosition());
				mytabR.setTotallistnumber(urr.getTotallist());
				mytabR.setSolutionID(maxsolutionID);// 唯一编号 actionid + SERCHTYPE+SEARCHID+POSTION
				mytabR.setTabName(urr.getTitle());
				mytabR.setTitleRecord(urr.getTitle());
				mytabR.setUrlRecord(urr.getUrl());
				mytabR.setReOpenIndex(reopenindex);
				mytabR.setSearchType(urr.getType());
				
				
					for (int i = 0; i < myTabRecords.size(); i++) {
					
						if (myTabRecords.get(i).getUrlRecord()!=null) {
						
					if (myTabRecords.get(i).getUrlRecord().equals(urr.getUrl())) {
						reopenindex=myTabRecords.get(i).getReOpenIndex();
						mytabR.setReOpenIndex(reopenindex+1);
					}
					
					}
					
				}	
				
				startTabTime=firstOpenTime;
				
				urr.setSolutionID(maxsolutionID);
								
				myTabRecords.add(mytabR);
				tab.setData(mytabR);
				
		
				
				
				DatabaseUtil.addUseResultsRecord(urr);
				
				

			}

			}


		
	}

	
	static SearchResults sResults;
	static List<WEBResult> googlesearchList=new ArrayList<WEBResult>();
	static List<WEBResult> resultsForTopicList=new ArrayList<WEBResult>();
	
	private static void dosearch(Query query, String searchID, String search) throws InterruptedException {

		Timestamp starttime;
		String searchResultOutput="\n============\n";
		sResults=new SearchResults();
		starttime=new Timestamp(System.currentTimeMillis());
		
		sResults.setSearchID(searchID);
			query.setTime(starttime);
			query.setIsbyuser(false);
			query.setUseKeywords(search);
			query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
		

//			IPreferenceStore ps=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
//			String cse_key=ps.getString(PreferenceConstants.CSE_KEY);
//			String cse_cx=ps.getString(PreferenceConstants.CSE_CX);

			//在此处从14个定制引擎中随机选择一个
			int temp=CommUtil.randomInt(CommUtil.getKeyCxList().size()-1, 0);
			String cse_key=CommUtil.getKeyCxList().get(temp).getKey();
			String cse_cx=CommUtil.getKeyCxList().get(temp).getCx();

			LoopGoogleAPICall apiCall = new LoopGoogleAPICall(cse_key,cse_cx,search);
			 
			apiCall.start();
			apiCall.join();
	
			googlesearchList = apiCall.getCurrentResults();
				
	if (googlesearchList.size()>0) 
	{
	// 清除tree				tree.removeAll();					
					// 2014.10.15
					for (int i = 0; i < resultsForTopicList.size(); i++) 
					{
						resultsForTopicList.remove(i);
					}
					// end of 2014.10.15
				
				int indexResultslist=0;
	for (WEBResult webResult : googlesearchList) 
		{
					String titleNoFormating = webResult.getTitleNoFormatting();
					titleNoFormating = titleNoFormating.replaceAll("&quot;", "\"");
					titleNoFormating.replaceAll("&#39;", "\'");
					titleNoFormating.replaceAll("<b>", " ");
					titleNoFormating.replaceAll("</"," ");
					titleNoFormating.replaceAll("b>"," ");
					
					searchResultOutput=searchResultOutput+"\n"+webResult.toString();
					
					// 2014.10.15
					webResult.setTitleNoFormatting(titleNoFormating);
					WEBResult forTopicPrepareItem=new WEBResult();
					forTopicPrepareItem.setContent(webResult.getContent());
					forTopicPrepareItem.setUrl(webResult.getUrl());
					forTopicPrepareItem.setTitle(webResult.getTitleNoFormatting());
					resultsForTopicList.add(forTopicPrepareItem);
					
					// end of 2014.10.15

				//以下是原有 tree的 节点的生成  暂时移除	
//					TreeItem item = new TreeItem(tree, SWT.NONE);
//					
//					String resultTitle =titleNoFormating;
//					
//					String[] tempsearchlist=search.split("[ ]");
//					String importantword="";
//					for (int i = 0; i < tempsearchlist.length; i++) {
//						if (resultTitle.contains(tempsearchlist[i].trim())) {
//							importantword=importantword+" "+tempsearchlist[i];
//						}
//					}
//					
//				if (!importantword.trim().equals("")) {
//					if (resultTitle.length()>60) {
//						resultTitle=resultTitle.substring(60);						
//					}else {
//						String templatespace="";
//						for (int i = resultTitle.length(); i < 60; i++) {
//							templatespace=templatespace+" ";
//						}
//						resultTitle=resultTitle+templatespace;
//					}					
//					resultTitle=resultTitle+" ["+importantword+"]";
//				}
//										
////					item.setText(xml.length()>50?xml.substring(0,47)+"...":xml);
//					item.setText(resultTitle);
//
//					item.setData(webResult.getUrl());
//					item.setForeground(Display.getDefault()
//							.getSystemColor(SWT.COLOR_BLUE));
//
//					String compareContent=titleNoFormating+" "+webResult.getContent();
//				
//					SearchNode sNode=new SearchNode();
//					sNode.setTitle(resultTitle);
//					sNode.setLink(webResult.getUrl());
//					
//					sNode.setSearchID(searchID);
//					sNode.setPositionInResultslist(indexResultslist);
//					indexResultslist=indexResultslist+1;
//								
//					//展示 URL
//					TreeItem urlItem=new TreeItem(item, SWT.NULL);
////					urlItem.setForeground(Display.getDefault()
////							.getSystemColor(SWT.COLOR_BLUE));
//					urlItem.setText(webResult.getUrl());
//					urlItem.setData(webResult.getUrl());
//					
//					//展示语言
//					if (webResult.getLanguage()!=null) {
//						TreeItem languageItem=new TreeItem(item, SWT.NULL);
//					languageItem.setText(webResult.getLanguage());
//					}					
//					//展示content  内容  还需要 优化处理  显示关键词 附近的词 构成的串 并突出显示
//					if (webResult.getContent()!=null)
//					{
//						
//						String content=webResult.getContent();
//						
//						//处理content  保留部分文字
//						if (!content.equals("")) {
//    					TreeItem contentItem=new TreeItem(item, SWT.NULL);
////    					String tempcontents=content.length()>50?content.substring(0,47)+"...":content;
//    					String tempcontents=content;
//    					
//					    contentItem.setText(tempcontents);
////					    contentItem.setData(webResult.getUrl());
//					    contentItem.setData(null);
//					    sNode.setContents(tempcontents);
//					    }
//					} //end if  webresult.getcontent
//					
//					//展开节点
//					item.setExpanded(true);
//					
//					//后续在这里适当过滤一下  如果有异常名字则显示出来   或者 是 
////					List<String> tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
//					compareContent=compareContent+" "+search;
//					List<String> tempContent=CommUtil.arrayToList(compareContent.split(Basic.SPLIT_STRING));
//					
//					 Set<String> boldWords=new HashSet<String>();;	
//
//					if (Basic.javaExceptionalNameList!=null)
//					{	for(String str: tempContent)
//					
//					{
//						
//					for (String jestr : Basic.javaExceptionalNameList)
//					{
//						if (str.equals(jestr))
//						{
//							boldWords.add(jestr);
//							break;
//						}
//					}
//					}
//					}
//					
//
//					item.setExpanded(true);
//					sResults.getSearchNode().add(sNode);					
		}  //end foreach googlesearchlist
				
				Timestamp endtime=new Timestamp(System.currentTimeMillis());
				query.setCosttime(endtime.getTime()-starttime.getTime());
			
				//生成选择和自己输入的关键词并记录数据库
				
				savekeywordsToDatabase(query, searchID, search, starttime,
						searchResultOutput);
				
				
				
				// 2014.10.15
				// 调用topic API 生成topictree
				
				genTopicTree();
				
				// end of 2014.10.15

						
	}  // if googlesearchlist.size > 0
	else 
	{
		System.out.println("No return results!");
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Search google faild! ", "Sorry connection wrong or no results! Please search agin wait for a while!" );
	}
		
	}  //end dosearch

	public static void genTopicTree() {

		
		List<Cluster> clusters = CarrotTopic.fromWebResults(Collections.unmodifiableList(resultsForTopicList));
		
		//测试试用句子 记得删除！！！
		
		List<WEBResult> results = SampleWebResults.WEB_RESULTS;
        if (clusters.isEmpty()) 
        {
           clusters = CarrotTopic.fromWebResults(results);
           System.out.println("clusters is null");
        }
        
//		
		// 生成topic tree
		// 只考虑第一层， level=0时 topic的数量  每个topic下面的子topic暂时不给出； cluster中已有，需要时可以去取
		topictree.removeAll();
		urlTree.removeAll();
		

		for(int i=0; i<clusters.size(); i++)
		 {
			 Cluster c = clusters.get(i);
			 
			 TreeItem topicitem = new TreeItem(topictree, SWT.NONE);
			 topicitem.setText(c.getLabel());
			 topicitem.setData("TOPIC"); 
			 //只有点击topic后，以该topic为单位，生成URL list 调用雪娇给出的LDA处理
			 //实现代码见 topictree的选择事件
			 topicitem.setForeground(Display.getDefault()
						.getSystemColor(SWT.COLOR_BLUE));

			 for(int j=0; j<c.getAllDocuments().size(); j++)
			 {		
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
		for (int i = 0; i < currentSearchWords.size(); i++) 
		{
			currentSearchWords.remove(i);
		}
		
		
		List<String> searchWords=CommUtil.arrayToList(search.split("[ ]"));
		
		for (int i = 0; i < searchWords.size(); i++) 
		{
			boolean flag=true;
			for (int j = 0; j < sendfromselectSearchWords.size(); j++) 
			{
				if (sendfromselectSearchWords.get(j).getKeywordName().trim().equals(searchWords.get(i).trim())) 
				{   
					sendfromselectSearchWords.get(j).setPositionInUseString(j);
					sendfromselectSearchWords.get(j).setRecommand(true);
					sendfromselectSearchWords.get(j).setTimes(starttime);
					sendfromselectSearchWords.get(j).setLastSearchID(searchID);
				    currentSearchWords.add(sendfromselectSearchWords.get(j));
				    flag=false;
				}	
			}
			if (flag) {
				KeyWord word=new KeyWord();
				word.setKeywordName(searchWords.get(i).trim());
				word.setLastSearchID(searchID);
				word.setPositionInUseString(i);
				word.setTimes(starttime);
				word.setSearchID("NO");//意味着这个是人工词汇
				word.setRecommand(false);
				currentSearchWords.add(word);
			}					
		}//end for i
		
		DatabaseUtil.addSearchWordsToDataBase(currentSearchWords);
		
		
// 需要保存关键词和当前cache到数据库中：
		DatabaseUtil.addKeyWordsToDataBase(query);
		for (SearchNode snNode : sResults.getSearchNode()) 
		{
			DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
		}
			searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
			FileHelper.appendContentToFile("result.txt", searchResultOutput);

			Cache.getInstance().setTimerAutoSearchmode(0);
			Cache.getInstance().setLastAutoSearchTime(new Timestamp(System.currentTimeMillis()));
	}

}
