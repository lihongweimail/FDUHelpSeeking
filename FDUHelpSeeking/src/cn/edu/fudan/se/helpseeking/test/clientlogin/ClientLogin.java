package cn.edu.fudan.se.helpseeking.test.clientlogin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ClientLogin {

	private static final String CHARSET_UTF8 = "UTF-8";
	// Timeout when reading from Input stream when a connection is established to a resource
	private static final int DEFULT_READ_TIMEOUT = 5000;
	// Timeout for establishing a connection.
	private static final int DEFULT_CONNECT_TIMEOUT = 5000;

	public static String GOOGLE = "GOOGLE";
	public static String HOSTED = "HOSTED";
	public static String HOSTED_OR_GOOGLE = "HOSTED_OR_GOOGLE"; 
	
	public static String getAuthToken(String email,String password, String service, String source){
		return getAuthToken(HOSTED_OR_GOOGLE, email, password, service, source);
	}
	
//	https://accounts.google.com/o/oauth2/auth?
//		  scope=email%20profile&
//		  redirect_uri=urn:ietf:wg:oauth:2.0:oob&
//		  response_type=code&
//		  client_id=812741506391-h38jh0j4fv0ce1krdkiq0hfvt6n5amrf.apps.googleusercontent.com
	
	public static String getAuthToken(String accountType, String email,String password, String service, String source) {
		try {
//			String str = post(
//					"https://accounts.google.com/o/oauth2/auth",
//					"accountType=" + accountType + "&Email=" + email
//							+ "&Passwd=" + password + "&service=" + service
//							+ "&source=" + source, "x-www-form-urlencoded");
			
			String str = post(
					"https://accounts.google.com/o/oauth2/auth",
					"scope=email%20profile&"
					+ "redirect_uri=urn:ietf:wg:oauth:2.0:oob&"
					+ "response_type=code&"
					+ "433540504100-ogf00jmc1kj142ctognbn8cbjbjegegt.apps.googleusercontent.com",
					"x-www-form-urlencoded");
			
			
			String[] lines = str.split(System.getProperty("line.separator"));
			for (String line : lines) {
				if (line.startsWith("Auth=")) {
					return line.substring(5);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}


	/*
	 * Get the HTTP response message from the server.
	 */
	private static String getResponseMessage(HttpURLConnection connection) throws UnsupportedEncodingException,
			IOException {
		String responseMessage = null;
		StringBuffer sb = new StringBuffer();
		InputStream dis = connection.getInputStream();
		int chr;
		while ((chr = dis.read()) != -1) {
			sb.append((char) chr);
		}
		if (sb != null) {
			responseMessage = sb.toString();
		}
		return responseMessage;
	}

	static private String post(String url, String query, String contentType)
			throws MalformedURLException, IOException {
		URLConnection connection = new URL(url).openConnection();
		connection.setReadTimeout(DEFULT_READ_TIMEOUT);
		connection.setConnectTimeout(DEFULT_CONNECT_TIMEOUT);
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", CHARSET_UTF8);
		connection.setRequestProperty("Content-Type", "application/"
				+ contentType);
		OutputStream output = null;
		try {
			output = connection.getOutputStream();
			output.write(query.getBytes(CHARSET_UTF8));
		} finally {
			closeSilently(output);
		}
		return getResponseMessage((HttpURLConnection) connection);
	}

	/*
	 * Close the connection, if the connection could not be closed (probably
	 * because its already closed) ignore the error, You could look the error or
	 * you could set the outPut to null to make it easier for GC
	 */
	private static void closeSilently(OutputStream output) {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
			}
		}
	}

}
