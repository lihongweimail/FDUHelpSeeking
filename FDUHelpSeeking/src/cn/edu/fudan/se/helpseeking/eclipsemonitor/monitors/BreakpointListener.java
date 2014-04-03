package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;
import java.util.Calendar;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaLineBreakpoint;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaMethodBreakpoint;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

@SuppressWarnings("restriction")
public class BreakpointListener extends AbstractUserActivityMonitor implements
		IBreakpointListener {

	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		try {
			if (isEnabled()) {
				if (!(breakpoint instanceof IJavaLineBreakpoint)) {
					return;
				} else {
					InteractionEvent e = new InteractionEvent();
					e.setByuser(true);
					e.setDate(Calendar.getInstance().getTime());
					e.setKind(Kind.DEBUG);
					if (breakpoint instanceof JavaLineBreakpoint) {
						JavaLineBreakpoint bp = (JavaLineBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Add a breakpoint at line "
								+ bp.getLineNumber());
					} else if (breakpoint instanceof JavaMethodBreakpoint) {
						JavaMethodBreakpoint bp = (JavaMethodBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Add a method breakpoint at line "
								+ bp.getLineNumber());
					}
					IResource resource = breakpoint.getMarker().getResource();
					ICompilationUnit icu = JavaCore.createCompilationUnitFrom((IFile) resource);

					Information info = new Information();
					info.setType("DebugCode");
					info.setDebugCode(CodeUtil.createDebugCodeByBreakpoint(icu, breakpoint));
					Action action = new Action();
					action.setTime(new Timestamp(System.currentTimeMillis()));
					action.setByuser(true);
					action.setActionKind(e.getKind());
					action.setActionName("Add Breakpoint");
					action.setDescription(e.getOriginId());
					info.setAction(action);
					//DatabaseUtil.addInformationToDatabase(info);
					//DatabaseUtil.addInteractionEventToDatabase(e);
				}
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		try {
			if (isEnabled()) {
				if (!(breakpoint instanceof IJavaLineBreakpoint)) {
					return;
				} else {
					InteractionEvent e = new InteractionEvent();
					e.setByuser(true);
					e.setDate(Calendar.getInstance().getTime());
					e.setKind(Kind.DEBUG);
					if (breakpoint instanceof JavaLineBreakpoint) {
						JavaLineBreakpoint bp = (JavaLineBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Delete a breakpoint at line "
								+ bp.getLineNumber());
					} else if (breakpoint instanceof JavaMethodBreakpoint) {
						JavaMethodBreakpoint bp = (JavaMethodBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Delete a method breakpoint at line "
								+ bp.getLineNumber());
					}
					DatabaseUtil.addInteractionEventToDatabase(e);
				}
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		try {
			if (isEnabled()) {
				if (!(breakpoint instanceof IJavaLineBreakpoint)) {
					return;
				} else {
					InteractionEvent e = new InteractionEvent();
					e.setByuser(true);
					e.setDate(Calendar.getInstance().getTime());
					e.setKind(Kind.DEBUG);
					if (breakpoint instanceof JavaLineBreakpoint) {
						JavaLineBreakpoint bp = (JavaLineBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Change a breakpoint at line "
								+ bp.getLineNumber());
					} else if (breakpoint instanceof JavaMethodBreakpoint) {
						JavaMethodBreakpoint bp = (JavaMethodBreakpoint) breakpoint;
						e.setLineno(String.valueOf(bp.getLineNumber()));
						e.setOriginId("Change a method breakpoint at line "
								+ bp.getLineNumber());
					}
					DatabaseUtil.addInteractionEventToDatabase(e);
				}
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void start() {
		DebugPlugin.getDefault().getBreakpointManager()
				.addBreakpointListener(this);

		setEnabled(true);
	}

	@Override
	public void stop() {
		DebugPlugin.getDefault().getBreakpointManager()
				.removeBreakpointListener(this);
		setEnabled(false);
	}

}
