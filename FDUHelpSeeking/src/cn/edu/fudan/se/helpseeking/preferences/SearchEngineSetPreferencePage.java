package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class SearchEngineSetPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	
	private StringFieldEditor cseKey; //google developer application Public API access key for browser application API key
	private StringFieldEditor cseCx;  //The custom search engine ID

	public SearchEngineSetPreferencePage() {
		super(GRID);
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Set your google custome search engin ID and Public API access key for browser application");
	}
	
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		cseKey=new StringFieldEditor(PreferenceConstants.CSE_KEY, "Public API access key: ", getFieldEditorParent());
		addField(cseKey);
		
		cseCx=new StringFieldEditor(PreferenceConstants.CSE_CX, "Your google custome search engin ID:", getFieldEditorParent());
		addField(cseCx);
		

	}

	protected void contributeButtons( Composite parent)
	{
		super.contributeButtons(parent);
	}	

}
