package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.ui.IWorkbenchPage;
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
import cn.edu.fudan.se.helpseeking.bean.QueryList;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;

public class HelpSeekingSearchView extends ViewPart {
	
//	private static class Singleton extends ViewPart{
//		
//		private static final HelpSeekingSearchView INSTANCE=new HelpSeekingSearchView();
//
//		@Override
//		public void createPartControl(Composite arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setFocus() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//	
	
//	public static final HelpSeekingSearchView getInstance()
//	{
//		return Singleton.INSTANCE;
//	}
//
	
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView"; //$NON-NLS-1$

	private Label label;
	private Text txtCandidateSearch;
	private Text txtSearchBox;
	//private static org.eclipse.swt.widgets.List list;
	private static Tree tree;
	private static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();

	

	public HelpSeekingSearchView() {
		super();
		part = FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.findView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
	}
	
	
	static IViewPart part;
	
	String username = System.getProperties().getProperty("user.name");
	
	private int currentActionID;
	private static String  currentSearchID="";

	@Override
	public void createPartControl(Composite arg0) {
		BorderLayout myboBorderLayout=new BorderLayout();
		arg0.setLayout(myboBorderLayout);

		Composite SearchComposite = new Composite(arg0, SWT.NONE);
		SearchComposite.setLayoutData(BorderLayout.NORTH);
		SearchComposite.setLayout(new GridLayout(3, false));
		
		label = new Label(SearchComposite, SWT.NONE);
		label.setText("SearchBox:");
		txtSearchBox = new Text(SearchComposite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL | SWT.MULTI);
		txtSearchBox.setText("");
		GridData agd_txtSearch = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		agd_txtSearch.heightHint = 20;
		agd_txtSearch.widthHint = -1;
		txtSearchBox.setLayoutData(agd_txtSearch);

		Button btnSearchGoogle = new Button(SearchComposite, SWT.NONE);
		btnSearchGoogle.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		btnSearchGoogle.setForeground(SWTResourceManager.getColor(0, 0, 0));
		btnSearchGoogle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//list.removeAll();
				tree.removeAll();
				String queryText = txtSearchBox.getText().trim();
				Query query=new Query();
				query.setInforID(getCurrentActionID());
				Timestamp starttime=new Timestamp(System.currentTimeMillis());
				query.setTime(starttime);
				query.setIsbyuser(true);
				query.setQueryLevel(QueryLevel.Middle);
				query.setUseKeywords(queryText);
				query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
				String searchID="P"+query.getInforID();
				query.setSearchID(searchID);
				
				
				
				//为测试注释，正式使用请注释 doTestTree()  !!!!!!!!!!!!
				dosearch(query, searchID, queryText);
//				doTestTree();
	

			}
		});
		btnSearchGoogle.setText("Search");

		
		label = new Label(SearchComposite, SWT.NONE);
		label.setText("CandidateWords:");
		txtCandidateSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL | SWT.MULTI);
		txtCandidateSearch.setText("");
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtSearch.heightHint = 20;
		gd_txtSearch.widthHint = -1;
		txtCandidateSearch.setLayoutData(gd_txtSearch);

		Button btnSwitchWords = new Button(SearchComposite, SWT.NONE);
		btnSwitchWords.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		btnSwitchWords.setForeground(SWTResourceManager.getColor(0, 0, 0));
		btnSwitchWords.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSwitchWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	         txtSearchBox.setText(txtCandidateSearch.getText());

			}
		});
		btnSwitchWords.setText("Switch to SearchBox");

	
		
		tree = new Tree(arg0, SWT.SINGLE|SWT.FULL_SELECTION| SWT.FILL | SWT.H_SCROLL	| SWT.V_SCROLL);		
		tree.setLayoutData(BorderLayout.CENTER);
		tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tree.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TreeItem item = (TreeItem) e.item;
				
				if (item.getData()==null) {
					return;
				}
				
				///////////////////////////////////////////////////////////////
				
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
					part=page.showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
					
				} catch (Exception ple) {
					ple.printStackTrace();
					return;
				}

				
				if ((part instanceof HelpSeekingSolutionView) && item.getData() != null)
				{
					HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
				v.getMyBrower().setDisableButton(false);
				v.getMyBrower().setNewUrl((String) item.getData());
				
				String content ="\n---------\n[user:]\t\t\t\t"+username+ "\n[at time:]\t\t\t" +(new Timestamp(System.currentTimeMillis())).toString()
						              +"\n[Current searchID:]\t"+getCurrentSearchID() +"\n[Selected Item:]\n"+item.getText()+"\n"+(String)item.getData()+"\n---------\n";
				FileHelper.appendContentToFile("result.txt", content);
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	

	public static String getCurrentSearchID() {
		return currentSearchID;
	}

	public static void setCurrentSearchID(String SearchID) {
		currentSearchID = SearchID;
	}

	public void setCandidateSearchWords(String search) {
		txtCandidateSearch.setText(search);
	}
	
	
	
//	 static String javaExceptionalFileName ="/StopResource/javaExceptionalName.txt";
//	static  Resource myResource=new Resource();
//	 static String javaExceptionalName = myResource.getResource(javaExceptionalFileName );
//
//	 static List<String> Basic.javaExceptionalNameList=CommUtil.arrayToList((javaExceptionalName).split(Basic.SPLIT_STRING));
//
	
	public  void searchQueryList() {
		
		System.out.println("say begin auto query web! starting ...");
		
		tree.removeAll();

		
		
		List<Query> querys = QueryList.getInstance().getQuerys();
		int qindex=0;
		
		
		
		for(Query query : querys){
			
			String searchID="A"+String.valueOf(qindex)+query.getQueryLevel()+query.getInforID();
			List<KeyWord> keyWords = query.getQueryKeyWords();
			String search = "";
			for(KeyWord keyWord : keyWords){
				search += keyWord.getKeywordName() + " ";
			}
			search = search.trim();
			query.setSearchID(searchID);
			if (currentSearchID.equals("")|| currentSearchID==null) {
				currentSearchID=searchID;
			}else {
				currentSearchID=currentSearchID+searchID;
			}			
			txtCandidateSearch.setText(search);
			
			dosearch(query, searchID, search);
			}

		//清除query 准备下一轮 自动查询构造
		querys.clear();

		}

	private static void dosearch(Query query, String searchID, String search) {
		Timestamp starttime;
		String searchResultOutput="\n============\n";
		SearchResults sResults=new SearchResults();
		starttime=new Timestamp(System.currentTimeMillis());
		
		sResults.setSearchID(searchID);
			query.setTime(starttime);
			query.setIsbyuser(false);
			query.setUseKeywords(search);
			query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
		

			LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
			try {
				googlesearchList = apiCall.searchWeb(search);
				for (WEBResult webResult : googlesearchList) {
					String xml = webResult.getTitleNoFormatting();
					xml = xml.replaceAll("&quot;", "\"");
					xml.replaceAll("&#39;", "\'");
					searchResultOutput=searchResultOutput+"\n"+webResult.toString();
					
					TreeItem item = new TreeItem(tree, SWT.NULL);
					item.setText(xml);
					item.setData(webResult.getUrl());

					String compareContent=xml+" "+webResult.getContent();
				
					
					SearchNode sNode=new SearchNode();
					sNode.setTitle(xml);
					sNode.setLink(webResult.getUrl());
					sResults.getSearchNode().add(sNode);
					
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
					    contentItem.setText(content);
//					    contentItem.setData(webResult.getUrl());
					    contentItem.setData(null);
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
						
					TreeItem subitem=new TreeItem(item, SWT.NULL);
					subitem.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_RED));
					subitem.setText(myExceptioinName);
					item.setExpanded(true);
					}

					
				}

					
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			Timestamp endtime=new Timestamp(System.currentTimeMillis());
			
			query.setCosttime(endtime.getTime()-starttime.getTime());
		
		// 需要保存关键词和当前cache到数据库中：
			DatabaseUtil.addKeyWordsToDataBase(query);
			for (SearchNode snNode : sResults.getSearchNode()) {
				DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
			}
				searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
				FileHelper.appendContentToFile("result.txt", searchResultOutput);
	}

	
	public int getCurrentActionID() {
		return currentActionID;
	}

	public void setCurrentActionID(int currentActionID) {
		this.currentActionID = currentActionID;
	}

	
	private void doTestTree()
	{
		//由于GOOGLE 封掉了IP  没法测试  使用这组测试数据
		txtSearchBox.setText("printStackTrace java.lang.Throwable ClassCastException");
		String search=txtSearchBox.getText();
		List<WEBResult> webResults=new ArrayList<>();
		
		WEBResult tResult1=new WEBResult();
		tResult1.setTitleNoFormatting("ClassCastException Throwable (Java Platform SE 7 ) - Oracle Documentation");
		tResult1.setUrl("http://docs.oracle.com/javase/7/docs/api/java/lang/Throwable.html");
		webResults.add(tResult1);
		
		WEBResult tResult2=new WEBResult();
		tResult2.setTitleNoFormatting("Java.lang.Throwable.printStackTrace() Method Example");
		tResult2.setUrl("http://www.tutorialspoint.com/java/lang/throwable_printstacktrace.htm");
		webResults.add(tResult2);
		
		WEBResult tResult3=new WEBResult();
		tResult3.setTitleNoFormatting("Class java.lang.Throwable");
		tResult3.setUrl("http://pic.dhe.ibm.com/infocenter/adiehelp/v5r1m1/topic/com.sun.api.doc/java/lang/Throwable.html");
		webResults.add(tResult3);
		
		WEBResult tResult4=new WEBResult();
		tResult4.setTitleNoFormatting("Class java.lang.Throwable ArrayIndexOutOfBoundsException");
		tResult4.setUrl("http://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/api/java.lang.Throwable.html");
		webResults.add(tResult4);
		
		WEBResult tResult5=new WEBResult();
		tResult5.setTitleNoFormatting("ArithmeticException Throwable | Android Developers");
		tResult5.setUrl("http://developer.android.com/reference/java/lang/Throwable.html");
		webResults.add(tResult5);
		
		
		String searchResultOutput="\n============\n";
		SearchResults sResults=new SearchResults();
		
		
					for (WEBResult webResult : webResults) {
					String xml = webResult.getTitleNoFormatting();
					xml = xml.replaceAll("&quot;", "\"");
					searchResultOutput=searchResultOutput+"\n"+webResult.toString();
					
					TreeItem item = new TreeItem(tree, SWT.NONE);

					String content=xml+" "+webResult.getContent();
				
					item.setText(xml);
					item.setData(webResult.getUrl());
					
					SearchNode sNode=new SearchNode();
					sNode.setTitle(xml);
					sNode.setLink(webResult.getUrl());
					sResults.getSearchNode().add(sNode);
					
					//后续在这里适当过滤一下  如果有异常名字则显示出来   或者 是 
//					List<String> tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
					content=content+search;
					List<String> tempContent=CommUtil.arrayToList(content.split(Basic.SPLIT_STRING));
					
					String boldWords="";
					
					for(String str: tempContent)
					
					{
					for (String jestr : Basic.javaExceptionalNameList)
					{
						if (str.equals(jestr))
						{
							if (boldWords.equals("")) {
								boldWords=jestr;
							}else {
								
							boldWords=boldWords+";"+jestr;
							}
						}
					}
					}
					
					if (!boldWords.equals("")) {
					TreeItem subitem=new TreeItem(item, SWT.NONE);
					subitem.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_RED));
					subitem.setText(boldWords);
					subitem.setData(item.getData());
					item.setExpanded(true);

					}

				}

			

			
			for (SearchNode snNode : sResults.getSearchNode()) {
				DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
			}

		
		
	}
	
	
