package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.util.List;

import com.google.gson.JsonObject;

public class CSEAPICallResults {

	String kind; //Unique identifier for the type of current object.
	MYURL url;  //The OpenSearch URL element that defines the template for this API.
	List<MYQuery> querys;

	List<MYITEMS> items;
	MYpromotions[] promotions;
	JsonObject  context;

	MYspelling spelling;

	SearchInformation	searchInformation;
	
	
	public String toString()
	{
		String toString="\n[your search results:]";
		if (kind!=null) {
			toString=toString+"\n[kind:]\n"+kind;
		}
		
		if (url!=null) {
			toString=toString+"\n[url:]\n"+url.toString();	
		}
			
		if (querys!=null) {
			
		
				toString=toString+"\n[querys:]\n"+querys.toString();}
		if (items!=null) {
			
				toString=toString+"\n[items:]\n"+items.toString();
		}
		if (promotions!=null) {
			toString=toString+"\n[promotions:]\n"+promotions.toString();
		}
		if (context!=null) {
				toString=toString+"\n[context:]\n"+context.toString();
		}
		if (spelling!=null) {
			toString=toString+"\n[spelling:]\n"+spelling.toString();
		}
				if (searchInformation!=null) {
					
								toString=toString+"\n[spelling:]\n"+searchInformation.toString();
				}
					
								
		
		
		
		return toString;
		
	}
	

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public MYURL getUrl() {
		return url;
	}

	public void setUrl(MYURL url) {
		this.url = url;
	}

	public List<MYQuery> getQuerys() {
		return querys;
	}

	public void setQuerys(List<MYQuery> querys) {
		this.querys = querys;
	}

	public List<MYITEMS> getItems() {
		return items;
	}

	public void setItems(List<MYITEMS> items) {
		this.items = items;
	}

	public MYpromotions[] getPromotions() {
		return promotions;
	}

	public void setPromotions(MYpromotions[] promotions) {
		this.promotions = promotions;
	}



	public JsonObject getContext() {
		return context;
	}

	public void setContext(JsonObject context) {
		this.context = context;
	}

	public MYspelling getSpelling() {
		return spelling;
	}

	public void setSpelling(MYspelling spelling) {
		this.spelling = spelling;
	}

	public SearchInformation getSearchInformation() {
		return searchInformation;
	}

	public void setSearchInformation(SearchInformation searchInformation) {
		this.searchInformation = searchInformation;
	}


	static class MyContext{
		String title;
		List <Myfacets> facets;

		public String toString() {
			String toString="\n[mycontext:]";
			toString=toString+"\n[title:]\n"+title;
			toString=toString+"\n[facets:]\n"+facets.toString();
			return toString;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<Myfacets> getFacets() {
			return facets;
		}

		public void setFacets(List<Myfacets> facets) {
			this.facets = facets;
		}

		static class Myfacets{
			String label;
			String anchor;
			
			public String toString() {
				String toString="\n[myfaces]";
				toString=toString+"\n[label:]\n"+label;
				toString=toString+"\n[anchor:]\n"+anchor;
				return toString;
			}
			
			public String getLabel() {
				return label;
			}
			public void setLabel(String label) {
				this.label = label;
			}
			public String getAnchor() {
				return anchor;
			}
			public void setAnchor(String anchor) {
				this.anchor = anchor;
			}
		}



	}





}
