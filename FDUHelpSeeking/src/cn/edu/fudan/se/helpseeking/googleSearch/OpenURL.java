package cn.edu.fudan.se.helpseeking.googleSearch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OpenURL {

	StringBuffer document = new StringBuffer();

	public String originUrl, buffer = "";

	KeywordCounting keywordCounting;

	public OpenURL(String originUrl) {
		this.originUrl = originUrl;
	}

	public RankingCount counting() {
		keywordCounting = new KeywordCounting();
		return keywordCounting.countingProcess(document);
	}

	public void urlRead() throws IOException {
		String charsetBuffer = "";
		String charset = "";
		boolean check = false;
		BufferedReader brChar, br = null;
		String charsetArray[] = { "euc-kr", "ksc5601", "iso-8859-1", "8859_1",
				"ms949", "ascii", "utf-8" };
		String charsetArrayBig[] = new String[charsetArray.length];

		for (int i = 0; i < charsetArrayBig.length; i++) {
			charsetArrayBig[i] = charsetArray[i].toUpperCase();
		}

		try {
			URL urlChar = new URL(originUrl);
			HttpURLConnection conChar = (HttpURLConnection) urlChar
					.openConnection();

			conChar.addRequestProperty("User-Agent", "Mozilla/4.0");
			conChar.setConnectTimeout(5000);
			conChar.setReadTimeout(3000);

			InputStream inputURLChar = conChar.getInputStream();
			InputStreamReader rdChar = null;

			rdChar = new InputStreamReader(inputURLChar, "UTF-8");

			brChar = new BufferedReader(rdChar);

			while ((buffer = brChar.readLine()) != null) {
				if (buffer.contains("charset")) {
					charsetBuffer = buffer;
					System.out.println("현재 사이트의 케릭터 셋 포함 문장: " + charsetBuffer);
					break;
				}
			}

			for (int i = 0; i < charsetArray.length; i++) {

				if (charsetBuffer.contains(charsetArray[i])) {
					charset = charsetArray[i];
					check = true;
				}

			}
			for (int i = 0; i < charsetArrayBig.length; i++) {

				if (charsetBuffer.contains(charsetArrayBig[i])) {
					charset = charsetArrayBig[i];
					check = true;
				}
			}

			if (check == false) {
				charset = "UTF-8";
			}

			rdChar.close();
			brChar.close();

		} catch (Exception e) {
			System.err.println(e);
		}

		try {
			URL url = new URL(originUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.addRequestProperty("User-Agent", "Mozilla/4.0");
			con.setConnectTimeout(5000);
			con.setReadTimeout(3000);

			InputStream inputURL = con.getInputStream();
			InputStreamReader rd = null;

			rd = new InputStreamReader(inputURL, charset);
			br = new BufferedReader(rd);

			while ((buffer = br.readLine()) != null) {
				document.append(buffer);

			}

			rd.close();
			br.close();

		} catch (Exception e) {
			System.err.println(e);
		}

		System.out.println(originUrl + " 분석 끝.");
	}
}