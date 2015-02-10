package cn.edu.fudan.se.helpseeking.editors;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import swing2swt.layout.BorderLayout;
import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.BrowserIDBean;
import cn.edu.fudan.se.helpseeking.views.BrowserEditorView;
import cn.edu.fudan.se.helpseeking.web.AmAssitBrowser;

public class BrowserEditor extends EditorPart {
	
	private Text text;
	boolean isDirty = false;
	private String currentPath = null;
    
	AmAssitBrowser mybroBrowser= new AmAssitBrowser();
	
	

	public AmAssitBrowser getMybroBrowser() {
		return mybroBrowser;
	}

	public void setMybroBrowser(AmAssitBrowser mybroBrowser) {
		this.mybroBrowser = mybroBrowser;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// your work here
		
		if (currentPath == null)
			doSaveAs();
		else
			saveFile(new File(currentPath));

	}

	@Override
	public void doSaveAs() {
		// your work here
		FileDialog fd = new FileDialog(this.getSite().getShell());
		String path = fd.open();
		path += ".txt";
		currentPath = path;
		saveFile(new File(path));

	}

	private void saveFile(File f) {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					f)));
			pw.print(text.getText());
			pw.flush();
			pw.close();
			System.out.println("save to:" + f.getAbsolutePath());
			isDirty = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void init(IEditorSite arg0, IEditorInput arg1)
			throws PartInitException {
		// TODO Auto-generated method stub
		setSite(arg0);
        setInput(arg1);
	}

	@Override
	public boolean isDirty() {
		// your work here
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// your work here
		
	
		return true;
	}

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
		
//		
		Composite composite = new Composite(arg0, SWT.NONE); 
        GridLayout layout = new GridLayout(1, true); 
        composite.setLayout(layout); 
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
		   mybroBrowser=new AmAssitBrowser(composite);

		   //mybroBrowser.setMyComposite(topComp);
		   
		   mybroBrowser.setDisplay(composite.getDisplay());
		   mybroBrowser.createShow();
		   mybroBrowser.getBrowser().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,7, 1));				
			
		   
		   mybroBrowser.setDisableButton(true);
		   
//		   mybroBrowser.setNewUrl("about:blank");
		   StringBuffer buffer=new StringBuffer();
		   
		   buffer.append("<!DOCTYPE html>\r\n");
		   buffer.append("<html lang=\"en\">\r\n");
           buffer.append(" <head> \r\n");
		   buffer.append("<title>Loading webpage ... </title>\r\n"); 
		   buffer.append("<meta charset=\"utf-8\" /> \r\n");
		buffer.append("</head> \r\n");
		buffer.append("<body class=\"\" id=\"body_solstice\" >\r\n"); 
		buffer.append("      <h2>   Loading WebPage ...  </h2>\r\n");
		buffer.append("      <h3>   Please waiting ...   </h3>\r\n");
		buffer.append("    </body>\r\n");
		buffer.append("   </html>\r\n");
		   
//		   mybroBrowser.getBrowser().setText(buffer.toString());
		   mybroBrowser.refreshBrowser();
           mybroBrowser.getBrowser().getParent().pack();
		   mybroBrowser.refreshBrowser();
		   mybroBrowser.getMyComposite().pack();




		
		
//		this.text = new Text(topComp, SWT.BORDER);
//		text.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				isDirty = true;
//				BrowserEditorView.getSingleTon().setText("text length:" + text.getText().length());
//			}
//		});
//		text.addKeyListener(new KeyListener() {
//
//			@Override
//			public void keyReleased(KeyEvent event) {
//				if (event.stateMask == (SWT.CTRL) && event.keyCode == 's') {
//					if (isDirty())
//						doSave(null);
//				}
//			}
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//			}
//		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
