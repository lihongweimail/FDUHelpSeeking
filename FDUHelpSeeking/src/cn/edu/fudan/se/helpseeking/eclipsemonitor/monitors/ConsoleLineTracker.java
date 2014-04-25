package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import java.util.ArrayList;

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
	
	private static String lastLine = "";

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
			
			if(!text.startsWith("\tat ")){
				if(!lastLine.endsWith("\n\n")){
					lastLine += text.replaceAll("\t", "") + "\n";
				}else {
					lastLine = text;
				}
			}else{
				ConsoleInformationList consoleInformationList = ConsoleInformationList
						.getInstance();
				ArrayList<ConsoleInformation> exceptionList = consoleInformationList
						.getExceptionList();
				
				if(!lastLine.startsWith("\tat ")){
					lastLine = lastLine.replaceAll("\n", "");
					ConsoleInformation information = new ConsoleInformation();
					int index1 = lastLine.lastIndexOf(" ");
					int index2 = lastLine.indexOf(":");
					if(index2 == -1){
						information.setExceptionName(lastLine.substring(index1 + 1));
					}else{
						index1 = lastLine.substring(0, index2).lastIndexOf(" ");
						information.setExceptionName(lastLine.substring(index1 + 1, index2));
						information.setDescription(lastLine.substring(index2 + 2));
					}
					exceptionList.add(information);
				}
				
				int size = exceptionList.size();
				if(size > 0){
					ConsoleInformation lastInformation = exceptionList.get(size - 1); 
					String location = text.substring(4);
					String[] split = location.split("\\(");
					int index1 = split[1].indexOf(":");
					if(index1 != -1){
						String filename = split[1].substring(0, index1);
						String file = filename.substring(0, filename.indexOf("."));
						String fileName = split[0].substring(0, 
								split[0].indexOf(file)) + filename;
						int index2 = split[1].indexOf(")");
						int lineNumber = Integer.parseInt(split[1]
								.substring(index1 + 1, index2));
						lastInformation.addLocation(fileName, lineNumber);
					}else{
						lastInformation.addLocation("Unknown Source", 0);
					}
					
					
				}
				lastLine = text;			
			}
			
			//ConsoleInformationList.getInstance().prettyPrint();
			
			
//			//如果更新了，且是Exception信息
//			InteractionEvent e=new InteractionEvent();
//			e.setByuser(false);
//			e.setDate(Calendar.getInstance().getTime());
//			e.setKind(Kind.ATTENTION);
//			e.setActionName("Console View Changed");
//			e.setOriginId("Console View Changed");
//			
//			
//			
//			Information info=new Information();
//			info.setType("Console view changed");
//			Action action=new Action();
//			action.setTime(new Timestamp(System.currentTimeMillis()));
//			action.setByuser(false);
//			action.setActionKind(e.getKind());
//			action.setActionName(e.getActionName());
//			action.setDescription(e.getOriginId());
//			info.setDebugCode(null);
//			info.setEditCode(null);
//			info.setExplorerRelated(null);
//			info.setIdeOutput(null);
//			info.setAction(action);
//			//需要先写入数据库，才能得到ID
//			int actionid=DatabaseUtil.addInformationToDatabase(info);
//							
//				Cache.getInstance().addInformationToCache(info,actionid);
//				
//			DatabaseUtil.addInteractionEventToDatabase(e);	
			
			

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void clearLastLine() {
		lastLine = "";
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
