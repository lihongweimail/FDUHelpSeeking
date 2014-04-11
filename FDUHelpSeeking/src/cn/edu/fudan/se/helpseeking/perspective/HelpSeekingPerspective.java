package cn.edu.fudan.se.helpseeking.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class HelpSeekingPerspective implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		String editorArea = layout.getEditorArea();
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.15f, editorArea);
		layout.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView", IPageLayout.RIGHT, 0.7f, editorArea);
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.6f, editorArea);
		bottom.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
		bottom.addView("cn.edu.fudan.se.helpseeking.eclipsemonitor.views.TaskTrackView");
		bottom.addView("cn.edu.fudan.se.helpseeking.eclipsemonitor.views.RecommendationView");
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(IPageLayout.ID_TASK_LIST);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		
		//bottom.addView(IPageLayout.);
		//layout.addView("HelpSeekingSolutionView", IPageLayout.BOTTOM, 0.6f, editorArea);
		layout.addView("cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView", IPageLayout.BOTTOM, 0.5f, "HelpSeekingSearchView");
		
		
		}

}
