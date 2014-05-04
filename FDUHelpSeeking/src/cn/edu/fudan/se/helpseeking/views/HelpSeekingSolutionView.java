package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
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
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.web.SimpleBrower;

public class HelpSeekingSolutionView extends ViewPart {
	public HelpSeekingSolutionView() {
	}
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView"; //$NON-NLS-1$

	public static SimpleBrower myBrower;
	 
	private static String javaExceptionalFileName ="/StopResource/javaExceptionalName.txt";
	private static  Resource myResource=new Resource();
	private static String javaExceptionalName = myResource.getResource(javaExceptionalFileName );
	private static List<String> javaExceptionalNameList=CommUtil.arrayToList((javaExceptionalName).split(Basic.SPLIT_STRING));

	private static CTabFolder tabFolder;
	private static CTabItem tabItem;
	private static Tree tree;
	private Text txtSearch;
	
	private static List<KeyWord> currentSearchWords=new ArrayList<>();
	private static List<KeyWord> sendfromselectSearchWords=new ArrayList<>();

	
	private static int currentActionID=0;
	private static String currentSearchID="0";
	

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

	public SimpleBrower getMyBrower() {
		return myBrower;
	}

	public void setMyBrower(SimpleBrower myBrower) {
		HelpSeekingSolutionView.myBrower = myBrower;
	}

	public void useOleBrowser() {
		// TODO Auto-generated method stub
	}
	
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
		
	//TODO 监听器 部分
		
		
		//记录对TAB的选择事件
		tabFolder.addSelectionListener(new SelectionAdapter() {
		
			@Override
			 public void widgetSelected(final SelectionEvent e)
			{
				if (tabFolder.getSelectionIndex()==0) {
					System.out.println(tabFolder.getItem(0).getText());
				}
				
				System.out.println("selectTabID"+selectTabID+"\nwidget selected:\n"+tabFolder.getSelectionIndex()+"\n"+e.toString()+"\n"+e.getClass());
				
				Timestamp selectTabTime=new Timestamp(System.currentTimeMillis());
				
				int thisselect=tabFolder.getSelectionIndex();
				if (thisselect==0 && thisselect==selectTabID) {
					return;
				}
				
				if (selectTabID!=0) {
					
					myTabRecords.get(selectTabID-1).getUseTimeList().add(selectTabTime.getTime()-startTabTime.getTime());
					
					
				}
				
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
				System.out.println("restore:\n" +event.toString()+"\n"+event.getClass());
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
				System.out.println("close:\n" +event.toString()+"\n"+event.getClass()+"\n 选择的是"+ tabFolder.getSelectionIndex());
				
				
				Timestamp disposeTime=new Timestamp(System.currentTimeMillis());
				if (selectTabID!=0) {
					
					myTabRecords.get(selectTabID-1).getUseTimeList().add(disposeTime.getTime()-startTabTime.getTime());
					myTabRecords.get(selectTabID-1).setEndTimestamp(disposeTime);
						DatabaseUtil.addBrowserUseToDataBase(myTabRecords.get(selectTabID-1));
						myTabRecords.remove(selectTabID-1);
				}
				startTabTime=disposeTime;		
				
			
				
				selectTabID=tabFolder.getSelectionIndex();		
				
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
					
				
				for (int i = 0; i < myTabRecords.size(); i++) {
					myTabRecords.get(i).setEndTimestamp(disposeTime);
					myTabRecords.get(i).getUseTimeList().add(disposeTime.getTime()-startTabTime.getTime());
					
					DatabaseUtil.addBrowserUseToDataBase(myTabRecords.get(i));
				}
			
				for (int i = 0; i < myTabRecords.size(); i++) {
					myTabRecords.remove(i);
				}
				}
				selectTabID=tabFolder.getSelectionIndex();
				
				
			}
		});
		
		tabFolder.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("focuse lost:\n" +e.toString()+"\n"+e.getClass());
				lostFocusTime=new Timestamp(System.currentTimeMillis());
//				selectTabID=tabFolder.getSelectionIndex();
				
