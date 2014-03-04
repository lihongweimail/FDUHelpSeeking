package cn.edu.fudan.se.helpseeking.eclipsemonitor.views;

import org.eclipse.jface.resource.ImageDescriptor;

public class Element {

	public String name = "";
	public ImageDescriptor icon = null;
	public String last = "";
	public Element[] children = new Element[0];
	public Element parent = null;

	public Element(String name, ImageDescriptor icon, String last) {
		super();
		this.name = name;
		this.icon = icon;
		this.last = last;
	}

	public Element(String name, ImageDescriptor icon, String last,
			Element[] children) {
		super();
		this.name = name;
		this.icon = icon;
		this.last = last;
		this.children = children;
	}

	public static Element[] example() {
		return new Element[] { new Element(
				"EclipseMonitor",
				Images.PACKAGES,
				"1 hr 35 min",
				new Element[] {
						new Element("cn.edu.fudan.se.helpseeking.eclipsemonitor",
								Images.PACKAGE, "7 min",
								new Element[] { new Element(
										"EclipseMonitorPlugin", Images.CLASS,
										"7 min", new Element[] { new Element(
												"init()", Images.METHPRI_OBJ,
												"7 min") }) }),
						new Element(
								"cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors",
								Images.PACKAGE,
								"1 hr 14 min",
								new Element[] {
										new Element(
												"BreakpointListener",
												Images.CLASS,
												"28 min",
												new Element[] {
														new Element(
																"breakpointAdded(IBreakpoint)",
																Images.METHPUB_OBJ,
																"11 min"),
														new Element(
																"breakpointChanged(IBreakpoint, IMarkerDelta)",
																Images.METHPUB_OBJ,
																"8 min"),
														new Element(
																"breakpointRemoved(IBreakpoint, IMarkerDelta)",
																Images.METHPUB_OBJ,
																"5 min"),
														new Element(
																"start()",
																Images.METHPRO_OBJ,
																"2 min"),
														new Element(
																"stop()",
																Images.METHPRO_OBJ,
																"2 min") }),
										new Element(
												"DebugEventSetListener",
												Images.CLASS,
												"19 min",
												new Element[] {
														new Element(
																"handleDebugEvents(DebugEvent[])",
																Images.METHPUB_OBJ,
																"17 min"),
														new Element(
																"start()",
																Images.METHPRO_OBJ,
																"1 min"),
														new Element(
																"stop()",
																Images.METHPRO_OBJ,
																"1 min") }),
										new Element(
												"SelectionListener",
												Images.CLASS,
												"27 min",
												new Element[] {
														new Element(
																"selectionChanged(IWorkbenchPart, ISelection)",
																Images.METHPUB_OBJ,
																"25 min"),
														new Element(
																"start()",
																Images.METHPRO_OBJ,
																"1 min"),
														new Element(
																"stop()",
																Images.METHPRO_OBJ,
																"1 min") }) }),
						new Element(
								"cn.edu.fudan.se.helpseeking.eclipsemonitor.util",
								Images.PACKAGE,
								"12 min",
								new Element[] { new Element(
										"DatabaseUtil",
										Images.CLASS,
										"12 min",
										new Element[] {
												new Element(
														"addInteractionToDatabase(InteractionEvent)",
														Images.METHPUB_OBJ,
														"9 min"),
												new Element("getCon()",
														Images.METHPUB_OBJ,
														"1 min"),
												new Element("init()",
														Images.METHPUB_OBJ,
														"2 min") }) }),
						new Element(
								"cn.edu.fudan.se.helpseeking.eclipsemonitor.util",
								Images.PACKAGE,
								"2 min",
								new Element[] { new Element(
										"DatabaseUtil",
										Images.CLASS,
										"2 min",
										new Element[] { new Element(
												"addInteractionToDatabase(InteractionEvent)",
												Images.METHPUB_OBJ, "2 min") }) }) }) };
	}

	public String toString() {
		return name;
	}
}
