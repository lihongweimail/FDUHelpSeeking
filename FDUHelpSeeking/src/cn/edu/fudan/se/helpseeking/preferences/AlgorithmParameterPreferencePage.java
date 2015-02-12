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

public class AlgorithmParameterPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

//	// 新算法权重
//	// 取走到配置
//	// 对于action来说有两个权重设置： weightOne 和 weightTwo 他们分别用于描述 interest level 中 action
//	// 的级别 以及 API 来源的级别
//	// DOI -model (Degree of Interestes)
//	// interest level action : select 1 < reveal 2 < save or debug 3 < execute 4
//	// interest level api : normal 1 < has-compile_error 2 < cause_exception 3
//
//	public final static int action_select = 1;
//	public final static int action_reveal = 2;
//	public final static int action_saveOrDebug = 3;
//	public final static int action_execute = 4;
//	public final static int action_selectfocus = 5;
//
//	public final static int api_normal = 1;
//	public final static int api_hasCompileError = 2;
//	public final static int api_causeException = 3;
//
//	public final static double gama = 2.0; // 新算法权重值
//
//	// 光标所在行附近的代码： 目前是设置为前后2行；共计5行；为了更加关注，设置为1行，就自己
////  以上均需取走
	private IntegerFieldEditor al_action_selectFieldEditor;
	private IntegerFieldEditor al_action_revealFieldEditor;
	private IntegerFieldEditor al_action_saveOrDebugFieldEditor;
	private IntegerFieldEditor al_action_executeFieldEditor;
	private IntegerFieldEditor al_action_selectfocusFieldEditor;
	
	
	private IntegerFieldEditor al_api_normalFieldEditor;
	private IntegerFieldEditor al_api_hasCompileErrorFieldEditor;
	private IntegerFieldEditor al_api_causeExceptionFieldEditor;
	
	private DoubleFieldEditor al_gamaFieldEditor;
	
	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
//		Label actionLabel=new Label(getFieldEditorParent(), SWT.LEFT);
//		       actionLabel.setText("Action DOI parameters");
				Group actiongroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
				
				actiongroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
				
				actiongroup.setLayout(new FillLayout(SWT.VERTICAL));
				actiongroup.setText("Action DOI parameters:");
				
				Composite actionComposite=new Composite(actiongroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
				actionComposite.setLayout(new GridLayout(2, true));
		       
		al_action_selectFieldEditor= new IntegerFieldEditor(PreferenceConstants.ACTION_SELECT_KEY, "Action select", actionComposite);
		addField(al_action_selectFieldEditor);
		
		al_action_revealFieldEditor=new IntegerFieldEditor(PreferenceConstants.ACTION_REVEAL_KEY, "Action revel", actionComposite);
		addField(al_action_revealFieldEditor);
		
		al_action_saveOrDebugFieldEditor=new IntegerFieldEditor(PreferenceConstants.ACTION_SAVEORDEBUG_KEY, "Action Save or Debug", actionComposite);
		addField(al_action_saveOrDebugFieldEditor);
		
		al_action_executeFieldEditor=new IntegerFieldEditor(PreferenceConstants.ACTION_EXECUTE_KEY,"Action execute",actionComposite);
		addField(al_action_executeFieldEditor);
		
		al_action_selectfocusFieldEditor=new IntegerFieldEditor(PreferenceConstants.ACTION_SELECTFOCUS_KEY, "Action select focus", actionComposite);
		addField(al_action_selectfocusFieldEditor);
		
//		Label apiLabel=new Label(getFieldEditorParent(), SWT.LEFT);
//		apiLabel.setText("API DOI parameters");
		Group apigroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		
		apigroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
		
		apigroup.setLayout(new FillLayout(SWT.VERTICAL));
		apigroup.setText("API DOI parameters:");
		
		Composite apiComposite=new Composite(apigroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
		apiComposite.setLayout(new GridLayout(2, true));

		
		al_api_normalFieldEditor=new IntegerFieldEditor(PreferenceConstants.API_NORMAL_KEY, "API Normal", apiComposite);
		addField(al_api_normalFieldEditor);
		
		al_api_hasCompileErrorFieldEditor= new IntegerFieldEditor(PreferenceConstants.API_HASCOMPILEERROR_KEY, "API has Compile Error", apiComposite);
		addField(al_api_hasCompileErrorFieldEditor);
		
		al_api_causeExceptionFieldEditor=new IntegerFieldEditor(PreferenceConstants.API_CAUSEEXCEPTION_KEY, "API cause Exception", apiComposite);
		addField(al_api_causeExceptionFieldEditor);
		
//		Label gamaLabel=new Label(getFieldEditorParent(), SWT.LEFT);
//		gamaLabel.setText("Algroithm DOI weight value");
       Group gamagroup=new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_OUT);
		
		gamagroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,	1));
		
		gamagroup.setLayout(new FillLayout(SWT.VERTICAL));
		gamagroup.setText("Algroithm DOI weight value:");
		
		Composite gamaComposite=new Composite(gamagroup, SWT.NONE);//SWT.H_SCROLL | SWT.V_SCROLL);
		gamaComposite.setLayout(new GridLayout(2, true));
		
		al_gamaFieldEditor=new DoubleFieldEditor(PreferenceConstants.GAMA_KEY, "gama value", gamaComposite);
		addField(al_gamaFieldEditor);
		
	}	
	
	public AlgorithmParameterPreferencePage() {
		super(GRID);
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Configurate the Algorithm parameters");
		
	}
	
	protected void contributeButtons(Composite parent) {
		super.contributeButtons(parent);
	}

	
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}



}