//				myRecord.getUseTimeList().add(lostTabTime.getTime()-startTabTime.getTime());	
				
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("focuse gained:\n" +e.toString()+"\n"+e.getClass());
				Timestamp gainFocusTime=new Timestamp(System.currentTimeMillis());
				int thisselect=tabFolder.getSelectionIndex();
				if (thisselect==0 && thisselect==selectTabID) {
					return;
				}
				
				if (selectTabID!=0) {
					myTabRecords.get(selectTabID-1).getUseTimeList().add(lostFocusTime.getTime()-gainFocusTime.getTime());
					startTabTime=gainFocusTime;		
				}
				selectTabID=tabFolder.getSelectionIndex();
			}
		});
		
	
		
		tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("Search Page");
		
		

	
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

				dosearch(query, searchID, queryText);
				}
				
				
			}
		});
		tree = new Tree(SearchComposite, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;

                String content="";
				String compData1=item.getText();
				String compData2=(String)item.getData();
				
				int currentindex=0;
				List <SearchNode> searchNode=sResults.getSearchNode();
				int totalresults=searchNode.size();
				
				
				for (int i = 0; i < searchNode.size(); i++) {
					
					if (compData1.equals(searchNode.get(i).getTitle())) {
						
						if (compData2.equals(searchNode.get(i).getLink())) {
							
							currentindex=searchNode.get(i).getPositionInResultslist();
							content=searchNode.get(i).getContents();
							compData1=searchNode.get(i).getTitle();
							compData2=searchNode.get(i).getLink();
								break;
						}
						
						
					}else {
						if (compData1.equals(searchNode.get(i).getLink())) {
							
							if (compData2.equals(searchNode.get(i).getLink())) {
								
								currentindex=searchNode.get(i).getPositionInResultslist();
								content=searchNode.get(i).getContents();
								compData1=searchNode.get(i).getTitle();
								compData2=searchNode.get(i).getLink();
								break;
							}
						
					}
					
					
				}
				}
				
				UseResultsRecord urr=new UseResultsRecord();
				urr.setContent(content);
				urr.setPosition(currentindex);
				urr.setSearchID(currentSearchID);
				urr.setSolutionID("0-0");
				urr.setTitle(compData1);
				urr.setTotallist(totalresults);
				urr.setType("Manual");
				urr.setUrl(compData2);
				urr.setTime(new Timestamp(System.currentTimeMillis()));
				
				openNewTabByURL(urr);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		/*TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("人人");
		item.setData("www.renren.com");*/

		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

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
				SimpleBrower myBrower = new SimpleBrower();
				myBrower.setMyComposite(tabComposite);
				myBrower.createShow();
				myBrower.refreshBrowser();
				myBrower.setDisableButton(true);

				myBrower.setNewUrl(urr.getUrl());
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
					if (myTabRecords.get(i).getUrlRecord().equals(urr.getUrl())) {
						reopenindex=myTabRecords.get(i).getReOpenIndex();
						mytabR.setReOpenIndex(reopenindex+1);
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
	
	private static void dosearch(Query query, String searchID, String search) {

		Timestamp starttime;
		String searchResultOutput="\n============\n";
		sResults=new SearchResults();
		starttime=new Timestamp(System.currentTimeMillis());
		
		sResults.setSearchID(searchID);
			query.setTime(starttime);
			query.setIsbyuser(false);
			query.setUseKeywords(search);
			query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
		

			LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
			
			
			try {
				List<WEBResult> googlesearchList = apiCall.searchWeb(search);
				if (googlesearchList.size()>0) {
					tree.removeAll();
				
				int indexResultslist=0;
				for (WEBResult webResult : googlesearchList) {
					String xml = webResult.getTitleNoFormatting();
					xml = xml.replaceAll("&quot;", "\"");
					xml.replaceAll("&#39;", "\'");
					xml.replaceAll("<b>", " ");
					xml.replaceAll("</"," ");
					xml.replaceAll("b>"," ");
					
					searchResultOutput=searchResultOutput+"\n"+webResult.toString();
					
					TreeItem item = new TreeItem(tree, SWT.NONE);
					
					item.setText(xml.length()>50?xml.substring(0,47)+"...":xml);
					item.setData(webResult.getUrl());
					item.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_BLUE));

					String compareContent=xml+" "+webResult.getContent();
				
					
					SearchNode sNode=new SearchNode();
					sNode.setTitle(xml);
					sNode.setLink(webResult.getUrl());
					
					sNode.setSearchID(searchID);
					sNode.setPositionInResultslist(indexResultslist);
					indexResultslist=indexResultslist+1;
					
					
				
					
					
					//展示 URL
					TreeItem urlItem=new TreeItem(item, SWT.NULL);
//					urlItem.setForeground(Display.getDefault()
//							.getSystemColor(SWT.COLOR_BLUE));
					urlItem.setText(webResult.getUrl());
					urlItem.setData(webResult.getUrl());
					
					
					//展示语言
					if (webResult.getLanguage()!=null) {
						TreeItem languageItem=new TreeItem(item, SWT.NULL);
					languageItem.setText(webResult.getLanguage());

					}
					
					//展示content  内容  还需要 优化处理  显示关键词 附近的词 构成的串 并突出显示
					if (webResult.getContent()!=null) {
						
						String content=webResult.getContent();
						
						//处理content  保留部分文字
						
						
						
						if (!content.equals("")) {
    					TreeItem contentItem=new TreeItem(item, SWT.NULL);
    					String tempcontents=content.length()>50?content.substring(0,47)+"...":content;
					    contentItem.setText(tempcontents);
//					    contentItem.setData(webResult.getUrl());
					    contentItem.setData(null);
					    sNode.setContents(tempcontents);
					    
					    
					    }
					}
					
					
					//展开节点
					item.setExpanded(true);
					
					//后续在这里适当过滤一下  如果有异常名字则显示出来   或者 是 
//					List<String> tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
					compareContent=compareContent+" "+search;
					List<String> tempContent=CommUtil.arrayToList(compareContent.split(Basic.SPLIT_STRING));
					
					 Set<String> boldWords=new HashSet<String>();;
//					tempContent.retainAll(Basic.javaExceptionalNameList);
//					for(Iterator it = tempContent.iterator();it.hasNext();){  
//			            if (boldWords.equals(""))
//			            boldWords=(String)it.next();
//			            else
//						boldWords=boldWords+";"+(String)it.next();
//			        }  
		

					if (Basic.javaExceptionalNameList!=null)
					{	for(String str: tempContent)
					
					{
						
					for (String jestr : Basic.javaExceptionalNameList)
					{
						if (str.equals(jestr))
						{
							boldWords.add(jestr);
							break;
						}
					}
					}
					}
					
					if (!boldWords.isEmpty()) {
					String myExceptioinName="";
					
					for (String exceptionName : boldWords) {
						if(myExceptioinName.equals(""))
							myExceptioinName=exceptionName;
						else 
							myExceptioinName=myExceptioinName+" ; "+exceptionName;
					}
						
					sNode.setJavaExceptionNames(myExceptioinName);
					TreeItem subitem=new TreeItem(item, SWT.NULL);
					subitem.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_RED));
					subitem.setText(myExceptioinName);
					subitem.setData(null);
					subitem.setExpanded(true);
					
					}

					item.setExpanded(true);
					sResults.getSearchNode().add(sNode);
					
					
				}
				
				Timestamp endtime=new Timestamp(System.currentTimeMillis());
				
				query.setCosttime(endtime.getTime()-starttime.getTime());
			
				//生成选择和自己输入的关键词并记录数据库
				
				List<String> searchWords=CommUtil.arrayToList(search.split("[ ]"));
				
				for (int i = 0; i < searchWords.size(); i++) {
					
					
					
					
					boolean flag=true;
					for (int j = 0; j < sendfromselectSearchWords.size(); j++) {
						
						if (sendfromselectSearchWords.get(j).getKeywordName().trim().equals(searchWords.get(i).trim())) {
							
							sendfromselectSearchWords.get(j).setPositionInUseString(j);
							sendfromselectSearchWords.get(j).setRecommand(true);
							sendfromselectSearchWords.get(j).setTimes(starttime);
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
					
					
				}
				
				DatabaseUtil.addSearchWordsToDataBase(currentSearchWords);
				
				
			// 需要保存关键词和当前cache到数据库中：
				DatabaseUtil.addKeyWordsToDataBase(query);
				for (SearchNode snNode : sResults.getSearchNode()) {
					DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
				}
					searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
					FileHelper.appendContentToFile("result.txt", searchResultOutput);

				
				}else {
					System.out.println("No return results!");
				}
					
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			

		
	}

}
