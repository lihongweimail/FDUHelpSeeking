package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class ExecutionListener extends AbstractUserActivityMonitor implements
		IExecutionListener {

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteFailure(String commandId,
			ExecutionException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		System.out.println("Command Id: " + commandId
				+ ", Execution Event Parameters Size: "
				+ event.getParameters().size());
		InteractionEvent e = new InteractionEvent();
		e.setByuser(true);
		e.setKind(Kind.COMMAND);
		try {
			e.setOriginId(event.getCommand().getName() == null ? event
					.getCommand().getId() : event.getCommand().getName());
			System.out.println(event.getCommand().getName());
		} catch (NotDefinedException e1) {
			e1.printStackTrace();
		}
		DatabaseUtil.addInteractionEventToDatabase(e);
	}

	@Override
	public void start() {
		ICommandService command = (ICommandService) PlatformUI.getWorkbench()
				.getAdapter(ICommandService.class);
		command.addExecutionListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		ICommandService command = (ICommandService) PlatformUI.getWorkbench()
				.getAdapter(ICommandService.class);
		if (command == null) {
			return;
		}
		command.removeExecutionListener(this);
		setEnabled(false);
	}

}
