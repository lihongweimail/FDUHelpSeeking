package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class WorkbenchListener extends AbstractUserActivityMonitor implements
		IWorkbenchListener {

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Workbench Pre Shutdown: " + workbench);
		DatabaseUtil.addInteractionEventToDatabase(event);
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Workbench Post Shutdown: " + workbench);
		DatabaseUtil.addInteractionEventToDatabase(event);
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
