package cn.edu.fudan.se.helpseeking.googleAPIcall;

public class SearchInformation {
	

	String	formattedSearchTime;
	String	formattedTotalResults;
	double	searchTime;
	long	totalResults;
	
	public String	 toString() {
		String toString="\n[searchinformation]";
		toString=toString+"\n[formattedsearchtime:]\n"+formattedSearchTime;
		toString=toString+"\n[formattedsearchresults:]\n"+formattedTotalResults;
		toString=toString+"\n[totalresults:]\n"+totalResults;
		return toString;
	}
	
	public String getFormattedSearchTime() {
		return formattedSearchTime;
	}
	public void setFormattedSearchTime(String formattedSearchTime) {
		this.formattedSearchTime = formattedSearchTime;
	}
	public String getFormattedTotalResults() {
		return formattedTotalResults;
	}
	public void setFormattedTotalResults(String formattedTotalResults) {
		this.formattedTotalResults = formattedTotalResults;
	}
	public double getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(double searchTime) {
		this.searchTime = searchTime;
	}
	public long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	


}
