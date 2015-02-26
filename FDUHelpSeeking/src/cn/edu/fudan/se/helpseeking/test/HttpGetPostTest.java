package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.ads.common.lib.exception.AuthenticationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.util.store.FileDataStoreFactory;

public class HttpGetPostTest {
	

	protected Socket client;
	protected BufferedOutputStream sender;
	protected BufferedInputStream receiver;
	protected ByteArrayInputStream byteStream;
	protected URL target;
	private int responseCode=-1;
	private String responseMessage="";
	private String serverVersion="";
	private Properties header = new Properties();
	
	
	public HttpGetPostTest() {
	}

	public HttpGetPostTest(String url) {
		GET(url);
	}
	

	/* GET方法根据URL，会请求文件、数据库查询结果、程序运行结果等多种内容 */
	public void GET(String url) {
		try {
			checkHTTP(url);
			openServer(target.getHost(), target.getPort());
			String cmd = "GET " + getURLFormat(target) + " HTTP/1.0\r\n"
					+ getBaseHeads() + "\r\n";
			sendMessage(cmd);
			receiveMessage();
			System.out.println(receiver.toString());
			
		} catch (ProtocolException p) {
			p.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}

	}
	
	
	public void MYGET(String url) {
//		retrieving annotations
		
//		GET http://www.google.com/cse/api/<USER_ID>/cse/<CSE_ID>/annotations/
//			Authorization: GoogleLogin auth="IM6F7Cx2fo0TAiwlhNVdSE8Ov8hw6aHV"
//		GET  "http://www.google.com/cse/api/014132305786414512501/cse/prmarh9sn8k/annotations/"
//		"Authorization: GoogleLogin auth=\"4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI\"\r\n" ;
		
		try {
			
				
			url="http://www.google.com/cse/api/014132305786414512501/cse/prmarh9sn8k/annotations/";
			checkHTTP(url);
			openServer(target.getHost(), target.getPort());
			String cmd = "GET " + getURLFormat(target) + " HTTP/1.0\r\n"
					+ "Authorization: GoogleLogin auth=\"4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI\"\r\n" ;
//					+ getBaseHeads() + "\r\n";
			
			sendMessage(cmd);
			receiveMessage();
			System.out.println(receiver.toString());
			
		} catch (ProtocolException p) {
			p.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}

	}
	
	/*
	* HEAD方法只请求URL的元信息，不包括URL本身。若怀疑本机和服务器上的
	* 文件相同，用这个方法检查最快捷有效。
	*/
	
	public void HEAD(String url) {
		try {
			checkHTTP(url);
			openServer(target.getHost(), target.getPort());
			String cmd = "HEAD " + getURLFormat(target) + " HTTP/1.0\r\n"
					+ getBaseHeads() + "\r\n";
			sendMessage(cmd);
			receiveMessage();
		} catch (ProtocolException p) {
			p.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}
	}
	
	
	
