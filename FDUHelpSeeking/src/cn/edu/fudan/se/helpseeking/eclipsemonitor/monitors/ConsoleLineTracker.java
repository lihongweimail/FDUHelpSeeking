package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.debug.ui.console.IConsoleLineTracker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import cn.edu.fudan.se.helpseeking.bean.MessageCollector;

public class ConsoleLineTracker extends AbstractUserActivityMonitor implements
		IConsoleLineTracker {
	private IConsole console;

	@Override
	public void init(IConsole console) {
		this.console = console;
	}

	@Override
	public void lineAppended(IRegion line) {
		try {
			int length = line.getLength();
			int offset = line.getOffset();

			String text;

			text = console.getDocument().get(offset, length);

//			//这里实现了从console view中获得新增行的内容
//			System.out.println(text);
//			System.out.println("split line ---------------------------------------------------");
//			System.out.println(console.getDocument().get());
//			System.out.println("split line ==================================");
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	
	public String getCurrentConsoleContent()
	{
	    
		String text;
	    IDocument id = console.getDocument();
		text = id.get();
//	    System.out.println(id.get());
	   return text;
		
	}

}
