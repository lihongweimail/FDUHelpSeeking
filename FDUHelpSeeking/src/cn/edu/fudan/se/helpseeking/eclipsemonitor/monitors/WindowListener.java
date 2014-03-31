package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class WindowListener extends AbstractUserActivityMonitor implements
		IWindowListener {

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Window Activated: " + window.toString());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Window Deactivated: " + window.toString());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Window Closed: " + window.toString());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Window Opened: " + window.toString());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().addWindowListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		PlatformUI.getWorkbench().removeWindowListener(this);
		setEnabled(false);
	}

}
