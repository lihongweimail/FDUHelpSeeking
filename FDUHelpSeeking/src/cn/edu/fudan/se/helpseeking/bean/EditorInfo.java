package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class EditorInfo {
	
//	一定时间内在编辑器的tab页上的选择和切换
	
	int size;
	List <String> classQualifiedNameList;
	public int getSize() {
		return size;
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

	
	
}
