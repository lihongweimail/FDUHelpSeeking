package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicArrowButton;

import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.web.SimpleBrower;



public class HelpSeekingSearchView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView"; //$NON-NLS-1$
	
	private Text txtSearch;
	private org.eclipse.swt.widgets.List list;
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
	   	 		list.add("检索词为："+queryText);
	   	 		list.add("检索结果为：");
	  
	   	 		if(part instanceof HelpSeekingSolutionView){
		   	 		HelpSeekingSolutionView v = (HelpSeekingSolutionView) part;
						v.getMyBrower().setNewUrl( "http://www.google.com/search?newwindow=1&safe=strict&q="+queryText);
		   	 		}
//		   	 	https://www.google.com.hk/search?newwindow=1&safe=strict&site=&source=hp&q=hello+world&oq=hello+world&gs_l=hp.3...2437.5350.0.6715.12.10.0.0.0.0.319.319.3-1.1.0....0...1c.1.41.hp..12.0.0.0.SzpSkykwWd4&bav=on.2,or.r_cp.&bvm=bv.64764171%2Cd.aGc%2Cpv.xjs.s.en_US.PQkb9ew9HrM.O&fp=f644e369b7289f73&biw=908&bih=354&dpr=1&tch=3&ech=1&psi=my1NU4i_KY-tiQefxYCYAg.1397566870615.3&wrapid=tlif139756687745911
		 		
	   	 		
	   	 	List<WEBResult> results=new ArrayList<WEBResult>();
	   	 		
	   	 			LoopGoogleAPICall apiCall=new LoopGoogleAPICall();
					try {
						results=apiCall.searchWeb(queryText);
					
					for (WEBResult webResult : results) {
						
						list.add(webResult.toString());
					}
				
					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	
	   	 			
	   	 		}
	   	 	});
	   	 	btnSearchGoogle.setText("Search Google");
	   	
	   	txtSearch = new Text(SearchComposite, SWT.BORDER | SWT.WRAP);
	   	txtSearch.setText("hello world");
	   	GridData gd_txtSearch = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	   	gd_txtSearch.widthHint = 183;
	   	txtSearch.setLayoutData(gd_txtSearch);
	   	
	   	list = new org.eclipse.swt.widgets.List(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	   	list.setLayoutData(BorderLayout.CENTER);
		
	
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	
	}

	public void setSearchValue(String search){
		txtSearch.setText(search);
	}


	
}
