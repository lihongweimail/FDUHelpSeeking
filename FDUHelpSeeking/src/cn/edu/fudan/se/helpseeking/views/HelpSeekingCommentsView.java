package cn.edu.fudan.se.helpseeking.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class HelpSeekingCommentsView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView"; //$NON-NLS-1$

	
Text text=null;
	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		text = new Text(arg0, SWT.FULL_SELECTION);
		text.setText("hello comments");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
      text.setFocus();
	}

}
