package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

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
	   System.out.println("I am a part of  class: "+part.getClass().toString());
		
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
			
		DatabaseUtil.addInformationToDatabase(info);
		Cache.getInstance().addInformationToCache(info);
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
								if (e instanceof IMethod) {
									IMethod m = (IMethod) e;
									ASTParser parser = ASTParser
											.newParser(AST.JLS4);
									parser.setKind(ASTParser.K_COMPILATION_UNIT);
									parser.setSource(m.getSource()
											.toCharArray());
									parser.setResolveBindings(true);
									CompilationUnit cu = (CompilationUnit) parser
											.createAST(null);
									System.out.println(cu == null);
									cu.accept(new ASTVisitor() {

										@Override
										public boolean visit(IfStatement node) {
											System.out.println(node.getLength());
											return super.visit(node);
										}

										@Override
										public void preVisit(ASTNode node) {
											// TODO Auto-generated method stub
											super.preVisit(node);
										}

										@Override
										public boolean preVisit2(ASTNode node) {
											// TODO Auto-generated method stub
											return super.preVisit2(node);
										}

										@Override
										public void postVisit(ASTNode node) {
											// TODO Auto-generated method stub
											super.postVisit(node);
										}

										@Override
										public boolean visit(
												AnnotationTypeDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												AnnotationTypeMemberDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												AnonymousClassDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ArrayAccess node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ArrayCreation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ArrayInitializer node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ArrayType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												AssertStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(Assignment node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(Block node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(BlockComment node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(BooleanLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(BreakStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(CastExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(CatchClause node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												CharacterLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ClassInstanceCreation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												CompilationUnit node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ConditionalExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ConstructorInvocation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ContinueStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(DoStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(EmptyStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												EnhancedForStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												EnumConstantDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												EnumDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ExpressionStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(FieldAccess node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												FieldDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ForStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ImportDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												InfixExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												InstanceofExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(Initializer node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(Javadoc node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												LabeledStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(LineComment node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												MarkerAnnotation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(MemberRef node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												MemberValuePair node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(MethodRef node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												MethodRefParameter node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												MethodDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												MethodInvocation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(Modifier node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												NormalAnnotation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(NullLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(NumberLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												PackageDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ParameterizedType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ParenthesizedExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												PostfixExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												PrefixExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(PrimitiveType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(QualifiedName node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(QualifiedType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												ReturnStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(SimpleName node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(SimpleType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SingleMemberAnnotation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SingleVariableDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(StringLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SuperConstructorInvocation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SuperFieldAccess node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SuperMethodInvocation node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(SwitchCase node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SwitchStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												SynchronizedStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(TagElement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(TextElement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ThisExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(ThrowStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(TryStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												TypeDeclaration node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												TypeDeclarationStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(TypeLiteral node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(TypeParameter node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(UnionType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												VariableDeclarationExpression node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												VariableDeclarationStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(
												VariableDeclarationFragment node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(WhileStatement node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public boolean visit(WildcardType node) {
											// TODO Auto-generated method stub
											return super.visit(node);
										}

										@Override
										public void endVisit(
												AnnotationTypeDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												AnnotationTypeMemberDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												AnonymousClassDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ArrayAccess node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ArrayCreation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ArrayInitializer node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ArrayType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												AssertStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(Assignment node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(Block node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(BlockComment node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(BooleanLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(BreakStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(CastExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(CatchClause node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												CharacterLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ClassInstanceCreation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												CompilationUnit node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ConditionalExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ConstructorInvocation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ContinueStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(DoStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(EmptyStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												EnhancedForStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												EnumConstantDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												EnumDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ExpressionStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(FieldAccess node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												FieldDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ForStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(IfStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ImportDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												InfixExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												InstanceofExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(Initializer node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(Javadoc node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												LabeledStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(LineComment node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												MarkerAnnotation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(MemberRef node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												MemberValuePair node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(MethodRef node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												MethodRefParameter node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												MethodDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												MethodInvocation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(Modifier node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												NormalAnnotation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(NullLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(NumberLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												PackageDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ParameterizedType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ParenthesizedExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												PostfixExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												PrefixExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(PrimitiveType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(QualifiedName node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(QualifiedType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												ReturnStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(SimpleName node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(SimpleType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SingleMemberAnnotation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SingleVariableDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(StringLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SuperConstructorInvocation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SuperFieldAccess node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SuperMethodInvocation node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(SwitchCase node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SwitchStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												SynchronizedStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(TagElement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(TextElement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ThisExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(ThrowStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(TryStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												TypeDeclaration node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												TypeDeclarationStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(TypeLiteral node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(TypeParameter node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(UnionType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												VariableDeclarationExpression node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												VariableDeclarationStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(
												VariableDeclarationFragment node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(WhileStatement node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public void endVisit(WildcardType node) {
											// TODO Auto-generated method stub
											super.endVisit(node);
										}

										@Override
										public int hashCode() {
											// TODO Auto-generated method stub
											return super.hashCode();
										}

										@Override
										public boolean equals(Object obj) {
											// TODO Auto-generated method stub
											return super.equals(obj);
										}

										@Override
										protected Object clone()
												throws CloneNotSupportedException {
											// TODO Auto-generated method stub
											return super.clone();
										}

										@Override
										public String toString() {
											// TODO Auto-generated method stub
											return super.toString();
										}

										@Override
										protected void finalize()
												throws Throwable {
											// TODO Auto-generated method stub
											super.finalize();
										}

									});
								}
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
						System.out.println(model.getAnnotationIterator().toString());
						
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
		if (part instanceof CompilationUnitEditor) {
			ContextUtil.deleteEditor((CompilationUnitEditor) part);
		}
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
			
		DatabaseUtil.addInformationToDatabase(info);
		Cache.getInstance().addInformationToCache(info);
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
