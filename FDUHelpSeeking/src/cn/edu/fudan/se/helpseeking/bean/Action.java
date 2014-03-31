package cn.edu.fudan.se.helpseeking.bean;

import java.util.Date;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;




public class Action {
	//通过cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent 复制
  //	获得所有相关的信息：
	
	Date time;                         //time
	Date endtime;                   //endtime
	Kind actionKind;                //kind
	String actionName = null;   //originId 的第一部分（有冒号时，前面部分）
//	'Window Deactivated: org.eclipse.ui.internal.WorkbenchWindow@7e7f9d6b'
//	'Part Deactivated: Project Explorer'
//	'Requesting Java AST from selection'
	String description;  //  originId
	boolean byuser = false;

	
	

}
