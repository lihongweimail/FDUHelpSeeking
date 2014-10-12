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

	
	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		
		AmAssitBrowser myBrower = new AmAssitBrowser();
		
		
		myBrower.setMyComposite(arg0);
		myBrower.setDisplay(arg0.getDisplay());
		myBrower.createShow();
		myBrower.refreshBrowser();
		myBrower.setDisableButton(true);
		

//		Resource foamtreeJsResource=new Resource();
//		String foamtreejscontent;
//		 
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.asserts.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.asserts.js", foamtreejscontent);
//
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.js", foamtreejscontent);
//
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.hints.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.util.hints.js", foamtreejscontent);
//
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.loading.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.util.loading.js", foamtreejscontent);
//
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.relaxationprogress.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.util.relaxationprogress.js", foamtreejscontent);
//
//		foamtreejscontent=foamtreeJsResource.getResource("/foamtree/carrotsearch.foamtree.util.treemodel.js",true);
//		FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/carrotsearch.foamtree.util.treemodel.js", foamtreejscontent);

		  
//		carrotsearch.foamtree.asserts.js
//		carrotsearch.foamtree.js
//		carrotsearch.foamtree.util.hints.js
//		carrotsearch.foamtree.util.loading.js
//		carrotsearch.foamtree.util.relaxationprogress.js
//		carrotsearch.foamtree.util.treemodel.js
		

		
		  
//		  Resource foamtreehtml=new Resource();
//		  String foamtreehtmlcontent=foamtreehtml.getResource("/foamtree/foamtreetest.html",true);
//		  File htmlFile=new File(CommUtil.getPluginCurrentPath()+"/foamtreetest.html");
//		  FileHelper.writeNewFile(CommUtil.getPluginCurrentPath()+"/foamtreetest.html", foamtreehtmlcontent);
//		  String userpath=htmlFile.getAbsolutePath();
		  
		  String userpath=CommUtil.getFDUPluginCurrentPath()+"foamtree/foamtreetest.html";
		
		
		myBrower.setNewUrl(userpath);
		myBrower.setDisableButton(false);
		myBrower.setRefreshOnly(true);
		myBrower.getMyComposite().pack();


	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
