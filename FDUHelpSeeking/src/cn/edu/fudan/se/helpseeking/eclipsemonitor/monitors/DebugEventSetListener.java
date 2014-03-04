package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class DebugEventSetListener extends AbstractUserActivityMonitor
		implements IDebugEventSetListener {

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (DebugEvent event : events) {
//			System.out.println("Event source:" + event.getSource());
//			System.out.println("Event kind:" + event.getKind());
//			System.out.println("Event detail:" + event.getDetail());
//			System.out.println("Event data:" + event.getData());
			InteractionEvent e = new InteractionEvent();
			e.setKind(InteractionEvent.Kind.DEBUG);
			e.setByuser(true);
			int kind = event.getKind();
			int detail = event.getDetail();
			String kindString = "";
			String detailString = "";
			String originId = "Unspecified";
			switch (kind) {
			case DebugEvent.RESUME:
				kindString = "Resume";
				break;
			case DebugEvent.SUSPEND:
				kindString = "Suspend";
				break;
			case DebugEvent.CREATE:
				kindString = "Create";
				break;
			case DebugEvent.TERMINATE:
				kindString = "Terminate";
				break;
			case DebugEvent.CHANGE:
				kindString = "Change";
				break;
			case DebugEvent.MODEL_SPECIFIC:
				kindString = "Model Specific";
				break;
			}
			switch (detail) {
			case DebugEvent.STEP_INTO:
				detailString = "Setp Into";
				break;
			case DebugEvent.STEP_RETURN:
				detailString = "Step Return";
				break;
			case DebugEvent.STEP_END:
				detailString = "Step End";
				break;
			case DebugEvent.BREAKPOINT:
				detailString = "Breakpoint";
				break;
			case DebugEvent.CLIENT_REQUEST:
				detailString = "Client Request";
				break;
			case DebugEvent.EVALUATION:
				detailString = "Evaluation";
				break;
			case DebugEvent.EVALUATION_IMPLICIT:
				detailString = "Evaluation Implicit";
				break;
			case DebugEvent.STATE:
				detailString = "State";
				break;
			case DebugEvent.CONTENT:
				detailString = "Content";
				break;
			}
			if (kindString.equals("")) {
				originId = "Kind: Unspecified, ";
			} else {
				originId = "Kind: " + kindString + ", ";
			}
			if (detailString.equals("")) {
				originId += "Detail: Unspecified";
			} else {
				originId += "Detail: " + detailString;
			}
			e.setOriginId(originId);
			DatabaseUtil.addInteractionToDatabase(e);
		}
	}

	@Override
	public void start() {
		DebugPlugin.getDefault().addDebugEventListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		DebugPlugin.getDefault().removeDebugEventListener(this);
		setEnabled(false);
	}

}
