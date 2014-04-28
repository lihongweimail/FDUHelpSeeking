package cn.edu.fudan.se.helpseeking.views;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.web.SimpleBrower;



public class HelpSeekingSolutionView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView"; //$NON-NLS-1$

 
	public static  SimpleBrower myBrower;  
	   
	public SimpleBrower getMyBrower() {
		return myBrower;
	}



	public void setMyBrower(SimpleBrower myBrower) {
		this.myBrower = myBrower;
	}

	
	public void useOleBrowser() {
		// TODO Auto-generated method stub
	}	
	
	
	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		  String newUrl="about:blank";
		  
		   myBrower = new SimpleBrower();
//		  myBrower.setDisableButton(true);
          myBrower.setMyComposite(arg0);
	        myBrower.createShow();  
	        myBrower.setNewUrl( newUrl);
	        myBrower.refreshBrowser();
	       
	    }




	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}


}
