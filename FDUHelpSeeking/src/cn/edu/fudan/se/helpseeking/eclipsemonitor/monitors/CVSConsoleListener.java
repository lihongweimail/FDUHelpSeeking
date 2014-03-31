package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.team.internal.ccvs.core.CVSProviderPlugin;
import org.eclipse.team.internal.ccvs.core.client.Session;
import org.eclipse.team.internal.ccvs.core.client.listeners.IConsoleListener;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class CVSConsoleListener extends AbstractUserActivityMonitor implements
		IConsoleListener {

	@Override
	public void commandCompleted(Session arg0, IStatus arg1, Exception arg2) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("CVS Command Completed: "
				+ arg0.getCurrentCommand().toString());
		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void commandInvoked(Session arg0, String arg1) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("CVS Command Invoked: " + arg0);
		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void errorLineReceived(Session arg0, String arg1, IStatus arg2) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("CVS Error: " + arg0);
		DatabaseUtil.addInteractionEventToDatabase(event);

	}

	@Override
	public void messageLineReceived(Session arg0, String arg1, IStatus arg2) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("CVS Message: " + arg0);
		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void start() {
		CVSProviderPlugin.getPlugin().setConsoleListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		CVSProviderPlugin.getPlugin().setConsoleListener(null);
		setEnabled(false);
	}

}
