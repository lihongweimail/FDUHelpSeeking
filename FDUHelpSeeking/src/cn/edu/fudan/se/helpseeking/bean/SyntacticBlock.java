package cn.edu.fudan.se.helpseeking.bean;

public class SyntacticBlock {
	
//	根据cursor中代码行识别出所在行的代码类型type：
//	Line / field / statementblock / method / class
//	从而扩展得到
//	如果在异常处理语句块中或方法签名上有throw等信息记录exceptionName
	
	String type;
	String code;
	String exceptionName;
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
	public String getExceptionName() {
		return exceptionName;
	}
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	
	
	


}