	/*
	* POST方法是向服务器传送数据，以便服务器做出相应的处理。例如网页上常用的
	* 提交表格。
	* 

	*/
	
	

//测试访问定制的引擎：
//[cseFduTest]
//cx=014132305786414512501:prmarh9sn8k
//		projectid=rosy-resolver-863
//		key=AIzaSyAlqmv7SHFrJptyWdEKjXnR5_PkTVAHpfs
//		csename=Helpseekingfdu
//		OAuth=4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI
//		clientid=433540504100-ogf00jmc1kj142ctognbn8cbjbjegegt.apps.googleusercontent.com
//		CLIENTSECRET=dBJKZCKSyRwnFK73dsGxDcRX
//		REDIRECTURIS=urn:ietf:wg:oauth:2.0:oob

//	  在cse的manager中上传成功这个context文件   2月23日 尝试改造为POST看是否成功
//	<?xml version="1.0" encoding="UTF-8" ?>
//	<CustomSearchEngine id="prmarh9sn8k" creator="014132305786414512501" language="en" >
//	  <Title>Helpseekingfdu</Title>
//	  <Description>Car Helpseeking fdu</Description>
//	  <Context>
//	    <BackgroundLabels>
//	      <Label name="_cse_prmarh9sn8k" mode="FILTER" />
//	      <Label name="_cse_exclude_prmarh9sn8k" mode="ELIMINATE" />
//	    </BackgroundLabels>
//	  </Context>
//	  <LookAndFeel nonprofit="false" />
//	</CustomSearchEngine>



	
	public void MYPOST(String url, String content, String contentType) {

		

			url="http://www.google.com/cse/api/014132305786414512501/cse/prmarh9sn8k";
			contentType= " text/xml";
			
		content="Authorization: GoogleLogin auth=\"4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI\"\r\n"
				+ "<CustomSearchEngine  id=\"prmarh9sn8k\" creator=\"014132305786414512501\" keywords=\"cars\" language=\"en\">\r\n"
				+ "<Title>Helpseekingfdu</Title>\r\n"
				+ "<Description>Car Helpseeking fdu</Description>\r\n"
				+ "<Context>\r\n"
				+ "<BackgroundLabels>\r\n"
				+ "<Label name=\"_cse_prmarh9sn8k\" mode=\"FILTER\" />\r\n"
				+ "<Label name=\"_cse_exclude_prmarh9sn8k\" mode=\"ELIMINATE\" />\r\n"
				+ "</BackgroundLabels>\r\n"
				+ "</Context>\r\n"
				+ "<LookAndFeel nonprofit=\"false\" />\r\n"
				+ "</CustomSearchEngine>";

 try {
		checkHTTP(url);
			openServer(target.getHost(), target.getPort());
			String cmd = "POST " + getURLFormat(target) + "HTTP/1.0\r\n"
					+ getBaseHeads();
			cmd += "Content-type: "+contentType+"\r\n";
//			cmd += "Content-length: " + content.length() + "\r\n\r\n";
			cmd +=
			cmd += content + "\r\n";
			sendMessage(cmd);
			receiveMessage();
		} catch (ProtocolException p) {
			p.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}
	}	

	
//	POST http://www.google.com/cse/api/<USER_ID>/annotations/<CSE_ID>
//		Content-Type: text/xml
//		Authorization: GoogleLogin auth="IM6F7Cx2fo0TAiwlhNVdSE8Ov8hw6aHV"
//	

	
	public void MYPOSTANNOTATION(String url, String content, String contentType) {

		url="http://www.google.com/cse/api/014132305786414512501/annotations/prmarh9sn8k";
		contentType= " text/xml";
		
	content="Authorization: GoogleLogin auth=\"4/MTbmwWh7aakH8FANk0mNcOZsCx67hvpOXxBGpjBsbgc.4kVfCtGD8yoR3oEBd8DOtNBtFg1klwI\"\r\n"
			+ "\r\n"
			+ "<Batch>\r\n"
			+ "<Add>\r\n"
			+ "<Annotations>\r\n"
			+ "<Annotation about=\"http://www.solarenergy.org/*\">\r\n"
			+ "<Label name=\"_cse_prmarh9sn8k\"/>\r\n"
			+ "</Annotation>\r\n"
			+ "<Annotation about=\"http://www.solarfacts.net/*\">\r\n"
			+ "<Label name=\"_cse_prmarh9sn8k\"/>\r\n"
			+ "</Annotation>\r\n"
			+ "<Annotation about=\"http://en.wikipedia.org/wiki/*\">\r\n"
			+ "<Label name=\"_cse_exclude_prmarh9sn8k\"/>\r\n"
			+ "</Annotation>\r\n"
			+ "</Annotations>\r\n"
			+ "</Add>\r\n"
			+ "</Batch>";

try {
	checkHTTP(url);
		openServer(target.getHost(), target.getPort());
		String cmd = "POST " + getURLFormat(target) + "HTTP/1.0\r\n"
				+ getBaseHeads();
		cmd += "Content-type: "+contentType+"\r\n";
//		cmd += "Content-length: " + content.length() + "\r\n\r\n";
		cmd +=
		cmd += content + "\r\n";
		sendMessage(cmd);
		receiveMessage();
	} catch (ProtocolException p) {
		p.printStackTrace();
		return;
	} catch (UnknownHostException e) {
		e.printStackTrace();
		return;
	} catch (IOException i) {
		i.printStackTrace();
		return;
	}
}	
	
	
	
