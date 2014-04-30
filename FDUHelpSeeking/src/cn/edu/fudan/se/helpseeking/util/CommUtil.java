package cn.edu.fudan.se.helpseeking.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic;

public class CommUtil 
{
	private static final String SPLITE_STRING = "[; ]";

	public static boolean stringNullOrZero(String str)
	{
		return str==null ||  str.trim().length() == 0;
	}
	
	
	
	
	private static int minLength =2;
	
	public static List<String> removeDuplicateWithOrder(List<String> list) {
		Collections.sort(list);
		List<String> listTemp= new ArrayList<String>();  
		 Iterator<String> it=list.iterator();  
		 while(it.hasNext()){  
		  String a=it.next();  
		  if(listTemp.contains(a)){  
		   it.remove();  
		  }  
		  else{  
		   listTemp.add(a);  
		  }  
		 }  
		        return listTemp;
		}

	
	
	public static List<String> removeStopWordsFromList( List<String> tokens) {
		List<String> result=new ArrayList<String>();
		 List<String> keyWords=constructDefaultFilterString("StopResource","javaStopList.txt","userStopList.txt");
		  
		  for (String token : tokens) {
		 
		if(token.length() >= minLength && (int) token.charAt(0)< 255)
		{	
				token = token.toLowerCase();		
		        boolean flage=false;
			for (String keyword : keyWords) {
				if (token.trim().equals(keyword.trim())) {
//					System.out.println("not add keyword:"+ token);
					flage=true;
					break;
				}
			}
			
			if (!flage) {
					result.add(token);
			}
			
			}
		}
		
		return result;
		
	}

	
public static List<String> removeStopWordsAsList( String tokens) {
	List<String> result=new ArrayList<String>();
	 List<String> keyWords=constructDefaultFilterString("StopResource","javaStopList.txt","userStopList.txt");
	  String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
	
	  for (String token : tokens.split(SPLIT_STRING)) {
	 
	if(token.length() >= minLength && (int) token.charAt(0)< 255)
	{	
			token = token.toLowerCase();		
	        boolean flage=false;
		for (String keyword : keyWords) {
			if (token.trim().equals(keyword.trim())) {
//				System.out.println("not add keyword:"+ token);
				flage=true;
				break;
			}
		}
		
		if (!flage) {
				result.add(token);
		}
		
		}
	}
	
	return result;
	
}

public static  String  removeStopWordsAsString(String tokens) 
{
	String result="";
if (tokens==null ||tokens.equals("")) {
	return null;
	
}
	List<String> keyWords=constructDefaultFilterString("StopResource","javaStopList.txt","userStopList.txt");
	 
	
	  for (String token : tokens.split(Basic.SPLIT_STRING)) {
	 
	if(token.length() >= minLength && (int) token.charAt(0)< 255)
	{	
			token = token.toLowerCase();		
	        boolean flage=false;
		for (String keyword : keyWords) {
			if (token.trim().equals(keyword.trim())) {
//				System.out.println("not add keyword:"+ token);
				flage=true;
				break;
			}
		}
		
		if (!flage) {
			if (result.equals("")) {
				result=token;
			}else
				{result=result+";"+token;}
		}
		
		}
	}
	
	return result;
	
}





	/**
	 * @param stop_list_path    "StopResource"
	 * @param stopfileName      "javaStopList.txt"
	 * @param userStopFileName  "userStopList.txt"
	 * @return
	 */
	public static List<String> constructDefaultFilterString(String stop_list_path,
			String stopfileName, String userStopFileName)
	{	
		 List<String> keyWords=new ArrayList<String>();
		  String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
		stopfileName = "/" + stop_list_path +  "/" + stopfileName;
		userStopFileName="/"+ stop_list_path + "/"+userStopFileName;
		
		Resource myResource=new Resource();
	     String tempKeyWords1 = myResource.getResource(stopfileName).toLowerCase();
	     String tempKeyWords2 = myResource.getResource(userStopFileName).toLowerCase();
	     
	     keyWords=CommUtil.arrayToList((tempKeyWords1+","+tempKeyWords2).split(SPLIT_STRING));
	     for (int i = 0; i < keyWords.size()-1; i++) {
			for (int j = i+1; j < keyWords.size(); j++) {
				if ((keyWords.get(i).trim()).equals(keyWords.get(j).trim())) {
					
//					System.out.println("remove word: "+keyWords.get(j)+" j="+j);
					keyWords.remove(j);
					j=j-1;
				}
			}
			
		}
	    	 
	 return keyWords;
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
	
	public static String ListToString(List<String> list)
	{
		String result = "";
		
		if (list!=null) {
			
	
		for(String object : list)
		{
			if(object!=null)
				result = result +  object.toString() + ";" ;
		}
		result=result.trim();
			}
		else {
			result=null;
		}
		
		return result;
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
	
	public static List<String> stringToList(String strContent, String mySpliteString)
	{
		List<String> result = new ArrayList<String>();
		for(String str : strContent.split(mySpliteString))
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



	public static List<String> stringToList(String tempString,
			String splitString, int miniLength) 
	{		
		List<String> result = new ArrayList<String>();
		for(String str : tempString.split(splitString))
		{
			if(str.trim().length()>miniLength)
				result.add(str);			
		}
		return result;
	}
}
