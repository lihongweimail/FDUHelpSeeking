package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.fudan.se.helpseeking.util.Resource;

public class ExpirmentTest {

	public static void main(String[] args) {
		Resource re=new Resource();
		System.out.println(re.getResource("/stopresource/userstoplist.txt"));
		
		
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
