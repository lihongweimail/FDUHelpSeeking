package cn.edu.fudan.se.helpseeking.googleAPIcall;

public class MYspelling {
	
	String correctedQuery;
	String htmlcorrectedQuery;
	public String toString() {
		String toString="\n[Myspelling]";
		toString=toString+"\n[correctedquery:]\n"+correctedQuery;
		toString=toString+"\n[htmlcorrectedquery:]\n"+htmlcorrectedQuery;
		return toString;
	}
	public String getCorrectedQuery() {
		return correctedQuery;
	}
	public void setCorrectedQuery(String correctedQuery) {
		this.correctedQuery = correctedQuery;
	}
	public String getHtmlcorrectedQuery() {
		return htmlcorrectedQuery;
	}
	public void setHtmlcorrectedQuery(String htmlcorrectedQuery) {
		this.htmlcorrectedQuery = htmlcorrectedQuery;
	}
	

}
