package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.eclipse.ltk.core.refactoring.history.RefactoringHistoryEvent;

import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class RefactoringHistoryListener extends AbstractUserActivityMonitor
		implements IRefactoringHistoryListener {

	@Override
	public void historyNotification(RefactoringHistoryEvent event) {
		InteractionEvent e = new InteractionEvent();
		e.setByuser(true);
		e.setKind(Basic.Kind.EDIT);
		int type = event.getEventType();
		String t = "";
		switch (type) {
		case RefactoringExecutionEvent.ABOUT_TO_PERFORM:
			t = "about to be performed";
			break;
		case RefactoringExecutionEvent.ABOUT_TO_REDO:
			t = "about to be redone";
			break;
		case RefactoringExecutionEvent.ABOUT_TO_UNDO:
			t = "about to be undone";
			break;
		case RefactoringExecutionEvent.PERFORMED:
			t = "has been performed";
			break;
		case RefactoringExecutionEvent.REDONE:
			t = "has been redone";
			break;
		case RefactoringExecutionEvent.UNDONE:
			t = "has been undone";
			break;
		}
		String originId = "";
		originId = "Refactor Event Type: " + t + ", Refactor Description: "
				+ event.getDescriptor().getDescription();
		e.setOriginId(originId);
		DatabaseUtil.addInteractionEventToDatabase(e);
	}

	@Override
	public void start() {
		RefactoringCore.getHistoryService().addHistoryListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		RefactoringCore.getHistoryService().removeHistoryListener(this);
		setEnabled(false);
	}

}
