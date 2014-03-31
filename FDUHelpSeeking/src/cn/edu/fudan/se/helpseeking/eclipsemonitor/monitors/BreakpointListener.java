package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.util.Calendar;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaLineBreakpoint;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaMethodBreakpoint;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

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
					DatabaseUtil.addInteractionEventToDatabase(e);
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
