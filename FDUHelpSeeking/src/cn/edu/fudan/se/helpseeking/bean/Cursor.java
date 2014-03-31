package cn.edu.fudan.se.helpseeking.bean;

public class Cursor {
	int lineNo; // line of the current cursor
	//if selecting some code block , there may have a scope.
	int lineFrom;
	int lineTo;
	String  methodQualifiedName;
	
	
	
	
	public String getMethodQualifiedName() {
		return methodQualifiedName;
	}
	public void setMethodQualifiedName(String methodQualifiedName) {
		this.methodQualifiedName = methodQualifiedName;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getLineFrom() {
		return lineFrom;
	}
	public void setLineFrom(int lineFrom) {
		this.lineFrom = lineFrom;
	}
	public int getLineTo() {
		return lineTo;
	}
	public void setLineTo(int lineTo) {
		this.lineTo = lineTo;
	}
	
	

}
