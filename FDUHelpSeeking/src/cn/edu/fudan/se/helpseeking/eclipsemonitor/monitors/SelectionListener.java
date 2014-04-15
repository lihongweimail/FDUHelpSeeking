package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.MarkerItem;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.MessageCollector;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.ConsoleInformationUtil;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.ProblemInformationUtil;

@SuppressWarnings("restriction")
public class SelectionListener extends AbstractUserActivityMonitor implements
ISelectionListener {

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		InteractionEvent event = new InteractionEvent();
		event.setByuser(true);
		event.setKind(Kind.SELECTION);

		// add this variable for extract info. hongwei 2014.3.4
		String selectionContent = "";

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			if (s.getFirstElement() == null) {
				return;
			} else {
				event.setOriginId("Select: " + s.getFirstElement().toString()
						+ " from Part: " + part.getTitle());
				// add

				selectionContent = s.getFirstElement().toString();
				System.out.println("part0");

				Object object = s.getFirstElement();
				if (object instanceof IJavaElement) {
					IJavaElement element = (IJavaElement) object;
					ContextUtil.setContext(element);

				} else if (object instanceof MarkerItem) {
					MarkerItem entry = (MarkerItem) object;
					//add for get problem view item message
					selectionContent =entry.getAttributeValue(IMarker.MESSAGE, "hongwei");
					System.out.println("marker element: "+selectionContent);
				}
			}
		} else if (selection instanceof ITextSelection) {
			ITextSelection s = (ITextSelection) selection;
			if (s.getLength() == 0) {
				event.setActionName("InsertCursor");
				event.setOriginId("Insert cursor in line " + s.getStartLine() 
						+ "of Part " + part.getTitle());


				IEditorPart unitEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActiveEditor();
				if(unitEditor instanceof CompilationUnitEditor){
					ITypeRoot typeRoot = JavaUI
							.getEditorInputTypeRoot(unitEditor.getEditorInput());
					ICompilationUnit icu = (ICompilationUnit) typeRoot
							.getAdapter(ICompilationUnit.class);

					Information info = new Information();
					info.setType("EditCode");
					info.setEditCode(CodeUtil.createEditCodeBySelection(icu, s));
					Action action = new Action();
					action.setTime(new Timestamp(System.currentTimeMillis()));
					action.setActionKind(event.getKind());
					action.setActionName(event.getActionName());
					action.setDescription("");
					action.setByuser(true);
					info.setAction(action);

					DatabaseUtil.addInformationToDatabase(info);
					//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据
					IWorkbenchPart currentIViewPart=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
					if (!ExceptionalPartAndView.checkPartAndView(currentIViewPart)) {

						Cache.getInstance().addInformationToCache(info);
					}
					//add end



				}
				//System.out.println("光标位置：" + s.getStartLine());
				return;// do things about cursor and code when length == 0
			}else{
				event.setOriginId("Select: " + s.getText() + " from Part: "
						+ part.getTitle());
				// add
				selectionContent = s.getText();
				System.out.println("part1");
			}

		} else if (selection instanceof IMarkSelection) {
			IMarkSelection s = (IMarkSelection) selection;
			event.setOriginId("Select: " + s.getDocument().get()
					+ " from Part: " + part.getTitle());
			// add
			selectionContent = s.getDocument().get();
			System.out.println("part2");

		}


		Information info = new Information();

		//			System.out.println("测试problem或console视图选择："+part.getClass().toString());
		// 可以加工
		if (part.getTitle().toString().equals("org.eclipse.ui.internal.console.ConsoleView") || part.getTitle().toString().equals("org.eclipse.ui.internal.views.markers.ProblemsView")) {

			IDEOutput ideo=new IDEOutput();
			CompileInformation cpi=new CompileInformation();
			RuntimeInformation ri=new RuntimeInformation();


			if (part.getTitle().toString().equals("org.eclipse.ui.internal.console.ConsoleView")) {
				info.setType("RuntimeInfo");		

				ri.setContent(selectionContent);
				ri.setType(RuntimeInfoType.ExceptionalMessage);

				ideo.setRuntimeInformation(ri);;
				info.setIdeOutput(ideo);
			}

			if (part.getTitle().toString().equals("org.eclipse.ui.internal.views.markers.ProblemsView")) {
				info.setType("CompileInfo");				
				cpi.setContent(selectionContent);
				cpi.setType(CompileInfoType.ERROR);
				ideo.setCompileInformation(cpi);
				info.setIdeOutput(ideo); 	
			}


			Action action = new Action();
			action.setTime(new Timestamp(System.currentTimeMillis()));
			action.setActionKind(event.getKind());
			action.setActionName(event.getActionName());
			action.setDescription(event.getOriginId());
			action.setByuser(event.isByuser());
			info.setAction(action);



			//add hongwei   20140414 测试  在插件自己的5个视图中不监控数据
			//		IWorkbenchPart currentIViewPart=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			if (!ExceptionalPartAndView.checkPartAndView(part)) {

				Cache.getInstance().addInformationToCache(info);
			}

		}


		System.out.println("selection action in this Part: " + part.getTitle());
		System.out.println("selectionContent: " + selectionContent);


		DatabaseUtil.addInteractionEventToDatabase(event);
	}

	@Override
	public void start() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.addPostSelectionListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.removePostSelectionListener(this);
		setEnabled(false);
	}

}
