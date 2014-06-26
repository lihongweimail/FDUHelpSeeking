package cn.edu.fudan.se.helpseeking.test;

import java.io.*;
import java.net.*;
import org.eclipse.core.runtime.*;
import org.osgi.framework.Bundle;

public class XULRunnerInitializer {
    static {
        Bundle bundle = Platform.getBundle("org.mozilla.xulrunner.win32.win32.x86"); //$NON-NLS-1$
        if (bundle != null) {
            URL resourceUrl = bundle.getResource("xulrunner"); //$NON-NLS-1$
            if (resourceUrl != null) {
                try {
                    URL fileUrl = FileLocator.toFileURL(resourceUrl);
                    File file = new File(fileUrl.toURI());
//                    System.setProperty("org.eclipse.swt.browser.XULRunnerPath",file.getAbsolutePath()); //$NON-NLS-1$
                    System.setProperty("org.eclipse.swt.browser.XULRunnerPath","D:/xulrunner64"); //$NON-NLS-1$
                } catch (IOException e) {
                    // log the exception
                } catch (URISyntaxException e) {
                    // log the exception
                }
            }
        }
    }
}