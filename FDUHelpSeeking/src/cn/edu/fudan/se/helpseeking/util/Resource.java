package cn.edu.fudan.se.helpseeking.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Resource {

	
	public   String  getResource( String resourcePath) {    
		
		// mode 表示是否在每行后面加上  回车换行  true 为普通文件   false 为 DB等文件
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
	
	public   String  getResource(String resourcePath, boolean mode) {    
		
		// mode 表示是否在每行后面加上  回车换行  true 为普通文件   false 为 DB等文件
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

				if (mode) {
					buffer.append(line);
				}else{
					buffer.append("\t" + line);
				buffer.append("\n");
				}
				

				
				

							

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
