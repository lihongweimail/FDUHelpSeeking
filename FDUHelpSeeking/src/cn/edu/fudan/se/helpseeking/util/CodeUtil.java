package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaLineBreakpoint;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaMethodBreakpoint;
import org.eclipse.jface.text.ITextSelection;

import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.ClassModel;
import cn.edu.fudan.se.helpseeking.bean.Cursor;
import cn.edu.fudan.se.helpseeking.bean.DebugCode;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.SyntacticBlock;

@SuppressWarnings("restriction")
public class CodeUtil {

	public static EditCode createEditCodeBySelection(ICompilationUnit unit,
			ITextSelection s) {
		String path = unit.getPath().toString().replace("/", ".");
		path = path.substring(path.indexOf(".") + 1);
		path = path.substring(path.indexOf(".") + 1);
		path = path.substring(path.indexOf(".") + 1);
		final String prefix = path.substring(0, path.length() / 2);
		
		EditCode ec = new EditCode();

		// TODO exception name
		String methodname = "";
		SyntacticBlock sblock = new SyntacticBlock();
		try {
			IJavaElement element = unit.getElementAt(s.getOffset());
			if (element instanceof IField) {
				sblock.setType("Field");
				sblock.setCode(((IField) element).getSource());
			} else if (element instanceof IMethod) {
				sblock.setType("Method");
				sblock.setCode(((IMethod) element).getSource());
				methodname = ((IMethod) element).getDeclaringType().getPackageFragment()
						.getElementName() + "." + ((IMethod) element).getDeclaringType()
						.getElementName() + "." + ((IMethod) element).getElementName();
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		ec.setSyntacticBlock(sblock);

		// TODO internalCaller upClass
		ClassModel cmodel = new ClassModel();
		cmodel.setType(sblock.getType());
		cmodel.setCode(sblock.getCode());

		final CompilationUnit cu = ASTUtil.parse(unit);
		final ArrayList<ASTNode> nodes = new ArrayList<>();
		final ArrayList<Integer> linenumbers = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			public boolean visit(FieldDeclaration node) {
				int lineNumber = cu.getLineNumber(node.getStartPosition()) - 1;
				nodes.add(node);
				linenumbers.add(lineNumber);
				return true;
			}

			public boolean visit(MethodDeclaration node) {
				int lineNumber = cu.getLineNumber(node.getStartPosition()) - 1;
				nodes.add(node);
				linenumbers.add(lineNumber);
				return true;
			}
		});
		int index = 0;
		for (Integer linenumber : linenumbers) {
			if (linenumber <= s.getStartLine()) {
				index = linenumbers.indexOf(linenumber);
			} else if (linenumber > s.getStartLine())
				break;
		}
		ASTNode node = nodes.get(index);

		final List<String> internalCallee = new ArrayList<>();
		final List<String> belowClass = new ArrayList<>();
		node.accept(new ASTVisitor() {
			public boolean visit(SimpleName name) {
				IBinding binding = name.resolveBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IField) {					
						String classString = ((IField) element).getDeclaringType()
							.getPackageFragment() .getElementName() + "." 
							+ ((IField) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IField) element)
								.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}

			public boolean visit(MethodInvocation method) {
				IMethodBinding binding = method.resolveMethodBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IMethod) {
						String classString = ((IMethod) element).getDeclaringType()
							.getPackageFragment().getElementName() + "." 
							+ ((IMethod) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IMethod) element)
								.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}

			public boolean visit(ClassInstanceCreation creation) {
				IMethodBinding binding = creation.resolveConstructorBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IMethod) {
						String classString = ((IMethod) element).getDeclaringType()
							.getPackageFragment().getElementName() + "." 
							+ ((IMethod) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IMethod) element)
								.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}
		});

		cmodel.setBelowClass(belowClass);
		cmodel.setInternalCallee(internalCallee);
		ec.setClassModel(cmodel);

		Cursor c = new Cursor();
		c.setLineNo(s.getStartLine());
		c.setMethodQualifiedName(methodname);
		c.setFileName(unit.getPath().toString());
		ec.setCursor(c);

		return ec;
	}

	public static EditCode createEditCodeByJavaElement(IJavaElement element) {
 		EditCode ec = new EditCode();
		Cursor c = new Cursor();

		// TODO exception name
		SyntacticBlock sblock = new SyntacticBlock();
		ICompilationUnit icu = null;
		if (element instanceof IField) {
			sblock.setType("Field");
			try {
				sblock.setCode(((IField) element).getSource());
				icu = ((IField) element).getCompilationUnit();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		} else if (element instanceof IMethod) {
			sblock.setType("Method");
			try {
				sblock.setCode(((IMethod) element).getSource());
				icu = ((IMethod) element).getCompilationUnit();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		} else if (element instanceof IImportDeclaration) {
			sblock.setType("Import");
			try {
				sblock.setCode(((IImportDeclaration) element).getSource());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		} else if (element instanceof IType) {
			sblock.setType("Class");
			try {
				sblock.setCode(((IType) element).getSource());
				icu = ((IType) element).getCompilationUnit();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		ec.setSyntacticBlock(sblock);

		// TODO internalCaller upClass
		ClassModel cmodel = new ClassModel();
		cmodel.setType(sblock.getType());
		cmodel.setCode(sblock.getCode());
		if (icu != null) {
			String path = icu.getPath().toString().replace("/", ".");
			path = path.substring(path.indexOf(".") + 1);
			path = path.substring(path.indexOf(".") + 1);
			path = path.substring(path.indexOf(".") + 1);
			final String prefix = path.substring(0, path.length() / 2);
			c.setFileName(icu.getPath().toString());
			
			CompilationUnit cu = ASTUtil.parse(icu);
			IJavaElement[] elements = { element };
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setSource(icu);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);
			IBinding[] bindings = parser.createBindings(elements, null);
			ASTNode node = null;
			if(bindings[0] != null){
				node = cu.findDeclaringNode(bindings[0].getKey());
			}
			
			if(node != null){
				c.setLineNo(cu.getLineNumber(node.getStartPosition()) - 1);
				
				final List<String> internalCallee = new ArrayList<>();
				final List<String> belowClass = new ArrayList<>();
				node.accept(new ASTVisitor() {
					public boolean visit(SimpleName name) {
						IBinding binding = name.resolveBinding();
						if(binding != null){
							IJavaElement element = binding.getJavaElement();
							if (element instanceof IField) {
								String classString = ((IField) element).getDeclaringType()
										.getPackageFragment().getElementName() + "." 
										+ ((IField) element).getDeclaringType().getElementName();
								internalCallee.add(classString + "." 
										+ ((IField) element).getElementName());
								belowClass.add(classString);
							}
						}
						return true;
					}

					public boolean visit(MethodInvocation method) {
						IMethodBinding binding = method.resolveMethodBinding();
						if(binding != null){
							IJavaElement element = binding.getJavaElement();
							if (element instanceof IMethod) {
								String classString = ((IMethod) element).getDeclaringType()
										.getPackageFragment().getElementName() + "." 
										+ ((IMethod) element).getDeclaringType().getElementName();
								internalCallee.add(classString + "." 
										+ ((IMethod) element).getElementName());
								belowClass.add(classString);
							}
						}
						return true;
					}

					public boolean visit(ClassInstanceCreation creation) {
						IMethodBinding binding = creation
							.resolveConstructorBinding();
						if(binding != null){
							IJavaElement element = binding.getJavaElement();
							if (element instanceof IMethod) {
								String classString = ((IMethod) element).getDeclaringType()
										.getPackageFragment().getElementName() + "." 
										+ ((IMethod) element).getDeclaringType().getElementName();
								internalCallee.add(classString + "." 
										+ ((IMethod) element).getElementName());
								belowClass.add(classString);
							}
						}
						return true;
					}
				});

				cmodel.setBelowClass(belowClass);
				cmodel.setInternalCallee(internalCallee);
			}
		}
		ec.setClassModel(cmodel);
		
		ec.setCursor(c);

		return ec;
	}

	public static DebugCode createDebugCodeByBreakpoint(ICompilationUnit unit,
			IBreakpoint bp) {
		String path = unit.getPath().toString().replace("/", ".");
		path = path.substring(path.indexOf(".") + 1);
		path = path.substring(path.indexOf(".") + 1);
		path = path.substring(path.indexOf(".") + 1);
		final String prefix = path.substring(0, path.length() / 2);
		
		DebugCode dc = new DebugCode();

		Breakpoint bpoint = new Breakpoint();
		bpoint.setFileName(unit.getPath().toString());
		//System.out.println(bpoint.getFileName());
		if (bp instanceof JavaMethodBreakpoint) {
			bpoint.setType("Method");
			try {
				bpoint.setLineNo(((JavaMethodBreakpoint) bp).getLineNumber());
				bpoint.setMethodQualifiedName(((JavaMethodBreakpoint) bp).getTypeName()	+ "."
						+ ((JavaMethodBreakpoint) bp).getMethodName());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else if (bp instanceof JavaLineBreakpoint) {
			bpoint.setType("Line");
			try {
				bpoint.setLineNo(((JavaLineBreakpoint) bp).getLineNumber());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		dc.setBreakpoint(bpoint);

		final CompilationUnit cu = ASTUtil.parse(unit);
		final ArrayList<ASTNode> nodes = new ArrayList<>();
		final ArrayList<Integer> linenumbers = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			public boolean visit(FieldDeclaration node) {
				int lineNumber = cu.getLineNumber(node.getStartPosition()) - 1;
				nodes.add(node);
				linenumbers.add(lineNumber);
				return true;
			}

			public boolean visit(MethodDeclaration node) {
				int lineNumber = cu.getLineNumber(node.getStartPosition()) - 1;
				nodes.add(node);
				linenumbers.add(lineNumber);
				return true;
			}
		});
		int index = 0;
		for (Integer linenumber : linenumbers) {
			if (linenumber <= bpoint.getLineNo()) {
				index = linenumbers.indexOf(linenumber);
			} else if (linenumber > bpoint.getLineNo())
				break;
		}
		ASTNode node = nodes.get(index);

		// TODO exception name
		SyntacticBlock sblock = new SyntacticBlock();
		try {
			IJavaElement element = unit.getElementAt(node.getStartPosition());
			if (element instanceof IField) {
				sblock.setType("Field");
				sblock.setCode(((IField) element).getSource());
			} else if (element instanceof IMethod) {
				sblock.setType("Method");
				sblock.setCode(((IMethod) element).getSource());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		dc.setSyntacticBlock(sblock);

		// TODO internalCaller upClass
		ClassModel cmodel = new ClassModel();
		cmodel.setType(sblock.getType());
		cmodel.setCode(sblock.getCode());
		final List<String> internalCallee = new ArrayList<>();
		final List<String> belowClass = new ArrayList<>();
		node.accept(new ASTVisitor() {
			public boolean visit(SimpleName name) {
				IBinding binding = name.resolveBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IField) {
						String classString = ((IField) element).getDeclaringType()
								.getPackageFragment().getElementName() + "." 
								+ ((IField) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IField) element)
									.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}

			public boolean visit(MethodInvocation method) {
				IMethodBinding binding = method.resolveMethodBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IMethod) {
						String classString = ((IMethod) element).getDeclaringType()
								.getPackageFragment().getElementName() + "." 
								+ ((IMethod) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IMethod) element)
									.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}

			public boolean visit(ClassInstanceCreation creation) {
				IMethodBinding binding = creation.resolveConstructorBinding();
				if(binding != null){
					IJavaElement element = binding.getJavaElement();
					if (element instanceof IMethod) {
						String classString = ((IMethod) element).getDeclaringType()
								.getPackageFragment().getElementName() + "." 
								+ ((IMethod) element).getDeclaringType().getElementName();
						if(!classString.startsWith(prefix)){
							internalCallee.add(classString + "." + ((IMethod) element)
									.getElementName());
							belowClass.add(classString);
						}
					}
				}
				return true;
			}
		});
		cmodel.setBelowClass(belowClass);
		cmodel.setInternalCallee(internalCallee);
		dc.setClassModel(cmodel);

		return dc;
	}

}
