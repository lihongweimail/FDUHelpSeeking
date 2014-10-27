package cn.edu.fudan.se.helpseeking.views;

import java.sql.Timestamp;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
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
import org.eclipse.ui.internal.handlers.WizardHandler.New;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.TabRecord;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
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
		//url list 
		  urlpartComposite=new Composite(sashForm, SWT.BORDER);
		 
		  urlpartComposite.setLayout(new FillLayout());
		  
		  SashForm urlSashForm=new SashForm(urlpartComposite, SWT.VERTICAL);
		  
		  Composite urllistComposite=new Composite(urlSashForm, SWT.BORDER);
		  
		  urllistComposite.setLayout(new GridLayout(7, false));
			
		  
		  preTopicBtn=new Button(urllistComposite,SWT.BORDER);
		  preTopicBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,1, 1));
		  preTopicBtn.setText("<");
		  preTopicBtn.setToolTipText("Previous Topic");
		  preTopicBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MessageDialog
				.openInformation(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"Coding",
						"Sorry! Please wait for a while!");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		  
		  
		  topicContentText = new Text(urllistComposite, SWT.BORDER | SWT.WRAP
					| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL
					| SWT.MULTI);
		  GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, true,5, 1);
			gd_txtSearch.heightHint = -1;
			gd_txtSearch.widthHint = -1;
			topicContentText.setLayoutData(gd_txtSearch);
			topicContentText.setForeground(SWTResourceManager.getColor(255, 0, 0));
			
			succTopicBtn=new Button(urllistComposite,SWT.BORDER);
			succTopicBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,1, 1));
			succTopicBtn.setText(">");
			succTopicBtn.setToolTipText("Succeeding Topic");

			
			
			succTopicBtn.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
//					UseResultsRecord urr=new UseResultsRecord();
//					urr.setTitle("hello click url!");
//					urr.setUrl(CommUtil.getPluginCurrentPath()+"foamtreetest.html");
//				  openNewTabByURL(urr);
					MessageDialog
					.openInformation(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"Coding",
							"Sorry! Please wait for a while!");

				  
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			urlTree=new Tree(urlSashForm, SWT.BORDER);
			urlTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,7,1));
			
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
						openNewTabByURL(urr);					
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			
			
			tabFolder=new CTabFolder(sashForm, SWT.BORDER);
			
		     sashForm.setWeights(new int[] {200, 200});


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
				tabBrowser.refreshBrowser();
//				  String userpath=CommUtil.getPluginCurrentPath()+"foamtreetest.html";
//					System.out.println("browser view path:" + userpath);
//				  //String userpath="about:blank";
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

	public void genUrlTree(TreeItem[] sendoutURLlist) {
		
		int R=CommUtil.randomInt(128, 0);
		int Y=CommUtil.randomInt(255, 0);
		int B=CommUtil.randomInt(255, 0);
			
		  urlTree.removeAll();
		 urlTree.setForeground(SWTResourceManager.getColor(R, Y, B));
		 
		 
		for (int i = 0; i < sendoutURLlist.length; i++) {
			TreeItem urlTreeItem= new TreeItem(urlTree, SWT.NONE);
			UseResultsRecord urr=new UseResultsRecord();
			urr.setTitle(sendoutURLlist[i].getItem(0).getText());
			urr.setUrl(sendoutURLlist[i].getItem(1).getText());
			urlTreeItem.setData(urr);
			urlTreeItem.setText(sendoutURLlist[i].getItem(0).getText());
	
			urlTreeItem.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_RED));
			
			
			TreeItem urlTreeItemofItemtitle =new TreeItem(urlTreeItem, SWT.NONE);
			urlTreeItemofItemtitle.setData(urr);
			urlTreeItemofItemtitle.setText(sendoutURLlist[i].getItem(1).getText());
			
			TreeItem urlTreeItemofItemsummary =new TreeItem(urlTreeItem, SWT.NONE);
			urlTreeItemofItemsummary.setData(urr);
			urlTreeItemofItemsummary.setText(sendoutURLlist[i].getItem(2).getText());
			
			urlTreeItem.setExpanded(true);
		}
		
		
	}
}
