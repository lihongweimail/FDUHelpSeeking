package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class PageListener extends AbstractUserActivityMonitor implements
		IPageListener {

	@Override
	public void pageActivated(IWorkbenchPage page) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Page Activated: " + page.getLabel());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void pageClosed(IWorkbenchPage page) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Page Closed: " + page.getLabel());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void pageOpened(IWorkbenchPage page) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Page Opened: " + page.getLabel());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.addPageListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.removePageListener(this);
		setEnabled(false);
	}

}
