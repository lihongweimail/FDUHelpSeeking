package cn.edu.fudan.se.helpseeking.test;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

/**
* 标题:利用htmlparser提取网页纯文本的例子 
* @author 
* 
*/
public class GetT {
   
	public static String testHtml(String url) throws Exception {
      
        String sCurrentLine;
        String sTotalString;
        sCurrentLine = "";
        sTotalString = "";
        java.io.InputStream l_urlStream;
        java.net.URL l_url = new java.net.URL(url);
        java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url
              .openConnection();
        l_connection.connect();
        l_connection.setConnectTimeout(2000);
        l_urlStream = l_connection.getInputStream();
        java.io.BufferedReader l_reader = new java.io.BufferedReader(
              new java.io.InputStreamReader(l_urlStream));
        while ((sCurrentLine = l_reader.readLine()) != null) {
           sTotalString += sCurrentLine+"\r\n";
        }
//        System.out.println(sTotalString);

//        System.out.println("====================");
        String testText = extractText(sTotalString);
//        System.out.println(testText);
     
        return testText;

   }

   /**
    * 抽取纯文本信息
    * 
    * @param inputHtml
    * @return
    */
   public static String extractText(String inputHtml) throws Exception {
      StringBuffer text = new StringBuffer();
//      System.out.println(inputHtml);
      Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
           "GBK"), "GBK");
      // 遍历所有的节点
      NodeFilter[] filters = new NodeFilter[3];
      filters[0] = new NodeClassFilter(TableTag.class);
      filters[1] = new NodeClassFilter(ParagraphTag.class);
      filters[2] = new NodeClassFilter(Div.class);
      NodeFilter filter =new OrFilter(filters);
      
      NodeList list = parser.extractAllNodesThatMatch(filter);
//      for(int i =0;i<list.size();i++){
//      	System.out.println(list.elementAt(i).toHtml());
//      }

      System.out.println(list.size());
      for (int i=0;i<list.size();i++){
       Node nodet = list.elementAt(i);
//       System.out.println("NODES["+i+"]:"+new String(nodet.toPlainTextString().getBytes("GBK")));
        text.append(new String(nodet.toPlainTextString().getBytes("GBK"))+"\r\n");       
      }
//    System.out.println(text.toString());
      return text.toString();
   }

   /**
    * 读取文件的方式来分析内容. filePath也可以是一个Url.
    * 
    * @param resource
    *        文件/Url
    */
   public static void test5(String resource) throws Exception {
      Parser myParser = new Parser(resource);

      // 设置编码
      myParser.setEncoding("GBK");
      String filterStr = "table";
      NodeFilter filter = new TagNameFilter(filterStr);
      NodeList nodeList = myParser.extractAllNodesThatMatch(filter);
      TableTag tabletag = (TableTag) nodeList.elementAt(11);
        
        System.out.println(tabletag.toHtml());
        
        System.out.println("==============");

   }

   public static void main(String[] args) throws Exception {
//      test5("http://news.ccidnet.com/index.htm");
      testHtml("http://www.oracle.com/technetwork/articles/java/index-jsp-135444.html");
   }
}