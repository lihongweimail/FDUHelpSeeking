package liuyang.nlp.lda.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.util.ParserException;


public class UrlConfig {

	public static String Url1 = "http://wordcram.org/";
	
	public static String Url2 = "http://xue.youdao.com/zx/archives/87688";
	
	
	public static String UrlPath = "data/UrlData/UrlData.txt";

	public static void UrlReader(ArrayList<String> lines) throws IOException, ParserException{
	
	BufferedReader UrlReader = null;
	String line = null;
	UrlReader = new BufferedReader(new FileReader(new File(UrlPath)));
	
	while ((line = UrlReader.readLine()) != null) {
		lines.add(line);
		//System.out.println(line);
	   }
		
	 UrlReader.close();
	}	
}
