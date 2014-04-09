package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.IProblemChangedListener;

public class ProblemChangedListener extends AbstractUserActivityMonitor
		implements IProblemChangedListener {

	@SuppressWarnings("restriction")
	@Override
	public void problemsChanged(IResource[] changedResources,
			boolean isMarkerChange) {
		try {
			for (int i = 0; i < changedResources.length; i++) {

				IResource resource = changedResources[i];
				IJavaElement element = JavaCore.create(resource);
				if (element.getElementType() < IJavaElement.COMPILATION_UNIT) {
					break;
				}
				if (element.getElementType() > IJavaElement.COMPILATION_UNIT) {
					element = element
							.getAncestor(IJavaElement.COMPILATION_UNIT);
				}
				ICompilationUnit unit = (ICompilationUnit) element;
				IMarker[] markers = resource.findMarkers(null, true,
						IResource.DEPTH_ZERO);
				System.out
						.println("==================================\nResource #"
								+ i);
				for (int j = 0; j < markers.length; j++) {
					IMarker m = markers[j];
					System.out.println("\nFile: "
							+ m.getResource().getLocation());
					for (String s : m.getAttributes().keySet()) {
						System.out.println("Attribute: " + s + ", Value: "
								+ m.getAttributes().get(s));
					}
					IJavaElement e = unit.getElementAt(m.getAttribute(
							"charStart", 0));
					if (e.getElementType() >= IJavaElement.METHOD) {
						e = e.getAncestor(IJavaElement.METHOD);
						if (e == null) {
							break;
						}
						IMethod method = (IMethod) e;
						String severity = "";
						switch (m.getAttribute("severity", 0)) {
						case IMarker.SEVERITY_ERROR:
							severity = "error";
							break;
						case IMarker.SEVERITY_WARNING:
							severity = "warning";
							break;
						case IMarker.SEVERITY_INFO:
							severity = "information";
						}
						System.out
								.println("-----------------------------------------------------\nRelated Method: "
										+ method.getElementName()
										+ "\t\tSeverity Level: "
										+ severity
										+ "\t\tLine Number:"
										+ m.getAttribute("lineNumber", 0)
										+ "\nMethod Path: "
										+ method.getPath()
										+ "\nDescription of "
										+ severity
										+ ": "
										+ m.getAttribute("message", "")
										+ "\nChar Start:"
										+ m.getAttribute("charStart", 0)
										+ "\t\tChar End:"
										+ m.getAttribute("charEnd", 0)
										+ "\t\tArguments cause to "
										+ severity
										+ ":" + m.getAttribute("arguments", ""));
						System.out.println("Source code of " + severity
								+ " method "

								+ method.getElementName() + ":");
						System.out.println(method.getSource());
						System.out.println();
					}
				}

			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void start() {
		JavaPlugin.getDefault().getProblemMarkerManager().addListener(this);
		System.out.println("Problems listener add!");
		setEnabled(true);
	}

	@Override
	public void stop() {
		JavaPlugin.getDefault().getProblemMarkerManager().removeListener(this);
		setEnabled(false);
	}

}
