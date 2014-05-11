package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.ConsoleInformationUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.ProblemInformationUtil;

@SuppressWarnings("restriction")
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
		/*System.out.println("Command Id: " + commandId
				+ ", Execution Event Parameters Size: "
				+ event.getParameters().size());*/
		InteractionEvent e = new InteractionEvent();
		e.setByuser(true);
		e.setKind(Kind.COMMAND);
		Kind mykind=Kind.COMMAND;
		String actionName="";
		String mytype="command";
		
		ICompilationUnit icu;
		

		
		try {
			

			
			e.setOriginId(event.getCommand().getName() == null ? event
					.getCommand().getId() : event.getCommand().getName());
			/*if (event.getCommand().getName().trim().equals("Inline edit")) {
				System.out.println("inline edit position:"+event.getCommand()
				.getName().trim());
			}*/
			
			actionName=event.getCommand().getName() == null ? event
					.getCommand().getId() : event.getCommand().getName();
			
			if(event.getCommand().getName().equals("Save")){
				mytype="command";
				actionName="save";
				mykind=Kind.COMMAND;
				//System.out.println("Yeah~");
			IEditorPart unitEditor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(unitEditor instanceof CompilationUnitEditor){
				ITypeRoot typeRoot = JavaUI
						.getEditorInputTypeRoot(unitEditor.getEditorInput());
				 icu = (ICompilationUnit) typeRoot
						.getAdapter(ICompilationUnit.class);
				 String fileName = icu.getPath().toString();
				
			
					Cache.getInstance().removeEditCodeAsDeleteOrProblemViewChange(fileName);
					//System.out.println(fileName);
					}
				
			}else 			
			if(event.getCommand().getName().equals("Copy")){
				mytype="edit";
				actionName="Copy";
				mykind=Kind.EDIT;
				
			}else
			if(event.getCommand().getName().equals("Cut")){
				mytype="edit";
				actionName="Cut";
				mykind=Kind.EDIT;
			}else
			if(event.getCommand().getName().equals("Paste")){
				mytype="edit";
				actionName="Paste";
				mykind=Kind.EDIT;
			}else
			if(event.getCommand().getName().equals("Delete")){
				mytype="edit";
				actionName="Delete";
				mykind=Kind.EDIT;
			}else
			if(event.getCommand().getName().equals("Undo")){
				mytype="edit";
				actionName="Undo";
				mykind=Kind.EDIT;
			}else
			if(event.getCommand().getName().equals("Import")){
				mytype="edit";
				actionName="Import";
				mykind=Kind.EDIT;
			}else
			if(event.getCommand().getName().equals("Resume")){
				mytype="debug";
				actionName="Resume";
				mykind=Kind.DEBUG;
			}else
			if(event.getCommand().getName().equals("Step Over")){
				mytype="debug";
				actionName="StepOver";
				mykind=Kind.DEBUG;
			}else
			if(event.getCommand().getName().equals("Run Java Application")){
				mytype="debug";
				actionName="RunJavaApplication";
				mykind=Kind.DEBUG;
			}

	
			
			e.setActionName(actionName);
			
			
		} catch (NotDefinedException e1) {
			e1.printStackTrace();
		}
		
		Information info = new Information();
		info.setType(mytype);
		info.setDebugCode(null);
		info.setEditCode(null);
		info.setExplorerRelated(null);
		info.setIdeOutput(null);
		
		Action action = new Action();
		action.setTime(new Timestamp(System.currentTimeMillis()));
		action.setByuser(true);
		action.setActionKind(mykind);
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
