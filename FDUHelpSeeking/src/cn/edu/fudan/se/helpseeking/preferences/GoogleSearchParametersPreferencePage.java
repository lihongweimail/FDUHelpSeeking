package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class GoogleSearchParametersPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

//	// 取走到配置
//	public final static int WAIT_GOOGLE_TIME = 6000;
//	// 取走到配置
//	public final static int GOOGLE_RESULT_NUMBERS = 10; // default 10 to get 10
//														// urls ; values as
//														// 1--10  用于取结果数量的，即一次检索后，从Google获得多少个URL
// 														// 一般为4  配合GOOGLE_SERCH_ROUND ， 即它们相乘为最后获得的URL数量
//	// 取走到配置
//	public final static int GOOGLE_SEARCH_ROUND = 3; // default 10 for get 100  max is 10
//														// url ; test as 1 get 10 urls
//                                                        // 用于设置获取url数量的参数，功能是，就一个查询的结果集中去取的次数，每次取的数量为GOOGLE_RESULT_NUMBERS个

	private IntegerFieldEditor wait_google_timeFieldEditor;
	private IntegerFieldEditor google_result_numbersFieldEditor;
	private IntegerFieldEditor google_search_roundFieldEditor;
	
	public GoogleSearchParametersPreferencePage() {
		super(GRID);
		setPreferenceStore(FDUHelpSeekingPlugin.getDefault().getPreferenceStore());
		setDescription("Configurate the GOOGLE SEARCH parameters");
		
	}

	
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		wait_google_timeFieldEditor=new IntegerFieldEditor(PreferenceConstants.WAIT_GOOGLE_TIME_KEY, "Wait google serach time delay", getFieldEditorParent());
		addField(wait_google_timeFieldEditor);
		
		google_result_numbersFieldEditor=new IntegerFieldEditor(PreferenceConstants.GOOGLE_RESULT_NUMBERS_KEY, "the number of one google search round", getFieldEditorParent());
		addField(google_result_numbersFieldEditor);
		
		google_search_roundFieldEditor=new IntegerFieldEditor(PreferenceConstants.GOOGLE_SEARCH_ROUND_KEY, "Google search Round with one query", getFieldEditorParent());
		addField(google_search_roundFieldEditor);
		
		
	}
	
	protected void contributeButtons(Composite parent) {
		super.contributeButtons(parent);
	}

}
