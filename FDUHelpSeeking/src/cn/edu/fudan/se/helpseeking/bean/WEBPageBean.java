package cn.edu.fudan.se.helpseeking.bean;

/**
 * @author Grand
 *
 */
public class WEBPageBean {
	
	String url="";
	String title="";
	String summary="";
	String content="";
	String containsStr="----";
	boolean isSelect=false; //该值为所在的topic是否被选择
	
	
	
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getContainsStr() {
		return containsStr;
	}
	public void setContainsStr(String containsStr) {
		this.containsStr = containsStr;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	

}
