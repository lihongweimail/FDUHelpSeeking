package cn.edu.fudan.se.helpseeking.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class HelpSeekingPerspective implements IPerspectiveFactory{

	public static final String ID = "cn.edu.fudan.se.helpseeking.perspective.HelpSeekingPerspective"; //$NON-NLS-1$
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		String editorArea = layout.getEditorArea();
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.15f, editorArea);
		IFolderLayout right=layout.createFolder("right", IPageLayout.RIGHT, 0.60f, editorArea);
//		layout.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView", IPageLayout.RIGHT, 0.7f, editorArea);
//		layout.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView", IPageLayout.RIGHT, 0.7f, editorArea);
//		layout.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView",  IPageLayout.BOTTOM, 0.7f, "cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
		
		right.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
//		right.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView");
		right.addView(IPageLayout.ID_PROBLEM_VIEW);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.6f, editorArea);
		
		
//		bottom.addView(IPageLayout.ID_PROP_SHEET);
//		bottom.addView(IPageLayout.ID_TASK_LIST);
		
		
		bottom.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
//		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		
//		bottom.addView("cn.edu.fudan.se.helpseeking.eclipsemonitor.views.TaskTrackView");
//		bottom.addView("cn.edu.fudan.se.helpseeking.eclipsemonitor.views.RecommendationView");
		
		
	
		
		layout.addShowViewShortcut("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
		layout.addPerspectiveShortcut(ID);
	
		
		}

}
