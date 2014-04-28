package cn.edu.fudan.se.helpseeking.views;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.bean.Cache;
import cn.edu.fudan.se.helpseeking.bean.TaskDescription;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.views.Images;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class HelpSeekingCommentsView extends ViewPart {
	public static final String ID = "cn.edu.fudan.se.helpseeking.views.HelpSeekingCommentsView"; //$NON-NLS-1$


	private Label label;
	
	
	private Action addTaskAction;
	private Action saveTaskAction;
	private Action refreshTaskAction;
	private SashForm sashForm;
	private Text txtTaskName;
	private Text txtContent;
	private Text txtTaskID;
	private Tree tree;
	
	private String taskNameMemo="";
	private String taskContentMemo="";
	private String taskIDMemo="";

	private List<TaskDescription> myTaskList=new ArrayList<TaskDescription>(); 
	
	/**
	 * The constructor.
	 */
	public HelpSeekingCommentsView() {
		
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new BorderLayout());
		
		Composite north=new Composite(parent, SWT.NONE);
		north.setLayoutData(BorderLayout.NORTH);
		north.setLayout(new GridLayout(4,false));
		
		
		label = new Label(north, SWT.NONE);
		label.setText("Task ID: ");
		
		txtTaskID=new Text(north, SWT.NONE);
		txtTaskID.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		txtTaskID.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
		txtTaskID.setEditable(false);
		
		label = new Label(north, SWT.NONE);
		label.setText("Task Name: ");
		
		txtTaskName=new Text(north, SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL);
		txtTaskName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		txtTaskName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
		txtTaskName.setEditable(false);
		
		
		Composite viewComposite= new Composite(parent, SWT.NONE);
		viewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		viewComposite.setLayoutData(BorderLayout.CENTER);
		viewComposite.setLayout(new GridLayout(1, false));
		
		sashForm=new SashForm(viewComposite, SWT.BORDER);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		
		
	
		
		tree = new Tree(sashForm, SWT.BORDER | SWT.H_SCROLL	| SWT.V_SCROLL);		
		tree.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tree.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				txtContent.setText((String)item.getData());
				String[] iName=item.getText().split("]");
				txtTaskID.setText(iName[0]);
				txtTaskName.setText(iName[1]);
				
				TokenExtractor tokenExtractor=new TokenExtractor();
				
				List<String> hh=CommUtil.removeDuplicateWithOrder(tokenExtractor.getIdentifierOccurenceOfString((String)item.getData()));
							
				Cache.getInstance().setTaskDescription(hh);
				
								
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		txtContent = new Text(sashForm, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtContent.setText("");
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1);

		txtContent.setLayoutData(gd_txtSearch);
		txtContent.setEditable(false);
		
		sashForm.setWeights(new int[] {194, 379});
		makeActions();
		hookContextMenu();
		genTree();
		contributeToActionBars();
	
	}


	
	private void genTree() {
		myTaskList=DatabaseUtil.getTaskDescriptionRecords();
		if (myTaskList!=null && myTaskList.size()>0) {
			
			if (tree!=null && (tree.getItemCount()>0)) {
				tree.removeAll();
			}
			
					
			for (TaskDescription tDescription : myTaskList) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(tDescription.getTaskID()+"]"+tDescription.getTaskName());
			item.setData(tDescription.getContent());
		}
		
		}
	}

	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				HelpSeekingCommentsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setMenu(menu);
		
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addTaskAction);
		manager.add(new Separator());
		manager.add(saveTaskAction);
		manager.add(refreshTaskAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addTaskAction);
		manager.add(saveTaskAction);
		manager.add(refreshTaskAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addTaskAction);
		manager.add(saveTaskAction);
		manager.add(refreshTaskAction);
	}

	private void makeActions() {
		
		
		
		addTaskAction = new Action() {
			public void run() {
				if (!showMessage("add a new Task")) {
					return;
				}
				
				saveTaskAction.setEnabled(true);
				addTaskAction.setEnabled(false);
				taskContentMemo=txtContent.getText();
				txtContent.setText("");
				taskNameMemo=txtTaskName.getText();
				taskIDMemo=txtTaskID.getText();
				txtTaskName.setText("");
				txtTaskID.setEditable(true);
				txtTaskName.setEditable(true);
				txtContent.setEditable(true);
				saveTaskAction.setImageDescriptor(Images.STOP_ACTIVATE);
				addTaskAction.setImageDescriptor(Images.ADD_DEACTIVATE);
			}
		};
		addTaskAction.setText("Add a task");
		addTaskAction.setToolTipText("Add a task");
		addTaskAction.setImageDescriptor(Images.ADD_ACTIVATE);

		
		
		saveTaskAction = new Action() {
			public void run() {
				
				if (!showMessage("Save Task")) {
					return;
				}
				
				saveTaskAction.setEnabled(false);
				addTaskAction.setEnabled(true);

				if (txtTaskName.getText()!=null || !txtTaskName.getText().trim().equals("")) {
					
					if (txtContent.getText()!=null || !txtContent.getText().trim().equals("")) {
						
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(txtTaskID.getText()+"]"+txtTaskName.getText());
						item.setData(txtContent.getText());
						
						TaskDescription td=new TaskDescription();
						td.setTaskName(txtTaskName.getText());
						td.setContent(txtContent.getText());
						txtTaskID.setEditable(false);
						txtTaskName.setEditable(false);
						txtContent.setEditable(false);
						td.setTaskID(txtTaskID.getText().trim());
						DatabaseUtil.addTaskDescriptionToDataBase(td);
						
						addTaskAction.setImageDescriptor(Images.ADD_ACTIVATE);
						
					}
						
						
					}
				}
				
				
				
			
		};
		saveTaskAction.setText("Save Task");
		saveTaskAction.setToolTipText("Save Task");
		saveTaskAction.setImageDescriptor(Images.STOP_DEACTIVATE);
		
		
		
		
		refreshTaskAction = new Action() {
			public void run() {
				;
				if (!showMessage("Refresh All Tasks")) {
					return;
				}

				
				addTaskAction.setEnabled(true);
				saveTaskAction.setEnabled(false);
				
				
				txtContent.setText(taskContentMemo);
				txtTaskName.setText(taskNameMemo);
				txtTaskID.setText(taskIDMemo);
				taskContentMemo="";
				taskNameMemo="";
				taskIDMemo="";
				txtTaskID.setEditable(false);
				txtTaskName.setEditable(false);
				txtContent.setEditable(false);
				genTree();
				
				addTaskAction.setImageDescriptor(Images.ADD_ACTIVATE);
				saveTaskAction.setImageDescriptor(Images.STOP_DEACTIVATE);
				
			}
		};
		refreshTaskAction.setText("Refresh Records");
		refreshTaskAction.setToolTipText("Refresh Records");
		refreshTaskAction.setImageDescriptor(Images.REFRESH);


	}



	private boolean  showMessage(String message) {
		
		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Comments View", message);
	
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		
	}
}
