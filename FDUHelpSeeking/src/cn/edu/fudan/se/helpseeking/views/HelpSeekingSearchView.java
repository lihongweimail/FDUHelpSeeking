package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

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

import org.eclipse.wb.swt.SWTResourceManager;

public class HelpSeekingSearchView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView"; //$NON-NLS-1$

	private Text txtSearch;
	//private static org.eclipse.swt.widgets.List list;
	private static Tree tree;
	private static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();

	public HelpSeekingSearchView() {
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
		arg0.setLayout(new BorderLayout(0, 0));

		Composite SearchComposite = new Composite(arg0, SWT.NONE);
		SearchComposite.setLayoutData(BorderLayout.NORTH);
		SearchComposite.setLayout(new GridLayout(2, false));
		
		txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtSearch.setText("hello world");
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1);
		gd_txtSearch.heightHint = 86;
		gd_txtSearch.widthHint = -1;
		txtSearch.setLayoutData(gd_txtSearch);

		Button btnSearchGoogle = new Button(SearchComposite, SWT.NONE);
		btnSearchGoogle.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 16, SWT.BOLD));
		btnSearchGoogle.setForeground(SWTResourceManager.getColor(0, 0, 0));
		btnSearchGoogle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//list.removeAll();
				doSimpleSearch();
	

			}
		});
		btnSearchGoogle.setText("Search");

	

		/*list = new org.eclipse.swt.widgets.List(arg0, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		list.setLayoutData(BorderLayout.CENTER);
		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.getSelectionIndex();
				if (part instanceof HelpSeekingSolutionView) {
					HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
					v.getMyBrower().setNewUrl(
							googlesearchList.get(list.getSelectionIndex())
									.getUrl());
					System.out.println("select item: "
							+ list.getSelectionIndex());
					System.out.println(googlesearchList.get(
							list.getSelectionIndex()).getUrl());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});*/
		
		tree = new Tree(arg0, SWT.BORDER | SWT.H_SCROLL	| SWT.V_SCROLL);		
		tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tree.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (part instanceof HelpSeekingSolutionView
						&& item.getData() != null) {
					HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
					v.getMyBrower().setNewUrl((String) item.getData());
					/*System.out.println("select item: "
							+ list.getSelectionIndex());
					System.out.println(googlesearchList.get(
							list.getSelectionIndex()).getUrl());*/
					
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

	public void setSearchValue(String search) {
		txtSearch.setText(search);
	}
	
	public static void searchQueryList() {
		tree.removeAll();
		TreeItem exception = new TreeItem(tree, SWT.NONE);
		exception.setText("Exception");
		TreeItem api = new TreeItem(tree, SWT.NONE);
		api.setText("API");
		TreeItem error = new TreeItem(tree, SWT.NONE);
		error.setText("Error");
		TreeItem other = new TreeItem(tree, SWT.NONE);
		other.setText("Other");		
		
		Timestamp starttime;
		
		List<Query> querys = QueryList.getInstance().getQuerys();
		int qindex=0;
		for(Query query : querys){
			
			String searchResultOutput="\n============\n";
			
			SearchResults sResults=new SearchResults();
			starttime=new Timestamp(System.currentTimeMillis());
			qindex=qindex+1;
			
			List<KeyWord> keyWords = query.getQueryKeyWords();
			String search = "";
			for(KeyWord keyWord : keyWords){
				search += keyWord.getKeywordName() + " ";
			}
			search = search.trim();
			if(!search.equals("")){
				if (part instanceof HelpSeekingSolutionView) {
					HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
					v.getMyBrower().setNewUrl(
							"http://www.baidu.com/s?wd=" + search);
				}
				
//				query.setInforID(getCurrentActionID());

				query.setTime(starttime);
				query.setIsbyuser(false);
//				query.setQueryLevel(QueryLevel.Middle);
				query.setUseKeywords(search);
				query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
				String searchID="A"+String.valueOf(qindex)+query.getQueryLevel()+query.getInforID();
				query.setSearchID(searchID);
				if (currentSearchID.equals("")|| currentSearchID==null) {
					currentSearchID=searchID;
				}else {
					currentSearchID=currentSearchID+searchID;
				}			
				
				
				
				sResults.setSearchID(searchID);
	

				LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
				try {
					googlesearchList = apiCall.searchWeb(search);
					for (WEBResult webResult : googlesearchList) {
						String xml = webResult.getTitleNoFormatting();
						xml = xml.replaceAll("&quot;", "\"");
						
						
						String useString="\n[Filter this item:]\n";
						
						//去除无关
						List<String> tempListforQurey=CommUtil.stringToList(search,Basic.SPLIT_STRING);
						Collections.sort(tempListforQurey);
						String tempString=webResult.getTitleNoFormatting()+webResult.getContent();
						List<String> tempListSearch=CommUtil.stringToList(tempString,Basic.SPLIT_STRING);
						Collections.sort(tempListSearch);
						
						if (Collections.disjoint(tempListforQurey, tempListSearch)) {
							searchResultOutput=searchResultOutput+useString+webResult.toString();
							continue;
						}
						
						useString="\n[Use item:]\n";
						
						searchResultOutput=searchResultOutput+useString+webResult.toString();
						
						
						TreeItem parent = null;
						String tag = keyWords.get(0).getTagName();
						if(tag.equals("Exception")){
							parent = exception;
						}else if(tag.equals("API")){
							parent = api;
						}else if(tag.equals("Error")){
							parent = error;
						}else{
							parent = other;
						}
						
						TreeItem item = new TreeItem(parent, SWT.NONE);
						if(query.getQueryLevel() == QueryLevel.High){
							item.setForeground(Display.getDefault()
									.getSystemColor(SWT.COLOR_RED));
						}						
						item.setText(xml);
						item.setData(webResult.getUrl());
						
						SearchNode sNode=new SearchNode();
						sNode.setTitle(xml);
						sNode.setLink(webResult.getUrl());
						sResults.getSearchNode().add(sNode);
						
					}

						
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				
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
		//清除query 准备下一轮 自动查询构造
		querys.clear();
		exception.setExpanded(true);
		api.setExpanded(true);
		error.setExpanded(true);
		other.setExpanded(true);	
	}

	public int getCurrentActionID() {
		return currentActionID;
	}

	public void setCurrentActionID(int currentActionID) {
		this.currentActionID = currentActionID;
	}

	private void doSimpleSearch() {
		tree.removeAll();
		String queryText = txtSearch.getText().trim();
		// list.add("key words:"+queryText);
		// list.add("search results:");
		//
		if (part instanceof HelpSeekingSolutionView) {
			HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
			// v.getMyBrower().setNewUrl(
			// "https://www.google.com/search?newwindow=1&q="+queryText);
			// https://www.google.com.hk/#newwindow=1&q=
			v.getMyBrower().setNewUrl(
					"http://www.baidu.com/s?wd=" + queryText);
		}
		// "https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q="
		
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
					
		String searchResultOutput="\n============\n";
		
		SearchResults sResults=new SearchResults();
		sResults.setSearchID(searchID);

		LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
		try {
			googlesearchList = apiCall.searchWeb(queryText);
			for (WEBResult webResult : googlesearchList) {
				String xml = webResult.getTitleNoFormatting();
				xml = xml.replaceAll("&quot;", "\"");
				
				
				String useString="\n[Filter this item:]\n";

				// 去除无关的项目 （采用标题中文字匹配）
				List<String> tempListforQurey=CommUtil.stringToList(queryText,Basic.SPLIT_STRING);
				Collections.sort(tempListforQurey);
				String tempString=webResult.getTitleNoFormatting()+webResult.getContent();
				List<String> tempListSearch=CommUtil.stringToList(tempString,Basic.SPLIT_STRING);
				Collections.sort(tempListSearch);
				
				if (Collections.disjoint(tempListforQurey, tempListSearch)) {
//					System.out.println("no disjoint");
					searchResultOutput=searchResultOutput+useString+webResult.toString();
					continue;
				}
				
				useString="\n[Use item:]\n";
				
				searchResultOutput=searchResultOutput+useString+webResult.toString();
		
//				System.out.println("disjoint");
				
				//list.add(xml);
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(xml);
				item.setData(webResult.getUrl());
				SearchNode sNode=new SearchNode();
				sNode.setTitle(xml);
				sNode.setLink(webResult.getUrl());
				sResults.getSearchNode().add(sNode);
				
				searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
				FileHelper.appendContentToFile("result.txt", searchResultOutput);
				
				
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Timestamp endtime=new Timestamp(System.currentTimeMillis());
		
		query.setCosttime(endtime.getTime()-starttime.getTime());
		setCurrentSearchID(query.getSearchID());
// 需要保存关键词和当前cache到数据库中：
		DatabaseUtil.addKeyWordsToDataBase(query);
		for (SearchNode snNode : sResults.getSearchNode()) {
			DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
			
		}
	}
	
	
	

}
