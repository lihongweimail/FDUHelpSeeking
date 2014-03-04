package cn.edu.fudan.se.helpseeking.eclipsemonitor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.AbstractUserActivityMonitor;

public class ActivityContextManager {

	private final Set<IUserAttentionListener> attentionListeners = new CopyOnWriteArraySet<IUserAttentionListener>();
	private final List<AbstractUserActivityMonitor> activityMonitors;
	private volatile String lastInteractionOrigin;
	private final CheckActivityJob checkJob;
	private IWorkingSet[] workingSets;

	private final IPropertyChangeListener WORKING_SET_CHANGE_LISTENER = new IPropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (IWorkingSetManager.CHANGE_WORKING_SET_CONTENT_CHANGE
					.equals(event.getProperty()))
				updateWorkingSetSelection();
		}

	};

	private void updateWorkingSetSelection() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				if (window != null) {
					IWorkbenchPage page = window.getActivePage();
					if (page != null) {
						workingSets = page.getWorkingSets();
					}
				}
			}
		});
	}

	public ActivityContextManager(List<AbstractUserActivityMonitor> monitors) {
		this.activityMonitors = new CopyOnWriteArrayList<>(monitors);
		checkJob = new CheckActivityJob(new IActivityManagerCallback() {

			@Override
			public void inactive() {
				ActivityContextManager.this.fireInactive();
			}

			@Override
			public long getLastEventTime() {
				return ActivityContextManager.this.getLastInteractionTime();
			}

			@Override
			public void addMonitoredActivityTime(long localStartTime,
					long currentTime) {
			}

			@Override
			public void active() {
				ActivityContextManager.this.fireActive();
			}
		});
	}

	private void fireActive() {
		for (IUserAttentionListener attentionListener : attentionListeners) {
			attentionListener.userAttentionGained();
		}
	}

	private void fireInactive() {
		for (IUserAttentionListener attentionListener : attentionListeners) {
			attentionListener.userAttentionLost();
		}
	}

	public long getLastInteractionTime() {
		for (final AbstractUserActivityMonitor monitor : activityMonitors) {
			final boolean[] success = new boolean[1];
			final long[] result = new long[1];
			SafeRunner.run(new ISafeRunnable() {

				@Override
				public void run() throws Exception {
					if (monitor.isEnabled()) {
						result[0] = monitor.getLastInteractionTime();
						lastInteractionOrigin = monitor.getOriginId();
						success[0] = true;
					}
				}

				@Override
				public void handleException(Throwable exception) {
					disableFailedMonitor(monitor, exception);
				}

			});
			if (success[0]) {
				return result[0];
			}
		}
		return -1;
	}

	private void disableFailedMonitor(AbstractUserActivityMonitor monitor,
			Throwable exception) {
		StatusHandler
				.log(new Status(
						IStatus.WARNING,
						FDUHelpSeekingPlugin.PLUGIN_ID,
						NLS.bind(
								"Activity monitor ''{0}'' was disabled due to a failure", monitor.getClass()), exception)); //$NON-NLS-1$
		activityMonitors.remove(monitor);
	}

	public void init(List<AbstractUserActivityMonitor> monitors) {
		this.activityMonitors.addAll(monitors);
	}

	public void start() {
		for (final AbstractUserActivityMonitor monitor : activityMonitors) {
			SafeRunner.run(new ISafeRunnable() {

				@Override
				public void run() throws Exception {
					monitor.start();
				}

				@Override
				public void handleException(Throwable exception) {
					disableFailedMonitor(monitor, exception);
				}
			});
		}
		updateWorkingSetSelection();
		if (PlatformUI.isWorkbenchRunning()) {
			PlatformUI.getWorkbench().getWorkingSetManager()
					.addPropertyChangeListener(WORKING_SET_CHANGE_LISTENER);
		}
		checkJob.reschedule();
	}

	public void stop() {
		checkJob.cancel();
		for (final AbstractUserActivityMonitor monitor : activityMonitors) {
			SafeRunner.run(new ISafeRunnable() {

				@Override
				public void run() throws Exception {
					monitor.stop();
				}

				@Override
				public void handleException(Throwable exception) {
					disableFailedMonitor(monitor, exception);
				}
			});
		}
		if (PlatformUI.isWorkbenchRunning()
				&& PlatformUI.getWorkbench().getWorkingSetManager() != null) {
			PlatformUI.getWorkbench().getWorkingSetManager()
					.removePropertyChangeListener(WORKING_SET_CHANGE_LISTENER);
		}
	}

	public void setInactivityTimeout(int inactivityTimeout) {
		checkJob.setInactivityTimeout(inactivityTimeout);
	}

}
