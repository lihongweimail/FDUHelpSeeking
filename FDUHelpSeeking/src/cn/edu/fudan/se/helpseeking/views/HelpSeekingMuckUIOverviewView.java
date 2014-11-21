package cn.edu.fudan.se.helpseeking.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;

public class HelpSeekingMuckUIOverviewView  extends ViewPart{
	
	
	private Browser overviewBrowser;

	String webPageOverviewFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/webPageOverview.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	
	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		
		initFoamTreeEnv(webPageOverviewFileNamePath);
		overviewBrowser=new Browser(arg0, SWT.NONE);
		String foamTreeContent = ""; // 使用工具生成foamtree的内容
		genFoamTree(300, 200, webPageOverviewFileNamePath, foamTreeContent,
				"HelloHongwei");
		
		overviewBrowser.setUrl(webPageOverviewFileNamePath);
		
	}
	
	public void initFoamTreeEnv(String foamtreeFilesPath) {
		Resource foamtreeJsResource = new Resource();
		String foamtreejscontent;

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.asserts.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.asserts.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.js", true);
		FileHelper.writeNewFile(
				foamtreeFilesPath + "/carrotsearch.foamtree.js",
				foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.hints.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.hints.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.loading.js", true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.loading.js", foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.relaxationprogress.js",
				true);
		FileHelper.writeNewFile(foamtreeFilesPath
				+ "/carrotsearch.foamtree.util.relaxationprogress.js",
				foamtreejscontent);

		foamtreejscontent = foamtreeJsResource.getResource(
				"/foamtree/carrotsearch.foamtree.util.treemodel.js", true);
		FileHelper
				.writeNewFile(foamtreeFilesPath
						+ "/carrotsearch.foamtree.util.treemodel.js",
						foamtreejscontent);
	}

	
	
	public void genFoamTree(int width, int height, String foamtreeFileNamePath,
			String foamTreeContent, String title) {

			
		
		if (foamTreeContent.equals("")) {
			foamTreeContent = "dataObject: {"
					+ "groups: ["
					+ "{ label: \"Welcome\", weight: 2.0 ,type: \"node\" },"
					+ "{ label: \"HelpSeeking\", weight: 4.0 ,type: \"node\" },"
					+ "{ label: \"To\", weight: 0.5 ,type: \"node\"},"
					+ "{ label: \"Plugin\", weight: 3.0 ,type: \"node\"},"
					+ "{ label: \"tool\", weight: 1.0 ,type: \"node\"},"
					+ "{ label: \"Double Click Rollout\", weight: 4.0 ,type: \"node\"},"
					+ "{ label: \"Shift + Double Click Pullback\", weight: 4.0 ,type: \"node\"}"
					+ "]" + "}";

			title = "HelloHongwei";
		}

		// File htmlFile = new File(foamtreeFileNamePath); //
		// CommUtil.getPluginCurrentPath()+"/foamtreetest.html"
		String foamtreehtmlcontent = "<!DOCTYPE html> \n" + "<html>\n"
				+ "<head>\n" + "<title>\n"
				+ title
				+ "</title>\n"
				+ "<meta charset=\"utf-8\" />\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<div id=\"visualization\" style=\"width: "
				+ String.valueOf(width < 100 ? 300 : width)
				+ "px; height: "
				+ String.valueOf(height < 100 ? 200 : height)
				+ "px\"></div> \n"
				+ "<script src=\"carrotsearch.foamtree.js\"></script>\n"
				+ "<script language=\"javascript\">\n"
				+ " window.addEventListener(\"load\", function() {\n"
				+ "var foamtree = new CarrotSearchFoamTree({\n"
				+ "id: \"visualization\""
				+ "\n,\n"
				+ foamTreeContent
				// + "\n,\n"
				// + "onGroupDoubleClick: function(event) { \n"
				// + "window.document.title=event.group.label;\n"
				// + "}\n"
				+ "\n,\n"
				+ "onGroupClick: function (event) {\n"
				+ "if (event.group.type==\"leaf\") {"
				+ "window.document.title=event.group.id;}\n"
				+ "}\n"
				+ "});\n" + "});\n" + "</script>\n" + "</body>\n" + "</html>\n";

		FileHelper.writeNewFile(foamtreeFileNamePath, foamtreehtmlcontent);
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
