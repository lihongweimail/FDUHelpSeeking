package cn.edu.fudan.se.helpseeking.googleCSEAPI;


import com.google.gson.JsonObject;

public class SearchResult {
	// 多余的字段可以不要
	private String kind;
	private JsonObject url;
	private JsonObject queries;
	private JsonObject context;
	private JsonObject searchInformation;
	// 这是我们需要的字段
	private SearchResultItem items[];
	
	
	
	public String getKind() {
		return kind;
	}
	public JsonObject getUrl() {
		return url;
	}
	public JsonObject getQueries() {
		return queries;
	}
	public JsonObject getContext() {
		return context;
	}
	public JsonObject getSearchInformation() {
		return searchInformation;
	}
	public SearchResultItem[] getItems() {
		return items;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public void setUrl(JsonObject url) {
		this.url = url;
	}
	public void setQueries(JsonObject queries) {
		this.queries = queries;
	}
	public void setContext(JsonObject context) {
		this.context = context;
	}
	public void setSearchInformation(JsonObject searchInformation) {
		this.searchInformation = searchInformation;
	}
	public void setItems(SearchResultItem[] items) {
		this.items = items;
	}
	

}

