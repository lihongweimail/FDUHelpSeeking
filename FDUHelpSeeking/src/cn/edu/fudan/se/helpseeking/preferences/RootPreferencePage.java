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
	public RootPreferencePage() {
		super();
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Welcome to use FDUHelpseeking plugin!");
		
	}
	
	public void init(IWorkbench workbench){
		
		
	}

	protected Control createContents(Composite parent) {
		Composite topComp=new Composite(parent,SWT.NONE) ;
		topComp.setLayout(new RowLayout());

		return topComp;
	}

}
