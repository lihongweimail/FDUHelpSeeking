package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.debug.ui.console.IConsoleLineTracker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import cn.edu.fudan.se.helpseeking.bean.ConsoleInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;

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

			String text = console.getDocument().get(offset, length);
			
			ConsoleInformationList consoleInformationList = ConsoleInformationList
					.getInstance();
			ArrayList<ConsoleInformation> exceptionList = consoleInformationList
					.getExceptionList();
			int size = exceptionList.size();
			ConsoleInformation lastInformation = null;
			if(size > 0){
				lastInformation = exceptionList.get(size - 1); 
			}
			
			if (size > 0 && text.startsWith("\tat ")) {
				String location = text.substring(4);
				String[] split = location.split("\\(");
				int index1 = split[1].indexOf(":");
				String filename = split[1].substring(0, index1);
				String file = filename.substring(0, filename.indexOf("."));
				String fileName = split[0].substring(0, split[0].indexOf(file)) + filename;
				int index2 = split[1].indexOf(")");
				int lineNumber = Integer.parseInt(split[1].substring(index1 + 1, index2));
				lastInformation.addLocation(fileName, lineNumber);
				//lastInformation.addLocation(text.substring(4));
			}else{
				Pattern pattern = Pattern.compile("[a-zA-Z].[A-Za-z]*Exception");
				Matcher matcher = pattern.matcher(text);
				if(matcher.find()){
					if(size > 0 && (lastInformation.getLocations() == null 
							|| lastInformation.getLocations().size() == 0)){
						exceptionList.remove(size - 1);
					}
					ConsoleInformation information = new ConsoleInformation();
					int index1 = text.lastIndexOf(" ");
					int index2 = text.indexOf(":");
					if(index2 == -1){
						information.setExceptionName(text.substring(index1 + 1));
					}else{
						index1 = text.substring(0, index2).lastIndexOf(" ");
						information.setExceptionName(text.substring(index1 + 1, index2));
						information.setDescription(text.substring(index2 + 2));
					}
					exceptionList.add(information);
				}else if(size > 0 && (lastInformation.getLocations() == null 
						|| lastInformation.getLocations().size() == 0)){
					exceptionList.remove(size - 1);
				}
			}
			
			//consoleInformationList.prettyPrint();

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
		//System.out.println(id.get());
	   return text;
		
	}

}
