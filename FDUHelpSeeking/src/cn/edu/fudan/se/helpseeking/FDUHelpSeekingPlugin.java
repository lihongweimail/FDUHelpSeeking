package cn.edu.fudan.se.helpseeking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cn.edu.fudan.se.helpseeking.bean.BrowserIDBean;
import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.ActivityContextManager;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.ExtensionPointReader;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.IMonitoredWindow;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.StatusHandler;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.AbstractUserActivityMonitor;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.BreakpointListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.CVSConsoleListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.DebugEventSetListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ElementChangedListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ExecutionListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.JobChangeListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.LaunchListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PageListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PartListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PerspectiveListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ProblemChangedListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PropertyChangeListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.RefactoringExecutionListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.RefactoringHistoryListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ResourceChangeListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.SVNConsoleListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.SelectionListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.TestRunListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WindowListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WorkbenchListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WorkbenchUserActivityMonitor;
import cn.edu.fudan.se.helpseeking.preferences.PreferenceConstants;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.INIHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class FDUHelpSeekingPlugin extends AbstractUIPlugin  {


	

	
	
	
	private static FDUHelpSeekingPlugin INSTANCE;
	public static FDUHelpSeekingPlugin getINSTANCE() {
		return INSTANCE;
	}
	private ActivityContextManager activityContextManager;

	// The plug-in ID
	public static final String PLUGIN_ID = "cn.edu.fudan.se.helpseeking.EclipseMonitor"; //$NON-NLS-1$

	// The shared instance
	private static FDUHelpSeekingPlugin plugin;

	private ArrayList<CompilationUnitEditor> editors = new ArrayList<CompilationUnitEditor>();
	private final Set<IWorkbenchWindow> monitoredWindows = new HashSet<IWorkbenchWindow>();
	private final List<AbstractUserActivityMonitor> monitors = new ArrayList<AbstractUserActivityMonitor>();
	protected Set<IPartListener> partListeners = new HashSet<IPartListener>();
	protected Set<IPageListener> pageListeners = new HashSet<IPageListener>();
	protected Set<IPerspectiveListener> perspectiveListeners = new HashSet<IPerspectiveListener>();
	protected Set<ISelectionListener> postSelectionListeners = new HashSet<ISelectionListener>();
	private IWorkbenchWindow launchingWorkbenchWindow = null;
	
	
	private  List<BrowserIDBean> currentBrowserIDs=new ArrayList<BrowserIDBean>();
	

	public  List<BrowserIDBean> getCurrentBrowserIDs() {
		return currentBrowserIDs;
	}

	public  void setCurrentBrowserIDs(List<BrowserIDBean> currentBrowserIDs) {
		FDUHelpSeekingPlugin.getINSTANCE().currentBrowserIDs = currentBrowserIDs;
	}
	
	
	public IPageLayout editorIPageLayout;
	public void setEditorIPageLayout(IPageLayout editorIPageLayout) {
		this.editorIPageLayout = editorIPageLayout;
	}

	public IPageLayout getEditorIPageLayout()
	{
		return this.editorIPageLayout;
	}
	
	
	private IViewPart helpSearchViewPart;

	public IViewPart getHelpSearchViewPart() {
		return helpSearchViewPart;
	}

	public void setHelpSearchViewPart(IViewPart helpSearchViewPart) {
		this.helpSearchViewPart = helpSearchViewPart;
	}

	public FDUHelpSeekingPlugin() {
		INSTANCE = this;
	}

	
	protected IWindowListener WINDOW_LISTENER = new IWindowListener() {

		@Override
		public void windowOpened(IWorkbenchWindow window) {
			if (getWorkbench().isClosing()) {
				return;
			}
			if (window instanceof IMonitoredWindow) {
				IMonitoredWindow awareWindow = (IMonitoredWindow) window;
				if (!awareWindow.isMonitored()) {
					return;
				}
			}
			addListenersToWindow(window);
		}

		@Override
		public void windowDeactivated(IWorkbenchWindow window) {

		}

		@Override
		public void windowClosed(IWorkbenchWindow window) {
			removeListenersFromWindow(window);
			if (window == launchingWorkbenchWindow) {

			}
		}

		@Override
		public void windowActivated(IWorkbenchWindow window) {

		}
	};

	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.activityContextManager = new ActivityContextManager(
				new ArrayList<AbstractUserActivityMonitor>(0));
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				init();
			}
		});
	}

	private void init() {
		try {
			getWorkbench().addWindowListener(WINDOW_LISTENER);

			IWorkbenchWindow[] windows = getWorkbench().getWorkbenchWindows();
			if (windows.length > 0) {
				launchingWorkbenchWindow = windows[0];
			}
			for (IWorkbenchWindow window : windows) {
				addListenersToWindow(window);
			}
			
			//find db preferencepage value
			
			
		
			IPreferenceStore ps=FDUHelpSeekingPlugin.getDefault().getPreferenceStore();
			String dbURL=ps.getString(PreferenceConstants.URL_KEY);
			String dbname=ps.getString(PreferenceConstants.USERNAME_KEY);
			String dbpwd=ps.getString(PreferenceConstants.PASSWORD_KEY);
			
			INIHelper mycseini=new INIHelper("/cse.ini",true);
			
			List<CxKeyPair> myCxKeyPairs=mycseini.getCxKeyPairs();
			ps.setValue(PreferenceConstants.CSE_CX, myCxKeyPairs.get(0).getCx());
			ps.setValue(PreferenceConstants.CSE_KEY, myCxKeyPairs.get(0).getKey());


			
		
			int connection=	DatabaseUtil.initDB(dbURL,dbname,dbpwd);
			
			if (connection<0) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "HELPSEEKING DB PARAMETER ERROR! ", "Please configurate it in Window -> Preference -> FDUHelpSeeking Configuration!" );
				return;
			}
				
			MonitorUiExtensionPointReader.initExtensions(monitors);
			monitors.add(new ElementChangedListener());
			monitors.add(new WorkbenchUserActivityMonitor());
			monitors.add(new BreakpointListener());
			monitors.add(new SelectionListener());
			monitors.add(new ExecutionListener());
			monitors.add(new JobChangeListener());
			monitors.add(new ProblemChangedListener());
			monitors.add(new DebugEventSetListener());
			monitors.add(new WindowListener());
			monitors.add(new WorkbenchListener());
			monitors.add(new PerspectiveListener());
			monitors.add(new PageListener());
			monitors.add(new PartListener());
			monitors.add(new ResourceChangeListener());
			monitors.add(new PropertyChangeListener());
			monitors.add(new TestRunListener());
			monitors.add(new SVNConsoleListener());
			monitors.add(new CVSConsoleListener());
			monitors.add(new LaunchListener());
			monitors.add(new RefactoringExecutionListener());
			monitors.add(new RefactoringHistoryListener());
			activityContextManager.init(monitors);
			updateActivityTimeout();
			activityContextManager.start();
			
			helpSearchViewPart = FDUHelpSeekingPlugin
					.getDefault()
					.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage()
					.findView(
							"cn.edu.fudan.se.helpseeking.views.HelpSeekingSearchView");
		} catch (Exception e) {
			StatusHandler.log(new Status(IStatus.ERROR,
					FDUHelpSeekingPlugin.PLUGIN_ID, "Monitor UI start failed",
					e));
		}
	}

	private void updateActivityTimeout() {
		activityContextManager.setInactivityTimeout(0);
	}

	public void stop(BundleContext context) throws Exception {
		
//IWorkbenchPage myeditors=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
//		for (int i = 0; i < myeditors.length; i++) {
//			IEditorReference[] myer=myeditors.getEditorReferences();
//			for (int k = 0; k < myer.length; k++) {
//				IEditorPart myiEditorParts=myer[k].getEditor(true);
//				if (myiEditorParts.getClass().toString().trim().equals("cn.edu.fudan.se.helpseeking.editors.BrowserEditor")) 
//				{
//					myiEditorParts.dispose();			
//				}
//
//			}
			
//					}

		IEditorReference[]	ieReferences=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		for (int i = 0; i < ieReferences.length; i++) {
			IEditorPart iePart=ieReferences[i].getEditor(true);
			if (iePart.getClass().toString().trim().equals("cn.edu.fudan.se.helpseeking.editors.BrowserEditor")) 
			{
				iePart.dispose();			
			}
		}
		
			
		super.stop(context);
		try {
			if (activityContextManager != null) {
				activityContextManager.stop();
			}
			if (Platform.isRunning()) {
				if (getWorkbench() != null && !getWorkbench().isClosing()) {
					getWorkbench().removeWindowListener(WINDOW_LISTENER);
					for (IWorkbenchWindow window : monitoredWindows) {
						removeListenersFromWindow(window);
					}
				}
			}
			
			
		} catch (Exception e) {
			StatusHandler
					.log(new Status(IStatus.ERROR,
							FDUHelpSeekingPlugin.PLUGIN_ID,
							"Monitor UI stop failed", e)); //$NON-NLS-1$
		}
		
		DatabaseUtil.closeAll();
		
		
		
		INSTANCE = null;
		
	}

	public static FDUHelpSeekingPlugin getDefault() {
		return INSTANCE;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private void removeListenersFromWindow(IWorkbenchWindow window) {
		for (IPageListener listener : pageListeners) {
			window.removePageListener(listener);
		}
		for (IPartListener listener : partListeners) {
			window.getPartService().removePartListener(listener);
		}
		for (IPerspectiveListener listener : perspectiveListeners) {
			window.removePerspectiveListener(listener);
		}
		for (ISelectionListener listener : postSelectionListeners) {
			window.getSelectionService().removeSelectionListener(listener);
		}
		monitoredWindows.remove(window);
	}

	private void addListenersToWindow(IWorkbenchWindow window) {
		for (IPageListener listener : pageListeners) {
			window.addPageListener(listener);
		}
		for (IPartListener listener : partListeners) {
			window.getPartService().addPartListener(listener);
		}
		for (IPerspectiveListener listener : perspectiveListeners) {
			window.addPerspectiveListener(listener);
		}
		for (ISelectionListener listener : postSelectionListeners) {
			window.getSelectionService().addPostSelectionListener(listener);
		}

		monitoredWindows.add(window);
	}

	static class MonitorUiExtensionPointReader {

		private static final String EXTENSION_ID_USER = "user"; //$NON-NLS-1$

		private static final String ELEMENT_ACTIVITY_TIMER = "osActivityTimer"; //$NON-NLS-1$

		private static boolean extensionsRead = false;

		private static void initExtensions(
				Collection<AbstractUserActivityMonitor> monitors) {
			if (!extensionsRead) {
				ExtensionPointReader<AbstractUserActivityMonitor> reader = new ExtensionPointReader<AbstractUserActivityMonitor>(
						PLUGIN_ID, EXTENSION_ID_USER, ELEMENT_ACTIVITY_TIMER,
						AbstractUserActivityMonitor.class);
				reader.read();
				List<AbstractUserActivityMonitor> items = reader.getItems();
				Collections.reverse(items);
				// TODO set id for monitor to identify instance
				monitors.addAll(items);

				extensionsRead = true;
			}
		}
	}

	

}
