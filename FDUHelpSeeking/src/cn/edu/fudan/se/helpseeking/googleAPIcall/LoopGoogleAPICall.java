package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LoopGoogleAPICall extends Thread{

	static String cse_cx="";

	
	static String cse_key="";
	
	
	
	static String search="";
	
	public static void commSearch() throws MalformedURLException,
	UnsupportedEncodingException, IOException {
		int j = 0; // count for the results

		// String address =
		// "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String query = "variable value used the of";

		//		for (int i = 0; i < 9; i = i + 4) {

		// see
		// https://developers.google.com/search-appliance/documentation/614/xml_reference
		// for setting query limit web pages language for English
		//		String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=005635559766885752621:va1etsiak-a&q=";
		String address="https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&num=10&start=0&q=";
		//			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&lr=lang_en&start="
		//					+ i + "&q=";
		// "类型不匹配 程序不能结束 Java Mismatch Or Never Ending Program";
		String charset = "UTF-8";

		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleAPICallResults results = new Gson().fromJson(reader,
				GoogleAPICallResults.class);

		System.out.println("[estimatedResutlCount:] "
				+ results.getResponseData().getEstimatedResultCount());
		System.out.println("[the list size:]"
				+ results.getResponseData().getResults().size());
		// Show title and URL of each results
		for (int m = 0; m < results.getResponseData().getResults().size(); m++) {

			System.out.println("Result " + (++j) + " :");

			System.out.println("content: "
					+ results.getResponseData().getResults().get(m)
					.getContent());
			System.out.println("unescapedUrl: "
					+ results.getResponseData().getResults().get(m)
					.getUnescapedUrl());
			System.out.println("URL: "
					+ results.getResponseData().getResults().get(m)
					.getUrl());
			System.out.println("Title: "
					+ results.getResponseData().getResults().get(m)
					.getTitle());
			System.out.println("titleNoFormatting: "
					+ results.getResponseData().getResults().get(m)
					.getTitleNoFormatting());
			System.out.println("location: "
					+ results.getResponseData().getResults().get(m)
					.getLocation());
			System.out.println("publisher: "
					+ results.getResponseData().getResults().get(m)
					.getPublisher());
			System.out.println("publishedDate: "
					+ results.getResponseData().getResults().get(m)
					.getPublishedDate());
			System.out.println("signedRedirectUrl: "
					+ results.getResponseData().getResults().get(m)
					.getSignedRedirectUrl());
			System.out.println("language: "
					+ results.getResponseData().getResults().get(m)
					.getLanguage());

			System.out.println();

		}
		//		}
	}
	
	
	
	public static String getCse_cx() {
		return cse_cx;
	}
	
	
	
	
	
	public static String getCse_key() {
		return cse_key;
	}


	public static String getSearch() {
		return search;
	}


	public static void main(String[] args) throws IOException {

		LoopGoogleAPICall apiCall=new LoopGoogleAPICall("getResource IllegalArgumentException");
		System.out.println("start search...");
		//		apiCall.searchWebCSE("DefaultHandler SAXParser SAXParserException");
		//		apiCall.commSearch();
		apiCall.commCSESearch("getResource IllegalArgumentException");

		//		apiCall.testURL();
		System.out.println("End search.");
	}


	public static void setCse_cx(String cse_cx) {
		LoopGoogleAPICall.cse_cx = cse_cx;
	}

	public static void setCse_key(String cse_key) {
		LoopGoogleAPICall.cse_key = cse_key;
	}


	public static void setSearch(String search) {
		LoopGoogleAPICall.search = search;
	}


	List<MYITEMS> currentCSEAPICallResults=new ArrayList<MYITEMS>();


	List<WEBResult> currentResults = new ArrayList<WEBResult>();


	public volatile boolean stop=false;
	
	public LoopGoogleAPICall(String search) {

		setSearch(search);
		setSearchmode(2);


	}

	
	int searchmode=1; //1 search from custom search engine API  ;  2  use the old ajax API 
	

	public int getSearchmode() {
		return searchmode;
	}



	public void setSearchmode(int searchmode) {
		this.searchmode = searchmode;
	}



	public LoopGoogleAPICall(String cse_key,String cse_cx,String search){
		setCse_key(cse_key);
		setCse_cx(cse_cx);
		setSearch(search);
		setSearchmode(1);
		
		
		
	}


	public void cancel() {

	}

	public void commCSESearch(String query) throws MalformedURLException,
	UnsupportedEncodingException, IOException {
		int j = 0; // count for the results

		//		String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=005635559766885752621:va1etsiak-a&q=";
		//		String address="https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&lr=lang_en&num=10&start=0&q=";
		String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=017576662512468239146:omuauf_lfve&lr=lang_en&num=10&q=";

		String charset = "UTF-8";
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);

		System.out.println(url.getContent());


		//		List<String> list2 = new Gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());

		CSEAPICallResults results = new Gson().fromJson(reader,	CSEAPICallResults.class);
		System.out.println(results.toString());


		System.out.println("\n[the list size:]"
				+ results.getItems().size());
		// Show title and URL of each results

		for (int m = 0; m < results.getItems().size(); m++) {

			System.out.println("Result " + (++j) + " :");

			System.out.println("content: "
					+ results.getItems().get(m)
					.getSnippet());
			System.out.println("unescapedUrl: "
					+ results.getItems().get(m)
					.getDisplayLink());
			System.out.println("URL: "
					+ results.getItems().get(m)
					.getLink());
			System.out.println("Title: "
					+ results.getItems().get(m)
					.getTitle());
			System.out.println("titleNoFormatting: "
					+ results.getItems().get(m).getTitle());


			System.out.println();

		}
		//		}
	}


	public List<WEBResult> CSESearch(String key, String cx, String query) throws MalformedURLException,
	UnsupportedEncodingException, IOException {

		List<WEBResult> searchResults = new ArrayList<WEBResult>();

		for (int i = 0; i < currentCSEAPICallResults.size(); i++) {
			currentCSEAPICallResults.remove(i);
		}
		for (int i = 0; i < currentResults.size(); i++) {
			currentResults.remove(i);
		}

		int j = 0; // count for the results

		//		String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=005635559766885752621:va1etsiak-a&q=";
		//		String address="https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&lr=lang_en&num=10&start=0&q=";
		//String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=017576662512468239146:omuauf_lfve&lr=lang_en&num=10&q=";
		String address="https://www.googleapis.com/customsearch/v1?key="+key+"&cx="+cx+"&lr=lang_en&num=10&q=";

		String charset = "UTF-8";
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);

		CSEAPICallResults results = new Gson().fromJson(reader,	CSEAPICallResults.class);
		System.out.println(results.toString());

