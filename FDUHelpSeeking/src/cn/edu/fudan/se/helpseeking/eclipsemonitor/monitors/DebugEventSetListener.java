package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.Basic.DebugAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.ConsoleInformationUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.ProblemInformationUtil;

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
			e.setKind(Kind.DEBUG);
			e.setByuser(true);
			int kind = event.getKind();
			int detail = event.getDetail();
			String kindString = "";
			String detailString = "";
			String originId = "Unspecified";
			String actionNameKind="";
			String actionNameDetail="";
			String actionName = "Unspecified";
			
			switch (kind) {
			case DebugEvent.RESUME:
				kindString = "Resume";
				actionNameKind=DebugAction.Resume.toString();//"Resume";
				break;
			case DebugEvent.SUSPEND:
				kindString = "Suspend";
				actionNameKind=DebugAction.Suspend.toString();//"Suspend";
				break;
			case DebugEvent.CREATE:
				kindString = "Create";
				actionNameKind="Create";
				break;
			case DebugEvent.TERMINATE:
				kindString = "Terminate";
				actionNameKind=DebugAction.Terminate.toString();//"Terminate";
				break;
			case DebugEvent.CHANGE:
				kindString = "Change";
				actionNameKind="Change";
				break;
			case DebugEvent.MODEL_SPECIFIC:
				kindString = "Model Specific";
				actionNameKind="ModelSpecific";
				break;
			}
			switch (detail) {
			case DebugEvent.STEP_INTO:
				detailString = "Setp Into";
				actionNameDetail=DebugAction.StepInto.toString();//"StepInto";
				break;
			case DebugEvent.STEP_RETURN:
				detailString = "Step Return";
				actionNameDetail=DebugAction.StepOver.toString();//"StepReturn";
				break;
			case DebugEvent.STEP_END:
				detailString = "Step End";
				actionNameDetail=DebugAction.StepEnd.toString();//"StepEnd";
				break;
			case DebugEvent.STEP_OVER:
				detailString = "Step Over";
				actionNameDetail=DebugAction.StepOver.toString();//"StepOver";
				break;
				
			case DebugEvent.BREAKPOINT:
				detailString = "Breakpoint";
				actionNameDetail="Breakpoint";
				break;
			case DebugEvent.CLIENT_REQUEST:
				detailString = "Client Request";
				actionNameDetail="ClientRequest";
				break;
			case DebugEvent.EVALUATION:
				detailString = "Evaluation";
				actionNameDetail="Evaluation";
				break;
			case DebugEvent.EVALUATION_IMPLICIT:
				detailString = "Evaluation Implicit";
				actionNameDetail="EvaluationImplicit";
				break;
			case DebugEvent.STATE:
				detailString = "State";
				actionNameDetail="State";
				break;
			case DebugEvent.CONTENT:
				detailString = "Content";
				actionNameDetail="Content";
				break;
			}
			if (kindString.equals("")) {
				originId = "Kind: Unspecified, ";
				actionNameDetail= "Kind: Unspecified";
			} else {
				originId = "Kind: " + kindString + ", ";
			}
			
			
			if (detailString.equals("")) {
				originId += "Detail: Unspecified";
				actionNameDetail="Detail: Unspecified";
			} else {
				originId += "Detail: " + detailString;
				
			}
			
			
			e.setOriginId(originId);
			actionName=actionNameKind+" "+actionNameDetail;
			e.setActionName(actionName);
			
			Information info = new Information();
			info.setType("DebugEvent");
			info.setDebugCode(null);
			info.setEditCode(null);
			info.setExplorerRelated(null);
			info.setIdeOutput(null);
			Action action = new Action();
			action.setTime(new Timestamp(System.currentTimeMillis()));
			action.setByuser(true);
			action.setActionKind(e.getKind());
			action.setActionName(e.getActionName());
			action.setDescription(e.getOriginId());
			info.setAction(action);
			
						
			
			//需要先写入数据库，才能得到ID
			int actionid=DatabaseUtil.addInformationToDatabase(info);
			
			//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据					
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
					|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
					|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
					|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage().getActivePart())) {						
				Cache.getInstance().addInformationToCache(info,actionid);
			}
			
			
			
			
			DatabaseUtil.addInteractionEventToDatabase(e);
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
