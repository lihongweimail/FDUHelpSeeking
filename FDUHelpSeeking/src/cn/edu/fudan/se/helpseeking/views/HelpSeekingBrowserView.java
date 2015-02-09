package cn.edu.fudan.se.helpseeking.views;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.htmlparser.tags.LabelTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.eclipse.ui.internal.browser.*;

import swing2swt.layout.BorderLayout;
import urlWordExtract.GetT;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.BrowserIDBean;
import cn.edu.fudan.se.helpseeking.bean.HistoryUrlSearch;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.NewWebUseInfo;
import cn.edu.fudan.se.helpseeking.bean.TopicWEBPagesBean;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

public class HelpSeekingBrowserView extends ViewPart {
	
	public static final String ID="cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView";

	private static  String baseUrl;
	
	public HelpSeekingBrowserView() {
		super();
		part = FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
	}

	static IViewPart part;
	AmAssitBrowser myBrowser;
	
	
	public AmAssitBrowser getMyBrowser() {
		return myBrowser;
	}

	public void setMyBrowser(AmAssitBrowser myBrowser) {
		this.myBrowser = myBrowser;
	}
	
	private static Tree urlTree;
	private static Tree urlTreeUnselect;
	private SashForm sashForm;
	
	IViewPart overviewpart;
	
	

	private static CTabFolder tabFolder;
	
	
	
	
	
	public static Tree getUrlTree() {
		return urlTree;
	}

	public static void setUrlTree(Tree urlTree) {
		HelpSeekingBrowserView.urlTree = urlTree;
	}

	public static Tree getUrlTreeUnselect() {
		return urlTreeUnselect;
	}

	public static void setUrlTreeUnselect(Tree urlTreeUnselect) {
		HelpSeekingBrowserView.urlTreeUnselect = urlTreeUnselect;
	}

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		
		
		 sashForm = new SashForm(arg0, SWT.VERTICAL);
			
		 Composite urlComposite = new Composite(sashForm, SWT.NONE);
			// SearchComposite.setLayoutData(BorderLayout.CENTER);
		 urlComposite.setLayout(new GridLayout(10, true));
		 
		 Text filetrLabel=new Text(urlComposite, SWT.MULTI | SWT.WRAP |SWT.None);
		 filetrLabel.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
		 filetrLabel.setText("Filtered Results:");

