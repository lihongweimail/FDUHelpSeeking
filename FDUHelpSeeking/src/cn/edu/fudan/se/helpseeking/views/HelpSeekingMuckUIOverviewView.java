package cn.edu.fudan.se.helpseeking.views;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class HelpSeekingMuckUIOverviewView  extends ViewPart{
	
	
	private Browser foamtreeBrowser;

	String foamTreeFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	String foamTreeTopicFilterFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/topicfilter.html";
	String searchHTMLPath = CommUtil.getFDUHelpseekingPluginWorkingPath() + "/search.html";

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