//	
//	private void doSimpleSearch() {
//		tree.removeAll();
//		String queryText = txtSearch.getText().trim();
//		// list.add("key words:"+queryText);
//		// list.add("search results:");
//		//
//		if (part instanceof HelpSeekingSolutionView) {
//			HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
//			// v.getMyBrower().setNewUrl(
//			// "https://www.google.com/search?newwindow=1&q="+queryText);
//			// https://www.google.com.hk/#newwindow=1&q=
//			v.getMyBrower().setNewUrl(
//					"http://www.baidu.com/s?wd=" + queryText);
//		}
//		// "https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q="
//		
//		Query query=new Query();
//		query.setInforID(getCurrentActionID());
//		Timestamp starttime=new Timestamp(System.currentTimeMillis());
//		query.setTime(starttime);
//		query.setIsbyuser(true);
//		query.setQueryLevel(QueryLevel.Middle);
//		query.setUseKeywords(queryText);
//		query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
//		String searchID="P"+query.getInforID();
//		query.setSearchID(searchID);
//					
//		String searchResultOutput="\n============\n";
//		
//		SearchResults sResults=new SearchResults();
//		sResults.setSearchID(searchID);
//
//		LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
//		try {
//			googlesearchList = apiCall.searchWeb(queryText);
//			for (WEBResult webResult : googlesearchList) {
//				String xml = webResult.getTitleNoFormatting();
//				xml = xml.replaceAll("&quot;", "\"");
//				
//				
//				String useString="\n[Filter this item:]\n";
//
//				// 去除无关的项目 （采用标题中文字匹配）
//				List<String> tempListforQurey=CommUtil.stringToList(queryText,Basic.SPLIT_STRING);
//				Collections.sort(tempListforQurey);
//				String tempString=webResult.getTitleNoFormatting()+webResult.getContent();
//				List<String> tempListSearch=CommUtil.stringToList(tempString,Basic.SPLIT_STRING);
//				Collections.sort(tempListSearch);
//				
//				if (Collections.disjoint(tempListforQurey, tempListSearch)) {
////					System.out.println("no disjoint");
//					searchResultOutput=searchResultOutput+useString+webResult.toString();
//					continue;
//				}
//				
//				useString="\n[Use item:]\n";
//				
//				searchResultOutput=searchResultOutput+useString+webResult.toString();
//		
////				System.out.println("disjoint");
//				
//				//list.add(xml);
//				TreeItem item = new TreeItem(tree, SWT.NONE);
//				item.setText(xml);
//				item.setData(webResult.getUrl());
//				SearchNode sNode=new SearchNode();
//				sNode.setTitle(xml);
//				sNode.setLink(webResult.getUrl());
//				sResults.getSearchNode().add(sNode);
//				
//				searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
//				FileHelper.appendContentToFile("result.txt", searchResultOutput);
//				
//				
//			}
//
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		Timestamp endtime=new Timestamp(System.currentTimeMillis());
//		
//		query.setCosttime(endtime.getTime()-starttime.getTime());
//		setCurrentSearchID(query.getSearchID());
//// 需要保存关键词和当前cache到数据库中：
//		DatabaseUtil.addKeyWordsToDataBase(query);
//		for (SearchNode snNode : sResults.getSearchNode()) {
//			DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
//			
//		}
//	}
//	

	

}
