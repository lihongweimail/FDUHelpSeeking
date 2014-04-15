package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class EditorInfo {
	
//	一定时间内在编辑器的tab页上的选择和切换
	
	int size=0;
	List <String> classQualifiedNameList=new ArrayList<String>();
	public int getSize() {
		return size;
	}
	public void addSize()
	{
		this.size=this.size+1;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<String> getClassQualifiedNameList() {
		return classQualifiedNameList;
	}
	public void setClassQualifiedNameList(List<String> classQualifiedNameList) {
		this.classQualifiedNameList = classQualifiedNameList;
	}
public String ClassQulifiedNameListToString()
{
	String result=" ";
	for (String str : this.classQualifiedNameList) {
		result=result.trim()+str+";";
	}
	
	return result;
}
	
	
}
