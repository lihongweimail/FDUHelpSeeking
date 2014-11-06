package cn.edu.fudan.se.helpseeking.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fnlp.nlp.cn.ner.stringPreHandlingModule;

import cn.edu.fudan.se.helpseeking.util.ChangeCharset;
import urlWordExtract.UrlWordExtract;

//执行多个带返回值的任务，并取得多个返回值

public class CallableAndFuture {
	
	public static void main(String[] args) 
	{
		ExecutorService threadPool = Executors.newCachedThreadPool();
		CompletionService<String> cs = new ExecutorCompletionService<String>(threadPool);
		
		final List<String> urllist=new ArrayList<String>();
		urllist.add("http://www.baidu.com");
		urllist.add("http://mil.news.sina.com.cn");
		urllist.add("http://www.apple.com");
		urllist.add("http://www.fudan.edu.cn");
		urllist.add("http://www.eclipse.org");
		
		String pageContent="";
		
		for(int i = 0; i < 5; i++) {
			final int taskID = i;
			System.out.println("taskid: "+taskID);
			cs.submit(
			new Callable<String>() 
			{
				String myString="";
				public String getMyString()
				{
					return this.myString;
				}
				public void setMyString(String s)
				{
					this.myString=s;
				}
				
				public String call() throws Exception 
				
				{
					ChangeCharset cc=new ChangeCharset();
					String content=cc.toUTF_8(UrlWordExtract.getText(urllist.get(taskID)));
					if (content==null) {
						content="";
					}
					setMyString(content);
					
					return content;
				}
		     }
			);
		}
		
	
		System.out.println("do something");
		// 可能做一些事情
		for(int i = 0; i < 5; i++) 
		{
			
			String str="";
			try {
				str=cs.take().get();
			
				
			//	System.out.println(str);
			} catch (InterruptedException e) 
			{
				System.out.println("this is i="+i+"interruptedexception "+e.getMessage());
			} catch (ExecutionException e) 
			{
				System.out.println("this is i="+i+"executionexception "+e.getMessage());
				if (e.getMessage().contains("java.lang.NullPointerException")) {
					str=" ";
				}
				if (e.getMessage().contains("java.lang.Error: Unresolved compilation problem:")) {
//					java.lang.Error: Unresolved compilation problem: 
//						urlConnection cannot be resolved to a type
//
					
					
				}
			}
			pageContent=pageContent+"\n ###hehe###"+i+": \n"+str;
		}
		
		System.out.println(pageContent);
		System.out.println("do something");	
	}
	
}
	