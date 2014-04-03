package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.text.ITextSelection;

import cn.edu.fudan.se.helpseeking.bean.ClassModel;
import cn.edu.fudan.se.helpseeking.bean.Cursor;
import cn.edu.fudan.se.helpseeking.bean.DebugCode;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.SyntacticBlock;

public class EditCodeUtil {
	
	public static Information createInformationBySelection(ICompilationUnit unit, ITextSelection s) {
		Information information = new Information();
		
		EditCode ec = new EditCode();
		SyntacticBlock sblock = new SyntacticBlock();
		try {
			IJavaElement element = unit.getElementAt(s.getOffset());
			if(element instanceof IField){
				sblock.setType("Field");
				sblock.setCode(((IField) element).getSource());
			}else if(element instanceof IMethod){
				sblock.setType("Method");
				sblock.setCode(((IMethod) element).getSource());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}		
		ec.setSyntacticBlock(sblock);
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
		
		information.setEditCode(ec);
		return information;		
	}
	
	public static Information createInformationByBreakpoint(ICompilationUnit unit, IBreakpoint bp) {
		Information information = new Information();
		
		DebugCode dc = new DebugCode();
		
		information.setDebugCode(dc);		
		return information;
	}

}
