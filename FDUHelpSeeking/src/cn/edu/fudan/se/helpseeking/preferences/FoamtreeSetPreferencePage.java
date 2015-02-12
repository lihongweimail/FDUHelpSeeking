package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class FoamtreeSetPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {


	 private IntegerFieldEditor foamtree_k_keywordsFieldEditor;
	 private IntegerFieldEditor differentposition_countFieldEditor;
	 private DoubleFieldEditor ratio_of_new_keywordsFieldEditor;
	 
	public FoamtreeSetPreferencePage() {
		super(GRID);
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Configurate the FOAMTREE genrating speed and foam numbers parameters");
		
	}

	
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		
		Group foamtreegroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		
		foamtreegroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
		
		foamtreegroup.setLayout(new FillLayout(SWT.VERTICAL));
		foamtreegroup.setText("set foamtree keywords numbers:");
		
		Composite foamtreeComposite=new Composite(foamtreegroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
		foamtreeComposite.setLayout(new GridLayout(2, true));

		foamtree_k_keywordsFieldEditor=new IntegerFieldEditor(PreferenceConstants.FOAMTREE_K_KEYWORDS_KEY, "FOAMTREE K KEYWORDS", foamtreeComposite);
		addField(foamtree_k_keywordsFieldEditor);
		
		Group foamtreeSpeedgroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		
		foamtreeSpeedgroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
		
		foamtreeSpeedgroup.setLayout(new FillLayout(SWT.VERTICAL));
		foamtreeSpeedgroup.setText("generate new foamtree speed:");
		
		Composite foamtreeSpeedComposite=new Composite(foamtreeSpeedgroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
		foamtreeSpeedComposite.setLayout(new GridLayout(2, true));

		differentposition_countFieldEditor=new IntegerFieldEditor(PreferenceConstants.DIFFERENTPOSITION_COUNT_KEY, "differet count in candidate Set", foamtreeSpeedComposite);
		addField(differentposition_countFieldEditor);
		ratio_of_new_keywordsFieldEditor=new DoubleFieldEditor(PreferenceConstants.RATIO_OF_NEW_KEYWORDS_KEY, "ratio of new keywords in candidate Set", foamtreeSpeedComposite);
		addField(ratio_of_new_keywordsFieldEditor);
			
	
		
	}
	
	protected void contributeButtons(Composite parent) {
		super.contributeButtons(parent);
	}


}
