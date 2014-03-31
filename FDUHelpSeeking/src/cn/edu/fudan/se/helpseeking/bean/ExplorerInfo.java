package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class ExplorerInfo {
	
//	在一定时间内在不同package explorer， outline，等上面选择的对象名
	
	int size;
	List <String> selectObjectNameList;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<String> getSelectObjectNameList() {
		return selectObjectNameList;
	}
	public void setSelectObjectNameList(List<String> selectObjectNameList) {
		this.selectObjectNameList = selectObjectNameList;
	}

	
	
}
