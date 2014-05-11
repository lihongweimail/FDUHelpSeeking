package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.sql.Timestamp;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
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
import org.eclipse.ui.swt.IFocusService;
import org.eclipse.ui.views.markers.MarkerItem;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.EditorInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerRelated;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformationList;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;
import cn.edu.fudan.se.helpseeking.bean.SyntacticBlock;
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
		Information info = new Information();
		Action action = new Action();
		IDEOutput ideOutput = new IDEOutput();
		CompileInformation cpi = new CompileInformation();
		EditCode ec=new EditCode();
		RuntimeInformation ri = new RuntimeInformation();
		ExplorerRelated eRelated = new ExplorerRelated();
		EditorInfo editorInfo = new EditorInfo();
		ExplorerInfo explorerInfo = new ExplorerInfo();

		// --------------
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			if (s.getFirstElement() == null) {
				return;
			} else {
				event.setOriginId("Select: " + s.getFirstElement().toString()
						+ " from Part: " + part.getTitle());
				// add
				String selectName = s.getFirstElement().toString().substring(0,
								s.getFirstElement().toString().length() > 20 ? 20
										: s.getFirstElement().toString().length());
				
				event.setActionName("select "+selectName);
				
				selectionContent = s.getFirstElement().toString();
				// System.out.println("part0");

				Object object = s.getFirstElement();
				if (object instanceof IJavaElement) {
					IJavaElement element = (IJavaElement) object;
					ContextUtil.setContext(element);

				} else if (object instanceof MarkerItem) {
					MarkerItem entry = (MarkerItem) object;
					IMarker m = entry.getMarker();
					// add for get problem view item message
					/*
					 * selectionContent =
					 * entry.getAttributeValue(IMarker.MESSAGE, "hongwei");
					 */
					// System.out.println("marker element: "+selectionContent);
					if(m == null)
						return;
					
					ProblemInformation information = new ProblemInformation();

					information.setSeverity(m.getAttribute("severity", 0));
					information.setDescription(m.getAttribute("message", ""));
					
					if ( m.getResource()!=null) {
											ICompilationUnit unit = JavaCore
							.createCompilationUnitFrom((IFile) m.getResource());
					try {
						IJavaElement e = unit.getElementAt(m.getAttribute(
								"charStart", 0));
						if (e != null
								&& e.getElementType() >= IJavaElement.METHOD) {
							e = e.getAncestor(IJavaElement.METHOD);
							if (e != null) {
								IMethod method = (IMethod) e;
								information.setSource(method.getSource());
							}
						}
					} catch (JavaModelException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}

										
					info.setType("CompileInfo");				
					ideOutput.setCompileInformation(ProblemInformationUtil
							.generateCompileInformationbyProblemInformation(information));
					info.setIdeOutput(ideOutput);	
					ec.setClassModel(null);
					ec.setCursor(null);
					SyntacticBlock sb=new SyntacticBlock();
					sb.setType("CompileInfoSelect");
					sb.setExceptionName(selectionContent);
					sb.setCode(selectionContent);
					ec.setSyntacticBlock(sb);
					info.setEditCode(ec);
					info.setDebugCode(null);
					info.setExplorerRelated(null);
					
					action.setTime(new Timestamp(System.currentTimeMillis()));
					action.setActionKind(event.getKind());
					action.setActionName(event.getActionName());
					action.setDescription(event.getOriginId());
					action.setByuser(true);
					info.setAction(action);

					// 需要先写入数据库，才能得到ID
					int actionid1 = DatabaseUtil.addInformationToDatabase(info);

					// add hongwei 20140414 测试 在插件自己的5个视图中不监控数据
					IWorkbenchPart currentIViewPart = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getActivePart();
					if (!ExceptionalPartAndView.checkPartAndView(currentIViewPart)) {
						Cache.getInstance().addInformationToCache(info, actionid1);
					}
				}

			}

			// System.out.println(part.getClass().getName().toString());
			//
			// if
			// (part.getClass().getName().toString().equals("org.eclipse.ui.navigator.resources.ProjectExplorer"))
			// {
			// List<String> objectNList=new ArrayList<String>();
			// objectNList.add(selectionContent);
			// explorerInfo.setSelectObjectNameList(objectNList);
			// eRelated.setExplorerInfo(explorerInfo);
			//
			//
			//
			// info.setType("ExplorerRelated");
			// info.setExplorerRelated(eRelated);
			//
			// action.setTime(new Timestamp(System.currentTimeMillis()));
			// action.setActionKind(event.getKind());
			// action.setActionName(event.getActionName());
			// action.setDescription(event.getOriginId());
			// action.setByuser(true);
			// info.setAction(action);
			//
			//
			//
			// //需要先写入数据库，才能得到ID
			// int actionid1=DatabaseUtil.addInformationToDatabase(info);
			//
			//
			// //add hongwei 20140414 测试 在插件自己的5个视图中不监控数据
			// IWorkbenchPart
			// currentIViewPart=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			// if (!ExceptionalPartAndView.checkPartAndView(currentIViewPart)) {
			//
			// Cache.getInstance().addInformationToCache(info,actionid1);
			// }
			// //add end
			//
			//
			// }

		} else if (selection instanceof ITextSelection) {
			ITextSelection s = (ITextSelection) selection;
			if (s.getLength() == 0) {
				event.setActionName("InsertCursor");
				event.setOriginId("Insert cursor in line " + s.getStartLine()
						+ " of Part " + part.getTitle());

				IEditorPart unitEditor = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				if (unitEditor instanceof CompilationUnitEditor) {
					ITypeRoot typeRoot = JavaUI
							.getEditorInputTypeRoot(unitEditor.getEditorInput());
					ICompilationUnit icu = (ICompilationUnit) typeRoot
							.getAdapter(ICompilationUnit.class);

					info.setType("EditCode");
					info.setEditCode(CodeUtil.createEditCodeBySelection(icu, s));

					ideOutput.setCompileInformation(ProblemInformationUtil
							.SelectProblemInformationByCursor(info
									.getEditCode().getCursor()));
					ideOutput.setRuntimeInformation(ConsoleInformationUtil
							.SelectConsoleInformationByCursor(info
									.getEditCode().getCursor()));
					info.setIdeOutput(ideOutput);

					action.setTime(new Timestamp(System.currentTimeMillis()));
					action.setActionKind(event.getKind());
					action.setActionName(event.getActionName());
					action.setDescription(event.getOriginId());
					action.setByuser(true);
					info.setAction(action);
					
					
					info.setDebugCode(null);
					info.setExplorerRelated(null);
					

					
					

					// 需要先写入数据库，才能得到ID
					int actionid1 = DatabaseUtil.addInformationToDatabase(info);

					// add hongwei 20140414 测试 在插件自己的5个视图中不监控数据
					IWorkbenchPart currentIViewPart = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getActivePart();
					if (!ExceptionalPartAndView
							.checkPartAndView(currentIViewPart)) {
						Cache.getInstance().addInformationToCache(info,
								actionid1);
					}
					// add end

				}
				// System.out.println("光标位置：" + s.getStartLine());
				return;// do things about cursor and code when length == 0
			} else {
				event.setOriginId("Select: " + s.getText() + " from Part: "
						+ part.getTitle());
				event.setActionName("Select "
						+ s.getText().substring(
								0,
								s.getText().length() > 20 ? 20 : s.getText()
										.length()));
				// add
				selectionContent = s.getText();
				// System.out.println("part1");

				// 选择了一部分代码，调整为关注的代码
				info.setType("CompileInfo");
				cpi.setContent(selectionContent);
				cpi.setType(CompileInfoType.WARNING);
				ideOutput.setCompileInformation(cpi);
				info.setIdeOutput(ideOutput);
				ec.setClassModel(null);
				ec.setCursor(null);
				SyntacticBlock sb=new SyntacticBlock();
				sb.setType("CompileInfoSelect");
				sb.setExceptionName(selectionContent);
				sb.setCode(selectionContent);
				ec.setSyntacticBlock(sb);
				info.setEditCode(ec);
				info.setDebugCode(null);
				info.setExplorerRelated(null);
				

				action.setTime(new Timestamp(System.currentTimeMillis()));
				action.setActionKind(event.getKind());
				action.setActionName(event.getActionName());
				action.setDescription(event.getOriginId());
				action.setByuser(true);
				info.setAction(action);

				// 需要先写入数据库，才能得到ID
				int actionid1 = DatabaseUtil.addInformationToDatabase(info);

				// add hongwei 20140414 测试 在插件自己的5个视图中不监控数据
				IWorkbenchPart currentIViewPart = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActivePart();
				if (!ExceptionalPartAndView.checkPartAndView(currentIViewPart)) {

					Cache.getInstance().addInformationToCache(info, actionid1);
				}
				// add end

			}

		} else if (selection instanceof IMarkSelection) {
			IMarkSelection s = (IMarkSelection) selection;
			event.setOriginId("Select: " + s.getDocument().get()
					+ " from Part: " + part.getTitle());
			event.setActionName("Select "
					+ s.getDocument()
							.get()
							.substring(
									0,
									s.getDocument().get().length() > 20 ? 20
											: s.getDocument().get().length()));
			// add
			selectionContent = s.getDocument().get();
			// System.out.println("part2");

		}

		// System.out.println("测试problem或console视图选择："+part.getClass().toString());
		// 可以加工
		if (part.getClass().getName().toString()
				.equals("org.eclipse.ui.internal.console.ConsoleView")
				|| part.getClass()
						.getName()
						.toString()
						.equals("org.eclipse.ui.internal.views.markers.ProblemsView")) {

			if (part.getClass().getName().toString()
					.equals("org.eclipse.ui.internal.console.ConsoleView")) {
				info.setType("RuntimeInfo");

				ri.setContent(selectionContent);
				ri.setType(RuntimeInfoType.ExceptionalMessage);

				ideOutput.setRuntimeInformation(ri);
				
				info.setIdeOutput(ideOutput);
				ec.setClassModel(null);
				ec.setCursor(null);
				SyntacticBlock sb=new SyntacticBlock();
				sb.setType("CompileInfoSelect");
				sb.setExceptionName(selectionContent);
				sb.setCode(selectionContent);
				ec.setSyntacticBlock(sb);
				info.setEditCode(ec);
				info.setDebugCode(null);
				info.setExplorerRelated(null);
			
				
				event.setKind(Kind.ATTENTION);
				event.setActionName("Console View Changed");
				
				
				
			}

			if (part.getClass()
					.getName()
					.toString()
					.equals("org.eclipse.ui.internal.views.markers.ProblemsView")) {
				
				event.setKind(Kind.ATTENTION);
				info.setType("CompileInfo");
				cpi.setContent(selectionContent);
				cpi.setType(CompileInfoType.ERROR);
				ideOutput.setCompileInformation(cpi);
				info.setIdeOutput(ideOutput);
				event.setActionName("Problem View Changed");
				
				ec.setClassModel(null);
				ec.setCursor(null);
				SyntacticBlock sb=new SyntacticBlock();
				sb.setType("CompileInfoSelect");
				sb.setExceptionName(selectionContent);
				sb.setCode(selectionContent);
				ec.setSyntacticBlock(sb);
				info.setEditCode(ec);
				info.setDebugCode(null);
				info.setExplorerRelated(null);
				
				
			}

			action.setTime(new Timestamp(System.currentTimeMillis()));
			action.setActionKind(event.getKind());
			action.setActionName(event.getActionName());
			action.setDescription(event.getOriginId());
			action.setByuser(event.isByuser());
			info.setAction(action);

			// 需要先写入数据库，才能得到ID
			int actionid1 = DatabaseUtil.addInformationToDatabase(info);

			// add hongwei 20140414 测试 在插件自己的5个视图中不监控数据
			// IWorkbenchPart
			// currentIViewPart=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			if (!ExceptionalPartAndView.checkPartAndView(part)) {
				Cache.getInstance().addInformationToCache(info, actionid1);
			}

		}

		// System.out.println("selection action in this Part: " +
		// part.getTitle());
		// System.out.println("selectionContent: " + selectionContent);

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
