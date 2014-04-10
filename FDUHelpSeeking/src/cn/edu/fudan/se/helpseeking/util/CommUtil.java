package cn.edu.fudan.se.helpseeking.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CommUtil 
{
	private static final String SPLITE_STRING = "[; ]";

	public static boolean stringNullOrZero(String str)
	{
		return str==null ||  str.trim().length() == 0;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getProjectNameFromProjectPath("Buddi"));
	}

	
	public static String getCurrentProjectPath()
	{	
		String result = new File("").getAbsolutePath();
		if(result.endsWith("target\\classes"))
			result= result.substring(0, result.length() - 15);
		return  result;
	}
	
	public static String getProjectNameFromProjectPath(String projectPath)
	{
		return new File(projectPath).getName();
	}
	
	public static String ListToString(List<Object> list)
	{
		String result = "";
		for(Object object : list)
		{
			if(object!=null)
				result = result +  object.toString() + ";" ;
		}
		return result.trim();
	}
	
	public static String tokenListToString(List<String> list)
	{
		String result = "";
		int count=0;
		for(String object : list)
		{
			count=count+object.length();
			if(object!=null || count<65530)
				result = result +  object + " " ;
		}
		return result.trim();
	}
	

	public static List<String> arrayToList(String[] strContent)
	{
		List<String> result = new ArrayList<String>();
		for(String str : strContent)
		{
			if(str.trim().length()>0)
				result.add(str);			
		}
		return result;
	}
	
	public static List<String> stringToList(String strContent)
	{
		List<String> result = new ArrayList<String>();
		for(String str : strContent.split(SPLITE_STRING))
		{
			if(str.trim().length()>0)
				result.add(str);			
		}
		return result;
	}
	
	public static String getDateTime()
	{
		String result;
		GregorianCalendar calendar = new GregorianCalendar();
		result = calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DATE);
		return result;
		
	}
	
	public static int CompareStr(String str1,String str2)
	{
		
		if( str1==null)
			str1="";
		if(str2 ==null)
			str2="";
		
		return str1.compareTo(str2);
	}

	

	

	




	

	public static String trimOnlySign(String fullMethodName)
	{
	    String trimMethodName=fullMethodName;
		int indexP=fullMethodName.indexOf("(", 0);
		trimMethodName=fullMethodName.substring(0, indexP);
			
		//System.out.println(trimMethodName);
					
		return trimMethodName;
		
	}
	
	public static String trimMethodAndSign(String fullMethodName)
	{
	    String trimMethodName=fullMethodName;
		int indexP=fullMethodName.indexOf("(", 0);
		trimMethodName=fullMethodName.substring(0, indexP);
		indexP=trimMethodName.lastIndexOf(".");
		trimMethodName=trimMethodName.substring(0, indexP);
		
		//System.out.println(trimMethodName);
		
				
		return trimMethodName;
		
	}
}
