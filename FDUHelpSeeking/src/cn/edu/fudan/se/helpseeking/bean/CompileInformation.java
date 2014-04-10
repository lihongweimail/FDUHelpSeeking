package cn.edu.fudan.se.helpseeking.bean;

public class CompileInformation {
	
//	编译信息
//	策略是监听problem view当有change事件时,更新内存单例，在每个动作时候都去获取信息：
//	如果变更则取出所有信息，并按照先error后warning信息；
//	如果有单击或双击选择事件，则取该项目信息。
	public enum CompileInfoType {
		ERROR,
		WARNING
	}

	CompileInfoType type;   // 1 error or 2 warning
	String content;
	String relatedCode; // 通过信息中所涉及的代码行，来定位该代码行的代码文本 line ？  
	
	
	public CompileInfoType getType() {
		return type;
	}
	public void setType(CompileInfoType type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRelatedCode() {
		return relatedCode;
	}
	public void setRelatedCode(String relatedCode) {
		this.relatedCode = relatedCode;
	}
	
	
	
	
	

}
