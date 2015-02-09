package cn.edu.fudan.se.helpseeking.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.KeyWord;



public class JSoupTest {
	
	
	
	
	public static List<WordsAroundAPI> getWordsAround(List<KeyWord> apis,String webURL) throws IOException 
	{
		
		JsoupParser parser = new JsoupParser(webURL);
		
				
		//HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<WordsAroundAPI> results = new ArrayList<WordsAroundAPI>();
		
		for(KeyWord api : apis)
		{
			//for(String s : parser.getAroundWord(api, 10))
			//{
			//	System.out.println("###" + s);
			//}
			//List<WordsAroundAPI> results = new ArrayList<WordsAroundAPI>();
			
			System.out.println(parser.getAroundWord(api.getKeywordName(), 4, results));
			//map.put(api, results);
			
		}
		
		for(WordsAroundAPI waa : results)
		{
			System.out.println(waa.getApi());
			for(String w : waa.getWords())
			{
				System.out.println("          " + w);
			}
		}
		
		//String str = ", try, {, string, everything;,   ioutils.tostring(inputstream)";
		//String s  = str.replaceAll("[;,\\.]+", " ");
		//s = s.replaceAll("\\s+", " ");
		
		
		//System.out.println(str);
		//System.out.println(s);
		//System.out.println(parser.toHtml(map));
		
		return results;
		
	}
	
	public static void main(String args[]) throws Exception
	{
		
		JsoupParser parser = new JsoupParser("http://stackoverflow.com/questions/4716503/best-way-to-read-a-text-file");
		
		String[] apis = {"FileInputStream"};
		
		//HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<WordsAroundAPI> results = new ArrayList<WordsAroundAPI>();
		for(String api : apis)
		{
			//for(String s : parser.getAroundWord(api, 10))
			//{
			//	System.out.println("###" + s);
			//}
			//List<WordsAroundAPI> results = new ArrayList<WordsAroundAPI>();
			
			System.out.println(parser.getAroundWord(api, 4, results));
			//map.put(api, results);
			
		}
		
		for(WordsAroundAPI waa : results)
		{
			System.out.println(waa.getApi());
			for(String w : waa.getWords())
			{
				System.out.println("          " + w);
			}
		}
		
		//String str = ", try, {, string, everything;,   ioutils.tostring(inputstream)";
		//String s  = str.replaceAll("[;,\\.]+", " ");
		//s = s.replaceAll("\\s+", " ");
		
		
		//System.out.println(str);
		//System.out.println(s);
		//System.out.println(parser.toHtml(map));
	}
}
