package cn.edu.fudan.se.helpseeking.eclipsemonitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;

public class CheckActivityJob extends Job {

	private final IActivityManagerCallback callback;

	private final int ACTIVE_TICK = 30 * 1000;// 30seconds
	protected long tick = ACTIVE_TICK;
	private boolean active;
	private int inactivityTimeout;
	protected long previousEventTime;

	public CheckActivityJob(IActivityManagerCallback callback) {
		super(Messages.CheckActivityJob_Activity_Monitor_Job);
		this.callback = callback;
	}

	public void reschedule() {
		schedule(active ? tick : tick / 6);
	}

	protected boolean isEnabled() {
		return Platform.isRunning() && !PlatformUI.getWorkbench().isClosing();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			if (isEnabled()) {
				try {
					long lastEventTime = callback.getLastEventTime();
					long currentTime = System.currentTimeMillis();
					if ((currentTime - lastEventTime) >= inactivityTimeout
							&& inactivityTimeout != 0) {
						if (active) {
							active = false;
							callback.inactive();
						}
					} else {
						if (!active) {
							active = true;
							if (inactivityTimeout != 0) {
								previousEventTime = lastEventTime;
							} else {
								previousEventTime = currentTime;
							}
							callback.active();
						} else {
							if (currentTime - previousEventTime > tick * 3) {
								if (inactivityTimeout != 0) {
									if (currentTime - lastEventTime <= tick) {
										previousEventTime = lastEventTime;
									} else {
										active = false;
										callback.inactive();
									}
								} else {
									previousEventTime = currentTime;
								}
							} else {
								callback.addMonitoredActivityTime(
										previousEventTime, currentTime);
								previousEventTime = currentTime;
							}
						}
					}
				} finally {
					reschedule();
				}
			}
		} catch (Throwable t) {

		}
		return Status.OK_STATUS;
	}

	public boolean isActive() {
		return active;
	}

	public int getInactivityTimeout() {
		return this.inactivityTimeout;
	}

	public void setInactivityTimeout(int inactivityTimeout) {
		this.inactivityTimeout = inactivityTimeout;
	}
}
