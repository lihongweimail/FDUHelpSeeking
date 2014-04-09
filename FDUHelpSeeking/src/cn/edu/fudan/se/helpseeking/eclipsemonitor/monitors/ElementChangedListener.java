package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

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

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

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
		int kind = delta.getKind();
		int flags = delta.getFlags();
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
			switch (e.getElementType()) {
			case IJavaElement.FIELD:
				event.setOriginId("Add Field: " + e.getHandleIdentifier());
				event.setActionName("AddField");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Add Method: " + m.getHandleIdentifier());
				event.setActionName("AddMethod");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
			
				event.setOriginId("Add Import Declaration: "
						+ id.getHandleIdentifier());
					event.setActionName("AddImportDeclaration");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Add Class: " + t.getHandleIdentifier());
				event.setActionName("AddClass");
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
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Delete Method: " + m.getHandleIdentifier());
				event.setActionName("DeleteMethod");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
				event.setOriginId("Delete Import Declaration: "
						+ id.getHandleIdentifier());
				event.setActionName("DeleteImportDeclaration");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Delete Class: " + t.getHandleIdentifier());
				event.setActionName("DeleteClass");
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
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.METHOD:
				IMethod m = (IMethod) e;
				event.setOriginId("Change Method: " + m.getHandleIdentifier());
				event.setActionName("ChangeMethod");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.IMPORT_DECLARATION:
				IImportDeclaration id = (IImportDeclaration) e;
				event.setOriginId("Change Import Declaration: "
						+ id.getHandleIdentifier());
				event.setActionName("ChangeImportDeclaration");
				DatabaseUtil.addInteractionEventToDatabase(event);
				break;
			case IJavaElement.TYPE:
				IType t = (IType) e;
				event.setOriginId("Change Class: " + t.getHandleIdentifier());
				event.setActionName("ChangeClass");
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
