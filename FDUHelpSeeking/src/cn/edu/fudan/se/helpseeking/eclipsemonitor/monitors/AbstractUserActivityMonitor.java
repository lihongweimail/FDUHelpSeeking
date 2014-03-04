package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

public abstract class AbstractUserActivityMonitor {
	private boolean isEnabled = false;

	private long lastEventTimeStamp = -1;

	public long getLastInteractionTime() {
		synchronized (this) {
			return lastEventTimeStamp;
		}
	}

	public void setLastEventTime(long lastEventTime) {
		synchronized (this) {
			lastEventTimeStamp = lastEventTime;
		}
	}

	public abstract void start();

	public abstract void stop();

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public String getOriginId() {
		return null;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
