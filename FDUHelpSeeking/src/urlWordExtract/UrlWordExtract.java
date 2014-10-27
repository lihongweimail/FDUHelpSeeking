package urlWordExtract;

import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
/** 
 * 根据提供的URL，获取此URL对应网页的纯文本信息 
 * @param url 提供的URL链接 
 * @return RL对应网页的纯文本信息 
 * @throws ParserException 
 */
public class UrlWordExtract {
	
	
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
    sb.setURL(url);  
    //返回解析后的网页纯文本信息  
    return sb.getStrings();  
}  

/*public static void main(String[] args) throws Exception {
    // test5("http://www.google.com");
	
	System.out.println(getText("http://wordcram.org/"));
  }*/
}