package cn.edu.fudan.se.helpseeking.util;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.fudan.se.helpseeking.test.TestBean;


public class JsonFileHelper {
	
	public static  String listToJsonString(List<Object> dataList)
	{
		
		JSONArray json = JSONArray.fromObject(dataList);
		
		
		return json.toString();
	}

	public static void jsonStringtoFile(String fileName, String content)
	{
		FileHelper.writeNewFile(fileName, content);
	}
	

	
		public static void main(String[] args) {
		System.out.println("test object to json file :");
		
		TestBean a1=new TestBean();
		    a1.setUrl("first");
		    a1.setTitle("2");
		    //a1.setContent("this is first content");
		   		    
		    TestBean a2=new TestBean();
		    a2.setUrl("second");
		    a2.setTitle("6");
		    //a2.setContent("this is second content");
		   		    
			List<Object> dataList=new ArrayList<Object>();
			dataList.add(a1);
			dataList.add(a2);

		    
		    String userpath=CommUtil.getCurrentProjectPath()+"/testjson.txt";
		    
//		    String content=listToJsonString(dataList);
		    
//		    JSONObject json = JSONObject.fromObject(a1);
	
		     String content = new Gson().toJson(dataList);
		    
		    System.out.println(userpath);
		    jsonStringtoFile(userpath, content); 
		    System.out.println(content);
		
		
	}


	
}
