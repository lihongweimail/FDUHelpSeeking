package cn.edu.fudan.se.helpseeking.googleSearch;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PageRank {

	public static void main(String[] args) {

		PageRank obj = new PageRank();
		System.out.println(obj.getPR("http://www.youtube.com/watch?v=OwEtaa7b9lc"));
		
	}

	public int getPR(String domain) {

		String result = "";

		JenkinsHash jenkinsHash = new JenkinsHash();
		long hash = jenkinsHash.hash(("info:" + domain).getBytes());

		// Append a 6 in front of the hashing value.
		String url = "http://toolbarqueries.google.com/tbr?client=navclient-auto&hl=en&"
				+ "ch=6"
				+ hash
				+ "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:"
				+ domain;

//		System.out.println("Sending request to : " + url);

		try {
			URLConnection conn = new URL(url).openConnection();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String input;
			while ((input = br.readLine()) != null) {

				// What Google returned? Example : Rank_1:1:9, PR = 9
				System.out.println(input);

				result = input.substring(input.lastIndexOf(":") + 1);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if ("".equals(result)) {
			return 0;
		} else {
			return Integer.valueOf(result);
		}

	}

}