package cn.edu.fudan.se.helpseeking.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class BrowserEditorView extends ViewPart{
	
	private static BrowserEditorView singleton;
	
	public BrowserEditorView(){
		singleton = this;
	}
	
	public static BrowserEditorView getSingleTon(){
		return singleton;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new FillLayout());
		text = new Text(topComp,SWT.NONE);
		//this.text = new Text(topComp, SWT.BORDER);
	}
	
	private Text text;
	
	public void setText(String textStr){
		text.setText(textStr);
	}

	@Override
	public void setFocus() {
		
	}

}
