package cn.edu.fudan.se.helpseeking.test;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class MainSendRequest {
	
		   public String getHTML(String urlToRead) {
		      URL url;
		      HttpURLConnection conn;
		      BufferedReader rd;
		      String line;
		      String result = "";
		      try {
		         url = new URL(urlToRead);
		         conn = (HttpURLConnection) url.openConnection();
		         conn.setRequestMethod("GET");
		         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		         while ((line = rd.readLine()) != null) {
		            result += line;
		         }
		         rd.close();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      return result;
		   }

		   
		   
		   public static void main(String args[])
		   {
			   MainSendRequest c = new MainSendRequest();
		     System.out.println(c.getHTML(args[0]));
		   }
		
	
//	POST http://www.google.com/cse/api/<USER_ID>/cse/<CSE_ID>my_first_cse Content-Type: text/xml
//		Authorization: GoogleLogin auth="IM6F7Cx2fo0TAiwlhNVdSE8Ov8hw6aHV"
//		<CustomSearchEngine keywords="cars" language="en"> <Title>My First CSE</Title>
//		<Description>Car Search</Description>
//		<Context>
//		<BackgroundLabels>
//		<Label name="_cse_my_first_cse" mode="FILTER" />
//		<Label name="_cse_exclude_my_first_cse" mode="ELIMINATE" />
//		</BackgroundLabels>
//		</Context>
//		<LookAndFeel nonprofit="false" />
//		</CustomSearchEngine>
	
	public static String excutePost(String targetURL, String urlParameters) {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");

	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  

	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response    
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}

 
}