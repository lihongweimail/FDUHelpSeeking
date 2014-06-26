package cn.edu.fudan.se.helpseeking.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.osgi.framework.Bundle;

public class TestMozillaBrowser {

    public static void main(String[] args) {
    	
    
        Bundle bundle = Platform.getBundle("org.mozilla.xulrunner.win32.win32.x86"); //$NON-NLS-1$
        if (bundle != null) {
            URL resourceUrl = bundle.getResource("xulrunner"); //$NON-NLS-1$
            if (resourceUrl != null) {
                try {
                    URL fileUrl = FileLocator.toFileURL(resourceUrl);
                    File file = new File(fileUrl.toURI());
//                    System.setProperty("org.eclipse.swt.browser.XULRunnerPath",file.getAbsolutePath()); //$NON-NLS-1$
                    System.setProperty("org.eclipse.swt.browser.XULRunnerPath","D:/xulrunner"); //$NON-NLS-1$
                } catch (IOException e) {
                    // log the exception
                } catch (URISyntaxException e) {
                    // log the exception
                }
            }
        }
    	
        Device.DEBUG = true;
        Display display = new Display();
        Shell shell = new Shell(display);
        System.out.println(">>>Snippet creating SWT.MOZILLA-style Browser");
        try {
            new Browser(shell, SWT.MOZILLA);
            System.out.println(">>>succeeded");
        } catch (Error e) {
            System.out.println(">>>This failed with the following error:");
            e.printStackTrace();
            System.out.println("\n\nSnippet creating SWT.NONE-style Browser");
            try {
                new Browser(shell, SWT.NONE);
                System.out.println(">>>succeeded");
            } catch (Error e2) {
                System.out.println(">>>This failed too, with the following error:");
                e2.printStackTrace();
            }
        }
        display.dispose();
    }
}

