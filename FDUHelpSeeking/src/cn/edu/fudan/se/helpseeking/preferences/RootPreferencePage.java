package cn.edu.fudan.se.helpseeking.preferences;


import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class RootPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{
	public void init(IWorkbench workbench){
		
		
	}

	protected Control createContents(Composite parent) {
		Composite topComp=new Composite(parent,SWT.NONE) ;
		topComp.setLayout(new RowLayout());
		new Label(topComp,SWT.NONE).setText("欢迎使用FDUhelpseeking插件");
		return topComp;
	}

}
