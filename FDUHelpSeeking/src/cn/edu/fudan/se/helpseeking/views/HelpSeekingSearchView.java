package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;



public class HelpSeekingSearchView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView"; //$NON-NLS-1$
	
	private Text txtSearch;
	private org.eclipse.swt.widgets.List list;
	private List<WEBResult> googlesearchList=new ArrayList<WEBResult>();
	
	public HelpSeekingSearchView() {
	   	 			part=FDUHelpSeekingPlugin
	   	 				.getDefault()
	   					.getWorkbench()
	   					.getActiveWorkbenchWindow()
	   					.getActivePage()
	   					.findView(
	   							"cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
	   	 		
   	 			}
	
	IViewPart part;
	
		
	   @Override
	public void createPartControl(Composite arg0) {
		 	   	 		
	  
		   
	   	arg0.setLayout(new BorderLayout(0, 0));
	   	
	   	Composite SearchComposite = new Composite(arg0, SWT.NONE);
	   	SearchComposite.setLayoutData(BorderLayout.NORTH);
	   	SearchComposite.setLayout(new GridLayout(2, false));
	   	
	   	Button btnSearchGoogle = new Button(SearchComposite, SWT.NONE);
	   	
	   	 	btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
	   	 		@Override
	   	 		public void widgetSelected(SelectionEvent e) {
	   	 			
	   	 			list.removeAll();
	   	 		String queryText=txtSearch.getText().trim();
//	   	 		list.add("key words:"+queryText);
//	   	 		list.add("search results:");
//	  
	   	 		if(part instanceof HelpSeekingSolutionView){
		   	 		HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
		   	 		
//						v.getMyBrower().setNewUrl( "https://www.google.com/search?newwindow=1&q="+queryText);

//						https://www.google.com.hk/#newwindow=1&q=
					v.getMyBrower().setNewUrl("http://www.baidu.com/s?wd="+queryText);
					
	

		   	 		}
//	   	 	"https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q=" 	 		
//	   	 		需要保存关键词和当前cache到数据库中：
	   	 		DatabaseUtil.addKeyWordsToDataBase(Cache.getInstance(),queryText);

	   
	   	 		
	   	 			LoopGoogleAPICall apiCall=new LoopGoogleAPICall();
					try {
						googlesearchList=apiCall.searchWeb(queryText);
					
					for (WEBResult webResult : googlesearchList) {
						String xml =webResult.getTitleNoFormatting(); 
						
						xml=xml.replaceAll("&quot;", "\"");
						
						//去除无关的项目 （采用标题中文字匹配）
	
								list.add(xml);
			
						
					
					}
				
					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	
	   	 			
	   	 		}
	   	 	});
	   	 	btnSearchGoogle.setText("Search Google");
	   	
	   	txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
	   	txtSearch.setText("hello world");
	   	GridData gd_txtSearch = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	   	gd_txtSearch.heightHint = 86;
	   	gd_txtSearch.widthHint = 183;
	   	txtSearch.setLayoutData(gd_txtSearch);
	   	
	   	list = new org.eclipse.swt.widgets.List(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	   	list.setLayoutData(BorderLayout.CENTER);
	   	list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				 list.getSelectionIndex();
		   	 		if(part instanceof HelpSeekingSolutionView){
			   	 		HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;

						v.getMyBrower().setNewUrl(googlesearchList.get(list.getSelectionIndex()).getUrl());
						System.out.println("select item: "+ list.getSelectionIndex());
						System.out.println(googlesearchList.get(list.getSelectionIndex()).getUrl());

			   	 		}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
		
				
			}
		});
		
	
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	
	}

	public void setSearchValue(String search){
		txtSearch.setText(search);
	}


	
}
