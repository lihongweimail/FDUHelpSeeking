package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.util.List;

public class MYQuery {
	String title;  //A description of the query.
	long totalResults;
	String searchTerms;//The search terms entered by the user.
	int count;
	int startIndex;
	int startPage;
	
	String language;
	String inputEncoding;
	String outputEncoding;
    String safe;
    String cx;
    String cref;
    String sort;
    String filter;
    String gl;
    String cr;
    String googleHost;
    String disableCnTwTranslation;
    String hq;
    String hl;
    String siteSearch;
    String siteSearchFilter;
    String exactTerms;
    String excludeTerms;
    String linkSite;
    String orTerms;
    String relatedSite;
    String dateRestrict;
    String lowRange;
    String highRange;
    String fileType;
    String searchType;
    String imgSize;
    String imgType;
    String imgColorType;
    String imgDominantColor;
    
    public String toString()
    {
    	String toString="\n[myquery]";
    	toString=toString+"\n[title:]\n"+title;
    	toString=toString+"\n[totalResults:]\n"+String.valueOf(totalResults);
    	toString=toString+"\n[cx:]\n"+cx;
    			toString=toString+"\n[cref:]\n"+cref;
    			
    			return toString;
    	
    }
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	public String getSearchTerms() {
		return searchTerms;
	}
	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getInputEncoding() {
		return inputEncoding;
	}
	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}
	public String getOutputEncoding() {
		return outputEncoding;
	}
	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
	public String getSafe() {
		return safe;
	}
	public void setSafe(String safe) {
		this.safe = safe;
	}
	public String getCx() {
		return cx;
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	public String getCref() {
		return cref;
	}
	public void setCref(String cref) {
		this.cref = cref;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getGl() {
		return gl;
	}
	public void setGl(String gl) {
		this.gl = gl;
	}
	public String getCr() {
		return cr;
	}
	public void setCr(String cr) {
		this.cr = cr;
	}
	public String getGoogleHost() {
		return googleHost;
	}
	public void setGoogleHost(String googleHost) {
		this.googleHost = googleHost;
	}
	public String getDisableCnTwTranslation() {
		return disableCnTwTranslation;
	}
	public void setDisableCnTwTranslation(String disableCnTwTranslation) {
		this.disableCnTwTranslation = disableCnTwTranslation;
	}
	public String getHq() {
		return hq;
	}
	public void setHq(String hq) {
		this.hq = hq;
	}
	public String getHl() {
		return hl;
	}
	public void setHl(String hl) {
		this.hl = hl;
	}
	public String getSiteSearch() {
		return siteSearch;
	}
	public void setSiteSearch(String siteSearch) {
		this.siteSearch = siteSearch;
	}
	public String getSiteSearchFilter() {
		return siteSearchFilter;
	}
	public void setSiteSearchFilter(String siteSearchFilter) {
		this.siteSearchFilter = siteSearchFilter;
	}
	public String getExactTerms() {
		return exactTerms;
	}
	public void setExactTerms(String exactTerms) {
		this.exactTerms = exactTerms;
	}
	public String getExcludeTerms() {
		return excludeTerms;
	}
	public void setExcludeTerms(String excludeTerms) {
		this.excludeTerms = excludeTerms;
	}
	public String getLinkSite() {
		return linkSite;
	}
	public void setLinkSite(String linkSite) {
		this.linkSite = linkSite;
	}
	public String getOrTerms() {
		return orTerms;
	}
	public void setOrTerms(String orTerms) {
		this.orTerms = orTerms;
	}
	public String getRelatedSite() {
		return relatedSite;
	}
	public void setRelatedSite(String relatedSite) {
		this.relatedSite = relatedSite;
	}
	public String getDateRestrict() {
		return dateRestrict;
	}
	public void setDateRestrict(String dateRestrict) {
		this.dateRestrict = dateRestrict;
	}
	public String getLowRange() {
		return lowRange;
	}
	public void setLowRange(String lowRange) {
		this.lowRange = lowRange;
	}
	public String getHighRange() {
		return highRange;
	}
	public void setHighRange(String highRange) {
		this.highRange = highRange;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getImgSize() {
		return imgSize;
	}
	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public String getImgColorType() {
		return imgColorType;
	}
	public void setImgColorType(String imgColorType) {
		this.imgColorType = imgColorType;
	}
	public String getImgDominantColor() {
		return imgDominantColor;
	}
	public void setImgDominantColor(String imgDominantColor) {
		this.imgDominantColor = imgDominantColor;
	}

    

}
