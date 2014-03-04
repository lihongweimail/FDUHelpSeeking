package cn.edu.fudan.se.helpseeking.eclipsemonitor;

public interface IActivityManagerCallback {

	public abstract void inactive();
	
	public abstract void active();
	
	public abstract void addMonitoredActivityTime(long localStartTime, long currentTime);
	
	public abstract long getLastEventTime();
}
