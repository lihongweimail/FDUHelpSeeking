package cn.edu.fudan.se.helpseeking.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class OleDocTest {
	
	public static void main(String[] args) {

		
		
		Display display = new Display();
		Shell shell = new Shell(display);
		OleFrame frame = new OleFrame(shell, SWT.NONE);
		OleDocTest thisClass= new OleDocTest();
		
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem1 = new MenuItem(bar, SWT.CASCADE);
		fileItem1.setText("&File_Item_1");
		MenuItem fileItem2 = new MenuItem(bar, SWT.CASCADE);
		fileItem2.setText("&File_Item_2");
		MenuItem containerItem = new MenuItem(bar, SWT.CASCADE);
		containerItem.setText("&Container_Item");
		MenuItem windowItem1 = new MenuItem(bar, SWT.CASCADE);
		windowItem1.setText("&Window_Item_1");
		MenuItem windowItem2 = new MenuItem(bar, SWT.CASCADE);
		windowItem2.setText("&Window_Item_2");
		frame.setFileMenus(new MenuItem[] {fileItem1, fileItem2});
		frame.setContainerMenus(new MenuItem[] {containerItem});
		frame.setWindowMenus(new MenuItem[] {windowItem1, windowItem2});
		
		

		shell.open();

	}

}
