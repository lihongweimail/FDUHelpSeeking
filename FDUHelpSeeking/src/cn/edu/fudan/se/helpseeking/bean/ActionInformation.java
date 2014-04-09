package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;

public class ActionInformation {
	
	Kind actionKind;                //kind
	String actionName = null;   //originId 的第一部分（有冒号时，前面部分）
    int count=0;
    float frequency=0;
    List<Integer>actionIDList=new ArrayList<Integer>();
//    几类信息的字符串 
//    0 对应Debugcode
//    1对应Editcode
//    2对应ideOutput
//    3对应explorerRelated
//    4对应当前类模型CruuentClassModel
    String[] inforString=new String[5];
    
    
    
    public ActionInformation() {
    	
		initialInforString();
	}
    
   public void initialInforString()
    {
	   for (int i = 0; i < inforString.length; i++) {
		 inforString[i]="";
	}
    	 
    }

    
    
    
	public String[] getInforString() {
		return inforString;
	}
	public void setInforString(String[] inforString) {
		this.inforString = inforString;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public Kind getActionKind() {
		return actionKind;
	}
	public void setActionKind(Kind actionKind) {
		this.actionKind = actionKind;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<Integer> getActionIDList() {
		return actionIDList;
	}
	public void setActionIDList(List<Integer> actionIDList) {
		this.actionIDList = actionIDList;
	}
	  

}
