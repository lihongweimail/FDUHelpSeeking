package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;

public class ExceptionalPartAndView {
	
	public static boolean checkPartAndView(IWorkbenchPart part)
	{
		
		boolean checkResult=false;
		IWorkbenchPage currentActiveIWorkbenchPage=FDUHelpSeekingPlugin.getDefault()
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage();
		
		IViewPart helpseekingSearchViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
		IViewPart helpSeekingSolutionViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingSolutionView");
		IViewPart helpSeekingCommentsViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView");
		IViewPart taskTrackViewViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.eclipsemonitor.views.TaskTrackView");
		IViewPart recommendationViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.eclipsemonitor.views.RecommendationView");
		IViewPart browserViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.eclipsemonitor.views.HelpSeekingBrowserView");
		IViewPart interactiveViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.eclipsemonitor.views.HelpSeekingInteractiveView");
		IViewPart tagCloundViewPart = currentActiveIWorkbenchPage
				.findView(
						"cn.edu.fudan.se.helpseeking.eclipsemonitor.views.HelpSeekingTagCloundView");

		
		if (part.equals(helpseekingSearchViewPart)) {
			checkResult=true;
		}
		if (part.equals(helpSeekingSolutionViewPart)) {
			checkResult=true;
		}
		if (part.equals(helpSeekingCommentsViewPart)) {
			checkResult=true;
		}
		if (part.equals(taskTrackViewViewPart)) {
			checkResult=true;
		}
		if (part.equals(recommendationViewPart)) {
			checkResult=true;
		}
		if (part.equals(browserViewPart)) {
			checkResult=true;
		}
		if (part.equals(interactiveViewPart)) {
			checkResult=true;
		}
		if (part.equals(tagCloundViewPart)) {
			checkResult=true;
		}
		
	
//		System.out.println("view check result: "+ checkResult);
		return checkResult;
	}

}
