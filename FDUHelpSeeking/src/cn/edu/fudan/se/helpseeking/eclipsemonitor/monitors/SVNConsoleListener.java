package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.io.File;

import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.client.IConsoleListener;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class SVNConsoleListener extends AbstractUserActivityMonitor implements
		IConsoleListener {

	@Override
	public void logCommandLine(String arg0) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Command Line: " + arg0);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void logCompleted(String arg0) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Completed: " + arg0);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void logError(String arg0) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Error: " + arg0);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void logMessage(String arg0) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Message: " + arg0);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void logRevision(long arg0, String arg1) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Revision: " + arg1 + " on Revision " + arg0);
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void onNotify(File arg0, SVNNodeKind arg1) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.REVISION);
		event.setOriginId("SVN Notify : " + arg1.toString() + ", File: "
				+ arg0.getPath() + arg0.getName());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void setCommand(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		SVNProviderPlugin.getPlugin().setConsoleListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		SVNProviderPlugin.getPlugin().setConsoleListener(null);
		setEnabled(false);
	}

}
