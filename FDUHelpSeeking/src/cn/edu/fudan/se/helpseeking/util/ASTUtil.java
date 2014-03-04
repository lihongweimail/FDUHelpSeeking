package cn.edu.fudan.se.helpseeking.util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTUtil {

	public static void parse(ICompilationUnit unit, int offset, int length) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(unit);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSourceRange(offset, length);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		System.out.println(cu);
	}

	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(unit);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}
}
