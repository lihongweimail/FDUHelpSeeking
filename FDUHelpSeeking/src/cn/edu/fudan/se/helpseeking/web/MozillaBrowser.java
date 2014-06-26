package cn.edu.fudan.se.helpseeking.web;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;


public class MozillaBrowser {
	
//	public static void main (String [] args) {
//		Display display = new Display ();
//		final Shell shell = new Shell (display);
//		shell.setLayout (new FillLayout ());
//		final Browser browser;
//		try {
//			browser = new Browser (shell, SWT.MOZILLA);
//		} catch (SWTError e) {
//			System.out.println ("Could not instantiate Browser: " + e.getMessage ());
//			display.dispose();
//			return;
//		}
//		browser.addProgressListener (new ProgressAdapter () {
//			@Override
//			public void completed (ProgressEvent event) {
//				nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser ();
//				nsIDOMWindow domWindow = webBrowser.getContentDOMWindow ();
//				nsIDOMEventTarget target = (nsIDOMEventTarget)domWindow.queryInterface (nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
//				nsIDOMEventListener listener = new nsIDOMEventListener () {
//					public nsISupports queryInterface (String uuid) {
//						if (uuid.equals (nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID) ||
//							uuid.equals (nsIDOMEventListener.NS_ISUPPORTS_IID)) {
//								return this;
//						}
//						return null;
//					}
//					public void handleEvent (nsIDOMEvent event) {
//						nsIDOMElement element = (nsIDOMElement)event.getTarget ().queryInterface (nsIDOMElement.NS_IDOMELEMENT_IID);
//						Menu menu = new Menu (browser);
//						MenuItem item = new MenuItem (menu, SWT.NONE);
//						item.setText ("custom menu for element with tag: " + element.getTagName ());
//						nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent)event.queryInterface (nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
//						menu.setLocation (mouseEvent.getScreenX (), mouseEvent.getScreenY ());
//						menu.setVisible (true);
//					}
//				};
//				target.addEventListener ("contextmenu", listener, false);
//			}
//		});
//		shell.open ();
//		browser.setUrl ("http://www.eclipse.org");
//		while (!shell.isDisposed ()) {
//			if (!display.readAndDispatch ()) display.sleep ();
//		}
//		display.dispose ();
//	}
//	

}
