package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class ResourceChangeListener extends AbstractUserActivityMonitor
		implements IResourceChangeListener, IResourceDeltaVisitor {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			event.getDelta().accept(this);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.RESOURCE);
		StringBuffer buf = new StringBuffer();
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			buf.append("Added: ");
			break;
		case IResourceDelta.REMOVED:
			buf.append("Removed: ");
			break;
		case IResourceDelta.CHANGED:
			buf.append("Changed: ");
			break;
		case IResourceDelta.COPIED_FROM:
			buf.append("Copied From: ");
			break;
		case IResourceDelta.MOVED_FROM:
			buf.append("Moved From: ");
			break;
		case IResourceDelta.MOVED_TO:
			buf.append("Moved To: ");
			break;
		case IResourceDelta.OPEN:
			buf.append("Open: ");
			break;
		default:
			buf.append("[");
			buf.append(delta.getKind());
			buf.append("]");
			break;
		}
		buf.append(" ");
		buf.append(delta.getResource());
		event.setOriginId(buf.toString());
		DatabaseUtil.addInteractionToDatabase(event);
		return true;
	}

	@Override
	public void start() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		setEnabled(false);
	}

}
