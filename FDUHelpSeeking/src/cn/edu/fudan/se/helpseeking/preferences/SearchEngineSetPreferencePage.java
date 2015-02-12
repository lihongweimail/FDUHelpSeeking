package cn.edu.fudan.se.helpseeking.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;
import cn.edu.fudan.se.helpseeking.util.INIHelper;
import org.eclipse.swt.layout.GridLayout;

public class SearchEngineSetPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	
	private StringFieldEditor cseKey; //google developer application Public API access key for browser application API key
	private StringFieldEditor cseCx;  //The custom search engine ID
	RadioGroupFieldEditor rgfeEditor;
	
	
	public StringFieldEditor getCseKey() {
		return cseKey;
	}

	public void setCseKey(StringFieldEditor cseKey) {
		this.cseKey = cseKey;
	}

	public StringFieldEditor getCseCx() {
		return cseCx;
	}

	public void setCseCx(StringFieldEditor cseCx) {
		this.cseCx = cseCx;
	}

	public RadioGroupFieldEditor getRgfeEditor() {
		return rgfeEditor;
	}

	public void setRgfeEditor(RadioGroupFieldEditor rgfeEditor) {
		this.rgfeEditor = rgfeEditor;
	}

	INIHelper mycseini=new INIHelper("/cse.ini",true);
	List<CxKeyPair> myCxKeyPairs=new ArrayList<CxKeyPair>();


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
		cseKey=new StringFieldEditor(PreferenceConstants.CSE_KEY, "Public API access key: (key)", getFieldEditorParent());
		addField(cseKey);
		
		cseCx=new StringFieldEditor(PreferenceConstants.CSE_CX, "Your google custom search engine ID:(cx)", getFieldEditorParent());
		addField(cseCx);
		
		mycseini=new INIHelper("/cse.ini",true);
		
		myCxKeyPairs=mycseini.getCxKeyPairs();
		
		
		new Label(getFieldEditorParent(), SWT.LEFT).setText("To change new Search Engine!");
		new Label(getFieldEditorParent(), SWT.LEFT).setText("copy cx and key to rewrite API access key and custom search engine ID");
		
		Group mygroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		
		mygroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
		
		mygroup.setLayout(new FillLayout(SWT.VERTICAL));
		mygroup.setText("Candidate cx and key:");
		
		Composite scrollComposite=new Composite(mygroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
		scrollComposite.setLayout(new GridLayout(2, true));
		
		
		for (int i = 0; i < myCxKeyPairs.size(); i++) {
			
			new Label(scrollComposite, SWT.None).setText("NO."+i+" [Custom Search engine: "+myCxKeyPairs.get(i).getCseName().toString()+"]");
			new Label(scrollComposite, SWT.NONE).setText(" ");
			
			new Label(scrollComposite, SWT.None).setText("[Public API access key:]");
			
			Text temptext=new Text(scrollComposite, SWT.RADIO|SWT.LEFT);
			temptext.setEditable(false);
			temptext.setText(myCxKeyPairs.get(i).getKey());
			temptext.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

			new Label(scrollComposite, SWT.None).setText("[google custom search engine ID:]");
			Text temptext2=new Text(scrollComposite, SWT.RADIO|SWT.LEFT);
			temptext2.setEditable(false);
			temptext2.setText(myCxKeyPairs.get(i).getCx());
			temptext2.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			new Label(mygroup, SWT.NONE).setText(" ");
			
			new Label(scrollComposite, SWT.None).setText("------------------------------------------------------------------");
			new Label(scrollComposite, SWT.None).setText("----------------------------------------");
			
		
		}

		
	}

	protected void contributeButtons( Composite parent)
	{
		super.contributeButtons(parent);
		
		
		
		
	}	
	
}