	public void POST(String url, String content, String contentType) {
		try {
			checkHTTP(url);
			openServer(target.getHost(), target.getPort());
			String cmd = "POST " + getURLFormat(target) + "HTTP/1.0\r\n"
					+ getBaseHeads();
			cmd += "Content-type: "+contentType+"\r\n";
			cmd += "Content-length: " + content.length() + "\r\n\r\n";
			cmd += content + "\r\n";
			sendMessage(cmd);
			receiveMessage();
		} catch (ProtocolException p) {
			p.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}
	}	
	
	protected void checkHTTP(String url) throws ProtocolException {
		try {
			URL target = new URL(url);
			if (target == null
					|| !target.getProtocol().toUpperCase().equals("HTTP"))
				throw new ProtocolException("这不是HTTP协议");
			this.target = target;
		} catch (MalformedURLException m) {
			throw new ProtocolException("协议格式错误");
		}
	}
	
	
	/*
	* 与Web服务器连接。若找不到Web服务器，InetAddress会引发UnknownHostException
	* 异常。若Socket连接失败，会引发IOException异常。
	*/

	protected void openServer(String host, int port)
			throws UnknownHostException, IOException {
		header.clear();
		responseMessage = "";
		responseCode = -1;
		try {
			if (client != null)
				closeServer();
			if (byteStream != null) {
				byteStream.close();
				byteStream = null;
			}
			InetAddress address = InetAddress.getByName(host);
			client = new Socket(address, port == -1 ? 80 : port);
			sender = new BufferedOutputStream(client.getOutputStream());
			receiver = new BufferedInputStream(client.getInputStream());
		} catch (UnknownHostException u) {
			throw u;
		} catch (IOException i) {
			throw i;
		}
	}
	
	
	/* 关闭与Web服务器的连接 */
	protected void closeServer() throws IOException {
		if (client == null)
			return;
		try {
			client.close();
			sender.close();
			receiver.close();
		} catch (IOException i) {
			throw i;
		}
		client = null;
		sender = null;
		receiver = null;
	}

	protected String getURLFormat(URL target) {
		String spec = "http://" + target.getHost();
		if (target.getPort() != -1)
			spec += ":" + target.getPort();
		return spec += target.getFile();
	}

