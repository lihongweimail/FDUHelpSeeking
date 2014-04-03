package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
	
	public static EditCode createEditCodeBySelection(ICompilationUnit unit, ITextSelection s) {		
		EditCode ec = new EditCode();
		
		//TODO exception name
		SyntacticBlock sblock = new SyntacticBlock();
		try {
			IJavaElement element = unit.getElementAt(s.getOffset());
			if(element instanceof IField){
				sblock.setType("Field");
				sblock.setCode(((IField) element).getSource());
			}else if(element instanceof IMethod){
				sblock.setType("Method");
				sblock.setCode(((IMethod) element).getSource());
				((IMethod) element).getDeclaringType().getPackageFragment().getElementName();
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}		
		ec.setSyntacticBlock(sblock);
		
		//TOODO 
		ClassModel cmodel = new ClassModel();
		cmodel.setType(sblock.getType());
		cmodel.setCode(sblock.getCode());
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(cmodel.getCode().toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		List <String> internalCallee = new ArrayList<>();
		List <String> belowClass = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			
		});
		cmodel.setBelowClass(belowClass);
		cmodel.setInternalCallee(internalCallee);
		ec.setClassModel(cmodel);
		
		Cursor c = new Cursor();
		c.setLineNo(s.getStartLine());
		ec.setCursor(c);
		
		return ec;		
	}
	
	public static DebugCode createDebugCodeByBreakpoint(ICompilationUnit unit, IBreakpoint bp) {		
		DebugCode dc = new DebugCode();
		
		Breakpoint bpoint = new Breakpoint();
		if(bp instanceof JavaLineBreakpoint){
			bpoint.setType("Line");
			try {
				bpoint.setLineNo(((JavaLineBreakpoint) bp).getLineNumber());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}else if(bp instanceof JavaMethodBreakpoint){
			bpoint.setType("Method");
			try {
				bpoint.setLineNo(((JavaMethodBreakpoint) bp).getLineNumber());
				System.out.println(((JavaMethodBreakpoint) bp).getTypeName());
			} catch (CoreException e) {
				e.printStackTrace();
			}
			//TODO method name
			bpoint.setMethodQualifiedName("");
		}
		dc.setBreakpoint(bpoint);
		
		SyntacticBlock sBlock = new SyntacticBlock();
		dc.setSyntacticBlock(sBlock);
		
		ClassModel cmodel = new ClassModel();
		dc.setClassModel(cmodel);
			
		return dc;
	}

}
