package cn.edu.fudan.se.helpseeking.views;

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

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.BrowserIDBean;
import cn.edu.fudan.se.helpseeking.bean.HistoryUrlSearch;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.NewWebUseInfo;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

public class HelpSeekingBrowserView extends ViewPart {

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
	private SashForm sashForm;
	private Composite urlpartComposite;
	private Button preTopicBtn;
	private Button succTopicBtn;
	private Label idlabel;
	private Text  topicContentText;
	public Text getTopicContentText() {
		return topicContentText;
	}

	

	private static CTabFolder tabFolder;
	private static CTabItem tabItem;

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		
		
		 sashForm = new SashForm(arg0, SWT.HORIZONTAL);
		 sashForm.setVisible(Basic.Visualize_flage);
		//url list 
		  urlpartComposite=new Composite(sashForm, SWT.BORDER);
		 
		  urlpartComposite.setLayout(new FillLayout());
		  
		  urlpartComposite.setVisible(Basic.Visualize_flage);
		  
		  SashForm urlSashForm=new SashForm(urlpartComposite, SWT.VERTICAL);
		  
		  urlSashForm.setVisible(Basic.Visualize_flage);
		  
		  Composite urllistComposite=new Composite(urlSashForm, SWT.BORDER);
		  
		  urllistComposite.setLayout(new GridLayout(9, false));
			urllistComposite.setVisible(Basic.Visualize_flage);
		  
		  preTopicBtn=new Button(urllistComposite,SWT.BORDER);
		  preTopicBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,1, 1));
		  preTopicBtn.setText("<");
		  preTopicBtn.setToolTipText("Previous Topic");
		  
		  preTopicBtn.setVisible(Basic.Visualize_flage);
		  
		  preTopicBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
//				MessageDialog
//				.openInformation(PlatformUI.getWorkbench()
//						.getActiveWorkbenchWindow().getShell(),
//						"Coding",
//						"Sorry! Please wait for a while!");

				if (historyUrlSearch.size()>0) {
					
	                 int id=(historyid-1)<0?0:(historyid-1);
	                 historyid=historyid-1;
	                 idlabel.setText("ID: "+id);
	                 doGenUrlTree(historyUrlSearch.get(id).getTopicName(), historyUrlSearch.get(id).getWebpageList()	, historyUrlSearch.get(id).getSearchList());
	             	
	                 NewWebUseInfo nwuiInfo=new NewWebUseInfo();
						
						nwuiInfo.setTopicName(historyUrlSearch.get(id).getTopicName());
						
						nwuiInfo.setTopicId(historyUrlSearch.get(id).getTopicId());
						
						
						nwuiInfo.setOpenTime(new Timestamp(System.currentTimeMillis()));
						
						DatabaseUtil.addNewWebUseInfo(nwuiInfo);
	                 
	                 if (id==0) {
							
							preTopicBtn.setEnabled(false);
						}			
	                 succTopicBtn.setEnabled(true);
				}
				
			
			
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		  
		  idlabel=new Label(urllistComposite, SWT.NONE);
		  idlabel.setVisible(Basic.Visualize_flage);
		 GridData gd_label = new GridData(SWT.FILL, SWT.FILL, true, true,2, 1);
		 idlabel.setText("ID:     ");
			
		  
		  topicContentText = new Text(urllistComposite, SWT.BORDER | SWT.WRAP
					| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL
					| SWT.MULTI);
		  GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, true,5, 1);
			gd_txtSearch.heightHint = -1;
			gd_txtSearch.widthHint = -1;
			topicContentText.setLayoutData(gd_txtSearch);
			topicContentText.setForeground(SWTResourceManager.getColor(255, 0, 0));
			topicContentText.setEditable(false);
			topicContentText.setVisible(Basic.Visualize_flage);
			
			succTopicBtn=new Button(urllistComposite,SWT.BORDER);
			succTopicBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,1, 1));
			succTopicBtn.setText(">");
			succTopicBtn.setToolTipText("Succeeding Topic");
			succTopicBtn.setVisible(Basic.Visualize_flage);

			
			
			succTopicBtn.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
