package cn.edu.fudan.se.helpseeking.googleCSEAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class GCSServlet extends HttpServlet {
	private static final long serialVersionUID = -1275024561023110262L;
	private static final int RESULT_OK = 200;
	private static final int BUF_SIZE = 1024 * 8; // 8k
	private static final String API_KEY = "AIzaSyBKmmLBxKXaZpIdDcdyIZDJA6_yrN-ejHI"; // API
																						// key
	private static final String UNIQUE_ID = "012004925040627705852:rsqwwhrqtr0"; // Unique
																					// ID

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle(request, response);
	}

	/**
	 * 
	 * 这里提供了如何使用请求的例子 https://developers.google.com/custom-search/v1/using_rest
	 * 注意以下几个参数： 标准参数： alt，可选参数json， atom，用于指定返回值格式，默认值是json
	 * callback，JavaScript回调函数 prettyPrint，返回内容是否采用便于人类阅读的格式，默认值是true API自定义参数：
	 * cx，CSE的唯一标识符 num，搜索结果个数，可选值1~10，默认为10 q，搜索表达式
	 * safe，可选值high(开启搜索结果最高安全级别过滤)、medium(开启中等级别安全过滤)、off(关闭安全过滤)
	 * searchType，可选值image，如果未设置，返回值将被限定为网页
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void handle(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int start = Integer.parseInt(request.getParameter("start"));
		int num = Integer.parseInt(request.getParameter("num"));
		String queryExpression = request.getParameter("flower");
		String strRequest = "https://www.googleapis.com/customsearch/v1?key=%API_KEY%&cx=%UNIQUE_ID%&q=%queryExpression%&start=%start%&num=%num%";
		strRequest = strRequest.replace("%API_KEY%", API_KEY)
				.replace("%UNIQUE_ID%", UNIQUE_ID)
				.replace("%queryExpression%", queryExpression)
				.replace("%start%", String.valueOf(start))
				.replace("%num%", Integer.toString(num));

		HttpURLConnection conn = null;
		String queryResult = null;
		try {
			URL url = new URL(strRequest);
			conn = (HttpURLConnection) url.openConnection();
			// 使用GET方法
			conn.setRequestMethod("GET");
			int resultCode = conn.getResponseCode();
			if (resultCode == RESULT_OK) {
				InputStream is = conn.getInputStream();
				queryResult = readAsString(is);
				is.close();
			} else {
				System.out.println("Fault on getting http result, code: " + resultCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}

		if ((queryResult != null) && (queryResult.length() != 0)) {
			String respStr = parseResult(queryResult);
			System.out.println(respStr);

			// 设置返回内容
			response.setContentType("application/json; charset=utf-8");
			// 设置不缓存内容
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			PrintWriter writer = response.getWriter();
			writer.write(respStr);
			writer.flush();
			writer.close();
		}
	}

	/**
	 * 将搜索结果字符串转化为需要的对象列表
	 * 
	 * @param queryResult
	 * @return
	 */
	private String parseResult(String queryResult) {
		Gson gson = new Gson();
		SearchResult e = gson.fromJson(queryResult, SearchResult.class);
		System.out.println(queryResult);
		SearchResultItem items[] = e.getItems();
		return gson.toJson(e);
	}

	/**
	 * 将输出内容转换为字符串形式
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(InputStream ins) throws IOException {
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUF_SIZE];
		int len = -1;
		try {
			while ((len = ins.read(buffer)) != -1) {
				outs.write(buffer, 0, len);
			}
		} finally {
			outs.flush();
			outs.close();
		}
		return outs.toString();
	}

}
