package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;

public class ConsoleInformationUtil {
	
	public static Information SelectConsoleInformationByBreakpont(Breakpoint bp) {
		Information information = new Information();
		
		information.setType("IDEOutput");
		Action action = new Action();
		action.setByuser(false);
		information.setAction(action);
		
		ArrayList<ConsoleInformation> list = ConsoleInformationList.getInstance()
				.getExceptionList();
		RuntimeInformation runtimeInformation = new RuntimeInformation();
		boolean found = false;
		
		for(ConsoleInformation consoleInformation : list){
			if(!found){
				HashMap<String, ArrayList<Integer>> locations = consoleInformation.getLocations();
				Set<Entry<String, ArrayList<Integer>>> entryset = locations.entrySet();
				for(Entry<String, ArrayList<Integer>> entry : entryset){
					String fileName = entry.getKey().replace(".", "/"); 
					if(bp.getFileName().contains(fileName)){
						for(Integer linenumber : entry.getValue()){
							int differ = linenumber - bp.getLineNo();
							if(differ > -5 && differ < 5){
								generateRuntimeInformationbyConsoleInformation(
										runtimeInformation, consoleInformation);
								found = true;
							}
						}
					}
				}
			}
		}
		if(!found && list.size() > 0){
			generateRuntimeInformationbyConsoleInformation(runtimeInformation, list.get(0));
		}
		
		
		IDEOutput ideOutput = new IDEOutput();
		ideOutput.setRuntimeInformation(runtimeInformation);
		information.setIdeOutput(ideOutput);
		
		return information;
	}
	
	private static void generateRuntimeInformationbyConsoleInformation(
			RuntimeInformation runtimeInformation, ConsoleInformation consoleInformation) {
		runtimeInformation.setExceptionName(consoleInformation.getExceptionName());
		runtimeInformation.setContent(consoleInformation.getDescription());
		runtimeInformation.setType(RuntimeInfoType.ExceptionalMessage);
	}

}