	/* 向Web服务器传送数据 */
	protected void sendMessage(String data) throws IOException {
		sender.write(data.getBytes(), 0, data.length());
		sender.flush();
	}
	
	
	/* 接收来自Web服务器的数据 */
	protected void receiveMessage() throws IOException {
		byte data[] = new byte[1024];
		int count = 0;
		int word = -1;
		// 解析第一行
		while ((word = receiver.read()) != -1) {
			if (word == '\r' || word == '\n') {
				word = receiver.read();
				if (word == '\n')
					word = receiver.read();
				break;
			}
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
		}
		String message = new String(data, 0, count);
		int mark = message.indexOf(32);
		serverVersion = message.substring(0, mark);
		while (mark < message.length() && message.charAt(mark + 1) == 32)
			mark++;
		responseCode = Integer.parseInt(message.substring(mark + 1, mark += 4));
		responseMessage = message.substring(mark, message.length()).trim();
		// 应答状态码和处理请读者添加
		switch (responseCode) {
		case 401:
			 throw new IOException("Authorization failure 认证失败");
		case 403:
			 throw new IOException("Forbidden禁止");
		case 405:
			 throw new IOException("method not allowed");
		case 413:
			 throw new IOException("file to large");

		case 500:
			 throw new IOException("server error");


		case 400:
			throw new IOException("错误请求");
		case 404:
			throw new FileNotFoundException(getURLFormat(target));
		case 503:
			throw new IOException("服务器不可用");
		}
		if (word == -1)
			throw new ProtocolException("信息接收异常终止");
		int symbol = -1;
		count = 0;
		// 解析元信息
		while (word != '\r' && word != '\n' && word > -1) {
			if (word == '\t')
				word = 32;
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
			parseLine: {
				while ((symbol = receiver.read()) > -1) {
					switch (symbol) {
					case '\t':
						symbol = 32;
						break;
					case '\r':
					case '\n':
						word = receiver.read();
						if (symbol == '\r' && word == '\n') {
							word = receiver.read();
							if (word == '\r')
								word = receiver.read();
						}
						if (word == '\r' || word == '\n' || word > 32)
							break parseLine;
						symbol = 32;
						break;
					}
					if (count == data.length)
						data = addCapacity(data);
					data[count++] = (byte) symbol;
				}
				word = -1;
			}
			message = new String(data, 0, count);
			mark = message.indexOf(':');
			String key = null;
			if (mark > 0)
				key = message.substring(0, mark);
			mark++;
			while (mark < message.length() && message.charAt(mark) <= 32)
				mark++;
			String value = message.substring(mark, message.length());
			header.put(key, value);
			count = 0;
		}
		// 获得正文数据
		while ((word = receiver.read()) != -1) {
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
		}
		if (count > 0)
			byteStream = new ByteArrayInputStream(data, 0, count);
		data = null;
		closeServer();
	}
	
	
	public String getResponseMessage() {
	return responseMessage;
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public InputStream getInputStream() {
		return byteStream;
	}
	
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpGetPostTest.class); 
	 
	 
    public static String fetchAccessToken(String code, Map<String, String> appProperties) throws IOException { 
        // try to promote code 
        final HttpClient httpClient = new DefaultHttpClient(); 
        final HttpPost post = new HttpPost("https://accounts.google.com/o/oauth2/token"); 
 
        final HttpEntity entity = new UrlEncodedFormEntity(Arrays.asList( 
                new BasicNameValuePair("client_id", appProperties.get("google.clientId")), 
                new BasicNameValuePair("client_secret", appProperties.get("google.clientSecret")), 
                new BasicNameValuePair("redirect_uri", "http://localhost:9090/gdata-webapp-test/oauth.do"), 
                new BasicNameValuePair("grant_type", "authorization_code"), 
                new BasicNameValuePair("code", code))); 
        post.setEntity(entity); 
 
        final HttpResponse response = httpClient.execute(post); 
        LOG.info("Status: {}", response.getStatusLine()); 
 
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
            if (response.getEntity() != null) { 
                //final int contentLength = (int) response.getEntity().getContentLength(); 
                final StringBuilder stringBuilder = new StringBuilder(); 
                final String encoding = "UTF-8"; 
 
                final InputStream inputStream = response.getEntity().getContent(); 
                try { 
                    final InputStreamReader reader = new InputStreamReader(inputStream, encoding); 
                    try { 
                        for (;;) { 
                            final char[] buffer = new char[1024]; 
                            final int charsRead = reader.read(buffer); 
                            if (charsRead <= 0) { 
                                break; 
                            } 
 
                            stringBuilder.append(buffer, 0, charsRead); 
                        } 
                    } finally { 
                        reader.close(); 
                    } 
                } finally { 
                    inputStream.close(); 
                } 
 
                final String contentStr = stringBuilder.toString(); 
                return getAccessTokenValue(contentStr); 
            } 
        } 
 
