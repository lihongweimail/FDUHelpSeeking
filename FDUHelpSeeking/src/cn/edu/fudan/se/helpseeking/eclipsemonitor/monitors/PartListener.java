package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionListener;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class PartListener extends AbstractUserActivityMonitor implements
		IPartListener {

	@SuppressWarnings("restriction")
	@Override
	public void partActivated(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Part Activated: " + part.getTitle());
		DatabaseUtil.addInteractionToDatabase(event);

		if (part instanceof CompilationUnitEditor) {
			ContextUtil.addEditor((CompilationUnitEditor) part);
			final CompilationUnitEditor ce = (CompilationUnitEditor) part;
			ISourceViewer sv = ce.getViewer();
			if (sv instanceof JavaSourceViewer) {
				JavaSourceViewer jsv = (JavaSourceViewer) sv;
				jsv.getContentAssistantFacade().addCompletionListener(
						new ICompletionListener() {

							@Override
							public void selectionChanged(
									ICompletionProposal proposal,
									boolean smartToggle) {
								InteractionEvent e = new InteractionEvent();
								e.setByuser(true);
								e.setKind(InteractionEvent.Kind.EDIT);
								e.setOriginId("Content Assist Selected: "
										+ proposal.getDisplayString());
								DatabaseUtil.addInteractionToDatabase(e);
							}

							@Override
							public void assistSessionStarted(
									ContentAssistEvent event) {
							}

							@Override
							public void assistSessionEnded(
									ContentAssistEvent event) {
								// TODO Auto-generated method stub

							}
						});
				jsv.addPostSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection s = event.getSelection();
						if (s instanceof TextSelection) {
							ITextSelection ts = (ITextSelection) s;
							ITypeRoot typeRoot = JavaUI
									.getEditorInputTypeRoot(ce.getEditorInput());
							ICompilationUnit icu = (ICompilationUnit) typeRoot
									.getAdapter(ICompilationUnit.class);
							try {
								IJavaElement e = icu.getElementAt(ts
										.getOffset());
								if (e != null) {
									ContextUtil.setContext(e);
								}
							} catch (JavaModelException e) {
								e.printStackTrace();
							}

						}
					}
				});
				IDocument doc = jsv.getDocument();

				doc.addDocumentListener(new IDocumentListener() {

					@Override
					public void documentChanged(DocumentEvent event) {
						// InteractionEvent e = new InteractionEvent();
						// e.setByuser(true);
						// e.setKind(InteractionEvent.Kind.EDIT);
						// e.setDelta(event.getText());
						// System.out.println(event.getText());
						// DatabaseUtil.addInteractionToDatabase(e);
						// System.out.println(event);
					}

					@Override
					public void documentAboutToBeChanged(DocumentEvent event) {

					}
				});
				StyledText st = jsv.getTextWidget();
				st.addKeyListener(new KeyListener() {

					@Override
					public void keyReleased(KeyEvent e) {
						// System.out.println("Key " + e.keyCode
						// + " released, Time: " + e.time);
					}

					@Override
					public void keyPressed(KeyEvent e) {
						// System.out.println("Key " + e.keyCode
						// + " pressed, Time: " + e.time);
					}
				});
				st.addHelpListener(new HelpListener() {

					@Override
					public void helpRequested(HelpEvent e) {
						InteractionEvent event = new InteractionEvent();
						event.setKind(InteractionEvent.Kind.COMMAND);
						event.setOriginId("Help Command");
						event.setByuser(true);
						DatabaseUtil.addInteractionToDatabase(event);
					}
				});
				st.addMouseListener(new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						// System.out.println("Mouse Up: " + e.x + ", " + e.y
						// + ", Time: " + e.time);

					}

					@Override
					public void mouseDown(MouseEvent e) {
						// System.out.println("Mouse Down: " + e.x + ", " + e.y
						// + ", Time: " + e.time);

					}

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// System.out.println("Mouse Double Click: " + e.x +
						// ", "
						// + e.y + ", Time: " + e.time);

					}
				});

				ScrollBar hBar = st.getHorizontalBar();
				ScrollBar vBar = st.getVerticalBar();
				hBar.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						// System.out.println("HBar Event: " + e.time);
						InteractionEvent event = new InteractionEvent();
						event.setByuser(true);
						event.setKind(InteractionEvent.Kind.EDIT);
						event.setOriginId("Scroll Horizontally: X: " + e.x
								+ ", Y: " + e.y);
						DatabaseUtil.addInteractionToDatabase(event);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// System.out.println("HBar Event: " + e.time);

					}
				});
				vBar.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						InteractionEvent event = new InteractionEvent();
						event.setByuser(true);
						event.setKind(InteractionEvent.Kind.EDIT);
						event.setOriginId("Scroll Vertically: X: " + e.x
								+ ", Y: " + e.y);
						DatabaseUtil.addInteractionToDatabase(event);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// System.out.println("VBar Event: " + e.time);
					}
				});
			}

		}

	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Part Brought To Top: " + part.getTitle());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Part Closed: " + part.getTitle());
		DatabaseUtil.addInteractionToDatabase(event);
		if (part instanceof CompilationUnitEditor) {
			ContextUtil.deleteEditor((CompilationUnitEditor) part);
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Part Deactivated: " + part.getTitle());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(InteractionEvent.Kind.ATTENTION);
		event.setOriginId("Part Opened: " + part.getTitle());
		DatabaseUtil.addInteractionToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.removePartListener(this);
		setEnabled(false);
	}

	private CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setProject(unit.getJavaProject());
		IPath path = unit.getPath();
		parser.setUnitName(path.toString());
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

}
