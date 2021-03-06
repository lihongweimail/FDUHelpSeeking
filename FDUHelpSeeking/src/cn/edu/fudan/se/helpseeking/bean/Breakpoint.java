package cn.edu.fudan.se.helpseeking.bean;

public class Breakpoint {
//	
//	Type记录断点类型。
//	MethodURI记录断点所在的方法的qualified name
//

	String type;     // breakpoint type :   line , method , value, exception etc.
	String methodQualifiedName; 
	int lineNo;  //? 不知道是否还需要
	String fileName;	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMethodQualifiedName() {
		return methodQualifiedName;
	}
	public void setMethodQualifiedName(String methodURI) {
		this.methodQualifiedName = methodURI;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}	

}
