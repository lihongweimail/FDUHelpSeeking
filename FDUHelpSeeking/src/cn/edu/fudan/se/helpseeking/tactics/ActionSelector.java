package cn.edu.fudan.se.helpseeking.tactics;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.AttentionAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.CommandAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.DebugAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.EditAction;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Basic.SelectionAction;

public class ActionSelector {
	
	public boolean checkAction(Action action)
	{
		boolean result=false;
		
		if (action.getActionKind()==Kind.DEBUG) {
			DebugAction a=DebugAction.fromString(action.getActionName());
					result=a.isChooseAction();
			}
		if (action.getActionKind()==Kind.COMMAND) {
			CommandAction a=CommandAction.fromString(action.getActionName());
					result=a.isChooseAction();
			}
		if (action.getActionKind()==Kind.EDIT) {
			EditAction a=EditAction.fromString(action.getActionName());
					result=a.isChooseAction();
			}
		if (action.getActionKind()==Kind.ATTENTION) {
			AttentionAction a=AttentionAction.fromString(action.getActionName());
					result=a.isChooseAction();
			}
		if (action.getActionKind()==Kind.SELECTION) {
			SelectionAction a=SelectionAction.fromString(action.getActionName());
					result=a.isChooseAction();
			}
	return  result;
		
	}

}
