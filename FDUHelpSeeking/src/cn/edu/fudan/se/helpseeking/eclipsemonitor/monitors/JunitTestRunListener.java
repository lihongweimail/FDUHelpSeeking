package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class JunitTestRunListener extends TestRunListener {

	@Override
	public void sessionFinished(ITestRunSession session) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.TEST);
		event.setOriginId("Session " + session.getTestRunName()
				+ " Finished. Elapsed Time: "
				+ session.getElapsedTimeInSeconds());
		DatabaseUtil.addInteractionEventToDatabase(event);
		super.sessionFinished(session);
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.TEST);
		event.setOriginId("Session " + session.getTestRunName() + " Launched.");
		DatabaseUtil.addInteractionEventToDatabase(event);
		super.sessionLaunched(session);
	}

	@Override
	public void sessionStarted(ITestRunSession session) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.TEST);
		event.setOriginId("Session " + session.getTestRunName() + " Started.");
		DatabaseUtil.addInteractionEventToDatabase(event);
		super.sessionStarted(session);
	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.TEST);
		event.setOriginId("Test Case Class "
				+ testCaseElement.getTestClassName() + " Method "
				+ testCaseElement.getTestMethodName()
				+ " Finished. Elapsed Time: "
				+ testCaseElement.getElapsedTimeInSeconds());
		DatabaseUtil.addInteractionEventToDatabase(event);
		super.testCaseFinished(testCaseElement);
	}

	@Override
	public void testCaseStarted(ITestCaseElement testCaseElement) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.TEST);
		event.setOriginId("Test Case Class "
				+ testCaseElement.getTestClassName() + " Method "
				+ testCaseElement.getTestMethodName() + " Started.");
		DatabaseUtil.addInteractionEventToDatabase(event);
		super.testCaseStarted(testCaseElement);
	}

}
