package cn.edu.fudan.se.helpseeking.eclipsemonitor.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

public class RecommendationView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "cn.edu.fudan.se.helpseeking.eclipsemonitor.views.RecommendationView";

	private Label label;
	private TreeViewer viewer;
	private Action addAction;
	private Action startAction;
	private Action stopAction;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ViewContentProvider extends ArrayContentProvider implements
			ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			Element e = (Element) parentElement;
			return e.children;
		}

		@Override
		public Object getParent(Object element) {
			Element e = (Element) element;
			return e.parent;
		}

		@Override
		public boolean hasChildren(Object element) {
			Element e = (Element) element;
			return e.children.length > 0;
		}

	}

	class ViewLabelProvider extends StyledCellLabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int index) {
			Element e = (Element) obj;
			return e.icon.createImage();
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Element e = (Element) element;
			return e.name;
		}

		@Override
		public void update(ViewerCell cell) {
			Element e = (Element) cell.getElement();
			StyledString text = new StyledString();
			cell.setImage(e.icon.createImage());
			text.append(e.name + " ");
			if (e.children.length > 0) {
				text.append("(" + e.children.length + ") ",
						StyledString.COUNTER_STYLER);
			}
			text.append(e.last, StyledString.QUALIFIER_STYLER);
			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public RecommendationView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		label = new Label(parent, SWT.NONE);
		label.setText("Current Task: Add Debug Events Monitoring Feature");
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(Element.example());
		Tree tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RecommendationView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addAction);
		manager.add(new Separator());
		manager.add(startAction);
		manager.add(stopAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addAction);
		manager.add(startAction);
		manager.add(stopAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addAction);
		manager.add(startAction);
		manager.add(stopAction);
	}

	private void makeActions() {
		addAction = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		addAction.setText("Add a task");
		addAction.setToolTipText("Add a task");
		addAction.setImageDescriptor(Images.ADD_ACTIVATE);

		startAction = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		startAction.setText("Start recording");
		startAction.setToolTipText("Start recording");
		startAction.setImageDescriptor(Images.START_DEACTIVATE);
		stopAction = new Action() {
			public void run() {
				showMessage("Action 3 executed");
			}
		};
		stopAction.setText("Stop recording");
		stopAction.setToolTipText("Stop recording");
		stopAction.setImageDescriptor(Images.STOP_ACTIVATE);

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"Recommendation View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}