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
		
		store.setDefault(PreferenceConstants.URL_KEY, PreferenceConstants.URL_DEFAULT);
		store.setDefault(PreferenceConstants.USERNAME_KEY, PreferenceConstants.USERNAME_DEFAULT);
		store.setDefault(PreferenceConstants.PASSWORD_KEY, PreferenceConstants.PASSWORD_DEFAULT);
		store.setDefault(PreferenceConstants.DATABASE_KEY, PreferenceConstants.DATABASE_DEFAULT);
		store.setDefault(PreferenceConstants.CSE_KEY, PreferenceConstants.CSE_KEY_DEFAULT);
		store.setDefault(PreferenceConstants.CSE_CX, PreferenceConstants.CSE_CX_DEFAULT);
		

	}

}
