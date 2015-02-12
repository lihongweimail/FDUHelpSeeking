package cn.edu.fudan.se.helpseeking.preferences;




import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class DBPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	private StringFieldEditor dbURLStringFieldEditor;
	private StringFieldEditor dbNameStringFieldEditor;
	private StringFieldEditor userNameStringFieldEditor;
	private StringFieldEditor pswStringFieldEditor;
	
	public DBPreferencePage() {
		super(GRID);
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Configurate the SQLite connection parameters");
		
	}
	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		dbURLStringFieldEditor=new StringFieldEditor(PreferenceConstants.URL_KEY, "DB URL: ", getFieldEditorParent());
		addField(dbURLStringFieldEditor);
		
		dbNameStringFieldEditor=new StringFieldEditor(PreferenceConstants.DATABASE_KEY, "DB_NAME", getFieldEditorParent());
		addField(dbNameStringFieldEditor);
		
		userNameStringFieldEditor=new StringFieldEditor(PreferenceConstants.USERNAME_KEY, "User Name: ", getFieldEditorParent());
		addField(userNameStringFieldEditor);
		
		pswStringFieldEditor=new StringFieldEditor(PreferenceConstants.PASSWORD_KEY, "Password: ", getFieldEditorParent());
		addField(pswStringFieldEditor);

	}

	protected void contributeButtons( Composite parent)
	{
		super.contributeButtons(parent);
		Button initialDBButton=new Button( parent,SWT.NONE);
		initialDBButton.setText("Create DB and TABLES");
		((GridLayout) parent.getLayout() ).numColumns++;
		
	   initialDBButton.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			int result=0;
			result=DatabaseUtil.initDB(dbURLStringFieldEditor.getStringValue(), userNameStringFieldEditor.getStringValue(), pswStringFieldEditor.getStringValue());
			
			if (result==-1) {
		MessageDialog.openInformation(getShell(), "HELPSEEKING DB PARAMETER ERROR! ", "Please check Configuration!" );
			}
			if (result==0) {
				MessageDialog.openInformation(getShell(), "HELPSEEKING DB SUCCESS! ", "Congratuation!" );
			}
			
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
		
		
		Button testButton=new Button( parent,SWT.NONE);
		testButton.setText("Test DB link");
		((GridLayout) parent.getLayout() ).numColumns++;
		
	   testButton.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			int result=0;
			result=DatabaseUtil.initDB(dbURLStringFieldEditor.getStringValue()+dbNameStringFieldEditor.getStringValue(), userNameStringFieldEditor.getStringValue(), pswStringFieldEditor.getStringValue());
			
			if (result==-1) {
		MessageDialog.openInformation(getShell(), "HELPSEEKING DB PARAMETER ERROR! ", "Please check Configuration!" );
			}
			if (result==0) {
				MessageDialog.openInformation(getShell(), "HELPSEEKING DB SUCCESS! ", "Congratuation!" );
			}
			
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
		
		
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

}
