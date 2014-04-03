package cn.edu.fudan.se.helpseeking.bean;

import java.sql.Timestamp;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;




public class Action {
	//通过cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent 复制
  //	获得所有相关的信息：
	
	Timestamp time;                         //time
	Timestamp endtime;                   //endtime
	Kind actionKind;                //kind
	String actionName = null;   //originId 的第一部分（有冒号时，前面部分）
//	'Window Deactivated: org.eclipse.ui.internal.WorkbenchWindow@7e7f9d6b'
//	'Part Deactivated: Project Explorer'
//	'Requesting Java AST from selection'
	String description;  //  originId
	boolean byuser = false;
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Timestamp getEndtime() {
		return endtime;
	}
	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isByuser() {
		return byuser;
	}
	public void setByuser(boolean byuser) {
		this.byuser = byuser;
	}

	
	

}
