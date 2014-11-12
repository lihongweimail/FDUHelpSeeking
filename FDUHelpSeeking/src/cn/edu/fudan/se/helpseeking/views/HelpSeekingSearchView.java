package cn.edu.fudan.se.helpseeking.views;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.AutoSearchWordsStruct;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Basic.QueryLevel;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.bean.KeyWordsCandidates;
import cn.edu.fudan.se.helpseeking.bean.Query;
import cn.edu.fudan.se.helpseeking.bean.QueryList;
import cn.edu.fudan.se.helpseeking.bean.SearchNode;
import cn.edu.fudan.se.helpseeking.bean.SearchResults;
import cn.edu.fudan.se.helpseeking.bean.UseResultsRecord;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.preferences.PreferenceConstants;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;

public class HelpSeekingSearchView extends ViewPart {



	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView"; //$NON-NLS-1$

	private Label label;
	// private Text txtCandidateSearch;
	// private Text txtSearchBox;
	// private static org.eclipse.swt.widgets.List list;
	private static Tree searchResultsTree;
	private static Tree keywordsTree;
	private static List<WEBResult> googlesearchList = new ArrayList<WEBResult>();

	public HelpSeekingSearchView() {
		super();
		part = FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
	}

	static IViewPart part;

	String username = System.getProperties().getProperty("user.name");

	private int currentActionID;
	private static String currentSearchID = "";
	Composite SearchComposite;
	Composite tagComposite;

	Button keywordsPrevious;
	Button keywordsNext;
	static Button autoResultsPrevious;
	static Button autoResultsNext;

	List<KeyWordsCandidates> mycacheCandWordTrees = Cache.getInstance()
			.getCacheCandWordTrees();
	int mycacheCandWordTreesIndex = 0;
	boolean mycandwordtreesSelectFlag = true;

	// TODO: 还没完
	static List<SearchResults> mycacheAutoSearchResults = Cache.getInstance()
			.getCacheAutoSearchResults();
	static int mycacheAutoSearchResultsIndex = 0;
	static int currentautosearcresultsindex=0;
	static boolean myAutoSearchResultsSelectFlag = true;

	// 记录自动查询时间：
	Timestamp startSearchAuto = new Timestamp(System.currentTimeMillis());
	Timestamp endSearchAuto = new Timestamp(System.currentTimeMillis());

	Composite northcomposite;
	Composite northtableComposite;

	List<KeyWord> tagButton = null;
	Label canlabel;
	Composite fatherComposite;
	//TODO ...
//	String mySearch="DefaultHandler SAXParser javax.xml.parsers.SAXParser.parse";

//	public String getMySearch() {
//		return mySearch;
//	}
//
//
//
//	public void setMySearch(String mySearch) {
//		this.mySearch = mySearch;
//	}
//


