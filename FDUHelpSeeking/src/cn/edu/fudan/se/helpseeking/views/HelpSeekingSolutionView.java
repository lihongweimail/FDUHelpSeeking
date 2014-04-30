package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
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

	@Override
	public void createPartControl(Composite arg0) {
		arg0.setLayout(new FillLayout());

		tabFolder = new CTabFolder(arg0, SWT.NONE);		
		tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("Search Page");

		Composite SearchComposite = new Composite(tabFolder, SWT.NONE);
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
				tree.removeAll();
				String queryText = txtSearch.getText().trim();
				Query query = new Query();
				// query.setInforID(getCurrentActionID());
				Timestamp starttime = new Timestamp(System.currentTimeMillis());
				query.setTime(starttime);
				query.setIsbyuser(true);
				query.setQueryLevel(QueryLevel.Middle);
				query.setUseKeywords(queryText);
				query.makeCandidateKeywords(Cache.getInstance()
						.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
				String searchID = "P" + query.getInforID();
				query.setSearchID(searchID);

				dosearch(query, searchID, queryText);
			}
		});
		tree = new Tree(SearchComposite, SWT.FILL | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				openNewTabByURL(item.getText(), (String) item.getData());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		/*TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("人人");
		item.setData("www.renren.com");*/

		tabItem.setControl(SearchComposite);
		new Label(SearchComposite, SWT.NONE);
		tabFolder.setSelection(tabItem);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public static void openNewTabByURL(String title, String url) {
		final CTabItem tab = new CTabItem(tabFolder, SWT.CLOSE);
		tab.setText(title);

		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tabComposite.setLayoutData(BorderLayout.NORTH);
		tabComposite.setLayout(new GridLayout(2, false));
		SimpleBrower myBrower = new SimpleBrower();
		myBrower.setMyComposite(tabComposite);
		myBrower.createShow();
		myBrower.refreshBrowser();
		myBrower.setDisableButton(true);
		myBrower.setNewUrl(url);

		tab.setControl(tabComposite);
		
		tabFolder.setSelection(tab);
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
				List<WEBResult> googlesearchList = apiCall.searchWeb(search);
				for (WEBResult webResult : googlesearchList) {
					String xml = webResult.getTitleNoFormatting();
					xml = xml.replaceAll("&quot;", "\"");
					xml.replaceAll("&#39;", "\'");
					xml.replaceAll("<b>", " ");
					xml.replaceAll("</"," ");
					xml.replaceAll("b>","");
					
					searchResultOutput=searchResultOutput+"\n"+webResult.toString();
					
					TreeItem item = new TreeItem(tree, SWT.NONE);
					
					item.setText(xml.length()>50?xml.substring(0,47)+"...":xml);
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
					    contentItem.setText(content.length()>50?content.substring(0,47)+"...":content);
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

}
