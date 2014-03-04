package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.MarkerItem;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class SelectionListener extends AbstractUserActivityMonitor implements
		ISelectionListener {

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.SELECTION);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			if (s.getFirstElement() == null) {
				return;
			} else {
				event.setOriginId("Select: " + s.getFirstElement().toString()
						+ " from Part: " + part.getTitle());

				Object object = s.getFirstElement();
				if (object instanceof IJavaElement) {
					IJavaElement element = (IJavaElement)object;
					ContextUtil.setContext(element);
				} else if (object instanceof MarkerItem) {
					MarkerItem entry = (MarkerItem) object;
				}
			}
		} else if (selection instanceof ITextSelection) {
			ITextSelection s = (ITextSelection) selection;
			if(s.getLength() == 0){
				return;//Don't record if length == 0
			}
			event.setOriginId("Select: " + s.getText() + " from Part: "
					+ part.getTitle());
		} else if (selection instanceof IMarkSelection) {
			IMarkSelection s = (IMarkSelection) selection;
			event.setOriginId("Select: " + s.getDocument().get()
					+ " from Part: " + part.getTitle());
		}
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPostSelectionListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.removePostSelectionListener(this);
		setEnabled(false);
	}

}
