package cn.edu.fudan.se.helpseeking.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import cn.edu.fudan.se.helpseeking.util.ConsoleFactory;

public class HelpSeekingPerspective implements IPerspectiveFactory{

	public static final String ID = "cn.edu.fudan.se.helpseeking.perspective.HelpSeekingPerspective"; //$NON-NLS-1$
	
	

	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		String editorArea = layout.getEditorArea();
		
		//setEditorIPageLayout(layout);
		
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.15f, editorArea);
		IFolderLayout leftbottom = layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.60f, IPageLayout.ID_PROJECT_EXPLORER);
		//leftbottom.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingTagCloundView");
		
		IFolderLayout right=layout.createFolder("right", IPageLayout.RIGHT, 0.60f, editorArea);

		right.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");
	
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.6f, editorArea);
		
		
//		bottom.addView(IPageLayout.ID_PROP_SHEET);
//		bottom.addView(IPageLayout.ID_TASK_LIST);

		
		bottom.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");

		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
//		 ConsoleFactory consoleFactory = new ConsoleFactory();  
//		    consoleFactory.openConsole(); 
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);	
	
		
	
		
		layout.addShowViewShortcut("cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");
		layout.addShowViewShortcut("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
		layout.addShowViewShortcut("cn.edu.fudan.se.helpseeking.views.HelpSeekingBrowserView");
		layout.addPerspectiveShortcut(ID);
	
		
		}

}
