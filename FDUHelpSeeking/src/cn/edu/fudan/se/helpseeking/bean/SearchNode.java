package cn.edu.fudan.se.helpseeking.bean;

public class SearchNode {

	String title;
	String link;
	String contents;
	String javaExceptionNames;
	
	
	String queryWords;
	String searchID;
	
	int positionInResultslist;
	
	
	
	
	public int getPositionInResultslist() {
		return positionInResultslist;
	}
	public void setPositionInResultslist(int positionInResultslist) {
		this.positionInResultslist = positionInResultslist;
	}
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	public String getQueryWords() {
		return queryWords;
	}
	public void setQueryWords(String queryWords) {
		this.queryWords = queryWords;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getJavaExceptionNames() {
		return javaExceptionNames;
	}
	public void setJavaExceptionNames(String javaExceptionNames) {
		this.javaExceptionNames = javaExceptionNames;
	}
	
	
	
}
