package cn.edu.fudan.se.helpseeking.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.MessageCollector;
import cn.edu.fudan.se.helpseeking.googleAPIcall.LoopGoogleAPICall;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;

public class CrawlWebPages {

	static LoopGoogleAPICall myGoogleSearch = new LoopGoogleAPICall("test");
	static List<WEBResult> searchResults = new ArrayList<WEBResult>();

	public void haha() {

		MessageCollector mcCollector = MessageCollector.getInstance();

		String queryTextString = "";
		queryTextString = mcCollector.getMessage();
		queryTextString = queryTextString.replace(".", " ");
		try {
			searchResults = myGoogleSearch.searchWeb(queryTextString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
