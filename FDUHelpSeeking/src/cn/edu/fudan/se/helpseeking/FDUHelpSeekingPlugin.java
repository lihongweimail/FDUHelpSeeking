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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

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
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PageListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PartListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PerspectiveListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ProblemChangedListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.PropertyChangeListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.ResourceChangeListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.SVNConsoleListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.SelectionListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.TestRunListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WindowListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WorkbenchListener;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors.WorkbenchUserActivityMonitor;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class FDUHelpSeekingPlugin extends AbstractUIPlugin {

	private static FDUHelpSeekingPlugin INSTANCE;
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
			DatabaseUtil.init();
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
			activityContextManager.init(monitors);
			updateActivityTimeout();
			activityContextManager.start();
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