List<MYITEMS> items=results.getItems();
if(items!=null)
{
if (items.size()>0) {

		currentCSEAPICallResults=results.getItems();

		System.out.println("\n[the list size:]"
				+ results.getItems().size());

		
		
			for (int m = 0; m < items.size(); m++) {


				WEBResult temp=new WEBResult();
				temp.setContent(items.get(m).getSnippet());
				temp.setLanguage(null);
				temp.setLocation(null);
				temp.setPublishedDate(null);;
				temp.setPublisher(null);
				temp.setSignedRedirectUrl(null);
				temp.setTitle(items.get(m).getTitle());
				temp.setTitleNoFormatting(items.get(m).getTitle());
				temp.setUnescapedUrl(items.get(m).getFormattedUrl());
				temp.setUrl(items.get(m).getLink());

				searchResults.add(temp);
				currentResults.add(temp);

				System.out.println("Result " + (++j) + " :");
				System.out.println("Title: "
						+ results.getItems().get(m).getTitle());
				System.out.println("content: "
						+ results.getItems().get(m).getSnippet());
				System.out.println("unescapedUrl: "
						+ results.getItems().get(m).getDisplayLink());
				System.out.println("URL: "
						+ results.getItems().get(m).getLink());
				System.out.println("titleNoFormatting: "
						+ results.getItems().get(m).getHtmlTitle());
				System.out.println();

			}

		}
     }
       return searchResults;

	}


	public List<MYITEMS> getCurrentCSEAPICallResults() {
		return currentCSEAPICallResults;
	}




	public List<WEBResult> getCurrentResults() {
		return currentResults;
	}



	public void run() 
	{



		try {
			if (searchmode==2) {
				searchWeb(search);
			}
			else {
				CSESearch(getCse_key(), getCse_cx(), getSearch());
			}
			


		} catch (IOException e) {
			
			e.printStackTrace();
		}



	}


	//	自定义的搜索引擎
	//	"https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q=" 

	public  List<WEBResult> searchWeb(String keywords) throws IOException {

		List<WEBResult> results = new ArrayList<WEBResult>();

		for (int i = 0; i < currentResults.size(); i++) {
			currentResults.remove(i);
		}


		int j = 0; // count for the results

		for (int i = 0; i < 9; i = i + 4) {
			//			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&lr=lang_en&start="
			//					+ i + "&q=";
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="
					+ i + "&q=";

			String query = keywords; // "Programcreek";
			String charset = "UTF-8";

			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);


			GoogleAPICallResults tempRresults = new Gson().fromJson(reader,
					GoogleAPICallResults.class);
			//			System.out.println(tempRresults.toString());
			if (tempRresults.getResponseData() != null 
					&& tempRresults.getResponseData().getResults().size()>0) {
				// Show title and URL of each results
				int size = tempRresults.getResponseData().getResults().size();
				int limit = size >4 ? 4: size;


				for (int m = 0; m < limit; m++) {
					System.out.println("Result " + (++j) + " :");
					System.out.println("Title: "
							+ tempRresults.getResponseData().getResults().get(m)
							.getTitle());
					System.out.println("Title no formatting: " +tempRresults.getResponseData().getResults().get(m).getTitleNoFormatting());
					System.out.println("URL: "
							+ tempRresults.getResponseData().getResults().get(m)
							.getUrl() + "\n");
					System.out.println("content: "+tempRresults.getResponseData().getResults().get(m).getContent()+"\n");
					WEBResult old=tempRresults.getResponseData().getResults().get(m);
					results.add(old);
					WEBResult temp=new WEBResult();
					temp.setContent(old.getContent());
					temp.setLanguage(old.getLanguage());
					temp.setLocation(old.getLocation());
					temp.setPublishedDate(old.getPublishedDate());;
					temp.setPublisher(old.getPublisher());
					temp.setSignedRedirectUrl(old.getSignedRedirectUrl());
					temp.setTitle(old.getTitle());
					temp.setTitleNoFormatting(old.getTitleNoFormatting());
					temp.setUnescapedUrl(old.getUnescapedUrl());
					temp.setUrl(old.getUrl());
					currentResults.add(temp);

				}
			}
		}

		//		System.out.println(results.size());
		return results;
	}



	public void setCurrentCSEAPICallResults(List<MYITEMS> currentCSEAPICallResults) {
		this.currentCSEAPICallResults = currentCSEAPICallResults;
	}

	public void setCurrentResults(List<WEBResult> currentResults) {
		this.currentResults = currentResults;
	}

	public void testURL() {

		String address="https://www.googleapis.com/customsearch/v1?key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM&cx=017576662512468239146:omuauf_lfve&lr=lang_en&num=10&q=";
		String query="getResource IllegalArgumentException";
		String charset = "UTF-8";

		try{
			URL url=new URL(address+URLEncoder.encode(query, charset));

			URLConnection connection=url.openConnection();

			connection.connect();
			InputStream reader=connection.getInputStream();
			InputStreamReader isr=new InputStreamReader(reader);
			BufferedReader br=new BufferedReader(isr);
			String str=null;
			while((str=br.readLine())!=null)
			{
				System.out.println(str); 
			}


		}
		catch(IOException e)
		{
			System.out.println(e);
		}




	}



}
