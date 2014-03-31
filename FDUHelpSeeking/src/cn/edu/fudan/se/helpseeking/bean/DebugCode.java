package cn.edu.fudan.se.helpseeking.bean;

public class DebugCode {
	
	SyntacticBlock syntacticBlock;
	ClassModel classModel;
	Breakpoint breakpoint; //?? 不知道是否需要保留
	
	
	public SyntacticBlock getSyntacticBlock() {
		return syntacticBlock;
	}
	public void setSyntacticBlock(SyntacticBlock syntacticBlock) {
		this.syntacticBlock = syntacticBlock;
	}
	public ClassModel getClassModel() {
		return classModel;
	}
	public void setClassModel(ClassModel classModel) {
		this.classModel = classModel;
	}
	public Breakpoint getBreakpoint() {
		return breakpoint;
	}
	public void setBreakpoint(Breakpoint breakpoint) {
		this.breakpoint = breakpoint;
	}
	
	

}
