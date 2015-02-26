package cn.edu.fudan.se.helpseeking.test.lhwhttp;

/*如果是GET的请求方式，那么所有参数都直接放到页面的URL后面用问号与页面地址隔开，
 * 每个参数用&隔开，例如：http://java.sun.com?name=liudong&mobile=123456，
 * 但是当使用POST方法时就会稍微有一点点麻烦。
 */
import java.io.IOException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * 
 提交参数演示
 * 
 * 
 * 该程序连接到一个用于查询手机号码所属地的页面
 * 
 * 
 * 以便查询号码段 1330227 所在的省份以及城市
 * 
 * 
 @author 
 */

public class SimpleHttpClient {
	
	
	public static void main(String[] args) throws IOException {
		
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.imobile.com.cn", 80, "http");
		HttpMethod method = getPostMethod();// 使用POST方式提交数据
		// client.executeMethod(method);
											
		// //打印服务器返回的状态
		System.out.println(method.getStatusLine()); 
		// 打印结果页面
		String response = new String(method.getResponseBodyAsString().getBytes(
				"8859_1"));
		// 打印返回的信息
		System.out.println(response);
		method.releaseConnection();
	}

	/**
	 * 使用GET方式提交数据
	 * 
	 * @return
	 */
	private static HttpMethod getGetMethod() {
		return new GetMethod("/simcard.php?simcard=1330227");
	}

	/**
	 * 使用POST方式提交数据 * @return
	 */
	private static HttpMethod getPostMethod() {
		PostMethod post = new PostMethod("/simcard.php");
		NameValuePair simcard = new NameValuePair("simcard", "1330227");
		post.setRequestBody(new NameValuePair[] { simcard });
		return post;
	}
}
