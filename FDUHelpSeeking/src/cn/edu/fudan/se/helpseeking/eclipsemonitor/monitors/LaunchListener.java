package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchListener;

import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;

public class LaunchListener extends AbstractUserActivityMonitor implements
		ILaunchListener {

	@Override
	public void launchRemoved(ILaunch launch) {
		/*InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Basic.Kind.DEBUG);
		try {
			event.setOriginId("Remove launch: "
					+ launch.getDebugTarget().getName());
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseUtil.addInteractionEventToDatabase(event);*/
	}

	@Override
	public void launchAdded(ILaunch launch) {
		//System.out.println("New launch!");
		ConsoleInformationList.getInstance().clearConsoleInformation();
		/*InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Basic.Kind.DEBUG);
		try {
			event.setOriginId("Add launch: "
					+ launch.getDebugTarget().getName());
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseUtil.addInteractionEventToDatabase(event);*/
	}

	@Override
	public void launchChanged(ILaunch launch) {
		/*InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Basic.Kind.DEBUG);
		try {
			event.setOriginId("Change launch: "
					+ launch.getDebugTarget().getName());
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseUtil.addInteractionEventToDatabase(event);*/
	}

	@Override
	public void start() {
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		DebugPlugin.getDefault().getLaunchManager().removeLaunchListener(this);
		setEnabled(false);
	}

}
