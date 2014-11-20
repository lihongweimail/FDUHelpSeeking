package cn.edu.fudan.se.helpseeking.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.KeyWord;
import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class HelpSeekingDOIModelView extends ViewPart {

	public HelpSeekingDOIModelView() {
	}

	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingDOIModelView"; //$NON-NLS-1$
    public static final String interactiveviewID="cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView";
	



	private static IViewPart interactiveIViewPart= FDUHelpSeekingPlugin
			.getDefault()
			.getWorkbench()
			.getActiveWorkbenchWindow()
			.getActivePage()
			.findView(
					"cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");
	HelpSeekingInteractiveView bv;
	


	private Browser foamtreeBrowser;

	String foamTreeFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/foamtreetest.html";// "http://localhost:8090/foamtreetest.html";//CommUtil.getPluginCurrentPath()+"/foamtreetest.html";
	String foamTreeTopicFilterFileNamePath = CommUtil.getFDUHelpseekingPluginWorkingPath()
			+ "/topicfilter.html";
	String searchHTMLPath = CommUtil.getFDUHelpseekingPluginWorkingPath() + "/search.html";

	@Override
	public void createPartControl(Composite arg0) {

		arg0.setLayout(new FillLayout());
		
		if ((interactiveIViewPart instanceof HelpSeekingInteractiveView)) {
			 bv = (HelpSeekingInteractiveView) interactiveIViewPart;
		}
			
		bv.initFoamTreeEnv(CommUtil.getFDUHelpseekingPluginWorkingPath());

		foamtreeBrowser = new Browser(arg0, SWT.BORDER);
		foamtreeBrowser
				.setToolTipText("Double Click to ROLL OUT!  Shift + Double Click to PULL BACK!");

		String foamTreeContent = ""; // 使用工具生成foamtree的内容
		bv.genFoamTree(300, 200, foamTreeFileNamePath, foamTreeContent,
				"HelloHongwei");

		foamtreeBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				interactiveIViewPart = FDUHelpSeekingPlugin
						.getDefault()
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.findView(
								"cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");
				try {
					FDUHelpSeekingPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("cn.edu.fudan.se.helpseeking.views.HelpSeekingInteractiveView");
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if ((interactiveIViewPart instanceof HelpSeekingInteractiveView)) {
					HelpSeekingInteractiveView bv = (HelpSeekingInteractiveView) interactiveIViewPart;
					
					bv.doFoamtreeClick(e.title.trim(),foamtreeBrowser.getBounds().width,foamtreeBrowser.getBounds().height);
					
				}

				
			}
		});

		foamtreeBrowser.setUrl(foamTreeFileNamePath);
		// browser.refresh();

	}

public void setNewWordsAndMode(List<KeyWord> snapShotAllKeyWords, List<KeyWord> keyWordsforQuery, int mode) {

	bv.setNewWordsAndMode(snapShotAllKeyWords, keyWordsforQuery, mode);
	
}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
