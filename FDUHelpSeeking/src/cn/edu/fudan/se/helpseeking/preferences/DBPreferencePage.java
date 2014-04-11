package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class DBPreferencePage extends PreferencePage     implements IWorkbenchPreferencePage, ModifyListener {  
	//为文本框定义三个键值      
	public static final String URL_KEY = "$URL_KEY"; 
	public static final String USERNAME_KEY = "$USERNAME_KEY";    
	public static final String PASSWORD_KEY = "$PASSWORD_KEY";     
	//为文本框值定义三个默认值      
	public static final String URL_DEFAULT = "jdbc:mysql://localhost:3306/helpseeking";     
	public static final String USERNAME_DEFAULT = "root";     
	public static final String PASSWORD_DEFAULT = "root";     
	//定义三个文本框      
	private Text urlText;     
	private Text usernameText;     
	private Text passwordText;     
	//定义一个IPreferenceStore对象      
	private IPreferenceStore ps;       
	/**       
	 * * 接口IWorkbenchPreferencePage的方法，负责初始化。在此方法中设置一个      
	 * * PreferenceStore对象，由此对象提供文本框值的读入/写出方法     
	 *  */ 
	public void init(IWorkbench workbench) 
	{       
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());    
	}       
	/**      
	 *  * 父类的界面创建方法      
	 *   */      
	protected Control createContents(Composite parent) 
	{         
		Composite topComp = new Composite(parent, SWT.NONE);         
		topComp.setLayout(new GridLayout(2, false));         
		/*          
		 *  * 创建三个文本框及其标签         
		 *    */          
		new Label(topComp, SWT.NONE).setText("URL:");         
		urlText = new Text(topComp, SWT.BORDER);          
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));           
		new Label(topComp, SWT.NONE).setText("用户名:");          
		usernameText = new Text(topComp, SWT.BORDER);          
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));           
		new Label(topComp, SWT.NONE).setText("密码:");          
		passwordText = new Text(topComp, SWT.BORDER | SWT.PASSWORD);         
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));        
		//取得一个IPreferenceStore对象         
		ps = getPreferenceStore();        
		/*
		 * 取出以前保存的值，并将其设置到文本框中，如果取出值为空          
		 * 或者是空字串，则填入默认值 
		 */

		String url = ps.getString(URL_KEY);

		if (url == null || url.trim().equals(""))
			urlText.setText(URL_DEFAULT); 
		else 
			urlText.setText(url);  
		String username = ps.getString(USERNAME_KEY); 
		if (username == null || username.trim().equals(""))  
			usernameText.setText(USERNAME_DEFAULT);  
		else 
			usernameText.setText(username);  
		String password = ps.getString(PASSWORD_KEY);    
		if (password == null || password.trim().equals("")) 
			passwordText.setText(PASSWORD_DEFAULT);   
		else 
			passwordText.setText(password);       
		/* 
		 * 添加事件监听，this代表本类，因本类也实现了ModifyListener接口，          
		 * 所以本类可以作为监听器使用 
		 */ 
		usernameText.addModifyListener(this);  
		passwordText.addModifyListener(this);  
		urlText.addModifyListener(this);   
		return topComp;     }  

	/** 
	 * 本类实现ModifyListener接口的方法，当三个文本框中发生修改时将执行此方法。     
	 * 方法中对输入值进行了验证并将“确定”、“应用”两按钮使能     
	 */ 
	public void modifyText(ModifyEvent e) 
	{ 
		String errorStr = null;   //将原错误信息清空        
		if (urlText.getText().trim().length() == 0) 
		{             
			errorStr = "URL不能为空！"; 
		}else if (usernameText.getText().trim().length() == 0)   {            
			errorStr = "用户名不能为空！"; 
		} else if (passwordText.getText().trim().length() == 0) 	 {             
			errorStr = "密码不能为空！"; 

		} 

		setErrorMessage(errorStr);  	//errorStr=null时复原为正常的提示文字 
		setValid(errorStr == null);//“确定”按钮
		getApplyButton().setEnabled(errorStr == null);//“应用”按钮      
	}      

	/**       
	 * * 父类方法，单击“复原默认值”按钮时将执行此方法，取出默认值设置到文本框中      
	 *  */      
	protected void performDefaults() 
	{        
		urlText.setText(URL_DEFAULT);   
		usernameText.setText(USERNAME_DEFAULT);    
		passwordText.setText(PASSWORD_DEFAULT);    
	}       
	/**      
	 *  * 父类方法，单击“应用”按钮时执行此方法，将文本框值保存并弹出成功的提示信息    
	 *    */      
	protected void performApply()
	{          
		doSave(); //自定义方法，保存设置         
		MessageDialog.openInformation(getShell(), "信息", "成功保存修改!");      
	}      
	/**      
	 *  * 父类方法，单击“确定”按钮时执行此方法，将文本框值保存并弹出成功的提示信息     
	 *   * @return true成功退出      
	 *    */     
	public boolean performOk() 
	{        
		doSave();       
		MessageDialog.openInformation(getShell(),"信息","修改在下次启动生效");        
		return true;    
	}       

	/**       
	 * * 自定义方法，保存文本框的值     
	 *   */     
	private void doSave() 
	{          
		ps.setValue(URL_KEY, urlText.getText());      
		ps.setValue(USERNAME_KEY, usernameText.getText());    
		ps.setValue(PASSWORD_KEY, passwordText.getText());   
	}


}
