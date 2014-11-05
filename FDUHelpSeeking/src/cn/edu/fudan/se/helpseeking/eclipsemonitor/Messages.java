package cn.edu.fudan.se.helpseeking.eclipsemonitor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "cn.edu.fudan.se.helpseeking.eclipsemonitor.messages";

	public static String DateUtil_ago="";

	public static String DateUtil_day="";

	public static String DateUtil_days="";

	public static String DateUtil_hour="";

	public static String DateUtil_hours="";

	public static String DateUtil_in="";

	public static String DateUtil_minute="";

	public static String DateUtil_minutes="";

	public static String DateUtil_month_multi="";

	public static String DateUtil_month_single="";

	public static String DateUtil_second="";

	public static String DateUtil_seconds="";

	public static String DateUtil_week="";

	public static String DateUtil_weeks="";

	static {
		reloadMessages();
	}

	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String CheckActivityJob_Activity_Monitor_Job;
}
