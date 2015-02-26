package cn.edu.fudan.se.helpseeking.googleCSEAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GCSTest {
	
	private static final int RESULT_OK = 200;
	private static final int BUF_SIZE = 1024 * 8; // 8k

	public static void main(String[] args) {
		// API key
		String strKey = "AIzaSyAoB0pBvZ6veuzDQbR21auME8HJUwmCaos";
		// Unique ID
		String strID = "005635559766885752621:ly8_ifzrwps"; 
		int strStart = 11;
		int num = 1;
		String strQ = "exception";
		/**
		 *  这里提供了如何使用请求的例子
		 *  https://developers.google.com/custom-search/v1/using_rest
		 *  注意以下几个参数：
		 *  标准参数：
		 *  alt，可选参数json， atom，用于指定返回值格式，默认值是json
		 *  callback，JavaScript回调函数
		 *  prettyPrint，返回内容是否采用便于人类阅读的格式，默认值是true
		 *  API自定义参数：
		 *  cx，CSE的唯一标识符
		 *  num，搜索结果个数，可选值1~10，默认为10
		 *  q，搜索表达式
		 *  safe，可选值high(开启搜索结果最高安全级别过滤)、medium(开启中等级别安全过滤)、off(关闭安全过滤)
		 *  searchType，可选值image，如果未设置，返回值将被限定为网页
		 */
		String strRequest = "https://www.googleapis.com/customsearch/v1?key=%key%&cx=%id%&q=%q%&start=%start%&num=%num%";
		strRequest = strRequest.replace("%key%", strKey).replace("%id%", strID).replace("%q%", strQ).replace("%start%", String.valueOf(strStart)).replace("%num%", Integer.toBinaryString(num));
		
		HttpURLConnection conn = null;
		String result = "";
		try {
			URL url = new URL(strRequest);
			conn = (HttpURLConnection) url.openConnection();
			// 使用GET方法
			conn.setRequestMethod("GET");
			int resultCode = conn.getResponseCode();
			if (resultCode == RESULT_OK) {
				InputStream is = conn.getInputStream();
				result = readAsString(is);
				is.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		
		System.out.println(result);
	}

	/**
	 * 将输出内容转换为字符串形式
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
