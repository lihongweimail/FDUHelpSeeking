package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;
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
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.ISourceViewer;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.EditorInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerRelated;
import cn.edu.fudan.se.helpseeking.bean.Information;
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
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Part Activated: " + part.getTitle());
//	   System.out.println("I am a part of  class: "+part.getClass().toString());
		
		if (ExceptionalPartAndView.checkPartAndView(part)) {
			return;
			
		}

		//add hongwei for exploerrelated 14-04-15
		Information info=new Information();
		Action ac=new Action();
		ac.setByuser(event.isByuser());
		ac.setActionKind(event.getKind());
		ac.setActionName("Part Activated");
		ac.setDescription(part.getTitle());
		info.setAction(ac);
		ExplorerRelated er=new ExplorerRelated();
		EditorInfo edi=new EditorInfo();
	
		
		if (part instanceof CompilationUnitEditor) {
			edi.addSize();
			edi.getClassQualifiedNameList().add(part.getTitle());
			er.setEditorInfo(edi);
			}

		
		info.setExplorerRelated(er);
			
		//需要先写入数据库，才能得到ID
		int actionid1=DatabaseUtil.addInformationToDatabase(info);
		Cache.getInstance().addInformationToCache(info,actionid1);
//    add end 		
		DatabaseUtil.addInteractionEventToDatabase(event);

		
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
								e.setKind(Kind.EDIT);
								e.setOriginId("Content Assist Selected: "
										+ proposal.getDisplayString());
								e.setActionName("UseAssist");
								
								Information info = new Information();
								info.setType("Assist");
								Action action = new Action();
								action.setActionKind(e.getKind());
								action.setActionName(e.getActionName());
								action.setByuser(true);
								action.setDescription(e.getOriginId());
								action.setTime(new Timestamp(System.currentTimeMillis()));
								info.setAction(action);
								
								//需要先写入数据库，才能得到ID
								int actionid=DatabaseUtil.addInformationToDatabase(info);

								//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据				
								if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null 
										|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null
										|| PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() == null
										|| !ExceptionalPartAndView.checkPartAndView(PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow().getActivePage().getActivePart())) {
													
									Cache.getInstance().addInformationToCache(info,actionid);
								}
								
								DatabaseUtil.addInteractionEventToDatabase(e);
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
				jsv.getVisualAnnotationModel().addAnnotationModelListener(new IAnnotationModelListener() {
					
					@Override
					public void modelChanged(IAnnotationModel model) {
//						System.out.println(model.getAnnotationIterator().toString());
						
					}
				});
				IDocument doc = jsv.getDocument();

				doc.addDocumentListener(new IDocumentListener() {

					@Override
					public void documentChanged(DocumentEvent event) {
						// InteractionEvent e = new InteractionEvent();
						// e.setByuser(true);
						// e.setKind(Kind.EDIT);
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
						event.setKind(Kind.COMMAND);
						event.setOriginId("Help Command");
						event.setByuser(true);
						DatabaseUtil.addInteractionEventToDatabase(event);
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
						event.setKind(Kind.EDIT);
						event.setOriginId("Scroll Horizontally: X: " + e.x
								+ ", Y: " + e.y);
						DatabaseUtil.addInteractionEventToDatabase(event);
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
						event.setKind(Kind.EDIT);
						event.setOriginId("Scroll Vertically: X: " + e.x
								+ ", Y: " + e.y);
						DatabaseUtil.addInteractionEventToDatabase(event);
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
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Part Brought To Top: " + part.getTitle());
		
		if (ExceptionalPartAndView.checkPartAndView(part)) {
			return;
		}	
		
		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Part Closed: " + part.getTitle());
		
		if (ExceptionalPartAndView.checkPartAndView(part)) {
			return;
		}	
		
		DatabaseUtil.addInteractionEventToDatabase(event);
		//注销关闭编辑器后，比较内容。
//		if (part instanceof CompilationUnitEditor) {
//			ContextUtil.deleteEditor((CompilationUnitEditor) part);
//		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Part Deactivated: " + part.getTitle());
		
		if (ExceptionalPartAndView.checkPartAndView(part)) {
			return;
		}	
		
		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(false);
		event.setKind(Kind.ATTENTION);
		event.setOriginId("Part Opened: " + part.getTitle());
		
		//TODO 
		
		if (ExceptionalPartAndView.checkPartAndView(part)) {
			return;
		}	
		//add hongwei for exploerrelated 14-04-15
		Information info=new Information();
		Action ac=new Action();
		ac.setByuser(event.isByuser());
		ac.setActionKind(event.getKind());
		ac.setActionName("Part Activated");
		ac.setDescription(part.getTitle());
		info.setAction(ac);
		ExplorerRelated er=new ExplorerRelated();
		EditorInfo edi=new EditorInfo();
//		ExplorerInfo exi=new ExplorerInfo();
		
		if (part.getClass().toString().equals("org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor")) {
			edi.addSize();
			edi.getClassQualifiedNameList().add(part.getTitle());
			er.setEditorInfo(edi);
			}
//		if (part.getClass().toString().equals("org.eclipse.ui.navigator.resources.ProjectExplorer")) {
//			exi.addSize();
//			exi.getSelectObjectNameList().add(part.getTitle());
//			er.setExplorerInfo(exi);
//			}
		
		info.setExplorerRelated(er);
			
		//需要先写入数据库，才能得到ID
		int actionid1=DatabaseUtil.addInformationToDatabase(info);
		Cache.getInstance().addInformationToCache(info,actionid1);
//    add end 	
		
		
		DatabaseUtil.addInteractionEventToDatabase(event);
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
