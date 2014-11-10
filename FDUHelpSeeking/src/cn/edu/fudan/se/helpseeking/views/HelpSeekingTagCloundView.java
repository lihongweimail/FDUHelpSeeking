package cn.edu.fudan.se.helpseeking.views;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

public class HelpSeekingTagCloundView extends ViewPart {

	private static  String baseUrl;
	
	public HelpSeekingTagCloundView() {
		super();
		part = FDUHelpSeekingPlugin
				.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingTagCloundView");
	}

	static IViewPart part;
	AmAssitBrowser myBrowser;
	
	
	public AmAssitBrowser getMyBrowser() {
		return myBrowser;
	}

	public void setMyBrowser(AmAssitBrowser myBrowser) {
		this.myBrowser = myBrowser;
	}

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		

		Resource foamtreeJsResource=new Resource();
		String foamtreejscontent;
		 
		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.asserts.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.asserts.js", foamtreejscontent);
        
		System.out.println("tagcloud view getplugincurrentpath() path: "+CommUtil.getFDUHelpseekingPluginWorkingPath());
		
		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.js", foamtreejscontent);

		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.hints.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.util.hints.js", foamtreejscontent);

		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.loading.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.util.loading.js", foamtreejscontent);

		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.relaxationprogress.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.util.relaxationprogress.js", foamtreejscontent);

		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.treemodel.js",true);
		FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/carrotsearch.foamtree.util.treemodel.js", foamtreejscontent);

		  Resource foamtreehtml=new Resource();
		  String foamtreehtmlcontent=foamtreehtml.getResource("/foamtree/foamtreetest.html",true);
		  File htmlFile=new File(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/foamtreetest.html");
		  foamtreehtmlcontent="<!DOCTYPE html>"
		  		+ "<html>"
		  		+ "<head>"
		  		+ "<title>HelloHongwei</title>"
		  		+ "<meta charset=\"utf-8\" />"
		  		+ "</head>"
		  		+ "<body>"
		  		+ "<div id=\"visualization\" style=\"width: 200px; height: 200px\"></div>"
		  		+ "<script src=\"carrotsearch.foamtree.js\"></script>"
		  		+ "<script language=\"javascript\">"
		  		+ " window.addEventListener(\"load\", function() {"
		  		+ "var foamtree = new CarrotSearchFoamTree({"
		  		+ "id: \"visualization\","
		  		+ "dataObject: {"
		  		+ "groups: ["
		  		+ "  { label: \"getResource\", weight: 2.0 },"
		  				+ " { label: \"illegalArgumentException\", weight: 3.0 },"
		  				+ "{ label: \"foamtree\", weight: 2.0 },"
		  				+ "{ label: \"null\", weight: 1.0 }"
		  				+ "]"
		  				+ "}"
		  				+ "});"
		  				+ "});"
		  				+ "document.write(location.pathname);"
		  				+ "</script>"
		  				+ "</body>"
		  				+ "</html>";
		  
		  
		  FileHelper.writeNewFile(CommUtil.getFDUHelpseekingPluginWorkingPath()+"/foamtreetestTag.html", foamtreehtmlcontent);

		 myBrowser = new AmAssitBrowser();
		
		
		myBrowser.setMyComposite(arg0);
		myBrowser.setDisplay(arg0.getDisplay());
		myBrowser.createShow();
		//myBrowser.refreshBrowser();
		myBrowser.setDisableButton(true);
		
		  String userpath=CommUtil.getFDUHelpseekingPluginWorkingPath()+"/foamtreetestTag.html";//"http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
			
			
		myBrowser.setNewUrl(userpath);
		//myBrowser.setDisableButton(false);
		//myBrowser.setRefreshOnly(true);
		myBrowser.getMyComposite().pack();

		
		


		  
//		carrotsearch.foamtree.asserts.js
//		carrotsearch.foamtree.js
//		carrotsearch.foamtree.util.hints.js
//		carrotsearch.foamtree.util.loading.js
//		carrotsearch.foamtree.util.relaxationprogress.js
//		carrotsearch.foamtree.util.treemodel.js
		

		
		  
		 // String userpath=htmlFile.getAbsolutePath();
		  


	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
