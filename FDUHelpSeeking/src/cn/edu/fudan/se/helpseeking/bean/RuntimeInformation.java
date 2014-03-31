package cn.edu.fudan.se.helpseeking.bean;

public class RuntimeInformation {
	
//	运行时信息：
//	需要考虑如何从console中的输出中区分type： 
//	framework message 
//  program output
//  exceptional message（*这个信息更重要）
	
	String type;
	String content;
	String relatedCode;  //	Related code： 暂时取代码行
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
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
