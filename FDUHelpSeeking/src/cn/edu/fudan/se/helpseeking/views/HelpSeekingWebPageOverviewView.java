package cn.edu.fudan.se.helpseeking.views;



import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.internal.browser.*;

import cn.edu.fudan.se.helpseeking.bean.AroundWordsBean;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.JsoupParser;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.util.WordsAroundAPI;

public class HelpSeekingWebPageOverviewView  extends ViewPart{
	
	
	private Browser overviewBrowser;
	static Tree listTree;
	public static String currentFoamtreeWords="";
	
	public static String currentSearchBoxtxt="";
	

	

	public static String getCurrentSearchBoxtxt() {
		return currentSearchBoxtxt;
	}

	public static void setCurrentSearchBoxtxt(String ctSBoxtxt) {
		currentSearchBoxtxt = ctSBoxtxt;
	}

	public static String getCurrentFoamtreeWords() {
		return currentFoamtreeWords;
	}

	public  static void setCurrentFoamtreeWords(String cFWords) {
		currentFoamtreeWords = cFWords;
	}


	String webPageOverviewFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/webPageOverview.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	
	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
		
		
		listTree=new Tree(arg0, SWT.NONE);
		TreeItem treeItem= new TreeItem(listTree, SWT.NONE);
		treeItem.setText("hello");
		
		
//		Button btnSearch = new Button(arg0, SWT.NONE);
//		btnSearch.setText("find");
//		
//		btnSearch.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				
//				TextEditor editor = (TextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//					    .getActivePage().getActiveEditor();
//				
//				
//				if (editor instanceof org.eclipse.ui.browser.IWebBrowser) {
//					org.eclipse.ui.browser.IWebBrowser ib=(org.eclipse.ui.browser.IWebBrowser) editor;
//					
////					editor.getSourceViewer().setSelectedRange(10, 10);
//				}
//				
//				
//						
//				
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	
//		
		
//		initFoamTreeEnv(webPageOverviewFileNamePath);
//		overviewBrowser=new Browser(arg0, SWT.NONE);
//		String foamTreeContent = ""; // 使用工具生成foamtree的内容
//		genFoamTree(300, 200, webPageOverviewFileNamePath, foamTreeContent,
//				"HelloHongwei");
//		
//		overviewBrowser.setUrl(webPageOverviewFileNamePath);
		
	}
	
	public void genlistTree(String webURL) throws IOException
	{
		
		String workString=currentFoamtreeWords;
		if (workString.trim().equals("")) {
			workString=currentSearchBoxtxt;
			if (workString.trim().equals("")) {
				return;
			}
			
		}
		List<String> apilist=CommUtil.stringToList(workString, " ");
		if (apilist.size()<1) {
			return;
		}
		
		
		 getWordsAround(apilist, webURL);
		 
		 listTree.removeAll();
		 
//		 aroundwordslist
		 
		 for (int i = 0; i < aroundwordslist.size(); i++) {
			TreeItem apiItem=new TreeItem(listTree, SWT.NONE);
			apiItem.setText(aroundwordslist.get(i).getKeywords());
			for (int j = 0; j < aroundwordslist.get(i).getArourndwords().size(); j++) {
				TreeItem worditem=new TreeItem(apiItem, SWT.NONE);
				worditem.setText(aroundwordslist.get(i).getArourndwords().get(j));
			}
			
			 apiItem.setExpanded(true);
		}
		
		
	}
	
	public static List<AroundWordsBean> aroundwordslist=new ArrayList<AroundWordsBean>();
	
	public static  void getWordsAround(List<String> apis,String webURL) throws IOException 
	{
		
		JsoupParser parser = new JsoupParser(webURL);
		
		for (int i = aroundwordslist.size(); i >0; i--) {
			aroundwordslist.remove(i-1);
		}
				
		//HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		
		
		
	
		for(String api : apis)
		{
			AroundWordsBean awb=new AroundWordsBean();
			List<WordsAroundAPI> results = new ArrayList<WordsAroundAPI>();
			parser.getAroundWord(api, 4, results);
			//map.put(api, results);
			awb.setKeywords(api);
			for (int i = 0; i < results.size(); i++) {
				
				awb.getArourndwords().add(CommUtil.ListToString(results.get(i).getWords(), ' '));
				
			}
			aroundwordslist.add(awb);
			
		}
		
		

		
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
