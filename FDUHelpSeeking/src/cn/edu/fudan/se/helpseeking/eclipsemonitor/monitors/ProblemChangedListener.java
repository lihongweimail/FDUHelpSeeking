package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.viewsupport.IProblemChangedListener;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.util.ASTUtil;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;

public class ProblemChangedListener extends AbstractUserActivityMonitor
		implements IProblemChangedListener {

	@Override
	public void problemsChanged(IResource[] changedResources,
			boolean isMarkerChange) {
		for (int i = 0; i < changedResources.length; i++) {
			IEditorPart part = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
			if (part instanceof CompilationUnitEditor) {
				final CompilationUnitEditor ce = (CompilationUnitEditor) part;
				ISourceViewer sv = ce.getViewer();
				if (sv instanceof JavaSourceViewer) {
					JavaSourceViewer jsv = (JavaSourceViewer) sv;
					jsv.addPostSelectionChangedListener(new ISelectionChangedListener() {
						@Override
						public void selectionChanged(SelectionChangedEvent event) {
							ISelection s = event.getSelection();
							if (s instanceof TextSelection) {
								ITextSelection ts = (ITextSelection) s;
								ITypeRoot typeRoot = JavaUI
										.getEditorInputTypeRoot(ce
												.getEditorInput());
								ICompilationUnit icu = (ICompilationUnit) typeRoot
										.getAdapter(ICompilationUnit.class);
								CompilationUnit cu = ASTUtil.parse(icu);
								try {
									IJavaElement e = icu.getElementAt(ts
											.getOffset());

									if (e != null) {
										ContextUtil.setContext(e);
										if (e.getElementType() == IJavaElement.METHOD) {
											IMethod method = (IMethod) e;
										} else if (e.getElementType() == IJavaElement.FIELD) {
											IMethod method = (IMethod) e
													.getAncestor(IJavaElement.METHOD);
											if (method == null) {
												return;
											} else {
											}
										}
									}
								} catch (JavaModelException e) {
									e.printStackTrace();
								}

							}
						}
					});
				}
			}

		}
	}

	@Override
	public void start() {
		JavaPlugin.getDefault().getProblemMarkerManager().addListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		JavaPlugin.getDefault().getProblemMarkerManager().removeListener(this);
		setEnabled(false);
	}

}
