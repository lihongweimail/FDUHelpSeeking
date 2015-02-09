package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Scanner;

import org.fnlp.nlp.cn.ner.stringPreHandlingModule;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.fudan.se.helpseeking.util.FileHelper;

public class HTMLfileParserTest {

	/**
	 * Example program to list links from a URL.
	 * 
	 * @throws IOException
	 */

	public static void doGenHighlightHTML(String url, Boolean isUrl,
			String codeString, String highlightwordscolorsCode, String newfilename)
			throws IOException {
		// String url = "http://www.nsftools.com/misc/SearchAndHighlight.htm";
		// url 是一个本地文件路径 则 isUrl=false ； 如果是 网页 isUrl为网络地址
		print("Fetching %s...", url);

		// 测试用没有 script标签 /Users/Grand/Downloads/testforhighlight.html
		// /Users/Grand/Downloads/JavaScript Text Highlighting.html

		Document doc;
		String charset="UTF-8";

		if (!isUrl) {

			// 文件

			// File input = new
			// File("/Users/Grand/Downloads/testforhighlight.html");
			File input = new File(url);
			doc = Jsoup.parse(input, "UTF-8");
			charset="UTF-8";
		} else {

			// 网页
			charset=Jsoup.connect(url).response().charset();

			doc = Jsoup.connect(url).timeout(1000).get();
			
		}

		Elements body = doc.select("body");
		Element head = doc.select("head").first();

		Elements javascript = head.select("script");
		
		codeString=codeString.replace("</script>", highlightwordscolorsCode);
		head.append(codeString);
//		Element javajsElement=head.select("script").last();
//		javajsElement.append(highlightwordscolorsCode);



//		if (javascript.isEmpty()) {
//			print("no script label");
//			print("do append");
//			// String
//			// codeString=FileHelper.getHTMLContent("/Users/Grand/Downloads/testjavascripthight.text");
////			head.appendElement("script");
////
////			Element javajsElement=head.select("script").last();
////			
////			javajsElement.attr("type", "text/javascript");
////			javajsElement.attr("defer", "async");
////			javajsElement.attr("language", "JavaScript");
////			javajsElement.append(codeString);
////			javajsElement.append(highlightwordscolorsCode);
//			
//
//		} else {
//            Element javajsElement=head.select("script").last();		
//			javajsElement.attr("type", "text/javascript");
//			javajsElement.attr("defer", "async");
//			javajsElement.attr("language", "JavaScript");
//			javajsElement.append(codeString);
//			javajsElement.append(highlightwordscolorsCode);
//		}
		
		
		if (!body.hasAttr("onload")) {
			// print("add attribution onload");
			body.attr("onload", "onloadinghighlight();");
			// print("立即输出 body content : %s ", body.attr("onload"));//
			// print("all document: %s ", doc.html());
		} else {
			String onloadString = body.attr("onload");
			if (onloadString.equals("")) // 没有启动脚本
			{
				// 新增
				body.attr("onload", "onloadinghighlight();");

			} else {
				// 添加
				onloadString = onloadString.replace(";",
						",onloadinghighlight();");
				body.attr("onload", onloadString);
				// print("立即输出 body content : %s ", body.attr("onload"));
			}

		}

		String html = doc.html();

		if(charset==null)
		{
			charset="UTF-8";
		}

		Writer writer = new PrintWriter(
				newfilename, charset);
		writer.write(html);
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		// String url =
		// "file:///Users/Grand/Downloads/JavaScript%20Text%20Highlighting.html";
		String url = "http://www.nsftools.com/misc/SearchAndHighlight.htm";
		print("Fetching %s...", url);

		String highlightwordscolorString = " function onloadinghighlight()\n"
				+ "{\n"
				+ " searchPrompt(\"unpredictable\", false, \"pink\", \"yellow\");\n"
				+ "searchPrompt(\"highlight\", true,\"red\",\"yellow\");\n"
				+ "\n " + " }\n"
				+ " </script> ";

		doGenHighlightHTML(
				"/Users/Grand/Downloads/testforhighlight.html",
				false,
				FileHelper
						.getHTMLContent("/Users/Grand/Downloads/testjavascripthight.text"),
						highlightwordscolorString,
						"/Users/Grand/Downloads/testforhighlightdo.html");
     
		// 测试用没有 script标签 /Users/Grand/Downloads/testforhighlight.html
		// /Users/Grand/Downloads/JavaScript Text Highlighting.html
		File input = new File("/Users/Grand/Downloads/testforhighlight.html");
		Document doc = Jsoup.parse(input, "UTF-8");

		// Document doc = Jsoup.parse(url).get();
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		Elements body = doc.select("body");
		Element head = doc.select("head").first();

		Elements javascript = doc.select("script");

		if (javascript.isEmpty()) {
			print("no script label");
			print("do append");

			// head.appendElement("script");
			// javascript=head.select("script");

			head.append(gettxtcodeString());

			// // javascript = doc.select("script");
			// //
			// // javascript.attr("type", "text/javascript");
			// // javascript.attr("defer", "async");
			// // javascript.attr("language", "JavaScript");
			//
			//
			// String content="";
			//
			// StringBuilder buffer = new StringBuilder();
			//
			//
			// try
			// {
			// File file = new
			// File("/Users/Grand/Downloads/testjavascripthight.text");
			// BufferedReader in = new BufferedReader(new FileReader(file));
			//
			// String line = null;
			// String crlf=System.getProperty("line.separator");
			// content=content+crlf+crlf+crlf;
			//
			// while (null != (line = in.readLine()))
			// {
			// buffer.append(line);
			// buffer.append("\r\n");
			// line=line+"\n";
			// content=content+line;
			// }
			//
			// in.close();
			//
			//
			// }
			// catch (IOException e)
			// {
			// e.printStackTrace();
			// }

			// javascript.append(buffer.toString());

			// javascript.html(content);

			// javascript.append(gethtmlcodestring());

			print("script content to string : \n %s",
					javascript.attr("language"));
			print("script content text : \n %s", javascript.text());
			print("all document: \n %s ", doc.html());

		}

		// print("body content : %s ", body.attr("onload"));

		if (!body.hasAttr("onload")) {
			print("add attribution onload");
			body.attr("onload", "onloadinghighlight();");

			print("立即输出 body content : %s ", body.attr("onload"));

			print("all document: %s ", doc.html());
		} else {
			String onloadString = body.attr("onload");
			if (onloadString.equals("")) // 没有启动脚本
			{
				// 新增
				// Jsoup.parse(new URL(""),
				// 0).getElementById("test").appendElement("h1").attr("id",
				// "header").text("Welcome");

				body.attr("onload", "onloadinghighlight();");

			} else {
				// 添加
				onloadString = onloadString.replace(";",
						",onloadinghighlight();");
				body.attr("onload", onloadString);
				print("立即输出 body content : %s ", body.attr("onload"));

			}

		}

		// body.attr("onload","highlightSearchTerms('search'),onloadinghighlight();");
		// Elements onloadElements=doc.select("body").select("onload");
		//
		// print("onload of ", args); onloadElements.html();
		//
		// onloadElements.html("highlightSearchTerms('search'),onloadinghighlight();");
		//

		print("body content : %s ", body.attr("onload"));

		// print("\nMedia: (%d)", media.size());
		// for (Element src : media) {
		// if (src.tagName().equals("img"))
		// print(" * %s: <%s> %sx%s (%s)", src.tagName(),
		// src.attr("abs:src"), src.attr("width"),
		// src.attr("height"), trim(src.attr("alt"), 20));
		// else
		// print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
		// }
		//
		// print("\nImports: (%d)", imports.size());
		// for (Element link : imports) {
		// print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"),
		// link.attr("rel"));
		// }
		//
		// print("\nLinks: (%d)", links.size());
		// for (Element link : links) {
		// print(" * a: <%s>  (%s)", link.attr("abs:href"),
		// trim(link.text(), 35));
		// }

		String html = doc.html();

		// Write it to file by PrintWriter#write() (with the right charset).
		// BufferedWriter htmlWriter = new BufferedWriter(new
		// OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		// System.out.println("\n" + doc.outerHtml());
		// htmlWriter.write(doc.toString());
		// htmlWriter.flush();
		// htmlWriter.close();
		//

		String charset = (Jsoup.connect(url).response().charset() == null ? "UTF-8"
				: Jsoup.connect(url).response().charset());

		// // ...
		Writer writer = new PrintWriter(
				"/Users/Grand/Downloads/testforhighlightdo.html", charset);
		writer.write(html);
		writer.close();

	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

	public static String gettxtcodeString() throws IOException {
		String codeString = "";
		// test1
		// File input = new
		// File("/Users/Grand/Downloads/testjavascripthight.text");
		codeString = FileHelper
				.getHTMLContent("/Users/Grand/Downloads/testjavascripthight.text");

		// //test2
		// File input = new
		// File("/Users/Grand/Downloads/testjavascripthight.text");
		// Document doc = Jsoup.parse(input, "UTF-8");
		//
		// String parsed = doc.html();
		// System.out.println(parsed);

		return codeString;
	}

	public static String gethtmlcodestring() {
		String codeString = "";

		codeString = " var isCwZExtension = true;"
				+ "function doHighlight(bodyText, searchTerm, highlightStartTag, highlightEndTag) "
				+ "{"
				+ " if ((!highlightStartTag) || (!highlightEndTag)) {"
				+ "highlightStartTag = \"<font style='color:blue; background-color:yellow;'>\";"
				+ "highlightEndTag = \"</font>\";"
				+ "} \r\n"
				+ "var newText = \"\";"
				+ "var i = -1;"
				+ "var lcSearchTerm = searchTerm.toLowerCase();"
				+ "var lcBodyText = bodyText.toLowerCase();"
				+ "while (bodyText.length > 0) {"
				+ "i = lcBodyText.indexOf(lcSearchTerm, i+1);"
				+ "if (i < 0) {"
				+ "newText += bodyText;"
				+ "bodyText = \"\";"
				+ "} else { \r\n"

				+ "if (bodyText.lastIndexOf(\">\", i) >= bodyText.lastIndexOf(\"<\", i)) { \r\n"

				+ "if (lcBodyText.lastIndexOf(\"/script>\", i) >= lcBodyText.lastIndexOf(\"<script\", i)) {"
				+ "newText += bodyText.substring(0, i) + highlightStartTag + bodyText.substr(i, searchTerm.length) + highlightEndTag;"
				+ "bodyText = bodyText.substr(i + searchTerm.length);"
				+ "lcBodyText = bodyText.toLowerCase();"
				+ "i = -1;"
				+ "}"
				+ "}"
				+ "}"
				+ "}"
				+ "return newText;"
				+ "}  \r\n"

				+ "function highlightSearchTerms(searchText, treatAsPhrase, warnOnFailure, highlightStartTag, highlightEndTag)"
				+ "{ \r\n"

				+ "if (treatAsPhrase) {"
				+ "searchArray = [searchText];"
				+ "} else {"
				+ "searchArray = searchText.split(\" \");"
				+ "}  "
				+ "if (!document.body || typeof(document.body.innerHTML) == \"undefined\") {"
				+ "if (warnOnFailure) {"
				+ "alert(\"Sorry, for some reason the text of this page is unavailable. Searching will not work.\");"
				+ "}"
				+ "return false;"
				+ "}"
				+ " \r\n"
				+ ""
				+ "var bodyText = document.body.innerHTML;"
				+ "for (var i = 0; i < searchArray.length; i++) {"
				+ "bodyText = doHighlight(bodyText, searchArray[i], highlightStartTag, highlightEndTag);"
				+ "}"
				+ "document.body.innerHTML = bodyText;"
				+ "return true;"
				+ "}"
				+ " \r\n"

				+ "function searchPrompt(defaultText, treatAsPhrase, textColor, bgColor)"
				+ "{ \r\n"

				+ "if (!defaultText) {"
				+ "defaultText = \"\";"
				+ "}"
				+ " \r\n"

				+ "if ((!textColor) || (!bgColor)) {"
				+ "highlightStartTag = \"\";"
				+ "highlightEndTag = \"\";"
				+ "} else {"
				+ "highlightStartTag = \"<font style='color:\" + textColor + \"; background-color:\" + bgColor + \";'>\";"
				+ "highlightEndTag = \"</font>\";"
				+ "}"
				+ ""
				+ "if (treatAsPhrase) {"
				+ "promptText = \"Please enter the phrase you'd like to search for:\";"
				+ "} else {"
				+ "promptText = \"Please enter the words you'd like to search for, separated by spaces:\";"
				+ "}"
				+ " \r\n"

				+ "searchText =  defaultText ;"
				+ ""
				+ "if (!searchText)  {"
				+ "alert(\"No search terms were entered. Exiting function.\");"
				+ "return false;"
				+ "}"
				+ ""
				+ "return highlightSearchTerms(searchText, treatAsPhrase, true, highlightStartTag, highlightEndTag);"
				+ "}"
				+ " \r\n"

				+ "function highlightGoogleSearchTerms(referrer)"
				+ "{ \r\n"

				+ "if (!referrer) {"
				+ "return false;"
				+ "}"
				+ " "
				+ "var queryPrefix = \"q=\";"
				+ "var startPos = referrer.toLowerCase().indexOf(queryPrefix);"
				+ "if ((startPos < 0) || (startPos + queryPrefix.length == referrer.length)) {"
				+ "return false;"
				+ "}"
				+ ""
				+ "var endPos = referrer.indexOf(\"&\", startPos);"
				+ "if (endPos < 0) {"
				+ "endPos = referrer.length;"
				+ "}"
				+ ""
				+ "var queryString = referrer.substring(startPos + queryPrefix.length, endPos); \r\n"

				+ "queryString = queryString.replace(/%20/gi, \" \"); \r\n"
				+ "queryString = queryString.replace(/\\+/gi, \" \");  \r\n"
				+ "queryString = queryString.replace(/%22/gi, \"\"); \r\n"
				+ "queryString = queryString.replace(/\\\"/gi, \"\"); \r\n"
				+ ""
				+ "return highlightSearchTerms(queryString, false);"
				+ "}"
				+ " \r\n"

				+ "function onloadinghighlight()"
				+ "{"
				+ "searchPrompt(\"unpredictable\", false, \"pink\", \"yellow\");"
				+ "searchPrompt(\"highlight\", true,\"red\",\"yellow\");" + "}";
		return codeString;

	}

}

// 参考内容
// Several ways. You can use Element#append() to append some piece of HTML to
// the element.
//
// Document document = Jsoup.connect(url).get();
// Element head = document.head();
// head.append("<link rel=\"stylesheet\" href=\"http://example.com/your.css\">");
// Or, use Element#attr(name, value) to add attributes to existing elements.
// Here's an example which adds style="color:pink;" to all links.
//
// Document document = Jsoup.connect(url).get();
// Elements links = document.select("a");
// links.attr("style", "color:pink;");
// Either way, after modification get the final HTML string by Document#html().
//
// String html = document.html();
// Write it to file by PrintWriter#write() (with the right charset).
//
// String charset = Jsoup.connect(url).response().charset();
// // ...
// Writer writer = new PrintWriter("/file.html", charset);
// writer.write(html);
// writer.close();
// Finally open it in the webview. Since I can't tell it from top of head,
// here's just a link with an example which I think is helpful:
// WebViewDemo.java. I found the link on this blog by the way (which I in turn
// found by Google).
//

