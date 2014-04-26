package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.opensymphony.xwork2.util.ArrayUtils;

import cn.edu.fudan.se.helpseeking.bean.TaskDescription;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class ExpirmentTest {

	 
	public static void main(String[] args) {
//		Resource re=new Resource();
//		System.out.println(re.getResource("/stopresource/userstoplist.txt"));
		List<TaskDescription> myTaskList=new ArrayList<TaskDescription>(); 	
		DatabaseUtil.init();
		
						myTaskList=DatabaseUtil.getTaskDescriptionRecords();
						TaskDescription tDescription=myTaskList.get(0);
						TaskDescription tDescription2=myTaskList.get(1);
						
						String test=tDescription.getContent();
						TokenExtractor tokenExtractor=new TokenExtractor();
                        List<String> hh=removeDuplicateWithOrder((tokenExtractor.getIdentifierOccurenceOfString(test)));
                        		Collections.sort(hh);
                        		
                        		String test2=tDescription2.getContent();
                        		List<String>hh2=removeDuplicateWithOrder(tokenExtractor.getIdentifierOccurenceOfString(test2));
                        		Collections.sort(hh2);
                        		
                        		Collection<String> aa= CollectionUtils.intersection(hh, hh2);
                        		
                        		Collections.sort( (List<String>) aa);
                        		System.out.println(aa.toString());

						String tokens=CommUtil.ListToString(hh);
						
					
						System.out.println(tokens);
						
		
		DatabaseUtil.closeAll();
		
		
		
	}
	
	public static List<String> removeDuplicateWithOrder(List<String> list) {
		
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


	
public   String  getResource( String resourcePath) {    
		
	//编译阶段将文件放入到BIN目录，  生成JAR包时 记得将文件打包到JAR包的根目录下； 使用相对路径
	// “/a/b.txt”  和 “a/b.txt”不同一个是从根出发， 一个是从当前调用这个方法的类所在的相对路径出发。  通常选前面的格式
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
