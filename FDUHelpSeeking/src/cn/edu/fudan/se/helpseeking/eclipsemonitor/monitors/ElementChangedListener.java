package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.ConsoleInformationUtil;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.ProblemInformationUtil;

public class ElementChangedListener extends AbstractUserActivityMonitor
		implements IElementChangedListener {

	@Override
	public void elementChanged(ElementChangedEvent arg0) {
		processDelta(arg0.getDelta());
	}

	@Override
	public void start() {
		JavaCore.addElementChangedListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		JavaCore.removeElementChangedListener(this);
		setEnabled(false);
	}

	protected void processDelta(IJavaElementDelta delta) {
		//int kind = delta.getKind();
		//int flags = delta.getFlags();
		List<IJavaElementDelta> additions = new ArrayList<IJavaElementDelta>();
		List<IJavaElementDelta> deletions = new ArrayList<IJavaElementDelta>();
		List<IJavaElementDelta> changes = new ArrayList<IJavaElementDelta>();
		

		traverse(delta, additions, deletions, changes);

		if (additions.size() + deletions.size() + changes.size() == 0) {
			return;
		}
		for (IJavaElementDelta d : additions) {
			InteractionEvent event = new InteractionEvent();
			event.setByuser(true);
			event.setKind(Kind.EDIT);
			IJavaElement e = d.getElement();
			ContextUtil.setContext(e);
			//add end 

			
			switch (e.getElementType()) {
			case IJavaElement.FIELD:
				event.setOriginId("Add Field: " + e.getHandleIdentifier());
				event.setActionName("AddField");
				
				Information fieldInfo = new Information();
				fieldInfo.setType("EditCode");
				fieldInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action fieldAction = new Action();
				fieldAction.setTime(new Timestamp(System.currentTimeMillis()));
				fieldAction.setActionKind(event.getKind());
				fieldAction.setActionName(event.getActionName());
				fieldAction.setDescription(event.getOriginId());
				fieldAction.setByuser(true);
				fieldInfo.setAction(fieldAction);
				
				IDEOutput fieldIdeOutput = new IDEOutput();
				fieldIdeOutput.setCompileInformation(ProblemInformationUtil
						.SelectProblemInformationByCursor(fieldInfo.getEditCode().getCursor()));
				fieldIdeOutput.setRuntimeInformation(ConsoleInformationUtil
						.SelectConsoleInformationByCursor(fieldInfo.getEditCode().getCursor()));
				fieldInfo.setIdeOutput(fieldIdeOutput);				
				
				//需要先写入数据库，才能得到ID
				int actionid=DatabaseUtil.addInformationToDatabase(fieldInfo);

				//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据				
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
						|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage().getActivePart())) {
									
					Cache.getInstance().addInformationToCache(fieldInfo,actionid);
				}
				
				
        			DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Add Method: " + m.getHandleIdentifier());
				event.setActionName("AddMethod");
				
				Information methodInfo = new Information();
				methodInfo.setType("EditCode");
				methodInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action methodAction = new Action();
				methodAction.setTime(new Timestamp(System.currentTimeMillis()));
				methodAction.setActionKind(event.getKind());
				methodAction.setActionName(event.getActionName());
				methodAction.setDescription(event.getOriginId());
				methodAction.setByuser(true);
				methodInfo.setAction(methodAction);
				
				IDEOutput methodIdeOutput = new IDEOutput();
				methodIdeOutput.setCompileInformation(ProblemInformationUtil
						.SelectProblemInformationByCursor(methodInfo.getEditCode().getCursor()));
				methodIdeOutput.setRuntimeInformation(ConsoleInformationUtil
						.SelectConsoleInformationByCursor(methodInfo.getEditCode().getCursor()));
				methodInfo.setIdeOutput(methodIdeOutput);				
				
				//需要先写入数据库，才能得到ID
				int actionid2=DatabaseUtil.addInformationToDatabase(methodInfo);
			
				//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
				|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
				|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
				|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().getActivePart())) {
					
					Cache.getInstance().addInformationToCache(methodInfo,actionid2);
				}

		
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
			
				event.setOriginId("Add Import Declaration: "
						+ id.getHandleIdentifier());
				event.setActionName("AddImportDeclaration");
					
				Information importInfo = new Information();
				importInfo.setType("EditCode");
				importInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action importAction = new Action();
				importAction.setTime(new Timestamp(System.currentTimeMillis()));
				importAction.setActionKind(event.getKind());
				importAction.setActionName(event.getActionName());
				importAction.setDescription(event.getOriginId());
				importAction.setByuser(true);
				importInfo.setAction(importAction);
				
				IDEOutput importIdeOutput = new IDEOutput();
				importIdeOutput.setCompileInformation(ProblemInformationUtil
						.SelectProblemInformationByCursor(importInfo.getEditCode().getCursor()));
				importIdeOutput.setRuntimeInformation(ConsoleInformationUtil
						.SelectConsoleInformationByCursor(importInfo.getEditCode().getCursor()));
				importInfo.setIdeOutput(importIdeOutput);
				
				//需要先写入数据库，才能得到ID
				int actionid3=DatabaseUtil.addInformationToDatabase(importInfo);


				//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
						|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage().getActivePart())) {
					
					Cache.getInstance().addInformationToCache(importInfo,actionid3);
				}

	
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Add Class: " + t.getHandleIdentifier());
				event.setActionName("AddClass");
				
				Information typeInfo = new Information();
				typeInfo.setType("EditCode");
				typeInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action typeAction = new Action();
				typeAction.setTime(new Timestamp(System.currentTimeMillis()));
				typeAction.setActionKind(event.getKind());
				typeAction.setActionName(event.getActionName());
				typeAction.setDescription(event.getOriginId());
				typeAction.setByuser(true);
				typeInfo.setAction(typeAction);
				
				IDEOutput typeIdeOutput = new IDEOutput();
				typeIdeOutput.setCompileInformation(ProblemInformationUtil
						.SelectProblemInformationByCursor(typeInfo.getEditCode().getCursor()));
				typeIdeOutput.setRuntimeInformation(ConsoleInformationUtil
						.SelectConsoleInformationByCursor(typeInfo.getEditCode().getCursor()));
				typeInfo.setIdeOutput(typeIdeOutput);
				
				//需要先写入数据库，才能得到ID
				int actionid1=DatabaseUtil.addInformationToDatabase(typeInfo);
				
				//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
						|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
						|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage().getActivePart())) {
					
					Cache.getInstance().addInformationToCache(typeInfo,actionid1);
				}

							
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			}
		}
		for (IJavaElementDelta d : deletions) {
			InteractionEvent event = new InteractionEvent();
			event.setByuser(true);
			event.setKind(Kind.EDIT);
			IJavaElement e = d.getElement();
			IJavaElement parent = e.getParent();
			while (parent == null) {
				parent = parent.getParent() == null ? null : parent.getParent();
			}
			ContextUtil.setContext(parent);
			switch (e.getElementType()) {
			case IJavaElement.FIELD:
				event.setOriginId("Delete Field: " + e.getHandleIdentifier());
				event.setActionName("DeleteField");

				
				//TODO  20140411 hognwei移除注释为测试			
/*				Information fieldInfo = new Information();
				fieldInfo.setType("EditCode");
				fieldInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action fieldAction = new Action();
				fieldAction.setTime(new Timestamp(System.currentTimeMillis()));
				fieldAction.setActionKind(event.getKind());
				fieldAction.setActionName(event.getActionName());
				fieldAction.setDescription(event.getOriginId());
				fieldAction.setByuser(true);
				fieldInfo.setAction(fieldAction);
				DatabaseUtil.addInformationToDatabase(fieldInfo);*/
				
//				Cache.getInstance().addInformationToCache(fieldInfo);
				
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Delete Method: " + m.getHandleIdentifier());
				event.setActionName("DeleteMethod");
				
				/*Information methodInfo = new Information();
				methodInfo.setType("EditCode");
				methodInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action methodAction = new Action();
				methodAction.setTime(new Timestamp(System.currentTimeMillis()));
				methodAction.setActionKind(event.getKind());
				methodAction.setActionName(event.getActionName());
				methodAction.setDescription(event.getOriginId());
				methodAction.setByuser(true);
				methodInfo.setAction(methodAction);
				DatabaseUtil.addInformationToDatabase(methodInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
				event.setOriginId("Delete Import Declaration: "
						+ id.getHandleIdentifier());
				event.setActionName("DeleteImportDeclaration");
				
				/*Information importInfo = new Information();
				importInfo.setType("EditCode");
				importInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action importAction = new Action();
				importAction.setTime(new Timestamp(System.currentTimeMillis()));
				importAction.setActionKind(event.getKind());
				importAction.setActionName(event.getActionName());
				importAction.setDescription(event.getOriginId());
				importAction.setByuser(true);
				importInfo.setAction(importAction);
				DatabaseUtil.addInformationToDatabase(importInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Delete Class: " + t.getHandleIdentifier());
				event.setActionName("DeleteClass");
				

/*				Information typeInfo = new Information();
				typeInfo.setType("EditCode");
				typeInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action typeAction = new Action();
				typeAction.setTime(new Timestamp(System.currentTimeMillis()));
				typeAction.setActionKind(event.getKind());
				typeAction.setActionName(event.getActionName());
				typeAction.setDescription(event.getOriginId());
				typeAction.setByuser(true);
				typeInfo.setAction(typeAction);
				DatabaseUtil.addInformationToDatabase(typeInfo);*/

				
				DatabaseUtil.addInteractionEventToDatabase(event);
				
				
				break;
			}
		}
		for (IJavaElementDelta d : changes) {
			InteractionEvent event = new InteractionEvent();
			event.setByuser(true);
			event.setKind(Kind.EDIT);
			IJavaElement e = d.getElement();
			ContextUtil.setContext(e);
			switch (e.getElementType()) {
			case IJavaElement.FIELD:
				event.setOriginId("Change Field: " + e.getHandleIdentifier());
				event.setActionName("ChangeField");
				
				/*Information fieldInfo = new Information();
				fieldInfo.setType("EditCode");
				fieldInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action fieldAction = new Action();
				fieldAction.setTime(new Timestamp(System.currentTimeMillis()));
				fieldAction.setActionKind(event.getKind());
				fieldAction.setActionName(event.getActionName());
				fieldAction.setDescription(event.getOriginId());
				fieldAction.setByuser(true);
				fieldInfo.setAction(fieldAction);
				DatabaseUtil.addInformationToDatabase(fieldInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Change Method: " + m.getHandleIdentifier());
				event.setActionName("ChangeMethod");
				
				/*Information methodInfo = new Information();
				methodInfo.setType("EditCode");
				methodInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action methodAction = new Action();
				methodAction.setTime(new Timestamp(System.currentTimeMillis()));
				methodAction.setActionKind(event.getKind());
				methodAction.setActionName(event.getActionName());
				methodAction.setDescription(event.getOriginId());
				methodAction.setByuser(true);
				methodInfo.setAction(methodAction);
				DatabaseUtil.addInformationToDatabase(methodInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
				event.setOriginId("Change Import Declaration: "
						+ id.getHandleIdentifier());
				event.setActionName("ChangeImportDeclaration");
				
				/*Information importInfo = new Information();
				importInfo.setType("EditCode");
				importInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action importAction = new Action();
				importAction.setTime(new Timestamp(System.currentTimeMillis()));
				importAction.setActionKind(event.getKind());
				importAction.setActionName(event.getActionName());
				importAction.setDescription(event.getOriginId());
				importAction.setByuser(true);
				importInfo.setAction(importAction);
				DatabaseUtil.addInformationToDatabase(importInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Change Class: " + t.getHandleIdentifier());
				event.setActionName("ChangeClass");
				
				/*Information typeInfo = new Information();
				typeInfo.setType("EditCode");
				typeInfo.setEditCode(CodeUtil.createEditCodeByJavaElement(e));
				Action typeAction = new Action();
				typeAction.setTime(new Timestamp(System.currentTimeMillis()));
				typeAction.setActionKind(event.getKind());
				typeAction.setActionName(event.getActionName());
				typeAction.setDescription(event.getOriginId());
				typeAction.setByuser(true);
				typeInfo.setAction(typeAction);
				DatabaseUtil.addInformationToDatabase(typeInfo);*/
				
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			}
		}

	}

	private void traverse(IJavaElementDelta delta,
			List<IJavaElementDelta> additions,
			List<IJavaElementDelta> deletions, List<IJavaElementDelta> changes) {
		if (delta.getKind() == IJavaElementDelta.ADDED) {
			additions.add(delta);
		} else if (delta.getKind() == IJavaElementDelta.REMOVED) {
			deletions.add(delta);
		} else if (delta.getKind() == IJavaElementDelta.CHANGED) {
			changes.add(delta);
		}
		IJavaElementDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			traverse(children[i], additions, deletions, changes);
		}
	}

	private boolean isOnClassPath(ICompilationUnit element) {
		IJavaProject project = element.getJavaProject();
		if (project == null || !project.exists()) {
			return false;
		}
		return project.isOnClasspath(element);
	}
}
