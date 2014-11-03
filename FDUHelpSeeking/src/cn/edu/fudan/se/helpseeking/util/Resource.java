package cn.edu.fudan.se.helpseeking.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;

public class Resource {

	
	
	public   LinkedList <String>  getResourcetoLinkedList( String resourcePath) {    
		
		//编译阶段将文件放入到BIN目录，  生成JAR包时 记得将文件打包到JAR包的根目录下； 使用相对路径
		// “/a/b.txt”  和 “a/b.txt”不同一个是从根出发， 一个是从当前调用这个方法的类所在的相对路径出发。  通常选前面的格式
		LinkedList <String>  content=new LinkedList<String>();
	        //返回读取指定资源的输入流    
			try{
	        InputStream is=this.getClass().getResourceAsStream(resourcePath);     //"/resource/res.txt"
	        BufferedReader in=new BufferedReader(new InputStreamReader(is));  
	        
	    	
			String line = null;

			while (null != (line = in.readLine()))
			{
				
			content.add(line);
				

			}

			in.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
			
		return content;
	    
	}

	
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
					buffer.append("\r\n");
				}else{
				buffer.append("\r\n" + line);
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

	
	//直接写入到文件中 
	//mode 为false时，  resourcePath 和 filenamepath 为绝对路径   
	//mode 为true ,    resourcepaht是JAR包种数据相对路径    filenamepath 为绝对路径
	public   void  getResource(String resourcePath, boolean mode, String fileNamePath) {    
		
	
		File file=new File(fileNamePath);
		//已有文件不写了
		if (file.exists()) {
			return;
		}

		
		InputStream is=null;
		 
		 
		if (mode) {
			is=this.getClass().getResourceAsStream(resourcePath);     //"/resource/res.txt"
		}else {
			try {
				is=new FileInputStream(resourcePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
 		
		OutputStream os=null;
		try{
		os=new FileOutputStream(file);
		byte buffer[]=new byte[4*1024];
		int len = 0;
		while((len = is.read(buffer)) != -1)
		{
		 os.write(buffer,0,len);

		}
		os.flush();
		}
		catch(Exception e){
		e.printStackTrace();
		}
		finally{
		try{
		os.close();
		}
		catch(Exception e){
		e.printStackTrace();
		}
		}
		
	}



}
