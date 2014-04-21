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

import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformationList;

@SuppressWarnings("restriction")
public class ProblemChangedListener extends AbstractUserActivityMonitor
		implements IProblemChangedListener {

	@Override
	public void problemsChanged(IResource[] changedResources,
			boolean isMarkerChange) {
		try {
			ProblemInformationList problemInformationList = ProblemInformationList.getInstance();
			//problemInformationList.clearProblemInformation();
			for (int i = 0; i < changedResources.length; i++) {

				IResource resource = changedResources[i];
				
				//System.out.println(resource.getLocation().toString());
				
				IJavaElement element = JavaCore.create(resource);
				if (element == null || element.getElementType() < IJavaElement.COMPILATION_UNIT) {
					break;
				}
				if (element.getElementType() > IJavaElement.COMPILATION_UNIT) {
					element = element
							.getAncestor(IJavaElement.COMPILATION_UNIT);
				}
				ICompilationUnit unit = (ICompilationUnit) element;
				IMarker[] markers = resource.findMarkers(null, true,
						IResource.DEPTH_ZERO);
				/*System.out
						.println("==================================\nResource #"
								+ i);*/
				for (int j = 0; j < markers.length; j++) {
					IMarker m = markers[j];
					ProblemInformation information = new ProblemInformation();
					
					information.setSeverity(m.getAttribute("severity", 0));
					information.setDescription(m.getAttribute("message", ""));
					information.setResource(m.getResource().getLocation().lastSegment());
					information.setPath(m.getResource().getLocation().toString());
					
					//System.out.println(information.getPath());
					
					information.setLineNumber(m.getAttribute("lineNumber", 0));
					information.setCharStart(m.getAttribute("charStart", 0));
					information.setCharEnd(m.getAttribute("charEnd", 0));
					information.setCause(m.getAttribute("arguments", ""));
					
					IJavaElement e = unit.getElementAt(m.getAttribute(
							"charStart", 0));
					if (e != null && e.getElementType() >= IJavaElement.METHOD) {
						e = e.getAncestor(IJavaElement.METHOD);
						if (e == null) {
							break;
						}
						IMethod method = (IMethod) e;
						information.setPath(method.getPath().toString());
						information.setRelatedMethod(method.getElementName());
						information.setSource(method.getSource());
					}
					
					problemInformationList.addProblemInformation(information);
					/*System.out.println("\nFile: "
							+ m.getResource().getLocation());
					for (String s : m.getAttributes().keySet()) {
						System.out.println("Attribute: " + s + ", Value: "
								+ m.getAttributes().get(s));
					}
					IJavaElement e = unit.getElementAt(m.getAttribute(
							"charStart", 0));

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
					
					if (e.getElementType() >= IJavaElement.METHOD) {
						e = e.getAncestor(IJavaElement.METHOD);
						if (e == null) {
							break;
						}
						IMethod method = (IMethod) e;
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
					}*/
				}

			}
			ProblemInformationList.getInstance().prettyPrint();

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void start() {
		JavaPlugin.getDefault().getProblemMarkerManager().addListener(this);
//		System.out.println("Problems listener add!");
		setEnabled(true);
	}

	@Override
	public void stop() {
		JavaPlugin.getDefault().getProblemMarkerManager().removeListener(this);
		setEnabled(false);
	}

}