//					UseResultsRecord urr=new UseResultsRecord();
//					urr.setTitle("hello click url!");
//					urr.setUrl(CommUtil.getPluginCurrentPath()+"foamtreetest.html");
//				  openNewTabByURL(urr);
//					MessageDialog
//					.openInformation(PlatformUI.getWorkbench()
//							.getActiveWorkbenchWindow().getShell(),
//							"Coding",
//							"Sorry! Please wait for a while!");
         if (historyUrlSearch.size()>0) {
	
	                 int id=(historyid+1)>currentHistoryUrlSearchID?currentHistoryUrlSearchID:(historyid+1);
					historyid=historyid+1;
					 idlabel.setText("ID: "+id);
	                 doGenUrlTree(historyUrlSearch.get(id).getTopicName(),historyUrlSearch.get(id).getWebpageList()	, historyUrlSearch.get(id).getSearchList());
							
	                 NewWebUseInfo nwuiInfo=new NewWebUseInfo();
						
						nwuiInfo.setTopicName(historyUrlSearch.get(id).getTopicName());
						nwuiInfo.setTopicId(historyUrlSearch.get(id).getTopicId());
					
						
						nwuiInfo.setOpenTime(new Timestamp(System.currentTimeMillis()));
						
						DatabaseUtil.addNewWebUseInfo(nwuiInfo);
	             
	                 if (id==currentHistoryUrlSearchID) {
								succTopicBtn.setEnabled(false);
								
							}			
							preTopicBtn.setEnabled(true);
				}
				
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			urlTree=new Tree(urlSashForm, SWT.BORDER);
			urlTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,7,1));
			urlTree.setVisible(Basic.Visualize_flage);
			
			 urlSashForm.setWeights(new int[] {100, 400});

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
						UseResultsRecord urr=(UseResultsRecord) item.getData();
						if(urr!=null)
						{
							//current compostie
//							openNewTabByURL(urr);
							
							//eclipse editor aero
							//获取4位时间整数
							Calendar c = Calendar.getInstance();
							  c.setTimeInMillis(new Date().getTime());
							long currentBrowserID=c.getTimeInMillis();
					          openNewURlinBrower(urr,currentBrowserID);
						
							
							
							
						}
						
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			
			
//			tabFolder=new CTabFolder(sashForm, SWT.BORDER);
			
//		     sashForm.setWeights(new int[] {400, 100});


	}

	
protected void openNewURlinBrower(UseResultsRecord urls, long currentBrowserID) 
{
		
	 try {
         IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
         IWebBrowser browser = support.createBrowser("OpenWebsite"+String.valueOf(currentBrowserID));
         BrowserIDBean newbrowserid=new BrowserIDBean();
         newbrowserid.setId(browser.getId());
//         FDUHelpSeekingPlugin pluginInstance=FDUHelpSeekingPlugin.getINSTANCE();
//		pluginInstance.getCurrentBrowserIDs().add(newbrowserid);

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
	
	
	public void genUrlTree(String currentTopicName, List<WEBPageBean> list, List<KeyWord> searchList, String searchId, String topicId) {
		HistoryUrlSearch hus=new HistoryUrlSearch();
		hus.setSearchList(searchList);
		
		for (int i = 0; i < list.size(); i++) {
			String containsstr=genHightLightStr(list.get(i),searchList);
			list.get(i).setContainsStr(containsstr);
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
		succTopicBtn.setEnabled(true);
		preTopicBtn.setEnabled(true);
		idlabel.setText("ID: "+hus.getId());
		
		
		
		

		
		doGenUrlTree(currentTopicName,list, searchList);
		
		
	}

	private void doGenUrlTree(String topicname, List<WEBPageBean> list, List<KeyWord> searchList) {
		int R=CommUtil.randomInt(128, 0);
		int Y=CommUtil.randomInt(255, 0);
		int B=CommUtil.randomInt(255, 0);
		
		topicContentText.setText(topicname);
			
		  urlTree.removeAll();
		 urlTree.setForeground(SWTResourceManager.getColor(R, Y, B));
		 
		 
		for (int i = 0; i < list.size(); i++) {
			TreeItem urlTreeItem= new TreeItem(urlTree, SWT.NONE);
			UseResultsRecord urr=new UseResultsRecord();
			urr.setTitle(list.get(i).getTitle().trim());
			urr.setUrl(list.get(i).getUrl().trim());
			
			
			urr.setHightlightString(list.get(i).getContainsStr());
			
			
			urlTreeItem.setData(urr);
			urlTreeItem.setText(list.get(i).getTitle().trim());
	
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
