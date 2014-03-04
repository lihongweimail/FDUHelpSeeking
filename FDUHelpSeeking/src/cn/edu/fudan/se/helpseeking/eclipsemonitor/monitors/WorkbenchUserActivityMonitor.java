package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class WorkbenchUserActivityMonitor extends AbstractUserActivityMonitor {

	private Listener interactionActivityListener;

	private Display display;

	@Override
	public void start() {
		display = FDUHelpSeekingPlugin.getDefault().getWorkbench().getDisplay();
		interactionActivityListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				setLastEventTime(System.currentTimeMillis());
				InteractionEvent e = new InteractionEvent();
			}
		};
		display.addFilter(SWT.KeyUp, interactionActivityListener);
		display.addFilter(SWT.MouseUp, interactionActivityListener);
	}

	@Override
	public void stop() {
		if (display != null && !display.isDisposed()
				&& interactionActivityListener != null) {
			display.removeFilter(SWT.KeyUp, interactionActivityListener);
			display.removeFilter(SWT.MouseUp, interactionActivityListener);
		}
	}

}
