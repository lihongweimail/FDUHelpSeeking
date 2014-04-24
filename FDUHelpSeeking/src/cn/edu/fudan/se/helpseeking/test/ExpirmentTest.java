package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExpirmentTest {

	public static void main(String[] args) {
		ExpirmentTest ex=new ExpirmentTest();
		String path = ex.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		System.out.println(".."+path);
		String path2 = System.getProperty("java.class.path");
		int firstIndex = path2.lastIndexOf(System.getProperty("path.separator"))+1;
				int lastIndex = path2.lastIndexOf(File.separator) +1;
				path2 = path2.substring(firstIndex, lastIndex);
				System.out.println(path2);
				
				String confPath = "D:/lihongwei/Research/research topic/help seeking/newIdeas/tool/FDUHelpSeeking/StopResource/javaStopList.txt";  
			
			InputStream heInputStream=ex.getClass().getClassLoader().getResourceAsStream(confPath);
			System.out.println(heInputStream.toString());
				
//		String xmlString=" Exception in thread &quot;main&quot; java.lang.ArithmeticException: / by zero ...";
//		
//		xmlString=xmlString.replaceAll("&quot;", "\"");
//		System.out.println(xmlString);
//		InputStream xString;
//		xString=ClassLoaderUtil.getResourceAsStream("../StopResource/javastoplist.txt", String.class);
//		
//		
//		System.out.println("classloaderutil: "+xString);
//		
		
//		String x=ex.getResource( "D:/lihongwei/Research/research topic/help seeking/newIdeas/tool/FDUHelpSeeking/StopResource/javaStopList.txt");
//		System.out.println("classloaderutil: "+x);
//		InputStream is = ex.getClass().getResourceAsStream(  
//                "D:/lihongwei/Research/research topic/help seeking/newIdeas/tool/FDUHelpSeeking/StopResource/javaStopList.txt");  
//BufferedReader br;  
//        StringBuilder strBlder = new StringBuilder("");  
//        try {  
//            br = new BufferedReader(new InputStreamReader(is));  
//            String line = "";  
//            while (null != (line = br.readLine())) {  
//                strBlder.append(line + "\n");  
//            }  
//            br.close();  
//        } catch (FileNotFoundException e) {  
//            e.printStackTrace();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        }  
//System.out.println(strBlder.toString());
		
		
	}
	
public   String  getResource( String resourcePath) {    
		
		String content="";
        //返回读取指定资源的输入流    
		try{
        InputStream is=this.getClass().getResourceAsStream(resourcePath);     //"/resource/res.txt"
        BufferedReader in=new BufferedReader(new InputStreamReader(is));  
        
    	StringBuilder buffer = new StringBuilder();
		String line = null;

		while (null != (line = in.readLine()))
		{
			buffer.append("\t" + line);
			buffer.append("\n");

		}

		content = buffer.toString();
		in.close();

	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
	return content;
    }    


  



}
