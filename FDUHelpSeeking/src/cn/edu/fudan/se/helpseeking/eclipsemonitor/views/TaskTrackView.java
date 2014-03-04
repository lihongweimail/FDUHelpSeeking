package cn.edu.fudan.se.helpseeking.eclipsemonitor.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class TaskTrackView extends ViewPart {

	public static final String ID = "cn.edu.fudan.se.helpseeking.eclipsemonitor.views.TaskTrackView"; //$NON-NLS-1$

	private Table table;
	private TaskTrackContentProvider cp = new TaskTrackContentProvider();
	private TaskTrackLabelProvider lp = new TaskTrackLabelProvider();
	private Action refreshAction;

	private TableViewer tableViewer;

	private Label lblCurrentTask;

	public TaskTrackView() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			lblCurrentTask = new Label(container, SWT.NONE);
			lblCurrentTask
					.setText("Current Task: Add Debug Events Monitoring Feature");
		}
		{
			tableViewer = new TableViewer(container, SWT.BORDER
					| SWT.FULL_SELECTION);
			table = tableViewer.getTable();
			table.setToolTipText("Behavior Sequence");
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
					1));
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colId = tableViewerColumn.getColumn();
				colId.setWidth(50);
				colId.setText("Id");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colProgrammer = tableViewerColumn.getColumn();
				colProgrammer.setWidth(100);
				colProgrammer.setText("Programmer");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colTime = tableViewerColumn.getColumn();
				colTime.setWidth(150);
				colTime.setText("Time");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colKind = tableViewerColumn.getColumn();
				colKind.setWidth(60);
				colKind.setText("Kind");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colDescription = tableViewerColumn.getColumn();
				colDescription.setWidth(250);
				colDescription.setText("Description");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colLineNumber = tableViewerColumn.getColumn();
				colLineNumber.setWidth(100);
				colLineNumber.setText("Line Number");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colMethod = tableViewerColumn.getColumn();
				colMethod.setWidth(100);
				colMethod.setText("Method");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colClass = tableViewerColumn.getColumn();
				colClass.setWidth(100);
				colClass.setText("Class");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colFile = tableViewerColumn.getColumn();
				colFile.setWidth(100);
				colFile.setText("File");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colPackage = tableViewerColumn.getColumn();
				colPackage.setWidth(100);
				colPackage.setText("Package");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colProject = tableViewerColumn.getColumn();
				colProject.setWidth(100);
				colProject.setText("Project");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colByuser = tableViewerColumn.getColumn();
				colByuser.setWidth(100);
				colByuser.setText("By User");
			}
			{
				TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn colContextHandler = tableViewerColumn.getColumn();
				colContextHandler.setWidth(200);
				colContextHandler.setText("Context Handler");
			}
			tableViewer.setContentProvider(cp);
			tableViewer.setLabelProvider(lp);
			tableViewer.setInput(DatabaseUtil.getRecords(500, "1"));
		}
		makeActions();
		initializeToolBar();
		initializeMenu();
		hookContextMenu();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TaskTrackView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	class TaskTrackContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				lblCurrentTask
						.setText("Current Task: Add Debug Events Monitoring Feature"
								+ " (Total "
								+ ((List) inputElement).size()
								+ " behaviors)");
				return ((List) inputElement).toArray();
			} else {
				return new Object[0];
			}
		}

	}

	class TaskTrackLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof BehaviorItem) {
				BehaviorItem item = (BehaviorItem) element;
				switch (columnIndex) {
				case 0:
					return item.id;
				case 1:
					return item.programmer;
				case 2:
					return item.time != null && item.time.indexOf('.') > -1 ? item.time
							.substring(0, item.time.indexOf('.'))
							: (item.time == null ? "N/A" : item.time);
				case 3:
					return item.kind;
				case 4:
					return item.description;
				case 5:
					return item.lineno == null ? "N/A" : item.lineno;
				case 6:
					return item.method == null ? "N/A" : item.method;
				case 7:
					return item.type == null ? "N/A" : item.type;
				case 8:
					return item.file == null ? "N/A" : item.file;
				case 9:
					return item.pack == null ? "N/A" : item.pack;
				case 10:
					return item.project == null ? "N/A" : item.project;
				case 11:
					return item.isbyuser.equals("1") ? "Yes" : "No";
				case 12:
					return item.handler;
				}
			}
			return null;
		}
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(refreshAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refreshAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
	}

	/**
	 * Create the actions.
	 */
	private void makeActions() {
		refreshAction = new Action() {
			public void run() {
				List<BehaviorItem> items = DatabaseUtil.getRecords(500, "1");
				tableViewer.setInput(items);
				lblCurrentTask
						.setText("Current Task: Add Debug Events Monitoring Feature"
								+ " (Total " + items.size() + " behaviors)");
				lblCurrentTask.update();
				System.out.println(items.size());
				tableViewer.refresh();
			}
		};
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refresh the table");
		refreshAction.setImageDescriptor(Images.REFRESH);

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
