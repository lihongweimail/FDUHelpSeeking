package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.eclipse.ui.internal.browser.*;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.BrowserIDBean;
import cn.edu.fudan.se.helpseeking.bean.HistoryUrlSearch;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.NewWebUseInfo;
import cn.edu.fudan.se.helpseeking.bean.TopicWEBPagesBean;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
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
		 urlComposite.setLayout(new GridLayout(2, false));
		 
		 Label filetrLabel=new Label(urlComposite, SWT.None);
		 filetrLabel.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,2,1));
		 filetrLabel.setText("Filtered Results:");

			urlTree=new Tree(urlComposite, SWT.BORDER);
			urlTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
			
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
				 urlunselectComposite.setLayout(new GridLayout(2, false));
				 
				 Label unselectLabel=new Label(urlunselectComposite, SWT.None);
				 unselectLabel.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,2,1));
				 unselectLabel.setText("Unselelct Results:");

				 
				 urlTreeUnselect=new Tree(urlunselectComposite, SWT.BORDER);
				 urlTreeUnselect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
					
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
	
	
protected void openNewURlinBrower(UseResultsRecord urls, long currentBrowserID) 
{
		
	 try {
		 
		 
		 
         IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
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
         browser.openURL(new URL((urls.getUrl())));
         
         
        
                
     } catch (PartInitException p) {
         p.printStackTrace();
     } catch (MalformedURLException p) {
         p.printStackTrace();
     }


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
	
	
	public void genUrlTree( String currentTopicName,  List<WEBPageBean> list, List<KeyWord> searchList, String searchId, String topicId) {
		
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