			urlTree=new Tree(urlComposite, SWT.BORDER);
			urlTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,9,1));
			
			 urlTree.setForeground(SWTResourceManager.getColor(0, 0, 0));
				
			 TreeItem welcomeI=new TreeItem(urlTree, SWT.NONE);
			 welcomeI.setText("Welcome");
			 TreeItem toI=new TreeItem(urlTree, SWT.NONE);
			 toI.setText("to");
			 TreeItem helpseekingI=new TreeItem(urlTree, SWT.NONE);
			 helpseekingI.setText("HelpSeeking");
			 TreeItem pluginI=new TreeItem(urlTree, SWT.NONE);
			 pluginI.setText("Plugin");
			 TreeItem toolI=new TreeItem(urlTree, SWT.NONE);
			 toolI.setText("Tool");
			 			
				urlTree.addSelectionListener(new SelectionListener() {
					
					@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				UseResultsRecord urr = (UseResultsRecord) item.getData();
				if (urr != null) {
					// current compostie
					// openNewTabByURL(urr);

					// eclipse editor aero
					// 获取4位时间整数
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(new Date().getTime());
					long currentBrowserID = c.getTimeInMillis();
					openNewURlinBrower(urr, currentBrowserID);

						try {
							PlatformUI
									.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.showView(
											"cn.edu.fudan.se.helpseeking.views.HelpSeekingWebPageOverviewView");
						} catch (PartInitException e1) {
							// TODO Auto-generated catch block
							System.out.println("please open OVerview view.");
						}

						overviewpart = FDUHelpSeekingPlugin
								.getDefault()
								.getWorkbench()
								.getActiveWorkbenchWindow()
								.getActivePage()
								.findView(
										"cn.edu.fudan.se.helpseeking.views.HelpSeekingWebPageOverviewView");

						if ((overviewpart instanceof HelpSeekingWebPageOverviewView)) {
							HelpSeekingWebPageOverviewView bv = (HelpSeekingWebPageOverviewView) overviewpart;
							try {
								bv.genlistTree(urr.getUrl());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

					}
				
			}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			
				 Composite urlunselectComposite = new Composite(sashForm, SWT.NONE);
					// SearchComposite.setLayoutData(BorderLayout.CENTER);
				 urlunselectComposite.setLayout(new GridLayout(10, true));
				 
				 Text unselectLabel=new Text(urlunselectComposite, SWT.MULTI | SWT.WRAP  |SWT.None);
				 unselectLabel.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,1,1));
				 unselectLabel.setText("Unselelct Results:");

				 
				 urlTreeUnselect=new Tree(urlunselectComposite, SWT.BORDER);
				 urlTreeUnselect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,9,1));
					
				 urlTreeUnselect.setForeground(SWTResourceManager.getColor(0, 0, 0));
						
					 TreeItem welcomeI1=new TreeItem(urlTreeUnselect, SWT.NONE);
					 welcomeI1.setText("Welcome");
					 TreeItem toI1=new TreeItem(urlTreeUnselect, SWT.NONE);
					 toI1.setText("to");
					 TreeItem helpseekingI1=new TreeItem(urlTreeUnselect, SWT.NONE);
					 helpseekingI1.setText("HelpSeeking");
					 TreeItem pluginI1=new TreeItem(urlTreeUnselect, SWT.NONE);
					 pluginI1.setText("Plugin");
					 TreeItem toolI1=new TreeItem(urlTreeUnselect, SWT.NONE);
					 toolI1.setText("Tool");
					 			
					 urlTreeUnselect.addSelectionListener(new SelectionListener() {
							
							@Override
							public void widgetSelected(SelectionEvent e) {
								TreeItem item = (TreeItem) e.item;
								UseResultsRecord urr=(UseResultsRecord) item.getData();
								if(urr!=null)
								{
									//current compostie
//									openNewTabByURL(urr);
									
									//eclipse editor aero
									//获取4位时间整数
									Calendar c = Calendar.getInstance();
									  c.setTimeInMillis(new Date().getTime());
									long currentBrowserID=c.getTimeInMillis();
							          openNewURlinBrower(urr,currentBrowserID);
							          
										try {
											PlatformUI
													.getWorkbench()
													.getActiveWorkbenchWindow()
													.getActivePage()
													.showView(
															"cn.edu.fudan.se.helpseeking.views.HelpSeekingWebPageOverviewView");
										} catch (PartInitException e1) {
											// TODO Auto-generated catch block
											System.out.println("please open OVerview view.");
										}

										overviewpart = FDUHelpSeekingPlugin
												.getDefault()
												.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage()
												.findView(
														"cn.edu.fudan.se.helpseeking.views.HelpSeekingWebPageOverviewView");

										if ((overviewpart instanceof HelpSeekingWebPageOverviewView)) {
											HelpSeekingWebPageOverviewView bv = (HelpSeekingWebPageOverviewView) overviewpart;
											try {
												bv.genlistTree(urr.getUrl());
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

										}
       
							          
									
									
									
								}
								
								
							}
							
							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub
								
							}
						});


				
				
				 sashForm.setWeights(new int[] {200, 300});


	}

	
      IWebBrowser browser; 
//	AmAssitBrowser browser; //2015.1.28 测试
// AmAssitBrowser mytestBrowser;  2015.1.28 测试
      final String highlightJScode=FileHelper
		.getHTMLContent(CommUtil.getFDUHelpseekingPluginWorkingPath()
				+ "/javascripthiglight.js");
      
      ///Users/Grand/temp/FDUHelpSeeking/FDUHelpSeeking/foamtree/javascripthiglight.js
		
