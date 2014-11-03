package urlWordExtract;

import java.net.ConnectException;
import java.util.concurrent.Callable;

import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;
/** 
 * 根据提供的URL，获取此URL对应网页的纯文本信息 
 * @param url 提供的URL链接 
 * @return RL对应网页的纯文本信息 
 * @throws ParserException 
 */
public class UrlWordExtract implements Callable<String>{
	
		private String urlstr="";
		
		
		public String getUrlstr() {
			return urlstr;
		}
		public void setUrlstr(String urlstr) {
			this.urlstr = urlstr;
		}
		
		
		public UrlWordExtract(String urlstr){
			setUrlstr(urlstr);
		}
		
		
		
public static String getText(String url) throws ParserException{  

    StringBean sb = new StringBean();  
//    String url = "http://wordcram.org/";  
    //设置不需要得到页面所包含的链接信息  
    sb.setLinks(false);  
    //设置将不间断空格由正规空格所替代  
    sb.setReplaceNonBreakingSpaces(true);  
    //设置将一序列空格由一个单一空格所代替  
    sb.setCollapse(true);  
    //传入要解析的URL 
    System.out.println("input url in urlwordextract :"+ url);
    sb.setURL(url);
   
    
    
    
    //返回解析后的网页纯文本信息  
   // return getPlainText(sb.getStrings());  
    return sb.getStrings();
}  

public static void main(String[] args)  {
    // test5("http://www.google.com");  ConnectException
	
	try {
			System.out.println(getText("http://cs.fudan.edu.cn/"));	

	} 	catch (ParserException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		if (e.getCause()!=null) {
			System.out.println(e.getCause().getMessage());
		}
		
	}
	  }

/**   
 *   利用Visitor模式解析html页面.   
 *   
 *   小优点:翻译了<>等符号     
 *   缺点:好多空格,无法提取link   
 *         
 */   
public   static   void   test3(String   content)   throws   Exception   
{   
       Parser   myParser;   
       myParser   =   Parser.createParser(content,   "ISO-8859-1");   

       TextExtractingVisitor   visitor   =   new   TextExtractingVisitor();   

       myParser.visitAllNodesWith(visitor);   

       String   textInPage   =   visitor.getExtractedText();   

       System.out.println(textInPage);   
}   



/**
 * 获取纯文本信息
 * 
 * @param str
 * @return
 */
public static String getPlainText(String str) {
 try {
  Parser parser = new Parser();
  parser.setInputHTML(str);

  StringBean sb = new StringBean();
  // 设置不需要得到页面所包含的链接信息
  sb.setLinks(false);
  // 设置将不间断空格由正规空格所替代
  sb.setReplaceNonBreakingSpaces(true);
  // 设置将一序列空格由一个单一空格所代替
  sb.setCollapse(true);
  parser.visitAllNodesWith(sb);
  str = sb.getStrings();
 } catch (ParserException e) {

	 e.printStackTrace();

 }

 return str;
}

@Override
public String call() throws Exception {
	// TODO Auto-generated method stub
	 StringBean sb = new StringBean();  
//   String url = "http://wordcram.org/";  
   //设置不需要得到页面所包含的链接信息  
   sb.setLinks(false);  
   //设置将不间断空格由正规空格所替代  
   sb.setReplaceNonBreakingSpaces(true);  
   //设置将一序列空格由一个单一空格所代替  
   sb.setCollapse(true);  
   //传入要解析的URL 
   System.out.println("input url in urlwordextract :"+ getUrlstr());
   sb.setURL(getUrlstr());

   //返回解析后的网页纯文本信息  
  // return getPlainText(sb.getStrings());  
   return sb.getStrings();
}




}