	@Override
	public void createPartControl(Composite arg0) {
		BorderLayout myboBorderLayout = new BorderLayout();
		arg0.setLayout(myboBorderLayout);

		fatherComposite=arg0;

		northcomposite = new Composite(arg0, SWT.NONE);
		northcomposite.setLayoutData(BorderLayout.NORTH);

		northcomposite.setLayout(new BorderLayout());

		northtableComposite = new Composite(northcomposite, SWT.NONE);
		northtableComposite.setLayoutData(BorderLayout.NORTH);
		GridLayout gl_NorthComposite = new GridLayout(24, true);
		gl_NorthComposite.marginWidth = 1;
		gl_NorthComposite.horizontalSpacing = 1;
		northtableComposite.setLayout(gl_NorthComposite);

		canlabel = new Label(northtableComposite, SWT.NONE);
		canlabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 22,	1));
		canlabel.setText("Working Focus:");

		keywordsPrevious = new Button(northtableComposite, SWT.CENTER);
		keywordsPrevious.setEnabled(false);
		keywordsPrevious.setToolTipText("previous group words");
		keywordsPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// mycacheCandWordTreesIndex 真实的位置index：从0开始记
				if (mycacheCandWordTrees.size() > 0) {
					// 有新加入的数据时，重新开始计算头部

					mycacheCandWordTreesIndex = mycacheCandWordTreesIndex - 1;


					if (mycacheCandWordTreesIndex<0 ) {
						keywordsPrevious.setEnabled(false);
						northtableComposite.redraw();
						northtableComposite.pack();

						tagComposite.redraw();
						tagComposite.pack();
						return;
					}else {
						if (mycacheCandWordTreesIndex>=mycacheCandWordTrees.size()) {
							mycacheCandWordTreesIndex=mycacheCandWordTrees.size()-1;
						}

					}

					if(mycandwordtreesSelectFlag == false)
					{mycandwordtreesSelectFlag = true;
					canlabel.setText("Working Focus:"+"  history index:"+mycacheCandWordTreesIndex);
					northtableComposite.redraw();
					northtableComposite.pack();
					tagComposite.redraw();
					tagComposite.pack();
					
					return;

					}


					System.out.println("keyword previous: index"+mycacheCandWordTreesIndex);

					keywordsNext.setEnabled(true);



					Control[] children = tagComposite.getChildren();
					for(Control child : children){
						if(!(child.isDisposed())){
							child.dispose();
						}
					}
					tagComposite.setLayoutData(BorderLayout.CENTER);
					RowLayout myRowLayout = new RowLayout();
					myRowLayout.type = SWT.HORIZONTAL;
					myRowLayout.marginLeft = 3;
					myRowLayout.marginTop = 3;
					myRowLayout.marginRight = 3;
					myRowLayout.marginBottom = 3;

					myRowLayout.wrap = true;
					myRowLayout.pack = true;
					myRowLayout.justify = true;
					tagComposite.setLayout(myRowLayout);

					canlabel.setText("Working Focus:"+"  history index:"+mycacheCandWordTreesIndex);


					List<KeyWord> kWords = mycacheCandWordTrees.get(
							mycacheCandWordTreesIndex).getKeyWords();

					for (int j = 0; j < kWords.size(); j++) {
						final Button btn = new Button(tagComposite, SWT.PUSH);
						btn.setText(kWords.get(j).getKeywordName());
						btn.setData(kWords.get(j).getPositionInRecommandlist());
						btn.setForeground(Display.getDefault().getSystemColor(
								SWT.COLOR_GRAY));
						btn.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {

								genBtnSelectedEvent(btn);

							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub

							}
						});

					}
					//					
					northtableComposite.redraw();
					northtableComposite.pack();

					tagComposite.redraw();
					tagComposite.pack();



					mycandwordtreesSelectFlag = true;


					//				SearchComposite.redraw();
					//				SearchComposite.pack();
					if (mycacheCandWordTreesIndex==0) {
						keywordsPrevious.setEnabled(false);
					}

				}


			}
		});


		keywordsPrevious.setImage(ResourceManager.getPluginImage(
				"cn.edu.fudan.se.helpseeking", "icons/previous.gif"));
		GridData gd_keywordsPrevious = new GridData(SWT.RIGHT, SWT.TOP, true,
				false, 1, 1);
		gd_keywordsPrevious.heightHint = 16;
		keywordsPrevious.setLayoutData(gd_keywordsPrevious);

		// ////////////////
		keywordsNext = new Button(northtableComposite, SWT.CENTER);
		keywordsNext.setEnabled(false);
		keywordsNext.setToolTipText("next group words");
		keywordsNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (mycacheCandWordTrees.size() > 0) {
					// index=size+1，重新开始计算头部

					mycacheCandWordTreesIndex = mycacheCandWordTreesIndex + 1;
					if (mycacheCandWordTreesIndex>mycacheCandWordTrees.size()-1) {
						keywordsNext.setEnabled(false);
						northtableComposite.redraw();
						northtableComposite.pack();

						tagComposite.redraw();
						tagComposite.pack();

						return;
					}else {
						if (mycacheCandWordTreesIndex<0) {

							mycacheCandWordTreesIndex=0;
						}
					}


					mycandwordtreesSelectFlag = true;

					// /

					System.out.println("keyword next: index"+mycacheCandWordTreesIndex);

					keywordsPrevious.setEnabled(true);
					//移除原有数据

					Control[] children = tagComposite.getChildren();
					for(Control child : children){
						if(!(child.isDisposed())){
							child.dispose();
						}
					}
					tagComposite.setLayoutData(BorderLayout.CENTER);
					RowLayout myRowLayout = new RowLayout();
					myRowLayout.type = SWT.HORIZONTAL;
					myRowLayout.marginLeft = 3;
					myRowLayout.marginTop = 3;
					myRowLayout.marginRight = 3;
					myRowLayout.marginBottom = 3;

					myRowLayout.wrap = true;
					myRowLayout.pack = true;
					myRowLayout.justify = true;
					tagComposite.setLayout(myRowLayout);


					canlabel.setText("Working Focus:"+"  history index:"+mycacheCandWordTreesIndex);


					List<KeyWord> kWords = mycacheCandWordTrees.get(
							mycacheCandWordTreesIndex).getKeyWords();

					for (int j = 0; j < kWords.size(); j++) {
						final Button btn = new Button(tagComposite, SWT.PUSH);
						btn.setText(kWords.get(j).getKeywordName());
						btn.setData(kWords.get(j).getPositionInRecommandlist());
						btn.setForeground(Display.getDefault().getSystemColor(
								SWT.COLOR_GRAY));
						btn.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {

								genBtnSelectedEvent(btn);
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub

							}
						});

					}

					northtableComposite.redraw();
					northtableComposite.pack();

					tagComposite.redraw();
					tagComposite.pack();

					//				SearchComposite.redraw();
					//				SearchComposite.pack();

					if (mycacheCandWordTreesIndex==mycacheCandWordTrees.size()-1) {
						keywordsNext.setEnabled(false);
					}


				}


			}
		});
		keywordsNext.setImage(ResourceManager.getPluginImage(
				"cn.edu.fudan.se.helpseeking", "icons/next.gif"));
		GridData gd_keywordsNext = new GridData(SWT.LEFT, SWT.TOP, true, false,
				1, 1);
		gd_keywordsNext.heightHint = 16;
		keywordsNext.setLayoutData(gd_keywordsNext);

		tagComposite = new Composite(northcomposite, SWT.NONE|SWT.BORDER_SOLID);
		tagComposite.setLayoutData(BorderLayout.CENTER);

		RowLayout myRowLayout = new RowLayout();
		myRowLayout.type = SWT.HORIZONTAL;
		myRowLayout.marginLeft = 3;
		myRowLayout.marginTop = 3;
		myRowLayout.marginRight = 3;
		myRowLayout.marginBottom = 3;

		myRowLayout.wrap = true;
		myRowLayout.pack = true;
		myRowLayout.justify = true;
		tagComposite.setLayout(myRowLayout);



		new Button(tagComposite, SWT.NONE).setText("");
		//		Button hello=new Button(tagComposite, SWT.NONE);
		//		hello.setText("SAXParser");
		//       hello.addSelectionListener(new SelectionListener() {
		//			
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		//				// TODO Auto-generated method stub
		//				
		//				 try {
		//					domysearch();
		//				} catch (InterruptedException e1) {
		//					// TODO Auto-generated catch block
		//					e1.printStackTrace();
		//				}
		//				
		//			}
		//			
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {
		//				// TODO Auto-generated method stub
		//				
		//			}
		//		});
		//		new Button(tagComposite, SWT.NONE).setText("DefaultHandler");
		//		new Button(tagComposite, SWT.NONE).setText("NullPointerException");
		//		new Button(tagComposite, SWT.NONE).setText("HashMap");
		//		new Button(tagComposite, SWT.NONE).setText("SAXParser.parse(File,DefaultHandler)");
		//		
		//		







		// =====================================
		SearchComposite = new Composite(northcomposite, SWT.NONE);
		SearchComposite.setLayoutData(BorderLayout.SOUTH);
		GridLayout gl_SearchComposite = new GridLayout(24, true);
		gl_SearchComposite.marginWidth = 1;
		gl_SearchComposite.horizontalSpacing = 1;
		SearchComposite.setLayout(gl_SearchComposite);

		// ================

		label = new Label(SearchComposite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 22, 1));
		label.setText(" Recommended Resources:");

		autoResultsPrevious = new Button(SearchComposite, SWT.CENTER);
		autoResultsPrevious.setEnabled(false);
		autoResultsPrevious.setToolTipText("previous group solution links");
		autoResultsPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (mycacheAutoSearchResults.size() > 0) {
					// 有新加入的数据时，重新开始计算头部

					mycacheAutoSearchResultsIndex = mycacheAutoSearchResultsIndex - 1;
					
					if (mycacheAutoSearchResultsIndex <0) {
						autoResultsPrevious.setEnabled(false);
						SearchComposite.redraw();
						SearchComposite.pack();
						return;
					} else {
						if (mycacheAutoSearchResultsIndex >= mycacheAutoSearchResults.size()) {
							mycacheAutoSearchResultsIndex = mycacheAutoSearchResults
									.size() - 1;
						}
					}
	
					
					
					if (myAutoSearchResultsSelectFlag==false) {
						myAutoSearchResultsSelectFlag=true;
						label.setText("Recommended Resources:"+" history index:"+mycacheAutoSearchResultsIndex);
						SearchComposite.redraw();
						SearchComposite.pack();
						
						return;
					}
					
					autoResultsNext.setEnabled(true);
				

					searchResultsTree.removeAll();
					label.setText("Recommended Resources:"+" history index:"+mycacheAutoSearchResultsIndex);
					TreeItem noderoot = new TreeItem(searchResultsTree,
							SWT.CHECK);
					noderoot.setText("history index:"
							+ String.valueOf(mycacheAutoSearchResultsIndex ));
					noderoot.setChecked(false);
					noderoot.setData(false);

					noderoot.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_BLUE));

					List<SearchNode> sNodes = mycacheAutoSearchResults.get(
							mycacheAutoSearchResultsIndex ).getSearchNode();
					setCurrentSearchID(mycacheAutoSearchResults.get(
							mycacheAutoSearchResultsIndex ).getSearchID());

				

					for (int j = 0; j < sNodes.size(); j++) {
						// TODO NEED DETATIL
						TreeItem item = new TreeItem(searchResultsTree,
								SWT.CHECK);
						item.setText(sNodes.get(j).getTitle());
						item.setData(sNodes.get(j).getLink());
						item.setChecked(false);
						item.setForeground(Display.getDefault().getSystemColor(
								SWT.COLOR_BLUE));

						TreeItem urlitem = new TreeItem(item, SWT.CHECK);
						urlitem.setText(sNodes.get(j).getLink());
						urlitem.setData(sNodes.get(j).getLink());
						urlitem.setChecked(false);
						urlitem.setExpanded(true);

						TreeItem contentitem = new TreeItem(item, SWT.CHECK);
						contentitem.setText(sNodes.get(j).getContents());
						contentitem.setChecked(false);
						contentitem.setExpanded(true);

//						if (sNodes.get(j).getJavaExceptionNames() != null) {
//							TreeItem javaExcptionitem = new TreeItem(item,
//									SWT.CHECK);
//							javaExcptionitem.setText(sNodes.get(j)
//									.getJavaExceptionNames());
//							javaExcptionitem.setChecked(false);
//							javaExcptionitem.setExpanded(true);
//							javaExcptionitem.setForeground(Display.getDefault()
//									.getSystemColor(SWT.COLOR_RED));
//						}

						item.setExpanded(true);

					}

					noderoot.setExpanded(true);

				}
				
				SearchComposite.redraw();
				SearchComposite.pack();
				
				myAutoSearchResultsSelectFlag=true;
				if (mycacheAutoSearchResultsIndex==0) {
					autoResultsPrevious.setEnabled(false);
				}
				

			}
		});

		autoResultsPrevious.setImage(ResourceManager.getPluginImage(
				"cn.edu.fudan.se.helpseeking", "icons/previous.gif"));
		GridData gd_autoResultsPrevious = new GridData(SWT.RIGHT, SWT.TOP,
				true, false, 1, 1);
		gd_autoResultsPrevious.heightHint = 16;
		autoResultsPrevious.setLayoutData(gd_autoResultsPrevious);

		// ////////////////
		autoResultsNext = new Button(SearchComposite, SWT.CENTER);
		autoResultsNext.setEnabled(false);
		autoResultsNext.setToolTipText("next group solution links");
		autoResultsNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (mycacheAutoSearchResults.size() > 0) {
					
					
					mycacheAutoSearchResultsIndex = mycacheCandWordTreesIndex + 1;
					// index=size+1，重新开始计算头部
					if (mycacheAutoSearchResultsIndex > mycacheAutoSearchResults.size()-1) {
						autoResultsNext.setEnabled(false);
						SearchComposite.redraw();
						SearchComposite.pack();
						
						return;
					} else {
						if (mycacheAutoSearchResultsIndex <0) {
							mycacheAutoSearchResultsIndex =0;

						}
					}

					// 开关
					myAutoSearchResultsSelectFlag = true;
					
					autoResultsPrevious.setEnabled(true);
					
 

					searchResultsTree.removeAll();
					label.setText("Recommended Resources:"+" history index:"+mycacheAutoSearchResultsIndex);

					TreeItem noderoot = new TreeItem(searchResultsTree,
							SWT.CHECK);
					noderoot.setText("history index:"
							+ String.valueOf(mycacheAutoSearchResultsIndex ));

					noderoot.setChecked(false);
					noderoot.setData(false);
					noderoot.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_GREEN));

					List<SearchNode> sNodes = mycacheAutoSearchResults.get(
							mycacheAutoSearchResultsIndex ).getSearchNode();
					setCurrentSearchID(mycacheAutoSearchResults.get(
							mycacheAutoSearchResultsIndex).getSearchID());

					currentautosearcresultsindex=mycacheAutoSearchResultsIndex - 1;

					for (int j = 0; j < sNodes.size(); j++) {
						// TODO NEED DETATIL
						TreeItem item = new TreeItem(searchResultsTree,
								SWT.CHECK);
						item.setText(sNodes.get(j).getTitle());
						item.setChecked(false);

						item.setData(sNodes.get(j).getLink());
						item.setForeground(Display.getDefault().getSystemColor(
								SWT.COLOR_BLUE));

						TreeItem urlitem = new TreeItem(item, SWT.CHECK);
						urlitem.setText(sNodes.get(j).getLink());
						urlitem.setData(sNodes.get(j).getLink());
						urlitem.setChecked(false);
						urlitem.setExpanded(true);

						TreeItem contentitem = new TreeItem(item, SWT.CHECK);
						contentitem.setText(sNodes.get(j).getContents());
						contentitem.setChecked(false);
						contentitem.setExpanded(true);
//
//						if (sNodes.get(j).getJavaExceptionNames() != null) {
//							TreeItem javaExcptionitem = new TreeItem(item,
//									SWT.CHECK);
//							javaExcptionitem.setText(sNodes.get(j)
//									.getJavaExceptionNames());
//							javaExcptionitem.setChecked(false);
//							javaExcptionitem.setExpanded(true);
//							javaExcptionitem.setForeground(Display.getDefault()
//									.getSystemColor(SWT.COLOR_RED));
//
//						}

						item.setExpanded(true);
					}

					SearchComposite.redraw();
					SearchComposite.pack();
					
					noderoot.setExpanded(true);

					if (mycacheAutoSearchResultsIndex==mycacheAutoSearchResults.size()-1) {
						autoResultsNext.setEnabled(false);
					}
				}
				
				

			}
		});
		autoResultsNext.setImage(ResourceManager.getPluginImage(
				"cn.edu.fudan.se.helpseeking", "icons/next.gif"));
		GridData gd_autoResultsNext = new GridData(SWT.LEFT, SWT.TOP, true,
				false, 1, 1);
		gd_autoResultsNext.heightHint = 16;
		autoResultsNext.setLayoutData(gd_autoResultsNext);

		searchResultsTree = new Tree(arg0, SWT.BORDER | SWT.FULL_SELECTION);

		// searchResultsTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
		// true, true, 24, 1));
		searchResultsTree.setLayoutData(BorderLayout.CENTER);
		searchResultsTree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		searchResultsTree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TreeItem item = (TreeItem) e.item;

				if (item.getData() == null) {
					return;
				}

				// 判断
				if (item.getData() != null) {
					if (!(item.getData() instanceof String)) {
						if ((boolean) item.getData() == false) {
							return;
						}
					}

				}

				// /////////////////////////////////////////////////////////////

				if (PlatformUI.getWorkbench() == null) {
					return;
				}
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
					return;
				}
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage() == null) {
					return;
				}

				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					part = page
							.showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");

				} catch (Exception ple) {
					ple.printStackTrace();
					return;
				}

				// 记录选择的检索结果
				int selectindex = 0;
				if (myAutoSearchResultsSelectFlag) {
					selectindex = -1;
				}
				// SearchNode
				// recordNode=mycacheAutoSearchResults.get(searchResultsTree.getSelectionCount()+selectindex);





				if ((part instanceof HelpSeekingSolutionView)
						&& item.getData() != null) {
					HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;


					String content="";
					String compData1=item.getText();
					String compData2=(String)item.getData();

					int currentindex=0;
					List <SearchNode> searchNode=mycacheAutoSearchResults.get(currentautosearcresultsindex).getSearchNode();
					int totalresults=searchNode.size();


					for (int i = 0; i < searchNode.size(); i++) {

						if (compData1.equals(searchNode.get(i).getTitle())) {

							if (compData2.equals(searchNode.get(i).getLink())) {

								currentindex=searchNode.get(i).getPositionInResultslist();
								content=searchNode.get(i).getContents();
								compData1=searchNode.get(i).getTitle();
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
					urr.setType("Auto");
					urr.setUrl(compData2);
					urr.setTime(new Timestamp(System.currentTimeMillis()));

					HelpSeekingSolutionView.openNewTabByURL(urr);



					String xcontent = "\n---------\n[user:]\t\t\t\t"
							+ username
							+ "\n[at time:]\t\t\t"
							+ (new Timestamp(System.currentTimeMillis()))
							.toString() + "\n[Current searchID:]\t"
							+ getCurrentSearchID() + "\n[Selected Item:]\n"
							+ item.getText() + "\n" + (String) item.getData()
							+ "\n---------\n";
					FileHelper.appendContentToFile("result.txt", xcontent);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// keywordsTree= new Tree(arg0, SWT.BORDER | SWT.FULL_SELECTION);
		//
		// keywordsTree.setLayoutData(BorderLayout.SOUTH);
		// keywordsTree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		// keywordsTree.addSelectionListener(new SelectionListener() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// TreeItem item = (TreeItem) e.item;
		//
		// if (item.getData()!=null) {
		// if (! (item.getData() instanceof String)) {
		// if ((boolean)item.getData()==false) {
		// return;
		// }
		// }
		//
		// }
		//
		//
		// ///////////////////////////////////////////////////////////////
		//
		// if (PlatformUI.getWorkbench() == null)
		// { return;}
		// if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()== null)
		// { return; }
		// if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()==
		// null )
		// {
		// return;
		// }
		//
		//
		// IWorkbenchPage page = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// try {
		// part=page.showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
		//
		// } catch (Exception ple) {
		// ple.printStackTrace();
		// return;
		// }
		//
		//
		// if ((part instanceof HelpSeekingSolutionView) )
		// {
		//
		// HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
		//
		//
		// String temp = v.getTxtSearch().getText();
		//
		// String temp2="";
		// boolean flag=true;
		// for (String str : temp.split("[ ]"))
		// {
		// if (!str.equals(item.getText()))
		// {
		// if (temp2.equals(""))
		// {
		// temp2=str;
		// }
		// else
		// {
		// temp2=temp2+" "+str;
		// }
		// }else {
		// flag=false;
		// }
		//
		//
		//
		// }
		//
		// if (flag) {
		// if (temp2.equals(""))
		// {
		// temp2=item.getText();
		// }
		// else
		// {
		// temp2=temp2+" "+item.getText();
		// }
		// }
		//
		// v.getTxtSearch().setText(temp2);
		//
		// }
		//
		// }
		//
		// @Override
		// public void widgetDefaultSelected(SelectionEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//

	}



	public void domysearch() throws InterruptedException
	{
		List<WEBResult> webResults = new ArrayList<>();

		WEBResult tResult1 = new WEBResult();
		tResult1.setTitleNoFormatting("SAX XML Parser throwing Null Pointer Exception - Stack Overflow [SAXParser,NullPointerException]");
		tResult1.setUrl("http://stackoverflow.com/questions/8761121/sax-xml-parser-throwing-null-pointer-exception");
		tResult1.setSummary("an SAX XML Parser in Java and I keep getting a null pointer exception that I can't seem to figure out how to fix.");
		webResults.add(tResult1);

		WEBResult tResult2 = new WEBResult();
		tResult2.setTitleNoFormatting("android - NullPointerException While Parsing XML - Stack Overflow [SAXParser,NullPointerException]");
		tResult2.setUrl("http://stackoverflow.com/questions/7147855/nullpointerexception-while-parsing-xml");
		tResult2.setSummary("... I am getting a NullPointerException. I've searched on StackOverflow and Google .. Any ideas on what I should do to fix this?");
		webResults.add(tResult2);

		WEBResult tResult3 = new WEBResult();
		tResult3.setTitleNoFormatting("XML parsing using SaxParser with complete code | Java Code Geeks [SAXParser]");
		tResult3.setUrl("http://www.javacodegeeks.com/2012/01/xml-parsing-using-saxparser-with.html");
		tResult3.setSummary("SAX parser use callback function (org.xml.sax.helpers.DefaultHandler) ... extend DefaultHandler and override few methods ...");
		webResults.add(tResult3);

		WEBResult tResult4 = new WEBResult();
		tResult4.setTitleNoFormatting("[#JDK-7157610] NullPointerException occurs when parsing XML ...[https://bugs.openjdk.java.net/browse/JDK-7157610]");
		tResult4.setUrl("https://bugs.openjdk.java.net/browse/JDK-7157610");
		tResult4.setSummary("The problem is found in JAXP RI 1.4.5.... SAXParserFactory object, XMLReader object or DocumentBuilderFactory object, NullPointerException..");
		webResults.add(tResult4);

		WEBResult tResult5 = new WEBResult();
		tResult5.setTitleNoFormatting("null pointer exception using SAX XML Parser ...[NullPointerException]");
		tResult5.setUrl("http://http://stackoverflow.com/questions/8761121/sax-xml-parser-throwing-null-pointer-exception");
		tResult5.setSummary("...SAX Parser for XML Parsing. The problem is if I print, everything is fine. ...");
		webResults.add(tResult5);

		WEBResult tResult6 = new WEBResult();
		tResult6.setTitleNoFormatting("File.listFiles() returns null pointer exception | DaniWeb [NullPointerException]");
		tResult6.setUrl(" http://www.daniweb.com/software-development/java/threads/459574/file.listfiles-returns-null-pointer-exception");
		tResult6.setSummary("...the lastmodified file from a folder,... File.listFiles() method ... returning a null pointer exception ...");
		webResults.add(tResult6);

		WEBResult tResult7 = new WEBResult();
		tResult7.setTitleNoFormatting("Step By Step guide to Read XML file in Java Using SAX Parser ...[SAXParser]");
		tResult7.setUrl("http://javarevisited.blogspot.com/2011/12/parse-read-xml-file-java-sax-parser.html");
		tResult7.setSummary("Reading XML file in java using SAX Parser is little different than reading xml file in Java with DOM parser ...");
		webResults.add(tResult7);

		WEBResult tResult8 = new WEBResult();
		tResult8.setTitleNoFormatting("ParserFactory | Android Developers ");
		tResult8.setUrl("http://developer.android.com/reference/org/xml/sax/helpers/ParserFactory.html");
		tResult8.setSummary("ParserFactory... Java-specific class for dynamically loading SAX parsers. .. SAX parser...");
		webResults.add(tResult8);

		googlesearchList = webResults;

		String searchResultOutput="\n============\n";
		SearchResults sResults = new SearchResults();
		SearchResults tempSearchResults = new SearchResults();

		Timestamp  starttime = new Timestamp(System.currentTimeMillis());

		//		sResults.setSearchID(searchID);
		//		tempSearchResults.setSearchID(searchID);

		// 防止为空值
		if (googlesearchList.size() > 0) {
			searchResultsTree.removeAll();

			int  indexsearchresultlist=0;
			for (WEBResult webResult : googlesearchList) {
				String xml = webResult.getTitleNoFormatting();
				xml = xml.replaceAll("&quot;", "\"");
				xml.replaceAll("&#39;", "\'");
				xml.replaceAll("<b>", " ");
				xml.replaceAll("</", " ");
				xml.replaceAll("b>", "");

				searchResultOutput = searchResultOutput + "\n"
						+ webResult.toString();

				TreeItem item = new TreeItem(searchResultsTree, SWT.NULL);
				String resultTitle = xml;
				item.setText(resultTitle);
				item.setForeground(Display.getDefault().getSystemColor(
						SWT.COLOR_BLUE));

				item.setData(webResult.getUrl());

				String compareContent = xml + " " + webResult.getSummary();

				SearchNode sNode = new SearchNode();
				sNode.setTitle(resultTitle);
				sNode.setLink(webResult.getUrl());
				//				sNode.setSearchID(searchID);
				sNode.setPositionInResultslist(indexsearchresultlist);


				// TODO: 为了备份用作历史检索
				SearchNode tempsSearchNode = new SearchNode();
				tempsSearchNode.setTitle(xml);
				tempsSearchNode.setLink(webResult.getUrl());
				//				tempsSearchNode.setSearchID(searchID);
				tempsSearchNode.setPositionInResultslist(indexsearchresultlist);


				indexsearchresultlist=indexsearchresultlist+1;

				// 展示 URL
				TreeItem urlItem = new TreeItem(item, SWT.NULL);
				urlItem.setText(webResult.getUrl());
				urlItem.setData(webResult.getUrl());

				// 展示语言
				// if (webResult.getLanguage()!=null) {
				// TreeItem languageItem=new TreeItem(item, SWT.NULL);
				// languageItem.setText(webResult.getLanguage());
				//
				// }

				// 展示content 内容 还需要 优化处理 显示关键词 附近的词 构成的串 并突出显示
				if (webResult.getSummary() != null) {

					String content = webResult.getSummary();

					// 处理content 保留部分文字

					if (!content.equals("")) {
						TreeItem contentItem = new TreeItem(item, SWT.NULL);
						String resultContent =  content;
						contentItem.setText(resultContent);
						// contentItem.setData(webResult.getUrl());
						sNode.setContents(resultContent);
						tempsSearchNode.setContents(resultContent);
						contentItem.setData(null);
					}
				}

				// 展开节点
				item.setExpanded(true);

				// 后续在这里适当过滤一下 如果有异常名字则显示出来 或者 是
				// List<String>
				// tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
				String search="DefaultHandler SAXParser NullPointerException  HashMap";
				compareContent = compareContent + " " +search ;
				List<String> tempContent = CommUtil
						.arrayToList(compareContent
								.split(Basic.SPLIT_STRING));

				Set<String> boldWords = new HashSet<String>();
				;
				// tempContent.retainAll(Basic.javaExceptionalNameList);
				// for(Iterator it = tempContent.iterator();it.hasNext();){
				// if (boldWords.equals(""))
				// boldWords=(String)it.next();
				// else
				// boldWords=boldWords+";"+(String)it.next();
				// }

				if (Basic.javaExceptionalNameList != null) {
					for (String str : tempContent)

					{

						for (String jestr : Basic.javaExceptionalNameList) {
							if (str.equals(jestr)) {
								boldWords.add(jestr);
								break;
							}
						}
					}
				}

				//				if (!boldWords.isEmpty()) {
				//					String myExceptioinName = "";
				//
				//					for (String exceptionName : boldWords) {
				//						if (myExceptioinName.equals(""))
				//							myExceptioinName = exceptionName;
				//						else
				//							myExceptioinName = myExceptioinName + " ; "
				//									+ exceptionName;
				//					}
				//
				//					TreeItem subitem = new TreeItem(item, SWT.NULL);
				//					subitem.setForeground(Display.getDefault()
				//							.getSystemColor(SWT.COLOR_RED));
				//					subitem.setText(myExceptioinName);
				//					sNode.setJavaExceptionNames(myExceptioinName);
				//					tempsSearchNode.setJavaExceptionNames(myExceptioinName);
				//					item.setExpanded(true);
				//				}

				sNode.setQueryWords(search);
				tempsSearchNode.setQueryWords(search);

				sResults.getSearchNode().add(sNode);

				if (tempsSearchNode.getTitle() != null
						&& !tempsSearchNode.getTitle().trim().equals("")) {
					tempSearchResults.getSearchNode().add(tempsSearchNode);
				}

			}
		}
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

	public void setCandidateSearchWords(String search,
			List<KeyWord> keyWordsforQuery) {

		// TODO 分割字符串
		if (search == null || search.trim().equals("")) {
			return;
		}

		List<String> prepare = CommUtil.stringToList(search, "[ ]", 3);
		if (prepare == null || prepare.size() <= 0) {
			return;
		}

		System.out.println("repare search words:\n" + search);
		if (tagButton == null) {
			tagButton = new ArrayList<>();
		}

		if (tagButton.size() > 0) {

			// 去除重复推荐词list
			boolean flag = true;
			int j = 0;
			for (int i = 0; i < prepare.size(); i++) {

				if (!prepare.get(i).equals(tagButton.get(j).getKeywordName())) {
					flag = false;

					break;
				}

				j = j + 1;

				if (j == tagButton.size()) {
					break;
				}

			}

			if (flag) {
				return;
			}

		}

		// keywordsTree.removeAll();

		tagButton=new ArrayList<>();
		canlabel.setText("Working Focus:");


		Control[] children = tagComposite.getChildren();
		for(Control child : children){
			if(!(child.isDisposed())){
				child.dispose();
			}
		}



		tagComposite.setLayoutData(BorderLayout.CENTER);
		RowLayout myRowLayout = new RowLayout();
		myRowLayout.type = SWT.HORIZONTAL;
		myRowLayout.marginLeft = 3;
		myRowLayout.marginTop = 3;
		myRowLayout.marginRight = 3;
		myRowLayout.marginBottom = 3;

		myRowLayout.wrap = true;
		myRowLayout.pack = true;
		myRowLayout.justify = true;
		tagComposite.setLayout(myRowLayout);


		List<KeyWord> backupKeywords = new ArrayList<>();

		int indexofkeywords=0;
		String recommdkeywordSearchID="U" + currentActionID;

		for (String str : prepare) {

			final Button btn = new Button(tagComposite, SWT.PUSH);


			String temp = "";
			if (str.length() > 20) {
				String[] atempList = str.split("[;:]");
				int len = 0;
				for (int i = 0; i < atempList.length; i++) {
					len = len + atempList[i].length();
					if (temp.equals("")) {
						temp = atempList[i];
					} else {
						temp = temp + " " + atempList[i];
						if (len > 20) {
							break;
						}
					}

				}

			} else {
				temp = str;
			}

			btn.setText(temp);

			btn.setData(indexofkeywords);

			btn.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_GRAY));

			btn.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					genBtnSelectedEvent(btn);

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			});



			KeyWord kw = new KeyWord();
			kw.setKeywordName(temp);
			kw.setPositionInRecommandlist(indexofkeywords);
			kw.setSearchID(recommdkeywordSearchID);

			backupKeywords.add(kw);
			KeyWord butkw=new KeyWord();
			butkw.setKeywordName(temp);
			butkw.setPositionInRecommandlist(indexofkeywords);
			butkw.setSearchID(recommdkeywordSearchID);
			tagButton.add(butkw);


			indexofkeywords=indexofkeywords+1;
		}


		//show compsite
		northtableComposite.redraw();
		northtableComposite.pack();
		tagComposite.redraw();
		tagComposite.pack();
		//		SearchComposite.redraw();
		//		SearchComposite.pack();

		// save a new tree

		KeyWordsCandidates tempCandidatesbackup = new KeyWordsCandidates();
		tempCandidatesbackup.setKeyWords(backupKeywords);

		Cache.getInstance().getCacheCandWordTrees().add(tempCandidatesbackup);
		keywordsNext.setEnabled(false);
		keywordsPrevious.setEnabled(true);
		mycandwordtreesSelectFlag = false;
		mycacheCandWordTreesIndex = Cache.getInstance().getCacheCandWordTrees()
				.size();
		Query query = new Query();
		query.setQueryKeyWords(keyWordsforQuery);

		query.setUseKeywords(query.genUserkeywords(query.getQueryKeyWords()));
		query.setQueryLevel(QueryLevel.Other);
		query.setInforID(currentActionID);
		query.setTime(new Timestamp(System.currentTimeMillis()));
		// 没有使用的检索词 “U”+inforID作为编号
		query.setSearchID("U" + query.getInforID());
		query.setCosttime(0l);
		query.setIsbyuser(false);
		query.setMode(1);
		query.makeCandidateKeywords(Cache.getInstance()
				.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);

		DatabaseUtil.addKeyWordsToDataBase(query);

	}

	
	public void timerAutoSearch(List<KeyWord>mywords, String search) {
		
		if (search==null || search.trim().equals("")) {
			return;
		}
		
		System.out.println("say begin auto timer search web! starting ...");

		Query query=new Query();
			String searchID = "A" + String.valueOf(0)
					+ "timerSearch" + getCurrentActionID();
			
			 
	        query.setUseKeywords(search);
			query.setSearchID(searchID);
			query.setQueryKeyWords(mywords);
			query.setIsbyuser(false);
			query.setInforID(getCurrentActionID());
			query.setMode(2);
			setCurrentSearchID(searchID);

			if (search == null || search.equals("")) {
				return;
			}

			try {
				dosearch(query, searchID, search);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}

	
	public void searchQueryList() {

		System.out.println("say begin auto query web! starting ...");

		List<Query> querys = QueryList.getInstance().getQuerys();
		int qindex = 0;

		for (Query query : querys) {

			String searchID = "A" + String.valueOf(qindex)
					+ query.getQueryLevel() + query.getInforID();
			List<KeyWord> keyWords = query.getQueryKeyWords();
			String search = "";
			for (KeyWord keyWord : keyWords) {
				search += keyWord.getKeywordName() + " ";
			}
			search = search.trim();
			query.setInforID(getCurrentActionID());
			query.setSearchID(searchID);
			query.setIsbyuser(false);
			query.setMode(1);
			setCurrentSearchID(searchID);

	
			if (search == null || search.equals("")) {
				continue;
			}

			try {
				dosearch(query, searchID, search);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 清除query 准备下一轮 自动查询构造
		querys.clear();

	}

	public static void dosearch(Query query, String searchID, String search) throws InterruptedException {
		Timestamp starttime;
		String searchResultOutput = "\n============\n";
		SearchResults sResults = new SearchResults();

		// 为了历史记录
		SearchResults tempSearchResults = new SearchResults();

		starttime = new Timestamp(System.currentTimeMillis());

		sResults.setSearchID(searchID);
		tempSearchResults.setSearchID(searchID);
		
		query.setTime(starttime);
		query.setIsbyuser(false);
		query.setUseKeywords(search);
		query.makeCandidateKeywords(Cache.getInstance()
				.getCurrentKeywordsList(), Basic.MAX_CANDIDATE_KEYWORDS);
		
		IPreferenceStore ps=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
		String cse_key=ps.getString(PreferenceConstants.CSE_KEY);
		String cse_cx=ps.getString(PreferenceConstants.CSE_CX);

		LoopGoogleAPICall apiCall = new LoopGoogleAPICall(cse_key,cse_cx,search);

		apiCall.start();
		apiCall.join();


		googlesearchList = apiCall.getCurrentResults();

		// 防止为空值
		if (googlesearchList.size() > 0) {
			searchResultsTree.removeAll();

			int  indexsearchresultlist=0;
			for (WEBResult webResult : googlesearchList) {
				String xml = webResult.getTitleNoFormatting();
				xml = xml.replaceAll("&quot;", "\"");
				xml.replaceAll("&#39;", "\'");
				xml.replaceAll("<b>", " ");
				xml.replaceAll("</", " ");
				xml.replaceAll("b>", "");

				searchResultOutput = searchResultOutput + "\n"
						+ webResult.toString();

				TreeItem item = new TreeItem(searchResultsTree, SWT.NULL);
//				String resultTitle = xml.length() > 50 ? xml.substring(0,
//						47) + "..." : xml;
				String resultTitle =xml;
				
				String[] tempsearchlist=search.split("[ ]");
				String importantword="";
				for (int i = 0; i < tempsearchlist.length; i++) {
					if (resultTitle.contains(tempsearchlist[i].trim())) {
						importantword=importantword+" "+tempsearchlist[i];
					}
				}
				
			if (!importantword.trim().equals("")) {
				
				if (resultTitle.length()>60) {
					resultTitle=resultTitle.substring(60);
					
				}else {
					String templatespace="";
					for (int i = resultTitle.length(); i < 60; i++) {
						templatespace=templatespace+" ";
					}
					resultTitle=resultTitle+templatespace;
				}
			
				resultTitle=resultTitle+" ["+importantword+"]";
				}
				
				item.setText(resultTitle);
				item.setForeground(Display.getDefault().getSystemColor(
						SWT.COLOR_BLUE));

				item.setData(webResult.getUrl());

//				String compareContent = xml + " " + webResult.getContent();

				SearchNode sNode = new SearchNode();
				sNode.setTitle(resultTitle);
				sNode.setLink(webResult.getUrl());
				sNode.setSearchID(searchID);
				sNode.setPositionInResultslist(indexsearchresultlist);


				// TODO: 为了备份用作历史检索
				SearchNode tempsSearchNode = new SearchNode();
				tempsSearchNode.setTitle(resultTitle);
				tempsSearchNode.setLink(webResult.getUrl());
				tempsSearchNode.setSearchID(searchID);
				tempsSearchNode.setPositionInResultslist(indexsearchresultlist);


				indexsearchresultlist=indexsearchresultlist+1;

				// 展示 URL
				TreeItem urlItem = new TreeItem(item, SWT.NULL);
				urlItem.setText(webResult.getUrl());
				urlItem.setData(webResult.getUrl());

				// 展示语言
				// if (webResult.getLanguage()!=null) {
				// TreeItem languageItem=new TreeItem(item, SWT.NULL);
				// languageItem.setText(webResult.getLanguage());
				//
				// }

				// 展示content 内容 还需要 优化处理 显示关键词 附近的词 构成的串 并突出显示
				if (webResult.getSummary() != null) {

					String content = webResult.getSummary();

					// 处理content 保留部分文字

					if (!content.equals("")) {
						TreeItem contentItem = new TreeItem(item, SWT.NULL);
//						String resultContent = content.length() > 50 ? content
//								.substring(0, 47) + "..."
//								: content;
						String resultContent = content;

						contentItem.setText(resultContent);
						// contentItem.setData(webResult.getUrl());
						sNode.setContents(resultContent);
						tempsSearchNode.setContents(resultContent);
						contentItem.setData(null);
					}
				}

				// 展开节点
				item.setExpanded(true);

				// 后续在这里适当过滤一下 如果有异常名字则显示出来 或者 是
				// List<String>
				// tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
//				compareContent = compareContent + " " + search;
//				List<String> tempContent = CommUtil
//						.arrayToList(compareContent
//								.split(Basic.SPLIT_STRING));
//
//				Set<String> boldWords = new HashSet<String>();
				
				// tempContent.retainAll(Basic.javaExceptionalNameList);
				// for(Iterator it = tempContent.iterator();it.hasNext();){
				// if (boldWords.equals(""))
				// boldWords=(String)it.next();
				// else
				// boldWords=boldWords+";"+(String)it.next();
				// }

//				if (Basic.javaExceptionalNameList != null) {
//					for (String str : tempContent)
//
//					{
//
//						for (String jestr : Basic.javaExceptionalNameList) {
//							if (str.equals(jestr)) {
//								boldWords.add(jestr);
//								break;
//							}
//						}
//					}
//				}
//
//				if (!boldWords.isEmpty()) {
//					String myExceptioinName = "";
//
//					for (String exceptionName : boldWords) {
//						if (myExceptioinName.equals(""))
//							myExceptioinName = exceptionName;
//						else
//							myExceptioinName = myExceptioinName + " ; "
//									+ exceptionName;
//					}
//
//					TreeItem subitem = new TreeItem(item, SWT.NULL);
//					subitem.setForeground(Display.getDefault()
//							.getSystemColor(SWT.COLOR_RED));
//					subitem.setText(myExceptioinName);
//					sNode.setJavaExceptionNames(myExceptioinName);
//					tempsSearchNode.setJavaExceptionNames(myExceptioinName);
//					item.setExpanded(true);
//				}

				sNode.setQueryWords(search);
				tempsSearchNode.setQueryWords(search);

				sResults.getSearchNode().add(sNode);

				if (tempsSearchNode.getTitle() != null
						&& !tempsSearchNode.getTitle().trim().equals("")) {
					tempSearchResults.getSearchNode().add(tempsSearchNode);
				}

			}

			Timestamp endtime = new Timestamp(System.currentTimeMillis());

			query.setCosttime(endtime.getTime() - starttime.getTime());

			if (tempSearchResults.getSearchNode().size() > 0) {
				myAutoSearchResultsSelectFlag = false;
				autoResultsNext.setEnabled(false);
				autoResultsPrevious.setEnabled(true);
				mycacheAutoSearchResults.add(tempSearchResults);
				mycacheAutoSearchResultsIndex = mycacheAutoSearchResults
						.size();
				currentautosearcresultsindex=mycacheAutoSearchResultsIndex - 1;
			}

			// 需要保存关键词和当前cache到数据库中：
			DatabaseUtil.addKeyWordsToDataBase(query);
			for (SearchNode snNode : sResults.getSearchNode()) {
				DatabaseUtil.addSearchResultsTODataBase(
						sResults.getSearchID(), snNode);
			}
			searchResultOutput = query.toString() + searchResultOutput
					+ "\n============\n";
			FileHelper
			.appendContentToFile("result.txt", searchResultOutput);
			
			Cache.getInstance().setTimerAutoSearchmode(0);
			Cache.getInstance().setLastAutoSearchTime(new Timestamp(System.currentTimeMillis()));

		}else {
			System.out.println("No return results!");
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Search google faild! ", "Sorry connection wrong or no results! Please search agin wait for a while!" );
		}



	}

	public int getCurrentActionID() {
		return currentActionID;
	}

	public void setCurrentActionID(int currentActionID) {
		this.currentActionID = currentActionID;
	}


	private void doTestTree() {
		// 由于GOOGLE 封掉了IP 没法测试 使用这组测试数据
		// txtSearchBox.setText("printStackTrace java.lang.Throwable ClassCastException");
		// String search=txtSearchBox.getText();
		List<WEBResult> webResults = new ArrayList<>();

		WEBResult tResult1 = new WEBResult();
		tResult1.setTitleNoFormatting("SAX XML Parser throwing Null Pointer Exception - Stack Overflow [SAXParser,NullPointerException]");
		tResult1.setUrl("http://stackoverflow.com/questions/8761121/sax-xml-parser-throwing-null-pointer-exception");
		webResults.add(tResult1);

		WEBResult tResult2 = new WEBResult();
		tResult2.setTitleNoFormatting("android - NullPointerException While Parsing XML - Stack Overflow [SAXParser,NullPointerException]");
		tResult2.setUrl("http://stackoverflow.com/questions/7147855/nullpointerexception-while-parsing-xml");
		webResults.add(tResult2);

		WEBResult tResult3 = new WEBResult();
		tResult3.setTitleNoFormatting("XML parsing using SaxParser with complete code | Java Code Geeks [SAXParser]");
		tResult3.setUrl("http://www.javacodegeeks.com/2012/01/xml-parsing-using-saxparser-with.html");
		webResults.add(tResult3);

		WEBResult tResult4 = new WEBResult();
		tResult4.setTitleNoFormatting("[#JDK-7157610] NullPointerException occurs when parsing XML ...[https://bugs.openjdk.java.net/browse/JDK-7157610]");
		tResult4.setUrl("https://bugs.openjdk.java.net/browse/JDK-7157610");
		webResults.add(tResult4);

		WEBResult tResult5 = new WEBResult();
		tResult5.setTitleNoFormatting("IBM PM21145: NULLPOINTEREXCEPTION IN SAX PARSER ...[NullPointerException]");
		tResult5.setUrl("http://www.ibm.com/support/docview.wss%3Fuid%3Dswg1PM21145");
		webResults.add(tResult5);

		WEBResult tResult6 = new WEBResult();
		tResult5.setTitleNoFormatting("File.listFiles() returns null pointer exception | DaniWeb [NullPointerException]");
		tResult5.setUrl(" http://www.daniweb.com/software-development/java/threads/459574/file.listfiles-returns-null-pointer-exception");
		webResults.add(tResult6);

		WEBResult tResult7 = new WEBResult();
		tResult5.setTitleNoFormatting("Step By Step guide to Read XML file in Java Using SAX Parser ...[SAXParser]");
		tResult5.setUrl("http://javarevisited.blogspot.com/2011/12/parse-read-xml-file-java-sax-parser.html");
		webResults.add(tResult7);

		WEBResult tResult8 = new WEBResult();
		tResult5.setTitleNoFormatting("ParserFactory | Android Developers ");
		tResult5.setUrl("http://developer.android.com/reference/org/xml/sax/helpers/ParserFactory.html");
		webResults.add(tResult8);

		String searchResultOutput = "\n============\n";
		SearchResults sResults = new SearchResults();

		for (WEBResult webResult : webResults) {
			String xml = webResult.getTitleNoFormatting();
			xml = xml.replaceAll("&quot;", "\"");
			searchResultOutput = searchResultOutput + "\n"
					+ webResult.toString();

			TreeItem item = new TreeItem(searchResultsTree, SWT.NONE);

			String content = xml + " " + webResult.getSummary();

			item.setText(xml);
			item.setData(webResult.getUrl());

			SearchNode sNode = new SearchNode();
			sNode.setTitle(xml);
			sNode.setLink(webResult.getUrl());
			sResults.getSearchNode().add(sNode);

			// 后续在这里适当过滤一下 如果有异常名字则显示出来 或者 是
			// List<String>
			// tempContent=CommUtil.arrayToList(search.split(Basic.SPLIT_STRING));
			// content=content+search;
//			List<String> tempContent = CommUtil.arrayToList(content
//					.split(Basic.SPLIT_STRING));
//
//			String boldWords = "";
//
//			for (String str : tempContent)
//
//			{
//				for (String jestr : Basic.javaExceptionalNameList) {
//					if (str.equals(jestr)) {
//						if (boldWords.equals("")) {
//							boldWords = jestr;
//						} else {
//
//							boldWords = boldWords + ";" + jestr;
//						}
//					}
//				}
//			}
//
//			if (!boldWords.equals("")) {
//				TreeItem subitem = new TreeItem(item, SWT.NONE);
//				subitem.setForeground(Display.getDefault().getSystemColor(
//						SWT.COLOR_RED));
//				subitem.setText(boldWords);
//				subitem.setData(item.getData());
//				item.setExpanded(true);
//
//			}

		}

		for (SearchNode snNode : sResults.getSearchNode()) {
			DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(),
					snNode);
		}

	}

	public void genBtnSelectedEvent(final Button btn) {
		if (PlatformUI.getWorkbench() == null) {
			return;
		}
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage() == null) {
			return;
		}

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			part = page
					.showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");


		} catch (Exception ple) {
			ple.printStackTrace();
			return;
		}

		if ((part instanceof HelpSeekingSolutionView)) {

			HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;

			String temp = v.getTxtSearch().getText();
			System.out.println("hello buttons"+btn.getData());



			String temp2 = "";
			boolean flag = true;
			for (String str : temp.split("[ ]")) {
				if (!str.equals(btn.getText())) {
					if (temp2.equals("")) {
						temp2 = str;
					} else {
						temp2 = temp2 + " " + str;
					}
				} else {
					btn.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_GRAY));
					flag = false;
				}

			}

			if (flag) {
				if (temp2.equals("")) {
					temp2 = btn.getText();
					btn.setForeground(Display.getDefault()
							.getSystemColor(SWT.COLOR_DARK_GRAY));

				} else {
					temp2 = temp2 + " " + btn.getText();
				}
			}




			if (!temp2.trim().equals("")) {

				List <KeyWord> mysendoutKeyWords=new ArrayList<>();		
				List<String> searchWords=CommUtil.arrayToList(temp2.split("[ ]"));
				for (int i = 0; i < searchWords.size(); i++) {

					for (int j = 0; j < tagButton.size(); j++) {

						if (tagButton.get(j).getKeywordName().trim().equals(searchWords.get(i).trim())) {
							tagButton.get(j).setPositionInRecommandlist((Integer)btn.getData());
							tagButton.get(j).setRecommand(true);
							mysendoutKeyWords.add(tagButton.get(j));

						}

					}

				}



				v.setTxtSeachText(mysendoutKeyWords);
			}	

			v.getTxtSearch().setText(temp2);
			HelpSeekingSolutionView.getTabFolder().setSelection(0);


		}
	}
}

