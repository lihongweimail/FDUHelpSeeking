package cn.edu.fudan.se.helpseeking.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.ui.part.ViewPart;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;



public class HelpSeekingSearchView extends ViewPart  implements IPropertyChangeListener{
	private Text txtSearch;
	private org.eclipse.swt.widgets.List list;
	public HelpSeekingSearchView() {
	}
	
	
	   @Override
	public void createPartControl(Composite arg0) {
		   
		 Cache.getInstance().addPropertyChangeListener(this);
		 
	   	arg0.setLayout(new BorderLayout(0, 0));
	   	
	   	Composite SearchComposite = new Composite(arg0, SWT.NONE);
	   	SearchComposite.setLayoutData(BorderLayout.NORTH);
	   	SearchComposite.setLayout(new GridLayout(2, false));
	   	
	   	Button btnSearchGoogle = new Button(SearchComposite, SWT.NONE);
	   	
	   	 	btnSearchGoogle.addSelectionListener(new SelectionAdapter() {
	   	 		@Override
	   	 		public void widgetSelected(SelectionEvent e) {
	   	 		String queryText=txtSearch.getText().trim();
	   	 		
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
	   	GridData gd_txtSearch = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	   	gd_txtSearch.widthHint = 183;
	   	txtSearch.setLayoutData(gd_txtSearch);
	   	
	   	list = new org.eclipse.swt.widgets.List(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	   	list.setLayoutData(BorderLayout.CENTER);
		
	
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	
	}


	@Override
	public void propertyChange(PropertyChangeEvent event) {
	     //The population data is being sourced by another plugin in the background.  
        if( event.getProperty().equals("newKeywords")) {  
            Object val = event.getNewValue();  
            //TODO LISTENER CHANGE TEXT VALUE
            
        }  
		
	}

	
}
