package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.jdt.junit.JUnitCore;

public class TestRunListener extends AbstractUserActivityMonitor {

	private JunitTestRunListener listener = new JunitTestRunListener();

	@Override
	public void start() {
		JUnitCore.addTestRunListener(listener);
		setEnabled(true);
	}

	@Override
	public void stop() {
		JUnitCore.removeTestRunListener(listener);
		setEnabled(false);
	}

}
