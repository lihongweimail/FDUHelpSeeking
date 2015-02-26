package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map.Entry;

public class URLFireRequestTest {

	// http://www.baidu.com/s?wd=快乐大本营&rsv_spt=1&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg
	String url = "http://www.baidu.com/s";
	String charset = "UTF-8"; // Or in Java 7 and later, use the constant:
								// java.nio.charset.StandardCharsets.UTF_8.name()
	String wd = "spring";
	String param2 = "value2";

	public String initQuery() {
		// ...
		String query = "";
		try {
			query = String.format("wd=%s", URLEncoder.encode(wd, charset));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return query;
	}

	public String httpGet(String query) {
		URLConnection connection;
		InputStream response;
		String result = "";
		BufferedReader rd;
		String line;

		try {
			connection = new URL(url + "?" + query).openConnection();

			connection.setRequestProperty("Accept-Charset", charset);
			response = connection.getInputStream();

			rd = new BufferedReader(new InputStreamReader(response));
			while ((line = rd.readLine()) != null) {
				result += line + "\r\n";
			}
			rd.close();

			// ...
			// If you don't need to set any headers, then you can even use the
			// URL#openStream() shortcut method.
			// InputStream response = new URL(url).openStream();
			// ... Either way, if the other side is a HttpServlet, then its
			// doGet() method will be called and the parameters will be
			// available by HttpServletRequest#getParameter().

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public void httpPost(String query) {
		URLConnection connection;

		try {
			connection = new URL(url).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			InputStream response = connection.getInputStream();
			// ...

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * // You can also cast the obtained URLConnection to HttpURLConnection
		 * and // use its HttpURLConnection#setRequestMethod() instead. But if
		 * you're // trying to use the connection for output you still need to
		 * set // URLConnection#setDoOutput() to true.ƒ // HttpURLConnection
		 * httpConnection; // try { // httpConnection = (HttpURLConnection) new
		 * URL(url) // .openConnection(); //
		 * httpConnection.setRequestMethod("POST"); // // ... // // } catch
		 * (MalformedURLException e) { // // TODO Auto-generated catch block //
		 * e.printStackTrace(); // } catch (IOException e) { // // TODO
		 * Auto-generated catch block // e.printStackTrace(); // } //
		 */
	}

	public void gatheringHttpRequestInfo(String query) {
		
		

		URLConnection connection;

		HttpURLConnection httpConnection;

		try {
			connection = new URL(url).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			InputStream response = connection.getInputStream();
			// ...

			httpConnection = (HttpURLConnection) new URL(url).openConnection();
			httpConnection.setRequestMethod("POST");
			// ...

			int status = httpConnection.getResponseCode();

			for (Entry<String, List<String>> header : connection
					.getHeaderFields().entrySet()) {
				System.out.println(header.getKey() + "=" + header.getValue());
			}

			String contentType = connection.getHeaderField("Content-Type");
			String charset = null;

			for (String param : contentType.replace(" ", "").split(";")) {
			    if (param.startsWith("charset=")) {
			        charset = param.split("=", 2)[1];
			        break;
			    }
			}

			if (charset != null) {
			    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset))) {
			        for (String line; (line = reader.readLine()) != null;) {
			            // ... System.out.println(line) ?
			        }
			    }
			}
			else {
			    // It's likely binary content, use InputStream/OutputStream.
			}
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void uploadFile() throws IOException {
		String param = "value";
		File textFile = new File("/path/to/file.txt");
		File binaryFile = new File("/path/to/file.bin");
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		try (
		    OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		) {
		    // Send normal param.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(param).append(CRLF).flush();

		    // Send text file.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
		    writer.append(CRLF).flush();
		    Files.copy(textFile.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

		    // Send binary file.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
		    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
		    writer.append(CRLF).flush();
		    Files.copy(binaryFile.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF).flush();
		}
	}

	public static void main(String[] args) {

		URLFireRequestTest myTest = new URLFireRequestTest();

		String queryString = myTest.initQuery();
		String qureyResult = myTest.httpGet(queryString);

		System.out.println("the query result:" + qureyResult);

		myTest.httpPost(queryString);

	}

}
