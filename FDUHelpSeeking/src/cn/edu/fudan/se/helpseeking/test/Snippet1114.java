package cn.edu.fudan.se.helpseeking.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Snippet1114 {

	  public static void main(String[] args) {
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    Tree tree = new Tree(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
	        | SWT.H_SCROLL);
	    for (int i = 0; i < 12; i++) {
	      TreeItem item = new TreeItem(tree, SWT.NONE);
	      item.setText("Item " + i);
	      TreeItem aItem=new TreeItem(item, SWT.None);
	      aItem.setText("Item "+i+"haha");
	    }
	    
	   
	    
	    tree.setSize(100, 100);
	    tree.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        String string = event.detail == SWT.CHECK ? "Checked"
	            : "Selected";
	        System.out.println(event.item + " " + string);
	      }
	    });
	    shell.setSize(200, 200);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	  }
	}