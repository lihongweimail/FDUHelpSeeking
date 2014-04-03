package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.JavaUI;
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
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.MessageCollector;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.processing.WebProcessing;
import cn.edu.fudan.se.helpseeking.util.CodeUtil;
import cn.edu.fudan.se.helpseeking.util.ContextUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

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
				IEditorPart unitEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActiveEditor();
				if(unitEditor instanceof CompilationUnitEditor){
					ITypeRoot typeRoot = JavaUI
							.getEditorInputTypeRoot(unitEditor.getEditorInput());
					ICompilationUnit icu = (ICompilationUnit) typeRoot
							.getAdapter(ICompilationUnit.class);
					/*try {
						System.out.println("文件位置："	+ icu.getPackageDeclarations()[0].getElementName()
								+ "." + icu.getElementName());
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					Information info = new Information();
					info.setType("EditCode");
					info.setEditCode(CodeUtil.createEditCodeBySelection(icu, s));
					Action action = new Action();
					action.setTime(new Timestamp(System.currentTimeMillis()));
					action.setActionKind(event.getKind());
					action.setActionName("Select");
					action.setDescription("");
					action.setByuser(true);
					info.setAction(action);
					DatabaseUtil.addInformationToDatabase(info);
				}
				//System.out.println("光标位置：" + s.getStartLine());
				return;// do things about cursor and code when length == 0
			}
			event.setOriginId("Select: " + s.getText() + " from Part: "
					+ part.getTitle());
			// add
			selectionContent = s.getText();
			System.out.println("part1");

		} else if (selection instanceof IMarkSelection) {
			IMarkSelection s = (IMarkSelection) selection;
			event.setOriginId("Select: " + s.getDocument().get()
					+ " from Part: " + part.getTitle());
			// add
			selectionContent = s.getDocument().get();
			System.out.println("part2");

		}

		System.out.println("selection action in this Part: " + part.getTitle());
		System.out.println("selectionContent: " + selectionContent);

// TODO: 给信息收集单例实例赋值
		MessageCollector mcCollector = MessageCollector.getInstance();
		// mcCollector.SetValues(message, methodQualifiedName, currentLineCode,
		// methodCode, messageType);
		mcCollector.setMessage(selectionContent);
		
		// 通知数据收集好，可以加工
		if (part.getTitle().startsWith("Console") || part.getTitle().startsWith("Problem")) {
			if (part.getTitle().startsWith("Console")) {
				mcCollector.setMessageType("exception error from console view");
			}
			
			if (part.getTitle().startsWith("Problem")) {
				mcCollector.setMessageType("compile information from problems view");
			}
			
			// 开启一个线程 处理
			
			System.out
					.println("start search web and running other process  ... ");
			WebProcessing wp = new WebProcessing();
			wp.start();
		}
//TODO: END 
		
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
