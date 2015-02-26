package cn.edu.fudan.se.helpseeking.test.lhwhttp;

import java.io.IOException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * * 最简单的HTTP客户端,用来演示通过GET或者POST方式访问某个页面
 * 
 * @author Grand
 *
 */
public class SimpleClient {
	public static void main(String[] args) throws IOException {
		HttpClient client = new HttpClient(); 
		
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		
		// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
		HttpMethod method = new GetMethod("http://www.baidu.com");
//		HttpMethod method = new GetMethod(genGetString());
		
		// 使用POST方法
		// HttpMethod method = new PostMethod("http://java.sun.com";);
		
		client.executeMethod(method); 
		
		// 打印服务器返回的状态
		System.out.println(method.getStatusLine()); // 打印返回的信息
		System.out.println(method.getResponseBodyAsString()); // 释放连接
		method.releaseConnection();
	}
	
//	GET http://www.google.com/cse/api/<USER_ID>/cse/<CSE_ID>/annotations/
//	Authorization: GoogleLogin auth="IM6F7Cx2fo0TAiwlhNVdSE8Ov8hw6aHV"

public static String genGetString() {
	
	StringBuffer buffer = new StringBuffer();
	
	buffer.append("http://www.google.com/cse/api/014132305786414512501/cse/prmarh9sn8k/annotations/\r\n");
	buffer.append("Authorization: GoogleLogin auth=\"4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI\"\r\n"); 
//	buffer.append("\r\n");
   
	return buffer.toString();

}
	
	
}