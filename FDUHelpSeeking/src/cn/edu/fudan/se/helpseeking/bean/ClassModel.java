package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class ClassModel {
	
//	internalCaller：类内中谁调用了自己（field，method）
//	internalCallee：类内中自己（field，method）调用了谁
//	upClass:这段代码中field，method（class）被哪些类调用
//	belowClass: 这段代码中调用了哪些类

//	??　根据代码行识别出所在行的代码类型type：
//	??　Line / field / statementblock / method / class

	//?? type 是否使用  ??code是否使用
	 String type;
	 String code;
	 
//	 这段代码在调用的方法，或那些方法调用了这段方法所在的方法
//	 保存方法的qualified name
	 List <String> internalCaller;
	 List <String> internalCallee;
//	 那些类与这段代码所在的方法（或类）之间有调用关系
//	 保存类的qualified name
	 List <String> upClass;
	 List <String> belowClass;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getInternalCaller() {
		return internalCaller;
	}
	public void setInternalCaller(List<String> internalCaller) {
		this.internalCaller = internalCaller;
	}
	public List<String> getInternalCallee() {
		return internalCallee;
	}
	public void setInternalCallee(List<String> internalCallee) {
		this.internalCallee = internalCallee;
	}
	public List<String> getUpClass() {
		return upClass;
	}
	public void setUpClass(List<String> upClass) {
		this.upClass = upClass;
	}
	public List<String> getBelowClass() {
		return belowClass;
	}
	public void setBelowClass(List<String> belowClass) {
		this.belowClass = belowClass;
	}
	 
	
	 

}