//
// private void doSimpleSearch() {
// tree.removeAll();
// String queryText = txtSearch.getText().trim();
// // list.add("key words:"+queryText);
// // list.add("search results:");
// //
// if (part instanceof HelpSeekingSolutionView) {
// HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
// // v.getMyBrower().setNewUrl(
// // "https://www.google.com/search?newwindow=1&q="+queryText);
// // https://www.google.com.hk/#newwindow=1&q=
// v.getMyBrower().setNewUrl(
// "http://www.baidu.com/s?wd=" + queryText);
// }
// //
// "https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q="
//
// Query query=new Query();
// query.setInforID(getCurrentActionID());
// Timestamp starttime=new Timestamp(System.currentTimeMillis());
// query.setTime(starttime);
// query.setIsbyuser(true);
// query.setQueryLevel(QueryLevel.Middle);
// query.setUseKeywords(queryText);
// query.makeCandidateKeywords(Cache.getInstance().getCurrentKeywordsList(),
// Basic.MAX_CANDIDATE_KEYWORDS);
// String searchID="P"+query.getInforID();
// query.setSearchID(searchID);
//
// String searchResultOutput="\n============\n";
//
// SearchResults sResults=new SearchResults();
// sResults.setSearchID(searchID);
//
// LoopGoogleAPICall apiCall = new LoopGoogleAPICall();
// try {
// googlesearchList = apiCall.searchWeb(queryText);
// for (WEBResult webResult : googlesearchList) {
// String xml = webResult.getTitleNoFormatting();
// xml = xml.replaceAll("&quot;", "\"");
//
//
// String useString="\n[Filter this item:]\n";
//
// // 去除无关的项目 （采用标题中文字匹配）
// List<String>
// tempListforQurey=CommUtil.stringToList(queryText,Basic.SPLIT_STRING);
// Collections.sort(tempListforQurey);
// String
// tempString=webResult.getTitleNoFormatting()+webResult.getContent();
// List<String>
// tempListSearch=CommUtil.stringToList(tempString,Basic.SPLIT_STRING);
// Collections.sort(tempListSearch);
//
// if (Collections.disjoint(tempListforQurey, tempListSearch)) {
// // System.out.println("no disjoint");
// searchResultOutput=searchResultOutput+useString+webResult.toString();
// continue;
// }
//
// useString="\n[Use item:]\n";
//
// searchResultOutput=searchResultOutput+useString+webResult.toString();
//
// // System.out.println("disjoint");
//
// //list.add(xml);
// TreeItem item = new TreeItem(tree, SWT.NONE);
// item.setText(xml);
// item.setData(webResult.getUrl());
// SearchNode sNode=new SearchNode();
// sNode.setTitle(xml);
// sNode.setLink(webResult.getUrl());
// sResults.getSearchNode().add(sNode);
//
// searchResultOutput=query.toString()+searchResultOutput+"\n============\n";
// FileHelper.appendContentToFile("result.txt", searchResultOutput);
//
//
// }
//
// } catch (IOException e1) {
// // TODO Auto-generated catch block
// e1.printStackTrace();
// }
//
//
// Timestamp endtime=new Timestamp(System.currentTimeMillis());
//
// query.setCosttime(endtime.getTime()-starttime.getTime());
// setCurrentSearchID(query.getSearchID());
// // 需要保存关键词和当前cache到数据库中：
// DatabaseUtil.addKeyWordsToDataBase(query);
// for (SearchNode snNode : sResults.getSearchNode()) {
// DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
//
// }
// }
//



// setCurrentSearchID(query.getSearchID());
// // 需要保存关键词和当前cache到数据库中：
// DatabaseUtil.addKeyWordsToDataBase(query);
// for (SearchNode snNode : sResults.getSearchNode()) {
// DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
//
// }
// }
//

//保存关键词和当前cache到数据库中：
// DatabaseUtil.addKeyWordsToDataBase(query);
// for (SearchNode snNode : sResults.getSearchNode()) {
// DatabaseUtil.addSearchResultsTODataBase(sResults.getSearchID(), snNode);
//
// }
// }
//

