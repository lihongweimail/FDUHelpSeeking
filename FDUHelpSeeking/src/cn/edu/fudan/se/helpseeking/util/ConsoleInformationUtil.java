package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformation;
import cn.edu.fudan.se.helpseeking.bean.ConsoleInformationList;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;

public class ConsoleInformationUtil {
	
	public static RuntimeInformation SelectConsoleInformationByBreakpont(Breakpoint bp) {
		RuntimeInformation runtimeInformation = new RuntimeInformation();
		ArrayList<ConsoleInformation> list = ConsoleInformationList.getInstance()
				.getExceptionList();
		boolean found = false;
		
		for(ConsoleInformation consoleInformation : list){
			if(!found){
				HashMap<String, ArrayList<Integer>> locations = consoleInformation.getLocations();
				Set<Entry<String, ArrayList<Integer>>> entryset = locations.entrySet();
				for(Entry<String, ArrayList<Integer>> entry : entryset){
					if(!found){
						String fileName = entry.getKey(); 
						int index = fileName.lastIndexOf(".");
						String suffix = fileName.substring(index);
						fileName = fileName.substring(0, index).replace(".", "/") + suffix;
						if(bp.getFileName().contains(fileName)){
							for(Integer linenumber : entry.getValue()){
								int differ = linenumber - bp.getLineNo();
								if(differ > -5 && differ < 5){
									generateRuntimeInformationbyConsoleInformation(
										runtimeInformation, consoleInformation);
									found = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		if(!found && list.size() > 0){
			generateRuntimeInformationbyConsoleInformation(runtimeInformation, list.get(0));
		}
				
		return runtimeInformation;
	}
	
	private static void generateRuntimeInformationbyConsoleInformation(
			RuntimeInformation runtimeInformation, ConsoleInformation consoleInformation) {
		runtimeInformation.setExceptionName(consoleInformation.getExceptionName());
		runtimeInformation.setContent(consoleInformation.getDescription());
		runtimeInformation.setType(RuntimeInfoType.ExceptionalMessage);
	}

}
