package cn.edu.fudan.se.helpseeking.eclipsemonitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.internal.runtime.PlatformActivator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;

public class StatusHandler {

	public static void log(IStatus status) {
		if (InternalPlatform.getDefault() != null
				&& PlatformActivator.getContext() != null) {
			ILog log = InternalPlatform.getDefault().getLog(
					PlatformActivator.getContext().getBundle());
			if (log != null) {
				log.log(status);
			}
		}
		dumpErrorToConsole(status);
	}

	private static void dumpErrorToConsole(IStatus status) {
		StringBuilder sb = new StringBuilder();
		sb.append("["); //$NON-NLS-1$
		Calendar now = Calendar.getInstance();
		sb.append(DateUtil.getIsoFormattedDateTime(now));
		sb.append("] "); //$NON-NLS-1$
		sb.append(status.toString() + ", "); //$NON-NLS-1$
		if (status.getException() != null) {
			sb.append("Exception:\n"); //$NON-NLS-1$
			sb.append(printStrackTrace(status.getException()));
		}
		System.err.println(sb.toString());
	}

	private static String printStrackTrace(Throwable t) {
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
