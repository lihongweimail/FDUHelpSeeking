package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class TacticSelection extends PreferencePage implements IWorkbenchPreferencePage, ModifyListener{


	public TacticSelection() {
	}

////	TACTIC  PREFERENCEPAGE VALUE
//    //	为策略定义键值
//	public static final String  TACTICMODE_KEY="$TACTICMODE_KEY";
//    //	默认值
//	public static final  int TACTICMODE_DEFAULT= 1;
//	public static final String SELECTTACTIC_DEFAULT="Simple Tactic";
//	move to PreferenceConstants
	
	
//	定义选项
		
	Button btnRBsimple ;
	Button btnRBslidewindow;
	Text selectedTacticText;  
	int tacticMode=1; //设置模式值   selectTacticText 为simple  ；  当值为slide window时 这个值设置为2
//	定义一个IPreferenceStore 对象
	private IPreferenceStore ps;
	
	
	
	@Override
	public void modifyText(ModifyEvent e) {
		// TODO Auto-generated method stub
		String errorStr=null;
		if (selectedTacticText.getText().trim().length()==0)
		{
			errorStr="NOT CHOOSE TACTIC";
		}
		setErrorMessage(errorStr);
		setValid(errorStr==null);
		getApplyButton().setEnabled(errorStr==null);
		
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite parent) {
		         
		// TODO Auto-generated method stub
		Composite topComp=new Composite(parent, SWT.NONE);
		topComp.setLayout(new GridLayout(2, false));
		
		 new Label(topComp, SWT.NONE).setText("your choice : ");
			selectedTacticText= new Text(topComp, SWT.BORDER);
			selectedTacticText.setEditable(false);
			selectedTacticText.setText("");
			selectedTacticText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		 new Label(topComp, SWT.NONE).setText("Simple Tactic");
		btnRBsimple= new Button(topComp, SWT.RADIO);
		btnRBsimple.setText("Simple Tactic");
		btnRBsimple.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btnRBsimple.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (btnRBsimple.getSelection()) {
					selectedTacticText.setText(btnRBsimple.getText());
					tacticMode=1;
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	
		new Label(topComp, SWT.NONE).setText("Slide Window Tactic");
		btnRBslidewindow= new Button(topComp, SWT.RADIO);
		btnRBslidewindow.setText("Slide Window Tactic");
		btnRBslidewindow.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btnRBslidewindow.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (btnRBslidewindow.getSelection()) {
					selectedTacticText.setText(btnRBslidewindow.getText());
					tacticMode=2;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		取得存储对象
		ps=getPreferenceStore();
		
//	取出 原来的值; 如果没有值则设置默认值: 简单策略

		int tacticmodeValue=ps.getInt(PreferenceConstants.TACTICMODE_KEY);
		
		System.out.println("TACTIC MODE: "+tacticmodeValue);
		btnRBsimple.setSelection(true);
		btnRBslidewindow.setSelection(false);
		tacticMode=tacticmodeValue;
		
	if (tacticmodeValue==1) {

		btnRBsimple.setSelection(true);
		btnRBslidewindow.setSelection(false);
		selectedTacticText.setText(btnRBsimple.getText());
	} else {	
	if (tacticmodeValue==2) {
		
		btnRBsimple.setSelection(false);
		btnRBslidewindow.setSelection(true);
		selectedTacticText.setText(btnRBslidewindow.getText());
	}else {
		btnRBsimple.setSelection(true);
		btnRBslidewindow.setSelection(false);
		selectedTacticText.setText(btnRBsimple.getText());
	tacticMode=PreferenceConstants.TACTICMODE_DEFAULT;
	}
	
	}
	
	selectedTacticText.addModifyListener(this);
		
		return topComp;
	}

	protected void performDefaults() {
		selectedTacticText.setText(PreferenceConstants.SELECTTACTIC_DEFAULT);
		
		
	}
	
	/**      
	 *  * 父类方法，单击“应用”按钮时执行此方法，将文本框值保存并弹出成功的提示信息    
	 *    */      
	protected void performApply()
	{          
		doSave(); //自定义方法，保存设置         
		MessageDialog.openInformation(getShell(), "MESSAGE", "SAVED SUCCESS");      
	}      
	/**      
	 *  * 父类方法，单击“确定”按钮时执行此方法，将文本框值保存并弹出成功的提示信息     
	 *   * @return true成功退出      
	 *    */     
	public boolean performOk() 
	{        
		doSave();       
		MessageDialog.openInformation(getShell(),"MESSAGE","RESTART AS AVILIABLE");        
		return true;    
	}       

	/**       
	 * * 自定义方法，保存文本框的值     
	 *   */     
	private void doSave() 
	{          
		ps.setValue(PreferenceConstants.TACTICMODE_KEY, tacticMode);      
		
		
	}
	
	
}
