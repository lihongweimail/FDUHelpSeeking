package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;



public class Cache2 {

   private Cache2()
	{
		super();
	}
	
public static Cache2 getInstance() {
	return Singleton.cache1;
}

private static class Singleton {
	 static Cache2 cache1 = new Cache2();
}

ActionQueue actions = new ActionQueue();// 包含滑动窗口
ConsoleInformationList consoles = ConsoleInformationList.getInstance();
private int consolesSize=0;

ClassModel currentClassModel = new ClassModel();
int currentID = 0;
List<KeyWord> currentKeywordsList = new ArrayList<KeyWord>();
List<InformationQueue> informations = new ArrayList<InformationQueue>();

ProblemInformationList problems = ProblemInformationList.getInstance();

private int problemsSize=0;

public void addInformationToCache2(Information information,int actionID) {
	
}


}
