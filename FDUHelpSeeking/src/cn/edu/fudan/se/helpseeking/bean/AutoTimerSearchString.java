package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class AutoTimerSearchString {
	
	String packageClassName;//如果串中没有包名等数据则用方法名替换，方法名处无值
	List<String> methodName=new ArrayList<String>();
	int count=0;
	public String getPackageClassName() {
		return packageClassName;
	}
	public void setPackageClassName(String packageClassName) {
		this.packageClassName = packageClassName;
	}
	public List<String> getMethodName() {
		return methodName;
	}
	public void setMethodName(List<String> methodName) {
		this.methodName = methodName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	

}
