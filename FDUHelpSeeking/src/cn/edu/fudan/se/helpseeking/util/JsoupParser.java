package cn.edu.fudan.se.helpseeking.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupParser {
	
	private Document doc;
	
	public static HashSet<String> STOPWORDS = new  HashSet<String>();
	static
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("stopwords"));
			
			String line = br.readLine();
			while(line != null)
			{
				if(!"".equals(line.trim()))
				{
					STOPWORDS.add(line.trim());
				}
				line = br.readLine();
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static boolean isNumeric(char c)  
	{  
	  String str = "" + c;
	  return isNumeric(str);
	}
	
	public JsoupParser(String url) throws IOException
	{
		doc = Jsoup.connect(url).post();
	}
	
	public String getWebPageContent()
	{
		return doc.text();
	}
	
	public String getWebPageHtml()
	{
		return doc.html();
	}
	
	public String getBodyContent()
	{
		return doc.body().text();
	}
	
	public String getBodyHtml()
	{
		return doc.body().html();
	}
	
	public int getAroundWord(String api, int step, List<WordsAroundAPI> results)
	{
		String content = getBodyContent();
		
		int count = 0;
		if(content != null)
		{
			//去掉一些特殊符号，换成空格, / ; , { } [ ] ( )
			content = content.replaceAll("[\\/;,{}\\[\\]\\(\\)]+", " ");
			content.replaceAll("\\s+", " ");
			
			String[] words = content.toLowerCase().split(" ");
			for(int i=0; i<words.length; i++)
			{
				if(words[i].indexOf(api.toLowerCase()) > -1)
				{
					count += 1;
					
					WordsAroundAPI wapi = new WordsAroundAPI(words[i]);
					
					int k = 0;
					for(int j=i-1; j>=0 && k<step; j--)
					{
						//不是stopwords, 并且不是数字， 和数字开头的
						if(!"".equals(words[j].trim()) && !STOPWORDS.contains(words[j].trim()) 
								&& !isNumeric(words[j]) && !isNumeric(words[j].charAt(0)))
						{
							wapi.addWord(words[j]);
							k++;
						}
					}
					
					k=0;
					for(int j=i+1; j<words.length && k<step; j++)
					{
						if(!"".equals(words[j].trim()) && !STOPWORDS.contains(words[j]) 
								&& !isNumeric(words[j]) && !isNumeric(words[j].charAt(0)))
						{
							wapi.addWord(words[j]);
							k++;
						}
					}
					
					results.add(wapi);
				}
				
			}
		}
		
		
		return count;
	}
	
	public String toHtml(HashMap<String, List<String>> map)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		buffer.append("<head>");
		buffer.append("<meta name=\"author\" content=\"bao lingfeng\">");
		buffer.append("<meta name=\"description\" content=\"Web Page Overview\">");
		buffer.append("<meta http-equiv=\"pragma\" content=\"nocache\">");
		buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">");
		buffer.append("<title>Web Page Overview</title>");
		buffer.append("</head>");
		buffer.append("<body>\n");
		
		Set<Map.Entry<String, List<String>>> set = map.entrySet(); 
		Iterator<Map.Entry<String, List<String>>> i = set.iterator(); 
		while(i.hasNext()) { 
			Map.Entry<String, List<String>> entry = i.next(); 
			String key = entry.getKey();
			List<String> value = entry.getValue();
			buffer.append("<ul>\n");
			buffer.append("<li>" + key + "</li>\n");
			
			buffer.append("<ul>\n");
			for(String str : value)
			{
				buffer.append("<li>" + str + "</li>\n");
			}
			buffer.append("</ul>\n");
			
			buffer.append("</ul>\n");
		}
		
		buffer.append("</body></html>");
		return buffer.toString();
	}
}
