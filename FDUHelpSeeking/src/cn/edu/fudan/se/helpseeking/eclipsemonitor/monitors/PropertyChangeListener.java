package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class PropertyChangeListener extends AbstractUserActivityMonitor
		implements IPropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		InteractionEvent e = new InteractionEvent();
		e.setByuser(true);
		e.setKind(Kind.PREFERENCE);
		e.setOriginId("Origin: " + event.getProperty().toString()
				+ ", Old Value: " + event.getOldValue().toString()
				+ ", New Value: " + event.getNewValue().toString());
		DatabaseUtil.addInteractionEventToDatabase(e);
	}

	@Override
	public void start() {
		PlatformUI.getPreferenceStore().addPropertyChangeListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		PlatformUI.getPreferenceStore().removePropertyChangeListener(this);
		setEnabled(false);
	}

}
