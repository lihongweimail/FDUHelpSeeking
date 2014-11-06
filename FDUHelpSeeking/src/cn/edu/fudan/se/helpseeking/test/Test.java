package cn.edu.fudan.se.helpseeking.test;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.*;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.TextExtractingVisitor;

public class Test {
	
	public static void main(String[] args){
		
		String title = "";//标题
		String body = "";//正文
		
		try{
			Parser parser = new Parser("http://www.chinanews.com/gj/2011/09-22/3347124.shtml");//要解析的网页
			parser.setEncoding("gb2312");//设置编码
			NodeFilter filter_title = new TagNameFilter("title");//title节点过滤
			NodeFilter filter_text = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","left_zw"));//正文节点过滤
			
			NodeList nodelist1 = parser.extractAllNodesThatMatch(filter_title);//过滤得符合过滤要求的节点的LIST
			Node node_title = nodelist1.elementAt(0);//取节点
			StringBuffer buftitle = new StringBuffer();
			if(node_title == null){//判断是否为空
				buftitle.append("");
			}
			else{
				buftitle.append(node_title.toPlainTextString());//把节点里的文本节点转化为String 加到buftitle上
			}
			title = buftitle.toString();//转化为String
			System.out.println(title);//输出

			parser.reset();//重置
			NodeList nodelist2 = parser.extractAllNodesThatMatch(filter_text);//过滤出符合filter_text的节点LIST
			Node[] nodes = nodelist2.toNodeArray();//转化为数组
			StringBuffer buftext = new StringBuffer();
			String line = null;
			for(int i=0; i<nodes.length; i++){//循环加到buftext上
				line = nodes[i].toPlainTextString();
				 if(line != null){
					 buftext.append(line);
				 }
			}
			body = buftext.toString();
			System.out.println(body);//输出
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}