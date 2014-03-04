package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class PerspectiveListener extends AbstractUserActivityMonitor implements
		IPerspectiveListener {

	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Perspective Activated: " + perspective.getLabel());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Perspective Changed To: " + perspective.getLabel());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.addPerspectiveListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.removePerspectiveListener(this);
		setEnabled(false);
	}

}