protected void openNewURlinBrower(UseResultsRecord urls, long currentBrowserID) 
{
		
	 try {
		 
         IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
//         browser = support.createBrowser("OpenWebsite"+String.valueOf(currentBrowserID));   2015.1.28 测试
//         browser.mysetId(currentBrowserID);    2015.1.28 测试
         browser = support.createBrowser("OpenWebsite"+String.valueOf(currentBrowserID));
         BrowserIDBean newbrowserid=new BrowserIDBean();
         newbrowserid.setId(browser.getId());
         FDUHelpSeekingPlugin pluginInstance=FDUHelpSeekingPlugin.getINSTANCE();
		 pluginInstance.getCurrentBrowserIDs().add(newbrowserid);
		 

			NewWebUseInfo nwuiInfo=new NewWebUseInfo();
			
			nwuiInfo.setWebURL(urls.getUrl());
			String tid="";
			if ((historyUrlSearch.get(currentHistoryUrlSearchID).getTopicId())!=null)  
			   tid=historyUrlSearch.get(currentHistoryUrlSearchID).getTopicId();
			
			nwuiInfo.setTopicId(tid);
			nwuiInfo.setTopicName(historyUrlSearch.get(currentHistoryUrlSearchID).getTopicName());
			nwuiInfo.setOpenTime(new Timestamp(System.currentTimeMillis()));
						DatabaseUtil.addNewWebUseInfo(nwuiInfo);
         
         
         System.out.println("browser id: "+"OpenWebsite"+String.valueOf(currentBrowserID) );
      
//         browser.openURL(new URL((urls.getUrl())));    
//        browser.openURL(new URL("file:///Users/Grand/Downloads/testforhighlightdo.html"));    //  2.2测试本地文件

         //         browser.setNewUrl(urls.getUrl());   // 2015.1.28 测试   
         
        
         final String urlString=urls.getUrl();
         
//	        (new URL("file:///Users/Grand/Downloads/testforhighlightdo.html"));
         
         final URL openUrl=new URL(urls.getUrl());
         //考虑用currentBrowserID作为本地文件名称
         System.out.println("the file name of URL is:  "+openUrl.getFile().toLowerCase().toString());
         final String openUrlFilepath=CommUtil.getFDUHelpseekingPluginWorkingPath()+"/"+String.valueOf(currentBrowserID)+".html";
       
         final String openUrlFileWithProtocolString="file://"+openUrlFilepath;
         final URL openUrlFileWithProtocol=new URL(openUrlFileWithProtocolString);
         
		 
		 
		 Job job = new Job("jobname获取数据WEB页面"){
		 protected IStatus run(IProgressMonitor monitor){
		 // 在此添加获取数据的代码

			 //添加的步骤:
			 //1、下载HTML网页
			 //2、加塞高亮代码   //高亮词汇和颜色值： foamtreeKeyWordsSet  高亮代码示例
			 //从传来的foamtree的词汇中获得词和颜色构造如下的代码作为串
//			 function onloadinghighlight()
//			 {
//			 searchPrompt("unpredictable", false, "pink", "yellow");
//			 searchPrompt("highlight", true,"red","yellow");
//			 }
//			 </script>

			 //3、写入本地文件
			 //4、给浏览器本地文件地址，展示
			 
				String  keywordsandcolorsJScode= "function onloadinghighlight()\n"
						+ "{\n";
				
//						+ " searchPrompt(\"unpredictable\", false, \"pink\", \"yellow\");\n"
//						+ " searchPrompt(\"highlight\", true,\"red\",\"yellow\");\n"

				for (int i = 0; i < foamtreeKeyWordsSet.size(); i++) {



					String labels = foamtreeKeyWordsSet.get(i).getKeywordName();
					labels= CommUtil.getNewSimpleWords(labels);
					String colors = foamtreeKeyWordsSet.get(i).getKeywordColorName();
					
					//如何求colors的对比色，反色呢？
					
					
					if (labels.contains(".")) {
						labels = labels.replaceAll("[.]", " ");
					}

					if (labels.contains("(")) {
						labels = labels.replaceAll("[(]", " ");
					}
					if (labels.contains(")")) {
						labels = labels.replaceAll("[)]", " ");
					}
					
					keywordsandcolorsJScode=keywordsandcolorsJScode+" searchPrompt(\""+labels+"\", false, \""+colors+"\", \"LightYellow\");\n";


				}
				
			keywordsandcolorsJScode= keywordsandcolorsJScode
						+ " }\n"
						+ " </script>\n";
				 
			 

			 
			 try {
				doGenHighlightHTML(urlString, true, highlightJScode, keywordsandcolorsJScode, openUrlFilepath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	 
			 
		 Display.getDefault().asyncExec(new Runnable(){
		 public void run(){
		 // 在此添加更新界面的代码
	         

			 try {
				browser.openURL(openUrlFileWithProtocol);
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			 
//  2.2测试本地文件
//			   try {
//                  
//				   browser.openURL(new URL("file:///Users/Grand/Downloads/testforhighlightdo.html"));
//				} catch (PartInitException | MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}    
//				   
	
			 
			 
		 }
		 });
		 return Status.OK_STATUS;
		 }
		 };
		 job.setRule(Schedule_RULE);
		 job.schedule();
		 
		 

        
                
     } catch (PartInitException | MalformedURLException p) {
         p.printStackTrace();
     }


  }
private static void print(String msg, Object... args) {
	System.out.println(String.format(msg, args));
}


public static void doGenHighlightHTML(String url, Boolean isUrl,
		String codeString, String highlightwordscolorsCode, String newfilename)
		throws IOException {
	// String url = "http://www.nsftools.com/misc/SearchAndHighlight.htm";
	// url 是一个本地文件路径 则 isUrl=false ； 如果是 网页 isUrl为网络地址
	print("Fetching %s...", url);

	// 测试用没有 script标签 /Users/Grand/Downloads/testforhighlight.html
	// /Users/Grand/Downloads/JavaScript Text Highlighting.html

	Document doc;
	String charset="UTF-8";

	if (!isUrl) {

		// 文件

		// File input = new
		// File("/Users/Grand/Downloads/testforhighlight.html");
		File input = new File(url);
		doc = Jsoup.parse(input, "UTF-8");
		charset="UTF-8";
	} else {

		// 网页
		charset=Jsoup.connect(url).response().charset();

		//这个时延不好调整
		doc = Jsoup.connect(url).timeout(160000).get();
		
	}

	Elements body = doc.select("body");
	Element head = doc.select("head").first();

	Elements javascript = head.select("script");
	
	codeString=codeString.replace("</script>", highlightwordscolorsCode);
	head.append(codeString);
	
	if (!body.hasAttr("onload")) {
		// print("add attribution onload");
		body.attr("onload", "onloadinghighlight();");
		// print("立即输出 body content : %s ", body.attr("onload"));//
		// print("all document: %s ", doc.html());
	} else {
		String onloadString = body.attr("onload");
		if (onloadString.equals("")) // 没有启动脚本
		{
			// 新增
			body.attr("onload", "onloadinghighlight();");

		} else {
			// 添加
			onloadString = onloadString.replace(";",
					",onloadinghighlight();");
			body.attr("onload", onloadString);
			// print("立即输出 body content : %s ", body.attr("onload"));
		}

	}

	String html = doc.html();

	if(charset==null)
	{
		charset="UTF-8";
	}

	Writer writer = new PrintWriter(
			newfilename, charset);
	writer.write(html);
	writer.close();
}

//防止两个同类job同时执行 myjob1.setRule(Schedule_RULE);
	// myjob2.setRule(Schedule_RULE);
	private static ISchedulingRule Schedule_RULE = new ISchedulingRule() {
		public boolean contains(ISchedulingRule rule) {
			return this.equals(rule);
		}

		public boolean isConflicting(ISchedulingRule rule) {
			return this.equals(rule);
		}
	};
//
//	// private void perform(){
//	// Job job = new Job("jobname获取数据"){
//	// protected IStatus run(IProgressMonitor monitor){
//	// // 在此添加获取数据的代码
//	// Display.getDefault().asyncExec(new Runnable(){
//	// public void run(){
//	// // 在此添加更新界面的代码
//	// }
//	// });
//	// return Status.OK_STATUS;
//	// }
//	// };
//	// job.setRule(Schedule_RULE);
//	// job.schedule();
//	// }



public void clawhtmlwebpage(UseResultsRecord urls) {






}



//	type:   auto   manual 
	public static void openNewTabByURL(UseResultsRecord urr) {		
			if (urr.getUrl()!=null) {
			if (!urr.getUrl().equals("")) {

				final CTabItem tab = new CTabItem(tabFolder, SWT.CLOSE);
				tab.setText(urr.getTitle());
//				Timestamp firstOpenTime=new Timestamp(System.currentTimeMillis());
				Composite tabComposite = new Composite(tabFolder, SWT.NONE);
				tabComposite.setLayoutData(BorderLayout.NORTH);
				tabComposite.setLayout(new GridLayout(7, false));
				
				AmAssitBrowser tabBrowser = new AmAssitBrowser();
				tabBrowser = new AmAssitBrowser();
				tabBrowser.setMyComposite(tabComposite);
				tabBrowser.setDisplay(tabComposite.getDisplay());
				tabBrowser.createShow();				
				tabBrowser.getBrowser().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,7, 1));				
				//tabBrowser.refreshBrowser();
//				  String userpath=CommUtil.getPluginCurrentPath()+"foamtreetest.html";
//					System.out.println("browser view path:" + userpath);
//				  //String userpath="about:blank";
				
				System.out.println("browser view: open url is : "+ urr.getUrl().toString());
				tabBrowser.setNewUrl(urr.getUrl().toString()); //urr.geturl();
				tabBrowser.getMyComposite().pack();

				tab.setControl(tabComposite);

				tabFolder.setSelection(tab);
			}
			}
				
	}

	
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private List<HistoryUrlSearch> historyUrlSearch=new ArrayList<HistoryUrlSearch>();
	private int currentHistoryUrlSearchID=0;
	private int historyid=0;
	public List<KeyWord> foamtreeKeyWordsSet= new ArrayList<KeyWord>();;
	
	public void genUrlTree( String currentTopicName,  List<WEBPageBean> list, List<KeyWord> searchList, String searchId, String topicId, List<KeyWord> foamtreeKeyWords) {
		
		foamtreeKeyWordsSet=foamtreeKeyWords;
		List<WEBPageBean> allwebpageListfirstPart=new ArrayList<WEBPageBean>();
		List<WEBPageBean> allwebpageListsecondPart=new ArrayList<WEBPageBean>();
		
		
		
		HistoryUrlSearch hus=new HistoryUrlSearch();
		
		hus.setSearchList(searchList);

		
		    
			
			for (int j = 0; j < list.size(); j++) {
			
				
					if (list.get(j).isSelect()) {
						allwebpageListfirstPart.add(list.get(j));
						
			}else {
				allwebpageListsecondPart.add(list.get(j));
			}
					
					
			
			
			
		}
		

		
		
		
		hus.setWebpageList(list);
		hus.setTopicName(currentTopicName);
		hus.setCoutpage(historyUrlSearch.size());
		hus.setSearchID(searchId);
		
		hus.setTopicId(topicId);
				
		
		historyUrlSearch.add(hus);
		
		
		
		
		
		currentHistoryUrlSearchID=historyUrlSearch.size()-1;
		historyid=currentHistoryUrlSearchID;
		
		hus.setId(historyid);
		
		
//		List<WEBPageBean> alllist=new ArrayList<WEBPageBean>();
//		for (int i = 0; i < allwebpageListfirstPart.size(); i++) {
//			alllist.add(allwebpageListfirstPart.get(i));
//			
//		}
//		for (int i = 0; i < allwebpageListsecondPart.size(); i++) {
//			alllist.add(allwebpageListsecondPart.get(i));
//		}

		
		doGenUrlTree(urlTree, allwebpageListfirstPart);
		doGenUrlTree(urlTreeUnselect, allwebpageListsecondPart);
		
		
	}

	public void doGenUrlTree(Tree myTree,  List<WEBPageBean> list) {
		int R=CommUtil.randomInt(128, 0);
		int Y=CommUtil.randomInt(255, 0);
		int B=CommUtil.randomInt(255, 0);
		
if (list==null) {
	return;
}

		myTree.removeAll();
		myTree.setForeground(SWTResourceManager.getColor(R, Y, B));
		 
		 List<WEBPageBean> displaylist=new ArrayList<WEBPageBean>();
		 
		 for (int i = 0; i < list.size(); i++) {
			displaylist.add(list.get(i));
			
		}
		 
		 
		 
		 
		for (int i = 0; i < list.size(); i++) {
			TreeItem urlTreeItem= new TreeItem(myTree, SWT.NONE);
			UseResultsRecord urr=new UseResultsRecord();
			urr.setTitle(list.get(i).getTitle().trim());
			urr.setUrl(list.get(i).getUrl().trim());
			
			
			urr.setHightlightString(list.get(i).getContainsStr());
			
			
			urlTreeItem.setData(urr);
			urlTreeItem.setText((i+1)+":  "+list.get(i).getTitle().trim());
	
			urlTreeItem.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_BLACK));
			
			TreeItem hightlightWordsitem=new TreeItem(urlTreeItem, SWT.NONE);
			hightlightWordsitem.setData(null);
			hightlightWordsitem.setText(urr.getHightlightString());
			if (!urr.getHightlightString().equals("----")) {
				hightlightWordsitem.setForeground(Display.getDefault()
						.getSystemColor(SWT.COLOR_RED));
			}
			

			
			
			TreeItem urlTreeItemofItemtitle =new TreeItem(urlTreeItem, SWT.NONE);
			urlTreeItemofItemtitle.setData(urr);
			urlTreeItemofItemtitle.setText(list.get(i).getUrl().trim());
			
			
		
			
			TreeItem urlTreeItemofItemsummary =new TreeItem(urlTreeItem, SWT.NONE);
			urlTreeItemofItemsummary.setData(urr);
			urlTreeItemofItemsummary.setText(list.get(i).getSummary().trim());
			urlTreeItemofItemsummary.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_BLACK));
			
			urlTreeItem.setExpanded(true);
		}
	}

	public String genHightLightStr(WEBPageBean webPageBean, List<KeyWord> searchList) {
		// TODO Auto-generated method stub
		String highlightstr="";
		if (searchList==null) {
			return "----";
		}
		for (int i = 0; i < searchList.size(); i++) {
			
			String[] tempsStrings=searchList.get(i).getKeywordName().split("[ .()]");
			if (tempsStrings!=null)
				if( tempsStrings.length>0) {
					for (int j = 0; j < tempsStrings.length; j++) {
						String temps=(tempsStrings[j]).toLowerCase().trim();
					if (!temps.equals("")) {
						if (webPageBean.getTitle().toLowerCase().trim().contains(temps)  
								|| webPageBean.getSummary().toLowerCase().trim().contains(temps)
								|| webPageBean.getContent().toLowerCase().trim().contains(temps)) {
							highlightstr=highlightstr+" "+temps;
						}
					}
				
					}

			}
			
			
			
		}
		if (highlightstr.equals("")) {
			highlightstr="----";
		}
		
		return highlightstr.trim();
	}
}
