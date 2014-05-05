package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class LoopGoogleAPICall extends Thread{

	public static void main(String[] args) throws IOException {

		LoopGoogleAPICall apiCall=new LoopGoogleAPICall("getResource IllegalArgumentException");
		System.out.println("start search...");
		apiCall.searchWeb("getResource IllegalArgumentException");
//		apiCall.commSearch();
		System.out.println("End search.");
	}

	static String search="";
	public volatile boolean stop=false;
	
	
	public static String getSearch() {
		return search;
	}


	public static void setSearch(String search) {
		LoopGoogleAPICall.search = search;
	}


	public LoopGoogleAPICall(String search) {
      
		setSearch(search);
		

	}
	
	
	public void cancel() {
		
	}
	public void run()
	{
		
		
			
			try {
				searchWeb(search);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}
	
	
	
	
	public static void commSearch() throws MalformedURLException,
	UnsupportedEncodingException, IOException {
		int j = 0; // count for the results

		// String address =
		// "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String query = "variable value used the of";

		for (int i = 0; i < 9; i = i + 4) {

			// see
			// https://developers.google.com/search-appliance/documentation/614/xml_reference
			// for setting query limit web pages language for English
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&lr=lang_en&start="
					+ i + "&q=";
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
		}
	}


//	自定义的搜索引擎
//	"https://www.google.com/cse/publicurl?cx=005635559766885752621:va1etsiak-a&q=" 
	
	List<WEBResult> currentResults = new ArrayList<WEBResult>();
	
	
	
	public List<WEBResult> getCurrentResults() {
		return currentResults;
	}

	public void setCurrentResults(List<WEBResult> currentResults) {
		this.currentResults = currentResults;
	}

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

}