        return null; 
    } 
 
    private static String getAccessTokenValue(String content) { 
        // 
        // Search the value of the pair "access_token": "<<ACCESS TOKEN VALUE>>", 
        // <<ACCESS TOKEN VALUE>> is returned (or null if there is no such value). 
 
        final String accessTokenKey = "\"access_token\""; 
        final int accessTokenPos = content.indexOf(accessTokenKey); 
        if (accessTokenPos < 0) { 
            return null; 
        } 
 
        final int valueStart = content.indexOf('"', accessTokenPos + accessTokenKey.length() + 1); 
        if (valueStart < 0) { 
            return null; 
        } 
 
        final int valueEnd = content.indexOf('"', valueStart + 1); 
        if (valueEnd < 0) { 
            return null; 
        } 
 
        return content.substring(valueStart + 1, valueEnd); 
    } 
	
public synchronized String getHeaderKey(int i)
	{
	
	if(i>=header.size())
		return null;
	
	header.propertyNames();
	Enumeration enum1 = header.propertyNames();
	
	String key = null;
	for(int j=0; j<=i; j++)
	key = (String) enum1.nextElement();
	return key;
	}

	public synchronized String getHeaderValue(int i) {
		if (i >= header.size())
			return null;
		return header.getProperty(getHeaderKey(i));
	}

	public synchronized String getHeaderValue(String key) {
		return header.getProperty(key);
	}

	protected String getBaseHeads() {
		String inf = "User-Agent: myselfHttp/1.0\r\n"
				+ "Accept: www/source; text/html; image/gif; */*\r\n";
		return inf;
	}

	private byte[] addCapacity(byte rece[]) {
		byte temp[] = new byte[rece.length + 1024];
		System.arraycopy(rece, 0, temp, 0, rece.length);
		return temp;
	}

	
	
	public static void main(String[] args) {
//		POST http://www.google.com/cse/api/<USER_ID>/cse/<CSE_ID>my_first_cse Content-Type: text/xml
//			Authorization: GoogleLogin auth="IM6F7Cx2fo0TAiwlhNVdSE8Ov8hw6aHV"
//			<CustomSearchEngine keywords="cars" language="en"> <Title>My First CSE</Title>
//			<Description>Car Search</Description>
//			<Context>
//			<BackgroundLabels>
//			<Label name="_cse_my_first_cse" mode="FILTER" />
//			<Label name="_cse_exclude_my_first_cse" mode="ELIMINATE" />
//			</BackgroundLabels>
//			</Context>
//			<LookAndFeel nonprofit="false" />
//			</CustomSearchEngine>
		
//		测试访问定制的引擎：
//		[cseFduTest]
//		cx=014132305786414512501:prmarh9sn8k
//				projectid=rosy-resolver-863
//				key=AIzaSyAlqmv7SHFrJptyWdEKjXnR5_PkTVAHpfs
//				csename=Helpseekingfdu
//				OAuth=4/AfqhooY6mHzk601fu_PeuRIzr-ijTGoo96BzR-XLofg.YrqlrWQA1w0U3oEBd8DOtNALOexelwI
//				clientid=433540504100-ogf00jmc1kj142ctognbn8cbjbjegegt.apps.googleusercontent.com
//				CLIENTSECRET=dBJKZCKSyRwnFK73dsGxDcRX
//
//
//
//				REDIRECTURIS=urn:ietf:wg:oauth:2.0:oob
//

		
		HttpGetPostTest myGetPostTest=new HttpGetPostTest();
		
		
//		myGetPostTest.GET("http://www.google.com/cse/api/014132305786414512501/cse/prmarh9sn8k");
		
		String url="";
		String content="";
		String contentType="";
//		myGetPostTest.MYPOST(url, content, contentType);
//		myGetPostTest.MYPOSTANNOTATION(url, content, contentType);
		
		myGetPostTest.MYGET(url);
		
		}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  String url = new GoogleBrowserClientRequestUrl("812741506391.apps.googleusercontent.com",
		      "https://oauth2-login-demo.appspot.com/oauthcallback", Arrays.asList(
		          "https://www.googleapis.com/auth/userinfo.email",
		          "https://www.googleapis.com/auth/userinfo.profile")).setState("/profile").build();
		  response.sendRedirect(url);
		}
	

	
	
}
