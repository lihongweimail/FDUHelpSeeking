package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;



public class SearchResults {
	
	String searchID;

	List <SearchNode> searchNode=new ArrayList<SearchNode>();

	public String getSearchID() {
		return searchID;
	}

	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}

	public List<SearchNode> getSearchNode() {
		return searchNode;
	}

	public void setSearchNode(List<SearchNode> searchNode) {
		this.searchNode = searchNode;
	}
	
	
	

}
