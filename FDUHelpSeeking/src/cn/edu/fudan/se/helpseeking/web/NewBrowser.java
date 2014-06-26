package cn.edu.fudan.se.helpseeking.web;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class NewBrowser {
static final ArrayList urls = new ArrayList(); // @jve:decl-index=0:
static boolean canRemove = false;
static Shell shell;
static Browser browser;



public static Browser getBrowser() {
	return browser;
}

public static void setBrowser(Browser browser) {
	NewBrowser.browser = browser;
}

public static Shell getShell() {
	return shell;
}

public static void setShell(Shell shell) {
	NewBrowser.shell = shell;
}

public static void main(String[] args) {
      Display display = new Display();
      Shell shell = new Shell(display);
      shell.setText("jog36行业招聘");


      shell.setLayout(new FillLayout());
      Browser browser = new Browser(shell, SWT.NONE);
      setBrowser(browser);
      setShell(shell);
      initialize(display, browser);
      shell.open();
      browser.setUrl("news.sina.com.cn");
      while (!shell.isDisposed()) {
        if (!display.readAndDispatch())
          display.sleep();
      }
      display.dispose();
}

static void initialize(final Display display, Browser browser) {
	
	 
	
      browser.addListener(SWT.MouseDown, new Listener() {
        public void handleEvent(Event event) {
          System.out.println("event.time:" + event.time);
        }
      });
      browser.addOpenWindowListener(new OpenWindowListener() {
        public void open(WindowEvent event) {
          // Embed the new window
//          final Shell shell = new Shell(display);
          shell.setText("New Window");
          shell.setLayout(new FillLayout());
//          final Browser browser = new Browser(shell, SWT.NONE);
//          initialize(display, browser);
          event.browser = getBrowser();
          event.display.asyncExec(new Runnable() {
              public void run() {
                String url = getBrowser().getUrl();
                System.out.println(url);
                System.out.println(getBrowser().getText());
                getBrowser().setUrl(url);
               
              }
          });
        }
      });
      browser.addVisibilityWindowListener(new VisibilityWindowListener() {
        public void hide(WindowEvent event) {
          Browser browser = (Browser) event.widget;
          Shell shell = browser.getShell();
          shell.setVisible(false);
        }

        public void show(WindowEvent event) {
          Browser browser = (Browser) event.widget;
          Shell shell = browser.getShell();
          if (event.location != null)
              shell.setLocation(event.location);
          if (event.size != null) {
              Point size = event.size;
              shell.setSize(shell.computeSize(size.x, size.y));
          }
          if (event.addressBar || event.menuBar || event.statusBar
                || event.toolBar) {
              // Create widgets for the address bar, menu bar, status bar and/or tool bar
              // leave enough space in the Shell to accomodate a Browser of the size
              // given by event.size
          }
          shell.open();
        }
      });
      browser.addDisposeListener(new DisposeListener(){
        public void widgetDisposed(DisposeEvent event){
          Browser browser = (Browser) event.widget;
          if (canRemove)
              urls.remove(browser.getUrl());
          Shell shell = browser.getShell();
         // shell.close();
        }
      });
}

}
