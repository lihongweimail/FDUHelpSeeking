package cn.edu.fudan.se.helpseeking.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		// TODO Auto-generated method stub
		IPreferenceStore store=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
//		store.setDefault(name, value);
		
		//=======/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/DBPreferencePage.java
		store.setDefault(PreferenceConstants.URL_KEY, PreferenceConstants.URL_DEFAULT);
		store.setDefault(PreferenceConstants.USERNAME_KEY, PreferenceConstants.USERNAME_DEFAULT);
		store.setDefault(PreferenceConstants.PASSWORD_KEY, PreferenceConstants.PASSWORD_DEFAULT);
		store.setDefault(PreferenceConstants.DATABASE_KEY, PreferenceConstants.DATABASE_DEFAULT);
		
		//=======/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/SearchEngineSetPreferencePage.java
		store.setDefault(PreferenceConstants.CSE_KEY, PreferenceConstants.CSE_KEY_DEFAULT);
		store.setDefault(PreferenceConstants.CSE_CX, PreferenceConstants.CSE_CX_DEFAULT);
		
		//==========/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/AlgorithmParameterPreferencePage.java
		store.setDefault(PreferenceConstants.ACTION_SELECT_KEY, PreferenceConstants.al_action_select_default);
		store.setDefault(PreferenceConstants.ACTION_REVEAL_KEY, PreferenceConstants.al_action_reveal_default);
		store.setDefault(PreferenceConstants.ACTION_SAVEORDEBUG_KEY, PreferenceConstants.al_action_saveOrDebug_default);
		store.setDefault(PreferenceConstants.ACTION_EXECUTE_KEY, PreferenceConstants.al_action_execute_default);
		store.setDefault(PreferenceConstants.ACTION_SELECTFOCUS_KEY, PreferenceConstants.al_action_selectfocus_default);

		store.setDefault(PreferenceConstants.API_NORMAL_KEY, PreferenceConstants.al_api_normal_default);
		store.setDefault(PreferenceConstants.API_HASCOMPILEERROR_KEY, PreferenceConstants.al_api_hasCompileError_default);
		store.setDefault(PreferenceConstants.API_CAUSEEXCEPTION_KEY, PreferenceConstants.al_api_causeException_default);

		store.setDefault(PreferenceConstants.GAMA_KEY, PreferenceConstants.al_gama_default);

		
		//========/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/GoogleSearchParametersPreferencePage.java
		store.setDefault(PreferenceConstants.WAIT_GOOGLE_TIME_KEY, PreferenceConstants.WAIT_GOOGLE_TIME_DEFAULT);
		store.setDefault(PreferenceConstants.GOOGLE_RESULT_NUMBERS_KEY, PreferenceConstants.GOOGLE_RESULT_NUMBERS_DEFAULT);
		store.setDefault(PreferenceConstants.GOOGLE_SEARCH_ROUND_KEY, PreferenceConstants.GOOGLE_SEARCH_ROUND_DEFAULT);


		//=== /FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/FoamtreeSetPreferencePage.java
		store.setDefault(PreferenceConstants.FOAMTREE_K_KEYWORDS_KEY, PreferenceConstants.FOAMTREE_K_KEYWORDS_DEFAULT);
		store.setDefault(PreferenceConstants.DIFFERENTPOSITION_COUNT_KEY, PreferenceConstants.DIFFERENTPOSITION_COUNT_DEFAULT);
		store.setDefault(PreferenceConstants.RATIO_OF_NEW_KEYWORDS_KEY, PreferenceConstants.RATIO_OF_NEW_KEYWORDS_DEFAULT);




		

	}

}
