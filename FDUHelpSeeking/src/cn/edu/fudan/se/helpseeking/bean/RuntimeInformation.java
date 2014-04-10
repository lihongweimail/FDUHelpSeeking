package cn.edu.fudan.se.helpseeking.bean;

public class RuntimeInformation {
	
//	运行时信息：
//	需要考虑如何从console中的输出中区分type： 
//	1 framework message 
//  2 program output
//  3 exceptional message（*这个信息更重要）
	public enum RuntimeInfoType {
		FrameWorkMessage,
		ProgramOutput,
		ExceptionalMessage
	}

	
	RuntimeInfoType type;
	String content;
	String relatedCode;  //	Related code： 暂时取代码行
	
	

	public RuntimeInfoType getType() {
		return type;
	}
	public void setType(RuntimeInfoType type) {
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
