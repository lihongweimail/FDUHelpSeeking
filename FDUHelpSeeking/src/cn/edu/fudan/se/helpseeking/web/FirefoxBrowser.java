package cn.edu.fudan.se.helpseeking.web;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.osgi.framework.Bundle;

public class FirefoxBrowser {
//	static Browser browser;
//	public static void main(String [] args){
////		System.out.println("start");
//		Bundle bundle = Platform.getBundle("org.mozilla.xulrunner.win32.win32.x86");
//		if(bundle != null){
////			System.out.println("XULRunner Bundle found");
//			URL resourceUrl = bundle.getResource("xulrunner");
//			if(resourceUrl != null){
//				try{
//					URL fileUrl = FileLocator.toFileURL(resourceUrl);
//					File file = new File(fileUrl.toURI());
//					System.setProperty("org.eclipse.swt.browser.DefaultType", "mozilla");
//					System.setProperty("org.eclipse.swt.browser.XULRunnerPath", file.getAbsolutePath());
//				} catch(IOException e){
//					System.out.println("Error1:"+e.getMessage());
//				} catch(URISyntaxException e){
//					System.out.println("Error2:"+e.getMessage());
//				}
//			}
//		}else
//			System.out.println("Can not find XULRunner Bundle");
//		
////		Display display = new Display();
////		Shell shell = new Shell(display);
////		browser = new Browser(shell, SWT.MOZILLA);
////		GridData grid = new GridData(GridData.FILL_BOTH);
////		browser.setLayoutData(grid);
////		browser.setUrl("www.ntu.edu.sg");
//		
//		
//		Display display = new Display();
//		final Shell shell = new Shell(display);
//		FillLayout layout = new FillLayout();
//		shell.setLayout(layout);
//		try{
//			browser = new Browser(shell, SWT.MOZILLA);
//		} catch(SWTError e){
//			System.out.println("Can not initialize browser"+e.getMessage());
//			display.dispose();
//			return;
//		}
//		
//		browser.addTitleListener(new TitleListener(){
//			public void changed(TitleEvent event){
//				shell.setText(event.title);
//			}
//		});
//		
////		GridData grid = new GridData(GridData.FILL_BOTH);
////		browser.setLayoutData(grid);
//		browser.setUrl("www.ntu.edu.sg");
//		shell.open();
//		while(!shell.isDisposed()){
//			if(!display.readAndDispatch()) display.sleep();
//		}
//		
//		display.dispose();
//	}
}
