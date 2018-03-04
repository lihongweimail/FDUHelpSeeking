package cn.edu.fudan.se.helpseeking.googleSearch;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GoogleSearch {
	private static final String google = "http://www.google.com/search?";
	private static final String key = "&key=AIzaSyCMGfdDaSfjqv5zYoS0mTJnOT3e9MURWkU";
	private static final String cx = "&cx=010276989280583185703:8ss-tvlus7w";
	private Vector<SearchResult> result = new Vector<SearchResult>();
	private String query;
	private String searchPage;
	private int limit;

	public GoogleSearch(String query, int limit) {
		this.query = query;
		this.limit = limit;
	}

	public Vector<SearchResult> getResult() {
		// Initialize
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputStream is;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			is = new ByteArrayInputStream(getXMLResult().getBytes("UTF-8"));
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.getDocumentElement().normalize();

		// Get Data
		Element root = document.getDocumentElement();
		Element res = (Element) root.getLastChild(); // RES
		if (res.getNodeName() != "RES") {
			return result; // No Results
		}
		
		NodeList list = res.getElementsByTagName("R");
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			Element element = (Element) node;

			String title = null; // T - Title
			String url = null; // U - URL
			String snippet = null; // S - Snippet

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				url = element.getElementsByTagName("U").item(0)
						.getTextContent();
				title = element.getElementsByTagName("T").item(0)
						.getTextContent();
				title = title.replaceAll(
						"<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

				snippet = element.getElementsByTagName("S").item(0)
						.getTextContent();
			}
			int resultNumber = i + 1;

			SearchResult searchResult = new SearchResult("Google", title, url,
					snippet, searchPage, resultNumber);
			result.add(searchResult);
		}

		// 최종적으로 만들어진 결과객체 리스트를 전달
		return result;
	}

	private String buildSearchUrl() {

		StringBuilder request = new StringBuilder(google);
		request.append("start=0");
		request.append("&num=" + limit); // 검색 제한 수 조정
		request.append(key); // API Key
		request.append(cx); // Custom Search Engine ID

		request.append("&q=" + query);

		request.append("&client=google-csbe"); // Search Engine Type
		request.append("&output=xml_no_dtd"); // Output to XML
		request.append("&ie=utf-8"); // Input Encoding
		request.append("&oe=utf-8"); // Output Encoding
		request.append("&lr=lang_ko");

		System.out.println("구글API XML주소 : " + request.toString());
		searchPage = request.toString().split("&output=xml_no_dtd")[0]
				+ request.toString().split("&output=xml_no_dtd")[1];
		System.out.println("구글 searchPage : " + searchPage);
		return request.toString();
	}

	private String getXMLResult() {
		HttpURLConnection conn = null;
		StringBuffer readBuffer = new StringBuffer();

		try {
			// Build URL
			URL url = new URL(buildSearchUrl());
			// Connect
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			// Read
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			// Save
			String read;
			while ((read = br.readLine()) != null) {
				readBuffer.append(read);
			}
			// Close
			br.close();
			isr.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}

		return readBuffer.toString();
	}
}