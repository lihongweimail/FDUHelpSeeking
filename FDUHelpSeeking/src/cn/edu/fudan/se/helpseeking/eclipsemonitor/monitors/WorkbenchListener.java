package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class WorkbenchListener extends AbstractUserActivityMonitor implements
		IWorkbenchListener {

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Workbench Pre Shutdown: " + workbench);
		DatabaseUtil.addInteractionToDatabase(event);
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Workbench Post Shutdown: " + workbench);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().addWorkbenchListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		PlatformUI.getWorkbench().removeWorkbenchListener(this);
		setEnabled(false);
	}

}